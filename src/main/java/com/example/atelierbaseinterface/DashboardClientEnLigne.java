package com.example.atelierbaseinterface;

import com.example.atelierbaseinterface.Connexion.Connexion;
import com.example.atelierbaseinterface.entites.Marathon;
import com.example.atelierbaseinterface.entites.Participation;
import com.example.atelierbaseinterface.entites.Runner;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.awt.Desktop;
import java.net.URL;
import java.sql.*;
import java.util.Comparator;
import java.util.ResourceBundle;

public class DashboardClientEnLigne implements Initializable {
    Statement smt;
    PreparedStatement ps;
    ResultSet result;
    Alert alert=null;

    @FXML
    private TableColumn<Participation, Marathon> tcodeMarathon;

    @FXML
    private TableColumn<Participation, Runner> tcodeRunner;

    @FXML
    private TableView<Participation> ttableParticipation;

    @FXML
    private TableColumn<Participation, Float> ttimeCol;
    ObservableList<Participation> dataParticipation = FXCollections.observableArrayList();


    public void btnMarathonList(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("espaceClient.fxml"));
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

    public void viewRankingList(ActionEvent actionEvent) {
        // Créer une instance de FileChooser
        FileChooser fileChooser = new FileChooser();

        // Définir le titre de la fenêtre de dialogue
        fileChooser.setTitle("Select the ranking list file");

        // Définir le filtre de type de fichier (optionnel)
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers texte (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        // Afficher la fenêtre de dialogue pour sélectionner le fichier de ranking list
        File file = fileChooser.showOpenDialog(((Node) actionEvent.getSource()).getScene().getWindow());
        if (file != null) {
            // Ouvrir le fichier de ranking list avec l'application par défaut
            try {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.OPEN)) {
                    desktop.open(file);
                } else {
                    // Afficher un message d'erreur si l'ouverture du fichier n'est pas supportée
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error opening file");
                    alert.setContentText("Opening the file is not supported on this system.");
                    alert.showAndWait();
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Afficher une boîte de dialogue d'erreur en cas d'échec d'ouverture du fichier
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error opening file");
                alert.setContentText("An error occurred while opening the ranking list file");
                alert.showAndWait();
            }
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tcodeMarathon.setCellValueFactory(new PropertyValueFactory<Participation, Marathon>("codeMarathon"));
        tcodeRunner.setCellValueFactory(new PropertyValueFactory<Participation, Runner>("codeRunner"));
        ttimeCol.setCellValueFactory(new PropertyValueFactory<Participation,Float>("temps"));

        ttableParticipation.setItems(dataParticipation);

        // Charger les coureurs à partir de la base de données
        consulter(null);

        dataParticipation.sort(Comparator.comparingDouble(Participation::getTemps).reversed());
        ttableParticipation.setItems(dataParticipation);

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

            ttableParticipation.setItems(dataParticipation);
        } catch (SQLException e) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Unable to view database");
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}
