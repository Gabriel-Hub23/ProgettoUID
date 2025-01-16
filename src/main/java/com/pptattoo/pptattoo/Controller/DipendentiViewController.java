package com.pptattoo.pptattoo.Controller;

import com.pptattoo.pptattoo.Model.*;
import com.pptattoo.pptattoo.SceneHandler;
import com.pptattoo.pptattoo.View.CardContainer;
import com.pptattoo.pptattoo.View.Dialog;
import com.pptattoo.pptattoo.View.MyInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class DipendentiViewController {
    @FXML
    private Button addButton;

    @FXML
    private AnchorPane ancorPane;

    @FXML
    private TextField cercaField;

    @FXML
    private ComboBox<String> filtroBox;

    @FXML
    private Label labelDipendenti;

    @FXML
    private Button searchButton;

    @FXML
    private HBox search_hbox;

    @FXML
    private VBox vboxEsterno;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    void actionAddButton(ActionEvent event) {
        Dialog.getInstance().requestDialog(Dialog.from.DIPENDENTI, Dialog.actions.AGGIUNGI, "-1", ancorPane);
    }

    @FXML
    void actionFiltroBox(ActionEvent event) {

    }

    private void generaCard(boolean cerca, String filtro, String valore){
        vboxEsterno.getChildren().clear();
        LinkedList<Dipendente> res = null;

        if(!cerca){
            res = ReusableDBResultsConverter.getInstance().getDipendenti((ArrayList<String>) GestoreDbThreaded.getInstance().runQuery(1, GestoreDB.entità.Dipendenti, null));

        }else{
            ArrayList<String> dipendenti = new ArrayList<String>();
            String [] parametri = {filtro , valore};
            dipendenti.add( (String) GestoreDbThreaded.getInstance().runQuery(6, GestoreDB.entità.Dipendenti, parametri));
            res = ReusableDBResultsConverter.getInstance().getDipendenti(dipendenti);
        }
        CardContainer.getInstance().setCardContainer(res, vboxEsterno);
    }

    @FXML
    void actionSearchButton(ActionEvent event) {
        if(filtroBox.getValue() == null){
            SceneHandler.getInstance().generaAlert("Non hai selezionato il filtro.", false);
        }else if(cercaField.getText().equals("")){
            SceneHandler.getInstance().generaAlert("Non hai inserito il valore da cercare.", false);
        }else{
            generaCard(true, filtroBox.getValue(), cercaField.getText());
        }
    }


    @FXML
    void initialize() throws IOException {
        vboxEsterno.setSpacing(15);
        if(MyInfo.getInstance().getFont().equals("Dyslexie")){
            labelDipendenti.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(),MyInfo.getInstance().getSizeLabel()));
            cercaField.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(),cercaField.getFont().getSize()-1));
            searchButton.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(), searchButton.getFont().getSize()-1));
            addButton.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(), addButton.getFont().getSize()-1));
        }else{
            labelDipendenti.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(),MyInfo.getInstance().getSizeLabel()));
            cercaField.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), cercaField.getFont().getSize()));
            searchButton.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), searchButton.getFont().getSize()));
            addButton.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), addButton.getFont().getSize()));
        }
        filtroBox.getItems().addAll("ID", "Nome", "Cognome", "Ruolo", "Salario");

        generaCard(false, "", "");

    }

}
