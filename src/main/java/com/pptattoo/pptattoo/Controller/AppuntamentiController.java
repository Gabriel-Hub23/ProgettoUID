package com.pptattoo.pptattoo.Controller;

import com.pptattoo.pptattoo.Model.Appuntamento;
import com.pptattoo.pptattoo.Model.GestoreAppuntamenti;
import com.pptattoo.pptattoo.Model.GestoreDB;
import com.pptattoo.pptattoo.Model.GestoreDbThreaded;
import com.pptattoo.pptattoo.SceneHandler;
import com.pptattoo.pptattoo.View.Dialog;
import com.pptattoo.pptattoo.View.MyInfo;
import com.itextpdf.text.DocumentException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class AppuntamentiController {

    @FXML
    private AnchorPane ancorPane;
    @FXML
    private HBox scontrino_hbox, search_hbox;

    @FXML
    private Label labelAppuntamenti;

    @FXML
    private TextField cercaField, idField, idScontrino;

    @FXML
    private Button removeButton, addButton, editButton, cercaButton, generaButton;

    @FXML
    private ComboBox<String> filtroBox;

    @FXML
    private TableView<Appuntamento> table;

    @FXML
    private TableColumn<Appuntamento, Integer> colonnaId;
    @FXML
    private TableColumn<Appuntamento, String> colonnaEmail;
    @FXML
    private TableColumn<Appuntamento, String> colonnaNomeCognome;
    @FXML
    private TableColumn<Appuntamento, String> colonnaNumero;
    @FXML
    private TableColumn<Appuntamento, Date> colonnaData;
    @FXML
    private TableColumn<Appuntamento, String> colonnaDipendente;
    @FXML
    private TableColumn<Appuntamento, String> colonnaServizio;
    @FXML
    private TableColumn<Appuntamento, Double> colonnaPrezzo;


    @FXML
    public void initialize() throws IOException {
        impostaTemi();
        aggiungiItems();
        popolaTabella();
    }


    private void setCellValue() {
        colonnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colonnaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colonnaData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colonnaPrezzo.setCellValueFactory(new PropertyValueFactory<>("prezzo"));
        colonnaDipendente.setCellValueFactory(new PropertyValueFactory<>("dipendente"));
        colonnaNomeCognome.setCellValueFactory(new PropertyValueFactory<>("identificativo"));
        colonnaNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colonnaServizio.setCellValueFactory(new PropertyValueFactory<>("servizio"));
    }


    private void popolaTabella() {
        table.getItems().clear();
        try {
            ArrayList<Appuntamento> lista = GestoreAppuntamenti.getInstance().listaAppuntamenti(false, "", "");
            ObservableList<Appuntamento> observableApp = FXCollections.observableArrayList(lista);
            table.setItems(observableApp);
            setCellValue();
        } catch (SQLException e) {
            SceneHandler.getInstance().generaAlert("Qualcosa è andato storto!", false);
        }
    }


    private void aggiungiItems() {
        filtroBox.getItems().addAll(
                "Id",
                "Email",
                "Nome",
                "Cognome",
                "Numero",
                "Data",
                "Dipendente",
                "Servizio",
                "Prezzo"
        );
    }


    private void impostaTemi() throws IOException {
        if (MyInfo.getInstance().getFont().equals("Dyslexie")) {
            labelAppuntamenti.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(), MyInfo.getInstance().getSizeLabel()));
            cercaField.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(), cercaField.getFont().getSize() - 1));
            idField.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(), idField.getFont().getSize() - 1));
            idScontrino.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(), idScontrino.getFont().getSize() - 1));
            cercaButton.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(), cercaButton.getFont().getSize() - 1));
            generaButton.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(), generaButton.getFont().getSize() - 1));
            addButton.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(), addButton.getFont().getSize() - 1));
            removeButton.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(), removeButton.getFont().getSize() - 1));
            editButton.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(), editButton.getFont().getSize() - 1));
        } else {
            labelAppuntamenti.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), MyInfo.getInstance().getSizeLabel()));

        }
    }


    @FXML
    void aggiungiAppuntamento(ActionEvent event) throws IOException {
        Dialog.getInstance().requestDialog(Dialog.from.APPUNTAMENTI, Dialog.actions.AGGIUNGI, "-1", ancorPane);
    }


    @FXML
    void modificaAppuntamento(ActionEvent event) throws IOException {
        if (!idField.getText().isEmpty()) {
            String[] parametri = {"Id", idField.getText()};
            if (!GestoreDbThreaded.getInstance().runQuery(5, GestoreDB.getInstance().getAppuntamenti(), parametri)
                    .toString().equals(idField.getText())) {
                SceneHandler.getInstance().generaAlert("Id inserito non trovato.", false);
            } else {
                Dialog.getInstance().requestDialog(Dialog.from.APPUNTAMENTI, Dialog.actions.MODIFICA, idField.getText(), ancorPane);
            }
        } else {
            SceneHandler.getInstance().generaAlert("Non hai inserito l'id per modificare", false);
        }
    }


    @FXML
    void rimuoviAppuntamento(ActionEvent event) throws IOException {
        if (!idField.getText().isEmpty()) {
            String[] parametri = {"Id", idField.getText()};
            if (!GestoreDbThreaded.getInstance().runQuery(5, GestoreDB.getInstance().getAppuntamenti(), parametri)
                    .toString().equals(idField.getText())) {
                SceneHandler.getInstance().generaAlert("Id inserito non trovato.", false);
            } else {
                String[] par = {idField.getText()};
                GestoreDbThreaded.getInstance().runQuery(4, GestoreDB.entità.Appuntamenti, par);
            }
        } else {
            SceneHandler.getInstance().generaAlert("Non hai inserito l'id per modificare", false);
        }

        popolaTabella();
    }

    @FXML
    void generaScontrino(ActionEvent event) throws DocumentException, FileNotFoundException, SQLException {
        if (idScontrino.getText().isEmpty()) {
            SceneHandler.getInstance().generaAlert("Non hai inserito l'id da cercare.", false);
        } else {
            String[] parametri = {"Id", idScontrino.getText()};
            if (!GestoreDbThreaded.getInstance().runQuery(5, GestoreDB.getInstance().getAppuntamenti(), parametri)
                    .toString().equals(idScontrino.getText())) {
                SceneHandler.getInstance().generaAlert("L'id inserito non esiste.", false);
            } else {

            }
        }
        idScontrino.clear();
        idScontrino.setPromptText("Id");
    }


    @FXML
    void cerca(ActionEvent event) throws SQLException {
        if (filtroBox.getValue() == null) {
            SceneHandler.getInstance().generaAlert("Non hai selezionato il filtro.", false);
        } else if (cercaField.getText().isEmpty()) {
            SceneHandler.getInstance().generaAlert("Non hai inserito il valore da cercare.", false);
        } else {
            table.getItems().clear();
            String filtro = switch (filtroBox.getValue()) {
                case "Id" -> "A.Id";
                case "Email" -> "C.Email";
                case "Nome" -> "C.Nome";
                case "Cognome" -> "C.Cognome";
                case "Numero" -> "C.Numero";
                case "Data" -> "A.Data";
                case "Dipendente" -> "D.Username";
                case "Servizio" -> "S.Tipo";
                case "Prezzo" -> "S.Prezzo";
                default -> "";
            };

            ObservableList<Appuntamento> app = FXCollections.observableArrayList(
                    GestoreAppuntamenti.getInstance().listaAppuntamenti(true, filtro, cercaField.getText())
            );
            table.setItems(app);
            setCellValue();
        }
        cercaField.clear();
        cercaField.setPromptText("Cerca");
    }
}
