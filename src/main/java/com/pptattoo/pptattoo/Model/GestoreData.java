package com.pptattoo.pptattoo.Model;

public class GestoreData {

    private static final GestoreData instance = new GestoreData();

    public static GestoreData getInstance() {
        return instance;
    }
    private GestoreData(){}

    public StringBuilder annoCorrente(String data){
        StringBuilder anno = new StringBuilder(data.substring(0, 4));
        return anno;
    }

    public StringBuilder meseCorrente(String data){
        StringBuilder mese = new StringBuilder(data.substring(5, 7));
        return mese;
    }

    public StringBuilder giornoCorrente(String data){
        StringBuilder giorno = new StringBuilder(data.substring(8, 10));
        return giorno;
    }

    public String generaDataAnno(int mese, int anno){
        String data;
        if (mese<10) {
            data = "%/0"+ String.valueOf(mese) +"/"+ String.valueOf(anno) ;
        }else{
            data = "%/"+ String.valueOf(mese) +"/"+ String.valueOf(anno) ;
        }
        return data;
    }

    public String generaMese(int mese){
        String m;
        if(mese<10){
            m = "0"+String.valueOf(mese);
        }else{
            m = String.valueOf(mese);
        }
        return m;
    }

    public String generaDataMese(int i, String m, int anno){
        String data;
        if (i<10) {
            data = "0"+String.valueOf(i)+"/"+ m +"/"+ String.valueOf(anno) ;
        }else{
            data = String.valueOf(i)+"/"+ m +"/"+ String.valueOf(anno) ;
        }
        return data;
    }

    public String generaDataOdierna(){
        String data = String.valueOf(java.time.LocalDateTime.now());
        StringBuilder anno = GestoreData.getInstance().annoCorrente(data);
        StringBuilder mese = GestoreData.getInstance().meseCorrente(data);
        StringBuilder giorno = GestoreData.getInstance().giornoCorrente(data);
        data = giorno.toString()+"/"+mese.toString()+"/"+anno.toString();
        return data;
    }

    public String generaDataSottratta(int x){
        String data = String.valueOf(java.time.LocalDateTime.now().minusDays(x));
        StringBuilder giorno = GestoreData.getInstance().giornoCorrente(data);
        StringBuilder anno = GestoreData.getInstance().annoCorrente(data);
        StringBuilder mese = GestoreData.getInstance().meseCorrente(data);
        return giorno.toString()+"/"+mese.toString()+"/"+anno.toString();
    }

}