package com.pptattoo.pptattoo.Model;

import com.pptattoo.pptattoo.SceneHandler;
import com.pptattoo.pptattoo.Settings;

import java.util.ArrayList;
import java.util.LinkedList;

public class ReusableDBResultsConverter {

    private static ReusableDBResultsConverter instance = new ReusableDBResultsConverter();
    public static ReusableDBResultsConverter getInstance() {
        return instance;
    }
    private ReusableDBResultsConverter() { }



    public LinkedList<Dipendente> getDipendenti(ArrayList<String> dbResults) {
        return convertiFormatoGestoreDBToListDipendenti(dbResults);
    }

    public LinkedList<Servizio> getServizi(ArrayList<String> dbResults) {
        return convertiFormatoGestoreDBToListServizi(dbResults);
    }

    public LinkedList<Cliente> getClienti(ArrayList<String> dbResults) {
        return convertiFormatoGestoreDBToListClienti(dbResults);
    }

    private LinkedList<Servizio> convertiFormatoGestoreDBToListServizi(ArrayList<String> dbResults) {
        LinkedList<Servizio> results = new LinkedList<>();
        for (String i : dbResults) {
            String[] rowValues = i.split(";");

            if (rowValues.length < Settings.CELL_DB_SERVIZI) {
                continue;
            }

            notAvailable(rowValues);

            Servizio servizio = new Servizio(rowValues[0], rowValues[1], rowValues[2]);

            results.add(servizio);
        }

        return results;
    }

    private LinkedList<Dipendente> convertiFormatoGestoreDBToListDipendenti(ArrayList<String> dbResults) {
        LinkedList<Dipendente> results = new LinkedList<>();
        for (String i : dbResults) {
            String[] rowValues = i.split(";");

            if (rowValues.length < Settings.CELL_DB_DIPENDENTI) {
                System.out.println("celle dipendenti < celle attese");
                continue;
            }

            notAvailable(rowValues);

            Dipendente dipendente = new Dipendente(rowValues[0],
                    rowValues[1],
                    rowValues[2],
                    rowValues[3],
                    rowValues[4]);

            results.add(dipendente);
        }

        return results;
    }

    private void notAvailable(String[] rowValues) {
        for(int i = 0; i < rowValues.length; i++)
            if (rowValues[i].equals("null") || rowValues[i].isEmpty())
                rowValues[i] = "n/a";
    }


    public LinkedList<Cliente> convertiFormatoGestoreDBToListClienti(ArrayList<String> dbResults) {
        LinkedList<Cliente> results = new LinkedList<>();
        for (String i : dbResults) {
            String[] rowValues = i.split(";");


            if (rowValues.length < Settings.CELL_DB_CLIENTI) {
                System.out.println("celle clienti < celle attese");
                continue;
            }

            notAvailable(rowValues);

            //0   1    2     3       4
            //cf email nome cognome numero
            Cliente cliente = new Cliente(rowValues[0],
                    rowValues[2],
                    rowValues[3],
                    rowValues[4],
                    rowValues[1]);

            results.add(cliente);
        }

        return results;
    }

    public LinkedList<Appuntamento> getAppuntamenti(ArrayList<String> dbResults) {
        LinkedList<Appuntamento> results = new LinkedList<>();
        for (String i : dbResults) {
            String[] rowValues = i.split(";");


            /*if (rowValues.length < Settings.Cell_DB_APPUNTAMENTI) {
                System.out.println("celle clienti < celle attese");
                continue;
            }
             */

            notAvailable(rowValues);


            try {
                // 0   1    2   3    4
                // id data cf iddip idser
                Appuntamento app = new Appuntamento(
                        Integer.parseInt(rowValues[0]),
                        rowValues[2],
                        "",
                        "",
                        "",
                        "",
                        rowValues[1], rowValues[3], rowValues[4], 0.0);
                results.add(app);
            }
            catch (Exception e) {
                SceneHandler.getInstance().generaAlert("ID non esistente o rimosso in precedenza", false);
            }


        }

        return results;
    }
}
