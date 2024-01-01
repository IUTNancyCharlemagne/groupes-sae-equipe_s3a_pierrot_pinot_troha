package tralleno.Modele;

import tralleno.Vues.Observateur;

/**
 * Interface dont un modèle peut hériter
 */
public interface Sujet {

    /**
     * Enregistre un observateur à la liste d'observateurs que le modèle doit contenir
     * @param observateur
     */
    public void enregistrerObservateur(Observateur observateur);

    /**
     * Supprime un observateur de la liste d'observateurs que le modèle doit contenir
     * @param observateur
     */
    public void supprimerObservateur(Observateur observateur);

    /**
     * Notifie tous les observateurs présents dans la liste d'obsservateurs du modèle
     */
    public void notifierObservateurs();
}
