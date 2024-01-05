package tralleno.Vues;

import javafx.scene.Scene;
import tralleno.Modele.ModeleBureau;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.Serializable;

/**
 * Vue globale de l'application
 */

public class VuePrincipale implements Serializable {

    public static int TABLEAU;

    public static int LISTE;


    private final BorderPane conteneurPrincipal;
    private final Stage primaryStage;
    private final ModeleBureau modeleBureau;

    private final VueBarreActions vueBarreActions;

    private final VueTableau vueTableau;

    private final VueListe vueListe;

    public VuePrincipale(Stage primaryStage, ModeleBureau modeleBureau) {
        this.primaryStage = primaryStage;
        this.modeleBureau = modeleBureau;

        vueBarreActions = new VueBarreActions(VueBarreActions.TABLEAU, this.modeleBureau, this);

        conteneurPrincipal = new BorderPane();
        conteneurPrincipal.setTop(vueBarreActions);

        this.vueTableau = new VueTableau(this.modeleBureau);
        this.modeleBureau.enregistrerObservateur(vueTableau);



        this.vueListe = new VueListe();
        this.modeleBureau.enregistrerObservateur(this.vueListe);


        changerVue(TABLEAU);
    }

    public void changerVue(int mode) {
        if(mode == TABLEAU){
            conteneurPrincipal.setCenter(this.vueTableau);
        }else if(mode == LISTE){
            conteneurPrincipal.setCenter(this.vueListe);
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public ModeleBureau getModeleBureau() {
        return modeleBureau;
    }

    public Scene getScene() {
        return new Scene(conteneurPrincipal, 800, 600);
    }
}

