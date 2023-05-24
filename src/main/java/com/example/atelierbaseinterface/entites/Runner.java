package com.example.atelierbaseinterface.entites;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Runner{

    private SimpleIntegerProperty codeRunner ;
    private SimpleStringProperty firstname;
    private SimpleStringProperty lastname;
    private SimpleIntegerProperty phonenumber;
    private SimpleStringProperty birthdate;
    private SimpleStringProperty email;

    private SimpleIntegerProperty marathonId;



    public Runner(Integer codeRunner,String firstname, String lastname, Integer phonenumber, String birthdate, String email, Integer marathonId) {
        this.codeRunner = new SimpleIntegerProperty(codeRunner);
        this.firstname = new SimpleStringProperty(firstname);
        this.lastname = new SimpleStringProperty(lastname);
        this.phonenumber = new SimpleIntegerProperty(phonenumber);
        this.birthdate = new SimpleStringProperty(birthdate);
        this.email = new SimpleStringProperty(email);
        this.marathonId = new SimpleIntegerProperty(marathonId);
    }



    public String getFirstname() {
        return firstname.get();
    }

    public String getLastname() {
        return lastname.get();
    }

    public int getPhonenumber() {
        return phonenumber.get();
    }

    public String getBirthdate() {
        return birthdate.get();
    }

    public String getEmail() {
        return email.get();
    }


    public int getCodeRunner() {
        return codeRunner.get();
    }
    public void setFirstname(String firstname) {
        this.firstname.set(firstname);
    }

    public void setLastname(String lastname) {
        this.lastname.set(lastname);
    }

    public void setPhonenumber(int phonenumber) {
        this.phonenumber.set(phonenumber);
    }

    public void setBirthdate(String birthdate) {
        this.birthdate.set(birthdate);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }


    @Override
    public String toString() {
        return "Runner{" +
                "firstname=" + firstname +
                ", lastname=" + lastname +
                ", phonenumber=" + phonenumber +
                ", birthdate=" + birthdate +
                ", email=" + email +
                '}';
    }



    public void setCodeRunner(Integer codeRunner) {
        this.codeRunner.set(codeRunner);
    }

    public int getMarathonId() {
        return marathonId.get();
    }

    public void setMarathonId(int marathonId) {
        this.marathonId.set(marathonId);
    }
}
