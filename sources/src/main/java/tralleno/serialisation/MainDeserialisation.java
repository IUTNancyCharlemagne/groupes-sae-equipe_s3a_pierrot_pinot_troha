package tralleno.serialisation;

import tralleno.Modele.ModeleBureau;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class MainDeserialisation {
    public static void main(String[] args){
        // Ici, on doit pouvoir récupérer notre modèle en le déserialisant du fichier qu'on a créé
        ModeleBureau modeleBureau = null;

        try {
            FileInputStream fichierEntree = new FileInputStream("./src/main/java/tralleno/serialisation/modele.ser");
            ObjectInputStream fluxEntree = new ObjectInputStream(fichierEntree);

            // Désérialisation de l'objet
            modeleBureau = (ModeleBureau) fluxEntree.readObject();

            fluxEntree.close();
            fichierEntree.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(modeleBureau.getSections().get(0).getNom());
    }
}
