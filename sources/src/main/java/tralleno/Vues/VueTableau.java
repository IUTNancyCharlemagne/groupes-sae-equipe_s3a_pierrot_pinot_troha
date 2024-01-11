package tralleno.Vues;

import javafx.geometry.Insets;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import tralleno.Modele.ModeleBureau;
import tralleno.Modele.Sujet;
import tralleno.Section.Section;

import java.io.Serializable;
import java.util.List;

/**
 * Vue des tâches sous forme de tableau kanban. Les sections sont ordonnées par colonnes et comportent des tâches
 */
public class VueTableau extends ScrollPane implements Observateur, Serializable {
    /**
     * Modèle qui contient toutes les données de l'application
     */
    private final ModeleBureau modeleBureau;

    /**
     * Construit une VueTableau à partir du modèle passé en paramètres
     *
     * @param modeleBureau
     */
    public VueTableau(ModeleBureau modeleBureau) {
        super();
        this.modeleBureau = modeleBureau;
        this.getStyleClass().add("vueTableau");

        actualiser(modeleBureau);
    }

    /**
     * Déclenchée lorsque l'état du modèle est modifiée
     * Elle crée pour chaque section du modèle une VueSection et chaque VueSection est ajoutée à la HBox qui les
     * contient
     * @param s
     */
    public void actualiser(Sujet s) {

        if (s instanceof ModeleBureau) {
            setContent(null); // On efface le contenu existant

            HBox containerSections = new HBox(20);
            containerSections.setPadding(new Insets(10));
            containerSections.getStyleClass().add("containerSections");

            List<Section> sections = ((ModeleBureau) s).getSections();

            for (Section section : sections) {
                VueSection vueSection = new VueSection(section, this.modeleBureau);
                // On crée un Pane car sinon tous les enfants de Hbox (donc toutes les sections) adaptent leur hauteur à celle de la section la plus haute
                // Là, cela restera dynamique dans le pane et selon leur nombre de tâches, les sections auront une hauteur différente
                Pane sectionPane = new Pane();
                sectionPane.getChildren().add(vueSection);
                sectionPane.setMaxHeight(Region.USE_PREF_SIZE);


                containerSections.getChildren().add(sectionPane);
            }

            setContent(containerSections);

            // Pour pouvoir glisser déposer les sections contenues dans les Pane
            setOnDragOver(event -> {
                if (event.getGestureSource() instanceof Pane) { // Comme les sections sont dans les Pane parce que c'était obligé faut vérifier Pane et pas VueSection (-30 minutes...)
                    event.acceptTransferModes(TransferMode.MOVE);
                    event.consume();
                }
            });

            // setOnDragDrop pour le déplacement des sections
            setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                boolean success = false;

                if (db.hasString()) {
                    Section section = this.modeleBureau.getSectionParId(Integer.valueOf(db.getString())); // On récupère la section dragged via l'id mis dans le dragboard
                    this.modeleBureau.setSectionCourante(section);
                    int targetIndex = determinerPositionInsertion(event); // On récupère l'index auquel insérer la section
                    modeleBureau.deplacerSection(targetIndex); // Et on la déplace
                    success = true;
                }

                event.setDropCompleted(success);
                event.consume();
            });
        }


    }

    /**
     * Méthode qui détermine l'index à laquelle la section est drop
     *
     * @param event
     * @return
     */
    private int determinerPositionInsertion(DragEvent event) {
        // On récupère l'abscisse de la souris
        double mouseX = event.getX();

        List<Section> sections = modeleBureau.getSections();

        // On sait qu'une section fait 250 de largeur
        double largeurSection = 250;

        for (int i = 0; i < sections.size(); i++) {
            // On récupère l'abscisse de la section en faisant i * 250
            double sectionX = i * largeurSection;

            // Et si l'abscisse de la section est supérieure à celle de la souris
            // On a trouvé l'indice d'insertion
            if (mouseX < sectionX) {
                return i - 1;
            }
        }
        // sinon la taille
        return sections.size();
    }
}

