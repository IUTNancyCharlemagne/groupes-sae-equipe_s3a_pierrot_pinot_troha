package tralleno.Modele;

import tralleno.Vues.Observateur;

public interface Sujet {

    public void enregistrerObservateur(Observateur observateur);

    public void supprimerObservateur(Observateur observateur);

    public void notifierObservateurs();
}
