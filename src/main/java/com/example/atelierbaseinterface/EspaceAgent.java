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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Comparator;
import java.util.ResourceBundle;

public class EspaceAgent implements Initializable {

    Statement smt;
    PreparedStatement ps;
    ResultSet result;
    Alert alert=null;

    @FXML
    private ComboBox<Integer> comboListMarathon;

    @FXML
    private ComboBox<Integer> comboListRunner;
    @FXML
    private TextField ttime;
    @FXML
    private TableView<Participation> tableParticipation;

    @FXML
    private TableColumn<Participation, Marathon> tcodeMarathonCol;

    @FXML
    private TableColumn<Participation, Runner> tcodeRunnerCol;

    @FXML
    private TableColumn<Participation, Float> ttimeCol;
    ObservableList<Integer> marathonIdsList = FXCollections.observableArrayList();
    ObservableList<Integer> runnerIdsList = FXCollections.observableArrayList();

    ObservableList<Participation> dataParticipation = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tcodeMarathonCol.setCellValueFactory(new PropertyValueFactory<Participation,Marathon>("codeMarathon"));
        tcodeRunnerCol.setCellValueFactory(new PropertyValueFactory<Participation,Runner>("codeRunner"));
        ttimeCol.setCellValueFactory(new PropertyValueFactory<Participation,Float>("temps"));

        tableParticipation.setItems(dataParticipation);

        // Charger les coureurs à partir de la base de données
        consulter(null);

        ObservableList<Integer> marathonIdsList = getMarathonIds();
        comboListMarathon.setItems(marathonIdsList);

        ObservableList<Integer> runnerIdsList = getRunnerIds();
        comboListRunner.setItems(runnerIdsList);

        dataParticipation.sort(Comparator.comparingDouble(Participation::getTemps).reversed());
        tableParticipation.setItems(dataParticipation);


    }

    private ObservableList<Integer> getRunnerIds() {
        ObservableList<Integer> runnerIdsList = FXCollections.observableArrayList();

        try {
            // Connexion à la base de données
            Connexion con = new Connexion();
            Connection c = con.connecter();

            // Exécution de la requête pour récupérer les IDs des marathons
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT codeRunner FROM runner");

            // Parcours des résultats et ajout des IDs à la liste
            while (resultSet.next()) {
                int runnerId = resultSet.getInt("codeRunner");
                runnerIdsList.add(runnerId);
            }

            resultSet.close();
            statement.close();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return runnerIdsList;
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
    public void register(ActionEvent actionEvent) {
        if (comboListMarathon.getValue() == null || comboListRunner.getValue() == null || ttime.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registration error");
            alert.setContentText("Please complete all fields.");
            alert.showAndWait();
            return;
        }
        try {
            Connexion con = new Connexion();
            Connection c = con.connecter();
            PreparedStatement ps = c.prepareStatement("INSERT INTO participation (codeMarathon,codeRunner,temps) VALUES (?, ?, ?);");
            ps.setInt(1, comboListMarathon.getValue());
            ps.setInt(2, comboListRunner.getValue());
            ps.setFloat(3, Float.parseFloat(ttime.getText()));
            int ligneInsere = ps.executeUpdate();
            if (ligneInsere > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Confirmation of registration");
                alert.setContentText("Registration completed successfully");
                alert.showAndWait();

                // Ajouter le nouveau coureur à la liste existante
                Participation newParticipation = new Participation(Integer.parseInt(String.valueOf(comboListMarathon.getValue())), comboListRunner.getValue(), Float.parseFloat(ttime.getText()));
                tableParticipation.getItems().add(newParticipation);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Form Error !");
                alert.setContentText("Error while saving");
                alert.showAndWait();
            }
            ps.close();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Enregistration Error");
            alert.setContentText("Unable to record runner time");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    public void saveDataToFile(ActionEvent actionEvent) {
        // Créer une instance de FileChooser
        FileChooser fileChooser = new FileChooser();

        // Définir le titre de la fenêtre de dialogue
        fileChooser.setTitle("Enregistrer les données");

        // Définir le filtre de type de fichier (optionnel)
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers texte (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        // Afficher la fenêtre de dialogue pour sélectionner le fichier de destination
        File file = fileChooser.showSaveDialog(((Node) actionEvent.getSource()).getScene().getWindow());

        if (file != null) {
            try {
                // Créer un BufferedWriter pour écrire les données dans le fichier
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));

                // Écrire les en-têtes des colonnes dans le fichier
                writer.write("Code Marathon\tCode Runner\tTemps\n");

                // Parcourir les données de la table view et les écrire dans le fichier
                for (Participation participation : dataParticipation) {
                    writer.write(participation.getCodeMarathon() + "\t" + participation.getCodeRunner() + "\t" + participation.getTemps() + "\n");
                }

                // Fermer le BufferedWriter
                writer.close();

                // Afficher une boîte de dialogue d'information pour indiquer que les données ont été enregistrées
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("recording completed");
                alert.setContentText("The data has been saved to the file.");
                alert.showAndWait();
            } catch (IOException e) {
                // Afficher une boîte de dialogue d'erreur en cas d'échec de l'enregistrement
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Registration error");
                alert.setContentText("An error occurred while saving data.");
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }
}
