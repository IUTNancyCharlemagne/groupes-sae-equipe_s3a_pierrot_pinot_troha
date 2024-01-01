package tralleno.Taches;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui représente une tâche pouvant comporter des sous-tâches etc...
 */
public class TacheMere extends Tache implements Serializable {

    /**
     * Liste des sous-tâches de la tacheMere (qui peuvent aussi être des TacheMere etc...)
     */
    protected List<Tache> sousTaches;

    /**
     * Construit une tâche à partir d'un titre et d'une description
     * @param t titre de la tâche
     * @param d description de la tâche
     */
    public TacheMere(String t,String d){
        super(t, d);
        this.sousTaches = new ArrayList<Tache>();
    }

    /**
     * Construit une tâche à partir d'un titre  d'une description et de dates de début et de fin
     * @param t
     * @param d
     * @param dD
     * @param dF
     */
    public TacheMere(String t, String d, LocalDate dD, LocalDate dF){
        super(t, d, dD, dF);
        this.sousTaches = new ArrayList<Tache>();
    }

    /**
     * Crée une tâche mère par copie de la tâcheMere passée en paramètres
     * @param t
     */
    public TacheMere(TacheMere t) {
        super(t.getTitre(), t.getDescription(), t.getDateDebut(), t.getDateFin());
        this.id = t.getId();
        this.sousTaches = t.getSousTaches();
    }

    /**
     * Permet d'ajouter une sous tâche à l'instance de Tâche qui appelle la méthode
     * @param t
     */
    public void ajouterSousTache(Tache t){

        t.setSectionParente(this.getSectionParente());
        this.sousTaches.add(t);
    }


    /**
     * Permet de supprimer une sous tâche de l'instance de Tâche qui appelle la méthode
     * @param t
     */
    public void supprimerSousTache(Tache t){
        this.sousTaches.remove(t);
    }

    /**
     * Retourne la liste de sous-tâches de la tâche mère
     * @return
     */
    public List<Tache> getSousTaches(){
        return this.sousTaches;
    }

    /**
     * Permet de changer la liste de sous-tâches de la tâche-mère
     * @param l
     */
    public void setSousTaches(List<Tache> l){
        this.sousTaches = l;
    }


}
