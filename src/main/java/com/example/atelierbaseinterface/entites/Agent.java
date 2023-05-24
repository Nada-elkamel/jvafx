package com.example.atelierbaseinterface.entites;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Agent {

    private SimpleIntegerProperty id;
    private SimpleStringProperty username;
    private SimpleStringProperty password ;
    private SimpleStringProperty role;
    private SimpleStringProperty email;

    public Agent(Integer id,String username, String password, String role, String email) {
        this.id = new SimpleIntegerProperty(id);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.role = new SimpleStringProperty(role);
        this.email = new SimpleStringProperty(email);
    }

    public int getId() {
        return id.get();
    }

    public String getUsername() {
        return username.get();
    }


    public String getPassword() {
        return password.get();
    }

    public String getRole() {
        return role.get();
    }


    public String getEmail() {
        return email.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }
}
