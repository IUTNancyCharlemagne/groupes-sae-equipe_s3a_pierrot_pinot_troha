module com.example.sources {
    requires javafx.controls;
    requires javafx.fxml;


    opens tralleno to javafx.fxml;
    exports tralleno;
    exports tralleno.Vues;
    opens tralleno.Vues to javafx.fxml;
    exports tralleno.Taches;
    opens tralleno.Taches to javafx.fxml;
    exports tralleno.Modele;
    opens tralleno.Modele to javafx.fxml;
    exports tralleno.Section;
    opens tralleno.Section to javafx.fxml;

    opens exemples to javafx.fxml;
    exports exemples;
}