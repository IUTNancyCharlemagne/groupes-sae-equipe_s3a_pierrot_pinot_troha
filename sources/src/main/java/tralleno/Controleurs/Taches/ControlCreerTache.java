package tralleno.Controleurs.Taches;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tralleno.Modele.ModeleBureau;
import tralleno.Section.Section;
import tralleno.Taches.Tache;
import tralleno.Taches.TacheFille;

import java.time.LocalDate;
import java.util.List;

public class ControlCreerTache implements EventHandler<MouseEvent> {

    private ModeleBureau modeleBureau;

    public ControlCreerTache(ModeleBureau modeleBureau){
        this.modeleBureau = modeleBureau;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        // On crée un nouveau stage (fenêtre)
        Stage fenetreCreationTache = new Stage();
        fenetreCreationTache.initModality(Modality.APPLICATION_MODAL);
        fenetreCreationTache.setTitle("Créer une Tâche");

// Préparation du formulaire
        Label titreTache = new Label("Titre de la tâche:");
        TextField champTitre = new TextField();

        Label labelDescription = new Label("Description:");
        TextField champDescription = new TextField();

        Label choixSection = new Label("Section :");
        // On récupère les noms des sections du modèle
        List<Section> listeSections = this.modeleBureau.getSections();

        ObservableList<Section> sections = FXCollections.observableArrayList(listeSections);
        ComboBox<Section> comboSection = new ComboBox<>(sections);

        // Maintenant les dates de début et fin
        Label labelDateDebut = new Label("Date de début:");
        DatePicker dateDebut = new DatePicker();

        Label labelDateFin = new Label("Date de fin:");
        DatePicker dateFin = new DatePicker();

        // HBox pour mettre les deux selecteurs de date sur la même ligne
        HBox choixDate = new HBox(10);
        choixDate.getChildren().addAll(labelDateDebut, dateDebut, labelDateFin, dateFin);


        // Pour les dépendances chronologiques
        Label tachesAvant = new Label("Tâches à faire avant :");
        List<Tache> taches = this.modeleBureau.getTaches();
        ObservableList<Tache> tachesAFaireAvant = FXCollections.observableArrayList(taches);
        ComboBox<Tache> comboTaches = new ComboBox<>(tachesAFaireAvant);



        // Bouton pour supprimer la tâche qu'on a selectionnée dans la viewlist
        Button supprimerTache = new Button("Supprimer tâche dépendante");

        HBox dependances = new HBox(10);
        dependances.getChildren().addAll(comboTaches, supprimerTache);

        ListView<Tache> listViewTachesAvant = new ListView<>(); // Affiche les tâches sélectionnées pour la dépendance chronologique
        listViewTachesAvant.setPrefHeight(100);



// EventHandler pour que la liste des tâches select se mette à jour
        comboTaches.setOnAction(event -> {
            Tache tacheSelectionnee = comboTaches.getValue();
            if (tacheSelectionnee != null && !listViewTachesAvant.getItems().contains(tacheSelectionnee)) {
                listViewTachesAvant.getItems().add(tacheSelectionnee);
            }
        });

        supprimerTache.setOnAction(event -> {
            Tache tacheSelectionneViewList = listViewTachesAvant.getSelectionModel().getSelectedItem();
            if (tacheSelectionneViewList != null) {
                listViewTachesAvant.getItems().remove(tacheSelectionneViewList);
            }
        });

        Button boutonCreerTache = new Button("Creer Tâche");

        // avant d'activer le click sur le bouton, il faut que certains champs soient remplis
        // C'est à dire le nom et la description
        boutonCreerTache.disableProperty().bind(
                champTitre.textProperty().isEmpty()
                        .or(champDescription.textProperty().isEmpty()
                                .or(comboSection.valueProperty().isNull())
                        )
        ); // trouvé sur internet, à voir si ça marche vraiment // bon bah ça marche vraiment

        boutonCreerTache.setOnAction(event -> {
            String titre = champTitre.getText();
            String description = champDescription.getText();
            Section sectionChoisie = comboSection.getValue();
            LocalDate dD = dateDebut.getValue();
            LocalDate dF = dateFin.getValue();


            // On met toutes les données qu'on veut récupérer pour après créer la tâche (comme ça ça la crée que si l'utilisateur clique sur créer tache)
            TacheFille tache = new TacheFille(titre, description, dD, dF);

            // On récupère la section choisie en tant qu'objet
            tache.setSectionParente(sectionChoisie);
            modeleBureau.ajouterTache(tache, sectionChoisie);

            // Maintenant on ajoute les dépendances chronologiques à la tâche s'il y en a
            ObservableList<Tache> tachesSelectionnees = listViewTachesAvant.getItems();

            this.modeleBureau.ajouterDependances(tache, tachesSelectionnees);

            // On ferme la fenêtre une fois la tâche créée
            fenetreCreationTache.close();
        });

        // On met le tout dans une Vbox qui est le formulaire
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        layout.getChildren().addAll(titreTache, champTitre, labelDescription, champDescription,
                choixSection, comboSection, choixDate,tachesAvant, dependances,
                listViewTachesAvant, boutonCreerTache);


        // On crée la nouvelle scène et on lui ajoute le formulaire
        Scene scene = new Scene(layout, 400, 425);
        fenetreCreationTache.setScene(scene);
        fenetreCreationTache.showAndWait();

    }
}
