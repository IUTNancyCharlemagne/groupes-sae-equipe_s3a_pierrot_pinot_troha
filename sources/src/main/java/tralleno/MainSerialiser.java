package tralleno;

import tralleno.Modele.ModeleBureau;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import tralleno.Vues.VueBarreActions;
import tralleno.Vues.VueTableau;

import java.io.*;

public class MainSerialiser extends Application{
    public static ModeleBureau modeleBureau;
    //chemin ou se trouve
    public static String chemin;



    /**
     * charge les attributs static de la classe à partir du fichier serialisé
     * @param chemin chemin ou se trouve le fichier serialisé
     */
    public static void loaderModele(String chemin){


        MainSerialiser.chemin=chemin;
        // on essai de recuperer le fichier si ça marche pas on créer un nouveau modele
        try {
            FileInputStream fichierEntree = new FileInputStream(chemin);
            ObjectInputStream fluxEntree = new ObjectInputStream(fichierEntree);

            // Désérialisation de l'objet
            MainSerialiser.modeleBureau = (ModeleBureau) fluxEntree.readObject();

            fluxEntree.close();
            fichierEntree.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("nouveau modele");
            MainSerialiser.modeleBureau=new ModeleBureau();
        }

    }

    //demarrage de l'application javafx avec l'attribut de modele en static
    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Tralleno - SAE3.01 Logiciel d'organisation de tâches personnel");

        // ###################################
        // On crée le modèle

        VueBarreActions vueBarreActions = new VueBarreActions(VueBarreActions.TABLEAU, modeleBureau);

        // Création des vues
        VueTableau vueTableau = new VueTableau(modeleBureau);

        // On ajoute les vues au modèle.
        MainSerialiser.modeleBureau.enregistrerObservateur(vueTableau);

        // Création du conteneur principal pour la barre et la vue tableau
        VBox conteneurPrincipal = new VBox();
        VBox.setVgrow(vueTableau, Priority.ALWAYS);
        conteneurPrincipal.getChildren().addAll(vueBarreActions, vueTableau);

        // Création de la scène et ajout du conteneur principal
        Scene scene = new Scene(conteneurPrincipal, 800, 600);
        primaryStage.setScene(scene);

        // Affichage de la fenêtre principale
        primaryStage.show();

    }


    /**
     * methode qui sauvegarde le modele static en fichier avec le chemin de fichier static
     */
    public static void serialiser(){
        try {
            FileOutputStream fichierSortie = new FileOutputStream(MainSerialiser.chemin);
            ObjectOutputStream fluxSortie = new ObjectOutputStream(fichierSortie);

            // Sérialisation de l'objet
            fluxSortie.writeObject(MainSerialiser.modeleBureau);

            fluxSortie.close();
            fichierSortie.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * lance l'application
     * @param args
     */
    public static void main(String[] args) {

        MainSerialiser.loaderModele("./src/main/java/tralleno/serialisation/modele.ser");
        //lunch(new String[]{});
        launch(args);
        MainSerialiser.serialiser();

    }
}
