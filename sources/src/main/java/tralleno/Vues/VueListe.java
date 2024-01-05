package tralleno.Vues;

import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import tralleno.Modele.Sujet;

public class VueListe extends VBox implements Observateur {

    private final ComboBox<String> choixListe;
    private final ListView<String> vueListe;

    public VueListe() {
        choixListe = new ComboBox<>();
        choixListe.getItems().addAll("Tâches Archivées", "Sections Archivées");
        choixListe.setValue("Tâches Archivées");

        vueListe = new ListView<>();
        // Initialise la vue avec les tâches archivées par défaut (à adapter selon ta logique)
        vueListe.setItems(FXCollections.observableArrayList(/* Liste des tâches archivées */));

        choixListe.setOnAction(event -> {
            if (choixListe.getValue().equals("Tâches Archivées")) {
                // Change la vue pour afficher les tâches archivées
                vueListe.setItems(FXCollections.observableArrayList(/* Liste des tâches archivées */));
            } else if (choixListe.getValue().equals("Sections Archivées")) {
                // Change la vue pour afficher les sections archivées
                vueListe.setItems(FXCollections.observableArrayList(/* Liste des sections archivées */));
            }
        });

        this.getChildren().addAll(choixListe, vueListe);
    }

    @Override
    public void actualiser(Sujet s) {

    }
}
