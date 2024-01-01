package tralleno.Vues;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import tralleno.Controleurs.Sections.ControlCreerSection;
import tralleno.Controleurs.Taches.ControlCreerTache;
import tralleno.Modele.ModeleBureau;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Représente graphiquement la barre de navigation en haut de l'application. Elle permet de créer une tâche, de créer une section
 * De changer de vue, d'accéder aux tâches/sections archivées
 */
public class VueBarreActions extends HBox {
    public static int TABLEAU = 0;
    public static int LISTE = 1;

    public static int GANTT = 2;

    /**
     * Crée une VueBarreAction à partir du type et du modele de l'application
     * @param type
     * @param modeleBureau
     */
    public VueBarreActions(int type, ModeleBureau modeleBureau) {
        super(10);

        String img=System.getProperty("user.dir")+"/src/main/resources/Images/logo.png";
        File file = new File(img);
        ImageView logo;
        try {
        FileInputStream f=new FileInputStream(file);
        logo = new ImageView(new Image(f));
        } catch (FileNotFoundException e) {
            logo = new ImageView();
        }
        logo.setFitHeight(30);
        logo.setPreserveRatio(true);

        Label nomAppli = new Label("Tralleno");
        nomAppli.setStyle("-fx-font-weight: bold;");


        Button creerTacheButton = new Button("Créer Tâche");
        creerTacheButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlCreerTache(modeleBureau));
        Button creerSectionButton = new Button("Créer Section");
        creerSectionButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlCreerSection(modeleBureau));

        ChoiceBox<String> choixVue = new ChoiceBox<>();
        choixVue.getItems().addAll("Vue Tableau", "Vue Liste");
        choixVue.setValue("Vue Tableau");
        // Créer le contrôleur de changement de vue quand on s'occupera de la vue liste


        Button tachesArchiveesButton = new Button("Tâches archivées");

        HBox.setHgrow(nomAppli, Priority.ALWAYS);

        this.setPadding(new Insets(10));
        this.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 5px;");
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER_LEFT);
        this.getChildren().addAll(logo, nomAppli, choixVue, creerTacheButton, creerSectionButton, tachesArchiveesButton);
    }
}
