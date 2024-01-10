package tralleno.Vues;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import tralleno.Controleurs.Sections.ControlModifSection;
import tralleno.Modele.ModeleBureau;
import tralleno.Modele.Sujet;
import tralleno.Section.Section;
import tralleno.Taches.Tache;
import tralleno.Taches.TacheMere;

import java.io.Serializable;
import java.util.List;

/**
 * Cette classe représente une section via une VBox. Les sections sont contenues dans les différentes vues et ne sont pas
 * directement reliées au modèle. Elles y sont par l'intermédiaire des VueTableau/VueListe etc...
 */
public class VueSection extends VBox implements Observateur, Serializable {

    /**
     * Section qui est représentée par la vue
     */
    private Section section;

    /**
     * Crée une VueSection à partir de la section qu'elle doit représenter.
     * @param section
     * @param modele
     */
    public VueSection(Section section, ModeleBureau modele) {
        super();
        this.section = section;

        this.setMinHeight(50);
        this.setPrefWidth(250);

        setSpacing(18);
        setPadding(new Insets(10));
        getStyleClass().add("section");


        this.actualiser(modele);

    }

    /**
     * Déclenchée par le constructeur, mais pour l'instant pas directement relié au modèle
     * @param s
     */
    @Override
    public void actualiser(Sujet s) {

        ModeleBureau modeleBureau = (ModeleBureau) s;


        String nom = section.getNom();

        String nomAbrege = nom.length > 30 ? nom.substring(0, 30) + "..." : nom;
        Label labelSection = new Label(nomAbrege);
        labelSection.setAlignment(Pos.CENTER);
        labelSection.getStyleClass().add("nomSection");


        // On crée un élément graphique juste pour que le nom de la section soit cliquable et pas toute la VBox
        VBox sect = new VBox();
        sect.setPadding(new Insets(5));
        sect.getStyleClass().add("fond_titre_section");

        sect.getChildren().add(labelSection);

        // On ajoute un contrôleur uniquement sur la partie qui contient le nom de la section pour pouvoir la modifier
        sect.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlModifSection(modeleBureau, section));

        getChildren().add(sect);

        // Puis pour chaque tâche que la section contient, on crée une VueTache
        for (Tache t : this.section.getTaches()) {
            VueTache vueTache = new VueTache(t, modeleBureau, null, false);
            getChildren().add(vueTache);
        }


        // Drag and drop pour déplacer les sections entre elles et modifier leur place sur le tableau
        sect.setOnDragDetected(event -> {
            Dragboard dragboard = sect.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString((String.valueOf(section.getId()))); // Ajoutez ici l'identifiant de la section
            content.putImage(sect.snapshot(new SnapshotParameters(), null));
            dragboard.setContent(content);
            event.consume();
        });



        // gestion du drag over, quand on passe la tâche au dessus de la vueSection.
        // Avec ça on peut empecher le dépot d'une tâche à certains endroits
        this.setOnDragOver(event -> {
            if (event.getGestureSource() instanceof VueTache) {
                if (!this.getStyleClass().contains("hovered")) {
                    this.getStyleClass().add("hovered");
                }
                // Lorsqu'on essaye de déplacer une tâche qui est déjà à la fin, à la fin... (pas logique)
                    // Sinon on accepte de recevoir des tâches
                    event.acceptTransferModes(TransferMode.MOVE);
                    event.consume();
            }
        });

        this.setOnDragExited(event -> {
            if (!this.contains(event.getX(), event.getY())) {
                this.getStyleClass().remove("hovered");
            }
            event.consume();
        });

        // Gestion du drop pour récupérer la tâche
        this.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                int idTacheParente = Integer.parseInt(db.getString());
                if(idTacheParente != -1){ // Si la tâche a une tâche mère la valeur de l'id sera différente de -1
                    TacheMere tacheParente = (TacheMere) modeleBureau.getTacheParId(idTacheParente);
                    tacheParente.supprimerSousTache(modeleBureau.getTacheCourante());
                }
                // On récupère l'index de l'endroit dans la section où la tâche est déposée
                int cibleIndex = determinerPositionMettreTache(event);
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
     * @param event
     * @return
     */
    private int determinerPositionMettreTache(DragEvent event) {
        // au cas où on trouve rien on le met à -1
        int index = -1;

        // On récupère la position de la souris
        double mouseY = event.getY();

        // On récupère la liste de chauque VueTache dans la VueSection
        ObservableList<Node> tachesVisuelles = this.getChildren();

        // Et on les parcourt pour trouver l'endroit d'insertion
        for (int i = 0; i < tachesVisuelles.size(); i++) {
            Node tacheVisuelle = tachesVisuelles.get(i);

            double tacheY = tacheVisuelle.getBoundsInParent().getMinY();

            if (mouseY < tacheY) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            index = tachesVisuelles.size();
        }

        return index - 1;
    }


}