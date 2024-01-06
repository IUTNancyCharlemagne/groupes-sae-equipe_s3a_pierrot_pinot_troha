package tralleno.Vues;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
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
    /**
     * Modèle qui contient toutes les données de l'application
     */
    private final ModeleBureau modeleBureau;

    /**
     * Construit une VueTableau à partir du modèle passé en paramètres
     * @param modeleBureau
     */
    public VueTableau(ModeleBureau modeleBureau) {
        super();
        //setPadding(new Insets(10));
        this.modeleBureau = modeleBureau;
        getStyleClass().add("vueTableau");

        actualiser(modeleBureau);
    }

    /**
     * Déclenchée lorsque l'état du modèle est modifiée
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
        }
    }
}