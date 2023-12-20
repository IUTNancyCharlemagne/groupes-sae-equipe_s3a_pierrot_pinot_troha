import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tralleno.Modele.ModeleBureau;
import tralleno.Section.Section;
import tralleno.Taches.TacheFille;

import static org.junit.jupiter.api.Assertions.*;

public class TestModeleBureau {

    ModeleBureau bureau;
    Section maths, sae, vide;
    TacheFille reviser1, reviser2, reviser3;

    @BeforeEach
    public void setUp(){
        //initialisation du bureau
        this.bureau = new ModeleBureau();

        //initialisation des sections
        this.maths = new Section("Maths");
        this.sae = new Section("SAE");
        this.vide = new Section("Vide");

        //initialisation des tâches
        this.reviser1 = new TacheFille("à réviser en premier", "relire le cours");
        this.reviser2 = new TacheFille("à reviser en deuxième", "refaire les exercices");
        this.reviser3 = new TacheFille("réviser le JavaFX", "réviser le javaFX pour comprendre son utilisation dans le projet trello");

        //ajout des tâches dans les sections
        this.maths.ajouterTache(this.reviser1);
        this.maths.ajouterTache(this.reviser2);
        this.sae.ajouterTache(this.reviser3);

        //ajout des sections dans le bureau
        this.bureau.ajouterSection(this.maths);
        this.bureau.ajouterSection(this.sae);
        this.bureau.ajouterSection(this.vide);

        //ajout de dépendances entre des tâches
        this.bureau.ajouterDependance(this.reviser2, this.reviser1);
    }

    @Test
    public void test_ajouterSection(){
        //préparation du test
        int nbSectionsAvant = this.bureau.getSections().size();
        Section peinture = new Section("Peinture");

        //test
        this.bureau.ajouterSection(peinture);

        //vérification
        int nbSectionsApres = this.bureau.getSections().size();
        assertEquals(nbSectionsAvant+1, nbSectionsApres);
    }

    @Test
    public void test_ajouterTache(){
        //préparation du test
        int nbTachesAvant = this.bureau.getTaches().size();
        TacheFille devApp = new TacheFille("Dev d'app", "Faire un trello");

        //test
        this.bureau.ajouterTache(devApp, this.sae);

        //vérification
        int nbTachesApres = this.bureau.getTaches().size();
        assertEquals(nbTachesAvant+1, nbTachesApres);
    }

    @Test
    public void test_getSection_nomOk(){
        assertEquals(this.maths, this.bureau.getSection("Maths"));
    }

    @Test
    public void test_getSection_mauvaisNom(){
        assertNull(this.bureau.getSection("Mauvais nom"));
    }

    @Test
    public void test_supprimer_Tache_sans_dependance(){
        //vérification globale et dans la section
        //préparation du test
        int nbTachesAvant = this.bureau.getTaches().size();
        int nbTachesAvantDansLaSection = this.bureau.getSection("SAE").getTaches().size();

        //test
        this.bureau.supprimerTache(this.reviser3);

        //vérification
        int nbTachesApres = this.bureau.getTaches().size();
        int nbTachesApresDansLaSection = this.bureau.getSection("SAE").getTaches().size();
        assertEquals(nbTachesAvant-1, nbTachesApres);
        assertEquals(nbTachesAvantDansLaSection-1, nbTachesApresDansLaSection);
    }

    @Test
    public void test_supprimer_Tache_avec_dependance(){
        //préparartion du test
        int nbDeDepAvant = this.bureau.getDependances().size();

        //test
        this.bureau.supprimerTache(this.reviser1);

        //vérification
        int nbDeDepApres = this.bureau.getDependances().size();
        System.out.println(this.bureau.getDependances());
        assertEquals(1, nbDeDepAvant);
        assertEquals(0, nbDeDepApres);
    }

    @Test
    public void test_supprimer_Section_vide(){
        //préparation du test
        int nbSectionsAvant = this.bureau.getSections().size();

        //test
        this.bureau.supprimerSection(this.vide);

        //vérification
        int nbSectionsApres = this.bureau.getSections().size();
        assertEquals(nbSectionsAvant-1, nbSectionsApres);
    }

    @Test
    public void test_supprimer_Section_pasVide(){
        //initialisation du test
        int nbSectionsAvant = this.bureau.getSections().size();
        int nbTachesAvant = this.bureau.getTaches().size();
        int nbDeDepAvant = this.bureau.getDependances().size();

        //test
        this.bureau.supprimerSection(this.maths);

        //vérification
        int nbSectionsApres = this.bureau.getSections().size();
        int nbTachesApres = this.bureau.getTaches().size();
        int nbDeDepApres = this.bureau.getDependances().size();

        assertEquals(nbSectionsAvant-1, nbSectionsApres);
        assertEquals(nbTachesAvant-2, nbTachesApres);
        assertEquals(nbDeDepAvant-1, nbDeDepApres);
    }

    @Test
    public void test_getNomSections(){
        assertEquals("Maths", this.bureau.getNomSections().get(0));
        assertEquals("SAE", this.bureau.getNomSections().get(1));
    }

    @Test
    public void test_ajouterDependance(){
        //préparation
        int nbDeDepAvant = this.bureau.getDependances().size();

        //test
        this.bureau.ajouterDependance(this.reviser1, this.reviser2);

        //vérification
        int nbDeDepApres = this.bureau.getDependances().size();
        assertEquals(nbDeDepAvant+1, nbDeDepApres);
    }
}
