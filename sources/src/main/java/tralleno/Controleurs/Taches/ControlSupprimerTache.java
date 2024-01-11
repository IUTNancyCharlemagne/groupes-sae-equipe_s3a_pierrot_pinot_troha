package tralleno.Controleurs.Taches;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tralleno.Modele.ModeleBureau;
import tralleno.Taches.Tache;

/**
 * Classe qui permet de gérer la suppression d'une tâche lorsque l'utilisateur le souhaite
 */
public class ControlSupprimerTache implements EventHandler<MouseEvent> {
    /**
     * Modèle qui contient les données de l'application
     */
    private final ModeleBureau modeleBureau;

    /**
     * tache concernée par la suppression
     */
    private final Tache tacheASupprimer;

    /**
     * Fenetre de modification de la tâche (qui est ouverte)
     */
    private final Stage fenetre;

    /**
     * Construit un contrôleur à partir du modèle, de la tâche concernée et de la fenêtre de modification ouverte
     *
     * @param m
     * @param t
     * @param fenetre
     */
    public ControlSupprimerTache(ModeleBureau m, Tache t, Stage fenetre) {
        this.modeleBureau = m;
        this.tacheASupprimer = t;
        this.fenetre = fenetre;
    }

    /**
     * Lorsque l'utilisateur clique sur un bouton supprimer tâche dans le menu de modification, cette méthode est appelée.
     * Elle prend en charge l'évenement en supprimant la tâche concernée
     *
     * @param mouseEvent
     */
    @Override
    public void handle(MouseEvent mouseEvent) {
        this.modeleBureau.setTacheCourante(this.tacheASupprimer);
        this.modeleBureau.supprimerTache();
        if (fenetre != null)
            this.fenetre.close();
    }
}
