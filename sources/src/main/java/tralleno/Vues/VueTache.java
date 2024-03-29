package tralleno.Vues;

import javafx.geometry.Insets;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import tralleno.Controleurs.Taches.ControlModifTache;
import tralleno.Modele.ModeleBureau;
import tralleno.Modele.Sujet;
import tralleno.Taches.Tache;
import tralleno.Taches.TacheMere;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Classe qui représente un élément graphique qui lui représente une tâche. Chaque tâche est représentée par un TitledPane
 */
public class VueTache extends TitledPane implements Observateur {

    /**
     * Tâche représentée
     */
    private final Tache tache;
    /**
     * Vbox qui contient les sous-tâches de la tâche
     */
    private final VBox sousTachesBox;

    /**
     * Tache qui vaut null ou la tâche mere de la tâche actuelle si elle est une sous-tâche
     */
    private final TacheMere tacheParente;

    /**
     * Permet de définir si c'est une tâche de VueListe ou VueTableau
     */
    private final boolean etendre;

    /**
     * Construit une vueTache à partir de la tâche que la vue est censée représenter graphiquement
     * du modele et de la tâche parente de la tâche. Si elle n'en a pas, elle vaudra null
     * Si on cherche à construire une tâche pour la VueTableau etendre vaudra faux car la tâche devra être d'une petite taille
     * Si etendre vaut vrai, cela construit une vueTache adaptée à la vueListe et à la largeur d'une VueSection
     * @param tache
     * @param modeleBureau
     * @param tacheParente
     * @param etendre
     */
    public VueTache(Tache tache, ModeleBureau modeleBureau, TacheMere tacheParente, boolean etendre) {
        super();
        this.etendre = etendre;
        if (!etendre) // Si etendre vaut vrai c'est qu'on crée une tâche depuis la VueListe
            setPrefWidth(150);
        this.setExpanded(true);
        this.tache = tache;
        this.sousTachesBox = new VBox();
        this.sousTachesBox.getStyleClass().add("sousTachesBox");
        this.tacheParente = tacheParente;
        this.getStyleClass().add("vueTache");
        actualiser(modeleBureau);
    }

    /**
     * Déclenchée par le constructeur, mais pour l'instant pas directement relié au modèle
     *
     * @param s
     */
    @Override
    public void actualiser(Sujet s) {

        ModeleBureau modeleBureau = (ModeleBureau) s;

        Label titreLabel = new Label(tache.getTitre());

        titreLabel.setFont(Font.font(14));
        titreLabel.getStyleClass().add("titreTache");

        Button modifierButton = new Button();
        String img = System.getProperty("user.dir") + "/src/main/resources/Images/pen.png";
        File file = new File(img);
        ImageView logo;
        try {
            FileInputStream f = new FileInputStream(file);
            logo = new ImageView(new Image(f));
        } catch (FileNotFoundException e) {
            logo = new ImageView();
        }

        modifierButton.setGraphic(logo);
        logo.setFitWidth(20);
        logo.setFitHeight(20);
        modifierButton.getStyleClass().add("modifier-button");
        modifierButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlModifTache(modeleBureau, this.tache, this.tacheParente));

        HBox headerBox = new HBox(titreLabel, modifierButton);
        headerBox.setSpacing(5);
        headerBox.setPadding(new Insets(5));
        headerBox.getStyleClass().add("enteteTache");

        VBox contenuTache = new VBox();
        contenuTache.setSpacing(5);
        contenuTache.setPadding(new Insets(5));
        contenuTache.getStyleClass().add("contenuTache");

        Label descriptionLabel = new Label(tache.getDescription());

        descriptionLabel.getStyleClass().add("descriptionLabel");

        // Si on est dans le cas d'une tâche de VueTableau
        if (!this.etendre) {
            titreLabel.setMaxWidth(150);
            titreLabel.setWrapText(false);
            descriptionLabel.setMaxWidth(150);
            descriptionLabel.setWrapText(false);
        }

        contenuTache.getChildren().addAll(descriptionLabel, sousTachesBox);
        this.setContent(contenuTache);

        this.setGraphic(headerBox);


        // On ajoute les sous-tâches aux enfants du TitledPane en créant d'autres VueTache seulement si la tâche a des sous-tâches
        if (this.tache instanceof TacheMere) {
            List<Tache> sousTaches = ((TacheMere) this.tache).getSousTaches();
            for (Tache sousTache : sousTaches) {
                VueTache vueSousTache = new VueTache(sousTache, modeleBureau, (TacheMere) this.tache, this.etendre);
                sousTachesBox.getChildren().add(vueSousTache);
                VBox.setMargin(vueSousTache, new Insets(0, 0, 5, 0));
            }
        }


        // Écouteur pour le drag and drop des tâches/sous-tâches
        this.setOnDragDetected(event -> {
            Dragboard dragboard = this.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putImage(this.snapshot(new SnapshotParameters(), null));
            this.getStyleClass().add("dragging");
            // Si la tâche est une sous-tâche, si on la drag and drop dans une autre section, il faudra
            // qu'elle soit supprimée des sous-tâches de sa tâche mère.
            // Donc si la tacheParente vaut null ça veut dire que la tâche actuelle n'a pas de tâcheMere
            // Si elle ne vaut pas null, alors on met l'id de sa tâche mere pour que lors du drop dans une section cela supprime bien
            if (this.tacheParente != null) {
                content.putString(String.valueOf(this.tacheParente.getId()));
            } else {
                content.putString(String.valueOf(-1));
            }
            modeleBureau.setTacheCourante(this.tache); // On met la tâche courante du modèle sur la tâche qu'on est en train de drag
            dragboard.setContent(content);
            event.consume();
        });

        // Enlever la couleur qui fait les contours de la tâche une fois le drag fini
        this.setOnDragDone(event -> {
            this.getStyleClass().remove("dragging");
        });

        // Écouteur pour accepter le drop de la tâche/sous-tâche pour en faire une sous-tâche
        this.setOnDragOver(event -> {
            if (event.getGestureSource() instanceof VueTache) {
                if (modeleBureau.estSousTacheDe(this.tache, modeleBureau.getTacheCourante())) {
                    // Empêche le drop en montrant visuellement qu'il n'est pas autorisé
                    event.acceptTransferModes(TransferMode.NONE);
                    event.consume();
                } else {
                    // Autorise le drop
                    if (!this.getStyleClass().contains("drag-over")) {
                        this.getStyleClass().add("drag-over");
                    }
                    event.acceptTransferModes(TransferMode.MOVE);
                    event.consume();
                }
            }
        });

        // Quand le drop est stoppé on enlève la classe qui fait les contours
        this.setOnDragExited(event -> {
            if (!this.contains(event.getX(), event.getY())) {
                this.getStyleClass().remove("drag-over");
            }
            event.consume();
        });

        // Idem
        this.setOnMouseExited(event -> {
            this.getStyleClass().remove("drag-over");
            event.consume();
        });

        // Écouteur pour transformer la tâche/sous-tâche lors du drop
        this.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (!(modeleBureau.estSousTacheDe(this.tache, modeleBureau.getTacheCourante()))) {

                if (db.hasString()) {
                    // On vérifie d'abord que tâchecible != tâchedragged
                    if (modeleBureau.getTacheCourante().getId() != this.tache.getId()) {
                        // Quand on crée une tâche, elle est tacheMere de base, sinon, cela va créer des problèmes de référence d'objets et
                        // des opérations inutiles. Ainsi, chaque VueTache est prête à en recevoir une autre.
                        // La tache qui est drag est la tacheCourante du modèle grâce au setDragDetected dans VueTache


                        // Si la tâche possèdait une tacheMere, la supprimer de sa tacheMere
                        int idTacheParente = Integer.parseInt(db.getString());
                        if (idTacheParente != -1) {
                            TacheMere tacheMere = (TacheMere) modeleBureau.getTacheParId(idTacheParente);
                            tacheMere.supprimerSousTache(modeleBureau.getTacheCourante());
                        }

                        modeleBureau.getTacheCourante().getSectionParente().supprimerTache(modeleBureau.getTacheCourante());
                        // Il nous reste à ajouter la tâche qui est dragged dans la tâche sur laquelle elle a été dragged.
                        ((TacheMere) this.tache).ajouterSousTache(modeleBureau.getTacheCourante());
                        modeleBureau.getTacheCourante().setSectionParente(this.tache.getSectionParente());
                        // On supprime également la date de la tâche quand elle est drop dans une autre car elle n'est qu'une sous-tâche
                        modeleBureau.getTacheCourante().setDateDebut(null);
                        modeleBureau.getTacheCourante().setDateFin(null);
                        modeleBureau.getTacheCourante().setDuree(1);
                        modeleBureau.supprimerDependances();// Car une sous-tâche ne peut pas avoir de dépendances
                        modeleBureau.notifierObservateurs();
                        success = true;
                    }
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }
}









