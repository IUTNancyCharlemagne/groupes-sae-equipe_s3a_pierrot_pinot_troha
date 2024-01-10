package tralleno.Vues;

import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.stage.Screen;
import tralleno.Modele.ModeleBureau;
import tralleno.Modele.Sujet;
import tralleno.Section.Section;

import java.io.Serializable;
import java.util.List;

import javafx.scene.control.TitledPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class VueListe extends ScrollPane implements Observateur, Serializable {
    private final ModeleBureau modeleBureau;

    public VueListe(ModeleBureau modeleBureau) {
        super();
        this.modeleBureau = modeleBureau;
        setStyle("-fx-border-color: red; -fx-border-width: 1px;");
        actualiser(modeleBureau);
    }

    public void actualiser(Sujet s) {
        if (s instanceof ModeleBureau) {
            setContent(null); // Effacer le contenu existant

            VBox sectionsVBox = new VBox();
            sectionsVBox.setSpacing(10);
            sectionsVBox.setPadding(new Insets(20));
            sectionsVBox.setFillWidth(true); // Permettre à la VBox de remplir toute la largeur disponible

            // Liaison bidirectionnelle des propriétés prefWidth du ScrollPane et de la VBox
            sectionsVBox.prefWidthProperty().bind(this.widthProperty());
            sectionsVBox.setStyle("-fx-background-color: white;");
            List<Section> sections = ((ModeleBureau) s).getSections();

            for (Section section : sections) {
                VueSectionListe vueSectionListe = new VueSectionListe(this.modeleBureau, section);
                sectionsVBox.getChildren().add(vueSectionListe);
            }

            setContent(sectionsVBox);
        }
    }
}

