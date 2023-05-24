package com.example.atelierbaseinterface;

import com.example.atelierbaseinterface.Connexion.Connexion;
import com.example.atelierbaseinterface.entites.Marathon;
import com.example.atelierbaseinterface.entites.Runner;
import com.example.atelierbaseinterface.entites.Sponsor;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashboardMarathon implements Initializable {
    Statement smt;
    PreparedStatement ps;
    ResultSet result;
    Alert alert=null;


    @FXML
    private Stage stage;

    @FXML
    private TableColumn<Marathon,String> arrivalCol;

    @FXML
    private TableColumn<Marathon,String> codeMarathonCol;

    @FXML
    private TableColumn<Marathon,String> dateCol;

    @FXML
    private TableColumn<Marathon,String > departureCol;

    @FXML
    private TableColumn<Marathon,Float> distanceCol;

    @FXML
    private TableColumn<Marathon,String> nameCol;

    @FXML
    private TableView<Marathon> tableMarathon;

    @FXML
    private TextField tarrival;

    @FXML
    private TextField tcodeMarathon;

    @FXML
    private TextField tdate;

    @FXML
    private TextField tdeparture;

    @FXML
    private TextField tdistance;

    @FXML
    private TextField tname;

    ObservableList<Marathon> dataMarathon = FXCollections.observableArrayList();

    public void closedashMarathon(ActionEvent actionEvent) {
        Platform.exit();
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

    public void minimizeMarathon(ActionEvent actionEvent) {
        stage = (Stage) ((javafx.scene.control.Button) actionEvent.getSource()).getScene().getWindow();
        stage.setIconified(true);
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

    public void addMarathon(ActionEvent actionEvent) {
        if (tcodeMarathon.getText().isEmpty() || tname.getText().isEmpty() || tdate.getText().isEmpty() ||
                tdeparture.getText().isEmpty() || tarrival.getText().isEmpty() || tdistance.getText().isEmpty()) {
            // Display an alert indicating that all fields should be filled
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registration error");
            alert.setContentText("Please complete all fields.");
            alert.showAndWait();
            return;
        }
        try {
            Connexion con = new Connexion();
            Connection c = con.connecter();
            PreparedStatement ps = c.prepareStatement("INSERT INTO marathon (codeMarathon, nom, date, lieuDepart, lieuArrivee, distance) VALUES (?, ?, ?, ?, ?, ?);");
            ps.setString(1, String.valueOf(Integer.parseInt(tcodeMarathon.getText())));
            ps.setString(2, tname.getText());
            ps.setString(3, tdate.getText());
            ps.setString(4, tdeparture.getText());
            ps.setString(5, tarrival.getText());
            ps.setFloat(6, Float.parseFloat(tdistance.getText()));

            int ligneInsere = ps.executeUpdate();
            if (ligneInsere > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Confirmation of registration");
                alert.setContentText("Registration completed successfully");
                alert.showAndWait();

                // Ajouter le nouveau coureur à la liste existante
                Marathon newMarathon = new Marathon(Integer.parseInt(tcodeMarathon.getText()), tname.getText(), tdate.getText(), tdeparture.getText(), tarrival.getText(),Float.parseFloat(tdistance.getText()));
                tableMarathon.getItems().add(newMarathon);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Form Error !");
                alert.setContentText("Error saving");
                alert.showAndWait();
            }
            ps.close();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Insertion Error");
            alert.setContentText("Unable to record marathon");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        codeMarathonCol.setCellValueFactory(new PropertyValueFactory<Marathon,String>("codeMarathon"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Marathon,String>("nom"));
        dateCol.setCellValueFactory(new PropertyValueFactory<Marathon,String>("date"));
        departureCol.setCellValueFactory(new PropertyValueFactory<Marathon,String>("lieuDepart"));
        arrivalCol.setCellValueFactory(new PropertyValueFactory<Marathon,String>("lieuArrivee"));
        distanceCol.setCellValueFactory(new PropertyValueFactory<Marathon,Float>("distance"));

        tableMarathon.setItems(dataMarathon);
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

            tableMarathon.setItems(dataMarathon);
        } catch (SQLException e) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Unable to view database");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    public void updateMarathon(ActionEvent actionEvent) {
        Marathon selectedMarathon = tableMarathon.getSelectionModel().getSelectedItem();

        if (selectedMarathon == null) {
            return;
        }

        try {
            Connexion con = new Connexion();
            Connection c = con.connecter();
            PreparedStatement ps = c.prepareStatement("UPDATE marathon SET nom=?, date=?, lieuDepart=?, lieuArrivee=?, distance=? WHERE codeMarathon=?");

            ps.setString(1, tname.getText());
            ps.setString(2, tdate.getText());
            ps.setString(3, tdeparture.getText());
            ps.setString(4, tarrival.getText());
            ps.setFloat(5,Float.parseFloat(tdistance.getText()));
            ps.setInt(6, selectedMarathon.getCodeMarathon());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Update confirmation");
                alert.setContentText("Update completed successfully");
                alert.showAndWait();

                // Mettre à jour les champs du coureur sélectionné dans la table
                selectedMarathon.setNom(tname.getText());
                selectedMarathon.setDate(tdate.getText());
                selectedMarathon.setLieuDepart(tdeparture.getText());
                selectedMarathon.setLieuArrivee(tarrival.getText());
                selectedMarathon.setDistance(Float.parseFloat(tdistance.getText()));

                // Rafraîchir la table
                tableMarathon.refresh();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Form Error !");
                alert.setContentText("Error while updating");
                alert.showAndWait();
            }
            ps.close();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Update Error");
            alert.setContentText("Unable to update marathon");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    public void deleteMarathon(ActionEvent actionEvent) {
        Marathon selectedMarathon = tableMarathon.getSelectionModel().getSelectedItem();
        if (selectedMarathon == null) {
            // No sponsor selected, show an error message
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setContentText("No marathon selected");
            errorAlert.showAndWait();
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete Confirmation");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to delete this marathon?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Connexion con = new Connexion();
                Connection c = con.connecter();

                // Supprimer les coureurs associés
                PreparedStatement deleteRunnersStmt = c.prepareStatement("DELETE FROM runner WHERE marathonId = ?");
                deleteRunnersStmt.setInt(1, selectedMarathon.getCodeMarathon());
                deleteRunnersStmt.executeUpdate();

                // Supprimer le marathon
                PreparedStatement deleteMarathonStmt = c.prepareStatement("DELETE FROM marathon WHERE codeMarathon = ?");
                deleteMarathonStmt.setInt(1, selectedMarathon.getCodeMarathon());
                int rowsDeleted = deleteMarathonStmt.executeUpdate();

                if (rowsDeleted > 0) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Confirmation");
                    successAlert.setContentText("The marathon has been successfully deleted.");
                    successAlert.showAndWait();

                    // Refresh the table view
                    ObservableList<Marathon> marathonList = tableMarathon.getItems();
                    marathonList.remove(selectedMarathon);
                    tableMarathon.refresh();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setContentText("Unable to delete marathon.");
                    errorAlert.showAndWait();
                }
            } catch (SQLException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setContentText("An error occurred while deleting the marathon.");
                errorAlert.showAndWait();
                e.printStackTrace();
            }
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

    public void btnRankingList(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("rankingList.fxml"));
            Parent signUpPage = loader.load();
            Scene signUpScene = new Scene(signUpPage);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(signUpScene);
            stage.setTitle("Ranking List"); // Ajoutez le titre souhaité
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void addMarathonSelect(){
        Marathon serDate = tableMarathon.getSelectionModel().getSelectedItem();
        int num = tableMarathon.getSelectionModel().getSelectedIndex();
        if ((num-1)< -1)
        {return;}
        tcodeMarathon.setText(String.valueOf(serDate.getCodeMarathon()));
        tname.setText(String.valueOf(serDate.getNom()));
        tdate.setText(String.valueOf(serDate.getDate()));
        tdeparture.setText(String.valueOf(serDate.getLieuDepart()));
        tarrival.setText(String.valueOf(serDate.getLieuArrivee()));
        tdistance.setText(String.valueOf(serDate.getDistance()));

    }
}

