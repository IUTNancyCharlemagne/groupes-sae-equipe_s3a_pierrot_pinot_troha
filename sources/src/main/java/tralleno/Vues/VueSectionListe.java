package tralleno.Vues;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import tralleno.Controleurs.Sections.ControlModifSection;
import tralleno.Modele.ModeleBureau;
import tralleno.Modele.Sujet;
import tralleno.Section.Section;
import tralleno.Taches.Tache;
import tralleno.Taches.TacheMere;

import java.io.Serializable;

public class VueSectionListe extends TitledPane implements Observateur, Serializable {
    private final Section section;

    public VueSectionListe(ModeleBureau modele, Section section) {
        super();
        this.section = section;

        setExpanded(true); // Déplier le TitledPane par défaut si nécessaire

        VBox sectionContent = new VBox();
        sectionContent.setSpacing(15);
        sectionContent.setPadding(new Insets(10));
        setContent(sectionContent);


        actualiser(modele);
    }

    @Override
    public void actualiser(Sujet s) {
        ModeleBureau modeleBureau = (ModeleBureau) s;

        String nom = section.getNom();

        String nomAbrege = nom.length() > 30 ? nom.substring(0, 30) + "..." : nom;
        Label labelSection = new Label(nomAbrege);
        labelSection.setAlignment(Pos.CENTER);
        labelSection.getStyleClass().add("nomSection");

        VBox sect = new VBox();
        sect.setStyle("-fx-background-color: transparent;");
        labelSection.setStyle("-fx-background-color: transparent;");
        sect.setPadding(new Insets(5));
        sect.getStyleClass().add("fond_titre_section");
        sect.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlModifSection(modeleBureau, section));

        this.setGraphic(sect);


        sect.getChildren().add(labelSection);

        for (Tache t : this.section.getTaches()) {
            VueTacheListe vueTacheListe = new VueTacheListe(t, modeleBureau, null);
            ((VBox) getContent()).getChildren().add(vueTacheListe);
        }

        // gestion du drag over, quand on passe la tâche au dessus de la vueSection.
        // Avec ça on peut empecher le dépot d'une tâche à certains endroits
        this.setOnDragOver(event -> {
            if (event.getGestureSource() instanceof VueTacheListe) {
                if (!this.getStyleClass().contains("hovered")) {
                    this.getStyleClass().add("hovered");
                }
                // Lorsqu'on essaye de déplacer une tâche qui est déjà à la fin, à la fin... (pas logique)
                // Sinon on accepte de recevoir des tâches
                System.out.println(this.determinerPositionMettreTache(event));
                event.acceptTransferModes(TransferMode.MOVE);
                event.consume();
            }
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

//    /**
//     * Permet de déteminer la position dans la section à laquelle la tâche devra être insérée
//     * @param event
//     * @return
//     */
//    private int determinerPositionMettreTache(DragEvent event) {
//        // au cas où on trouve rien on le met à -1
//        int index = -1;
//
//        // On récupère la position de la souris
//        double mouseY = event.getY();
//
//        // On récupère la liste de chauque VueTache dans la VueSection
//        ObservableList<Node> tachesVisuelles = this.getChildren();
//
//        // Et on les parcourt pour trouver l'endroit d'insertion
//        for (int i = 0; i < tachesVisuelles.size(); i++) {
//            Node tacheVisuelle = tachesVisuelles.get(i);
//
//            double tacheY = tacheVisuelle.getBoundsInParent().getMinY();
//
//            if (mouseY < tacheY) {
//                index = i;
//                break;
//            }
//        }
//
//        if (index == -1) {
//            index = tachesVisuelles.size();
//        }
//
//        return index - 1;
//    }

//    private int determinerPositionMettreTache(DragEvent event) {
//        int index = -1;
//        double mouseY = event.getY();
//
//        ObservableList<Node> tachesVisuelles = ((VBox) getContent()).getChildren();
//
//        for (int i = 0; i < tachesVisuelles.size(); i++) {
//            Node tacheVisuelle = tachesVisuelles.get(i);
//            double tacheY = tacheVisuelle.localToScene(tacheVisuelle.getBoundsInLocal()).getMinY();
//
//            if (mouseY < tacheY) {
//                index = i;
//                break;
//            }
//        }
//
//        if (index == -1) {
//            index = tachesVisuelles.size();
//        }
//
//        return index + 1;
//    }

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

