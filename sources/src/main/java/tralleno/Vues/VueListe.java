package tralleno.Vues;

import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import tralleno.Modele.ModeleBureau;
import tralleno.Modele.Sujet;

import java.io.Serializable;

public class VueListe extends VBox implements Observateur, Serializable {
//    private final transient ComboBox<String> choixListe;
//    private final transient ListView<String> vueListe;



    private ModeleBureau modeleBureau;

    public VueListe(ModeleBureau modeleBureau) {
        this.modeleBureau = modeleBureau;
    }
    @Override
    public void actualiser(Sujet s) {

    }
}
