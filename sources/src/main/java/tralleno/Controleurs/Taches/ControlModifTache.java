package tralleno.Controleurs.Taches;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tralleno.Modele.ModeleBureau;
import tralleno.Section.Section;
import tralleno.Taches.Tache;
import tralleno.Taches.TacheFille;

import java.time.LocalDate;
import java.util.List;

/**
 * Contrôleur quand on clique sur une tâche, qui va ouvrir un menu de modification
 */
public class ControlModifTache implements EventHandler<MouseEvent> {

    private ModeleBureau modeleBureau;

    private Tache tache;

    public ControlModifTache(ModeleBureau modeleBureau, Tache t){
        this.modeleBureau = modeleBureau;
        this.tache = t;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        // On crée un nouveau stage (fenêtre)
        Stage fenetreModificationTache = new Stage();
        fenetreModificationTache.initModality(Modality.APPLICATION_MODAL);
        fenetreModificationTache.setTitle("Modifier Tâche");




        // On crée la nouvelle scène et on lui ajoute le formulaire
//        Scene scene = new Scene(layout, 400, 425);
//        fenetreModificationTache.setScene(scene);
        fenetreModificationTache.showAndWait();
    }
}
