package tralleno.Vues;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tralleno.Controleurs.Sections.ControlModifSection;
import tralleno.Controleurs.Taches.ControlModifTache;
import tralleno.Modele.ModeleBureau;
import tralleno.Modele.Sujet;
import tralleno.Section.Section;
import tralleno.Taches.Tache;
import tralleno.Taches.TacheMere;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;

/**
 * Représente les sections dans la VueListe sous forme de TitledPane, pour que lorsqu'on la déplie, on ait
 * accès aux tâches de la section
 */
public class VueSectionListe extends TitledPane implements Observateur, Serializable {
    /**
     * Section représentée par la vue
     */
    private final Section section;

    /**
     * Construit une vueSection à partir de la section que la vue doit représenter et le modèle
     * @param modele
     * @param section
     */
    public VueSectionListe(ModeleBureau modele, Section section) {
        super();
        this.section = section;
        setExpanded(true);

        this.getStyleClass().add("vueSectionListe");


        actualiser(modele);

    }

    /**
     * Lorsqu'elle est appelée, crée la VueSection, en ajoutant pour chaque tâche de la seciton
     * une VueTache lui étant associée
     * @param s
     */
    @Override
    public void actualiser(Sujet s) {
        ModeleBureau modeleBureau = (ModeleBureau) s;


        VBox content = new VBox();
        content.setSpacing(15);
        content.setPadding(Insets.EMPTY);

        VBox sectionContent;

        sectionContent = new VBox();
        sectionContent.setSpacing(15);
        sectionContent.setPadding(new Insets(15));
        sectionContent.setMinHeight(50);
        sectionContent.getStyleClass().add("sectionContent");

        // On met la Vbox dans l'autre qui est dans le contenu
        content.getChildren().add(sectionContent);

        setContent(content);

        String nom = section.getNom();

        String nomAbrege = nom.length() > 30 ? nom.substring(0, 30) + "..." : nom;
        Label labelSection = new Label(nomAbrege);
        labelSection.setAlignment(Pos.CENTER);
        labelSection.getStyleClass().add("nomSection");

        HBox sect = new HBox();
        sect.setPadding(new Insets(5));
        sect.setSpacing(20);
        sect.getStyleClass().add("fond_titre_section");

        Button boutonModifierSect = new Button();
        String img = System.getProperty("user.dir") + "/src/main/resources/Images/pen.png";
        File file = new File(img);
        ImageView logo;
        try {
            FileInputStream f = new FileInputStream(file);
            logo = new ImageView(new Image(f));
        } catch (FileNotFoundException e) {
            logo = new ImageView();
        }

        boutonModifierSect.setGraphic(logo);
        logo.setFitWidth(20);
        logo.setFitHeight(20);
        boutonModifierSect.getStyleClass().add("modifier-button");
        boutonModifierSect.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlModifSection(modeleBureau, this.section));

        // Dans l'en-tête du TitledPane qui représente la section on met le nom de la section avec le bouton de modification
        this.setGraphic(sect);


        sect.getChildren().addAll(labelSection, boutonModifierSect);

        sectionContent.getChildren().clear();

        // Pour chaque tâche de la section on crée une VueTache et on l'ajoute
        for (Tache t : this.section.getTaches()) {
            VueTache vueTacheListe = new VueTache(t, modeleBureau, null, true);
            sectionContent.getChildren().add(vueTacheListe);
        }


        // Drag and drop pour déplacer les sections entre elles et modifier leur place sur le tableau
        this.setOnDragDetected(event -> {
            Dragboard dragboard = this.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent contenu = new ClipboardContent();
            contenu.putString((String.valueOf(section.getId()))); // On met l'id de la section
            // pour le drag and drop de la section dans la VueListe
            contenu.putImage(sect.snapshot(new SnapshotParameters(), null));
            dragboard.setContent(contenu);
            event.consume();
        });


        // gestion du drag over, quand on passe la tâche au dessus de la vueSection.
        // Avec ça on peut empecher le dépot d'une tâche à certains endroits
        sectionContent.setOnDragOver(event -> {
            if (event.getGestureSource() instanceof VueTache) {
                // Lorsqu'on essaye de déplacer une tâche qui est déjà à la fin, à la fin... (pas logique)
                // Sinon on accepte de recevoir des tâches
                event.acceptTransferModes(TransferMode.MOVE);
                event.consume();
            }
        });

        // Pour empêcher le drag de la VueListe ou d'une VueSection dans l'en-tête d'une section (éviter les problèmes)
        // Mais à cause de javafx le setgraphic() ne représente que la partie qui contient le nom de la seciton et le bouton
        // de modification et pas tout l'espace que prend l'en-tête
        this.getGraphic().setOnDragOver(event -> {
            if (event.getGestureSource() instanceof VueListe || event.getGestureSource() instanceof VueSectionListe) {
                event.acceptTransferModes(TransferMode.NONE);
                event.consume();
            }
        });

        // On accepte uniquement le drop de VueTache dans la partie contenu de la section
        this.getContent().setOnDragOver(event -> {
            if (event.getGestureSource() instanceof VueSectionListe) {
                event.acceptTransferModes(TransferMode.NONE);
                event.consume();
            }
        });


        // Gestion du drop pour récupérer la tâche
        sectionContent.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                int idTacheParente = Integer.parseInt(db.getString());
                if (idTacheParente != -1) { // Si la tâche a une tâche mère la valeur de l'id sera différente de -1
                    TacheMere tacheParente = (TacheMere) modeleBureau.getTacheParId(idTacheParente);
                    tacheParente.supprimerSousTache(modeleBureau.getTacheCourante());
                }
                // On récupère l'index de l'endroit dans la section où la tâche est déposée
                int cibleIndex = determinerPositionMettreTache(event, sectionContent);
                if (cibleIndex != -1) {
                    // Si c'est différent de -1 ça veut dire qu'il y a une position spécifique, donc on ajoute à la bonne position avec une méthode du modèle (pour le notifier observateurs)
                    modeleBureau.changerSection(section, cibleIndex);
                } else {
                    // Sinon, on ajoute à la fin de la section, donc à l'index = à la taille (en dernière place)
                    modeleBureau.changerSection(section, this.section.getTaches().size());
                }
                success = true;
            }

            event.setDropCompleted(success);
            event.consume();
        });

    }

    /**
     * Permet de déteminer la position dans la section à laquelle la tâche devra être insérée
     *
     * @param event
     * @return
     */
    private int determinerPositionMettreTache(DragEvent event, VBox sectionContent) {
        // au cas où on trouve rien on le met à -1
        int index = -1;

        // On récupère la position de la souris
        double mouseY = event.getY();

        // On récupère la liste de chauque VueTache dans la VueSection
        ObservableList<Node> tachesVisuelles = sectionContent.getChildren();

        // Et on les parcourt pour trouver l'endroit d'insertion
        for (int i = 0; i < tachesVisuelles.size(); i++) {
            Node tacheVisuelle = tachesVisuelles.get(i);

            // On récupère ici la position en ordonnée de la VueTache parcourue
            double tacheY = tacheVisuelle.getBoundsInParent().getMinY();

            // Et si en ordonnée elle est supérieure à la souris alors on a trouvé l'endroit où on veut insérer
            if (mouseY < tacheY) {
                index = i;
                break;
            }
        }

        // Si mauvaise position alors on met la taille de la liste comme ça dans le setOnDragDropped ça ajoutera à la fin
        if (index == -1) {
            index = tachesVisuelles.size();
        }

        return index;
    }
}

