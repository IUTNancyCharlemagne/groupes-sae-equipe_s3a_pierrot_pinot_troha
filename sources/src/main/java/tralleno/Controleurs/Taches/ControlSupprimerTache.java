package tralleno.Controleurs.Taches;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tralleno.Modele.ModeleBureau;
import tralleno.Section.Section;
import tralleno.Taches.Tache;

public class ControlSupprimerTache implements EventHandler<MouseEvent> {
    private ModeleBureau modeleBureau;

    private Tache tacheASupprimer;

    private Stage fenetre;

    public ControlSupprimerTache(ModeleBureau m, Tache t, Stage fenetre){
        this.modeleBureau = m;
        this.tacheASupprimer = t;
        this.fenetre = fenetre;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        this.modeleBureau.supprimerTache(this.tacheASupprimer);
        this.fenetre.close();
    }
}
