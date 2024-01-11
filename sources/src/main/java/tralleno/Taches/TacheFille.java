package tralleno.Taches;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Tâche qui ne comporte pas de sous-tâches
 */
public class TacheFille extends Tache implements Serializable {

    /**
     * Construit une tâche fille à partir d'un titre et d'une description
     *
     * @param t
     * @param d
     */
    public TacheFille(String t, String d) {
        super(t, d);
    }

    /**
     * Construit une tâche fille à partir d'un titre, d'une description, et de dates de début et de fin
     *
     * @param titre
     * @param description
     * @param dD
     * @param dF
     */
    public TacheFille(String titre, String description, LocalDate dD, LocalDate dF) {
        super(titre, description, dD, dF);
    }
}
