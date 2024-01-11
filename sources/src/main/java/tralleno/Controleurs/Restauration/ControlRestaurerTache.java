package tralleno.Controleurs.Restauration;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import tralleno.Modele.ModeleBureau;
import tralleno.Taches.Tache;

/**
 * Classe qui permet à l'utilisateur lorsqu'il clique sur le bouton restaurer d'une tâche dans la VueArchivage
 * de restaurer cette même tâche
 */
public class ControlRestaurerTache implements EventHandler<MouseEvent> {

    /**
     * Modèle qui contient les données de l'application
     */
    private final ModeleBureau modeleBureau;

    /**
     * Tâche concernée par la restauration
     */
    private final Tache tache;


    /**
     * Construit un ControlRestaurerTache à partir du modèle et de la tâche concernée
     * @param modeleBureau
     * @param tache
     */
    public ControlRestaurerTache(ModeleBureau modeleBureau, Tache tache) {
        this.modeleBureau = modeleBureau;
        this.tache = tache;
    }

    /**
     * Méhode déclenchée lorsque l'utilisateur clique sur le bouton restaurer d'une tâche dans la VueArchivage
     * @param mouseEvent
     */
    @Override
    public void handle(MouseEvent mouseEvent) {
        this.modeleBureau.setTacheCourante(this.tache);
        this.modeleBureau.restaurerTache();
    }
}
