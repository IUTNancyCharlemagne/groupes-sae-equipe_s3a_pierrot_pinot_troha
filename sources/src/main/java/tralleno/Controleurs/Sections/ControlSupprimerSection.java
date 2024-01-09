package tralleno.Controleurs.Sections;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tralleno.Modele.ModeleBureau;
import tralleno.Section.Section;

/**
 * Classe qui gère la suppression d'une section après action de l'utilisateur
 */
public class ControlSupprimerSection implements EventHandler<MouseEvent> {

    /**
     * Modèle qui comporte les données de l'application
     */
    private ModeleBureau modeleBureau;

    /**
     * Section sur laquelle l'évenement de suppression est appelé
     */
    private Section section;

    /**
     * Fenetre ouverte lors de la modification de la section étant donné que pour la supprimer
     * il faut ouvrir la fenêtre de modification
     */
    private Stage fenetre;

    /**
     * Crée le contrôleur de suppression d'une section à partir du modèle de l'application, de la section associée et de la fenetre de modification ouverte
     * @param m
     * @param s
     * @param fenetre
     */
    public ControlSupprimerSection(ModeleBureau m, Section s, Stage fenetre){
        this.modeleBureau = m;
        this.section = s;
        this.fenetre = fenetre;
    }

    /**
     * Lorsque l'utilisateur clique sur un bouton suppriemer section, cette méthode est appelée.
     * Elle prend en charge l'évenement en supprimant la section
     * @param mouseEvent
     */
    @Override
    public void handle(MouseEvent mouseEvent) {
        this.modeleBureau.setSectionCourante(this.section);
        this.modeleBureau.supprimerSection();
        if(fenetre != null)
            this.fenetre.close();
    }
}
