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
     *
     * @param modeleBureau
     */
    public ControlCreerTache(ModeleBureau modeleBureau) {
        this.modeleBureau = modeleBureau;
    }

    /**
     * Lorsque l'utilisateur clique sur un bouton créer tâche, cette méthode est appelée.
     * Elle prend en charge l'évenement en créant une tâche à partir des informations
     * que l'utilisateur choisira de renseigner
     *
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

        //on ajoute le style du thème courant à la fenêtre popup
        scene.getStylesheets().add(getClass().getResource(VuePrincipale.getThemePopup()).toExternalForm());

        fenetreCreationTache.setScene(scene);
        fenetreCreationTache.showAndWait();
    }
}
