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
     * Map contenant en clé les Tâches archivées et en valeur ses dépendances (tâches à faire avant de pouvoir la commencer)
     */
    private Map<Tache, List<Tache>> tachesArchivees;

    /**
     * Map contenant en clé les Sections archivées et en valeur les tâches qui se trouvaient dans la section
     */
    private Map<Section, List<Tache>> sectionsArchivees;

    public ModeleBureau(){
        this.observateurs = new ArrayList<Observateur>();
        this.dependances = new TreeMap<Tache, List<Tache>>();
        this.sections = new ArrayList<Section>();
    }

    /**
     * Permet d'ajouter une section au modèle
     * @param s section à ajouter
     */
    public void ajouterSection(Section s){
        this.sections.add(s);
        this.notifierObservateurs();
    }

    public void ajouterTache(Tache t, Section s){
        for (Section section : this.sections) {
            if (section.equals(s)) {
                section.ajouterTache(t);
                break;
            }
        }
        this.notifierObservateurs();
    }


    public Section getSection(String nom){
        int i = 0;
        boolean trouve = false;
        while(i < this.sections.size() && !trouve){
           if(this.sections.get(i).getNom().equals(nom))
               trouve = true;
           else{
               i++;
           }
        }
        Section s = null;
        if(trouve){
            s = this.sections.get(i);
        }
        return s;
    }

    /**
     * Retourne la section dans laquelle est contenue une tâche
     * @param t
     * @return
     */
    public Section getSection(Tache t){
        Section section = null;
        for(Section s : this.sections){
            if(s.getTaches().contains(t)){
                section = s;
                break;
            }
        }
        return section;
    }

    public List<String> getNomSections() {
        List<String> res = new ArrayList<String>();
        for(Section s : this.sections){
            res.add(s.getNom());
        }
        return res;
    }

    public void ajouterDependance(Tache t, Tache dependance){
        if(dependance != null){
            if(this.dependances.containsKey(t)){ // Si la map de dépendances contient déjà la tâche en entrée
                // Alors on ajoute la tâche dépendance à la liste des tâches dépendantes
                this.dependances.get(t).add(dependance);
            }else{
                // Sinon la tâche n'a encore aucune dépendance et il faudra aussi initialiser la liste
                this.dependances.put(t, new ArrayList<>());
                this.dependances.get(t).add(dependance);
            }
        }
        this.notifierObservateurs();
    }

    public void ajouterDependances(Tache t, List<Tache> dependances){
        if(dependances != null && !(dependances.isEmpty())){
            if(this.dependances.containsKey(t)){ // Si la map de dépendances contient déjà la tâche en entrée
                // Alors on ajoute la tâche dépendance à la liste des tâches dépendantes
                this.dependances.get(t).addAll(dependances);
            }else{
                // Sinon la tâche n'a encore aucune dépendance et il faudra aussi initialiser la liste
                this.dependances.put(t, new ArrayList<Tache>());

                this.dependances.get(t).addAll(dependances);
            }
        }
        this.notifierObservateurs();
    }

    public void modifierNomSection(Section s, String nom){
        for(Section section : this.sections){
            if (section == s) {
                section.setNom(nom);
                break;
            }
        }
        this.notifierObservateurs();
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Tache> getTaches(){
        List<Tache> res = new ArrayList<Tache>();
        for(Section s : this.sections){
            res.addAll(s.getTaches());
        }
        return res;
    }

    public List<String> getTitreTaches(){
        List<String> res = new ArrayList<String>();
        for(Tache t : this.getTaches()) {
            res.add(t.getTitre());
        }
        return res;
    }

    public Map<Tache, List<Tache>> getDependances() {
        return this.dependances != null ? this.dependances : null;
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
     * @param s section à supprimer
     */
    public void supprimerSection(Section s){
        //copie de la liste des tâches pour pouvoir la parcourir correctement
        List<Tache> listeTacheCopie = new ArrayList<>(s.getTaches());

        // on supprime toutes les tâches et leurs dépendances avant de supprimer la section elle même
        // (pour éviter tout problème avec les dépendances)
        for(Tache t : listeTacheCopie){
            supprimerTache(t);
        }

        this.sections.remove(s);

        //on notifie tous les observateurs de la mise à jour
        this.notifierObservateurs();
    }

    /**
     * Méthode qui permet de supprimer une tâche, cela a pour conséquence de supprimer
     * toutes les dépendances chronologiques qu'elle pourrait avoir avec des autres tâches
     *
     * @param t tâche à supprimer
     */
    public void supprimerTache(Tache t){
        Set<Tache> listeTachesQuiOntDesDependances = this.dependances.keySet();
        //on stock les tâches qui n'ont plus de dépendances dans cette liste pour les supprimer après le parcourt de
        // la liste de tâches qui ont des dépendances sinon ça pose problème
        ArrayList<Tache> tachesARetirer = new ArrayList<Tache>();

        // on parcourt la liste de toutes les tâches qui ont des dépendances
        for(Tache tache : listeTachesQuiOntDesDependances){
            //on récupère la liste des dépendances de la tâche courante
            List<Tache> listeDesDependances = this.dependances.get(tache);

            // on regarde si la liste des dépendances contient la tâche à supprimer
            if(listeDesDependances.contains(t)){
                //on retire la dépendance de la tâche qu'on veut supprimer
                this.dependances.get(tache).remove(t);

                //si une tâche n'a plus de dépendances alors on la met dans une liste pour la supprimer plus tard
                if(this.dependances.get(tache).isEmpty()){
                    tachesARetirer.add(tache);
                }
            }
        }

        //on retire toutes les tâches qui n'ont plus de dépendances de la map dependances
        for(Tache tache : tachesARetirer){
                this.dependances.remove(tache);
        }

        //on retire les dépendances de la tâche à supprimer
        this.dependances.remove(t);

        //on retire la tâche de la liste de tâche de sa section
        for(Section section : this.sections){
            if(section.getTaches().contains(t)){
                section.supprimerTache(t);
            }
        }
        // on notifie tous les observateurs de la mise à jour
        this.notifierObservateurs();
    }

    /**
     * Méthode qui permet d'archiver une tâche avec toutes ses dépendances
     *
     * @param t tâche à archiver
     */
    public void archiverTache(Tache t){
        Set<Tache> listeTachesQuiOntDesDependances = this.dependances.keySet();
        // on stock les tâches qui n'ont plus de dépendances dans cette liste pour les supprimer après le parcourt de
        // la liste de tâches qui ont des dépendances sinon ça pose problème
        ArrayList<Tache> tachesARetirer = new ArrayList<Tache>();

        // on parcourt la liste de toutes les tâches qui ont des dépendances
        for(Tache tache : listeTachesQuiOntDesDependances){
            //on récupère la liste des dépendances de la tâche courante
            List<Tache> listeDesDependances = this.dependances.get(tache);

            // on regarde si la liste des dépendances contient la tâche à archiver
            if(listeDesDependances.contains(t)){
                if(this.tachesArchivees.containsKey(t)){ // Si la map des tâches archivées contient déjà la tâche en entrée
                    // Alors on ajoute la tâche à la liste des tâches dépendantes archivées
                    this.tachesArchivees.get(t).add(tache);
                    // et on retire la tâche des dépendances
                    this.dependances.get(tache).remove(t);
                }else{
                    // Sinon la tâche n'a encore aucune dépendance archivée et il faut aussi initialiser la liste
                    this.tachesArchivees.put(t, new ArrayList<Tache>());
                    this.tachesArchivees.get(t).add(tache);
                    // et on retire la tâche des dépendances
                    this.dependances.get(tache).remove(t);
                }

                //si une tâche n'a plus de dépendances alors on la met dans une liste pour la supprimer plus tard
                if(this.dependances.get(tache).isEmpty()){
                    tachesARetirer.add(tache);
                }
            }
        }
        //on retire toutes les tâches qui n'ont plus de dépendances de la map dependances
        for(Tache tache : tachesARetirer){
            this.dependances.remove(tache);
        }
        // on parcourt la liste des sections jusqu'à trouver la section de la tâche à archiver
        for(Section section : this.sections){
            if(section.getTaches().contains(t)){
                section.getTaches().remove(t);
            }
        }

        //on notifie les observateurs de la modification
        this.notifierObservateurs();
    }

    /**
     * Méthode qui permet d'archiver une section en entier avec toutes ses tâches
     *
     * @param s section à archiver
     */
    public void archiverSection(Section s){
        //on archive la section avec toutes ses tâches
        this.sectionsArchivees.put(s, new ArrayList<Tache>());

        //copie de la liste des tâches pour pouvoir la parcourir correctement
        List<Tache> listeTacheCopie = new ArrayList<>(s.getTaches());

        // on archive toutes les tâches et leurs dépendances avant d'archiver la section elle même
        // (pour éviter tout problème avec les dépendances)
        for(Tache t : listeTacheCopie){
            archiverTache(t);
            this.sectionsArchivees.get(s).add(t);
        }

        // on retire la section des sections non archivées
        this.sections.remove(s);

        //on notifie les observateurs de la modification
        this.notifierObservateurs();
    }

    public void restaurerTache(Tache t){
        //TODO
    }

    public void restaurerSection(Section s){
        //TODO
    }

    public List<Observateur> getObservateurs() {
        return observateurs;
    }

    public void setObservateurs(List<Observateur> observateurs) {
        this.observateurs = observateurs;
    }

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
