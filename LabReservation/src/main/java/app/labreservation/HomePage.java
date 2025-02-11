package app.labreservation;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class HomePage {
    public void show(Patient patient){

        Pane patientPane = new Pane();
        patientPane.setPrefWidth(720.0);
        patientPane.setPrefHeight(623);

        ImageView imageView = new ImageView();
        ImageViewThread thread1 = new ImageViewThread(imageView);
        thread1.run();

        ListView<Labotest> lvAvailableTests = new ListView<>();
        lvAvailableTests.setLayoutX(390.0);
        lvAvailableTests.setLayoutY(150);
        lvAvailableTests.setPrefHeight(200);
        lvAvailableTests.setPrefWidth(300);

        ListView<Labotest> lvReservedTests = new ListView<>();
        lvReservedTests.setLayoutX(29.0);
        lvReservedTests.setLayoutY(350);
        lvReservedTests.setPrefHeight(200);
        lvReservedTests.setPrefWidth(300);
        RListViewThread thread3 = new RListViewThread(lvReservedTests,patient);
        thread3.run();

        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setLayoutX(14.0);
        datePicker.setLayoutY(179.0);
        //datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        datePicker.setEditable(false);
        datePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0 );
            }
        });

        ComboBox<String> timecomboBox = new ComboBox<>();
        ComboBoxThread thread2 = new ComboBoxThread(timecomboBox);
        thread2.run();
        timecomboBox.setOnAction(e->{
            lvAvailableTests.getItems().clear();
            try{
                Connection con = DBLab.getConnection();
                    ResultSet rs = con.createStatement().executeQuery("SELECT `labotest`.*\n" +
                            "    FROM `labreservation`.`labotest` \n" +
                            "    WHERE `labotest`.`number` NOT IN \n" +
                            "    (SELECT `reservation`.`number` FROM `labreservation`.`reservation` \n" +
                            "    WHERE `reservation`.`date` = '"+datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+"' AND `reservation`.`time` ='"+ timecomboBox.getValue()+"');");
                    while (rs.next()) {
                        lvAvailableTests.getItems().add(new Labotest(rs.getInt("number"), rs.getString("type"),
                                rs.getDouble("price"), rs.getString("option1"),rs.getString("option2")));
                        }
                con.close();
                }
                catch(Exception ex){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Available Tests");
                    alert.setHeaderText(ex.getLocalizedMessage());
                    alert.show();
                    System.out.println(ex.toString());
                    ex.printStackTrace();
                }
            }
        );

        Button btCancellation = new Button("Cancel Reservation");
        btCancellation.setLayoutX(89.0);
        btCancellation.setLayoutY(566.0);
        btCancellation.setMnemonicParsing(false);
        btCancellation.setPrefWidth(180.0);
        btCancellation.setPrefHeight(40.0);
        btCancellation.setFont(Font.font(18.0));
        btCancellation.setOnAction(e->
            {
                try{
                    Connection con = DBLab.getConnection();
                    String s = "DELETE FROM `labreservation`.`reservation` WHERE `reservation`.`number` = ? " +
                            "AND `reservation`.`date` = ? AND `reservation`.`time` =? ";
                    PreparedStatement ps = con.prepareStatement(s);
                    if (lvReservedTests.getSelectionModel().getSelectedItem()!=null){
                        String [] a = lvReservedTests.getSelectionModel().getSelectedItem().toString().split(" ");
                        ps.setInt(1, Integer.parseInt(a[0]));
                        ps.setDate(2, java.sql.Date.valueOf(a[3]));
                        ps.setTime(3,java.sql.Time.valueOf(a[4]));
                        if(ps.executeUpdate()==1){
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Delete");
                            alert.setHeaderText("Reservation Deleted Successfully");
                            alert.show();
                            ps.close();
                            thread3.run();
                        }
                        else {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Delete");
                            alert.setHeaderText("No reservation selected");
                            alert.show();
                            ps.close();
                        }
                    }
                    con.close();
                }
                catch(Exception ex){
                    System.out.println(ex.toString());
                    ex.printStackTrace();
                }
            }
        );

        Button btConfirmation = new Button("Confirm Reservation");
        btConfirmation.setLayoutX(441.0);
        btConfirmation.setLayoutY(367.0);
        btConfirmation.setMnemonicParsing(false);
        btConfirmation.setPrefWidth(200.0);
        btConfirmation.setPrefHeight(40.0);
        btConfirmation.setFont(Font.font(18.0));
        btConfirmation.setOnAction(e->
            {
                try{
                    Labotest test = new Labotest();
                    Connection con = DBLab.getConnection();

                    patient.setT(new Labotest());
                    String [] p = lvAvailableTests.getSelectionModel().getSelectedItem().toString().split(" ");
                    String [] a = datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).split("-");
                    patient.getT().setTest_date(new Date(Integer.parseInt(a[0]),Integer.parseInt(a[1]),Integer.parseInt(a[2])));
                    String[] f = timecomboBox.getValue().split(":");
                    patient.getT().setTest_time(new Time(Integer.parseInt(f[0]),Integer.parseInt(f[1]),Integer.parseInt(f[2])));

                    String s = " INSERT INTO `labreservation`.`reservation` (`number`, `firstName`, `lastName`,\n" +
                            "`date`, `time`, `result`) VALUES (?, ?, ?, ?, ?,?);";
                    PreparedStatement ps = con.prepareStatement(s);
                    ps.setInt(1, Integer.parseInt(p[0]));
                    ps.setString(2, patient.getFirstName());
                    ps.setString(3,patient.getLastName());
                    ps.setDate(4, java.sql.Date.valueOf(patient.getT().getTest_date().toString()));
                    ps.setTime(5,java.sql.Time.valueOf(patient.getT().getTest_time().toString()));
                    ps.setString(6,"No Result yet");

                    if(ps.executeUpdate()==1){
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Insert");
                        alert.setHeaderText("Test Insertion Completed Successfully");
                        alert.show();
                        lvAvailableTests.getSelectionModel().clearSelection();
                        timecomboBox.getSelectionModel().clearSelection();
                        thread3.run();
                        ps.close();
                    }
                    con.close();
                }
                catch(Exception ex){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Unable to reserve test");
                    alert.show();
                    System.out.println(ex.toString());
                    ex.printStackTrace();
                }
            }
        );

        Button btReceipt = new Button("Receipt");
        btReceipt.setLayoutX(477.0);
        btReceipt.setLayoutY(492.0);
        btReceipt.setMnemonicParsing(false);
        btReceipt.setPrefWidth(127.0);
        btReceipt.setPrefHeight(58.0);
        btReceipt.setFont(Font.font(24.0));
        btReceipt.setOnAction(e->
            {
                String filename = patient.getFirstName()+patient.getLastName()+"Receipt"+".txt";
                try{
                    Connection con = DBLab.getConnection();
                    ResultSet rs = con.createStatement().executeQuery("SELECT `labotest`.`type`,\n" +
                            "    `labotest`.`price`,\n" +
                            "    `reservation`.`date`,\n" +
                            "    `reservation`.`time`,\n" +
                            "    `reservation`.`result`\n" +
                            "    FROM `labreservation`.`labotest`INNER JOIN `labreservation`.`reservation`\n" +
                            "    ON `labotest`.`number` = `reservation`.`number` \n" +
                            "    WHERE `reservation`.`firstName` = '"+ patient.getFirstName() +"' AND `reservation`.`lastName` ='"+patient.getLastName()+"'\n" +
                            "    ORDER BY `labotest`.`price` ASC;  ");
                    PrintWriter outputStream =
                            new PrintWriter(new FileOutputStream(filename));
                    outputStream.print("Patient: "+patient.getFirstName()+" "+patient.getLastName()+"\n\n");
                    double [] prices = new double[4];
                    int i = 0;
                    while (rs.next()) {
                        prices[i] = rs.getDouble("price");
                        i++;
                        outputStream.print(rs.getString("type")+"\t");
                        outputStream.print(rs.getDouble("price")+"$\t");
                        outputStream.print(rs.getString("date")+"\t");
                        outputStream.print(rs.getString("time")+"\t");
                        if (rs.getString("result")==null){
                            outputStream.print("No Result yet"+"\n");
                        }
                        else outputStream.print(rs.getString("result")+"\n");
                    }
                    outputStream.print("\nThe total price to pay(with VAT): ");
                    double sum = 0;
                    for(int l=0;l<prices.length;l++){
                        sum+=prices[l];
                    }
                    outputStream.print(sum*1.1);
                    outputStream.print("\n\nThank you!");
                    con.close();
                    outputStream.close();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Receipt");
                    alert.setHeaderText("Receipt is generated");
                    alert.show();
                }
                catch(IOException io)
                {
                    System.out.println("Error reading from file " + filename);
                }
                catch(Exception ex){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Receipt");
                    alert.setHeaderText(ex.toString());
                    alert.setContentText(ex.getLocalizedMessage());
                    alert.show();
                    System.out.println(ex.toString());
                    ex.printStackTrace();
                }
            }
        );

        Label lbDateTime = new Label("Choose Date & Time ");
        lbDateTime.setLayoutX(130.0);
        lbDateTime.setLayoutY(150.0);
        lbDateTime.setFont(Font.font(14.0));

        patientPane.getChildren().addAll(btCancellation,btReceipt,btConfirmation,lbDateTime,
                lvAvailableTests, lvReservedTests, datePicker,timecomboBox,imageView);
        Stage secondaryStage = new Stage();
        Scene homeScene = new Scene(patientPane,720,623);
        secondaryStage.setTitle("Home");
        secondaryStage.setScene(homeScene);
        secondaryStage.show();
    }
    class ImageViewThread extends Thread{
        private ImageView myImageView;

        public ImageViewThread (ImageView myImageView) {
            this.myImageView = myImageView;
        }

        public void run(){
            myImageView.setFitHeight(142.0);
            myImageView.setFitWidth(720);
            myImageView.setPickOnBounds(true);
            myImageView.setPreserveRatio(false);
            Image myImage = new Image(getClass().getResourceAsStream("MedicalCareLab.jpg"));
            myImageView. setImage(myImage);
        }
    }
    class ComboBoxThread extends Thread{
        private  ComboBox<String> comboBox;

        public ComboBoxThread(ComboBox<String> comboBox) {
            this.comboBox = comboBox;
        }

        public void run(){
            comboBox.setLayoutX(195.0);
            comboBox.setLayoutY(180.0);
            comboBox.setPrefWidth(150.0);
            comboBox.setPromptText("hh:mm:ss");
            LocalTime time = LocalTime.of(9,0,0);
            for (int i=0;i<=20;i++) {
                comboBox.getItems().add(time.toString()+":00");
                time = time.plusMinutes(15);
            }
        }
    }
    class RListViewThread extends Thread{
        private ListView<Labotest> listView;
        private Patient patient;

        public RListViewThread(ListView<Labotest> listView, Patient patient) {
            this.listView = listView;
            this.patient = patient;
        }

        public void run(){
            listView.getItems().clear();
            try{
                Connection con = DBLab.getConnection();
                ResultSet rs = con.createStatement().executeQuery("SELECT `labotest`.`number`,\n" +
                        "    `labotest`.`type`,\n" +
                        "    `labotest`.`price`,\n" +
                        "    `reservation`.`date`,\n" +
                        "    `reservation`.`time`\n" +
                        "    FROM `labreservation`.`labotest`INNER JOIN `labreservation`.`reservation`\n" +
                        "    ON `labotest`.`number` = `reservation`.`number` \n" +
                        "    WHERE `reservation`.`firstName` = '"+ patient.getFirstName() +"' AND `reservation`.`lastName` ='"+patient.getLastName()+"';");
                while (rs.next()) {
                    String [] a = rs.getDate("date").toString().split("-");
                    Date date = new Date(Integer.parseInt(a[0]),Integer.parseInt(a[1]),Integer.parseInt(a[2]));

                    a = rs.getTime("time").toString().split(":");
                    Time time = new Time(Integer.parseInt(a[0]),Integer.parseInt(a[1]),Integer.parseInt(a[2]));
                    listView.getItems().add(new Labotest (rs.getInt("number"), rs.getString("type"),
                            rs.getDouble("price"), date,time));
                }
                con.close();
            }
            catch(Exception ex){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Reserved Tests");
                alert.setHeaderText(ex.getLocalizedMessage());
                alert.show();
                System.out.println(ex.toString());
                ex.printStackTrace();
            }
        }
    }
}
