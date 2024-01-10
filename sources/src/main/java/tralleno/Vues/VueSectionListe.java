package tralleno.Vues;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import tralleno.Controleurs.Sections.ControlModifSection;
import tralleno.Modele.ModeleBureau;
import tralleno.Modele.Sujet;
import tralleno.Section.Section;
import tralleno.Taches.Tache;
import tralleno.Vues.VueTache;

import java.io.Serializable;

public class VueSectionListe extends TitledPane implements Observateur, Serializable {

    private final Section section;

    public VueSectionListe(ModeleBureau modele, Section section) {
        super();
        this.section = section;

        setText(section.getNom());
        setExpanded(true); // Déplier le TitledPane par défaut si nécessaire

        VBox sectionContent = new VBox();
        sectionContent.setSpacing(10);
        sectionContent.setPadding(new Insets(10));
        setContent(sectionContent);

        // CSS pour étendre la section sur toute la largeur
        this.setStyle("-fx-pref-width: 100%;");
        actualiser(modele);
    }

    @Override
    public void actualiser(Sujet s) {
        ModeleBureau modeleBureau = (ModeleBureau) s;

        for (Tache t : this.section.getTaches()) {
            VueTache vueTache = new VueTache(t, modeleBureau, null);
            ((VBox) getContent()).getChildren().add(vueTache);
        }

        // Ajoutez ici d'autres éléments graphiques si nécessaire
    }
}

