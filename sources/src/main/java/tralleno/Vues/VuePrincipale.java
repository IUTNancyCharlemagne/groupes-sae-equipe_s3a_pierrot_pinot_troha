package tralleno.Vues;

import javafx.animation.TranslateTransition;
import javafx.scene.Scene;
import javafx.util.Duration;
import tralleno.Modele.ModeleBureau;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.Serializable;

/**
 * Vue globale de l'application
 */
public class VuePrincipale implements Serializable {

    /**
     * Constante qui correspond à la VueTableau
     */
    public static final int TABLEAU = 1;

    /**
     * Constante qui correspond à la VueListe
     */
    public static final int LISTE = 2;

    /**
     * Constante qui correspond au thème de base (nuances de gris)
     */
    public static final int THEMEBASE = 1;

    /**
     * Constante qui correspond au thème en bleu
     */
    public static final int THEMEBLUE = 2;


    /**
     * Conteneur principal de l'application
     */
    private final BorderPane conteneurPrincipal;

    /**
     * Scène
     */
    private Scene scene;


    /**
     * A
     */
    private final Stage primaryStage;

    /**
     * Modèle qui contient les données de l'application
     */
    private final ModeleBureau modeleBureau;

    private static int themeCourant;

    /**
     * Barre située en haut de l'application qui permet de créer des sections, tâches de changer de vue, d'accéder à l'archivage
     */
    private final VueBarreActions vueBarreActions;

    /**
     * Vue des tâches/sections sous forme de tableau
     */
    private final VueTableau vueTableau;

    /**
     * Vue des tâches/Sections sous forme de Listes dépliantes
     */
    private final VueListe vueListe;

    /**
     * Vue pour les tâches et sections archivées qui apparaît à droite lorsque l'utilisateur souhaite les consulter
     */
    private final VueArchivage vueArchivage;
    private TranslateTransition afficherMenuTransition;
    private TranslateTransition cacherMenuTransition;

    public VuePrincipale(Stage primaryStage, ModeleBureau modeleBureau) {
        this.primaryStage = primaryStage;
        this.modeleBureau = modeleBureau;

        vueBarreActions = new VueBarreActions(VueBarreActions.TABLEAU, this.modeleBureau, this);

        conteneurPrincipal = new BorderPane();
        conteneurPrincipal.setTop(vueBarreActions);

        this.vueTableau = new VueTableau(this.modeleBureau);
        this.modeleBureau.enregistrerObservateur(vueTableau);


        this.vueListe = new VueListe(this.modeleBureau);
        this.modeleBureau.enregistrerObservateur(this.vueListe);


        // Initialisation du menu d'archivage
        vueArchivage = new VueArchivage(this.modeleBureau); // Crée le panneau d'archivage
        vueArchivage.setPrefWidth(320); // Définit la largeur du panneau d'archivage
        vueArchivage.setVisible(false); // Rend le panneau d'archivage

        this.modeleBureau.enregistrerObservateur(vueArchivage);


        // Animation pour afficher le menu d'archivage
        afficherMenuTransition = new TranslateTransition(Duration.seconds(0.3), vueArchivage);
        afficherMenuTransition.setToX(-vueArchivage.getWidth());
//        showMenuTransition.setToX(0);

        // Animation pour cacher le menu d'archivage
        cacherMenuTransition = new TranslateTransition(Duration.seconds(0.3), vueArchivage);
        cacherMenuTransition.setToX(0);

        changerVue(TABLEAU);
        this.changerTheme(THEMEBASE);
    }

    public void changerVue(int mode) {
        switch (mode){
            case TABLEAU -> conteneurPrincipal.setCenter(this.vueTableau);
            case LISTE -> conteneurPrincipal.setCenter(this.vueListe);
        }
    }

    // Nouvelle méthode pour afficher le menu d'archivage avec animation
    public void afficherArchivage() {
        if (!vueArchivage.isVisible()) {
            conteneurPrincipal.setRight(vueArchivage); // Ajoute la VBox à droite du BorderPane
            afficherMenuTransition.play();
            vueArchivage.setVisible(true);
        }
    }

    // Nouvelle méthode pour cacher le menu d'archivage avec animation
    public void cacherArchivage() {
        if (vueArchivage.isVisible()) {
            cacherMenuTransition.play();
            vueArchivage.setVisible(false);
            conteneurPrincipal.getChildren().remove(vueArchivage); // Enlève la VBox du BorderPane
        }
    }

    // Méthode pour afficher ou cacher le menu d'archivage en fonction de son état actuel
    public void afficherOuCacherArchivage() {
        if (vueArchivage.isVisible()) {
            cacherArchivage();
        } else {
            afficherArchivage();
        }
    }

    /**
     * Méthode qui permet de changer le thème de l'application
     *
     * @param theme numéro du thème choisi
     */
    public void changerTheme(int theme){
        switch (theme){
            case THEMEBASE:
                this.getScene().getStylesheets().clear();
                this.getScene().getStylesheets().add(getClass().getResource("/tralleno/css/Base/trallenoStyleBase.css").toExternalForm());
                this.themeCourant = THEMEBASE;
                break;
            case THEMEBLUE:
                this.getScene().getStylesheets().clear();
                this.getScene().getStylesheets().add(getClass().getResource("/tralleno/css/Blue/trallenoStyleBlue.css").toExternalForm());
                this.themeCourant = THEMEBLUE;
                break;
            default:
                this.getScene().getStylesheets().clear();
                this.getScene().getStylesheets().add(getClass().getResource("/tralleno/css/Base/trallenoStyleBase.css").toExternalForm());
                this.themeCourant = THEMEBASE;
                break;
        }
    }


    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public ModeleBureau getModeleBureau() {
        return modeleBureau;
    }

    public Scene getScene() {
        if(scene == null){
            scene = new Scene(conteneurPrincipal, 800, 600);
        }
        return scene;
    }

    /**
     * @return le numéro du thème courant
     */
    public static int getThemeCourant(){
        return themeCourant;
    }
}

