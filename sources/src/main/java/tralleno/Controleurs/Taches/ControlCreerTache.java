package tralleno.Controleurs.Taches;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tralleno.Modele.ModeleBureau;
import tralleno.Section.Section;
import tralleno.Taches.Tache;
import tralleno.Taches.TacheMere;
import tralleno.Vues.VuePrincipale;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Classe qui gère la création d'une tâche lorsque l'utilisateur clique sur le bouton pour
 */
public class ControlCreerTache implements EventHandler<MouseEvent>, Serializable {

    /**
     * Modèle qui comporte les données de l'application
     */
    private ModeleBureau modeleBureau;

    /**
     * Crée le contrôleur de création de tâche uniquement à partir du modèle
     * @param modeleBureau
     */
    public ControlCreerTache(ModeleBureau modeleBureau){
        this.modeleBureau = modeleBureau;
    }

    /**
     * Lorsque l'utilisateur clique sur un bouton créer tâche, cette méthode est appelée.
     * Elle prend en charge l'évenement en créant une tâche à partir des informations
     * que l'utilisateur choisira de renseigner
     * @param mouseEvent
     */
    @Override
    public void handle(MouseEvent mouseEvent) {
        // On crée un nouveau stage (fenêtre)
        Stage fenetreCreationTache = new Stage();
        fenetreCreationTache.initModality(Modality.APPLICATION_MODAL);
        fenetreCreationTache.setTitle("Créer une Tâche");

        // Préparation du formulaire
        Label titreTache = new Label("Titre de la tâche:");
        titreTache.getStyleClass().add("titreChamp");
        TextField champTitre = new TextField();
        champTitre.getStyleClass().add("champTexteTache");

        Label labelDescription = new Label("Description:");
        labelDescription.getStyleClass().add("titreChamp");
        TextField champDescription = new TextField();
        champDescription.getStyleClass().add("champTexteTache");

        Label choixSection = new Label("Section :");
        choixSection.getStyleClass().add("titreChamp");
        // On récupère les noms des sections du modèle pour que l'utilisateur puisse faire son choix
        List<Section> listeSections = this.modeleBureau.getSections();

        ObservableList<Section> sections = FXCollections.observableArrayList(listeSections);
        ComboBox<Section> comboSection = new ComboBox<>(sections);
        comboSection.getStyleClass().add("comboBox");

        // Maintenant les dates de début et fin
        Label labelDateDebut = new Label("Date de début:");
        labelDateDebut.getStyleClass().add("titreChamp");
        DatePicker dateDebut = new DatePicker();
        dateDebut.getStyleClass().add("datePicker");

        Label labelDateFin = new Label("Date de fin:");
        labelDateFin.getStyleClass().add("titreChamp");
        DatePicker dateFin = new DatePicker();
        dateFin.getStyleClass().add("datePicker");

        // HBox pour mettre les deux selecteurs de date sur la même ligne
        HBox choixDate = new HBox(10);
        choixDate.getChildren().addAll(labelDateDebut, dateDebut, labelDateFin, dateFin);
        choixDate.getStyleClass().add("conteneurDates");


        // Vérification de la date de début et de fin
        dateDebut.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && dateFin.getValue() != null) {
                if (newValue.isAfter(dateFin.getValue())) {
                    // Si la date de début est après la date de fin on efface la date de fin
                    dateFin.setValue(null);
                }
            }
        });

        dateFin.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && dateDebut.getValue() != null) {
                if (newValue.isBefore(dateDebut.getValue())) {
                    // Si la date de fin est avant la date de début on efface la date de début
                    dateDebut.setValue(null);
                }
            }
        });


        // Pour les dépendances chronologiques
        Label tachesAvant = new Label("Tâches à faire avant :");
        tachesAvant.getStyleClass().add("titreChamp");
        List<Tache> taches = this.modeleBureau.getTaches();
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

        // Puis le bouton final pour créer la tâche
        Button boutonCreerTache = new Button("Creer Tâche");
        boutonCreerTache.getStyleClass().add("Btn");

        // avant d'activer le click sur le bouton, il faut que certains champs soient remplis
        // C'est à dire le nom et la description de la tâche
        boutonCreerTache.disableProperty().bind(
                champTitre.textProperty().isEmpty()
                        .or(champDescription.textProperty().isEmpty()
                                .or(comboSection.valueProperty().isNull())
                        )
        );

        // Lorsque l'utilisateur clique sur le bouton créer d'une tâche.
        boutonCreerTache.setOnAction(event -> {
            // On récupère les informations renseignées par l'utilisateur
            String titre = champTitre.getText();
            String description = champDescription.getText();
            Section sectionChoisie = comboSection.getValue();
            LocalDate dD = dateDebut.getValue();
            LocalDate dF = dateFin.getValue();

            // Long processus de vérification de cohérence entre les dates
            // L'utilisateur, soit n'en choisit aucune des deux (laisse vide)
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
            // Si les dates sont valides, alors on peut créer la tâche
            TacheMere tache = new TacheMere(titre, description, dD, dF);
            this.modeleBureau.setTacheCourante(tache);

            // On récupère la section choisie en tant qu'objet
            this.modeleBureau.setSectionCourante(sectionChoisie);
            modeleBureau.ajouterTache();

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


        // On met le tout dans une Vbox qui est le conteneur du formulaire
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getStyleClass().add("VBoxFormulaire");

        layout.getChildren().addAll(titreTache, champTitre, labelDescription, champDescription,
                choixSection, comboSection, choixDate,tachesAvant, dependances,
                listViewTachesAvant, boutonCreerTache);

        // On crée la nouvelle scène et on lui ajoute le formulaire
        Scene scene = new Scene(layout, 400, 425);

        int themeApp = VuePrincipale.getThemeCourant();
        switch (themeApp){
            case 1:
                scene.getStylesheets().clear();
                scene.getStylesheets().add(getClass().getResource("/tralleno/css/Base/popupStyleBase.css").toExternalForm());
                break;
            case 2:
                scene.getStylesheets().clear();
                scene.getStylesheets().add(getClass().getResource("/tralleno/css/Blue/popupStyleBlue.css").toExternalForm());
                break;
            default:
                scene.getStylesheets().clear();
                scene.getStylesheets().add(getClass().getResource("/tralleno/css/Base/popupStyleBase.css").toExternalForm());
                break;
        }

        fenetreCreationTache.setScene(scene);
        fenetreCreationTache.showAndWait();

    }
}
