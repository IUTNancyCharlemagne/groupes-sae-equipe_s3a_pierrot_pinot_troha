package tralleno.Vues;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import tralleno.Controleurs.Sections.ControlModifSection;
import tralleno.Controleurs.Taches.ControlModifTache;
import tralleno.Modele.ModeleBureau;
import tralleno.Modele.Sujet;
import tralleno.Section.Section;
import tralleno.Taches.Tache;

import java.util.List;

public class VueSection extends VBox implements Observateur {

    public VueSection(Section section, List<Tache> taches, ModeleBureau modele) {
        super();
        setPrefWidth(250);
        setMinHeight(50);
        setSpacing(10);
        setPadding(new Insets(10));
        setStyle("-fx-background-color: lightgray; -fx-border-color: darkgray; -fx-border-width: 1px;");

        String nom = section.getNom();

        String nomAbrege = nom.length() > 30 ? nom.substring(0, 30) + "..." : nom;
        Label labelSection = new Label(nom);
        labelSection.setStyle("-fx-font-weight: bold;");

        // On crée un élément graphique juste pour que le nom de la section soit cliquable
        VBox sect = new VBox();
        sect.setPadding(new Insets(5));

        sect.getChildren().add(labelSection);

        sect.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlModifSection(modele, section));

        getChildren().add(sect);

        for (Tache t : taches) {
            VueTache vueTache = new VueTache(t);
            vueTache.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlModifTache(modele, t)); // On ajoute un contrôleur à chaque tache créée.
            getChildren().add(vueTache);
        }
    }

    @Override
    public void actualiser(Sujet s) {

    }
}
