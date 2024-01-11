package tralleno.Vues;

import javafx.scene.Scene;
import tralleno.Controleurs.ControlVues;
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
    private static final int GANTT = 3;
    public static final int SELECTGANTT = 4;

    /**
     * Constante qui correspond au thème de base (nuances de gris)
     */
    public static final int THEMEBROUILLARD = 1;

    /**
     * Constante qui correspond au thème en bleu
     */
    public static final int THEMEOCEAN = 2;

    /**
     * Constante qui correspond au thème en nuances de rouge/orange
     */
    public static final int THEMECREPUSCULE = 3;

    /**
     * Constante qui correspond au thème en vert
     */
    public static final int THEMEFORET = 4;

    /**
     * Constante qui correspond au thème en nuance foncées
     */
    public static final int THEMENUIT = 5;

    /**
     * Constante qui correspond au thème en jaune
     */
    public static final int THEMEPLAGE = 6;

    /**
     * Conteneur principal de l'application
     */
    private final BorderPane conteneurPrincipal;

    /**
     * Scène qui joue l'attribut de Singleton, car lors du getScene qui est appelé de la VuePrincipale
     * pour le primary stage, il faut que ce soit en permanence la même scène, et si elle n'a pas encore été instanciée, alors il faut l'instancier
     * Car dans le main on fait appel plusieurs fois à getScene
     */
    private Scene scene;


    /**
     * Fenêtre de base de l'application
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
     * Vue des tâches sous forme de diagramme de Gantt
     */
    private final VueGantt vueGantt;

    /**
     * Vue dans laquelle on sélectionne les tâches nécessaires à la génération du diagramme de Gantt
     */
    private final VueSelecteurGantt vueSelecteurGantt;

    /**
     * Vue des tâches/Sections sous forme de Listes dépliantes
     */
    private final VueListe vueListe;

    /**
     * Vue pour les tâches et sections archivées qui apparaît à droite lorsque l'utilisateur souhaite les consulter
     */
    private final VueArchivage vueArchivage;

    public VuePrincipale(Stage primaryStage, ModeleBureau modeleBureau) {
        this.primaryStage = primaryStage;
        this.modeleBureau = modeleBureau;

        vueBarreActions = new VueBarreActions(VueBarreActions.TABLEAU, this.modeleBureau, this);

        conteneurPrincipal = new BorderPane();
        conteneurPrincipal.setTop(vueBarreActions);
        conteneurPrincipal.getTop().getStyleClass().add("BPTop_BarreAction");

        this.vueTableau = new VueTableau(this.modeleBureau);
        this.modeleBureau.enregistrerObservateur(vueTableau);


        this.vueListe = new VueListe(this.modeleBureau);
        this.modeleBureau.enregistrerObservateur(this.vueListe);

        this.vueGantt = new VueGantt(this.modeleBureau);
        this.modeleBureau.enregistrerObservateur(this.vueGantt);

        this.vueSelecteurGantt = new VueSelecteurGantt(this.modeleBureau, this);
        this.modeleBureau.enregistrerObservateur(this.vueSelecteurGantt);

        // Initialisation du menu d'archivage
        vueArchivage = new VueArchivage(this.modeleBureau); // Crée le panneau d'archivage
        vueArchivage.setPrefWidth(320); // Définit la largeur du panneau d'archivage
        vueArchivage.setVisible(false); // Rend le panneau d'archivage

        this.modeleBureau.enregistrerObservateur(vueArchivage);

        changerVue(TABLEAU);
        this.changerTheme(THEMEOCEAN);
    }

    public void changerVue(int mode) {
        switch (mode) {
            case TABLEAU -> {
                conteneurPrincipal.setCenter(this.vueTableau);
            }
            case LISTE -> {
                conteneurPrincipal.setCenter(this.vueListe);
            }
            case SELECTGANTT -> {
                conteneurPrincipal.setCenter(this.vueSelecteurGantt);
            }
            case GANTT -> {
                conteneurPrincipal.setCenter(this.vueGantt);
            }
        }
        this.modeleBureau.notifierObservateurs();
    }

    /**
     * Permet d'afficher le menu d'archivage en l'ajoutant à droite du borderpane
     */
    public void afficherArchivage() {
        if (!vueArchivage.isVisible()) {
            conteneurPrincipal.setRight(vueArchivage); // Ajoute la VBox à droite du BorderPane
            conteneurPrincipal.getRight().getStyleClass().add("BTRight_Archivage");
            ControlVues.getBoutonArchivage().setId("BtnArchivage_ArchivageOuvert");
            vueArchivage.setVisible(true);
        }
    }

    /**
     * Permet de cacher le menu d'archivage quand il est ouvert, le supprime du BorderPane
     */
    public void cacherArchivage() {
        if (vueArchivage.isVisible()) {
            vueArchivage.setVisible(false);
            conteneurPrincipal.getChildren().remove(vueArchivage);
            ControlVues.getBoutonArchivage().setId("aucunStyleSupplementaire");
        }
    }

    /**
     * Méthode pour afficher ou cacher le menu d'archivage en fonction de son état actuel
     */
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
    public void changerTheme(int theme) {
        switch (theme) {
            case THEMEBROUILLARD:
                this.getScene().getStylesheets().clear();
                this.getScene().getStylesheets().add(getClass().getResource("/tralleno/css/Brouillard/trallenoStyleBrouillard.css").toExternalForm());
                themeCourant = THEMEBROUILLARD;
                break;
            case THEMEOCEAN:
                this.getScene().getStylesheets().clear();
                this.getScene().getStylesheets().add(getClass().getResource("/tralleno/css/Ocean/trallenoStyleOcean.css").toExternalForm());
                themeCourant = THEMEOCEAN;
                break;
            case THEMECREPUSCULE:
                this.getScene().getStylesheets().clear();
                this.getScene().getStylesheets().add(getClass().getResource("/tralleno/css/Crepuscule/trallenoStyleCrepuscule.css").toExternalForm());
                themeCourant = THEMECREPUSCULE;
                break;
            case THEMEFORET:
                this.getScene().getStylesheets().clear();
                this.getScene().getStylesheets().add(getClass().getResource("/tralleno/css/Foret/trallenoStyleForet.css").toExternalForm());
                themeCourant = THEMEFORET;
                break;
            case THEMENUIT:
                this.getScene().getStylesheets().clear();
                this.getScene().getStylesheets().add(getClass().getResource("/tralleno/css/Nuit/trallenoStyleNuit.css").toExternalForm());
                themeCourant = THEMENUIT;
                break;
            case THEMEPLAGE:
                this.getScene().getStylesheets().clear();
                this.getScene().getStylesheets().add(getClass().getResource("/tralleno/css/Plage/trallenoStylePlage.css").toExternalForm());
                themeCourant = THEMEPLAGE;
                break;
            default:
                this.getScene().getStylesheets().clear();
                this.getScene().getStylesheets().add(getClass().getResource("/tralleno/css/Brouillard/trallenoStyleBrouillard.css").toExternalForm());
                themeCourant = THEMEBROUILLARD;
                break;
        }
    }

    /**
     * Retourne le modèle bureau que contient la VuePrincipale
     * @return
     */
    public ModeleBureau getModeleBureau() {
        return modeleBureau;
    }

    /**
     * Retourne la scène de la vueprincipale, car le fait d'y ajouter du css peut faire appeler plusieurs fois
     * la méthode getScene, il faut donc en créer une s'il n'y en a pas encore, ou la renvoyer si elle existe
     * @return
     */
    public synchronized Scene getScene() {
        if (scene == null) {
            scene = new Scene(conteneurPrincipal, 800, 600);
        }
        return scene;
    }

    /**
     * @return le numéro du thème courant
     */
    public static int getThemeCourant() {
        return themeCourant;
    }
}

