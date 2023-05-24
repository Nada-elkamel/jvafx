module com.example.atelierbaseinterface {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.mail;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;

    opens com.example.atelierbaseinterface to javafx.fxml;
    exports com.example.atelierbaseinterface;
    exports com.example.atelierbaseinterface.Connexion;
    opens com.example.atelierbaseinterface.Connexion to javafx.fxml;
    opens com.example.atelierbaseinterface.entites to javafx.base;


}