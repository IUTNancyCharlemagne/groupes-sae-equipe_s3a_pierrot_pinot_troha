//package tralleno.Vues;
//
//import javafx.geometry.Insets;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.HBox;
//import tralleno.Controleurs.Sections.ControlModifSection;
//import tralleno.Modele.ModeleBureau;
//import tralleno.Modele.Sujet;
//import tralleno.Section.Section;
//import tralleno.Taches.Tache;
//
//import java.io.Serializable;
//import java.util.List;
//
///**
// * Vue des tâches sous forme de tableau kanban. Les sections sont ordonnées par colonnes et comportent des tâches
// */
//public class VueTableau extends HBox implements Observateur, Serializable {
//        private final ModeleBureau modeleBureau;
//
//    public VueTableau(ModeleBureau modeleBureau) {
//        super(20);
//        setPadding(new Insets(10));
//        this.modeleBureau = modeleBureau;
//
//        actualiser(modeleBureau);
//    }
//
//    @Override
//    public void actualiser(Sujet s) {
//        if (s instanceof ModeleBureau) {
//            getChildren().clear(); // On efface les vues pour le momeent
//
//            List<Section> sections = ((ModeleBureau) s).getSections();
//
//            for (Section section : sections) {
//                List<Tache> taches = section.getTaches();
//                VueSection vueSection = new VueSection(section, taches, this.modeleBureau);
//                getChildren().add(vueSection);
//            }
//        }
//    }
//}

package tralleno.Vues;

import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import tralleno.Controleurs.Sections.ControlModifSection;
import tralleno.Modele.ModeleBureau;
import tralleno.Modele.Sujet;
import tralleno.Section.Section;
import tralleno.Taches.Tache;

import java.io.Serializable;
import java.util.List;

/**
 * Vue des tâches sous forme de tableau kanban. Les sections sont ordonnées par colonnes et comportent des tâches
 */
public class VueTableau extends ScrollPane implements Observateur, Serializable {
    private final ModeleBureau modeleBureau;

    public VueTableau(ModeleBureau modeleBureau) {
        super();
        setPadding(new Insets(10));
        this.modeleBureau = modeleBureau;
        setStyle("-fx-background-color: #add8e6;");

        actualiser(modeleBureau);
    }

    public void actualiser(Sujet s) {
        if (s instanceof ModeleBureau) {
            setContent(null); // On efface le contenu existant

            HBox containerSections = new HBox(20);
            containerSections.setPadding(new Insets(10));

            List<Section> sections = ((ModeleBureau) s).getSections();

            for (Section section : sections) {
                List<Tache> taches = section.getTaches();
                VueSection vueSection = new VueSection(section, taches, this.modeleBureau);

                // On crée un Pane car sinon tous les enfants de Hbox (donc toutes les sections) adaptent leur hauteur à celle de la section la plus haute
                // Là, cela restera dynamique dans le pane et selon leur nombre de tâches, les sections auront une hauteur différente
                Pane sectionPane = new Pane();
                sectionPane.getChildren().add(vueSection);
                sectionPane.setMaxHeight(Region.USE_PREF_SIZE);

                containerSections.getChildren().add(sectionPane);
            }

            setContent(containerSections);
        }
    }
}