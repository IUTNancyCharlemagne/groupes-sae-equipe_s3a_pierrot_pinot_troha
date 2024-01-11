import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tralleno.Modele.ModeleBureau;
import tralleno.Section.Section;
import tralleno.Taches.TacheFille;

import static org.junit.jupiter.api.Assertions.*;

public class TestModeleBureau {

    ModeleBureau bureau;
    Section maths, sae, vide;
    TacheFille reviser1, reviser2, reviser3, reviser4, reviser5;

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
        this.reviser5 = new TacheFille("multiple dépendance", "autre dépendance");

        //ajout des tâches dans les sections
        this.maths.ajouterTache(this.reviser1);
        this.maths.ajouterTache(this.reviser2);
        this.maths.ajouterTache(this.reviser5);
        this.sae.ajouterTache(this.reviser3);

        //ajout des sections dans le bureau
        this.bureau.ajouterSection(this.maths);
        this.bureau.ajouterSection(this.sae);
        this.bureau.ajouterSection(this.vide);

        //ajout de dépendances entre des tâches
        this.bureau.setTacheCourante(this.reviser2);
        this.bureau.ajouterDependance(this.reviser1);
        this.bureau.setTacheCourante(this.reviser5);
        this.bureau.ajouterDependance(this.reviser1);
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
        this.bureau.setTacheCourante(devApp);
        //test
        this.bureau.setSectionCourante(this.sae);
        this.bureau.ajouterTache(0);

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
        this.bureau.setTacheCourante(this.reviser3);
        this.bureau.supprimerTache();

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
        this.bureau.setTacheCourante(this.reviser1);
        this.bureau.supprimerTache();

        //vérification
        int nbDeDepApres = this.bureau.getDependances().size();
        assertEquals(2, nbDeDepAvant);
        assertEquals(0, nbDeDepApres);
    }

    @Test
    public void test_supprimer_Section_vide(){
        //préparation du test
        int nbSectionsAvant = this.bureau.getSections().size();
        this.bureau.setSectionCourante(this.vide);
        //test
        this.bureau.supprimerSection();

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
        this.bureau.setSectionCourante(this.maths);
        //test
        this.bureau.supprimerSection();

        //vérification
        int nbSectionsApres = this.bureau.getSections().size();
        int nbTachesApres = this.bureau.getTaches().size();
        int nbDeDepApres = this.bureau.getDependances().size();

        assertEquals(nbSectionsAvant-1, nbSectionsApres);
        assertEquals(nbTachesAvant-3, nbTachesApres);
        assertEquals(nbDeDepAvant-2, nbDeDepApres);
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
        this.bureau.setTacheCourante(this.reviser1);
        this.bureau.ajouterDependance(this.reviser2);

        //vérification
        int nbDeDepApres = this.bureau.getDependances().size();
        assertEquals(nbDeDepAvant+1, nbDeDepApres);
    }
    @Test
    public void test_archiverTacheSansDependences(){
        this.bureau.setTacheCourante(this.reviser3);
        this.bureau.archiverTache();
        assertFalse(this.bureau.getTaches().contains(this.reviser3),"la tache reviser3 ne devrait plus être dans les sections");
        assertTrue(this.bureau.getTachesArchivees().contains(this.reviser3),"la tache reviser3 devrait être dans les taches archivés");
    }

    @Test
    public void test_archiverTachesAvecDependences(){
        this.bureau.setTacheCourante(this.reviser2);
        this.bureau.archiverTache();
        assertFalse(this.bureau.getTaches().contains(this.reviser2),"la tache reviser2 ne devrait plus être dans les sections");
        assertTrue(this.bureau.getTachesArchivees().contains(this.reviser2),"la tache reviser2 devrait être dans les taches archivés");
        assertTrue(this.bureau.getDependances().containsKey(this.reviser2),"la tache reviser2 doit toujours être dans dependences");
    }

    @Test
    public void test_archiverSectionSansTache(){
        this.bureau.setSectionCourante(this.vide);
       this.bureau.archiverSection();
       assertFalse(this.bureau.getSections().contains(this.vide),"la section vide ne doit plus être presente dans la liste de section");
       assertTrue(this.bureau.getSectionsArchivees().contains(this.vide),"la section vide doit être dans la liste de section archivés");
    }
    @Test
    public void test_archiverSectionAvecTache(){
        this.bureau.setSectionCourante(this.maths);
        this.bureau.archiverSection();
        assertFalse(this.bureau.getSections().contains(this.maths),"la section sae ne doit plus être presente dans la liste des sections");
        assertTrue(this.bureau.getSectionsArchivees().contains(this.maths),"la section sae doit être dans les sections archivés");
        assertTrue(this.maths.getTaches().isEmpty(),"la section doit être vide de tache");
    }
}
