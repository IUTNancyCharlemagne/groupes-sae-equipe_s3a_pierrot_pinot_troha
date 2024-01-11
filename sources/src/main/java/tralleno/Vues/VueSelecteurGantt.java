package tralleno.Vues;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import tralleno.Modele.ModeleBureau;
import tralleno.Modele.Sujet;
import tralleno.Taches.Tache;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Vue du selecteur de tâche qui affiche une liste de tâche selectionnable et les enregistre dans le modele
 */
public class VueSelecteurGantt extends ScrollPane implements Observateur, Serializable {

    /**
     * Modèle qui contient toutes les données de l'application
     */
    private ModeleBureau modele;

    /**
     * Vue Principale qu'on utilise dans un controleur pour changer de vue quand on à terminer
     */
    private transient VuePrincipale vp;


    /**
     * Instantiates a new Vue selecteur gantt.
     *
     * @param modele     the modele
     * @param vueprincip the vueprincip
     */
    public VueSelecteurGantt(ModeleBureau modele, VuePrincipale vueprincip) {
        super();
        this.modele = modele;
        this.vp = vueprincip;
        this.modele.setSelectionTacheGantt(new ArrayList<>());
        this.getStyleClass().add("vueSelecteurGantt");
    }

    /**
     * affiche un menu de selection avec lequel on enregistre des taches dans la liste selectionTacheGantt dans le modele
     *
     * @param s
     */
    @Override
    public void actualiser(Sujet s) {


        //on affiche tout dans une gridpane
        GridPane gp = new GridPane();
        gp.getStyleClass().add("gridSelectionGantt");
        Button b = new Button("Generer Diagramme De Gantt");
        b.setOnAction(actionEvent -> {
            this.vp.changerVue(3);
        });
        //gp.setGridLinesVisible(true);
        gp.setVgap(5);
        gp.setHgap(5);
        gp.setPadding(new Insets(10));
        b.getStyleClass().add("boutonGenererGantt");
        ArrayList<Tache> listTache = (ArrayList<Tache>) this.modele.getTaches();
        int colonne = 0;
        int ligne = 0;
        VBox tempVB;
        Label tempLabSection;
        Label tempLabTitre;
        CheckBox tempCheck = null;

        ArrayList<Tache> listeTacheSelectionne = (ArrayList<Tache>) this.modele.getSelectionTacheGantt();
        ArrayList<CheckBox> listeCheckBox = new ArrayList<>();

        //sert a savoir si toute les case sont cochés;
        boolean allBoxChecked = true;
        if (listTache.isEmpty()) {
            this.modele.clearSelectionTacheGantt();

            allBoxChecked = false;
        }

        for (Tache t : listTache) {

            //on construit la boit qui contient la tache
            tempVB = new VBox();
            tempLabSection = new Label(t.getSectionParente().getNom());
            tempLabSection.getStyleClass().add("labelSelectionTacheSectionGantt");
            tempLabTitre = new Label(t.getTitre());
            tempLabTitre.getStyleClass().add("labelSelectionTacheTitreGantt");
            tempCheck = new CheckBox();
            tempCheck.getStyleClass().add("checkboxSelectionTacheGantt");

            tempVB.getChildren().addAll(tempLabTitre, tempLabSection);
            tempVB.setMinWidth(100);
            tempVB.getStyleClass().add("containerSelectionTacheGantt");
            //on recupère l'id de la tâche avec la propriété graphicTextGap
            tempCheck.setGraphicTextGap(ligne);

            //on verifie si la tâche est déjà dans la liste de tâche selectionné si oui on check la box
            if (listeTacheSelectionne.contains(t)) {
                tempCheck.setSelected(true);
            } else {
                allBoxChecked = false;
            }

            listeCheckBox.add(tempCheck);
            //on met en event que si la checkbox est coché alors on ajoute la tâche a la liste du modele si elle est décoché on l'enelve
            tempCheck.setOnAction(actionEvent -> {
                CheckBox c = (CheckBox) actionEvent.getSource();
                Tache tacheCheck = listTache.get((int) c.getGraphicTextGap());
                if (c.isSelected()) {
                    this.modele.addSelectionTacheGantt(tacheCheck);
                } else {
                    this.modele.removeSelectionTacheGantt(tacheCheck);
                }
            });
            gp.add(tempVB, colonne, ligne);
            gp.add(tempCheck, colonne + 1, ligne);
            ligne++;
        }
        CheckBox selectTout = new CheckBox();
        selectTout.getStyleClass().add("checkboxSelectionTacheGantt");
        selectTout.setSelected(allBoxChecked);
        //event de la checkbox qui selectionne toute les tâche puis qui coche toute les checkbox
        selectTout.setOnAction(actionEvent -> {
            CheckBox checkTout = (CheckBox) actionEvent.getSource();
            if (checkTout.isSelected()) {
                ArrayList<Tache> tachesG = (ArrayList<Tache>) this.modele.getSelectionTacheGantt();

                for (CheckBox c : listeCheckBox) {
                    c.setSelected(true);
                    Tache tacheCheck = listTache.get((int) c.getGraphicTextGap());
                    if (!tachesG.contains(tacheCheck)) {
                        this.modele.addSelectionTacheGantt(tacheCheck);
                    }
                }
            } else {
                this.modele.clearSelectionTacheGantt();
                for (CheckBox c : listeCheckBox) {
                    c.setSelected(false);

                }

            }
        });

        gp.add(b, colonne, ligne);
        gp.add(selectTout, colonne + 1, ligne);
        this.setContent(gp);
    }


}