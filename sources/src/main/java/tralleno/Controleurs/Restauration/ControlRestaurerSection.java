package tralleno.Controleurs.Restauration;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import tralleno.Modele.ModeleBureau;
import tralleno.Section.Section;

/**
 * Permet de restaurer une section lorsque l'utilisateur le souhaite
 */
public class ControlRestaurerSection implements EventHandler<MouseEvent> {

    /**
     * Modèle qui contient les données de l'application
     */
    private ModeleBureau modeleBureau;

    /**
     * Section concernée par la restauration
     */
    private Section section;


    /**
     * Construit un ControlRestaurerSection à partir du modèle du bureau et de la section en question
     * @param modeleBureau
     * @param section
     */
    public ControlRestaurerSection(ModeleBureau modeleBureau, Section section) {
        this.modeleBureau = modeleBureau;
        this.section = section;
    }

    /**
     * Méhode déclenchée lorsque l'utilisateur clique sur le bouton restaurer d'une section dans la VueArchivage
     * @param mouseEvent
     */
    @Override
    public void handle(MouseEvent mouseEvent) {
        this.modeleBureau.setSectionCourante(this.section);
        this.modeleBureau.restaurerSection();
    }
}
