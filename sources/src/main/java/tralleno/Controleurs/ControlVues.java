package tralleno.Controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import tralleno.Vues.VuePrincipale;

import static tralleno.Vues.VuePrincipale.*;

/**
 * Classe qui gère le changement de vues global, donc que ce soit pour changer la vue entre la vuetableau et la vueliste, ou encore la vue des tâches archivées
 * ou encore de la VueGantt
 */
public class ControlVues implements EventHandler<ActionEvent> {

    /**
     * Correspond à la liste dépliante permettant à l'utilisateur de choisir la vue qu'il veut
     */
    private ChoiceBox<String> choixVue;

    /**
     * Correspond au bouton archivage dans la barre en haut de l'application pour accéder aux tâches et sections archivées
     */
    private static Button boutonArchivage;

    /**
     * Correspond à la liste dépliante permettant à l'utilisateur de choisir le thème de l'application
     */
    private ChoiceBox<String> theme;

    /**
     * On va devoir changer le centre du borderpane de la vue principale selon le mode sélectionné
     */
    private final VuePrincipale vuePrincipale;

    /**
     * @param choixVue
     * @param vuePrincipale
     */
    public ControlVues(ChoiceBox<String> choixVue, VuePrincipale vuePrincipale) {
        this.choixVue = choixVue;
        this.vuePrincipale = vuePrincipale;
    }

    /**
     * @param archivage
     * @param vuePrincipale
     */
    public ControlVues(Button archivage, VuePrincipale vuePrincipale) {
        boutonArchivage = archivage;
        this.vuePrincipale = vuePrincipale;
    }

    /**
     * Contrôleur pour la choicebox pour les thèmes
     * @param theme
     * @param vuePrincipale
     * @param contournement
     */
    public ControlVues(ChoiceBox<String> theme, VuePrincipale vuePrincipale, String contournement) {
        this.theme = theme;
        this.vuePrincipale = vuePrincipale;
    }

    /**
     * Méthode, qui selon l'action sur les attributs du contrôleur, gère les actions. Pour les choicebox, les boutons etc..
     * Utile la source de l'évènement pour définir quoi faire
     * @param actionEvent
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        if (actionEvent.getSource() == choixVue) {
            String vueChoisie = choixVue.getValue();
            if (vueChoisie.equals("Vue Tableau")) {
                this.vuePrincipale.changerVue(VuePrincipale.TABLEAU);
            } else if (vueChoisie.equals("Vue Liste")) {
                this.vuePrincipale.changerVue(VuePrincipale.LISTE);
            } else if (vueChoisie.equals("Vue Gantt")) {
                this.vuePrincipale.changerVue(VuePrincipale.SELECTGANTT);
            }
        } else if (actionEvent.getSource() == boutonArchivage) {
            String bouton = boutonArchivage.getText();
            if (bouton.equals("Archivage")) {
                this.vuePrincipale.afficherOuCacherArchivage();
            }
        } else if (actionEvent.getSource() == theme) {
            String themeChoisi = theme.getValue();
            switch (themeChoisi) {
                case "Brouillard":
                    this.vuePrincipale.changerTheme(THEMEBROUILLARD);
                    break;
                case "Ocean":
                    this.vuePrincipale.changerTheme(THEMEOCEAN);
                    break;
                case "Crepuscule":
                    this.vuePrincipale.changerTheme(THEMECREPUSCULE);
                    break;
                case "Foret":
                    this.vuePrincipale.changerTheme(THEMEFORET);
                    break;
                case "Nuit":
                    this.vuePrincipale.changerTheme(THEMENUIT);
                    break;
                case "Plage":
                    this.vuePrincipale.changerTheme(THEMEPLAGE);
                    break;
                default:
                    this.vuePrincipale.changerTheme(THEMEBROUILLARD);
                    break;
            }
        }
    }

    /**
     * @return le bouton qui permet d'ouvrir la fenêtre d'archivage
     */
    public static Button getBoutonArchivage() {
        return boutonArchivage;
    }
}

