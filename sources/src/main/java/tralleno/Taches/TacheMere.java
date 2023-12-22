package tralleno.Taches;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TacheMere extends Tache implements Serializable {

    /**
     * Liste des sous-tâches de l'instance de tâche
     */
    protected List<Tache> sousTaches;

    /**
     *
     * @param t titre de la tâche
     * @param d description de la tâche
     */
    public TacheMere(String t,String d){
        super(t, d);
        this.sousTaches = new ArrayList<Tache>();
    }

    /**
     * les paramètres doivent êtres vérifiés avant
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
     *
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

    public List<Tache> getSousTaches(){
        return this.sousTaches;
    }

    public void setSousTaches(List<Tache> l){
        this.sousTaches = l;
    }


}
