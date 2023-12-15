import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tralleno.Section.Section;
import tralleno.Taches.Tache;
import tralleno.Taches.TacheFille;
import tralleno.Taches.TacheMere;

import static org.junit.jupiter.api.Assertions.*;

public class TestSection {

    Section devoirs;
    Tache faireLeRapport;
    Tache faireLesTestsJUnit;


    @BeforeEach
    public void setUp(){
        this.devoirs = new Section("Devoirs");

        this.faireLesTestsJUnit = new TacheFille("Test JUnit", "Faire les test JUnit pour la SAE 3.01");
        this.faireLeRapport = new TacheFille("Rapport Iteration 1", "Faire le rapport pour l'itération 1");

        this.devoirs.ajouterTache(this.faireLeRapport);
    }

    @Test
    public void test_ajouter_Tache(){
        //avant
        int nbAvant = this.devoirs.getTaches().size();

        //ajoute d'une nouvelle tâche dans la section
        this.devoirs.ajouterTache(this.faireLesTestsJUnit);

        //apres
        int nbApres = this.devoirs.getTaches().size();

        assertEquals(nbApres, nbAvant + 1);
    }

    @Test
    public void test_supprimer_Tache(){
        //avant
        int nbAvant = this.devoirs.getTaches().size();

        //suppression d'une tâche dans la section
        this.devoirs.supprimerTache(this.faireLeRapport);

        //apres
        int nbApres = this.devoirs.getTaches().size();

        assertEquals(nbApres, nbAvant - 1);
    }
}
