package com.pptattoo.pptattoo.Model;

import javafx.beans.property.*;

public class Appuntamento {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty email;
    private final SimpleStringProperty identificativo;
    private final SimpleStringProperty numero;
    private final SimpleStringProperty data;

    private String cf;
    private final SimpleStringProperty dipendente;
    private final SimpleStringProperty servizio;
    private final SimpleDoubleProperty prezzo;
    public Appuntamento(Integer id, String cf, String email, String nome, String cognome, String numero, String data, String dipendente, String servizio, Double prezzo){
        this.id = new SimpleIntegerProperty(id);
        this.cf = cf;
        this.email = new SimpleStringProperty(email);
        this.identificativo = new SimpleStringProperty(nome + " " + cognome);
        this.numero = new SimpleStringProperty(numero);
        this.data = new SimpleStringProperty(data);
        this.dipendente = new SimpleStringProperty(dipendente);
        this.servizio = new SimpleStringProperty(servizio);
        this.prezzo = new SimpleDoubleProperty(prezzo);
    }

    public Appuntamento(String [] s){
        this.id = new SimpleIntegerProperty(Integer.parseInt(s[0]));
        this.cf = s[1];
        this.email = new SimpleStringProperty(s[2]);
        this.identificativo = new SimpleStringProperty(s[3]+" "+s[4]);
        this.numero = new SimpleStringProperty(s[5]);
        this.data = new SimpleStringProperty(s[6]);
        this.dipendente = new SimpleStringProperty(s[7]);
        this.servizio = new SimpleStringProperty(s[8]);
        this.prezzo = new SimpleDoubleProperty(Double.parseDouble(s[9]));
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getIdentificativo() {
        return identificativo.get();
    }

    public void setIdentificativo(String identificativo) {
        this.identificativo.set(identificativo);
    }

    public String getCf() {return cf;}
    public void setCf(String cf) {this.cf = cf;}

    public String getNumero() {
        return numero.get();
    }

    public void setNumero(String numero) {
        this.numero.set(numero);
    }

    public String getData() {
        return data.get();
    }

    public void setData(String data) {
        this.data.set(data);
    }

    public String getDipendente() {
        return dipendente.get();
    }

    public void setDipendente(String dipendente) {
        this.dipendente.set(dipendente);
    }

    public String getServizio() {
        return servizio.get();
    }

    public void setServizio(String servizio) {
        this.servizio.set(servizio);
    }

    public double getPrezzo() {
        return prezzo.get();
    }

    public void setPrezzo(double prezzo) {
        this.prezzo.set(prezzo);
    }

}            