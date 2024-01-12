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
     *
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
     *
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

        //on ajoute le style du thème courant à la fenêtre popup
        scene.getStylesheets().add(getClass().getResource(VuePrincipale.getThemePopup()).toExternalForm());

        fenetreModificationTache.setScene(scene);
        fenetreModificationTache.showAndWait();

    }

}
