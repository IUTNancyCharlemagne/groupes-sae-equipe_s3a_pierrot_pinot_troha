package tralleno.Controleurs.Sections;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tralleno.Modele.ModeleBureau;
import tralleno.Section.Section;

public class ControlCreerSection implements EventHandler<MouseEvent> {

    private ModeleBureau modeleBureau;

    public ControlCreerSection(ModeleBureau modeleBureau){
        this.modeleBureau = modeleBureau;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        // On crée un nouveau stage (fenêtre)
        Stage fenetreCreationSection = new Stage();
        fenetreCreationSection.initModality(Modality.APPLICATION_MODAL);
        fenetreCreationSection.setTitle("Créer une Section");

        // On demande le titre de la section
        Label nomSection = new Label("Nom de la section:");
        TextField champNom = new TextField();


        Button boutonCreerSection = new Button("Créer Section");


        boutonCreerSection.disableProperty().bind(
                champNom.textProperty().isEmpty()
        );

        boutonCreerSection.setOnAction(event ->{
            // On récupère le nom de la section
            String nom = champNom.getText();

            // Puis on la crée
            Section section = new Section(nom);
            modeleBureau.ajouterSection(section);

            fenetreCreationSection.close();

        });


        // On met le tout dans une Vbox qui est le formulaire
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(nomSection, champNom, boutonCreerSection);

        // On crée la nouvelle scène et on lui ajoute le formulaire
        Scene scene = new Scene(layout, 400, 150);
        fenetreCreationSection.setScene(scene);
        fenetreCreationSection.showAndWait();
    }
}
