package com.pptattoo.pptattoo.Controller;

import com.pptattoo.pptattoo.Model.*;
import com.pptattoo.pptattoo.SceneHandler;
import com.pptattoo.pptattoo.View.CardContainer;
import com.pptattoo.pptattoo.View.Dialog;
import com.pptattoo.pptattoo.View.MyInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class ClientiController {
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button addButton;

    @FXML
    private Button cercaButton;

    @FXML
    private TextField cercaField;

    @FXML
    private ComboBox<String> filtro;

    @FXML
    private Label labelClienti;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private HBox searchHbox;

    @FXML
    private VBox vboxEsterno;

    private void generaCard(boolean cerca, String filtro, String valore){
        vboxEsterno.getChildren().clear();
        LinkedList<Cliente> res = null;

        if(!cerca){
             res = ReusableDBResultsConverter.getInstance().getClienti((ArrayList<String>) GestoreDbThreaded.getInstance().runQuery(1, GestoreDB.entità.Clienti, null));

        }else{
            ArrayList<String> clienti = new ArrayList<String>();
            String [] parametri = {filtro , valore};
            clienti.add( (String) GestoreDbThreaded.getInstance().runQuery(6, GestoreDB.entità.Clienti, parametri));
            res = ReusableDBResultsConverter.getInstance().getClienti(clienti);
        }
        CardContainer.getInstance().setCardContainer(res, vboxEsterno);
    }

    @FXML
    void actionAggiungiCliente(ActionEvent event) {
        Dialog.getInstance().requestDialog(Dialog.from.CLIENTI, Dialog.actions.AGGIUNGI, "-1", anchorPane);
    }

    @FXML
    void actionCerca(ActionEvent event) {
        if(filtro.getValue() == null){
            SceneHandler.getInstance().generaAlert("Non hai selezionato il filtro.", false);
        }else if(cercaField.getText().equals("")){
            SceneHandler.getInstance().generaAlert("Non hai inserito il valore da cercare.", false);
        }else{
            generaCard(true, filtro.getValue(), cercaField.getText());
        }
    }

    private void impostaTemi() throws IOException {
        if(MyInfo.getInstance().getFont().equals("Dyslexie")){
            labelClienti.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(),MyInfo.getInstance().getSizeLabel()));
            cercaField.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(),cercaField.getFont().getSize()-1));
            cercaButton.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(), cercaButton.getFont().getSize()-1));
            addButton.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(), addButton.getFont().getSize()-1));
        }else{
            labelClienti.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(),MyInfo.getInstance().getSizeLabel()));
            cercaField.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), cercaField.getFont().getSize()));
            cercaButton.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), cercaButton.getFont().getSize()));
            addButton.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), addButton.getFont().getSize()));
        }
    }

    @FXML
    void initialize() throws IOException {
        vboxEsterno.setSpacing(15);
        impostaTemi();
        filtro.getItems().addAll("CF", "Nome", "Cognome", "Email", "Numero");
        generaCard(false, "", "");
    }

}
