package com.pptattoo.pptattoo.Model;

import java.sql.SQLException;
import java.util.concurrent.*;

public class GestoreDbThreaded {

    private static final GestoreDbThreaded instance = new GestoreDbThreaded();

    public static GestoreDbThreaded getInstance() {
        return instance;
    }
    private GestoreDbThreaded(){executor = Executors.newSingleThreadExecutor();}

    private final ExecutorService executor;

    public Object runQuery(int query, GestoreDB.entità entità, String [] parametri){
        Future<Object> future = executor.submit(new QueryRun(query, entità, parametri));
        Object obj = null;
        try{
            obj = future.get();
        }catch (ExecutionException | InterruptedException e){}
        return obj;
    }


    private static class QueryRun implements Callable<Object>{

        private final int query;
        private final GestoreDB.entità entità;
        private final String[] parametri;
        public QueryRun(int query, GestoreDB.entità entità, String [] parametri){
            this.query = query;
            this.entità = entità;
            this.parametri = parametri;
        }
        @Override
        public Object call() throws Exception {
            Object obj = null;
            switch (query){
                case 1 -> {
                    try {
                        obj = GestoreDB.getInstance().leggiEntità(entità);
                    } catch (SQLException e) {}
                }
                case 2 -> {
                    try {
                        GestoreDB.getInstance().inserimento(entità, parametri);
                    } catch (SQLException e) {}
                }
                case 3 -> {
                    try {
                        GestoreDB.getInstance().aggiornamento(entità, parametri);
                    } catch (SQLException e) {}
                }
                case 4 -> {
                    try {
                        GestoreDB.getInstance().rimozione(entità, parametri[0]);
                    } catch (SQLException e) {}
                }
                case 5 -> {
                    try {
                        obj = GestoreDB.getInstance().selezionaValore(entità, parametri[0], parametri[1]);
                    } catch (SQLException e) {}
                }
                case 6 -> {
                    try {
                        obj = GestoreDB.getInstance().cercaTramiteValore(entità, parametri[0], parametri[1]);
                    } catch (SQLException e) {}
                }
                case 7 -> {
                    try {
                        obj = GestoreDB.getInstance().cercaRiga(entità, parametri[0]);
                    } catch (SQLException e) {}
                }
                case 8 -> {
                    try {
                        obj = GestoreDB.getInstance().conta(parametri[0], Integer.parseInt(parametri[1]));
                    } catch (SQLException e) {}
                }
                case 9 -> {
                    try {
                        GestoreDB.getInstance().cambiaPassword(parametri[0], parametri[1]);
                    } catch (SQLException e) {}
                }
                case 10 -> {
                    try {
                        obj = GestoreDB.getInstance().calcolaGuadagno(parametri[0]);
                    } catch (SQLException e) {}
                }
                case 11 -> {
                    try {
                        obj = GestoreDB.getInstance().creaLista(Boolean.parseBoolean(parametri[0]), parametri[1], parametri[2]);
                    } catch (SQLException e) {}
                }
                case 12 -> {
                    try {
                        obj = GestoreDB.getInstance().login(parametri[0], parametri[1]);
                    } catch (SQLException e) {}
                }
                case 13 -> {
                    try {
                        GestoreDB.getInstance().svuota();
                    } catch (SQLException e) {}
                }
            }
            return obj;
        }
    }
}
