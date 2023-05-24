package com.example.atelierbaseinterface;

import com.example.atelierbaseinterface.Connexion.Connexion;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignUpController {

    @FXML
    private RadioButton btAdmin;

    @FXML
    private RadioButton btRider;

    @FXML
    private TextField temail;

    @FXML
    private PasswordField tpassword;

    @FXML
    private PasswordField tpasswordconfirm;

    @FXML
    private TextField tusername;

    public void loginpage(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent loginPage = loader.load();
            Scene loginScene = new Scene(loginPage);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Login Form"); // Ajoutez le titre souhaité
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closesignup(ActionEvent actionEvent) {
        Platform.exit();
    }

    private boolean emailExists(String email) {
        try (
                Connexion con = new Connexion();
                Connection c = con.connecter();
             PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM users WHERE email = ?")) {
            ps.setString(1, email);
            ResultSet result = ps.executeQuery();
            if (result.next()) {
                int count = result.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean insertUser(String username, String password, String role, String email) {
        try (
                Connexion con = new Connexion();
                Connection c = con.connecter();
             PreparedStatement ps = c.prepareStatement("INSERT INTO users (username, password, role, email) VALUES (?, ?, ?, ?)")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);
            ps.setString(4, email);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void signup(ActionEvent actionEvent) {
        Connexion con = new Connexion();
        Connection c = con.connecter();
        String username = tusername.getText();
        String password = tpassword.getText();
        String confirmPassword = tpasswordconfirm.getText();
        String role = "Rider";
        String email = temail.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please complete all fields.");
        } else if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match.");
        } else {
            if (emailExists(email)) {
                showAlert(Alert.AlertType.ERROR, "Error", "Email already exists");
            } else {
                if (insertUser(username, password, role, email)) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Account created successfully.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while creating the account.");
                }
            }
        }
    }

}
