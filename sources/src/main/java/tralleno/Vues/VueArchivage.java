package tralleno.Vues;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import tralleno.Controleurs.Sections.ControlSupprimerSection;
import tralleno.Controleurs.Taches.ControlSupprimerTache;
import tralleno.Modele.ModeleBureau;
import tralleno.Modele.Sujet;
import tralleno.Section.Section;
import tralleno.Taches.Tache;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ListIterator;
import java.util.Set;

public class VueArchivage extends VBox implements Observateur, Serializable{

    private ModeleBureau modeleBureau;

    private transient ComboBox<String> choixListe;

    private transient ListView<HBox> vueListe;


    public VueArchivage(ModeleBureau modeleBureau){
        super();
        this.modeleBureau = modeleBureau;

        // On crée une ComboBox qui permet de choisir entre la visualisation soit des tâches archivées soit des sections archivées.
        this.choixListe = new ComboBox<>();
        this.choixListe.getItems().addAll("Tâches Archivées", "Sections Archivées");
        this.choixListe.setValue("Tâches Archivées");


        this.vueListe = new ListView<>();
        this.getChildren().addAll(choixListe, vueListe);


        this.actualiser(this.modeleBureau);
    }

    @Override
    public void actualiser(Sujet s) {
        vueListe.getItems().clear();
        choixListe.setOnAction(event -> {
            if (choixListe.getValue().equals("Tâches Archivées")) {
                // On parcourt les tâches archivées du modèle pour les afficher sous forme d'élément graphique
                ListIterator<Tache> iterateur = this.modeleBureau.getTachesArchivees().listIterator(this.modeleBureau.getTachesArchivees().size());
                while(iterateur.hasPrevious()) {
                    Tache tache = iterateur.previous();
                    VBox tacheBox = creerTache(tache.getTitre(), tache.getDescription());

                    // Bouton pour supprimer la tâche
                    Button supprimerBouton = new Button("Supprimer");
                    supprimerBouton.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlSupprimerTache(this.modeleBureau, tache, null));

                    Button restaurerBouton = new Button("Restaurer");
                    // Controleur restaurer

                    VBox boutons = new VBox();
                    boutons.getChildren().addAll(supprimerBouton, restaurerBouton);

                    HBox element = new HBox();
                    element.getChildren().addAll(tacheBox, boutons);

                    vueListe.getItems().add(element);
                }
            } else if (choixListe.getValue().equals("Sections Archivées")) {
                ListIterator<Section> iterateur = this.modeleBureau.getSectionsArchivees().listIterator(this.modeleBureau.getSectionsArchivees().size());
                while(iterateur.hasPrevious()) {
                    Section section = iterateur.previous();
                    VBox tacheBox = creerTache(section.getNom(), "SECTION");

                    // Bouton pour supprimer la tâche
                    Button supprimerBouton = new Button("Supprimer");
                    supprimerBouton.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlSupprimerSection(this.modeleBureau, section, null));

                    Button restaurerBouton = new Button("Restaurer");
                    // Controleur restaurer

                    VBox boutons = new VBox();
                    boutons.getChildren().addAll(supprimerBouton, restaurerBouton);

                    HBox element = new HBox();
                    element.getChildren().addAll(tacheBox, boutons);

                    vueListe.getItems().add(element);
                }

            }
        });


    }


    public VBox creerTache(String titre, String description){
        VBox vBox = new VBox();

        Label titreLabel = new Label(titre);
        String descriptionAbregee = description.length() > 20 ? description.substring(0, 20) + "..." : description;
        Label descriptionLabel = new Label(descriptionAbregee);

        titreLabel.setFont(Font.font("Arial", 14));
        descriptionLabel.setFont(Font.font("Arial", 10));

        titreLabel.setWrapText(true);
        descriptionLabel.setWrapText(true);

        VBox texteBox = new VBox(titreLabel, descriptionLabel);
        texteBox.setPadding(new Insets(5));
        texteBox.setSpacing(5);

        texteBox.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10px; " +
                "-fx-border-color: black; -fx-border-width: 1px;");

        vBox.getChildren().add(texteBox);

        return vBox;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        // Il faut que la liste soit réinitialisée après la désérialisation des données de l'application
        // Parce que comme c'est un élément graphique on ne peut pas la sérialiser
        this.vueListe = new ListView<>();
        this.choixListe = new ComboBox<>();
    }



//    private void setUpChoixListe(){
//
//    }


//    public void actualiserSansEvent(){
//        vueListe.getItems().clear();
//        if(choixListe.getValue() != null){
//            if (choixListe.getValue().equals("Tâches Archivées")) {
//                // On parcourt les tâches archivées du modèle pour les afficher sous forme d'élément graphique
//                ListIterator<Tache> iterateur = this.modeleBureau.getTachesArchivees().listIterator(this.modeleBureau.getTachesArchivees().size());
//                while(iterateur.hasPrevious()) {
//                    Tache tache = iterateur.previous();
//                    VBox tacheBox = creerTache(tache.getTitre(), tache.getDescription());
//
//                    // Bouton pour supprimer la tâche
//                    Button supprimerBouton = new Button("Supprimer");
//                    supprimerBouton.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlSupprimerTache(this.modeleBureau, tache, null));
//
//                    Button restaurerBouton = new Button("Restaurer");
//                    // Controleur restaurer
//
//                    VBox boutons = new VBox();
//                    boutons.getChildren().addAll(supprimerBouton, restaurerBouton);
//
//                    HBox element = new HBox();
//                    element.getChildren().addAll(tacheBox, boutons);
//
//                    vueListe.getItems().add(element);
//                }
//            } else if (choixListe.getValue().equals("Sections Archivées")) {
//                ListIterator<Section> iterateur = this.modeleBureau.getSectionsArchivees().listIterator(this.modeleBureau.getSectionsArchivees().size());
//                while(iterateur.hasPrevious()) {
//                    Section section = iterateur.previous();
//                    VBox tacheBox = creerTache(section.getNom(), "SECTION");
//
//                    // Bouton pour supprimer la tâche
//                    Button supprimerBouton = new Button("Supprimer");
//                    supprimerBouton.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlSupprimerSection(this.modeleBureau, section, null));
//
//                    Button restaurerBouton = new Button("Restaurer");
//                    // Controleur restaurer
//
//                    VBox boutons = new VBox();
//                    boutons.getChildren().addAll(supprimerBouton, restaurerBouton);
//
//                    HBox element = new HBox();
//                    element.getChildren().addAll(tacheBox, boutons);
//
//                    vueListe.getItems().add(element);
//                }
//            }
//        }
//
//    }
}


