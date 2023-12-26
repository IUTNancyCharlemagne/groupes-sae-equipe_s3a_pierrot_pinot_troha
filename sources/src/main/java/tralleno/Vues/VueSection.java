package tralleno.Vues;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import tralleno.Controleurs.Sections.ControlModifSection;
import tralleno.Controleurs.Taches.ControlModifTache;
import tralleno.Modele.ModeleBureau;
import tralleno.Modele.Sujet;
import tralleno.Section.Section;
import tralleno.Taches.Tache;
import tralleno.Vues.VueTache;

import java.util.List;

public class VueSection extends VBox implements Observateur {

    private Section section;

    public VueSection(Section section, List<Tache> taches, ModeleBureau modele) {
        super();
        this.section = section;

        setMinHeight(50);
        setPrefWidth(200);
        setSpacing(10);
        setPadding(new Insets(10));
        setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #cec0c0; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-radius: 5px;");


        String nom = section.getNom();

        String nomAbrege = nom.length() > 30 ? nom.substring(0, 30) + "..." : nom;
        Label labelSection = new Label(nom);
        labelSection.setStyle("-fx-font-weight: bold;");


        // On crée un élément graphique juste pour que le nom de la section soit cliquable
        VBox sect = new VBox();
        sect.setPadding(new Insets(5));

        sect.getChildren().add(labelSection);

        sect.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlModifSection(modele, section));

        getChildren().add(sect);

        for (Tache t : taches) {
            VueTache vueTache = new VueTache(t, modele);
            vueTache.addEventHandler(MouseEvent.MOUSE_CLICKED, new ControlModifTache(modele, t)); // On ajoute un contrôleur à chaque tache créée.
            getChildren().add(vueTache);
        }




        // Gestion du "drag over" pour accepter les tâches
        this.setOnDragOver(event -> {
            if (event.getGestureSource() instanceof VueTache) {
                event.acceptTransferModes(TransferMode.MOVE);
                event.consume();
            }
        });

        // Gestion du "drop" pour récupérer la tâche
        this.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                Tache tacheADeplacer = modele.getTacheParId(Integer.valueOf(db.getString()));

                modele.changerSection(section);

                success = true;
            }

            event.setDropCompleted(success);
            event.consume();
        });


    }

    @Override
    public void actualiser(Sujet s) {

    }
}