package com.example.atelierbaseinterface.Connexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion implements AutoCloseable {
    private Connection connection;

    public Connection connecter() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/marathontry", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
