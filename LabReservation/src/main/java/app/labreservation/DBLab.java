package app.labreservation;

import javafx.scene.control.Alert;
import java.sql.*;

public class DBLab {

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/labreservation","root","F195io");
        }
        catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(e.toString());
            alert.setHeaderText(e.getLocalizedMessage());
            alert.setContentText("Unable to connect to Database!");
            alert.show();
            e.printStackTrace();
            return null;
        }
    }
}
