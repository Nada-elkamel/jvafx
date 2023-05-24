package com.example.atelierbaseinterface;

import com.example.atelierbaseinterface.Connexion.Connexion;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;

public class DashboardCoureur implements Initializable {
    Statement smt;
    PreparedStatement ps;
    ResultSet result;
    Alert alert=null;

    @FXML
    private TextField tbirthdate;

    @FXML
    private TextField temail;

    @FXML
    private TextField tfirstname;

    @FXML
    private TextField tlastname;

    @FXML
    private TextField tphonenumber;

    @FXML
    private TextField tcodeRunner;
    @FXML
    private ComboBox<String> comboList;

    @FXML
    private Stage stage;

    @FXML
    private TableColumn<Runner, String> emailCol;

    @FXML
    private TableColumn<Runner, String> firstnameCol;

    @FXML
    private TableColumn<Runner, String> lastnameCol;

    @FXML
    private TableColumn<Runner, String> paiementCol;

    @FXML
    private TableColumn<Runner, String> birthdateCol;

    @FXML
    private TableColumn<Runner, Integer> phonenumberCol;

    @FXML
    private TableColumn<Runner, String> codeRunnerCol;
    @FXML
    private TableView<Runner> tableRunner;

    @FXML
    private ComboBox<Integer> comboListMarathon;
    ObservableList<Runner> dataRunner = FXCollections.observableArrayList();

    ObservableList<Integer> marathonIdsList = FXCollections.observableArrayList();

    //private Map<String, Integer> marathonIdMap;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        codeRunnerCol.setCellValueFactory(new PropertyValueFactory<Runner,String>("codeRunner"));
        firstnameCol.setCellValueFactory(new PropertyValueFactory<Runner,String>("firstname"));
        lastnameCol.setCellValueFactory(new PropertyValueFactory<Runner,String>("lastname"));
        phonenumberCol.setCellValueFactory(new PropertyValueFactory<Runner,Integer>("phonenumber"));
        birthdateCol.setCellValueFactory(new PropertyValueFactory<Runner,String>("birthdate"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Runner,String>("email"));
        //paiementCol.setCellValueFactory(new PropertyValueFactory<Runner,String>("paiement"));

        tableRunner.setItems(dataRunner);


        ObservableList<Integer> marathonIdsList = getMarathonIds();
        comboListMarathon.setItems(marathonIdsList);
        
        // Charger les coureurs à partir de la base de données
        consulter(null);

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

    public void closeRunnuer(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void minimizeRunnuer(ActionEvent actionEvent) {
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

    public void inscrire(ActionEvent actionEvent) {
        if (tcodeRunner.getText().isEmpty() || tfirstname.getText().isEmpty() || tlastname.getText().isEmpty() ||
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
            PreparedStatement ps = c.prepareStatement("INSERT INTO runner (codeRunner,firstname, lastname, phonenumber, birthdate, email, marathonId) VALUES (?, ?, ?, ?, ?, ?,?)");
            ps.setInt(1, Integer.parseInt(tcodeRunner.getText()));
            ps.setString(2, tfirstname.getText());
            ps.setString(3, tlastname.getText());
            ps.setString(4, tphonenumber.getText());
            ps.setString(5, tbirthdate.getText());
            ps.setString(6, temail.getText());
            ps.setInt(7, comboListMarathon.getValue());
            int ligneInsere = ps.executeUpdate();
            if (ligneInsere > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Confirmation of registration");
                alert.setContentText("Registration successfully completed");
                alert.showAndWait();

                // Ajouter le nouveau coureur à la liste existante
                Runner newRunner = new Runner(Integer.parseInt(tcodeRunner.getText()),tfirstname.getText(), tlastname.getText(),  Integer.parseInt(tphonenumber.getText()), tbirthdate.getText(), temail.getText(), comboListMarathon.getValue());
                tableRunner.getItems().add(newRunner);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Form Error !");
                alert.setContentText("Error while saving");
                alert.showAndWait();
            }
            ps.close();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Enregistration error");
            alert.setContentText("Unable to save runner");
            alert.showAndWait();
            e.printStackTrace();
        }
    }



    public void consulter(ActionEvent actionEvent) {
        try {
            Connexion con = new Connexion();
            Connection c = con.connecter();
            smt=(Statement) c.createStatement();
            result = smt.executeQuery("select * from runner");

            while (result.next()){
                Integer code = result.getInt(1);
                String fname= result.getString(2);
                String lname = result.getString(3);
                Integer phone = result.getInt(4);
                String date = result.getString(5);
                String email= result.getString(6);
                Integer marathonId = result.getInt(7);
                Runner r = new Runner(code,fname,lname,phone,date,email,marathonId);

                dataRunner.add(r);
            }

            tableRunner.setItems(dataRunner);
        } catch (SQLException e) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Unable to view database");
            alert.showAndWait();
            e.printStackTrace();
        }
    }


    public void updateRunner(ActionEvent actionEvent) {
        Runner selectedRunner = tableRunner.getSelectionModel().getSelectedItem();

        if (selectedRunner == null) {
            // Aucun coureur sélectionné
            return;
        }

        try {
            Connexion con = new Connexion();
            Connection c = con.connecter();
            PreparedStatement ps = c.prepareStatement("UPDATE runner SET firstname=?, lastname=?, phonenumber=?, birthdate=?, email=? ,marathonId=? WHERE codeRunner = ?");

            ps.setString(1, tfirstname.getText());
            ps.setString(2, tlastname.getText());
            ps.setInt(3, Integer.parseInt(tphonenumber.getText()));
            ps.setString(4, tbirthdate.getText());
            ps.setString(5, temail.getText());
            ps.setString(6, String.valueOf(comboListMarathon.getValue()));
            ps.setInt(7, selectedRunner.getCodeRunner());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Update Confirmation");
                alert.setContentText("Update completed successfully");
                alert.showAndWait();

                // Mettre à jour les champs du coureur sélectionné dans la table
                selectedRunner.setFirstname(tfirstname.getText());
                selectedRunner.setLastname(tlastname.getText());
                selectedRunner.setPhonenumber(Integer.parseInt(tphonenumber.getText()));
                selectedRunner.setBirthdate(tbirthdate.getText());
                selectedRunner.setEmail(temail.getText());

                // Rafraîchir la table
                tableRunner.refresh();
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
            alert.setContentText("Unable to update runner");
            alert.showAndWait();
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

    public void deleteRunner(ActionEvent actionEvent) {
        Runner selectedRunner = tableRunner.getSelectionModel().getSelectedItem();
        if (selectedRunner == null) {
            // No sponsor selected, show an error message
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setContentText("No runner selected.");
            errorAlert.showAndWait();
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete Confirmation");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to delete this runner?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Connexion con = new Connexion();
                Connection c = con.connecter();
                c.setAutoCommit(false);  // Démarre une transaction

                // Supprimer l'enregistrement de la table d'association
                ps = c.prepareStatement("DELETE FROM participation WHERE codeRunner = ? ");
                ps.setInt(1, selectedRunner.getCodeRunner());
                ps.executeUpdate();

                // Supprimer l'enregistrement de la table des runners
                ps = c.prepareStatement("DELETE FROM runner WHERE codeRunner = ? ");
                ps.setInt(1, selectedRunner.getCodeRunner());
                int rowsDeleted = ps.executeUpdate();

                if (rowsDeleted > 0) {
                    // Confirmer la transaction et enregistrer les modifications
                    c.commit();

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Confirmation");
                    successAlert.setContentText("The runner was successfully deleted.");
                    successAlert.showAndWait();

                    // Refresh the table view
                    ObservableList<Runner> agentList = tableRunner.getItems();
                    agentList.remove(selectedRunner);
                    tableRunner.refresh();
                } else {
                    // Annuler la transaction si aucun enregistrement n'a été supprimé
                    c.rollback();

                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setContentText("Unable to delete runner.");
                    errorAlert.showAndWait();
                }
            } catch (SQLException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setContentText("An error occurred while deleting the runner.");
                errorAlert.showAndWait();
                e.printStackTrace();
            }
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

    public void acceptRunner() {
        Runner selectedRunner = tableRunner.getSelectionModel().getSelectedItem();
        if (selectedRunner != null) {
            String runnerEmail = selectedRunner.getEmail(); // Get the email of the selected runner

            try {
                String subject = "Marathon Registration Confirmation";
                String content = "Dear Runner,\n\n" +
                        "We are pleased to inform you that your registration for the upcoming marathon has been confirmed.\n" +
                        "Please make sure to arrive at the event venue on time and bring all the necessary equipment and documents.\n" +
                        "If you have any further questions or require any assistance, please don't hesitate to contact us.\n\n" +
                        "Best regards,\n" +
                        "Marathon Team";

                Properties properties = new Properties();
                properties.put("mail.smtp.host", "smtp-relay.sendinblue.com");
                properties.put("mail.smtp.port", 587);
                properties.put("mail.smtp.auth", "true");

                // Create a session with the SMTP server
                Session session = Session.getInstance(properties, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("nada_elkamel2020@yahoo.com", "xE3LXZAnSj0gc5tk");
                    }
                });

                // Create a new MimeMessage object
                Message message = new MimeMessage(session);

                // Set the sender address
                message.setFrom(new InternetAddress("nada_elkamel2020@yahoo.com"));

                // Set the recipient address
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(runnerEmail));

                // Set the email subject and content
                message.setSubject(subject);
                message.setText(content);

                // Send the email
                Transport.send(message);

                System.out.println("Email sent successfully!");
            } catch (MessagingException e) {
                System.out.println("Failed to send email: " + e.getMessage());
            }
        } else {
            // No runner selected, show an error message
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setContentText("No runner selected.");
            errorAlert.showAndWait();
        }
    }


    public void rejectRunner(ActionEvent actionEvent) {
        Runner selectedRunner = tableRunner.getSelectionModel().getSelectedItem();
        if (selectedRunner != null) {
            String runnerEmail = selectedRunner.getEmail(); // Get the email of the selected runner

            try {
                String subject = "Runner Registration Rejection - Payment Pending";
                String content = "Dear Runner,\n\n" +
                        "We regret to inform you that your registration for the upcoming marathon has been rejected due to non-payment of the registration fee.\n" +
                        "Despite our previous communication and reminders, we have not received the required payment within the specified deadline. As per our registration guidelines, the registration fee is an essential requirement to secure your participation in the marathon.\n" +
                        "We appreciate your interest in participating in our marathon, and we encourage you to consider registering for future events. We hope to have the opportunity to welcome you as a participant in the future.\n\n" +
                        "Thank you for your understanding,\n"+
                        "Best regards,\n" +
                        "Marathon Team";

                Properties properties = new Properties();
                properties.put("mail.smtp.host", "smtp-relay.sendinblue.com");
                properties.put("mail.smtp.port", 587);
                properties.put("mail.smtp.auth", "true");

                // Create a session with the SMTP server
                Session session = Session.getInstance(properties, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("nada_elkamel2020@yahoo.com", "xE3LXZAnSj0gc5tk");
                    }
                });

                // Create a new MimeMessage object
                Message message = new MimeMessage(session);

                // Set the sender address
                message.setFrom(new InternetAddress("nada_elkamel2020@yahoo.com"));

                // Set the recipient address
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(runnerEmail));

                // Set the email subject and content
                message.setSubject(subject);
                message.setText(content);

                // Send the email
                Transport.send(message);

                System.out.println("Email sent successfully!");
            } catch (MessagingException e) {
                System.out.println("Failed to send email: " + e.getMessage());
            }
        } else {
            // No runner selected, show an error message
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setContentText("No runner selected.");
            errorAlert.showAndWait();
        }
    }
    public void aadCoureurSelected(){
        Runner serDate = tableRunner.getSelectionModel().getSelectedItem();
        int num = tableRunner.getSelectionModel().getSelectedIndex();
        if ((num-1)< -1)
        {return;}
        tcodeRunner.setText(String.valueOf(serDate.getCodeRunner()));
        tfirstname.setText(String.valueOf(serDate.getFirstname()));
        tlastname.setText(String.valueOf(serDate.getLastname()));
        tbirthdate.setText(String.valueOf(serDate.getBirthdate()));
        temail.setText(String.valueOf(serDate.getEmail()));
        tphonenumber.setText(String.valueOf(serDate.getPhonenumber()));
    }
}
