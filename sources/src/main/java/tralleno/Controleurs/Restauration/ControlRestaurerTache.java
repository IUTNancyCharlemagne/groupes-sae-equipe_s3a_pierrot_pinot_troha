package tralleno.Controleurs.Restauration;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import tralleno.Modele.ModeleBureau;
import tralleno.Taches.Tache;

public class ControlRestaurerTache implements EventHandler<MouseEvent> {


    private ModeleBureau modeleBureau;

    private Tache tache;


    public ControlRestaurerTache(ModeleBureau modeleBureau, Tache tache) {
        this.modeleBureau = modeleBureau;
        this.tache = tache;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        this.modeleBureau.setTacheCourante(this.tache);
        this.modeleBureau.restaurerTache();
    }
}
