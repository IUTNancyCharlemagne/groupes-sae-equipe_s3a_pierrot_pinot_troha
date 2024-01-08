package tralleno.Controleurs.Restauration;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import tralleno.Controleurs.Archivage.ControlArchiverSection;
import tralleno.Modele.ModeleBureau;
import tralleno.Section.Section;

public class ControlRestaurerSection implements EventHandler<MouseEvent> {

    private ModeleBureau modeleBureau;

    private Section section;


    public ControlRestaurerSection(ModeleBureau modeleBureau, Section section){
        this.modeleBureau = modeleBureau;
        this.section = section;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        System.out.println("Essai de restauration de section " + this.section.getNom());
        this.modeleBureau.setSectionCourante(this.section);
        this.modeleBureau.restaurerSection();
    }
}
