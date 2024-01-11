package tralleno;

import tralleno.Modele.ModeleBureau;
import javafx.application.Application;
import javafx.stage.Stage;
import tralleno.Vues.VuePrincipale;

import java.io.*;

public class MainSerialiser extends Application {
    /**
     * Modèle qui comporte les données de l'application
     */
    public static ModeleBureau modeleBureau;

    /**
     * Chemin du fichier contenant les données sérialisées
     */
    public static String chemin;


    /**
     * charge les attributs static de la classe à partir du fichier serialisé
     *
     * @param chemin chemin ou se trouve le fichier serialisé
     */
    public static void loaderModele(String chemin) {
        MainSerialiser.chemin = chemin;
        // on essai de recuperer le fichier si ça marche pas on créer un nouveau modele
        try {
            FileInputStream fichierEntree = new FileInputStream(chemin);
            ObjectInputStream fluxEntree = new ObjectInputStream(fichierEntree);

            // Désérialisation de l'objet
            MainSerialiser.modeleBureau = (ModeleBureau) fluxEntree.readObject();
            ModeleBureau.IDTACHEACTUELLE = MainSerialiser.modeleBureau.getIdtacheactuelle();
            ModeleBureau.IDSECTIONACTUELLE = MainSerialiser.modeleBureau.getIdsectionactuelle();

            fluxEntree.close();
            fichierEntree.close();
        } catch (Exception e) {
            System.out.println("nouveau modele");
            MainSerialiser.modeleBureau = new ModeleBureau();
        }

    }

    //demarrage de l'application javafx avec l'attribut de modele en static
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tralleno - SAE3.01 Logiciel d'organisation de tâches personnel");

        VuePrincipale vuePrincipale = new VuePrincipale(primaryStage, modeleBureau);

        primaryStage.setScene(vuePrincipale.getScene());

        primaryStage.show();
    }


    /**
     * methode qui sauvegarde le modele static en fichier avec le chemin de fichier static
     */
    public static void serialiser() {
        try {
            FileOutputStream fichierSortie = new FileOutputStream(MainSerialiser.chemin);
            ObjectOutputStream fluxSortie = new ObjectOutputStream(fichierSortie);

            // Sérialisation de l'objet
            MainSerialiser.modeleBureau.setIdsectionactuelle(ModeleBureau.IDSECTIONACTUELLE);
            MainSerialiser.modeleBureau.setIdtacheactuelle(ModeleBureau.IDTACHEACTUELLE);

            fluxSortie.writeObject(MainSerialiser.modeleBureau);

            fluxSortie.close();
            fichierSortie.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * lance l'application
     *
     * @param args
     */
    public static void main(String[] args) {

        MainSerialiser.loaderModele("./src/main/resources/tralleno/serialisation/modele.ser");
        //lunch(new String[]{});
        launch(args);
        MainSerialiser.serialiser();
        System.out.println(ModeleBureau.IDTACHEACTUELLE);

    }
}