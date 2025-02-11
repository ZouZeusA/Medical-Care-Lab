package app.labreservation;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegistrationPage {
    public void show (Stage primaryStage){

        TextField tfFirstName = new TextField();
        TextField tfLastName = new TextField();
        TextField tfEmailAddress = new TextField();
        TextField tfTelephone = new TextField();

        PasswordField pfPassword = new PasswordField();

        Label lbFirstName = new Label("First Name:");
        Label lbLastName = new Label("Last Name:");
        Label lbPassword = new Label("Password:");
        Label lbEmailAddress = new Label("Email Address:");
        Label lbTelephone = new Label("Telephone:");

        Button btSignUp = new Button("Submit");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.add(lbFirstName, 0, 0);
        gridPane.add(tfFirstName, 1, 0);
        gridPane.add(lbLastName, 0, 1);
        gridPane.add(tfLastName, 1, 1);
        gridPane.add(lbPassword, 0, 2);
        gridPane.add(pfPassword, 1, 2);
        gridPane.add(lbEmailAddress, 0, 3);
        gridPane.add(tfEmailAddress, 1, 3);
        gridPane.add(lbTelephone, 0, 4);
        gridPane.add(tfTelephone, 1, 4);
        gridPane.add(btSignUp, 1, 5);

        // Set properties for UI
        gridPane.setAlignment(Pos.CENTER);
        tfFirstName.setAlignment(Pos.CENTER_LEFT);
        tfLastName.setAlignment(Pos.CENTER_LEFT);
        pfPassword.setAlignment(Pos.CENTER_LEFT);
        tfEmailAddress.setAlignment(Pos.CENTER_LEFT);
        tfTelephone.setAlignment(Pos.CENTER_LEFT);
        GridPane.setHalignment(btSignUp, HPos.RIGHT);

        // Process events
        btSignUp.setOnAction(e ->
             {
                try{
                    Patient patient = new Patient(tfFirstName.getText(),tfLastName.getText(),pfPassword.getText(),tfEmailAddress.getText(),tfTelephone.getText());
                    Connection con = DBLab.getConnection();
                    String s = "INSERT INTO `labreservation`.`patient` (`firstName`, `lastName`, `password`, `address`, `telephone`) VALUES (?, ?, SHA1(?), ?, ?)";
                    PreparedStatement ps = con.prepareStatement(s);
                    ps.setString(1, patient.getFirstName());
                    ps.setString(2, patient.getLastName());
                    ps.setString(3, patient.getPassword());
                    ps.setString(4, patient.getAddress());
                    ps.setString(5, patient.getTel());
                    if(ps.executeUpdate()==1){
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Registration");
                        alert.setHeaderText("Patient Insertion Completed Successfully");
                        alert.show();
                        LoginPage loginPage = new LoginPage();
                        loginPage.show(primaryStage);
                        ps.close();
                    }
                    con.close();
                }
                catch (RuntimeException re){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Registration");
                    alert.setHeaderText(re.toString());
                    alert.setContentText(re.getLocalizedMessage());
                    alert.show();
                }
                catch(Exception ex){
                    System.out.println(ex.toString());
                    ex.printStackTrace();
                }
                finally {
                    tfFirstName.setText("");
                    tfLastName.setText("");
                    pfPassword.setText("");
                    tfEmailAddress.setText("");
                    tfTelephone.setText("");
                }
            }
        );

        // Create a scene and place it in the stage
        Scene registrationScene = new Scene(gridPane, 300, 200);
        primaryStage.setTitle("Registration"); // Set title
        primaryStage.setScene(registrationScene); // Place the scene in the stage
        primaryStage.show(); // Display the stage
    }
}
