package tralleno.Vues;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import tralleno.Modele.Sujet;
import tralleno.Taches.Tache;

import java.util.List;

public class VueSection extends VBox implements Observateur {

    public VueSection(String nom, List<Tache> taches) {
        super();
        setPrefWidth(250);
        setMinHeight(50);
        setSpacing(10);
        setPadding(new Insets(10));
        setStyle("-fx-background-color: lightgray; -fx-border-color: darkgray; -fx-border-width: 1px;");

        Label labelSection = new Label(nom);
        labelSection.setStyle("-fx-font-weight: bold;");

        getChildren().add(labelSection);

        for (Tache t : taches) {
            VueTache vueTache = new VueTache(t);
            getChildren().add(vueTache);
        }
    }

    @Override
    public void actualiser(Sujet s) {

    }
}
