package com.pptattoo.pptattoo.Model;

public class Statistiche {
    private String data;
    private Integer numeroApp;

    public Statistiche(String data, Integer numero){
        this.data = data;
        this.numeroApp = numero;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getNumeroApp() {
        return numeroApp;
    }

    public void setNumeroApp(Integer numeroApp) {
        this.numeroApp = numeroApp;
    }
}
