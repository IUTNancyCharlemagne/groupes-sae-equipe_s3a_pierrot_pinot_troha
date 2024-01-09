package tralleno.Controleurs.Sections;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tralleno.Controleurs.Archivage.ControlArchiverSection;
import tralleno.Modele.ModeleBureau;
import tralleno.Section.Section;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import tralleno.Vues.VuePrincipale;

/**
 * Classe qui gère la modification d'une section
 */
public class ControlModifSection implements EventHandler<MouseEvent> {

    /**
     * Modèle qui comporte les données de l'application
     */
    private ModeleBureau modeleBureau;

    /**
     * Section qui est modifiée
     */
    private Section section;

    /**
     * Construit un contrôleur à partir d'un modèle et d'une section
     * @param modeleBureau
     * @param section
     */
    public ControlModifSection(ModeleBureau modeleBureau, Section section) {
        this.modeleBureau = modeleBureau;
        this.section = section;
    }

    /**
     * Lorsque l'utilisateur clique sur une section, cette méthode est appelée.
     * Elle prend en charge l'évenement en modifiant une section
     * @param mouseEvent
     */
    @Override
    public void handle(MouseEvent mouseEvent) {
        this.modeleBureau.setSectionCourante(this.section);
        // On crée un nouveau stage (fenêtre)
        Stage fenetreModificationSection = new Stage();
        fenetreModificationSection.initModality(Modality.APPLICATION_MODAL);
        fenetreModificationSection.setTitle("Modifier section");


        // Puis on met dans les champs les valeurs actuelles de la section
        Label nomSection = new Label("Nom de la section:");
        nomSection.getStyleClass().add("titreChamp");
        TextField champNom = new TextField(this.section.getNom());
        champNom.getStyleClass().add("champTexteTache");

        Button supprimerSection = new Button("Supprimer");
        supprimerSection.getStyleClass().add("Btn");
        // On ajoute le contrôleur relatif à la suppression d'une section pour le bouton supprimer
        supprimerSection.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlSupprimerSection(this.modeleBureau, this.section, fenetreModificationSection));
        Button archiverSection = new Button("Archiver");
        archiverSection.getStyleClass().add("Btn");
        archiverSection.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlArchiverSection(this.modeleBureau, this.section, fenetreModificationSection));

        HBox actions = new HBox(10);
        actions.getChildren().addAll(supprimerSection, archiverSection);
        actions.setAlignment(Pos.CENTER);

        Button boutonModifierSection = new Button("Modifier Section");
        boutonModifierSection.getStyleClass().add("Btn");
        VBox boutonContainer = new VBox(boutonModifierSection);
        boutonContainer.setAlignment(Pos.CENTER);

        // Lorsque le bouton modifier est cliqué, alors on récupère le nouveau nom de la section
        // Et on modifie la section
        boutonModifierSection.setOnAction(event -> {
            this.modeleBureau.modifierNomSection(champNom.getText());
            fenetreModificationSection.close();
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(nomSection, champNom, actions, boutonContainer);
        layout.getStyleClass().add("VBoxFormulaire");

        Scene scene = new Scene(layout, 400, 170);

        int themeApp = VuePrincipale.getThemeCourant();
        switch (themeApp){
            case 1:
                scene.getStylesheets().clear();
                scene.getStylesheets().add(getClass().getResource("/tralleno/css/Base/popupStyleBase.css").toExternalForm());
                break;
            case 2:
                scene.getStylesheets().clear();
                scene.getStylesheets().add(getClass().getResource("/tralleno/css/Blue/popupStyleBlue.css").toExternalForm());
                break;
            default:
                scene.getStylesheets().clear();
                scene.getStylesheets().add(getClass().getResource("/tralleno/css/Base/popupStyleBase.css").toExternalForm());
                break;
        }

        fenetreModificationSection.setScene(scene);
        fenetreModificationSection.showAndWait();
    }
}
