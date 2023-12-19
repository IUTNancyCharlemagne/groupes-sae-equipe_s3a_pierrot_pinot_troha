package tralleno.Vues;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import tralleno.Modele.Sujet;
import tralleno.Taches.Tache;

public class VueTache extends VBox implements Observateur {
    public VueTache(Tache tache) {
        super();

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
    }


    @Override
    public void actualiser(Sujet s) {

    }
}
