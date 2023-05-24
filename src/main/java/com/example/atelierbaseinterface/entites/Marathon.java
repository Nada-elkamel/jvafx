package com.example.atelierbaseinterface.entites;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Marathon {

        private SimpleIntegerProperty codeMarathon;
        private SimpleStringProperty nom;
        private SimpleStringProperty date;
        private SimpleStringProperty lieuDepart;
        private SimpleStringProperty lieuArrivee;
        private SimpleFloatProperty distance;

    public Marathon(Integer codeMarathon ,String nom, String date, String lieuDepart, String lieuArrivee, float distance) {
        this.codeMarathon = new SimpleIntegerProperty(codeMarathon);
        this.nom = new SimpleStringProperty(nom);
        this.date = new SimpleStringProperty(date);
        this.lieuDepart = new SimpleStringProperty(lieuDepart);
        this.lieuArrivee = new SimpleStringProperty(lieuArrivee);
        this.distance = new SimpleFloatProperty(distance);

    }

    public String getNom() {
        return nom.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getLieuDepart() {
        return lieuDepart.get();
    }

    public String getLieuArrivee() {
        return lieuArrivee.get();
    }

    public float getDistance() {
        return distance.get();
    }


    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public void setLieuDepart(String lieuDepart) {
        this.lieuDepart.set(lieuDepart);
    }

    public void setLieuArrivee(String lieuArrivee) {
        this.lieuArrivee.set(lieuArrivee);
    }

    public void setDistance(float distance) {
        this.distance.set(distance);
    }

    public Integer getCodeMarathon() {
        return codeMarathon.get();
    }

    public void setCodeMarathon(Integer codeMarathon) {
        this.codeMarathon.set(codeMarathon);
    }
}
