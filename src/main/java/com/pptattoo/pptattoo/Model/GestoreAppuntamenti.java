package com.pptattoo.pptattoo.Model;

import java.sql.SQLException;
import java.util.ArrayList;

public class GestoreAppuntamenti {

    private static final GestoreAppuntamenti instance = new GestoreAppuntamenti();

    public static GestoreAppuntamenti getInstance() {
        return instance;
    }

    private GestoreAppuntamenti(){}

    public ArrayList<Appuntamento> listaAppuntamenti(boolean cerca, String filtro, String valore) throws SQLException {
        ArrayList<Appuntamento> lista = new ArrayList<>();

        // Esegui la query
        ArrayList<String> query = (ArrayList<String>) GestoreDbThreaded.getInstance().runQuery(11, null, new String[]{String.valueOf(cerca), filtro, valore});

        // Elabora i risultati
        for (String i : query) {
            Appuntamento a = new Appuntamento(i.split(";"));
            lista.add(a);
        }

        return lista;

    }
}