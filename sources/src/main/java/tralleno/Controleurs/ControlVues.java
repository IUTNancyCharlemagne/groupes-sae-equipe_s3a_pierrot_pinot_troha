package tralleno.Controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import tralleno.Vues.VuePrincipale;
import tralleno.Vues.VueTableau;

public class ControlVues implements EventHandler<ActionEvent> {

    /**
     * Correspond à la liste dépliante permettant à l'utilisateur de choisir la vue qu'il veut
     */
    private ChoiceBox<String> choixVue;

    /**
     * On va devoir changer le centre du borderpane de la vue principale selon le mode sélectionné
     */
    private VuePrincipale vuePrincipale;

    public ControlVues(ChoiceBox<String> choixVue, VuePrincipale vuePrincipale){
        this.choixVue = choixVue;
        this.vuePrincipale = vuePrincipale;
    }


    @Override
    public void handle(ActionEvent actionEvent) {
        if (actionEvent.getSource() == choixVue) {
            String vueChoisie = choixVue.getValue();
            if (vueChoisie.equals("Vue Tableau")) {
                this.vuePrincipale.changerVue(VuePrincipale.TABLEAU);
            } else if (vueChoisie.equals("Vue Liste")) {
                this.vuePrincipale.changerVue(VuePrincipale.LISTE);
            }
        }
    }
}
