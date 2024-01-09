package tralleno.Vues;

import javafx.scene.control.Label;
import tralleno.Taches.Tache;

public class VueTacheSelection extends Label{
    private Tache t;

    public VueTacheSelection(Tache tache){
        super(tache.toStringDate());
        this.t=tache;
    }

    public Tache getT() {
        return t;
    }
}
