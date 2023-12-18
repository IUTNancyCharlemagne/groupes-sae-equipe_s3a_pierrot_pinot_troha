package tralleno.Modele;

import tralleno.Section.Section;
import tralleno.Taches.Tache;
import tralleno.Vues.Observateur;

import java.io.Serializable;
import java.util.*;

/**
 * Modèle de l'application, qui notifie ses observateurs lors de changements d'état
 */
public class ModeleBureau implements Sujet, Serializable {

    /**
     * Liste des observateurs du modèle
     */
    private List<Observateur> observateurs;

    /**
     * Liste des sections que le modele contient
     */
    private List<Section> sections;

    /**
     * Map qui contient en entrée une tâche, et en valeur associée à l'entrée les tâches dont elle dépend (à faire avant de pouvoir la commencer).
     */
    private Map<Tache, List<Tache>> dependances;

    /**
     * Tache courante selectionnée par l'utilisateur
     */
    private Tache tacheCourante;

    /**
     * nombre d'id de tache total, sert à avoir une id unique pour chaque tache
     */
    private static int IDTACHEACTUELLE=0;
    /**
     * nombre d'id de section total, sert à avoir une id unique pour chaque section
     */
    private static int IDSECTIONACTUELLE=0;
    /**
     * renvoi un id unique de section et incremente pour la suite
     * @return int id section unique
     */
    public static int getIDSECTIONACTUELLE() {
        return IDSECTIONACTUELLE++;
    }

    /**
     * renvoi un id unique de tâche et incremente pour la suite
     * @return int id tache unique
     */
    public static int getIDTACHEACTUELLE(){
        return IDTACHEACTUELLE++;
    }

    public ModeleBureau(){
        this.observateurs = new ArrayList<Observateur>();
        this.dependances = new TreeMap<Tache, List<Tache>>();
        this.sections = new ArrayList<Section>();
    }

    /**
     * Permet d'ajouter une section au modèle
     * @param s
     */
    public void ajouterSection(Section s){
        this.sections.add(s);
        this.notifierObservateurs();
    }

    public List<Observateur> getObservateurs() {
        return observateurs;
    }

    public void setObservateurs(List<Observateur> observateurs) {
        this.observateurs = observateurs;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public Map<Tache, List<Tache>> getDependances() {
        return dependances;
    }

    public void setDependances(Map<Tache, List<Tache>> dependances) {
        this.dependances = dependances;
    }

    public Tache getTacheCourante() {
        return tacheCourante;
    }

    public void setTacheCourante(Tache tacheCourante) {
        this.tacheCourante = tacheCourante;
    }

    /**
     * Permet de supprimer une section du modèle
     * @param s
     */
    public void supprimerSection(Section s){
        this.sections.remove(s);
        this.notifierObservateurs();
    }


    /**
     * Permet d'enregistrer un observateur à la liste
     * @param observateur
     */
    @Override
    public void enregistrerObservateur(Observateur observateur) {
        this.observateurs.add(observateur);
    }

    /**
     * Permet de supprimer un observateur de la liste
     * @param observateur
     */
    @Override
    public void supprimerObservateur(Observateur observateur) {
        this.observateurs.remove(observateur);
    }

    /**
     * Notifie tous les observateurs du modèle et les actualise
     */
    @Override
    public void notifierObservateurs() {
        for(Observateur o : this.observateurs){
            o.actualiser(this);
        }
    }
}
