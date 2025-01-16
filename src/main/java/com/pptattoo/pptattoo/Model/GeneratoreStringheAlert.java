package com.pptattoo.pptattoo.Model;

import java.sql.SQLException;
import java.util.ArrayList;

public class GeneratoreStringheAlert {

    private static final GeneratoreStringheAlert instance = new GeneratoreStringheAlert();

    public static GeneratoreStringheAlert getInstance() {
        return instance;
    }
    private GeneratoreStringheAlert(){}

    public String generaNotificaTesto(String data) throws SQLException {
        String [] parametri ={String.valueOf(true),"Data", data};
        ArrayList <String> app = (ArrayList<String>) GestoreDbThreaded.getInstance().runQuery(11, null, parametri);
        StringBuilder s = new StringBuilder();
        s.append("Nome ").append(" Cognome ").append(" Numero        ").append("Servizio\n");
        for(String i : app){
            String [] info = i.split(";");
            s.append(info[3]).append(" ").append(info[4]).append("   ").append(info[5]).append(" ").append(info[8]).append("\n");
        }
        return s.toString();
    }
}
