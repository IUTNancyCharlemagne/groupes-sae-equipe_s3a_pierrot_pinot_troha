package tralleno.Controleurs.Taches;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tralleno.Controleurs.Archivage.ControlArchiverTache;
import tralleno.Modele.ModeleBureau;
import tralleno.Taches.Tache;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Classe qui gère la modification d'une tâche lorsque l'utilisateur clique sur le bouton modifier d'une tâche
 */
public class ControlModifTache implements EventHandler<MouseEvent>, Serializable {

    /**
     * Modèle qui comporte les données de l'application
     */
    private ModeleBureau modeleBureau;

    /**
     * Tâche concernée par la modification
     */
    private Tache tacheAModifier;

    /**
     * Construit le contrôleur à partir du modele et de la tâche à modifier
     * @param modeleBureau
     * @param tacheAModifier
     */
    public ControlModifTache(ModeleBureau modeleBureau, Tache tacheAModifier) {
        this.modeleBureau = modeleBureau;
        this.tacheAModifier = tacheAModifier;
    }

    /**
     * Lorsque l'utilisateur clique sur le bouton "..." de la tâche, cette méthode est appelée.
     * Elle prend en charge l'évenement en permettant à l'utilisateur de modifier la tâche
     * @param mouseEvent
     */
    @Override
    public void handle(MouseEvent mouseEvent) {
        this.modeleBureau.setTacheCourante(this.tacheAModifier);

        // On crée un nouveau stage (fenêtre)
        Stage fenetreModificationTache = new Stage();
        fenetreModificationTache.initModality(Modality.APPLICATION_MODAL);
        fenetreModificationTache.setTitle("Modifier une Tâche");

        // Préparation du formulaire
        Label titreTache = new Label("Titre de la tâche:");
        titreTache.getStyleClass().add("titreChamp");
        Label labelDescription = new Label("Description:");
        labelDescription.getStyleClass().add("titreChamp");

        // Et évidemment il faut que les champs soient pré-remplis
        TextField champTitre = new TextField(tacheAModifier.getTitre());
        champTitre.getStyleClass().add("champTexteTache");
        TextField champDescription = new TextField(tacheAModifier.getDescription());
        champDescription.getStyleClass().add("champTexteTache");

        // Concernant les sections, étant donné que le drag and drop a été implémenté, on peut pour l'instant se passer
        // de la modification de section via le menu de modification d'une tâche
//        Label choixSection = new Label("Section :");
//        // On récupère les noms des sections du modèle pour la liste
//        List<Section> listeSections = this.modeleBureau.getSections();
//
//        ObservableList<Section> sections = FXCollections.observableArrayList(listeSections);
//        ComboBox<Section> comboSection = new ComboBox<>(sections);
//
//        // Et après on met en valeur déjà sélecionnée la section à laquelle la tache appartient
//        comboSection.setValue(this.tacheAModifier.getSectionParente());

        // Maintenant les dates de début et fin
        Label labelDateDebut = new Label("Date de début:");
        labelDateDebut.getStyleClass().add("titreChamp");
        DatePicker dateDebut = new DatePicker();
        dateDebut.getStyleClass().add("datePicker");

        Label labelDateFin = new Label("Date de fin:");
        labelDateFin.getStyleClass().add("titreChamp");
        DatePicker dateFin = new DatePicker();
        dateFin.getStyleClass().add("datePicker");

        // On met également les valeurs connues de la tâche à l'intérieur de chaque DatePicker
        dateDebut.setValue(this.tacheAModifier.getDateDebut());
        dateFin.setValue(this.tacheAModifier.getDateFin());

        // HBox pour mettre les deux selecteurs de date sur la même ligne
        HBox choixDate = new HBox(10);
        choixDate.getChildren().addAll(labelDateDebut, dateDebut, labelDateFin, dateFin);
        choixDate.getStyleClass().add("conteneurDates");


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
        tachesAvant.getStyleClass().add("titreChamp");
        List<Tache> taches = this.modeleBureau.getTaches();
        taches.remove(this.tacheAModifier);
        ObservableList<Tache> tachesAFaireAvant = FXCollections.observableArrayList(taches);
        ComboBox<Tache> comboTaches = new ComboBox<>(tachesAFaireAvant);
        comboTaches.getStyleClass().add("comboBox");


        // Bouton pour supprimer la tâche qu'on a selectionnée dans la viewlist
        Button supprimerTache = new Button("Supprimer tâche dépendante");
        supprimerTache.getStyleClass().add("Btn");

        HBox dependances = new HBox(10);
        dependances.getChildren().addAll(comboTaches, supprimerTache);
        dependances.getStyleClass().add("conteneurDependances");

        // La liste qui contient les tâches dont on veut la dépendance
        ListView<Tache> listViewTachesAvant = new ListView<>(); // Affiche les tâches sélectionnées pour la dépendance chronologique
        listViewTachesAvant.setPrefHeight(100);
        listViewTachesAvant.getStyleClass().add("listeTachesAvant");

        List<Tache> dependancesTacheCourante = this.modeleBureau.getDependancesTache(); // Récupère la liste des dépendances de la tâche actuelle
        ObservableList<Tache> dependancesTache = FXCollections.observableArrayList(dependancesTacheCourante);
        listViewTachesAvant.setItems(dependancesTache);



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

        Button boutonModifierTache = new Button("Modifier Tâche");
        boutonModifierTache.getStyleClass().add("Btn");

        Button boutonSupprimerTache = new Button("Supprimer la tâche");
        boutonSupprimerTache.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlSupprimerTache(this.modeleBureau, this.tacheAModifier, fenetreModificationTache));
        boutonSupprimerTache.getStyleClass().add("Btn");
        Button boutonArchiverTache = new Button("Archiver la tâche");
        boutonArchiverTache.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlArchiverTache(this.modeleBureau, this.tacheAModifier, fenetreModificationTache));
        boutonArchiverTache.getStyleClass().add("Btn");

        HBox actions = new HBox(10);
        actions.getChildren().addAll(boutonSupprimerTache, boutonArchiverTache);

        // avant d'activer le click sur le bouton, il faut que certains champs soient remplis
        // C'est à dire le nom et la description
        boutonModifierTache.disableProperty().bind(
                champTitre.textProperty().isEmpty()
                        .or(champDescription.textProperty().isEmpty()
//                                .or(comboSection.valueProperty().isNull())
                        )
        ); // trouvé sur internet, à voir si ça marche vraiment // bon bah ça marche vraiment


        boutonModifierTache.setOnAction(event -> {
            String titre = champTitre.getText();
            String description = champDescription.getText();
//            Section sectionChoisie = comboSection.getValue();
            LocalDate dD = dateDebut.getValue();
            LocalDate dF = dateFin.getValue();
            ObservableList<Tache> tachesSelectionnees = listViewTachesAvant.getItems();

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

                // Dans ce cas là, on va devoir modifier la tâche.
                this.tacheAModifier.setTitre(titre);
                this.tacheAModifier.setDescription(description);

                this.tacheAModifier.setDateDebut(dD);
                this.tacheAModifier.setDateFin(dF);
                // On récupère la section choisie en tant qu'objet et on l'ajoute après l'avoir changée de section
                // Il faut vérifier supprimer le
//                this.modeleBureau.changerSection(sectionChoisie);

                // Maintenant on ajoute les dépendances chronologiques à la tâche s'il y en a
               this.modeleBureau.ajouterDependances(tachesSelectionnees);
                System.out.println(tachesSelectionnees);

//                this.modeleBureau.ajouterDependances(tache, tachesSelectionnees);

                // On ferme la fenêtre une fois la tâche créée
                fenetreModificationTache.close();
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
        layout.getStyleClass().add("VBoxFormulaire");

        // choixSection, comboSection à ajouter aussi au layout
        layout.getChildren().addAll(titreTache, champTitre, labelDescription, champDescription,
                choixDate,tachesAvant, dependances,
                listViewTachesAvant, actions, boutonModifierTache);


        // On crée la nouvelle scène et on lui ajoute le formulaire
        Scene scene = new Scene(layout, 400, 400);
        scene.getStylesheets().add(getClass().getResource("/tralleno/css/popupStyle.css").toExternalForm());
        fenetreModificationTache.setScene(scene);
        fenetreModificationTache.showAndWait();

    }
}
