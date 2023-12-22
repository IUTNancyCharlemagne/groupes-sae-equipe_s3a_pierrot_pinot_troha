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

    public Section getSectionCourante() {
        return sectionCourante;
    }

    public void setSectionCourante(Section sectionCourante) {
        this.sectionCourante = sectionCourante;
    }

    public void setTachesArchivees(List<Tache> tachesArchivees) {
        this.tachesArchivees = tachesArchivees;
    }

    public void setSectionsArchivees(List<Section> sectionsArchivees) {
        this.sectionsArchivees = sectionsArchivees;
    }

    /**
     * Section courante sélectionnée par l'utilisateur
     */
    private Section sectionCourante;

    /**
     * nombre d'id de tache total, sert à avoir une id unique pour chaque tache
     */
    private static int IDTACHEACTUELLE = 0;

    /**
     * nombre d'id de section total, sert à avoir une id unique pour chaque section
     */
    private static int IDSECTIONACTUELLE = 0;

    /**
     * Map contenant en clé les Tâches archivées et en valeur ses dépendances (tâches à faire avant de pouvoir la commencer)
     */
    private List<Tache> tachesArchivees;

    public List<Tache> getTachesArchivees() {
        return (this.tachesArchivees);
    }

    /**
     * Map contenant en clé les Sections archivées et en valeur les tâches qui se trouvaient dans la section
     */
    private List<Section> sectionsArchivees;

    public ModeleBureau() {
        this.observateurs = new ArrayList<Observateur>();
        this.dependances = new TreeMap<Tache, List<Tache>>();
        this.sections = new ArrayList<Section>();
        this.tachesArchivees = new ArrayList<Tache>();
        this.sectionsArchivees = new ArrayList<Section>();
    }

    /**
     * Permet d'ajouter une section au modèle
     *
     * @param s section à ajouter
     */
    public void ajouterSection(Section s) {
        this.sections.add(s);
        this.notifierObservateurs();
    }

    public void ajouterTache(Section s) {
        for (Section section : this.sections) {
            if (section.equals(s)) {
                section.ajouterTache(this.tacheCourante);
                this.tacheCourante.setSectionParente(s);
                break;
            }
        }
        this.notifierObservateurs();
    }


    public Section getSection(String nom) {
        int i = 0;
        boolean trouve = false;
        while (i < this.sections.size() && !trouve) {
            if (this.sections.get(i).getNom().equals(nom))
                trouve = true;
            else {
                i++;
            }
        }
        Section s = null;
        if (trouve) {
            s = this.sections.get(i);
        }
        return s;
    }


    public List<String> getNomSections() {
        List<String> res = new ArrayList<String>();
        for (Section s : this.sections) {
            res.add(s.getNom());
        }
        return res;
    }

    public void ajouterDependance(Tache dependance) {
        if (dependance != null) {
            if (this.dependances.containsKey(this.tacheCourante)) { // Si la map de dépendances contient déjà la tâche en entrée
                // Alors on ajoute la tâche dépendance à la liste des tâches dépendantes
                this.dependances.get(this.tacheCourante).add(dependance);
            } else {
                // Sinon la tâche n'a encore aucune dépendance et il faudra aussi initialiser la liste
                this.dependances.put(this.tacheCourante, new ArrayList<>());
                this.dependances.get(this.tacheCourante).add(dependance);
            }
        }
        this.notifierObservateurs();
    }

    public void ajouterDependances(List<Tache> dependances) {
        if (dependances != null && !(dependances.isEmpty())) {
            if (this.dependances.containsKey(this.tacheCourante)) { // Si la map de dépendances contient déjà la tâche en entrée
                // Alors on ajoute la tâche dépendance à la liste des tâches dépendantes
                this.dependances.get(this.tacheCourante).addAll(dependances);
            } else {
                // Sinon la tâche n'a encore aucune dépendance et il faudra aussi initialiser la liste
                this.dependances.put(this.tacheCourante, new ArrayList<Tache>());

                this.dependances.get(this.tacheCourante).addAll(dependances);
            }
        }
        this.notifierObservateurs();
    }

    public void modifierNomSection(Section s, String nom) {
        for (Section section : this.sections) {
            if (section == s) {
                section.setNom(nom);
                break;
            }
        }
        this.notifierObservateurs();
    }

    public void changerSection(Section section) {
        if (!(this.tacheCourante.getSectionParente() == section)) {
            this.tacheCourante.getSectionParente().supprimerTache(this.tacheCourante); // On supprime la tâche de la section dans laquelle est est actuellement
            // Puis on l'ajoute à la nouvelle section
            this.ajouterTache(section);
            this.tacheCourante.setSectionParente(section);
        }
    }

    /**
     * Retourne la liste des tâches dépendantes chronologiquement de la tâcheCourante
     *
     * @return
     */
    public List<Tache> getDependancesTache() {
        List<Tache> l = new ArrayList<>();
        if (this.dependances.containsKey(this.tacheCourante)) {
            return this.dependances.get(this.tacheCourante);
        }
        return l;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Tache> getTaches() {
        List<Tache> res = new ArrayList<Tache>();
        for (Section s : this.sections) {
            res.addAll(s.getTaches());
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
     * Méthode qui permet de déterminer si une tâche est archivée ou non
     *
     * @param t tâche pour laquelle on veut connaitre sont etat
     * @return true si la tâche est archivée et false sinon
     */
    public boolean isArchivee_Tache(Tache t) {
        return this.tachesArchivees != null && (this.tachesArchivees.contains(t));
    }

    /**
     * Méthode qui permet de déterminer si une section est archivée ou non
     *
     * @param s section pour laquelle on veut connaitre sont etat
     * @return true si la section est archivée et false sinon
     */
    public boolean isArchivee_Section(Section s) {
        return this.sectionsArchivees != null && (this.sectionsArchivees.contains(s));
    }

    public boolean isSupprimee_Section(Section s) {
        return this.sections.contains(s) || this.sectionsArchivees.contains(s);
    }

    /**
     * Permet de supprimer une section du modèle
     */
    public void supprimerSection() {
        //on regarde si la section est archivée ou pas
        if (isArchivee_Section(this.sectionCourante)) { //si elle est archivée, alors on supprime juste la section
            this.sectionsArchivees.remove(this.sectionCourante);
        } else { //si la section n'est pas archivée, on supprime la section et toutes ses tâches
            //copie de la liste des tâches pour pouvoir la parcourir correctement
            List<Tache> listeTacheCopie = new ArrayList<>(this.sectionCourante.getTaches());

            // on supprime toutes les tâches et leurs dépendances avant de supprimer la section elle même
            // (pour éviter tout problème avec les dépendances)
            for (Tache t : listeTacheCopie) {
                this.tacheCourante = t;
                this.supprimerTache();

            }
            this.sections.remove(this.sectionCourante);
            //on notifie tous les observateurs de la mise à jour
            this.notifierObservateurs();
        }
    }

        /**
         * Méthode qui permet de supprimer une tâche, cela a pour conséquence de supprimer
         * toutes les dépendances chronologiques qu'elle pourrait avoir avec des autres tâches
         *
         */
        public void supprimerTache () {
            Set<Tache> listeTachesQuiOntDesDependances = this.dependances.keySet();
            //on stock les tâches qui n'ont plus de dépendances dans cette liste pour les supprimer après le parcourt de
            // la liste de tâches qui ont des dépendances sinon ça pose problème
            ArrayList<Tache> tachesARetirer = new ArrayList<Tache>();

            // on parcourt la liste de toutes les tâches qui ont des dépendances
            for (Tache tache : listeTachesQuiOntDesDependances) {
                //on récupère la liste des dépendances de la tâche courante
                List<Tache> listeDesDependances = this.dependances.get(tache);

                // on regarde si la liste des dépendances contient la tâche à supprimer
                if (listeDesDependances.contains(this.tacheCourante)) {
                    //on retire la dépendance de la tâche qu'on veut supprimer
                    this.dependances.get(tache).remove(this.tacheCourante);

                    //si une tâche n'a plus de dépendances alors on la met dans une liste pour la supprimer plus tard
                    if (this.dependances.get(tache).isEmpty()) {
                        tachesARetirer.add(tache);
                    }
                }
            }

            //on retire toutes les tâches qui n'ont plus de dépendances de la map dependances
            for (Tache tache : tachesARetirer) {
                this.dependances.remove(tache);
            }

            //on retire les dépendances de la tâche à supprimer
            this.dependances.remove(this.tacheCourante);

            //on regarde si la section de la tâche est archivée ou si la tâche est archivée ou non
            if (isArchivee_Section(this.tacheCourante.getSectionParente())) { //si la section est archivée
                this.tachesArchivees.remove(this.tacheCourante);
            } else { //si la section n'est pas archivée alors la tâche peut être archivée ou pas
                if (isArchivee_Tache(this.tacheCourante)) {
                    this.tachesArchivees.remove(this.tacheCourante);
                } else {
                    for (Section section : this.sections) {
                        if (section.getTaches().contains(this.tacheCourante)) {
                            section.supprimerTache(this.tacheCourante);
                        }
                    }
                }
            }
            // on notifie tous les observateurs de la mise à jour
            this.notifierObservateurs();
        }

        /**
         * Méthode qui permet d'archiver une tâche avec toutes ses dépendances
         *
         */
        public void archiverTache () {
            // on parcourt la liste des sections jusqu'à trouver la section de la tâche à archiver
            for (Section section : this.sections) {
                if (section.getTaches().contains(this.tacheCourante)) {
                    this.tachesArchivees.add(this.tacheCourante);
                    section.getTaches().remove(this.tacheCourante);
                }
            }

            //on notifie les observateurs de la modification
            this.notifierObservateurs();
        }

        /**
         * Méthode qui permet d'archiver une section en entier avec toutes ses tâches
         *
         */
        public void archiverSection () {
            //on archive la section avec toutes ses tâches
            this.sectionsArchivees.add(this.sectionCourante);

            //copie de la liste des tâches pour pouvoir la parcourir correctement
            List<Tache> listeTacheCopie = new ArrayList<>(this.sectionCourante.getTaches());

            // on archive toutes les tâches et leurs dépendances avant d'archiver la section elle même
            // (pour éviter tout problème avec les dépendances)
            for (Tache t : listeTacheCopie) {
                this.tacheCourante = t;
                this.archiverTache();
            }

            // on retire la section des sections non archivées
            this.sections.remove(this.sectionCourante);

            //on notifie les observateurs de la modification
            this.notifierObservateurs();
        }

        /**
         * Méthode qui permet d'ajouter une tâche dans une section donnée
         *
         * @param t tâche à ajouter
         * @param s section dans laquelle ajouter la tâche
         */
        public void ajouterTacheDansSection (Tache t, Section s){
            //on parcourt la liste des sections jusqu'à trouver la bonne et ajouter la tâche
            for (Section section : this.sections) {
                if (section.equals(s)) {
                    section.getTaches().add(t);
                    break;
                }
            }
        }

        /**
         * Méthode qui permet de restaurer une tâche dans la section dans laquelle elle se trouvait
         *
         * @param t tâche à restaurer
         */
        public void restaurerTache (Tache t){
            Section sectionParente = t.getSectionParente();
            if (!isSupprimee_Section(sectionParente)) {
                if (!isArchivee_Section(sectionParente)) { //si la section est dans son état normal
                    ajouterTacheDansSection(t, sectionParente);
                } else { //si la section est archivée
                    //on restaure la section
                    restaurerSection(sectionParente);
                    ajouterTacheDansSection(t, sectionParente);
                }
            } else { //si la section a été supprimée
                //on recréé la section de la tâche
                this.sections.add(sectionParente);
                ajouterTacheDansSection(t, sectionParente);
            }
        }

        /**
         * Méthode qui permet de restaurer une section
         *
         * @param s section à restaurer
         */public void restaurerSection (Section s){

            this.sectionsArchivees.remove(s);
            this.sections.add(s);
        }

        public List<Observateur> getObservateurs () {
            return observateurs;
        }

        public void setObservateurs (List < Observateur > observateurs) {
            this.observateurs = observateurs;
        }

        /**
         * renvoi un id unique de section et incremente pour la suite
         *
         * @return int id section unique
         */
        public static int getIDSECTIONACTUELLE () {
            return IDSECTIONACTUELLE++;
        }

        /**
         * renvoi un id unique de tâche et incremente pour la suite
         *
         * @return int id tache unique
         */
        public static int getIDTACHEACTUELLE () {
            return IDTACHEACTUELLE++;
        }

        /**
         * Permet d'enregistrer un observateur à la liste
         *
         * @param observateur
         */
        @Override
        public void enregistrerObservateur (Observateur observateur){
            this.observateurs.add(observateur);
        }

        /**
         * Permet de supprimer un observateur de la liste
         *
         * @param observateur
         */
        @Override
        public void supprimerObservateur (Observateur observateur){
            this.observateurs.remove(observateur);
        }

        /**
         * Notifie tous les observateurs du modèle et les actualise
         */
        @Override
        public void notifierObservateurs () {
            for (Observateur o : this.observateurs) {
                o.actualiser(this);
            }
        }

        public List<Section> getSectionsArchivees () {
            return (this.sectionsArchivees);
        }
    }
