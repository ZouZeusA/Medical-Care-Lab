module app.labreservation {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens app.labreservation to javafx.fxml;
    exports app.labreservation;
}