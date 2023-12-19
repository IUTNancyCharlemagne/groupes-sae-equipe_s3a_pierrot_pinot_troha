package tralleno.Vues;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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

public class VueBarreActions extends HBox {
    public static int TABLEAU = 0;
    public static int LISTE = 1;

    public static int GANTT = 2;

    public VueBarreActions(int type, ModeleBureau modeleBureau) {
        super(10);

        // Logo de trellano (pour l'instant qu'avec chemin absolu, on verra plus tard)

        String img=System.getProperty("user.dir")+"/src/main/resources/Images/logo.png";
        System.out.println(img);
        File file = new File(img);

        try {
        FileInputStream f=new FileInputStream(file);

        ImageView logo = new ImageView(new Image(f));

        logo.setFitHeight(30);
        logo.setPreserveRatio(true);

        Label nomAppli = new Label("Tralleno");
        nomAppli.setStyle("-fx-font-weight: bold;");

        // Barre d'actions
        if (type != GANTT) {
            Button creerTacheButton = new Button("Créer Tâche");
            creerTacheButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlCreerTache(modeleBureau));
            Button creerSectionButton = new Button("Créer Section");
            creerSectionButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlCreerSection(modeleBureau));
            Button vueTypeButton = (type == TABLEAU) ? new Button("Vue Liste") : new Button("Vue Tableau");
            Button tachesArchiveesButton = new Button("Tâches archivées");

            HBox.setHgrow(nomAppli, Priority.ALWAYS);

            this.setPadding(new Insets(10));
            this.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 5px;");
            this.setSpacing(10);
            this.setAlignment(Pos.CENTER_LEFT);
            this.getChildren().addAll(logo, nomAppli, creerTacheButton, creerSectionButton, vueTypeButton, tachesArchiveesButton);
        }} catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
