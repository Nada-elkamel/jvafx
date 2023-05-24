package com.example.atelierbaseinterface;

import com.example.atelierbaseinterface.Connexion.Connexion;
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


public class DashboardSponsor implements Initializable {

    Statement smt;
    PreparedStatement ps;
    ResultSet result;
    Alert alert=null;

    @FXML
    private Stage stage;

    @FXML
    private TableColumn<Sponsor,Float> amountCol;

    @FXML
    private TableColumn<Sponsor,String> companyCol;

    @FXML
    private TableColumn<Sponsor,String> emailCol;


    @FXML
    private TableColumn<Sponsor, String> nameCol;

    @FXML
    private TableColumn<Sponsor, Integer> phoneCol;

    @FXML
    private TableColumn<Sponsor,String> codeSponsorCol;

    @FXML
    private TextField tamount;

    @FXML
    private TextField tcodeSponsor;

    @FXML
    private TextField tcompany;

    @FXML
    private TextField temail;

    @FXML
    private TextField tname;

    @FXML
    private TextField tphone;

    @FXML
    private TableView<Sponsor> tableSponsor;
    ObservableList<Sponsor> dataSponsor = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        codeSponsorCol.setCellValueFactory(new PropertyValueFactory<Sponsor,String>("codeSponsor"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Sponsor,String>("name"));
        companyCol.setCellValueFactory(new PropertyValueFactory<Sponsor,String>("company"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<Sponsor,Integer>("phonenumberS"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Sponsor,String>("emailS"));
        amountCol.setCellValueFactory(new PropertyValueFactory<Sponsor,Float>("amount"));

        tableSponsor.setItems(dataSponsor);

        // Charger les coureurs à partir de la base de données
        consulter(null);
    }

    private void consulter(Object o) {
        try {
            Connexion con = new Connexion();
            Connection c = con.connecter();
            smt=(Statement) c.createStatement();
            result = smt.executeQuery("select * from sponsor");

            while (result.next()){
                Integer code = result.getInt(1);
                String name= result.getString(2);
                String company = result.getString(3);
                Integer phone = result.getInt(4);
                String email= result.getString(5);
                Float amount = result.getFloat(6);
                Sponsor s = new Sponsor(code,name,company,phone,email,amount);

                dataSponsor.add(s);
            }

            tableSponsor.setItems(dataSponsor);
        } catch (SQLException e) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Unable to consult the database");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    public void btnListMarathon(ActionEvent actionEvent) {
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

    public void closeSponsor(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void minimizeSponsor(ActionEvent actionEvent) {
        stage = (Stage) ((javafx.scene.control.Button) actionEvent.getSource()).getScene().getWindow();
        stage.setIconified(true);
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

    public void addSponsor(ActionEvent actionEvent) {
        if (tcodeSponsor.getText().isEmpty() || tname.getText().isEmpty() || tcompany.getText().isEmpty() ||
                tphone.getText().isEmpty() || temail.getText().isEmpty() || tamount.getText().isEmpty()) {
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
            PreparedStatement ps = c.prepareStatement("INSERT INTO sponsor (codeSponsor, name, company, phonenumberS, emailS, amount) VALUES (?, ?, ?, ?, ?, ?);");
            ps.setInt(1, Integer.parseInt(tcodeSponsor.getText()));
            ps.setString(2, tname.getText());
            ps.setString(3, tcompany.getText());
            ps.setInt(4, Integer.parseInt(tphone.getText()));
            ps.setString(5, temail.getText());
            ps.setString(6, String.valueOf(Float.parseFloat(tamount.getText())));

            int ligneInsere = ps.executeUpdate();
            if (ligneInsere > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Confirmation of registration");
                alert.setContentText("Registration completed successfully");
                alert.showAndWait();

                // Ajouter le nouveau coureur à la liste existante
                Sponsor newSponsor = new Sponsor(Integer.parseInt(tcodeSponsor.getText()), tname.getText(), tcompany.getText(), Integer.parseInt(tphone.getText()), temail.getText(), Float.parseFloat(tamount.getText()));
                tableSponsor.getItems().add(newSponsor);
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
            alert.setContentText("Unable to save sponsor");
            alert.showAndWait();
            e.printStackTrace();
        }
    }


    public void updateSponsor(ActionEvent actionEvent) {
        Sponsor selectedSponsor = tableSponsor.getSelectionModel().getSelectedItem();

        if (selectedSponsor == null) {
            // Aucun coureur sélectionné
            return;
        }

        try {
            Connexion con = new Connexion();
            Connection c = con.connecter();
            PreparedStatement ps = c.prepareStatement("UPDATE sponsor SET name=?, company=?, phonenumberS=?,emailS=?, amount=? WHERE codeSponsor=?");

            ps.setString(1, tname.getText());
            ps.setString(2, tcompany.getText());
            ps.setInt(3, Integer.parseInt(tphone.getText()));
            ps.setString(4, temail.getText());
            ps.setString(5, String.valueOf(Float.parseFloat(tamount.getText())));
            ps.setInt(6, selectedSponsor.getCodeSponsor());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Update confirmation");
                alert.setContentText("Update completed successfully");
                alert.showAndWait();

                // Mettre à jour les champs du coureur sélectionné dans la table
                selectedSponsor.setName(tname.getText());
                selectedSponsor.setCompany(tcompany.getText());
                selectedSponsor.setPhonenumberS(Integer.parseInt(tphone.getText()));
                selectedSponsor.setEmailS(temail.getText());
                selectedSponsor.setAmount(Float.parseFloat(tamount.getText()));

                // Rafraîchir la table
                tableSponsor.refresh();
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
            alert.setContentText("Unable to update sponsor");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    public void deleteSponsor(ActionEvent actionEvent) {
        Sponsor selectedSponsor = tableSponsor.getSelectionModel().getSelectedItem();
        if (selectedSponsor == null) {
            // No sponsor selected, show an error message
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setContentText("No sponsor selected");
            errorAlert.showAndWait();
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete confirmation");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to delete this sponsor?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Connexion con = new Connexion();
                Connection c = con.connecter();
                ps = c.prepareStatement("DELETE FROM sponsor WHERE codeSponsor = ?");
                ps.setInt(1, selectedSponsor.getCodeSponsor());

                int rowsDeleted = ps.executeUpdate();
                if (rowsDeleted > 0) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Confirmation");
                    successAlert.setContentText("Sponsor has been successfully removed.");
                    successAlert.showAndWait();

                    // Refresh the table view
                    ObservableList<Sponsor> sponsorList = tableSponsor.getItems();
                    sponsorList.remove(selectedSponsor);
                    tableSponsor.refresh();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setContentText("Unable to remove sponsor.");
                    errorAlert.showAndWait();
                }
            } catch (SQLException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setContentText("An error occurred while deleting the sponsor.");
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

    public void addSponsorSelect(){
        Sponsor serDate = tableSponsor.getSelectionModel().getSelectedItem();
        int num = tableSponsor.getSelectionModel().getSelectedIndex();
        if ((num-1)< -1)
        {return;}
        tcodeSponsor.setText(String.valueOf(serDate.getCodeSponsor()));
        tname.setText(String.valueOf(serDate.getName()));
        tamount.setText(String.valueOf(serDate.getAmount()));
        tcompany.setText(String.valueOf(serDate.getCompany()));
        tphone.setText(String.valueOf(serDate.getPhonenumberS()));
        temail.setText(String.valueOf(serDate.getEmailS()));
    }
}



