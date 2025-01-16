package com.pptattoo.pptattoo.Model;

public class Cliente {

    private String CF;
    private String email;
    private String nome;
    private String cognome;
    private String numero;


    public Cliente(String CF, String nome, String cognome, String numero, String email) {
        this.CF = CF;
        this.nome = nome;
        this.cognome = cognome;
        this.numero = numero;
        this.email = email;
    }


    public String getCF() { return CF; }
    public String getEmail() { return email; }
    public String getNome() { return nome; }
    public String getCognome() { return cognome; }
    public String getNumero() { return numero; }
}
