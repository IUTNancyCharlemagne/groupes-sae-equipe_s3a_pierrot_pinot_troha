package tralleno;

import java.util.Calendar;

public abstract class Tache {
    protected String titre;
    protected int id;
    protected Calendar dateDebut,dateFin;

    protected int duree ; //duree en jour

    protected String description;

    public static int IDVALIDACTUELLE=0;

    public Tache(String t,String d){
        this.titre=t;
        this.description=d;
        this.id=Tache.IDVALIDACTUELLE++;

    }

    /**
     * les paramètres doivent êtres vérifiés avant
     * @param t
     * @param d
     * @param dD
     * @param dF
     */
    public Tache(String t,String d,Calendar dD,Calendar dF){
        this.titre=t;
        this.description=d;
        this.dateDebut=dD;
        this.dateFin=dF;
        this.id=Tache.IDVALIDACTUELLE++;
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

    public Calendar getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Calendar dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Calendar getDateFin() {
        return dateFin;
    }

    public void setDateFin(Calendar dateFin) {
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
}
