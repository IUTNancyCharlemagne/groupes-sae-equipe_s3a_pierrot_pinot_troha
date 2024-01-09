package tralleno.Controleurs.Taches;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tralleno.Modele.ModeleBureau;
import tralleno.Section.Section;
import tralleno.Taches.Tache;
import tralleno.Taches.TacheMere;
import tralleno.Vues.VuePrincipale;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Classe qui gère la création d'une tâche lorsque l'utilisateur clique sur le bouton pour
 */
public class ControlCreerTache implements EventHandler<MouseEvent>, Serializable {

    /**
     * Modèle qui comporte les données de l'application
     */
    private ModeleBureau modeleBureau;

    /**
     * Crée le contrôleur de création de tâche uniquement à partir du modèle
     * @param modeleBureau
     */
    public ControlCreerTache(ModeleBureau modeleBureau){
        this.modeleBureau = modeleBureau;
    }

    /**
     * Lorsque l'utilisateur clique sur un bouton créer tâche, cette méthode est appelée.
     * Elle prend en charge l'évenement en créant une tâche à partir des informations
     * que l'utilisateur choisira de renseigner
     * @param mouseEvent
     */
    @Override
    public void handle(MouseEvent mouseEvent) {
        this.modeleBureau.setTacheCourante(null);
        // On crée un nouveau stage (fenêtre)
        Stage fenetreCreationTache = new Stage();
        fenetreCreationTache.initModality(Modality.APPLICATION_MODAL);
        fenetreCreationTache.setTitle("Créer une Tâche");

        // On crée la nouvelle scène et on lui ajoute le formulaire
        Scene scene = FormulaireTache.creerFormulaireTache(this.modeleBureau, null, null, fenetreCreationTache);

        int themeApp = VuePrincipale.getThemeCourant();
        switch (themeApp){
            case 1:
                scene.getStylesheets().clear();
                scene.getStylesheets().add(getClass().getResource("/tralleno/css/Base/popupStyleBase.css").toExternalForm());
                break;
            case 2:
                scene.getStylesheets().clear();
                scene.getStylesheets().add(getClass().getResource("/tralleno/css/Blue/popupStyleBlue.css").toExternalForm());
                break;
            default:
                scene.getStylesheets().clear();
                scene.getStylesheets().add(getClass().getResource("/tralleno/css/Base/popupStyleBase.css").toExternalForm());
                break;
        }

        fenetreCreationTache.setScene(scene);
        fenetreCreationTache.showAndWait();
    }
}
