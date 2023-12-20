package tralleno.Controleurs.Sections;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tralleno.Modele.ModeleBureau;
import tralleno.Section.Section;

public class ControlSupprimerSection implements EventHandler<MouseEvent> {

    private ModeleBureau modeleBureau;

    private Section section;

    private Stage fenetre;

    public ControlSupprimerSection(ModeleBureau m, Section s, Stage fenetre){
        this.modeleBureau = m;
        this.section = s;
        this.fenetre = fenetre;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        this.modeleBureau.supprimerSection(this.section);
        this.fenetre.close();
    }
}
