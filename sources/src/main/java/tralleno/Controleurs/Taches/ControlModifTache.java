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
import tralleno.Modele.ModeleBureau;
import tralleno.Section.Section;
import tralleno.Taches.Tache;
import tralleno.Taches.TacheFille;

import java.time.LocalDate;
import java.util.List;

public class ControlModifTache implements EventHandler<MouseEvent> {

    private ModeleBureau modeleBureau;
    private Tache tacheAModifier;

    public ControlModifTache(ModeleBureau modeleBureau, Tache tacheAModifier) {
        this.modeleBureau = modeleBureau;
        this.tacheAModifier = tacheAModifier;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        this.modeleBureau.setTacheCourante(this.tacheAModifier);

        // On crée un nouveau stage (fenêtre)
        Stage fenetreModificationTache = new Stage();
        fenetreModificationTache.initModality(Modality.APPLICATION_MODAL);
        fenetreModificationTache.setTitle("Modifier une Tâche");

        // Préparation du formulaire
        Label titreTache = new Label("Titre de la tâche:");
        Label labelDescription = new Label("Description:");

        // Et évidemment il faut que les champs soient pré-remplis
        TextField champTitre = new TextField(tacheAModifier.getTitre());
        TextField champDescription = new TextField(tacheAModifier.getDescription());

        Label choixSection = new Label("Section :");
        // On récupère les noms des sections du modèle pour la liste
        List<Section> listeSections = this.modeleBureau.getSections();

        ObservableList<Section> sections = FXCollections.observableArrayList(listeSections);
        ComboBox<Section> comboSection = new ComboBox<>(sections);

        // Et après on met en valeur déjà sélecionnée la section à laquelle la tache appartient
        comboSection.setValue(this.tacheAModifier.getSectionParente());

        // Maintenant les dates de début et fin
        Label labelDateDebut = new Label("Date de début:");
        DatePicker dateDebut = new DatePicker();

        Label labelDateFin = new Label("Date de fin:");
        DatePicker dateFin = new DatePicker();

        // On met également les valeurs connues de la tâche à l'intérieur de chaque DatePicker
        dateDebut.setValue(this.tacheAModifier.getDateDebut());
        dateFin.setValue(this.tacheAModifier.getDateFin());

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

        // La liste qui contient les tâches dont on veut la dépendance
        ListView<Tache> listViewTachesAvant = new ListView<>(); // Affiche les tâches sélectionnées pour la dépendance chronologique
        listViewTachesAvant.setPrefHeight(100);

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

        Button boutonSupprimerTache = new Button("Supprimer la tâche");
        boutonSupprimerTache.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlSupprimerTache(this.modeleBureau, this.tacheAModifier, fenetreModificationTache));
        Button boutonArchiverTache = new Button("Archiver la tâche");

        HBox actions = new HBox(10);
        actions.getChildren().addAll(boutonSupprimerTache, boutonArchiverTache);

        // avant d'activer le click sur le bouton, il faut que certains champs soient remplis
        // C'est à dire le nom et la description
        boutonModifierTache.disableProperty().bind(
                champTitre.textProperty().isEmpty()
                        .or(champDescription.textProperty().isEmpty()
                                .or(comboSection.valueProperty().isNull())
                        )
        ); // trouvé sur internet, à voir si ça marche vraiment // bon bah ça marche vraiment


        boutonModifierTache.setOnAction(event -> {
            String titre = champTitre.getText();
            String description = champDescription.getText();
            Section sectionChoisie = comboSection.getValue();
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
                this.modeleBureau.changerSection(sectionChoisie);

                // Maintenant on ajoute les dépendances chronologiques à la tâche s'il y en a
               this.modeleBureau.ajouterDependances(tachesSelectionnees);

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

        layout.getChildren().addAll(titreTache, champTitre, labelDescription, champDescription,
                choixSection, comboSection, choixDate,tachesAvant, dependances,
                listViewTachesAvant, actions, boutonModifierTache);


        // On crée la nouvelle scène et on lui ajoute le formulaire
        Scene scene = new Scene(layout, 400, 440);
        fenetreModificationTache.setScene(scene);
        fenetreModificationTache.showAndWait();

    }
}
