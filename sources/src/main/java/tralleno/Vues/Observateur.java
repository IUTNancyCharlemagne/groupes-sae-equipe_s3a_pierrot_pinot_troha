package tralleno.Vues;

import tralleno.Modele.Sujet;

/**
 * Observe un sujet et s'actualise lorsqu'il change.
 */
public interface Observateur {

    /**
     * Se déclenche lorsque l'état du modèle change
     * @param s
     */
    public void actualiser(Sujet s);
}
