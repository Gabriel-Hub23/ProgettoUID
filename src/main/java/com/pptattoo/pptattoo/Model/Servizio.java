package com.pptattoo.pptattoo.Model;

public class Servizio {

    private String id;
    private String tipo;
    private String prezzo;


    public Servizio(String id, String tipo, String prezzo) {
        this.id = id;
        this.tipo = tipo;
        this.prezzo = prezzo;
    }

    public String getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public String getPrezzo() {
        return prezzo;
    }
}
