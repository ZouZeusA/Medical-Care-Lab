package app.labreservation;

import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.ResultSet;
import static javafx.scene.paint.Color.WHITE;

public class LoginPage {
    public void show (Stage primaryStage){
        AnchorPane LoginApane = new AnchorPane();
        LoginApane.setPrefHeight(340);
        LoginApane.setPrefWidth(372.0);

        Label Welcomelabel = new Label("Medical Care Laboratory");
        Welcomelabel.setPrefHeight(62);
        Welcomelabel.setPrefWidth(372.0);
        Welcomelabel.setStyle("-fx-background-color: #263F73;");
        Welcomelabel.setTextFill(WHITE);
        Welcomelabel.setTextAlignment(TextAlignment.CENTER);
        Welcomelabel.setAlignment(Pos.CENTER);
        Welcomelabel.setFont(Font.font("Verdana", FontWeight.BOLD, 24.0));

        CheckBox adminCheckbox = new CheckBox("Admin");
        adminCheckbox.setLayoutX(149.0);
        adminCheckbox.setLayoutY(237.0);
        adminCheckbox.setTextAlignment(TextAlignment.CENTER);
        adminCheckbox.setMnemonicParsing(false);
        adminCheckbox.setFont(Font.font("System",FontWeight.BOLD,14.0));

        PasswordField loginPassfield = new PasswordField();
        loginPassfield.setLayoutX(135.0);
        loginPassfield.setLayoutY(150.0);
        loginPassfield.setPrefHeight(26.0);
        loginPassfield.setPrefWidth(192.0);

        Label loginemailLabel = new Label("Email:");
        loginemailLabel.setFont(Font.font(18.0));
        loginemailLabel.setLayoutY(101.0);
        loginemailLabel.setLayoutX(75.0);

        TextField loginemailText = new TextField();
        loginemailText.setLayoutY(102.0);
        loginemailText.setLayoutX(134.0);
        loginemailText.setPrefHeight(26.0);
        loginemailText.setPrefWidth(192.0);

        Label loginpassLabel = new Label("Password:");
        loginpassLabel.setFont(Font.font(18.0));
        loginpassLabel.setLayoutY(149.0);
        loginpassLabel.setLayoutX(43.0);

        Label registrationLabel = new Label("Don't have an account? Sign up");
        registrationLabel.setFont(Font.font(14.0));
        registrationLabel.setLayoutY(194.0);
        registrationLabel.setLayoutX(90.0);
        registrationLabel.setPrefWidth(200.0);
        registrationLabel.setPrefHeight(34.0);
        registrationLabel.setUnderline(true);
        registrationLabel.setTextFill(Paint.valueOf("#060808"));
        registrationLabel.setOnMouseEntered( //Anonymous inner class
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent e) {
                        registrationLabel.setTextFill(Paint.valueOf("#263F73"));
                    }
                }
        );
        registrationLabel.setOnMouseExited( //Anonymous inner class
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent e) {
                        registrationLabel.setTextFill(Paint.valueOf("#060808"));
                    }
                }
        );
        registrationLabel.setOnMouseReleased( //lambda function
                e->{
                    RegistrationPage registrationpage = new RegistrationPage();
                    registrationpage.show(primaryStage);
                }
        );

        Button loginButton =new Button("Login");
        loginButton.setAlignment(Pos.CENTER);
        loginButton.setTextAlignment(TextAlignment.CENTER);
        loginButton.setContentDisplay(ContentDisplay.CENTER);
        loginButton.setLayoutX(112.0);
        loginButton.setLayoutY(274.0);
        loginButton.setMnemonicParsing(false);
        loginButton.setStyle("-fx-background-color: #263F73;");
        loginButton.setTextFill(WHITE);
        loginButton.setPrefHeight(34.0);
        loginButton.setPrefWidth(146.0);
        loginButton.setFont(Font.font("System",FontWeight.BOLD,18.0));
        loginButton.setOnAction( //lambda function
            e->{
                if(adminCheckbox.isSelected()){
                    AdminPage adminpage = new AdminPage();
                    adminpage.show(primaryStage);
                }
                else if(loginPassfield.getText()=="" || loginemailText.getText()==""){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Login");
                    alert.setHeaderText("Missing Credentials!");
                    alert.show();
                }
                else {
                    try {
                        Connection con = DBLab.getConnection();
                        ResultSet rs = con.createStatement().executeQuery("SELECT `patient`.`idPatient`,\n" +
                                "    `patient`.`firstName`,\n" +
                                "    `patient`.`lastName`,\n" +
                                "    `patient`.`password`,\n" +
                                "    `patient`.`address`,\n" +
                                "    `patient`.`telephone`\n" +
                                "FROM `labreservation`.`patient` WHERE `address` = '"+loginemailText.getText()+"' AND `password` = SHA1('"+loginPassfield.getText()+"')");
                        if (rs.next()){
                            Patient patient = new Patient(rs.getInt("idPatient"), rs.getString("firstName"),rs.getString("lastName"),
                                    loginPassfield.getText(), rs.getString("address"), rs.getString("telephone"));
                            HomePage homePage = new HomePage();
                            homePage.show(patient);
                            primaryStage.close();
                        }
                        else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Login");
                            alert.setHeaderText("Invalid credentials!");
                            alert.show();
                        }
                    }
                    catch (RuntimeException re){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Login");
                        alert.setHeaderText(re.toString());
                        alert.setContentText(re.getLocalizedMessage());
                        alert.show();
                    }
                    catch(Exception ex){
                        System.out.println(ex.toString());
                        ex.printStackTrace();
                    }
                    finally {
                        loginemailText.setText("");
                        loginPassfield.setText("");
                    }
                }
            }
        );

        LoginApane.getChildren().add(adminCheckbox);
        LoginApane.getChildren().add(loginButton);
        LoginApane.getChildren().add(loginPassfield);
        LoginApane.getChildren().add(loginemailLabel);
        LoginApane.getChildren().add(loginemailText);
        LoginApane.getChildren().add(loginpassLabel);
        LoginApane.getChildren().add(registrationLabel);
        LoginApane.getChildren().add(Welcomelabel);

        Scene loginScene = new Scene(LoginApane,372,340);
        primaryStage.setTitle("Login");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }
}
