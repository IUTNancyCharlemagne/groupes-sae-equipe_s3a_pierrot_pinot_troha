package tralleno.Taches;

import tralleno.Modele.ModeleBureau;
import tralleno.Section.Section;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Classe représentant une tâche, se déclinant en deux type de tâches. TacheFille, étant une tâche ne possèdant pas de sous-tâche
 * Et TacheMere, possèdant des sous-tâches
 */
public abstract class Tache implements Comparable<Tache>, Serializable {

    /**
     * Référence de la section dans laquelle une tâche est contenue
     */
    protected Section sectionParente;
    /**
     * Titre de la tâche
     */
    protected String titre;
    /**
     * Identifiant unique de la tâche
     */
    protected int id;
    /**
     * Dates de début et de fin de la tâche
     */
    protected LocalDate dateDebut, dateFin;

    /**
     * Durée de la tâche (déduite des dates de début et de fin)
     */
    protected int duree; //duree en jour

    /**
     * Description textuelle de la tâche
     */
    protected String description;


    /**
     * Construit une tâche à partir de son titre et de sa description
     *
     * @param t titre de la tâche
     * @param d description de la tâche
     */
    public Tache(String t, String d) {
        this.titre = t;
        this.description = d;
        this.id = ModeleBureau.getIDTACHEACTUELLE();
    }

    /**
     * Construit une tâche à partir de son titre, sa description, ses dates de début et de fin
     *
     * @param t
     * @param d
     * @param dD
     * @param dF
     */
    public Tache(String t, String d, LocalDate dD, LocalDate dF) {
        this.titre = t;
        this.description = d;
        this.dateDebut = dD;
        this.dateFin = dF;
        this.duree = 1;
        if (!(dD == null && dF == null)) {
            this.duree = (int) ChronoUnit.DAYS.between(dD, dF);
        }
        this.id = ModeleBureau.getIDTACHEACTUELLE();
    }

    /**
     * Construit une tâche par copie de la tâche passée en paramètre
     *
     * @param t
     */
    public Tache(Tache t) {
        this.titre = t.getTitre();
        this.description = t.getDescription();
        this.dateDebut = t.getDateDebut();
        this.dateFin = t.getDateFin();
        this.id = t.getId();
    }

    /**
     * Retourne la section parente de la tâche sur laquelle la méthode est appelée
     * @return
     */
    public Section getSectionParente() {
        return sectionParente;
    }

    /**
     * Permet de changer la section parente de la tâche qui appelle la méthode
     * @param sectionParente
     */
    public void setSectionParente(Section sectionParente) {
        this.sectionParente = sectionParente;
    }

    /**
     * Renvoie le titre de la tâche
     * @return
     */
    public String getTitre() {
        return titre;
    }

    /**
     * Change le titre de la tâche
     * @param titre
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
     * Retourne l'identifiant unique de la tâche
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Change l'id de la tâche
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retourne la date de début de la tâche
     * @return
     */
    public LocalDate getDateDebut() {
        return dateDebut;
    }

    /**
     * Permet de changer la date de début de la tâche, et par conséquent la durée qui elle aussi change
     * @param dateDebut
     */
    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;

        if (this.dateDebut != null && this.dateFin != null) {
            this.duree = (int) ChronoUnit.DAYS.between(this.dateDebut, this.dateFin);
        }
    }

    /**
     * Retourne la date de fin de la tâche
     * @return
     */
    public LocalDate getDateFin() {
        return dateFin;
    }

    /**
     * Permet de changer la date de fin de la tâche, et par conséquent la durée qui elle aussi change
     * @param dateFin
     */
    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
        if (this.dateDebut != null && this.dateFin != null) {
            this.duree = (int) ChronoUnit.DAYS.between(this.dateDebut, this.dateFin);
        }
    }

    /**
     * Retourne la durée de la tâche
     * @return
     */
    public int getDuree() {
        return duree;
    }

    /**
     * Change la durée de la tâche par celle passée en paramètres
     * @param duree
     */
    public void setDuree(int duree) {
        this.duree = duree;
    }

    /**
     * Retourne la description de la tâche
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Permet de changer la description de la tâche
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Compare deux tâches selon leur id. Nécessaire pour la manière dont elles peuvent être triées.
     *
     * @param t the object to be compared.
     * @return
     */
    @Override
    public int compareTo(Tache t) {
        return Integer.compare(this.id, t.getId());
    }

    /**
     * Retourne le titre de la tâche ainsi que le nom de la section à laquelle elle appartient
     *
     * @return String
     */
    public String toString() {
        String aff = this.titre + " - " + this.sectionParente.getNom() + "\n";
        return aff;
    }

}
