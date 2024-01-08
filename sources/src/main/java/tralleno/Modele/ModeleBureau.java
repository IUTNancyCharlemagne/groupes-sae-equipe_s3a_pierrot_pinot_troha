package tralleno.Modele;

import tralleno.Section.Section;
import tralleno.Taches.Tache;
import tralleno.Taches.TacheMere;
import tralleno.Vues.Observateur;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
     * Section courante sélectionnée par l'utilisateur
     */
    private Section sectionCourante;

    /**
     * Nombre d'id de tache total, sert à avoir une id unique pour chaque tache
     */
    public static int IDTACHEACTUELLE = 0;

    public int getIdtacheactuelle() {
        return idtacheactuelle;
    }

    public void setIdtacheactuelle(int idtacheactuelle) {
        this.idtacheactuelle = idtacheactuelle;
    }

    public int getIdsectionactuelle() {
        return idsectionactuelle;
    }

    public void setIdsectionactuelle(int idsectionactuelle) {
        this.idsectionactuelle = idsectionactuelle;
    }

    private int idtacheactuelle;
    /**
     * Nombre d'id de section total, sert à avoir une id unique pour chaque section
     */
    public static int IDSECTIONACTUELLE = 0;
    private int idsectionactuelle;
    /**
     * Liste contenant les Tâches archivées
     */
    private List<Tache> tachesArchivees;

    /**
     * Liste contenant les sections archivées
     */
    private List<Section> sectionsArchivees;

    /**
     * Construit le modèle en initialisant ses attributs.
     */
    public ModeleBureau() {
        this.observateurs = new ArrayList<Observateur>();
        this.sections = new ArrayList<Section>();
        this.dependances = new TreeMap<Tache, List<Tache>>();
        this.tachesArchivees = new ArrayList<Tache>();
        this.sectionsArchivees = new ArrayList<Section>();
        idsectionactuelle = 0;
        idtacheactuelle = 0;
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

    /**
     * Permet d'ajouter une tâche à la section courante (this.sectionCourante)
     */
    public void ajouterTache() {
        for (Section section : this.sections) {
            if (section.equals(this.sectionCourante)) {
                section.ajouterTache(this.tacheCourante);
                this.tacheCourante.setSectionParente(this.sectionCourante);
                break;
            }
        }
        this.notifierObservateurs();
    }

    /**
     * Permet d'ajouter une tâche en dépendance à la tâche courante.
     * C'est à dire que la tâche courante aura besoin de la tâche passée en paramètres de la méthode pour
     * pouvoir se commencer
     *
     * @param dependance
     */
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

    /**
     * Permet d'ajouter une liste entière de tâches dépendantes à la tâche courante (tâche à faire avant la tâche courante)
     *
     * @param dependances liste des tâches à faire avant la tâche courante
     */
    public void ajouterDependances(List<Tache> dependances) {
        if (dependances != null && !(dependances.isEmpty())) {
            if (this.dependances.containsKey(this.tacheCourante)) { // Si la map de dépendances contient déjà la tâche en entrée
                this.dependances.get(this.tacheCourante).clear();
                // Alors on ajoute la tâche dépendance à la liste des tâches dépendantes
                this.dependances.get(this.tacheCourante).addAll(dependances);
            } else {
                // Sinon la tâche n'a encore aucune dépendance et il faudra aussi initialiser la liste
                this.dependances.put(this.tacheCourante, new ArrayList<Tache>());

                this.dependances.get(this.tacheCourante).addAll(dependances);
            }
        } else {
            if (this.dependances.containsKey(this.tacheCourante)) {
                this.dependances.get(this.tacheCourante).clear();
            }
        }
        this.notifierObservateurs();
    }

    /**
     * Méthode qui retourne l'objet section à partir de son nom
     *
     * @param nom
     * @return
     */
    public Section getSection(String nom) {
        for (Section section : this.sections) {
            if (section.getNom().equals(nom)) {
                return section;
            }
        }
        return null;
    }

    /**
     * Méthode qui retourne la liste de noms des sections du modèle
     *
     * @return
     */
    public List<String> getNomSections() {
        List<String> res = new ArrayList<String>();
        for (Section s : this.sections) {
            res.add(s.getNom());
        }
        return res;
    }

    /**
     * Permet de modifier le nom de la section courante sectionCourante
     *
     * @param nom
     */
    public void modifierNomSection(String nom) {
        this.sectionCourante.setNom(nom);
        this.notifierObservateurs();
    }

    /**
     * Permet de déplacer une tâche (et ses sous-tâches) de la section dans laquelle elle était de base
     * c'est à dire sectionCourante, pour ensuite la déplacer dans la section passée en paramètres,
     * qui devient à son tour la section courante
     *
     * @param section
     */
    public void changerSection(Section section) {
        this.tacheCourante.getSectionParente().supprimerTache(this.tacheCourante); // On supprime la tâche de la section dans laquelle est est actuellement
        // Puis on l'ajoute à la nouvelle section
        this.sectionCourante = section;
        this.ajouterTache();
        this.tacheCourante.setSectionParente(section);

        // Mettre à jour récursivement la section parente pour les sous-tâches
        mettreAJourSousTachesSection(this.tacheCourante, section);

        this.notifierObservateurs();
    }

    /**
     * Méthode récursive utiliser par changerSection(Section section)
     * qui permet de mettre à jour les sous-tâches de la tâche qu'on a déplacée de section.
     * Car les sous-tâches ne sont pas directement contenues dans la liste de tâche d'une section, mais indiquent
     * la section dans laquelle elles se trouvent via leur attribut sectionParente
     *
     * @param tache
     * @param section
     */
    private void mettreAJourSousTachesSection(Tache tache, Section section) {
        if (tache instanceof TacheMere) { // Si elle contient des sous-tâches
            List<Tache> sousTaches = ((TacheMere) tache).getSousTaches();
            for (Tache sousTache : sousTaches) {
                sousTache.setSectionParente(section); // On met à jour sa section parente.
                mettreAJourSousTachesSection(sousTache, section);
            }
        }
    }

    /**
     * Retourne la liste des tâches dont la tâcheCourante dépend chronologiquement
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


    /**
     * Retourne la liste des tâches du modèle.
     * Pour l'instant ne retourne pas les sous-tâches des tâches.
     * A voir du point de vue dépendances chronologiques
     *
     * @return
     */
    public List<Tache> getTaches() {
        List<Tache> res = new ArrayList<Tache>();
        for (Section s : this.sections) {
            res.addAll(s.getTaches());
        }
        return res;
    }

    /**
     * Retourne une tâche selon son id en utilisant une autre méthode récursive, qui descend dans la hiérarchie des tâches et de ses sous-tâches
     *
     * @param id
     * @return
     */
    public Tache getTacheParId(int id) {
        for (Section section : sections) {
            for (Tache tache : section.getTaches()) {
                Tache found = chercherTache(id, tache);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    /**
     * Méthode récursive qui descend jusqu'en bas des sous-tâches d'une tâche
     * pour trouver la tâche qui correspond via l'id passé en paramètres.
     *
     * @param id
     * @param tache
     * @return
     */
    private Tache chercherTache(int id, Tache tache) {
        // Si l'id de la tâche en paramètre est le meme que la tâche on l'a trouvée
        if (tache.getId() == id) {
            return tache;
        }
        if (tache instanceof TacheMere) { // Si la tâche est une tâcheMère on parcourt ses sous-tâches pour répéter l'opération
            TacheMere tacheMere = (TacheMere) tache;
            for (Tache sousTache : tacheMere.getSousTaches()) {
                Tache found = chercherTache(id, sousTache);
                if (found != null) {
                    return found;
                }
            }
        }
        return null; // sinon aucune tâche ne possède cet id
    }

    /**
     * Méthode qui permet d'ajouter la tâche courante dans la section courante
     */
    public void ajouterTacheDansSection() {
        //on parcourt la liste des sections jusqu'à trouver la bonne et ajouter la tâche
        for (Section section : this.sections) {
            if (section.equals(this.sectionCourante)) {
                section.getTaches().add(this.tacheCourante);
                break;
            }
        }
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
     * Permet de supprimer la sectionCourante du modèle. Si la section est archivée cela supprime uniquement la section
     * Si elle ne l'est pas, cela supprimera également ses tâches et sous-tâches.
     */
    public void supprimerSection() {
        //on regarde si la section est archivée ou pas
        if (isArchivee_Section(this.sectionCourante)) { //si elle est archivée, alors on supprime juste la section
            this.sectionsArchivees.remove(this.sectionCourante);
        } else { //si la section n'est pas archivée, on supprime la section et toutes ses tâches
            // (pour éviter tout problème avec les dépendances)
            List<Tache> taches = new ArrayList<>(this.sectionCourante.getTaches()); // Pour éviter les ConcurrentModificationException
            for (Tache t : taches) {
                this.tacheCourante = t;
                this.supprimerTache(); // Méthode qui supprime toutes les dépendances chronologiques d'une tâche également.
            }
            this.sections.remove(this.sectionCourante);
        }
        this.notifierObservateurs();
    }


    /**
     * Méthode qui permet de supprimer la tacheCourante, cela a pour conséquence de supprimer
     * toutes les dépendances chronologiques qu'elle pourrait avoir avec des autres tâches
     */
    public void supprimerTache() {
        Set<Tache> listeTachesQuiOntDesDependances = this.dependances.keySet();
        //on stock les tâches qui n'ont plus de dépendances dans cette liste pour les supprimer après le parcours de
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
            // La tâche n'est pas contenue directement dans sa section parente. Elle est uniquement dans tachesArchivees

            // On va donc créer une section qui prend la liste des tâches archivées pour la passer en paramètres de
            // la méthode de suppression récursive
            Section sectionTachesArchivees = new Section("archive", this.tachesArchivees);
            supprimerTacheRecursive(sectionTachesArchivees); // On utilise cette méthode au cas où ce soit une sous-tâche
            // qui soit archivée, car dans ce cas là, il faut parcourir toute la liste de tâches pour trouver l'éventuelle
            // sous-tâche
        } else { //si la section n'est pas archivée alors la tâche peut être quand meme être archivée ou pas
            if (isArchivee_Tache(this.tacheCourante)) {
                // La tâche n'est pas contenue directement dans sa section parente. Elle est uniquement dans tachesArchivees
                // On va donc créer une section qui prend la liste des tâches archivées pour la passer en paramètres de
                // la méthode de suppression récursive
                Section sectionTachesArchivees = new Section("archive", this.tachesArchivees);
                supprimerTacheRecursive(sectionTachesArchivees);
            } else { // Si elle n'est pas archivée
                this.tacheCourante.getSectionParente().supprimerTache(this.tacheCourante);
                supprimerTacheRecursive(this.tacheCourante.getSectionParente()); // la section dans laquelle est contenue la tâche est sa section parente
            }
        }
        this.notifierObservateurs();
    }

    /**
     * Parcourt la section dans laquelle la tâche/sous-tâche est contenue, et tant qu'on ne la trouve pas on descendra
     * dans les sous-tâches en appelant la méthode supprimerSousTacheRecursive
     */
    public void supprimerTacheRecursive(Section section) {
        // Obtient la tâche à supprimer depuis l'attribut tacheCourante du modèle
        Tache tacheASupprimer = this.tacheCourante;

        List<Tache> tachesSection = section.getTaches();
        for (Tache tache : new ArrayList<>(tachesSection)) {
            if (tache == tacheASupprimer) { // Comparaison par référence
                tachesSection.remove(tache);
            } else if (tache instanceof TacheMere) {
                TacheMere tacheMere = (TacheMere) tache;
                supprimerSousTacheRecursive(tacheASupprimer, tacheMere.getSousTaches());
            }
        }
    }

    /**
     * Méthode récursive, qui pour chaque sous tâche, parcourt à nouveau ses sous-tâches jusqu'à trouver la tâche à supprimer
     * Et la supprimer, une fois la sous-tâche trouvée, ses sous-tâches seront elles aussi supprimées grâce au patron composite
     *
     * @param tacheASupprimer
     * @param sousTaches
     */
    private void supprimerSousTacheRecursive(Tache tacheASupprimer, List<Tache> sousTaches) {
        for (Tache sousTache : new ArrayList<>(sousTaches)) {
            if (sousTache == tacheASupprimer) { // Comparaison par référence
                sousTaches.remove(sousTache);
            } else if (sousTache instanceof TacheMere) {
                TacheMere tacheMere = (TacheMere) sousTache;
                supprimerSousTacheRecursive(tacheASupprimer, tacheMere.getSousTaches());
            }
        }
    }


    /**
     * Méthode récursive qui permet de déterminer si la tacheMere est une sous-tâche de la tacheActuelle
     * Cela permet d'empêcher (dans cette application) le déplacement d'une tâche dans une de ses sous-tâches
     *
     * @param tacheMere
     * @param tacheActuelle
     * @return
     */
    public boolean estSousTacheDe(Tache tacheMere, Tache tacheActuelle) {
        if (tacheActuelle instanceof TacheMere) {
            List<Tache> sousTaches = ((TacheMere) tacheActuelle).getSousTaches();
            for (Tache sousTache : sousTaches) {
                if (sousTache == tacheMere) {
                    return true;
                } else if (sousTache instanceof TacheMere) {
                    if (estSousTacheDe(tacheMere, sousTache)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * Méthode qui permet d'archiver la tâcheCourante avec toutes ses dépendances
     * (et donc ses sous-tâches qui sont naturellement inclues)
     */
    public void archiverTache() {
        System.out.println("ARCHIVAGE TAGE " + this.tacheCourante);
        this.tacheCourante.getSectionParente().supprimerTache(this.tacheCourante);
        this.tachesArchivees.add(this.tacheCourante);

        //on notifie les observateurs de la modification
        this.notifierObservateurs();
    }

    /**
     * Méthode qui permet d'archiver une section en entière ainsi que toutes ses tâches
     */
    public void archiverSection() {
        //on archive la section avec toutes ses tâches
        this.sectionsArchivees.add(this.sectionCourante);

        List<Tache> taches = new ArrayList<>(this.sectionCourante.getTaches());

        // on archive toutes les tâches et leurs dépendances avant d'archiver la section elle même
        // (pour éviter tout problème avec les dépendances)
        for (Tache t : taches) {
            this.tacheCourante = t;
            this.archiverTache();
        }

        // on retire la section des sections non archivées
        this.sections.remove(this.sectionCourante);

        //on notifie les observateurs de la modification
        this.notifierObservateurs();
    }


    /**
     * Méthode qui permet de restaurer la tâche courante dans la section dans
     * laquelle elle se trouvait avant d'être archivée
     */
    public void restaurerTache() {
        Section sectionParente = this.tacheCourante.getSectionParente();
        this.sectionCourante = sectionParente;
        if (!isSupprimee_Section(sectionParente)) {
            if (!isArchivee_Section(sectionParente)) { //si la section est dans son état normal
                ajouterTacheDansSection();
            } else { //si la section est archivée
                //on restaure la section
                restaurerSection();
                ajouterTacheDansSection();
            }
        } else { //si la section a été supprimée
            //on recréé la section de la tâche
            this.sections.add(sectionParente);
            ajouterTacheDansSection();
        }
    }

    /**
     * Méthode qui permet de restaurer une section
     */
    public void restaurerSection() {
        this.sectionsArchivees.remove(this.sectionCourante);
        this.sections.add(this.sectionCourante);
    }

    /**
     * Permet d'enregistrer un observateur à la liste
     *
     * @param observateur
     */
    @Override
    public void enregistrerObservateur(Observateur observateur) {
        this.observateurs.add(observateur);
    }

    /**
     * Permet de supprimer un observateur de la liste
     *
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
        for (Observateur o : this.observateurs) {
            o.actualiser(this);
        }
    }

    /**
     * Retourne la liste des observateurs du modèle
     *
     * @return
     */
    public List<Observateur> getObservateurs() {
        return observateurs;
    }

    /**
     * Permet de changer la liste des observateurs du modèle par celle passée en paramètre
     *
     * @param observateurs
     */
    public void setObservateurs(List<Observateur> observateurs) {
        this.observateurs = observateurs;
    }

    /**
     * renvoi un id unique de section et incremente pour la suite
     *
     * @return int id section unique
     */
    public static int getIDSECTIONACTUELLE() {
        return IDSECTIONACTUELLE++;
    }

    /**
     * renvoi un id unique de tâche et incremente pour la suite
     *
     * @return int id tache unique
     */
    public static int getIDTACHEACTUELLE() {
        return IDTACHEACTUELLE++;
    }


    /**
     * Retourne la tâche courante
     *
     * @return
     */
    public Tache getTacheCourante() {
        return tacheCourante;
    }

    /**
     * Permet de modifier la tâche courante
     *
     * @param tacheCourante
     */
    public void setTacheCourante(Tache tacheCourante) {
        this.tacheCourante = tacheCourante;
    }

    /**
     * Retourne la section courante
     *
     * @return
     */
    public Section getSectionCourante() {
        return sectionCourante;
    }

    /**
     * Permet de changer la section courante
     *
     * @param sectionCourante
     */
    public void setSectionCourante(Section sectionCourante) {
        this.sectionCourante = sectionCourante;
    }

    /**
     * Retourne la liste des tâches archivées
     *
     * @return
     */
    public List<Tache> getTachesArchivees() {
        return (this.tachesArchivees);
    }


    /**
     * Permet de changer la liste de tâches archivées
     *
     * @param tachesArchivees
     */
    public void setTachesArchivees(List<Tache> tachesArchivees) {
        this.tachesArchivees = tachesArchivees;
    }

    /**
     * Retourne la liste des sections archivées
     *
     * @return
     */
    public List<Section> getSectionsArchivees() {
        return (this.sectionsArchivees);
    }

    /**
     * Permet de changer la liste des sections archivées
     *
     * @param sectionsArchivees
     */
    public void setSectionsArchivees(List<Section> sectionsArchivees) {
        this.sectionsArchivees = sectionsArchivees;
    }

    /**
     * Retourne la table des dépendances chronologiques
     *
     * @return
     */
    public Map<Tache, List<Tache>> getDependances() {
        return this.dependances != null ? this.dependances : null;
    }

    /**
     * Permet de changer la table des dépendances chronologiques
     *
     * @param dependances
     */
    public void setDependances(Map<Tache, List<Tache>> dependances) {
        this.dependances = dependances;
    }

    /**
     * Retourne la liste des sections du modèle
     *
     * @return
     */
    public List<Section> getSections() {
        return sections;
    }

    /**
     * Permet de changer la liste des sections du modèles par la liste de sections passée en paramètre
     *
     * @param sections
     */
    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public void gentTest() {
        LocalDate dateMin = LocalDate.MAX;
        LocalDate dateMax = LocalDate.MIN;
        //creation de la liste de tache pour les test, il faut qu'il y ait une date de debut et de fin et que ce soit pas une sous tache

        List<Tache> listTacheGantt = this.getTaches();
        //on parcours la liste des taches et on enleve celle qui n'on pas de date
        if (!listTacheGantt.isEmpty()) {
            for (Tache t : listTacheGantt) {
                if (t.getDateDebut() == null) {
                    listTacheGantt.remove(t);
                } else {
                    if (dateMin.isAfter(t.getDateDebut())) dateMin = t.getDateDebut();
                    if (dateMax.isBefore(t.getDateFin())) dateMax = t.getDateFin();
                }
            }
            ArrayList<Tache> listeDepTach;
            for (Tache t : listTacheGantt) {

                System.out.println(t.getTitre() + "Debut: " + t.getDateDebut() + " Fin: " + t.getDateFin());
                System.out.println("Tache a faire avant:");
                listeDepTach = (ArrayList<Tache>) this.dependances.get(t);
                if (listeDepTach != null && !listeDepTach.isEmpty()) {
                    for (Tache taDep : listeDepTach) {
                        if (listTacheGantt.contains(taDep)) {
                            System.out.println(taDep.getTitre());
                        }
                    }

                }
            }
            System.out.println("Date max = "+ dateMax);
            System.out.println("Date min = "+ dateMin);
            int difjour= (int) ChronoUnit.DAYS.between(dateMin,dateMax)+1;
            System.out.println("jour entre datemax et datemin :"+ difjour);
        }
    }

    private List<Tache> selectionTacheGantt;

    public List<Tache> getSelectionTacheGantt() {
        return selectionTacheGantt;
    }

    public void setSelectionTacheGantt(List<Tache> selectionTacheGantt) {
        this.selectionTacheGantt = selectionTacheGantt;
    }
}
