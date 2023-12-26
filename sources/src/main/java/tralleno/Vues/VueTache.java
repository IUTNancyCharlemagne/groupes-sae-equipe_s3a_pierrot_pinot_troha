package tralleno.Vues;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import tralleno.Modele.ModeleBureau;
import tralleno.Modele.Sujet;
import tralleno.Taches.Tache;

import java.util.List;

public class VueTache extends VBox implements Observateur {

    private Tache tache;
    public VueTache(Tache tache, ModeleBureau modeleBureau) {
        super();

        this.tache = tache;

        this.actualiser(modeleBureau);

    }

    @Override
    public void actualiser(Sujet s) {
        String nomAbrege = tache.getTitre().length() > 15 ? tache.getTitre().substring(0, 15) + "..." : tache.getTitre();
        Label titreLabel = new Label(nomAbrege);

        String description = tache.getDescription();
        String descriptionAbregee = description.length() > 20 ? description.substring(0, 20) + "..." : description;
        Label descriptionLabel = new Label(descriptionAbregee);

        titreLabel.setFont(Font.font("Arial", 14));
        descriptionLabel.setFont(Font.font("Arial", 10));

        titreLabel.setWrapText(true);
        descriptionLabel.setWrapText(true);

        VBox texteBox = new VBox(titreLabel, descriptionLabel);
        texteBox.setPadding(new Insets(5));
        texteBox.setSpacing(5);

        texteBox.setStyle("-fx-border-color: #e8cdcd; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-color: white; -fx-background-radius: 5px;");

        ModeleBureau modeleBureau = (ModeleBureau) s;

        this.getChildren().add(texteBox);

        // Pour déplacer les tâches d'une section à l'autre
        this.setOnDragDetected(event -> {
            Dragboard dragboard = this.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(String.valueOf(this.tache.getId()));
            modeleBureau.setTacheCourante(this.tache);
            dragboard.setContent(content);
            event.consume();
        });
    }
}
