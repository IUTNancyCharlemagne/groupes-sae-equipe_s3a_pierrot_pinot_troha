package tralleno.Controleurs.Taches;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tralleno.Controleurs.Archivage.ControlArchiverTache;
import tralleno.Modele.ModeleBureau;
import tralleno.Section.Section;
import tralleno.Taches.Tache;
import tralleno.Taches.TacheMere;
import tralleno.Vues.VuePrincipale;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui représente le formulaire retourné lors de la création ou modification d'une tâche
 */
public class FormulaireTache implements Serializable {

    /**
     * Méthode, qui selon ses attributs retourne un formulaire de création/modification adapté à la tâche
     * @param modeleBureau
     * @param tacheAModifier
     * @param tacheParente
     * @param fenetreActionTache
     * @return
     */
    public static Scene creerFormulaireTache(ModeleBureau modeleBureau, Tache tacheAModifier, TacheMere tacheParente, Stage fenetreActionTache){

        // Préparation des données du formulaire
        Label titreTache = new Label("Titre de la tâche:");
        titreTache.getStyleClass().add("titreChamp");
        Label labelDescription = new Label("Description:");
        labelDescription.getStyleClass().add("titreChamp");

        TextField champTitre = new TextField();
        champTitre.getStyleClass().add("champTexteTache");
        TextField champDescription = new TextField();
        champDescription.getStyleClass().add("champTexteTache");

        ComboBox<Section> comboSection = new ComboBox<>();

        Label labelDateDebut = new Label("Date de début :");
        labelDateDebut.getStyleClass().add("titreChamp");
        DatePicker dateDebut = new DatePicker();
        dateDebut.getStyleClass().add("datePicker");

        Label labelDateFin = new Label("Date de fin :");
        labelDateFin.getStyleClass().add("titreChamp");
        DatePicker dateFin = new DatePicker();
        dateFin.getStyleClass().add("datePicker");


        VBox dateDebutBox = new VBox(5);
        dateDebutBox.getChildren().addAll(labelDateDebut, dateDebut);

        VBox dateFinBox = new VBox(5);
        dateFinBox.getChildren().addAll(labelDateFin, dateFin);


        HBox champsDate = new HBox(20);
        champsDate.getChildren().addAll(dateDebutBox, dateFinBox);

        if(tacheAModifier != null){ // On doit le faire avant l'afficher de tachesDisponibles()
            // On pré-remplit aussi les dates
            // On met également les valeurs connues de la tâche à l'intérieur de chaque DatePicker
            dateDebut.setValue(tacheAModifier.getDateDebut());
            dateFin.setValue(tacheAModifier.getDateFin());
        }


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
        List<Tache> taches = modeleBureau.getTachesDisponibles(dateDebut.getValue());
        ObservableList<Tache> tachesAFaireAvant = FXCollections.observableArrayList(taches);
        ComboBox<Tache> comboTaches = new ComboBox<>(tachesAFaireAvant);
        comboTaches.setPrefWidth(100);
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


        // Pour les dépendances chronologiques on doit ajouter des listeners pour que la liste des tâches disponibles s'actualise
        dateDebut.setOnAction(event -> {
            LocalDate nouvelleDateDebut = dateDebut.getValue();

            if (nouvelleDateDebut != null) {
                List<Tache> tachesDisponibles = modeleBureau.getTachesDisponibles(nouvelleDateDebut);
                tachesAFaireAvant.setAll(tachesDisponibles);

                // Aussi vérifier les éléments de la listView, si la date est changée et que certaines des tâches qui étaient à faire ne sont plus disponibles car
                // Leur date de fin sera après la date de début de la tâche actuelle.
                // Donc supprimer les tâches qui ne sont plus faisables avant la date de début de la tâche actuelle
                listViewTachesAvant.getItems().removeIf(tache -> tache.getDateFin().isAfter(nouvelleDateDebut));
            }
        });


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

        Button boutonActionTache = new Button();

        // Lorsque l'utilisateur clique sur le bouton d'action (modifier ou créer une tâche)
        boutonActionTache.setOnAction(event -> {
            // On récupère les informations renseignées par l'utilisateur
            String titre = champTitre.getText();
            String description = champDescription.getText();
            Section sectionChoisie = null;
            if(tacheAModifier == null){
                sectionChoisie = comboSection.getValue();
            }
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
                // Si les dates sont valides, alors on peut créer/modifier la tâche

                if (tacheAModifier != null) { // On est dans le cas où on modifie une tâche
                    // Dans ce cas là, on va devoir modifier la tâche.
                    tacheAModifier.setTitre(titre);
                    tacheAModifier.setDescription(description);

                    tacheAModifier.setDateDebut(dD);
                    tacheAModifier.setDateFin(dF);
                }else{ // On est dans le cas où on crée une tâche
                    // Donc on la crée
                    TacheMere tache = new TacheMere(titre, description, dD, dF);
                    modeleBureau.setTacheCourante(tache);

                    // On récupère la section choisie en tant qu'objet
                    modeleBureau.setSectionCourante(sectionChoisie);
                    modeleBureau.ajouterTache(sectionChoisie.getTaches().size());
                }


                // Maintenant on ajoute les dépendances chronologiques à la tâche s'il y en a
                ObservableList<Tache> tachesSelectionnees = listViewTachesAvant.getItems();
                modeleBureau.ajouterDependances(tachesSelectionnees);

                // On ferme la fenêtre une fois la tâche créée
                fenetreActionTache.close();
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
        int taillePopUp = 475;
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getStyleClass().add("VBoxFormulaire");


        // Si tacheAModifier vaut null, cela veut dire qu'on se trouve dans le cas où on cherche à créer une tâche, et non à en modifier une
        if(tacheAModifier == null){

            // Quand on crée une tâche, il faut pouvoir l'ajouter à la section choisie, car quand on la modifie, le choix de la section se fait via le drag and drop
            Label choixSection = new Label("Section :");
            choixSection.getStyleClass().add("titreChamp");
            // On récupère les noms des sections du modèle pour que l'utilisateur puisse faire son choix
            List<Section> listeSections = modeleBureau.getSections();

            ObservableList<Section> sections = FXCollections.observableArrayList(listeSections);
            comboSection.setItems(sections);
            comboSection.getStyleClass().add("comboBox");


            // Puis le bouton final pour créer la tâche
            boutonActionTache.setText("Créer");
            boutonActionTache.getStyleClass().add("Btn");
            VBox boutonContainer = new VBox(boutonActionTache);
            boutonContainer.setAlignment(Pos.CENTER);

            // avant d'activer le click sur le bouton, il faut que certains champs soient remplis
            // C'est à dire le nom et la description de la tâche
            boutonActionTache.disableProperty().bind(
                    champTitre.textProperty().isEmpty()
                            .or(champDescription.textProperty().isEmpty()
                                    .or(comboSection.valueProperty().isNull())
                            )
            );

            layout.getChildren().addAll(titreTache, champTitre, labelDescription, champDescription,
                    choixSection, comboSection, champsDate,tachesAvant, dependances,
                    listViewTachesAvant, boutonContainer);

        }else{

            // Dans le cas où on est dans la modification d'une tâche, on doit pré-remplir les champs
            champTitre.setText(tacheAModifier.getTitre());
            champTitre.getStyleClass().add("champTexteTache");
            champDescription.setText(tacheAModifier.getDescription());
            champDescription.getStyleClass().add("champTexteTache");




            List<Tache> dependancesTacheCourante = modeleBureau.getDependancesTache(); // Récupère la liste des dépendances de la tâche actuelle
            ObservableList<Tache> dependancesTache = FXCollections.observableArrayList(dependancesTacheCourante);
            listViewTachesAvant.setItems(dependancesTache);

            boutonActionTache.setText("Modifier");
            boutonActionTache.getStyleClass().add("Btn");
            VBox boutonContainer = new VBox(boutonActionTache);
            boutonContainer.setAlignment(Pos.CENTER);

            Button boutonSupprimerTache = new Button("Supprimer la tâche");
            boutonSupprimerTache.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlSupprimerTache(modeleBureau, tacheAModifier, fenetreActionTache));
            boutonSupprimerTache.getStyleClass().add("Btn");

            HBox actions = new HBox(10);
            actions.setAlignment(Pos.CENTER);
            actions.getChildren().add(boutonSupprimerTache);


            // On ne peut archiver que les tâches de niveau 1, et pas les sous-tâches directement
            // Donc on vérifie si la tacheParente est nulle, si oui, on peut ajouter le bouton d'archivage à la tâche
            if (tacheParente == null) {
                Button boutonArchiverTache = new Button("Archiver la tâche");
                boutonArchiverTache.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlArchiverTache(modeleBureau, tacheAModifier, fenetreActionTache));
                boutonArchiverTache.getStyleClass().add("Btn");
                actions.getChildren().add(boutonArchiverTache);
            }

            // avant d'activer le click sur le bouton, il faut que certains champs soient remplis
            // C'est à dire le nom et la description
            boutonActionTache.disableProperty().bind(
                    champTitre.textProperty().isEmpty()
                            .or(champDescription.textProperty().isEmpty()
                            )
            );

            // Pour l'affichage des dates, on doit vérifier que la tâche n'est pas une sous-tâche, si c'est le cas,
            // On ne doit pas laisser la possibilité d'attribuer une date.
            if(tacheParente != null){
                layout.getChildren().addAll(titreTache, champTitre, labelDescription, champDescription,
                        actions, boutonContainer);
                taillePopUp = 250;
            }else{
                layout.getChildren().addAll(titreTache, champTitre, labelDescription, champDescription,
                        champsDate,tachesAvant, dependances,
                        listViewTachesAvant, actions, boutonContainer);
            }


        }

        // On crée la nouvelle scène et on lui ajoute le formulaire
        Scene scene = new Scene(layout, 400, taillePopUp);


        return scene;
    }
}
