package program;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        primaryStage.setTitle("Contractor");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static void connect(){

        Connection connection = null;

        try {
            String url = "jdbc:sqlite:test.db";
            connection = DriverManager.getConnection(url);
            System.out.println("database connected");

            PreparedStatement create = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS 'Contractor' ('ContractorID' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,'Name' TEXT, 'Username' TEXT, 'Password' TEXT, 'Email' TEXT, 'PhoneNumber' TEXT)");
            PreparedStatement create2 = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS 'Corporation' ('CorporationID' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,'Name' TEXT, 'Username' TEXT, 'Password' TEXT, 'Email' TEXT, 'PhoneNumber' TEXT)");
            PreparedStatement create3 = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS 'Job' ('JobID' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'Type' TEXT, 'Address' TEXT, 'Date' TEXT, 'Status' TEXT, 'Budget' INTEGER, 'ContractorID' TEXT, FOREIGN KEY (ContractorID) REFERENCES 'Contractor' ('ContractorID'))");
            PreparedStatement create4 = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS 'Equipment' ('EquipmentID' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,'EquipmentName' TEXT, 'Price' INTEGER)");
            PreparedStatement create5 = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS 'EquipmentList' ('JobID' INTEGER, 'EquipmentID' INTEGER, 'Quantity' INTEGER, 'Amount' INTEGER, 'Detail' TEXT, PRIMARY KEY (JobID, EquipmentID), FOREIGN KEY (JobID) REFERENCES 'Job' ('JobID'), FOREIGN KEY (EquipmentID) REFERENCES 'Equipment' ('EquipmentID'))");

            create.execute();
            create2.execute();
            create3.execute();
            create4.execute();
            create5.execute();


        }catch (SQLException e){
            System.err.println(e.getMessage());
        }finally {
            try {
                if(connection != null){
                    connection.close();
                }
            }catch (SQLException e){
                System.err.println(e.getMessage());
            }

        }
    }


    public static void main(String[] args) {
        connect();
        launch(args);
    }
}
