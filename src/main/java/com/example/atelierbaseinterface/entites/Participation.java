package com.example.atelierbaseinterface.entites;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Participation {
    private SimpleIntegerProperty codeMarathon;
    private SimpleIntegerProperty codeRunner;
    private SimpleFloatProperty temps;


    public Participation(Integer codeMarathon, Integer codeRunner, float temps) {
        this.codeMarathon = new SimpleIntegerProperty(codeMarathon);
        this.codeRunner = new SimpleIntegerProperty(codeRunner);
        this.temps = new SimpleFloatProperty(temps);
    }

    public SimpleIntegerProperty codeMarathonProperty() {
        return codeMarathon;
    }

    public SimpleIntegerProperty codeRunnerProperty() {
        return codeRunner;
    }

    public SimpleFloatProperty tempsProperty() {
        return temps;
    }

    public int getCodeMarathon() {
        return codeMarathon.get();
    }

    public int getCodeRunner() {
        return codeRunner.get();
    }

    public float getTemps() {
        return temps.get();
    }

    public void setCodeMarathon(Marathon codeMarathon) {
        this.codeMarathon.set(codeMarathon.getCodeMarathon());
    }

    public void setCodeRunner(Runner codeRunner) {
        this.codeRunner.set(codeRunner.getCodeRunner());
    }

    public void setTemps(float temps) {
        this.temps.set(temps);
    }
}
