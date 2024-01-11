package tralleno.Controleurs.Taches;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tralleno.Modele.ModeleBureau;
import tralleno.Taches.Tache;
import tralleno.Taches.TacheMere;
import tralleno.Vues.VuePrincipale;

import java.io.Serializable;

/**
 * Classe qui gère la modification d'une tâche lorsque l'utilisateur clique sur le bouton modifier d'une tâche
 */
public class ControlModifTache implements EventHandler<MouseEvent>, Serializable {

    /**
     * Modèle qui comporte les données de l'application
     */
    private ModeleBureau modeleBureau;

    /**
     * Tâche concernée par la modification
     */
    private Tache tacheAModifier;

    /**
     * Tache parente de la tâche à modifier, qui vaut null si la tâche n'est pas une sous-tâche
     */
    private TacheMere tacheParente;

    /**
     * Construit le contrôleur à partir du modele et de la tâche à modifier
     * @param modeleBureau
     * @param tacheAModifier
     * @param tacheParente
     */
    public ControlModifTache(ModeleBureau modeleBureau, Tache tacheAModifier, TacheMere tacheParente) {
        this.modeleBureau = modeleBureau;
        this.tacheAModifier = tacheAModifier;
        this.tacheParente = tacheParente;
    }

    /**
     * Lorsque l'utilisateur clique sur le bouton "..." de la tâche, cette méthode est appelée.
     * Elle prend en charge l'évenement en permettant à l'utilisateur de modifier la tâche
     * @param mouseEvent
     */
    @Override
    public void handle(MouseEvent mouseEvent) {
        this.modeleBureau.setTacheCourante(this.tacheAModifier);

        // On crée un nouveau stage (fenêtre)
        Stage fenetreModificationTache = new Stage();
        fenetreModificationTache.initModality(Modality.APPLICATION_MODAL);
        fenetreModificationTache.setTitle("Modifier une Tâche");


        // On crée la nouvelle scène et on lui ajoute le formulaire
        Scene scene = FormulaireTache.creerFormulaireTache(this.modeleBureau, this.tacheAModifier, this.tacheParente, fenetreModificationTache);

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

        fenetreModificationTache.setScene(scene);
        fenetreModificationTache.showAndWait();

    }

}
