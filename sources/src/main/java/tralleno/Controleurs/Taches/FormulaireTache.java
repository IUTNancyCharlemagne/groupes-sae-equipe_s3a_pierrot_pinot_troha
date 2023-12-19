//package tralleno.Controleurs.Taches;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.scene.control.*;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import tralleno.Section.Section;
//import tralleno.Taches.TacheFille;
//
//import java.lang.module.ModuleFinder;
//import java.time.LocalDate;
//import java.util.List;
//
//public class FormulaireTache {
//
//    public static int CREER = 0;
//
//    public static int MODIFIER = 1;
//
//    public static VBox getFormulaire(int i) {
//        // Préparation du formulaire
//        Label titreTache = new Label("Titre de la tâche:");
//        TextField champTitre = new TextField();
//
//        Label labelDescription = new Label("Description:");
//        TextField champDescription = new TextField();
//
//        Label choixSection = new Label("Section :");
//        // On récupère les noms des sections du modèle
//        List<String> listeSections = this.modeleBureau.getNomSections();
//
//        ObservableList<String> sections = FXCollections.observableArrayList(listeSections);
//        ComboBox<String> comboSection = new ComboBox<>(sections);
//
//        // Maintenant les dates de début et fin
//        Label labelDateDebut = new Label("Date de début:");
//        DatePicker dateDebut = new DatePicker();
//
//        Label labelDateFin = new Label("Date de fin:");
//        DatePicker dateFin = new DatePicker();
//
//        // HBox pour mettre les deux selecteurs de date sur la même ligne
//        HBox choixDate = new HBox(10);
//        choixDate.getChildren().addAll(labelDateDebut, dateDebut, labelDateFin, dateFin);
//
//
//        // Pour les dépendances chronologiques
//        Label tachesAvant = new Label("Tâches à faire avant :");
//        List<String> taches = this.modeleBureau.getTitreTaches();
//        ObservableList<String> tachesAFaireAvant = FXCollections.observableArrayList(taches);
//        ComboBox<String> comboTaches = new ComboBox<>(tachesAFaireAvant);
//
//        // Bouton pour supprimer la tâche qu'on a selectionnée dans la viewlist
//        Button supprimerTache = new Button("Supprimer tâche dépendante");
//
//        HBox dependances = new HBox(10);
//        dependances.getChildren().addAll(comboTaches, supprimerTache);
//
//        ListView<String> listViewTachesAvant = new ListView<>(); // Affiche les tâches sélectionnées pour la dépendance chronologique
//        listViewTachesAvant.setPrefHeight(100);
//
//// EventHandler pour que la liste des tâches select se mette à jour
//        comboTaches.setOnAction(event -> {
//            String tacheSelectionnee = comboTaches.getValue();
//            if (tacheSelectionnee != null && !listViewTachesAvant.getItems().contains(tacheSelectionnee)) {
//                listViewTachesAvant.getItems().add(tacheSelectionnee);
//            }
//        });
//
//        supprimerTache.setOnAction(event -> {
//            String tacheSelectionneViewList = listViewTachesAvant.getSelectionModel().getSelectedItem();
//            if (tacheSelectionneViewList != null) {
//                listViewTachesAvant.getItems().remove(tacheSelectionneViewList);
//            }
//        });
//
//        Button boutonActionTache = null;
//
//        // avant d'activer le click sur le bouton, il faut que certains champs soient remplis
//        // C'est à dire le nom et la description
//        boutonActionTache.disableProperty().bind(
//                champTitre.textProperty().isEmpty()
//                        .or(champDescription.textProperty().isEmpty()
//                                .or(comboSection.valueProperty().isNull())
//                        )
//        ); // trouvé sur internet, à voir si ça marche vraiment // bon bah ça marche vraiment
//
//        // ### Cas o
//        if(i == MODIFIER){
//            boutonActionTache = new Button("Modifier");
//            boutonActionTache.setOnAction(event -> {
//                String titre = champTitre.getText();
//                String description = champDescription.getText();
//                String sectionChoisie = comboSection.getValue();
//                LocalDate dD = dateDebut.getValue();
//                LocalDate dF = dateFin.getValue();
//
//
//                // On met toutes les données qu'on veut récupérer pour après créer la tâche (comme ça ça la crée que si l'utilisateur clique sur créer tache)
//                TacheFille tache = new TacheFille(titre, description, dD, dF);
//
//                // Maintenant on ajoute les dépendances chronologiques à la tâche s'il y en a
//                ObservableList<String> tachesSelectionnees = listViewTachesAvant.getSelectionModel().getSelectedItems();
//
//                // On récupère la section choisie en tant qu'objet
//                Section section = this.modeleBureau.getSection(sectionChoisie);
//                System.out.println(section.getNom());
//                modeleBureau.ajouterTache(tache, section);
//
//                fenetreCreationTache.close(); // Fermer la fenêtre une fois la tâche créée
//            });
//
//        }else{
//            boutonActionTache = new Button("Créer");
//        }
//
//
//    }
//}
