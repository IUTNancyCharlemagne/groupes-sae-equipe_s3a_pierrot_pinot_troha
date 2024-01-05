package tralleno.Vues;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
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

        setMinHeight(50);
        setPrefWidth(200);
        setSpacing(10);
        setPadding(new Insets(10));
        setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #cec0c0; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-radius: 5px;");


        actualiser(modele);

    }

    /**
     * Déclenchée par le constructeur, mais pour l'instant pas directement relié au modèle
     * @param s
     */
    @Override
    public void actualiser(Sujet s) {

        ModeleBureau modeleBureau = (ModeleBureau) s;


        String nom = section.getNom();

        String nomAbrege = nom.length() > 30 ? nom.substring(0, 30) + "..." : nom;
        Label labelSection = new Label(nom);
        labelSection.setStyle("-fx-font-weight: bold;");


        // On crée un élément graphique juste pour que le nom de la section soit cliquable et pas toute la VBox
        VBox sect = new VBox();
        sect.setPadding(new Insets(5));

        sect.getChildren().add(labelSection);

        // On ajoute un contrôleur uniquement sur la partie qui contient le nom de la section pour pouvoir la modifier
        sect.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlModifSection(modeleBureau, section));

        getChildren().add(sect);

        // Puis pour chaque tâche que la section contient, on crée une VueTache
        for (Tache t : this.section.getTaches()) {
            VueTache vueTache = new VueTache(t, modeleBureau, null);
            getChildren().add(vueTache);
        }


        // gestion du drag over, quand on passe la tâche au dessus de la vueSection.
        // Avec ça on peut empecher le dépot d'une tâche à certains endroits
        this.setOnDragOver(event -> {
            if (event.getGestureSource() instanceof VueTache) {
                // Vérifie si la section sur laquelle on veut dropper est la même que la section parente de la tâche
                // SI c'est le cas on interdit le drop (car aucune utilité et complique)
                // Donc on vérifie si la tâche est directement contenue dans la section
                // Si c'est le cas, elle n'est pas sous-tâche, et n'a donc pas d'intérêt à être déplacée
                // En revanche si c'est la sous-tâche d'une tâche de la section, on peut la déplacer
                if ((modeleBureau.getTacheCourante().getSectionParente().getId() == this.section.getId()) && (this.section.getTaches().contains(modeleBureau.getTacheCourante()))) {
                    event.acceptTransferModes(TransferMode.NONE); // Rejette le drop si c'est dans la même section
                    event.consume();
                } else { // Sinon on accepte de recevoir des tâches
                    event.acceptTransferModes(TransferMode.MOVE);
                    event.consume();
                }
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
                modeleBureau.changerSection(section);

                success = true;
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }
}