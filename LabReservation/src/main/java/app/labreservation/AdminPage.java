package app.labreservation;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import java.sql.*;

public class AdminPage {
    public void show (Stage primaryStage){

        TableView<Labotest> tableView = new TableView<>();
        setcolumns(tableView);
        //BorderPane.alignment="CENTER";

        BorderPane borderPane = new BorderPane();
        borderPane.setPrefHeight(400.0);
        borderPane.setPrefWidth(600);
        borderPane.setTop(tableView);
        borderPane.setBottom(getGridPane(tableView));

        Scene adminScene = new Scene(borderPane,600,370);
        primaryStage.setTitle("Admin");
        primaryStage.setScene(adminScene);
        primaryStage.show();
    }

    public void setcolumns(TableView <Labotest> tableView){
        tableView.setPrefHeight(210.0);
        tableView.setPrefWidth(600.0);
        tableView.setEditable(false);
        TableColumn<Labotest,String> colNumber = new TableColumn<>("Number");
        TableColumn<Labotest,String> colType = new TableColumn<>("Test Type");
        TableColumn<Labotest, String> colPrice = new TableColumn<>("Price");
        TableColumn<Labotest,String> colOption1 = new TableColumn<>("Option1");
        TableColumn<Labotest,String> colOption2 = new TableColumn<>("Option2");
        colType.setCellValueFactory (new PropertyValueFactory("type") ) ;
        colNumber.setCellValueFactory (new PropertyValueFactory("num") ) ;
        colPrice.setCellValueFactory (new PropertyValueFactory("price") ) ;
        colOption1.setCellValueFactory (new PropertyValueFactory("Option1") ) ;
        colOption2.setCellValueFactory (new PropertyValueFactory("Option2") ) ;
        tableView.getColumns().add(colNumber);
        tableView.getColumns().add(colType);
        tableView.getColumns().add(colPrice);
        tableView.getColumns().add(colOption1);
        tableView.getColumns().add(colOption2);
        try{
            Connection con = DBLab.getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM labreservation.labotest");
            while (rs.next()) {
                tableView.getItems().add(new Labotest(rs.getInt("number"), rs.getString("type"),
                        rs.getDouble("price"), rs.getString("option1"),rs.getString("option2")));
            }
            con.close();
        }
        catch(Exception e){
                e.printStackTrace();
        }
    }
    public void updatecolumns(TableView <Labotest> tableView){
        try{
            for ( int i = 0; i<tableView.getItems().size(); i++) {
                tableView.getItems().clear();
            }
            Connection con = DBLab.getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM labreservation.labotest");
            while (rs.next()) {
                tableView.getItems().add(new Labotest(rs.getInt("number"), rs.getString("type"),
                        rs.getDouble("price"), rs.getString("option1"),rs.getString("option2")));
            }
            con.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    private GridPane getGridPane(TableView<Labotest> tableView){

        GridPane gridPane = new GridPane();
        gridPane.setPrefHeight(170.0);
        gridPane.setPrefWidth(600.0);
        gridPane.setHgap(5);
        gridPane.setVgap(10);
        // BorderPane.alignment="CENTER"
        //<RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES" />
        //<ColumnConstraints hgrow="SOMETIMES" minWidth="10.0" prefWidth="100.0" />

        TextField tfOption1 = new TextField();
        gridPane.add(tfOption1, 3, 0);

        TextField tfOption2 = new TextField();
        gridPane.add(tfOption2, 3, 1);
        //prefHeight="26.0" prefWidth="90.0"

        TextField tftestNum = new TextField();
        gridPane.add(tftestNum, 1, 0);

        TextField tftestPrice = new TextField();
        gridPane.add(tftestPrice, 1, 2);

        TextField tftestType = new TextField();
        gridPane.add(tftestType, 1, 1);

        Button btnDeletetest = new Button("Delete");
        btnDeletetest.setAlignment(Pos.CENTER);
        btnDeletetest.setMnemonicParsing(false);
        btnDeletetest.setPrefHeight(26.0);
        btnDeletetest.setPrefWidth(150);
        gridPane.add(btnDeletetest, 2, 3);
        btnDeletetest.setOnAction(e ->
            {
                try{
                    Connection con = DBLab.getConnection();
                    String s = "DELETE FROM `labreservation`.`labotest` WHERE `number` = ?";
                    PreparedStatement ps = con.prepareStatement(s);
                    if (tftestNum.getText()!=""){
                        ps.setInt(1, Integer.parseInt(tftestNum.getText()));
                        if(ps.executeUpdate()==1){
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Delete");
                            alert.setHeaderText("Test Deleted Successfully");
                            alert.show();
                            ps.close();
                            updatecolumns(tableView);
                        }
                        else {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Delete");
                            alert.setHeaderText("Test Number doesn't exist");
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

        Button btnInserttest = new Button("Console Insertion");
        btnInserttest.setAlignment(Pos.CENTER);
        btnInserttest.setMnemonicParsing(false);
        btnInserttest.setPrefHeight(26.0);
        btnInserttest.setPrefWidth(150);
        gridPane.add(btnInserttest, 0, 3);
        btnInserttest.setOnAction(e ->
            {
                try{
                    Labotest test = new Labotest();
                    Connection con = DBLab.getConnection();
                    ResultSet rs = con.createStatement().executeQuery("SELECT number FROM labreservation.labotest ORDER BY number DESC LIMIT 1");
                    rs.next();
                    String s = "INSERT INTO `labreservation`.`labotest` (`number`, `type`, `price`, `option1`, `option2`) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement ps = con.prepareStatement(s);
                    ps.setInt(1, test.getNum());
                    ps.setString(2, test.getType());
                    ps.setDouble(3, test.getPrice());
                    ps.setString(4,test.getOption1());
                    ps.setString(5,test.getOption2());
                    if(ps.executeUpdate()==1){
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Insert");
                        alert.setHeaderText("Test Insertion Completed Successfully");
                        alert.show();
                        ps.close();
                    }
                    tableView.getItems().add(test);
                    con.close();
                }
                catch(Exception ex){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Insert");
                    alert.setHeaderText("Unable to insert test!");
                    alert.show();
                    System.out.println(ex.toString());
                    ex.printStackTrace();
                }
            }
        );

        Button btnUpdatetest = new Button("Update");
        btnUpdatetest.setAlignment(Pos.CENTER);
        btnUpdatetest.setMnemonicParsing(false);
        btnUpdatetest.setPrefHeight(26.0);
        btnUpdatetest.setPrefWidth(150);
        gridPane.add(btnUpdatetest, 1, 3);
        btnUpdatetest.setOnAction(e ->
            {
                try{
                    Labotest test = new Labotest();
                    Connection con = DBLab.getConnection();
                    if (tftestNum.getText()!=""){
                        test.setNum(Integer.parseInt(tftestNum.getText()));
                        test.setType(tftestType.getText());
                        test.setPrice(Double.parseDouble(tftestPrice.getText()));
                        test.setOptions(new String[]{tfOption1.getText(),tfOption2.getText()});
                        String s = "UPDATE `labreservation`.`labotest` SET `type` = ?, `price` = ?, `option1` = ?, `option2` = ? WHERE `number` = ?";
                        PreparedStatement ps = con.prepareStatement(s);
                        ps.setString(1, test.getType());
                        ps.setDouble(2, test.getPrice());
                        ps.setString(3,test.getOption1());
                        ps.setString(4,test.getOption2());
                        ps.setInt(5, test.getNum());
                        if(ps.executeUpdate()==1){
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Update");
                            alert.setHeaderText("Test " + test.getNum()+ " updated successfully!");
                            alert.show();
                            updatecolumns(tableView);
                            ps.close();
                        }
                        else{
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Update");
                            alert.setHeaderText("Unable to Update test");
                            alert.show();
                            ps.close();
                        }
                        con.close();
                    }
                   else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Update");
                        alert.setHeaderText("Invalid Test Number to update!");
                        alert.show();
                        con.close();
                    }
                }
                catch (SQLException sq){
                    System.out.println(sq.toString());
                    sq.printStackTrace();
                }
                catch(Exception ex){
                    System.out.println(ex.toString());
                    ex.printStackTrace();
                }
            }
        );

        Button btnVerifytest = new Button("Verify Test Type");
        btnVerifytest.setAlignment(Pos.CENTER);
        btnVerifytest.setMnemonicParsing(false);
        btnVerifytest.setPrefHeight(26.0);
        btnVerifytest.setPrefWidth(150);
        gridPane.add(btnVerifytest, 3, 3);
        btnVerifytest.setOnAction(e ->
            {
                try{
                    Connection con = DBLab.getConnection();
                    if(tftestType.getText()==""){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Verify");
                        alert.setHeaderText("Empty Type Text Field");
                        alert.show();
                    }
                    else {
                        ResultSet rs = con.createStatement().executeQuery("SELECT COUNT(1) FROM labreservation.labotest WHERE `type` = '"+ tftestType.getText()+"'");
                        while (rs.next()) {
                            if(rs.getInt(1)==1){
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Verify");
                                alert.setHeaderText("Test "+ tftestType.getText()+" exists!");
                                alert.show();
                            }
                            else {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setTitle("Verify");
                                alert.setHeaderText("Test "+ tftestType.getText()+" doesn't exist!");
                                alert.show();
                            }
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

        Label lbOption1 = new Label("Option 1: ");
        lbOption1.setAlignment(Pos.CENTER_RIGHT);
        lbOption1.setTextAlignment(TextAlignment.RIGHT);
        lbOption1.setPrefWidth(150.0);
        lbOption1.setPrefHeight(18.0);
        gridPane.add(lbOption1, 2, 0);

        Label lbOption2 = new Label("Option 2: ");
        lbOption2.setAlignment(Pos.CENTER_RIGHT);
        lbOption2.setTextAlignment(TextAlignment.RIGHT);
        lbOption2.setPrefWidth(150.0);
        lbOption2.setPrefHeight(18.0);
        gridPane.add(lbOption2, 2, 1);

        Label lbtestNum = new Label("Test Number: ");
        lbtestNum.setAlignment(Pos.CENTER_RIGHT);
        lbtestNum.setTextAlignment(TextAlignment.RIGHT);
        lbtestNum.setPrefWidth(150.0);
        lbtestNum.setPrefHeight(18.0);
        gridPane.add(lbtestNum, 0, 0);

        Label lbtestPrice = new Label("Price: ");
        lbtestPrice.setAlignment(Pos.CENTER_RIGHT);
        lbtestPrice.setTextAlignment(TextAlignment.RIGHT);
        lbtestPrice.setPrefWidth(150.0);
        lbtestPrice.setPrefHeight(18.0);
        gridPane.add(lbtestPrice, 0, 2);

        Label lbtestType = new Label("Test Type: ");
        lbtestType.setAlignment(Pos.CENTER_RIGHT);
        lbtestType.setTextAlignment(TextAlignment.RIGHT);
        lbtestType.setPrefWidth(150.0);
        lbtestType.setPrefHeight(18.0);
        gridPane.add(lbtestType, 0, 1);

        return gridPane;
    }
}
