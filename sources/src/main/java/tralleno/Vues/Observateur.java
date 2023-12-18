package tralleno.Vues;

import tralleno.Modele.Sujet;

/**
 * Observe un sujet et s'actualise lorsqu'il change.
 */
public interface Observateur {

    public void actualiser(Sujet s);
}
