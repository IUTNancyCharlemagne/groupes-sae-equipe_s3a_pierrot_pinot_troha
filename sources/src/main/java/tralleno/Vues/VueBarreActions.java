package tralleno.Vues;

import javafx.event.ActionEvent;
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
import tralleno.Controleurs.ControlVues;
import tralleno.Controleurs.Sections.ControlCreerSection;
import tralleno.Controleurs.Taches.ControlCreerTache;
import tralleno.Modele.ModeleBureau;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;

/**
 * Représente graphiquement la barre de navigation en haut de l'application. Elle permet de créer une tâche, de créer une section
 * De changer de vue, d'accéder aux tâches/sections archivées
 */
public class VueBarreActions extends HBox implements Serializable {
    public static int TABLEAU = 0;
    public static int LISTE = 1;

    public static int GANTT = 2;

    /**
     * Crée une VueBarreAction à partir du type et du modele de l'application
     * @param type
     * @param modeleBureau
     */
    public VueBarreActions(int type, ModeleBureau modeleBureau, VuePrincipale vuePrincipale) {
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
        nomAppli.getStyleClass().add("nomAppli");

        Button creerTacheButton = new Button("Créer Tâche");
        creerTacheButton.getStyleClass().add("actionButton");
        creerTacheButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlCreerTache(modeleBureau));
        Button creerSectionButton = new Button("Créer Section");
        creerSectionButton.getStyleClass().add("actionButton");
        creerSectionButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlCreerSection(modeleBureau));

        ChoiceBox<String> choixVue = new ChoiceBox<>();
        choixVue.getItems().addAll("Vue Tableau", "Vue Liste", "Vue Gantt");
        choixVue.getStyleClass().add("choixVue");
        choixVue.setValue("Vue Tableau");
        choixVue.addEventHandler(ActionEvent.ACTION, new ControlVues(choixVue, vuePrincipale));

        HBox elementsDroite = new HBox();
        elementsDroite.getStyleClass().add("elementsDroite");

        Button tachesArchiveesButton = new Button("Archivage");
        tachesArchiveesButton.addEventHandler(ActionEvent.ACTION, new ControlVues(tachesArchiveesButton, vuePrincipale));
        tachesArchiveesButton.getStyleClass().add("actionButton");

        Label theme = new Label("Thème :");
        theme.getStyleClass().add("labelTheme");

        ChoiceBox<String> choixtheme = new ChoiceBox<>();
        choixtheme.getItems().addAll("Base", "Bleu");
        choixtheme.getStyleClass().add("choixVue");
        choixtheme.setValue("Base");
        choixtheme.addEventHandler(ActionEvent.ACTION, new ControlVues(choixtheme, vuePrincipale, "Contournement"));

        elementsDroite.getChildren().addAll(tachesArchiveesButton, theme, choixtheme);

        HBox.setHgrow(nomAppli, Priority.ALWAYS);
        HBox.setHgrow(elementsDroite, Priority.ALWAYS);

        this.setPadding(new Insets(10));
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER_LEFT);
        this.getChildren().addAll(logo, nomAppli, choixVue, creerSectionButton, creerTacheButton, elementsDroite);
        this.getStyleClass().add("entete");
    }
}
