package tralleno.Section;

import tralleno.Modele.ModeleBureau;
import tralleno.Taches.Tache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Sorte de conteneur qui contient des tâches et sous tâches
 */
public class Section implements Serializable {

    /**
     * Liste des tâches contenues dans une section. Le patron composite mis en place pour les tâches permet qu'une section
     * stocke tâches et sous tâches etc...
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
     * @param n
     */
    public Section(String n){
        this.nom = n;
        this.taches = new ArrayList<Tache>();
    }


    /**
     * Construit une section à partir de son nom et d'une liste de tâches
     * @param n
     * @param l
     */
    public Section(String n, List<Tache> l){
        this.nom = n;
        this.taches = l;
        this.id= ModeleBureau.getIDSECTIONACTUELLE();
    }

    /**
     * Ajoute une tâche à la section
     * @param t
     */
    public void ajouterTache(Tache t){
        this.taches.add(t);
        t.setSectionParente(this);
    }

    /**
     * Supprime une tâche de la section
     * @param t
     */
    public void supprimerTache(Tache t){
        this.taches.remove(t);
    }




    public List<Tache> getTaches() {
        return taches;
    }

    public void setTaches(List<Tache> taches) {
        this.taches = taches;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        return this.getNom();
    }

}
