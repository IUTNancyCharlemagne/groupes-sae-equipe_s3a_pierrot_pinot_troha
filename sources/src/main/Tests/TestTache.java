import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tralleno.Taches.Tache;
import tralleno.Taches.TacheFille;
import tralleno.Taches.TacheMere;

import static org.junit.jupiter.api.Assertions.*;

public class TestTache {

    TacheMere faireDevoirs;
    Tache reviserDS;
    Tache reviserTest;
    TacheMere faireExpose;
    Tache preparerDiapo;
    Tache preparerScript;

    @BeforeEach
    public void setUp(){
        //création des tâches
        this.faireDevoirs = new TacheMere("Réviser le Droit", "Faire mes devoirs de droit (réviser pour le ds surprise et faire l'exposé)");
        this.reviserDS = new TacheFille("Réviser DS", "Réviser pour le DS de Maths de Mercredi 20 décembre");
        this.reviserTest = new TacheFille("Reviser Test Droit", "Réviser le test de droit pour Lundi 18 décembre");
        this.faireExpose = new TacheMere("Faire Exposé Droit", "Préparer Exposé de Droit sur les GNU/GPL pour le mercredi 20 décembre");
        this.preparerDiapo = new TacheFille("Préparer Diapo Exposé Droit", "Préparer le Diapo pour l'exposé de Droit sur les GNU/GPL");
        this.preparerScript = new TacheFille("Préparer Script Exposé Droit", "Préparer le Script pour l'exposé de Droit les GNU/GPL");

        //création des dépendances entre les tâches
        this.faireDevoirs.ajouterSousTache(this.reviserDS);
        //this.faireDevoirs.ajouterSousTache(this.reviserTest); // à ajouter dans les Test avec la méthode setSousTache
        this.faireDevoirs.ajouterSousTache(this.faireExpose);

        this.faireExpose.ajouterSousTache(this.preparerDiapo);
        this.faireExpose.ajouterSousTache(this.preparerScript);
    }


    @Test
    public void test_ID_pareil(){
        assertEquals(this.faireDevoirs.compareTo(this.faireDevoirs), 0);
    }

    @Test
    public void test_ID_Pas_pareil_Un(){
        assertEquals(this.faireDevoirs.compareTo(this.reviserDS), -1);
    }

    @Test
    public void test_ID_Pas_pareil_Deux(){
        assertEquals(this.reviserDS.compareTo(this.faireDevoirs), 1);
    }

    @Test
    public void test_toString(){
        String resAttendu = "Titre : " + this.reviserDS.getTitre() + "\n" +
                            "ID : " + this.reviserDS.getId() + "\n" +
                            "dateDebut : " + this.reviserDS.getDateDebut() + "\n" +
                            "dateFin : " + this.reviserDS.getDateFin() + "\n" +
                            "duree : " + this.reviserDS.getDuree() + "\n" +
                            "Description : " + this.reviserDS.getDescription() + "\n";
        String resActuel = this.reviserDS.toString();

        assertEquals(resAttendu, resActuel);
    }

    @Test
    public void test_Liste_Sous_Taches(){
        String resAttendu = "[" + this.preparerDiapo.toString() + ", " + this.preparerScript.toString() + "]";
        String resActuel = this.faireExpose.getSousTaches().toString();

        assertEquals(resAttendu, resActuel);
    }

    @Test
    public void test_ajouter_sous_tache(){
        //avant
        int nbAvant = this.faireDevoirs.getSousTaches().size();

        //ajout de la nouvelle sous tâche
        this.faireDevoirs.ajouterSousTache(this.reviserTest);

        //après
        int nbApres = this.faireDevoirs.getSousTaches().size();

        assertEquals(nbApres, nbAvant + 1);
    }

    @Test
    public void test_supprimer_sous_tache(){
        //avant
        int nbAvant = this.faireDevoirs.getSousTaches().size();

        //suppression d'une sous tâche
        this.faireDevoirs.supprimerSousTache(this.faireExpose);

        //après
        int nbApres = this.faireDevoirs.getSousTaches().size();

        assertEquals(nbApres, nbAvant - 1);
    }

    @Test
    public void test_supprimer_sous_tache_inexistante(){
        //avant
        int nbAvant = this.faireDevoirs.getSousTaches().size();

        //suppression d'une sous tâche qui n'est pas dans la liste des sous tâches
        this.faireDevoirs.supprimerSousTache(this.reviserTest);

        //après
        int nbApres = this.faireDevoirs.getSousTaches().size();

        assertEquals(nbApres, nbAvant);
    }
}
