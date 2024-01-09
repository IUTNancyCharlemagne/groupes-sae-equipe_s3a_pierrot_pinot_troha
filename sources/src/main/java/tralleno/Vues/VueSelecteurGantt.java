package tralleno.Vues;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
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
    }

    /**
     * affiche un menu de selection avec lequel on enregistre des taches dans la liste selectionTacheGantt dans le modele
     *
     * @param s
     */
    @Override
    public void actualiser(Sujet s) {

        GridPane gp = new GridPane();
        Button b=new Button("Generer Diagramme De Gantt");
        b.setOnAction(actionEvent -> {
            this.vp.changerVue(3);
        });
        //gp.setGridLinesVisible(true);
        gp.setVgap(5);
        gp.setHgap(5);
        gp.setPadding(new Insets(10));
        gp.getStyleClass().add("gridSelectionGantt");
        b.getStyleClass().add("boutonGenererGantt");
        ArrayList<Tache> listTache = (ArrayList<Tache>) this.modele.getTaches();
        int colonne = 0;
        int ligne = 0;
        VBox tempVB;
        Label tempLabSection;
        Label tempLabTitre;
        CheckBox tempCheck;

        ArrayList<Tache> listeTacheSelectionne= (ArrayList<Tache>) this.modele.getSelectionTacheGantt();

        for (Tache t : listTache) {

            tempVB=new VBox();
            tempLabSection=new Label(t.getSectionParente().getNom());
            tempLabTitre=new Label(t.getTitre());
            tempCheck=new CheckBox();
            tempVB.getChildren().addAll(tempLabTitre,tempLabSection);
            tempVB.setMinWidth(100);
            tempVB.getStyleClass().add("containerSelectionTacheGantt");
            tempLabSection.getStyleClass().add("labelSelectionTacheSectionGantt");
            tempLabTitre.getStyleClass().add("labelSelectionTacheTitreGantt");
            tempCheck.getStyleClass().add("checkboxSelectionTacheGantt");
            tempCheck.setGraphicTextGap(ligne);
            if(listeTacheSelectionne.contains(t)){
                tempCheck.setSelected(true);
            }
            tempCheck.setOnAction(actionEvent -> {
                CheckBox c= (CheckBox) actionEvent.getSource();
                Tache tacheCheck=listTache.get((int) c.getGraphicTextGap());
                if(c.isSelected()){
                    this.modele.addSelectionTacheGantt(tacheCheck);
                }else{
                    this.modele.removeSelectionTacheGantt(tacheCheck);
                }
            });
            gp.add(tempVB, colonne, ligne);
            gp.add(tempCheck,colonne+1,ligne);
            ligne++;
        }
        gp.add(b,colonne,ligne);
        this.setContent(gp);


    }

    public void afficherSelecteurDropdownList() {

        VBox vb = new VBox();

        vb.getStyleClass().add("VBoxFormulaire");
        // Pour les dépendances chronologiques
        Label tachesGantt = new Label("Selectionnez les tâches à afficher sur le diagramme de gantt");
        tachesGantt.getStyleClass().add("titreChamp");
        List<Tache> taches = this.modele.getTaches();
        ObservableList<Tache> tachesAFaireAvant = FXCollections.observableArrayList(taches);
        ComboBox<Tache> comboTaches = new ComboBox<>(tachesAFaireAvant);
        comboTaches.getStyleClass().add("comboBox");

        // Bouton pour supprimer la tâche qu'on a selectionnée dans la viewlist
        Button supprimerTache = new Button("Supprimer tâche");
        supprimerTache.getStyleClass().add("Btn");

        HBox dependances = new HBox(10);
        dependances.getChildren().addAll(comboTaches, supprimerTache);
        dependances.getStyleClass().add("conteneurDependances");

        ListView<Tache> listVueTachesGantt = new ListView<>(); // Affiche les tâches sélectionnées pour la dépendance chronologique
        listVueTachesGantt.setPrefHeight(100);
        listVueTachesGantt.getStyleClass().add("listeTachesAvant");


        // EventHandler pour que la liste des tâches select se mette à jour
        comboTaches.setOnAction(event -> {
            Tache tacheSelectionnee = comboTaches.getValue();
            if (tacheSelectionnee != null && !listVueTachesGantt.getItems().contains(tacheSelectionnee)) {
                listVueTachesGantt.getItems().add(tacheSelectionnee);
            }
        });

        // Si on supprime une tâche qui est en dépendance chronologique
        supprimerTache.setOnAction(event -> {
            Tache tacheSelectionneViewList = listVueTachesGantt.getSelectionModel().getSelectedItem();
            if (tacheSelectionneViewList != null) {
                listVueTachesGantt.getItems().remove(tacheSelectionneViewList);
            }
        });

        Button boutonGenererGantt = new Button("Generer Diagramme Gantt");
        boutonGenererGantt.setOnAction(event -> {
            if (!listVueTachesGantt.getItems().isEmpty()) {
                modele.setSelectionTacheGantt(new ArrayList<>(listVueTachesGantt.getItems()));
            }
            this.vp.changerVue(3);//vue 3 =vue gantt
        });
        vb.getChildren().addAll(tachesGantt, dependances,
                listVueTachesGantt, boutonGenererGantt);

        this.setContent(vb);


    }

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }
}
