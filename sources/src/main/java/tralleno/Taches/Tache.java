package tralleno.Taches;

import tralleno.Modele.ModeleBureau;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Calendar;

public abstract class Tache implements Comparable<Tache>, Serializable{
    protected String titre;
    protected int id;
    protected LocalDate dateDebut,dateFin;

    protected int duree ; //duree en jour

    protected String description;

    public static int IDVALIDACTUEL=1;

    /**
     *
     * @param t titre de la tâche
     * @param d description de la tâche
     */
    public Tache(String t,String d){
        this.titre=t;
        this.description=d;
        this.id= ModeleBureau.getIDTACHEACTUELLE();
    }

    /**
     * les paramètres doivent êtres vérifiés avant
     * @param t
     * @param d
     * @param dD
     * @param dF
     */
    public Tache(String t,String d,LocalDate dD,LocalDate dF){
        this.titre=t;
        this.description=d;
        this.dateDebut=dD;
        this.dateFin=dF;
        this.id=ModeleBureau.getIDTACHEACTUELLE();
    }

    public Tache(Tache t) {
        this.titre=t.getTitre();
        this.description=t.getDescription();
        this.dateDebut=t.getDateDebut();
        this.dateFin=t.getDateFin();
        this.id=t.getId();
    }


    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int compareTo(Tache t) {
        // Par exemple, comparaison basée sur l'ID des tâches
        return Integer.compare(this.id, t.getId());
    }

    public String toString(){
        String aff = "Titre : " + this.titre + "\n";
        aff += "ID : " + this.id + "\n";
        aff += "dateDebut : " + this.dateDebut + "\n";
        aff += "dateFin : " + this.dateFin + "\n";
        aff += "duree : " + this.duree + "\n";
        aff += "Description : " + this.description + "\n";

        return aff;
    }
}
