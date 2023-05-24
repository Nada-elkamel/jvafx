package com.example.atelierbaseinterface;

import com.example.atelierbaseinterface.Connexion.Connexion;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class RegisterController implements Initializable {
    @FXML
    private ComboBox<Integer> comboListMarathon;

    @FXML
    private TextField tbirthdate;

    @FXML
    private TextField tcodeRunner;

    @FXML
    private TextField temail;

    @FXML
    private TextField tfirstname;

    @FXML
    private TextField tlastname;

    @FXML
    private TextField tphonenumber;
    public void inscrire(ActionEvent actionEvent) {
        if (tfirstname.getText().isEmpty() || tlastname.getText().isEmpty() ||
                tphonenumber.getText().isEmpty() || tbirthdate.getText().isEmpty() || temail.getText().isEmpty() ||
                comboListMarathon.getValue() == null) {
            // Afficher une alerte indiquant que tous les champs doivent être remplis
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registration error");
            alert.setContentText("Please complete all fields.");
            alert.showAndWait();
            return;
        }
        try {
            Connexion con = new Connexion();
            Connection c = con.connecter();
            PreparedStatement ps = c.prepareStatement("INSERT INTO runner (firstname, lastname, phonenumber, birthdate, email, marathonId) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setString(1, tfirstname.getText());
            ps.setString(2, tlastname.getText());
            ps.setString(3, tphonenumber.getText());
            ps.setString(4, tbirthdate.getText());
            ps.setString(5, temail.getText());
            ps.setInt(6, comboListMarathon.getValue());
            int ligneInsere = ps.executeUpdate();
            if (ligneInsere > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Confirmation of registration");
                alert.setContentText("Registration successfully completed");
                alert.showAndWait();

                // Ajouter le nouveau coureur à la liste existante
                if (tcodeRunner != null) {
                    int codeRunner = Integer.parseInt(tcodeRunner.getText());
                    Runner newRunner = new Runner(codeRunner, tfirstname.getText(), tlastname.getText(), Integer.parseInt(tphonenumber.getText()), tbirthdate.getText(), temail.getText(), comboListMarathon.getValue());
                    //tableRunner.getItems().add(newRunner);
                } else {
                    // Gérer le cas où tcodeRunner est null
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Form Error !");
                alert.setContentText("Error while saving");
                alert.showAndWait();
            }
            ps.close();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registration Error");
            alert.setContentText("Unable to register");
            alert.showAndWait();
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Integer> marathonIdsList = getMarathonIds();
        comboListMarathon.setItems(marathonIdsList);
    }

    private ObservableList<Integer> getMarathonIds() {
        ObservableList<Integer> marathonIdsList = FXCollections.observableArrayList();

        try {
            // Connexion à la base de données
            Connexion con = new Connexion();
            Connection c = con.connecter();

            // Exécution de la requête pour récupérer les IDs des marathons
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT codeMarathon FROM marathon");

            // Parcours des résultats et ajout des IDs à la liste
            while (resultSet.next()) {
                int marathonId = resultSet.getInt("codeMarathon");
                marathonIdsList.add(marathonId);
            }

            resultSet.close();
            statement.close();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return marathonIdsList;
    }

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
}
