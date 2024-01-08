package tralleno.Vues;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import tralleno.Modele.ModeleBureau;
import tralleno.Modele.Sujet;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javafx.scene.control.ScrollPane;
import tralleno.Taches.Tache;

import static javafx.scene.layout.BorderStroke.*;

public class VueGantt extends ScrollPane implements Observateur, Serializable {

    private ModeleBureau modele;

    public VueGantt(ModeleBureau modele) {
        super();
        this.modele = modele;
    }

    @Override
    public void actualiser(Sujet s) {


        LocalDate dateMin = LocalDate.MAX; //date min et max des taches selectionné, permet de savoir à partir de quel date on genere le diagramme
        LocalDate dateMax = LocalDate.MIN;
        int difjour = 0; //difference entre la dateMin et la dateMax

        //creation de la liste de tache pour les test, il faut qu'il y ait une date de debut et de fin et que ce soit pas une sous tache

        List<Tache> listTacheGantt = new ArrayList<>(this.modele.getTaches());


        //on parcours la liste des taches et on enleve celles qui n'ont pas de date
        if (!listTacheGantt.isEmpty()) {
            Iterator<Tache> iteraTache = listTacheGantt.iterator();
            while (iteraTache.hasNext()) {
                Tache t = iteraTache.next();
                if (t.getDateDebut() == null) {
                    iteraTache.remove();
                } else {
                    //on ajuste les dates min et max
                    if (dateMin.isAfter(t.getDateDebut())) dateMin = t.getDateDebut();
                    if (dateMax.isBefore(t.getDateFin())) dateMax = t.getDateFin();
                }
            }

            //parcour de la liste pour trouver les dependances
            ArrayList<Tache> listeDepTach;
            for (Tache t : listTacheGantt) {

                //System.out.println(t.getTitre() + "Debut: " + t.getDateDebut() + " Fin: " + t.getDateFin());
                //System.out.println("Tache a faire avant:");
                listeDepTach = (ArrayList<Tache>) this.modele.getDependances().get(t);
                if (listeDepTach != null && !listeDepTach.isEmpty()) {
                    for (Tache taDep : listeDepTach) {
                        if (listTacheGantt.contains(taDep)) {
                            //ici la tache t est une dependance de la tache taDep
                            //System.out.println(taDep.getTitre());
                        }
                    }

                }
            }
            //System.out.println("Date max = "+ dateMax);
            //System.out.println("Date min = "+ dateMin);

            difjour = (int) ChronoUnit.DAYS.between(dateMin, dateMax) + 1;
            //System.out.println("jour entre datemax et datemin :" + difjour);
        }
        if (!listTacheGantt.isEmpty()) {


            //container est la horizontal box qui vas contenir tout le diagramme de gantt, on la remet immediatement a la place de l'ancienne dans la scrollpane
            HBox container = new HBox();

            StackPane st = new StackPane();
            Canvas cv = new Canvas(1000, 1000);
            GraphicsContext gc = cv.getGraphicsContext2D();
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
            st.getChildren().addAll(container, cv);
            this.setContent(st);


            //boxjour c'est la colonne qui correspond à une date du calendrier gantt
            VBox boxJour;
            //dateJour c'est la date qui correspondra au jour de la colonne boxJour
            LocalDate dateJour;

            //nbTache c'est la hauteur des colonnnes
            int nbTache = listTacheGantt.size();

            //grilleJour c'est la grille des jours de chaque tache, ex: grilleJour[1][2] c'est le jour index 2 (dateMin+2) de la tache index 1
            //si le jour est dans l'intervalle dateDebut dateFin de la tache qui correspond alors il faut le signifier (ici avec des rectangles de couleurs)
            //si on les sauvegarde pas dans un tableau à part c'est difficile de les retrouver quand on les modifies.
            StackPane[][] grilleJour = new StackPane[nbTache][difjour];

            StackPane temp;
            Rectangle tempRect;
            Label tempLab;
            //largeur des rectangles et donc des colonnes
            double largeurBox = 100;
            double hauteurRectangle = 20;
            cv.setHeight(nbTache * hauteurRectangle);
            cv.setWidth(difjour * largeurBox);
            //on va créer autant de colonne que de jours entre la dateMin et dateMax de la selection de tache
            for (int i = 0; i < difjour; i++) {
                //date du jour qui correspond a la colonne
                dateJour = (dateMin.plusDays(i));
                boxJour = new VBox();
                boxJour.setMaxWidth(largeurBox);

                //on va rajouter dans la colonne autant de rectangle que de tache
                for (int j = 0; j < nbTache; j++) {
                    temp = new StackPane();
                    tempRect = new Rectangle(largeurBox, hauteurRectangle);
                    tempRect.setFill(Color.WHITE);
                    tempLab = new Label();
                    temp.getChildren().addAll(tempRect, tempLab);
                    //on ajoute le rectangle dans la grille
                    grilleJour[j][i] = temp;
                    //et dans la colonne
                    boxJour.getChildren().add(temp);

                }
                //on ajoute la date en ba de la colonne
                boxJour.getChildren().add(new Label(" " + dateJour.toString() + " "));

                boxJour.setSpacing(1);
                //finalement on ajoute la colonne a la hbox
                container.getChildren().add(boxJour);

            }
            HashMap<Tache, Point2D> pointTache = new HashMap<>(nbTache);
            //les index nous indique quand commence et quand fini la tache
            int indexJdep, indexJfin;
            for (int i = 0; i < nbTache; i++) {
                indexJdep = (int) ChronoUnit.DAYS.between(dateMin, listTacheGantt.get(i).getDateDebut());
                indexJfin = (int) ChronoUnit.DAYS.between(dateMin, listTacheGantt.get(i).getDateFin());
                //on change la couleur des rectangles dans l'intervalle de date de la tache
                Label l = (Label) grilleJour[i][indexJdep].getChildren().get(1);
                l.setText(listTacheGantt.get(i).getTitre());
                pointTache.put(listTacheGantt.get(i), new Point2D(largeurBox * (indexJdep), hauteurRectangle * (i)));
                for (int j = indexJdep; j < indexJfin + 1; j++) {
                    //i c'est la date
                    //j c'est la tache
                    Rectangle r = (Rectangle) grilleJour[i][j].getChildren().get(0);
                    r.setFill(Color.RED);
                }

            }
            ArrayList<Tache> listeDepTach;
            for (Tache t : listTacheGantt) {

                //System.out.println(t.getTitre() + "Debut: " + t.getDateDebut() + " Fin: " + t.getDateFin());
                //System.out.println("Tache a faire avant:");
                listeDepTach = (ArrayList<Tache>) this.modele.getDependances().get(t);
                if (listeDepTach != null && !listeDepTach.isEmpty()) {
                    for (Tache taDep : listeDepTach) {
                        if (listTacheGantt.contains(taDep)) {
                            //ici la tache t est une dependance de la tache taDep
                            gc.strokeLine(pointTache.get(t).getX(), pointTache.get(t).getY(), pointTache.get(taDep).getX() + (taDep.getDuree()+1) * largeurBox, pointTache.get(taDep).getY());
                        }
                    }

                }
            }

        }
    }
}
