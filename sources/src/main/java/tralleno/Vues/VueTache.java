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

public class VueTache extends VBox implements Observateur {

    private Tache tache;
    public VueTache(Tache tache, ModeleBureau modeleBureau) {
        super();

        this.tache = tache;


        Label titreLabel = new Label(tache.getTitre());
        String description = tache.getDescription();
        String descriptionAbregee = description.length() > 20 ? description.substring(0, 20) + "..." : description;
        Label descriptionLabel = new Label(descriptionAbregee);

        titreLabel.setFont(Font.font("Arial", 14)); // Style pour le titre en gras et plus grand
        descriptionLabel.setFont(Font.font("Arial", 10)); // Style pour la description en plus petit

        titreLabel.setWrapText(true); // Permet au texte de se mettre à la ligne s'il est trop long
        descriptionLabel.setWrapText(true);

        VBox texteBox = new VBox(titreLabel, descriptionLabel);
        texteBox.setPadding(new Insets(5)); // Ajoute un espacement entre le texte et les bords du conteneur
        texteBox.setSpacing(5); // Espacement entre le titre et la description

        texteBox.setStyle("-fx-border-color: black; -fx-border-width: 1px;"); // Ajoute une bordure autour du VBox

        this.getChildren().add(texteBox); // Ajoute le VBox dans la StackPane


        // On rend la tâche draggable c'est à dire qu'on peut la maintenir et la déposer quelque part d'autre
        this.setOnDragDetected(event -> {
            Dragboard dragboard = this.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(String.valueOf(this.tache.getId()));
            modeleBureau.setTacheCourante(this.tache);
            dragboard.setContent(content);
            event.consume();
        });
    }


    @Override
    public void actualiser(Sujet s) {

    }
}