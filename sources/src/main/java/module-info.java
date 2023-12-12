module com.example.sources {
    requires javafx.controls;
    requires javafx.fxml;


    opens tralleno to javafx.fxml;
    exports tralleno;
}