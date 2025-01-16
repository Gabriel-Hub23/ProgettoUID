package com.pptattoo.pptattoo.Model;

import java.sql.SQLException;
import java.util.ArrayList;

public class GestoreStatistiche {

    private static final GestoreStatistiche instance = new GestoreStatistiche();

    public static GestoreStatistiche getInstance() {
        return instance;
    }
    private GestoreStatistiche(){}
    private enum tipo {Giornaliero,Settimanale, Mensile, Annuale}

    public tipo getSettimanale(){
        return tipo.Settimanale;
    }
    public tipo getMensile(){
        return tipo.Mensile;
    }
    public tipo getAnnuale(){
        return tipo.Annuale;
    }
    public tipo getGiornaliero(){
        return tipo.Giornaliero;
    }

    public ArrayList<Statistiche> statistiche( tipo p ,int mese, int anno, String data) throws SQLException{
        ArrayList<Statistiche> info = new ArrayList<Statistiche>();
        switch (p){
            case Giornaliero -> {
                String [] parametri ={data,"0"};
                int query = (int) GestoreDbThreaded.getInstance().runQuery(8, null, parametri);
                info.add(new Statistiche(data, query));
            }
            case Settimanale -> {
                for(int i = 6; i>-1; i--){
                    String s = GestoreData.getInstance().generaDataSottratta(i);
                    String [] parametri ={s+"%","0"};
                    int query = (int) GestoreDbThreaded.getInstance().runQuery(8, null, parametri);
                    info.add(new Statistiche(s, query));
                }
            }
            case Mensile -> {
                int n;
                switch(mese){
                    case 1,3,5,7,8,10,12 -> n = 31;
                    case 4,6,9,11 -> n = 30;
                    case 2 -> n = 28;
                    default -> n=0;
                }
                String m = GestoreData.getInstance().generaMese(mese);
                for(int i = 1; i<=n; i++){
                    String dataGenerata = GestoreData.getInstance().generaDataMese(i, m, anno);
                    String [] parametri ={dataGenerata+"%","0"};
                    int query = (int) GestoreDbThreaded.getInstance().runQuery(8, null, parametri);
                    info.add(new Statistiche(dataGenerata.substring(0,dataGenerata.length()-5), query));
                }
            }
            case Annuale -> {
                for(int i = 1; i<=12; i++){
                    String dataGenerata = GestoreData.getInstance().generaDataAnno(i, anno);
                    String [] parametri ={dataGenerata+"%","0"};
                    int query = (int) GestoreDbThreaded.getInstance().runQuery(8, null, parametri);
                    info.add(new Statistiche(dataGenerata.substring(2), query));
                }
            }
        }
        return info;
    }

}