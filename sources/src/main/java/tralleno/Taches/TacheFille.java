package tralleno.Taches;

import tralleno.Taches.Tache;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class TacheFille extends Tache implements Serializable {
    public TacheFille(String t, String d) {
        super(t, d);
    }

    public TacheFille(String titre, String description, LocalDate dD, LocalDate dF) {
        super(titre, description, dD, dF);
    }
}
