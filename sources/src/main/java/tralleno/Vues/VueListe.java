package tralleno.Vues;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import tralleno.Modele.ModeleBureau;
import tralleno.Modele.Sujet;
import tralleno.Section.Section;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.TitledPane;
import javafx.scene.control.ScrollPane;

/**
 * Classe qui représente les tâches et sections contenues dans le modèle
 */
public class VueListe extends ScrollPane implements Observateur, Serializable {
    /**
     * Modèle qui contient les données de l'application
     */
    private final ModeleBureau modeleBureau;

    /**
     * VBox non sérialisable qui va contenir les sections (qui elles sont des TitledPane)
     */
    private transient VBox sectionsVBox;

    /**
     * Crée une vue liste uniquement à partir du modèle de l'application
     * @param modeleBureau
     */
    public VueListe(ModeleBureau modeleBureau) {
        super();
        this.modeleBureau = modeleBureau;
        this.getStyleClass().add("vueListe");
        actualiser(modeleBureau);
    }

    /**
     * Lorsque la méthode est déclenchée elle construit dans son conteneur de sections des VueSection
     * qui elles, à leur tour vont construire des VueTache
     * Ajoute aussi les listeners pour le drag and drop dans la zone qui contient les sections pour permettre
     * le drag and drop de sections
     * @param s
     */
    public void actualiser(Sujet s) {
        if (s instanceof ModeleBureau) {
            setContent(null);

            sectionsVBox = new VBox();
            sectionsVBox.getStyleClass().add("containerSectionsListe");
            sectionsVBox.setSpacing(20);
            sectionsVBox.setPadding(new Insets(20));
            sectionsVBox.setFillWidth(true); // Permettre à la VBox de remplir toute la largeur disponible

            // Liaison bidirectionnelle des propriétés prefWidth du ScrollPane et de la VBox
            sectionsVBox.prefWidthProperty().bind(this.widthProperty());
            List<Section> sections = ((ModeleBureau) s).getSections();

            for (Section section : sections) {
                VueSectionListe vueSectionListe = new VueSectionListe(this.modeleBureau, section);
                sectionsVBox.getChildren().add(vueSectionListe);
            }

            setContent(sectionsVBox);

            sectionsVBox.setOnDragOver(event -> {
                // On vérifie qu'on puisse pas mettre de tâche en dehors d'une VueSection car là on
                // est dans la VueListe donc dans la Vbox ou carrément le scrollpane
                if (event.getGestureSource() instanceof VueTache) {
                    event.acceptTransferModes(TransferMode.NONE);
                    event.consume();
                } // Les VueSection étant aussi des TitledPane il fallait faire la vérification de la tâche avant pour pas qu'elle puisse être acceptée
                // Si c'est un TitledPane c'est que c'est une VueSection donc on autorise le drop
                if (event.getGestureSource() instanceof TitledPane) {
                    event.acceptTransferModes(TransferMode.MOVE);
                    event.consume();
                }
            });

            // setOnDragDrop pour le déplacement des sections
            sectionsVBox.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                boolean success = false;

                if (db.hasString()) {
                    Section section = this.modeleBureau.getSectionParId(Integer.valueOf(db.getString()));
                    this.modeleBureau.setSectionCourante(section);
                    int targetIndex = determinePositionInsertion(event);
                    modeleBureau.deplacerSection(targetIndex);
                    success = true;
                }

                event.setDropCompleted(success);
                event.consume();
            });

        }
    }

    /**
     * Méthode qui détermine l'index à laquelle la section dragged est dropped
     *
     * @param event
     * @return
     */
    private int determinePositionInsertion(DragEvent event) {
        // On récupère la position de la souris au moment où la méthode est appelée
        double mouseY = event.getY();

        // On a besoin des VueSection pour savoir laquelle est à quel indice de la VBox
        ObservableList<Node> sectionsVisuelles = sectionsVBox.getChildren();

        // Au cas où mauvaise position
        int index = -1;

        for (int i = 0; i < sectionsVisuelles.size(); i++) {
            Node sectionVisuelle = sectionsVisuelles.get(i);

            // On récupère la position en ordonnée de la VueSection
            double sectionY = sectionVisuelle.getBoundsInParent().getMinY();

            // Et si, en ordonnée la souris est en dessous de la section, alors l'index c'est celui de la section et
            // quand on utilisera l'index ça sera celui de la section actuelle
            if (mouseY < sectionY) {
                index = i;
                break;
            }
        }

        // Et si la position est mauvaise on prend la taille de la section et
        // on ajoutera la section à la fin
        if (index == -1) {
            index = sectionsVisuelles.size();
        }

        return index;
    }
}

