package tralleno;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import tralleno.Modele.ModeleBureau;
import tralleno.Section.Section;
import tralleno.Taches.Tache;
import tralleno.Taches.TacheFille;
import tralleno.Taches.TacheMere;
import tralleno.Vues.VueBarreActions;
import tralleno.Vues.VueTableau;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tralleno - SAE3.01 Logiciel d'organisation de tâches personnel");

        // ###################################
        // On crée le modèle
        ModeleBureau modeleBureau = new ModeleBureau();
        // On lui ajoute quelques données pour l'affichage
        Section section1 = new Section("Section 1");
        Section section2 = new Section("Section 2");
        Section section3 = new Section("Section 3");
        Section section4 = new Section("Section 4");
        Section section5 = new Section("Section 5");

        TacheMere tache1 = new TacheMere("Tache 1", "Nettoyer maison");
        Tache tache2 = new TacheFille("Tache 2", "Passer l'aspirateur");
        Tache tache3 = new TacheFille("Tache 3", "Passer la serpillière");
        TacheMere tache4 = new TacheMere("Tache 4", "Ratisser le jardin");
        TacheMere tache5 = new TacheMere("Tache 5", "Réparer la voiture");
        tache1.ajouterSousTache(tache2);
        tache1.ajouterSousTache(tache3);

        section1.ajouterTache(tache1);
        tache1.setSectionParente(section1);

        section1.ajouterTache(tache4);
        tache4.setSectionParente(section1);

        section1.ajouterTache(tache5);
        tache5.setSectionParente(section1);

        modeleBureau.ajouterSection(section1);
        modeleBureau.ajouterSection(section2);
        modeleBureau.ajouterSection(section3);
        modeleBureau.ajouterSection(section4);
        modeleBureau.ajouterSection(section5);
        // #################################

        VueBarreActions vueBarreActions = new VueBarreActions(VueBarreActions.TABLEAU, modeleBureau);

        // Création des vues
        VueTableau vueTableau = new VueTableau(modeleBureau);

        // On ajoute les vues au modèle.
        modeleBureau.enregistrerObservateur(vueTableau);

        // Création du conteneur principal pour la barre et la vue tableau
        VBox conteneurPrincipal = new VBox();
        conteneurPrincipal.getChildren().addAll(vueBarreActions, vueTableau);

        // Création de la scène et ajout du conteneur principal
        Scene scene = new Scene(conteneurPrincipal, 800, 600);
        primaryStage.setScene(scene);

        // Affichage de la fenêtre principale
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
