package tralleno.Vues;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    }

    @Override
    public void actualiser(Sujet s) {
        System.out.println("afficherselecteur");
        VBox vb = new VBox();

        vb.getStyleClass().add("VBoxFormulaire");
        // Pour les dépendances chronologiques
        Label tachesAvant = new Label("Tâches à faire avant :");
        tachesAvant.getStyleClass().add("titreChamp");
        List<Tache> taches = this.modele.getTaches();
        ObservableList<Tache> tachesAFaireAvant = FXCollections.observableArrayList(taches);
        ComboBox<Tache> comboTaches = new ComboBox<>(tachesAFaireAvant);
        comboTaches.getStyleClass().add("comboBox");

        // Bouton pour supprimer la tâche qu'on a selectionnée dans la viewlist
        Button supprimerTache = new Button("Supprimer tâche dépendante");
        supprimerTache.getStyleClass().add("Btn");

        HBox dependances = new HBox(10);
        dependances.getChildren().addAll(comboTaches, supprimerTache);
        dependances.getStyleClass().add("conteneurDependances");

        ListView<Tache> listViewTachesAvant = new ListView<>(); // Affiche les tâches sélectionnées pour la dépendance chronologique
        listViewTachesAvant.setPrefHeight(100);
        listViewTachesAvant.getStyleClass().add("listeTachesAvant");


        // EventHandler pour que la liste des tâches select se mette à jour
        comboTaches.setOnAction(event -> {
            Tache tacheSelectionnee = comboTaches.getValue();
            if (tacheSelectionnee != null && !listViewTachesAvant.getItems().contains(tacheSelectionnee)) {
                listViewTachesAvant.getItems().add(tacheSelectionnee);
            }
        });

        // Si on supprime une tâche qui est en dépendance chronologique
        supprimerTache.setOnAction(event -> {
            Tache tacheSelectionneViewList = listViewTachesAvant.getSelectionModel().getSelectedItem();
            if (tacheSelectionneViewList != null) {
                listViewTachesAvant.getItems().remove(tacheSelectionneViewList);
            }
        });

        Button boutonGenererGantt = new Button("Generer Diagramme Gantt");
        boutonGenererGantt.setOnAction(event -> {
            if (!listViewTachesAvant.getItems().isEmpty()) {
                modele.setSelectionTacheGantt(new ArrayList<>(listViewTachesAvant.getItems()));
            }
            this.vp.changerVue(3);//vue 3 =vue gantt
        });
        vb.getChildren().addAll(tachesAvant, dependances,
                listViewTachesAvant, boutonGenererGantt);

        this.setContent(vb);

    }

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }
}
