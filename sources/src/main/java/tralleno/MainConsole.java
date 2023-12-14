package tralleno;

import tralleno.Taches.Tache;
import tralleno.Taches.TacheFille;
import tralleno.Taches.TacheMere;

import java.util.List;

public class MainConsole {

    public static void main(String[] args){

        TacheMere tache1 = new TacheMere("Nettoyer la maison", "Devoir nettoyer la maison");
        TacheMere tache2 = new TacheMere("Passer l'aspirateur", "Le passer à l'étage, et au rez de chaussée");
        Tache tache3 = new TacheFille("Passer l'aspirateur à l'étage", "chambre salle de bain");
        Tache tache4 = new TacheFille("Passer l'aspirateur au rez de chaussée", "cuisine salon");

        tache1.ajouterSousTache(tache2);
        tache2.ajouterSousTache(tache3);
        tache2.ajouterSousTache(tache4);

        List<Tache> l = tache1.getSousTaches();

    }

}
