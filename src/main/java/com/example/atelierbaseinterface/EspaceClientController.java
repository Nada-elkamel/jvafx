package com.example.atelierbaseinterface;

import com.example.atelierbaseinterface.Connexion.Connexion;
import com.example.atelierbaseinterface.entites.Marathon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class EspaceClientController implements Initializable {

    Statement smt;
    PreparedStatement ps;
    ResultSet result;
    Alert alert=null;
    @FXML
    private TableView<Marathon> tableMarathonClient;

    @FXML
    private TableColumn<Marathon,String> tarrivee;

    @FXML
    private TableColumn<Marathon,Integer> tcodeMarathon;

    @FXML
    private TableColumn<Marathon,String> tdate;

    @FXML
    private TableColumn<Marathon,String> tdepart;

    @FXML
    private TableColumn<Marathon, Float> tdistance;

    @FXML
    private TableColumn<Marathon,String > tname;

    ObservableList<Marathon> dataMarathon = FXCollections.observableArrayList();

    public void logout(ActionEvent actionEvent) {
        // Fermer la fenêtre actuelle
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        currentStage.close();

        try {
            // Charger la page de connexion
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène pour la page de connexion
            Scene scene = new Scene(root);

            // Créer une nouvelle fenêtre pour la scène de connexion
            Stage loginStage = new Stage();
            loginStage.setScene(scene);
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tcodeMarathon.setCellValueFactory(new PropertyValueFactory<Marathon,Integer>("codeMarathon"));
        tname.setCellValueFactory(new PropertyValueFactory<Marathon,String>("nom"));
        tdate.setCellValueFactory(new PropertyValueFactory<Marathon,String>("date"));
        tdepart.setCellValueFactory(new PropertyValueFactory<Marathon,String>("lieuDepart"));
        tarrivee.setCellValueFactory(new PropertyValueFactory<Marathon,String>("lieuArrivee"));
        tdistance.setCellValueFactory(new PropertyValueFactory<Marathon,Float>("distance"));

        tableMarathonClient.setItems(dataMarathon);
        consulter(null);
    }

    private void consulter(Object o) {
        try {
            Connexion con = new Connexion();
            Connection c = con.connecter();
            smt=(Statement) c.createStatement();
            result = smt.executeQuery("select * from marathon");

            while (result.next()){
                Integer code = result.getInt(1);
                String nom= result.getString(2);
                String date = result.getString(3);
                String depart = result.getString(4);
                String arrivee = result.getString(5);
                Float distance= result.getFloat(6);
                Marathon m = new Marathon(code,nom,date,depart,arrivee,distance);

                dataMarathon.add(m);
            }

            tableMarathonClient.setItems(dataMarathon);
        } catch (SQLException e) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Unable to view database");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    public void btnDashboard(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboardClient.fxml"));
            Parent signUpPage = loader.load();
            Scene signUpScene = new Scene(signUpPage);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(signUpScene);
            stage.setTitle("Dashboard Client"); // Ajoutez le titre souhaité
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnRegister(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("registerClient.fxml"));
            Parent signUpPage = loader.load();
            Scene signUpScene = new Scene(signUpPage);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(signUpScene);
            stage.setTitle("Register"); // Ajoutez le titre souhaité
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
