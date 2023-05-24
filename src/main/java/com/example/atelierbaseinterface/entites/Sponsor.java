package com.example.atelierbaseinterface.entites;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Sponsor {

    private SimpleIntegerProperty codeSponsor;
    private SimpleStringProperty name;
    private SimpleStringProperty company;
    private SimpleIntegerProperty phonenumberS;
    private SimpleStringProperty emailS;
    private SimpleFloatProperty amount;

    public Sponsor(Integer codeSponsor,String name, String company, Integer phonenumberS, String emailS, Float amount) {
        this.codeSponsor = new SimpleIntegerProperty(codeSponsor);
        this.name = new SimpleStringProperty(name);
        this.company = new SimpleStringProperty(company);
        this.phonenumberS = new SimpleIntegerProperty(phonenumberS);
        this.emailS = new SimpleStringProperty(emailS);
        this.amount = new SimpleFloatProperty(amount);
    }

    public Integer getCodeSponsor() {
        return codeSponsor.get();
    }

    public String getName() {
        return name.get();
    }

    public String getCompany() {
        return company.get();
    }

    public int getPhonenumberS() {
        return phonenumberS.get();
    }

    public String getEmailS() {
        return emailS.get();
    }

    public float getAmount() {
        return amount.get();
    }

    public void setCodeSponsor(Integer codeSponsor) {
        this.codeSponsor.set(codeSponsor);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setCompany(String company) {
        this.company.set(company);
    }

    public void setPhonenumberS(int phonenumberS) {
        this.phonenumberS.set(phonenumberS);
    }

    public void setEmailS(String emailS) {
        this.emailS.set(emailS);
    }

    public void setAmount(float amount) {
        this.amount.set(amount);
    }
}
