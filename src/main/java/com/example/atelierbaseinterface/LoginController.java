package com.example.atelierbaseinterface;

import com.example.atelierbaseinterface.Connexion.Connexion;
import com.example.atelierbaseinterface.entites.Marathon;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.Printer;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LoginController{
    Statement smt;
    PreparedStatement ps;
    ResultSet result;
    Alert alert = null;
    @FXML
    public PasswordField tpassword;

    @FXML
    public TextField tusername;

    @FXML
    public Button btnLogin;


    public void signuppage(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("signUp.fxml"));
            Parent signUpPage = loader.load();
            Scene signUpScene = new Scene(signUpPage);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(signUpScene);
            stage.setTitle("Sign Up Form"); // Ajoutez le titre souhaité
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closelogin(ActionEvent actionEvent) {
        Platform.exit();
    }


    public void login(ActionEvent actionEvent) throws ClassNotFoundException {
        try {
            Connexion con = new Connexion();
            Connection c = con.connecter();
            ps = c.prepareStatement("SELECT * FROM users WHERE username=? AND password=? AND role='Agent'");
            ps.setString(1, tusername.getText());
            ps.setString(2, tpassword.getText());
            result = ps.executeQuery();
            if (result.next()) {
                // L'utilisateur est un agent
                Stage currentStage = (Stage) btnLogin.getScene().getWindow();
                currentStage.close();

                Stage agentDashboardStage = new Stage();
                Parent agentRoot = FXMLLoader.load(getClass().getResource("EspaceAgent.fxml"));
                agentDashboardStage.setTitle("Agent Espace");
                agentDashboardStage.setScene(new Scene(agentRoot, 750, 550));
                agentDashboardStage.show();
            } else {
                // Vérifier si l'utilisateur est un administrateur
                ps = c.prepareStatement("SELECT * FROM users WHERE username=? AND password=? AND role='Admin'");
                ps.setString(1, tusername.getText());
                ps.setString(2, tpassword.getText());
                result = ps.executeQuery();
                if (result.next()) {
                    // L'utilisateur est un administrateur
                    Stage currentStage = (Stage) btnLogin.getScene().getWindow();
                    currentStage.close();

                    Stage adminDashboardStage = new Stage();
                    Parent adminRoot = FXMLLoader.load(getClass().getResource("dashboardMarathon.fxml"));
                    adminDashboardStage.setTitle("Admin Dashboard");
                    adminDashboardStage.setScene(new Scene(adminRoot, 1100, 600));
                    adminDashboardStage.show();
                } else{
                    // Vérifier si l'utilisateur est un rider
                    ps = c.prepareStatement("SELECT * FROM users WHERE username=? AND password=? AND role='Rider'");
                    ps.setString(1, tusername.getText());
                    ps.setString(2, tpassword.getText());
                    result = ps.executeQuery();
                    if (result.next()) {
                        // L'utilisateur est un rider
                        Stage currentStage = (Stage) btnLogin.getScene().getWindow();
                        currentStage.close();

                        Stage riderDashboardStage = new Stage();
                        Parent riderRoot = FXMLLoader.load(getClass().getResource("dashboardClient.fxml"));
                        riderDashboardStage.setTitle("Dashboard Client");
                        riderDashboardStage.setScene(new Scene(riderRoot, 750, 500));
                        riderDashboardStage.show();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Incorrect credentials", ButtonType.OK);
                        alert.show();
                    }
                }
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
