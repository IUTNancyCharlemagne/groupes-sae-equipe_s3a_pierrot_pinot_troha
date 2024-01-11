package tralleno.Vues;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import tralleno.Modele.ModeleBureau;
import tralleno.Modele.Sujet;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import tralleno.Taches.Tache;

public class VueGantt extends ScrollPane implements Observateur, Serializable {

    private ModeleBureau modele;

    /**
     * @param modele modele a partir duquel il va chercher les données
     */
    public VueGantt(ModeleBureau modele) {
        super();
        this.modele = modele;
        this.getStyleClass().add("vueGantt");
    }

    /**
     * affiche le tableau de gantt à partir de la liste selectionTacheGantt du modele
     */
    @Override
    public void actualiser(Sujet s) {
        afficherGantt();
    }


    /**
     * affiche le tableau de gantt à partir de la liste selectionTacheGantt du modele
     */
    public void afficherGantt() {

        //on enleve l'ancien contenu car si il n'y a pas de tache on n'affiche rien
        this.setContent(null);

        //date min et max des taches selectionné, permet de savoir à partir de quel date on genere le diagramme
        LocalDate dateMin = LocalDate.MAX;
        LocalDate dateMax = LocalDate.MIN;

        //difference entre la dateMin et la dateMax
        int difjour = 0;

        //creation de la liste de tache pour les test, il faut qu'il y ait une date de debut et de fin et que ce soit pas une sous tache

        if (this.modele.getSelectionTacheGantt() != null) {
            List<Tache> listTacheGantt = new ArrayList<Tache>(this.modele.getSelectionTacheGantt());
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

                difjour = (int) ChronoUnit.DAYS.between(dateMin, dateMax) + 1;
            }
            //on verifie que la liste de tache ne soit pas vide après qu'on ait enlevé celles sans date
            if (!listTacheGantt.isEmpty()) {

                //stackpane pour pouvoir mettre un padding pour les elements
                StackPane st = new StackPane();
                st.setPadding(new Insets(20));


                this.setContent(st);

                //espace vertical entre chaque ligne de tache
                int spacingEntreTache = 1;


                //dateJour c'est la date qui correspondra au jour de la colonne
                LocalDate dateJour;

                //nbTache c'est la hauteur des colonnnes
                int nbTache = listTacheGantt.size();

                //variables qu'on vas intialiser en boucle
                StackPane temp;
                Label tempLab;
                VBox tempVBox;

                //largeur des rectangles et donc des colonnes
                double largeurBox = 70;
                double hauteurRectangle = 50;


                //sert à mettre le format de la date en plus court
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
                //grille ou on vas mettres les taches
                GridPane gridGantt = new GridPane();
                gridGantt.setVgap(spacingEntreTache);
                gridGantt.getStyleClass().add("gridGantt");

                //on ajoute les dates en bas de la grille
                for (int i = 0; i < difjour + 24; i++) {
                    if (i % 2 == 0) {
                        tempVBox = new VBox();
                        tempVBox.getStyleClass().add("vboxGrilleGantt");
                        tempVBox.setMinWidth(largeurBox);
                        tempVBox.setMaxWidth(largeurBox);
                        gridGantt.add(tempVBox, i, 0, 1, nbTache + 1);
                    }
                    dateJour = (dateMin.plusDays(i));
                    tempLab = new Label(" " + dateJour.format(formatter) + " ");
                    tempLab.getStyleClass().add("dateTacheGantt");
                    tempLab.setMinWidth(largeurBox);
                    tempLab.setMaxWidth(largeurBox);
                    tempLab.setAlignment(Pos.CENTER);
                    gridGantt.add(tempLab, i, nbTache);
                }


                //les index nous indique quand commence et quand fini la tache
                int indexJdep, indexJfin;
                Tache tActuelle;
                //on vas placer les tâches sur la grid, ce sont des stackpanes qu'on vas ensuite coloré avec le css
                for (int i = 0; i < nbTache; i++) {
                    tActuelle = listTacheGantt.get(i);
                    //i c'est l'index de la tache, c'est aussi l'index de la ligne (coordonées y)

                    //indexJdep c'est l'index X de la première date de la tache i et indexJfin index de la dernière date
                    indexJdep = (int) ChronoUnit.DAYS.between(dateMin, tActuelle.getDateDebut());
                    indexJfin = (int) ChronoUnit.DAYS.between(dateMin, tActuelle.getDateFin());

                    //le stackpane c'est la tache, elle va s'etendre sur la grille, du premier au dernier jour de la tache
                    //on va mettre le titre de la tache a gauche du stackpane
                    temp = new StackPane();
                    temp.getStyleClass().add("stackPaneTacheGantt");

                    temp.setMinSize(largeurBox * (indexJfin - indexJdep + 1), hauteurRectangle);
                    //temp.setMaxSize(largeurBox * (indexJfin - indexJdep + 1), hauteurRectangle);

                    //label pour le nom de la tache
                    tempLab = new Label(tActuelle.getTitre());
                    tempLab.getStyleClass().add("titreTacheGantt");
                    //on met la taille maximal pour que si le titre est plus grand que la tache il n'agrandisse pas la tache créant des problemes
                    tempLab.setMaxWidth(largeurBox * (tActuelle.getDuree() + 1));

                    temp.setAlignment(Pos.CENTER_LEFT);
                    temp.setPadding(new Insets(5));
                    temp.getChildren().add(tempLab);
                    gridGantt.add(temp, indexJdep, i, tActuelle.getDuree() + 1, 1);


                }

                //on vas parcourir les dependances de chaque tache pour tirer les traits de dependances
                ArrayList<Tache> listeDepTach;
                Line diagonalLine;
                int dureeDepart, xDepart, yDepart, xArrive, yArrive;
                for (Tache t : listTacheGantt) {

                    listeDepTach = (ArrayList<Tache>) this.modele.getDependances().get(t);
                    if (listeDepTach != null && !listeDepTach.isEmpty()) {
                        for (Tache taDep : listeDepTach) {
                            if (listTacheGantt.contains(taDep)) {
                                //ici la tache t est une dependance de la tache taDep
                                //on prend le point de depart + la duree de la tache * la largeur de chaque jour pour que le trait de dependances parte de l'avant de la tache a faire avant
                                //gc.strokeLine(pointTache.get(t).getX(), pointTache.get(t).getY() + (listTacheGantt.indexOf(t) * spacingEntreTache), pointTache.get(taDep).getX() + (taDep.getDuree() + 1) * largeurBox, pointTache.get(taDep).getY() + (listTacheGantt.indexOf(taDep) * spacingEntreTache));
                                dureeDepart = taDep.getDuree();
                                xDepart = (int) ChronoUnit.DAYS.between(dateMin, taDep.getDateDebut());
                                xArrive = (int) ChronoUnit.DAYS.between(dateMin, t.getDateDebut());
                                yDepart = listTacheGantt.indexOf(taDep);
                                yArrive = listTacheGantt.indexOf(t);
                                diagonalLine = new Line(0, 0, largeurBox * (xArrive - xDepart - dureeDepart - 1), hauteurRectangle * (yArrive - yDepart)); // Adjust the coordinates as needed
                                int rowspan = xArrive - xDepart + dureeDepart;
                                diagonalLine.getStyleClass().add("ligneGantt");
                                //en fonction de la position verticale des tâches par rapport l'une à l'autre il faut changer certain parametre d'index dans la grid
                                if (yDepart > yArrive) {
                                    gridGantt.add(diagonalLine, xDepart + dureeDepart + 1, yArrive, rowspan, yDepart - yArrive + 1);
                                } else {
                                    gridGantt.add(diagonalLine, xDepart + dureeDepart + 1, yDepart, rowspan, yArrive - yDepart + 1);
                                }

                            }
                        }
                    }
                }
                st.getChildren().addAll(gridGantt);
            }
        }
    }
}
