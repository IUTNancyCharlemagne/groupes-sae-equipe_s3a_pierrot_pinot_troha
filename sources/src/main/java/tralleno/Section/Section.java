package tralleno.Section;

import tralleno.Modele.ModeleBureau;
import tralleno.Taches.Tache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Sorte de conteneur qui contient des tâches
 */
public class Section implements Serializable {

    /**
     * Liste des tâches contenues dans une section. Le patron composite mis en place pour les tâches permet qu'une section
     * stocke des tâches, qui elles stockent des sous-tâches etc... Mais les sous-tâches des tâches ne sont pas directement
     * stockées dans cette liste de tâches. Elles sont accessibles car leur taches meres sont dans cette liste
     */
    private List<Tache> taches;

    /**
     * Nom de la section
     */
    private String nom;

    /**
     * Id de la section
     */
    private int id;

    /**
     * Construit une section vide à partir de son nom
     *
     * @param n
     */
    public Section(String n) {
        this.nom = n;
        this.taches = new ArrayList<Tache>();
        this.id = ModeleBureau.getIDSECTIONACTUELLE();
    }


    /**
     * Construit une section à partir de son nom et d'une liste de tâches
     *
     * @param n
     * @param l
     */
    public Section(String n, List<Tache> l) {
        this.nom = n;
        this.taches = l;
        this.id = ModeleBureau.getIDSECTIONACTUELLE();
    }

    /**
     * Ajoute une tâche à la section
     *
     * @param t
     */
    public void ajouterTache(Tache t) {
        this.taches.add(t);
        t.setSectionParente(this);
    }

    /**
     * Supprime une tâche de la section
     *
     * @param t
     */
    public void supprimerTache(Tache t) {
        this.taches.remove(t);
    }

    /**
     * Retourne la liste de tâches que la section possède
     *
     * @return
     */
    public List<Tache> getTaches() {
        return taches;
    }

    /**
     * Permet de changer la liste de tâches que la section possède
     *
     * @param taches
     */
    public void setTaches(List<Tache> taches) {
        this.taches = taches;
    }

    /**
     * Retourne le nom de la section
     */
    public String getNom() {
        return nom;
    }

    /**
     * Permet de modifier le nom de la section
     *
     * @param nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne l'id de la section
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Permet de changer l'id de la section
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retourne le nom de la section
     *
     * @return
     */
    public String toString() {
        return this.getNom();
    }

}
