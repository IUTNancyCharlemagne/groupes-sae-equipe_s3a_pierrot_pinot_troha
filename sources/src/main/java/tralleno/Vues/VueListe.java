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

public class VueListe extends ScrollPane implements Observateur, Serializable {
    private final ModeleBureau modeleBureau;

    private transient VBox sectionsVBox;

    public VueListe(ModeleBureau modeleBureau) {
        super();
        this.modeleBureau = modeleBureau;
        this.getStyleClass().add("vueListe");
        actualiser(modeleBureau);
    }

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


            // Pour pouvoir glisser déposer les sections contenues dans la Vbox
            sectionsVBox.setOnDragOver(event -> {
                if(event.getGestureSource() instanceof VueTache){
                    event.acceptTransferModes(TransferMode.NONE);
                    event.consume();
                }
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
     * Méthode qui détermine l'index à laquelle la section est drop
     * @param event
     * @return
     */
    private int determinePositionInsertion(DragEvent event) {
        double mouseY = event.getY();
        ObservableList<Node> sectionsVisuelles = sectionsVBox.getChildren();
        int index = -1;

        for (int i = 0; i < sectionsVisuelles.size(); i++) {
            Node sectionVisuelle = sectionsVisuelles.get(i);
            double sectionY = sectionVisuelle.getBoundsInParent().getMinY();

            if (mouseY < sectionY) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            index = sectionsVisuelles.size();
        }

        return index;
    }


}

