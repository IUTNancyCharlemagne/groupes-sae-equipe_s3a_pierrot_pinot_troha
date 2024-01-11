package tralleno.Controleurs.Archivage;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tralleno.Modele.ModeleBureau;
import tralleno.Taches.Tache;

/**
 * Classe qui permet d'archiver la tâche sélectionnée par l'utilisateur lors de son interaction
 */
public class ControlArchiverTache implements EventHandler<MouseEvent> {

    /**
     * Modèle qui contient les données de l'application
     */
    private final ModeleBureau modeleBureau;

    /**
     * Tâche à archiver
     */
    private final Tache tache;

    /**
     * Fenetre ouverte lors de la modification d'une tâche (menu depuis le quel on peut archiver la tâche)
     */
    private final Stage fenetre;

    /**
     * Construit un contrôleur à partir du modèle et d'une section et de la fenêtre ouverte
     *
     * @param modeleBureau
     * @param tache
     * @param fenetre
     */
    public ControlArchiverTache(ModeleBureau modeleBureau, Tache tache, Stage fenetre) {
        this.modeleBureau = modeleBureau;
        this.tache = tache;
        this.fenetre = fenetre;
    }

    /**
     * Lorsque l'utilisateur clique sur un bouton archiver tâche, cette méthode est appelée.
     * Elle prend en charge l'évenement en archivant la tâche en question.
     *
     * @param mouseEvent
     */
    @Override
    public void handle(MouseEvent mouseEvent) {
        this.modeleBureau.setTacheCourante(this.tache);
        this.modeleBureau.archiverTache();
        this.fenetre.close();
    }
}
