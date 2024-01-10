package tralleno.Vues;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import tralleno.Modele.ModeleBureau;
import tralleno.Modele.Sujet;
import tralleno.Taches.Tache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VueSelecteurGantt extends ScrollPane implements Observateur, Serializable {
    private ModeleBureau modele;

    private transient VuePrincipale vp;

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

        boolean allBoxChecked = true;
        if (listTache.isEmpty()) {
            this.modele.clearSelectionTacheGantt();

            allBoxChecked=false;
        }

        for (Tache t : listTache) {

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
            tempCheck.setGraphicTextGap(ligne);

            if (listeTacheSelectionne.contains(t)) {
                tempCheck.setSelected(true);
            } else {
                allBoxChecked = false;
            }

            listeCheckBox.add(tempCheck);
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
