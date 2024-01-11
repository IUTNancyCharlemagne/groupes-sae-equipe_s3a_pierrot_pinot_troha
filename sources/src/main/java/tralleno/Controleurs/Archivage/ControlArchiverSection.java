package tralleno.Controleurs.Archivage;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tralleno.Modele.ModeleBureau;
import tralleno.Section.Section;

/**
 * Classe qui permet de gérer l'archivage d'une section après interaction de l'utilisateur
 */
public class ControlArchiverSection implements EventHandler<MouseEvent> {

    /**
     * Modèle qui contient les données de l'application
     */
    private final ModeleBureau modeleBureau;

    /**
     * Section concernée par l'archivage
     */
    private final Section section;

    /**
     * Fenêtre ouverte lors de la modification de la section, à refermer
     */
    private final Stage fenetre;

    /**
     * Construit un contrôleur à partir du modèle et d'une section, et de la fenetre ouverte
     *
     * @param modeleBureau
     * @param section
     */
    public ControlArchiverSection(ModeleBureau modeleBureau, Section section, Stage fenetre) {
        this.modeleBureau = modeleBureau;
        this.section = section;
        this.fenetre = fenetre;
    }

    /**
     * Lorsque l'utilisateur clique sur un bouton archiver section, cette méthode est appelée.
     * Elle prend en charge l'évenement en archivant la section en question.
     *
     * @param mouseEvent
     */
    @Override
    public void handle(MouseEvent mouseEvent) {
        this.modeleBureau.setSectionCourante(this.section);
        this.modeleBureau.archiverSection();
        this.fenetre.close();
    }
}
