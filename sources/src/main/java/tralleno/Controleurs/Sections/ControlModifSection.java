package tralleno.Controleurs.Sections;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tralleno.Modele.ModeleBureau;
import tralleno.Section.Section;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;

public class ControlModifSection implements EventHandler<MouseEvent> {

    private ModeleBureau modeleBureau;

    private Section section;

    public ControlModifSection(ModeleBureau modeleBureau, Section section) {
        this.modeleBureau = modeleBureau;
        this.section = section;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        this.modeleBureau.setSectionCourante(this.section);
        // On crée un nouveau stage (fenêtre)
        Stage fenetreModificationSection = new Stage();
        fenetreModificationSection.initModality(Modality.APPLICATION_MODAL);
        fenetreModificationSection.setTitle("Modifier section");


        // Puis on met dans les champs les valeurs actuelles de la section
        Label nomSection = new Label("Nom de la section:");
        TextField champNom = new TextField(this.section.getNom());

        Button supprimerSection = new Button("Supprimer");
        supprimerSection.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlSupprimerSection(this.modeleBureau, this.section, fenetreModificationSection));
        Button archiverSection = new Button("Archiver");

        HBox actions = new HBox(10);

        actions.getChildren().addAll(supprimerSection, archiverSection);

        Button boutonModifierSection = new Button("Modifier Section");


        boutonModifierSection.setOnAction(event -> {
            this.modeleBureau.modifierNomSection(this.section, champNom.getText());
            fenetreModificationSection.close();
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(nomSection, champNom, actions, boutonModifierSection);

        Scene scene = new Scene(layout, 400, 150);
        fenetreModificationSection.setScene(scene);
        fenetreModificationSection.showAndWait();
    }
}
