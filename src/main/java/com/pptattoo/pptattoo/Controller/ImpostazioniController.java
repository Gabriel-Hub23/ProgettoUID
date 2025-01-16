package com.pptattoo.pptattoo.Controller;

import com.pptattoo.pptattoo.Model.GestoreDB;
import com.pptattoo.pptattoo.Model.GestoreDbThreaded;
import com.pptattoo.pptattoo.SceneHandler;
import com.pptattoo.pptattoo.View.Dialog;
import com.pptattoo.pptattoo.View.MyInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;

public class ImpostazioniController {

    @FXML
    private AnchorPane ancorPane;
    @FXML
    private Label labelImpostazioni;

    @FXML
    private ComboBox<String> temiComboBox;

    @FXML
    private Text txtTemi;

    @FXML
    private Label labelTemi;

    @FXML
    private CheckBox checkDislessia;

    @FXML
    private Label labelDislessia;

    @FXML
    private Text txtDislessia;

    @FXML
    private CheckBox checkNotifica;

    @FXML
    private Label labelNotifica;

    @FXML
    private Text txtNotifica;

    @FXML
    private Label labelRipristina;
    @FXML
    private Text txtRipristina;

    @FXML
    private Button ripristinaButton;

    @FXML
    private Button cambiaButton;

    @FXML
    private Label labelCambia;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Text txtBasso;

    // Funzione che imposta i tutti i dettagli dei font scelti: Tema, Font e Size(quest'ultima non è modificabile all'utente)
    private void impostaFont() throws IOException {
        if(MyInfo.getInstance().getFont().equals("Dyslexie")){
            labelImpostazioni.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(),MyInfo.getInstance().getSizeLabel()-1));
            labelDislessia.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(),MyInfo.getInstance().getSizeLabel()-1));
            labelTemi.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(),MyInfo.getInstance().getSizeLabel()-1));
            labelRipristina.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(),MyInfo.getInstance().getSizeLabel()-1));
            labelNotifica.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(),MyInfo.getInstance().getSizeLabel()-1));
            labelCambia.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(),MyInfo.getInstance().getSizeLabel()-1));
            txtRipristina.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(),MyInfo.getInstance().getSizeTxt()-1));
            txtTemi.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(),MyInfo.getInstance().getSizeTxt()-1));
            txtDislessia.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(),MyInfo.getInstance().getSizeTxt()-1));
            txtBasso.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(),MyInfo.getInstance().getSizeTxt()-1));
            txtNotifica.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(),MyInfo.getInstance().getSizeTxt()-1));
            passwordField.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(),MyInfo.getInstance().getSizeTxt()-1));
            ripristinaButton.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(), MyInfo.getInstance().getSizeTxt()-3));
            cambiaButton.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(), MyInfo.getInstance().getSizeTxt()-3));
        }else{
            labelImpostazioni.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(),MyInfo.getInstance().getSizeLabel()));
            labelDislessia.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), MyInfo.getInstance().getSizeLabel()));
            labelTemi.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), MyInfo.getInstance().getSizeLabel()));
            labelNotifica.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), MyInfo.getInstance().getSizeLabel()));
            labelRipristina.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), MyInfo.getInstance().getSizeLabel()));
            labelCambia.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), MyInfo.getInstance().getSizeLabel()));
            txtTemi.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), MyInfo.getInstance().getSizeTxt()));
            txtDislessia.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), MyInfo.getInstance().getSizeTxt()));
            txtNotifica.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), MyInfo.getInstance().getSizeTxt()));
            txtRipristina.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), MyInfo.getInstance().getSizeTxt()));
            txtBasso.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), MyInfo.getInstance().getSizeTxt()));
            passwordField.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), MyInfo.getInstance().getSizeTxt()));
            ripristinaButton.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), ripristinaButton.getFont().getSize()));
            cambiaButton.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), ripristinaButton.getFont().getSize()));
        }
    }

    // É la funzione che imposta il tema dei testi e bottoni, ogni volta aggiorna anche i dati nel DB
    @FXML
    void scegliTema(ActionEvent event) throws SQLException, IOException {
        if (temiComboBox.getSelectionModel().isSelected(0)){
            MyInfo.getInstance().setTema("DARK");
        }else if (temiComboBox.getSelectionModel().isSelected(1)){
            MyInfo.getInstance().setTema("LIGHT");
        }else if (temiComboBox.getSelectionModel().isSelected(2)){
            MyInfo.getInstance().setTema("BLU");
        }
        String info = MyInfo.getInstance().getTema()+";"+ MyInfo.getInstance().getFont()+";"+MyInfo.getInstance().getNotifica();
        GestoreDbThreaded.getInstance().runQuery(3, GestoreDB.getInstance().getTemplate(), info.split(";"));
        SceneHandler.getInstance().setTheme();
        initialize();
    }

    @FXML
    void cambiaPassword(ActionEvent event) {
        if(passwordField.getText().equals("")){
            SceneHandler.getInstance().generaAlert("Password non inserita.",false);
        }else{
            String [] parametri = {String.valueOf(MyInfo.getInstance().getId()),passwordField.getText()};
            GestoreDbThreaded.getInstance().runQuery(9,null, parametri);
        }
        passwordField.setPromptText("Nuova password");
    }

    // Funzione che attiva il font in base alla check box
    @FXML
    void attivaDislessia(ActionEvent event) throws SQLException, IOException {
        MyInfo.getInstance().setFont(checkDislessia.isSelected());
        String info = MyInfo.getInstance().getTema()+";"+ MyInfo.getInstance().getFont()+";"+MyInfo.getInstance().getNotifica();
        GestoreDbThreaded.getInstance().runQuery(3, GestoreDB.getInstance().getTemplate(), info.split(";"));
        initialize();
    }

    @FXML
    void attivaNotifica(ActionEvent event) throws SQLException, IOException {
        if(checkNotifica.isSelected()){
            MyInfo.getInstance().setNotifica(0);
        }else{
            MyInfo.getInstance().setNotifica(1);
        }
        String info = MyInfo.getInstance().getTema()+";"+ MyInfo.getInstance().getFont()+";"+MyInfo.getInstance().getNotifica();
        GestoreDbThreaded.getInstance().runQuery(3, GestoreDB.getInstance().getTemplate(), info.split(";"));
    }


    @FXML
    void ripristina(ActionEvent event) throws SQLException {
        if(SceneHandler.getInstance().generaAlertConfirm()) {
            if(Dialog.getInstance().requestDialog(Dialog.from.DIPENDENTI, Dialog.actions.AGGIUNGI, "-2", ancorPane).isPresent()){
                GestoreDbThreaded.getInstance().runQuery(13, null, null);
                SceneHandler.getInstance().launchLogin();
            }
        }
    }

    //Initialize, vengono inserite tutte le cose all'inizio
    @FXML
    public void initialize() throws IOException {
        if(MyInfo.getInstance().getFont().equals("Dyslexie")){
            checkDislessia.setSelected(true);
        }
        if(MyInfo.getInstance().getNotifica() == 0){
            checkNotifica.setSelected(true);
        }
        if (temiComboBox.getItems().size() == 0){
            temiComboBox.getItems().add("Scuro");
            temiComboBox.getItems().add("Chiaro");
            temiComboBox.getItems().add("Blu");
        }
        String tema = MyInfo.getInstance().getTema();
        switch (tema){
            case "DARK"->{temiComboBox.setValue("Scuro");}
            case "LIGHT"->{temiComboBox.setValue("Chiaro");}
            case "BLU"->{temiComboBox.setValue("Blu");}
        }
        impostaFont();
    }
}
