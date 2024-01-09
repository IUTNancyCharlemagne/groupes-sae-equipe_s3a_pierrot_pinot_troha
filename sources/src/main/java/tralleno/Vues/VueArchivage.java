package tralleno.Vues;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import tralleno.Controleurs.Restauration.ControlRestaurerSection;
import tralleno.Controleurs.Restauration.ControlRestaurerTache;
import tralleno.Controleurs.Sections.ControlSupprimerSection;
import tralleno.Controleurs.Taches.ControlSupprimerTache;
import tralleno.Modele.ModeleBureau;
import tralleno.Modele.Sujet;
import tralleno.Section.Section;
import tralleno.Taches.Tache;
import javafx.scene.control.ScrollPane;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ListIterator;
import java.util.Set;

public class VueArchivage extends VBox implements Observateur, Serializable{

    private ModeleBureau modeleBureau;

    private transient Button tachesArchivees, sectionsArchivees;
    private transient Button boutonCourant;

    private transient VBox vueListe;


    public VueArchivage(ModeleBureau modeleBureau){
        super();
        this.modeleBureau = modeleBureau;

        // On crée deux boutons, pour voir les tâches archivées ou les sections archivées
        HBox boutonsTachesSection = new HBox();
        boutonsTachesSection.getStyleClass().add("boutonsTachesSection");
        this.tachesArchivees = new Button("Tâches Archivées");
        this.tachesArchivees.setAlignment(Pos.CENTER);
        this.tachesArchivees.getStyleClass().clear();
        this.tachesArchivees.getStyleClass().add("BtnTacheSelected");
        this.sectionsArchivees = new Button("Sections Archivées");
        this.sectionsArchivees.setAlignment(Pos.CENTER);
        this.sectionsArchivees.getStyleClass().clear();
        this.sectionsArchivees.getStyleClass().add("BtnSectionNotSelected");
        this.boutonCourant = this.tachesArchivees;
        boutonsTachesSection.getChildren().addAll(this.tachesArchivees, this.sectionsArchivees);


        this.vueListe = new VBox();
        this.vueListe.setPadding(new Insets(15, 3, 0, 3));
        this.vueListe.setSpacing(5);
        this.vueListe.getStyleClass().add("listeElementArchive");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("ScrollPaneArchivage");
        scrollPane.setContent(this.vueListe);

        //this.vueListe.getStyleClass().add("tousLesElementsArchives");
        VBox.setVgrow(this.vueListe, Priority.ALWAYS);
        this.getChildren().addAll(boutonsTachesSection, scrollPane);

        // 1er appel avant que la Vue soit sérialisée
        this.tachesArchivees.setOnAction(event -> {
            if(!(this.boutonCourant.equals(this.tachesArchivees))){
                this.changerClasseCSSBoutons();
            }
            this.boutonCourant = this.tachesArchivees;
            mettreAJourListeArchivage();
        });
        this.sectionsArchivees.setOnAction(event -> {
            if(!(this.boutonCourant.equals(this.sectionsArchivees))){
                this.changerClasseCSSBoutons();
            }
            this.boutonCourant = this.sectionsArchivees;
            mettreAJourListeArchivage();

        });

        this.actualiser(this.modeleBureau);
        this.getStyleClass().add("archivage");
    }

    @Override
    public void actualiser(Sujet s) {
        mettreAJourListeArchivage();
    }


    public VBox creerTache(String titre, String description){
        VBox vBox = new VBox();

        Label titreLabel = new Label(titre);
        titreLabel.getStyleClass().add("titreTacheArchivee");
        String descriptionAbregee = description.length() > 20 ? description.substring(0, 20) + "..." : description;
        Label descriptionLabel = new Label(descriptionAbregee);
        descriptionLabel.getStyleClass().add("descriptionTacheAbregee");

        titreLabel.setFont(Font.font("Arial", 14));
        descriptionLabel.setFont(Font.font("Arial", 10));

        titreLabel.setWrapText(true);
        descriptionLabel.setWrapText(true);

        VBox texteBox = new VBox(titreLabel, descriptionLabel);
        texteBox.setPadding(new Insets(5));
        texteBox.setSpacing(5);
        texteBox.getStyleClass().add("texteBox");

        vBox.getChildren().add(texteBox);

        return vBox;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        // Il faut que la liste soit réinitialisée après la désérialisation des données de l'application
        // Parce que comme c'est un élément graphique on ne peut pas la sérialiser
        this.vueListe = new VBox();
        this.tachesArchivees = new Button("Tâches Archivées");
        this.sectionsArchivees = new Button("Sections Archivées");

        // Il faut aussi rajouter le listener d'évenement
        this.tachesArchivees.setOnAction(event -> {
            this.boutonCourant = this.tachesArchivees;
            mettreAJourListeArchivage();
        });
        this.sectionsArchivees.setOnAction(event -> {
            this.boutonCourant = this.sectionsArchivees;
            mettreAJourListeArchivage();
        });
    }

    public void changerClasseCSSBoutons(){
        if(this.boutonCourant.equals(this.tachesArchivees)){
            this.tachesArchivees.getStyleClass().clear();
            this.tachesArchivees.getStyleClass().add("BtnTacheNotSelected");
            this.sectionsArchivees.getStyleClass().clear();
            this.sectionsArchivees.getStyleClass().add("BtnSectionSelected");
        }else{
            this.tachesArchivees.getStyleClass().clear();
            this.tachesArchivees.getStyleClass().add("BtnTacheSelected");
            this.sectionsArchivees.getStyleClass().clear();
            this.sectionsArchivees.getStyleClass().add("BtnSectionNotSelected");
        }
    }


    private void mettreAJourListeArchivage(){
        vueListe.getChildren().clear();
        if(this.boutonCourant != null){
            if (this.boutonCourant.equals(this.tachesArchivees)) {
                // On parcourt les tâches archivées du modèle pour les afficher sous forme d'élément graphique
                ListIterator<Tache> iterateur = this.modeleBureau.getTachesArchivees().listIterator(this.modeleBureau.getTachesArchivees().size());
                while(iterateur.hasPrevious()) {
                    Tache tache = iterateur.previous();
                    VBox tacheBox = creerTache(tache.getTitre(), tache.getDescription());
                    tacheBox.getStyleClass().add("nomEtDescriptionTacheArchivee");

                    // Bouton pour supprimer la tâche
                    Button supprimerBouton = new Button("Supprimer");
                    supprimerBouton.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlSupprimerTache(this.modeleBureau, tache, null));
                    supprimerBouton.getStyleClass().add("btnElementArchive");

                    Button restaurerBouton = new Button("Restaurer");
                    restaurerBouton.setPadding(new Insets(4, 10, 4, 10));
                    restaurerBouton.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlRestaurerTache(this.modeleBureau, tache));
                    restaurerBouton.getStyleClass().add("btnElementArchive");

                    VBox boutons = new VBox();
                    boutons.getChildren().addAll(supprimerBouton, restaurerBouton);
                    boutons.setSpacing(2);
                    boutons.getStyleClass().add("conteneurBoutons");

                    HBox element = new HBox();
                    element.getChildren().addAll(tacheBox, boutons);
                    element.setPadding(new Insets(5));
                    element.setSpacing(10);
                    element.getStyleClass().add("tacheEtBoutons");

                    vueListe.getChildren().add(element);
                }
            } else if (this.boutonCourant.equals(this.sectionsArchivees)) {
                ListIterator<Section> iterateur = this.modeleBureau.getSectionsArchivees().listIterator(this.modeleBureau.getSectionsArchivees().size());
                while(iterateur.hasPrevious()) {
                    Section section = iterateur.previous();
                    VBox tacheBox = creerTache(section.getNom(), "");
                    tacheBox.getStyleClass().add("titreTacheArchivee");

                    // Bouton pour supprimer la tâche
                    Button supprimerBouton = new Button("Supprimer");
                    supprimerBouton.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlSupprimerSection(this.modeleBureau, section, null));
                    supprimerBouton.getStyleClass().add("btnElementArchive");

                    Button restaurerBouton = new Button("Restaurer");
                    restaurerBouton.setPadding(new Insets(4, 10, 4, 10));
                    restaurerBouton.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlRestaurerSection(this.modeleBureau, section));
                    restaurerBouton.getStyleClass().add("btnElementArchive");

                    VBox boutons = new VBox();
                    boutons.getChildren().addAll(supprimerBouton, restaurerBouton);
                    boutons.setSpacing(2);
                    boutons.getStyleClass().add("conteneurBoutons");

                    HBox element = new HBox();
                    element.getChildren().addAll(tacheBox, boutons);
                    element.setPadding(new Insets(5));
                    element.setSpacing(10);
                    element.getStyleClass().add("tacheEtBoutons");

                    vueListe.getChildren().add(element);
                }

            }
        }
        if (this.vueListe.getChildren().isEmpty()) {
            this.vueListe.getStyleClass().add("fondVide");
        } else {
            this.vueListe.getStyleClass().remove("fondVide");
        }
    }

}


