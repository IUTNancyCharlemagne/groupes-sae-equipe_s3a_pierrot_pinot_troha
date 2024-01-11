package tralleno.Controleurs.Taches;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tralleno.Modele.ModeleBureau;
import tralleno.Vues.VuePrincipale;

import java.io.Serializable;

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
                scene.getStylesheets().add(getClass().getResource("/tralleno/css/Brouillard/popupStyleBrouillard.css").toExternalForm());
                break;
            case 2:
                scene.getStylesheets().clear();
                scene.getStylesheets().add(getClass().getResource("/tralleno/css/Ocean/popupStyleOcean.css").toExternalForm());
                break;
            case 3:
                scene.getStylesheets().clear();
                scene.getStylesheets().add(getClass().getResource("/tralleno/css/Crepuscule/popupStyleCrepuscule.css").toExternalForm());
                break;
            case 4:
                scene.getStylesheets().clear();
                scene.getStylesheets().add(getClass().getResource("/tralleno/css/Foret/popupStyleForet.css").toExternalForm());
                break;
            case 5:
                scene.getStylesheets().clear();
                scene.getStylesheets().add(getClass().getResource("/tralleno/css/Nuit/popupStyleNuit.css").toExternalForm());
                break;
            case 6:
                scene.getStylesheets().clear();
                scene.getStylesheets().add(getClass().getResource("/tralleno/css/Plage/popupStylePlage.css").toExternalForm());
                break;
            default:
                scene.getStylesheets().clear();
                scene.getStylesheets().add(getClass().getResource("/tralleno/css/Brouillard/popupStyleBrouillard.css").toExternalForm());
                break;
        }

        fenetreCreationTache.setScene(scene);
        fenetreCreationTache.showAndWait();
    }
}
