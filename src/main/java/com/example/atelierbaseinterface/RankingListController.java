package com.example.atelierbaseinterface;

import com.example.atelierbaseinterface.Connexion.Connexion;
import com.example.atelierbaseinterface.entites.Marathon;
import com.example.atelierbaseinterface.entites.Participation;
import com.example.atelierbaseinterface.entites.Runner;
import javafx.application.Platform;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Comparator;
import java.util.ResourceBundle;

public class RankingListController implements Initializable {
    Statement smt;
    PreparedStatement ps;
    ResultSet result;
    Alert alert=null;

    @FXML
    private Stage stage;

    @FXML
    private TableView<Participation> tableParticipation;

    @FXML
    private TableColumn<Participation, Marathon> tcodeMarathonCol;

    @FXML
    private TableColumn<Participation, Runner> tcodeRunnerCol;

    @FXML
    private TableColumn<Participation,Float> ttimeCol;
    ObservableList<Participation> dataParticipation = FXCollections.observableArrayList();


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

    public void close(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void minimize(ActionEvent actionEvent) {
        stage = (Stage) ((javafx.scene.control.Button) actionEvent.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    public void btnMarathonList(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboardMarathon.fxml"));
            Parent signUpPage = loader.load();
            Scene signUpScene = new Scene(signUpPage);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(signUpScene);
            stage.setTitle("Marathon List"); // Ajoutez le titre souhaité
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnListSponsor(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboardSponsor.fxml"));
            Parent signUpPage = loader.load();
            Scene signUpScene = new Scene(signUpPage);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(signUpScene);
            stage.setTitle("Sponsor List"); // Ajoutez le titre souhaité
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnListRunner(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboardCoureur.fxml"));
            Parent signUpPage = loader.load();
            Scene signUpScene = new Scene(signUpPage);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(signUpScene);
            stage.setTitle("Runner List"); // Ajoutez le titre souhaité
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnListAgent(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboardAgent.fxml"));
            Parent signUpPage = loader.load();
            Scene signUpScene = new Scene(signUpPage);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(signUpScene);
            stage.setTitle("Agent List"); // Ajoutez le titre souhaité
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tcodeMarathonCol.setCellValueFactory(new PropertyValueFactory<Participation, Marathon>("codeMarathon"));
        tcodeRunnerCol.setCellValueFactory(new PropertyValueFactory<Participation, Runner>("codeRunner"));
        ttimeCol.setCellValueFactory(new PropertyValueFactory<Participation,Float>("temps"));

        tableParticipation.setItems(dataParticipation);

        // Charger les coureurs à partir de la base de données
        consulter(null);

        //dataParticipation.sort(Comparator.comparingDouble(Participation::getTemps).reversed());
        //tableParticipation.setItems(dataParticipation);
    }

    private void consulter(Object o) {
        try {
            Connexion con = new Connexion();
            Connection c = con.connecter();
            smt=(Statement) c.createStatement();
            result = smt.executeQuery("select * from participation");

            while (result.next()){
                Integer codeM = result.getInt(1);
                Integer codeR = result.getInt(2);
                Float temps = result.getFloat(3);
                Participation p = new Participation(codeM,codeR,temps);

                dataParticipation.add(p);
            }

            tableParticipation.setItems(dataParticipation);
        } catch (SQLException e) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Unable to view database");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    public void search(ActionEvent actionEvent) {
        if (dataParticipation.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Search error");
            alert.setContentText("No entries found.");
            alert.showAndWait();
            return;
        }

        Participation smallestTimeParticipation = dataParticipation.stream()
                .min(Comparator.comparingDouble(Participation::getTemps))
                .orElse(null);

        if (smallestTimeParticipation != null) {
            int runnerId = smallestTimeParticipation.getCodeRunner();

            // Display the runner ID in an alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setContentText("ID of the runner with the shortest time: " + runnerId);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Search error");
            alert.setContentText("No entries found.");
            alert.showAndWait();
        }
    }

}
