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


        // Vérification de la date de début et de fin
        dateDebut.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && dateFin.getValue() != null) {
                if (newValue.isAfter(dateFin.getValue())) {
                    // Si la date de début est après la date de fin, efface la date de fin
                    dateFin.setValue(null);
                }
            }
        });

        dateFin.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && dateDebut.getValue() != null) {
                if (newValue.isBefore(dateDebut.getValue())) {
                    // Si la date de fin est avant la date de début, efface la date de début
                    dateDebut.setValue(null);
                }
            }
        });


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

            // Long processus de vérfication de cohérence entre les dates
            // L'utilisateur, soit n'en choisi aucune des deux (laisse vide)
            // Soit il doit renseigner les deux dates, et la date de début doit être avant la date de fin
            boolean datesValides = true;
            String messageErreur = "";

            if ((dD == null && dF != null) || (dD != null && dF == null)) { // si une des dates est vide alors que l'autre est renseignée
                datesValides = false;
                messageErreur = "Veuillez remplir les deux champs de date ou laisser les deux vides.";
            } else if (dD != null && dF != null && dD.isAfter(dF)) { // Si la date de début est après la date de fin
                datesValides = false;
                messageErreur = "La date de début ne peut pas être postérieure à la date de fin.";
            }

            if (datesValides) {
            TacheFille tache = new TacheFille(titre, description, dD, dF);
            this.modeleBureau.setTacheCourante(tache);
            // On récupère la section choisie en tant qu'objet
            tache.setSectionParente(sectionChoisie);
            modeleBureau.ajouterTache(sectionChoisie);

            // Maintenant on ajoute les dépendances chronologiques à la tâche s'il y en a
            ObservableList<Tache> tachesSelectionnees = listViewTachesAvant.getItems();

            this.modeleBureau.ajouterDependances(tachesSelectionnees);

            // On ferme la fenêtre une fois la tâche créée
            fenetreCreationTache.close();
            } else {
                // On affiche une alerte à l'utilisateur si les dates sont pas cohérentes
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de dates");
                alert.setHeaderText(null);
                alert.setContentText(messageErreur);
                alert.showAndWait();
            }
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
