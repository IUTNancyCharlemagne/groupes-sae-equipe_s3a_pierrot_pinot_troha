package tralleno.serialisation;

import tralleno.Modele.ModeleBureau;
import tralleno.Section.Section;
import tralleno.Taches.TacheFille;
import tralleno.Taches.TacheMere;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class MainSerialisation {
    public static void main(String[] args){
        // Pour qu'un objet soit sérialisable, lui et les autres objets qu'il contient doivent
        // implémenter l'interface Serializable

        // On crée un modèle pour faire le test
        ModeleBureau modeleBureau = new ModeleBureau();

        Section s1 = new Section("Section Test Serialisation");
        TacheMere tacheMere = new TacheMere("Tache Mere 1", "Description de la tâche Mere 1");
        TacheFille tacheFille = new TacheFille("Tache Fille 1", "Description de la sous tâche");

        tacheMere.ajouterSousTache(tacheFille);
        s1.ajouterTache(tacheMere);

        modeleBureau.ajouterSection(s1);

        System.out.println(modeleBureau.getSections().get(0).getNom());

        // Maintenant, on va tenter de sérialiser l'objet dans un fichier.
        try {
            FileOutputStream fichierSortie = new FileOutputStream("./src/main/java/tralleno/serialisation/modele.ser");
            ObjectOutputStream fluxSortie = new ObjectOutputStream(fichierSortie);

            // Sérialisation de l'objet
            fluxSortie.writeObject(modeleBureau);

            fluxSortie.close();
            fichierSortie.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
