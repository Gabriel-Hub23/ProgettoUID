package com.pptattoo.pptattoo.Model;

import com.pptattoo.pptattoo.SceneHandler;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

public class GeneraScontrino {

    private static final GeneraScontrino instance = new GeneraScontrino();

    public static GeneraScontrino getInstance() {
        return instance;
    }
    private GeneraScontrino(){}

    public void generaScontrino(String id) throws FileNotFoundException, DocumentException, SQLException {
        Document doc = new Document();
        PdfWriter writer = null;
        boolean trovato = true;
        try{
            writer = PdfWriter.getInstance(doc, new FileOutputStream(SceneHandler.getInstance().apriDirectoryChooser()+"/Scontrino"+id+".pdf"));
        }catch(FileNotFoundException e){ SceneHandler.getInstance().generaAlert("Non hai inserito nessuna cartella",false); trovato = false;}
        if(trovato){
            doc.open();
            doc.addTitle("Scontrino "+id);
            ArrayList<Appuntamento> app = GestoreAppuntamenti.getInstance().listaAppuntamenti(true,"A.Id", id);
            Appuntamento a = app.get(0);
            String data = GestoreData.getInstance().generaDataOdierna() +" "+ String.valueOf(java.time.LocalDateTime.now()).substring(11, 19);
            String parag = "Id appuntamento: "+a.getId()+"\n"+"Email: "+a.getEmail()+"\n"+"Nome e cognome: "+a.getIdentificativo()+"\n"+"Numero: "+a.getNumero()+"\n"+"Data appuntamento: "+a.getData()+"\nData generazione scontrino: "+data+"\n"+"Dipendente: "+a.getDipendente()+"\n"+"Servizio: "+a.getServizio()+"\n"+"Prezzo: "+a.getPrezzo()+"\n"+"Grazie per averci scelto\n";
            doc.add(new Paragraph(parag));
            doc.close();
            writer.close();
        }

    }

}
