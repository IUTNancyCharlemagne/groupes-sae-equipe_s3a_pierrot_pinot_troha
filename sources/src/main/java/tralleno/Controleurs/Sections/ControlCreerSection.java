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

/**
 * Classe qui permet de gérer la création de section lorsque l'utilisateur le souhaite
 */
public class ControlCreerSection implements EventHandler<MouseEvent> {

    /**
     * Modele qui comporte toutes les données
     */
    private ModeleBureau modeleBureau;

    /**
     * Construit à partir d'un bureau le controleur qui permet de créer une section
     * @param modeleBureau
     */
    public ControlCreerSection(ModeleBureau modeleBureau){
        this.modeleBureau = modeleBureau;
    }

    /**
     * Lorsque l'utilisateur clique sur un bouton créer section, cette méthode est appelée.
     * Elle prend en charge l'évenement en créant une section
     * @param mouseEvent
     */
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

        // On désactive la possibilité de cliquer sur créer si aucun nom n'est renseigné
        boutonCreerSection.disableProperty().bind(
                champNom.textProperty().isEmpty()
        );

        // Lorsque le bouton est cliqué
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
