package com.pptattoo.pptattoo.View;

import com.pptattoo.pptattoo.Model.*;
import com.pptattoo.pptattoo.SceneHandler;
import com.pptattoo.pptattoo.Settings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.*;

public class Dialog {

    private final static Dialog instance = new Dialog();
    private Button rimuoviBt;

    private Dialog() { }
    public static Dialog getInstance() { return instance; }


    private AnchorPane anchorPaneFather;
    public void setAnchorPaneFather(AnchorPane anchorPaneFather) {
        this.anchorPaneFather = anchorPaneFather;
    }
    private boolean hasBeenSetUp = false;


    public enum from { APPUNTAMENTI, CLIENTI, DIPENDENTI, SERVIZI }
    public enum actions {AGGIUNGI(0), MODIFICA(1), RIMUOVI(2);
        actions(int i) { }
    }

    private String[] headerDescriptions = {
            "Compila campi sottostanti",
            "Modifica i campi sottostanti",
            "Conferma la rimozione dei dati sottostanti dal sistema" };

    private javafx.scene.control.Dialog<DialogResponse> dialog;
    private Button okButton;


    private void setDialog(from fromView, actions exeAction, String id) {
        javafx.scene.control.Dialog<DialogResponse> dialog = new javafx.scene.control.Dialog<>();
        this.dialog = dialog;
        dialog.setTitle("Tattoo-Piercing P&P - " + exeAction.toString().toLowerCase());
        dialog.setHeaderText(headerDescriptions[exeAction.ordinal()]);

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);


        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        this.okButton = okButton;

        if (exeAction == actions.MODIFICA){
            dialogPane.getButtonTypes().add(ButtonType.FINISH);
            Button rimuoviBt = (Button) dialogPane.lookupButton(ButtonType.FINISH);
            rimuoviBt.setText("Rimuovi");
            rimuoviBt.getStyleClass().add("rimuoviButton");
            rimuoviBt.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    String[] parametri = {String.valueOf(id)};
                    if (fromView == from.DIPENDENTI && id == String.valueOf(1)) {
                        SceneHandler.getInstance().generaAlert("Non è possibile rimuovere il creatore", true);
                    }

                    switch (fromView) {
                        case APPUNTAMENTI -> {
                            GestoreDbThreaded.getInstance().runQuery(4, GestoreDB.entità.Appuntamenti, parametri);
                        }
                        case CLIENTI -> {
                            GestoreDbThreaded.getInstance().runQuery(4, GestoreDB.entità.Clienti, parametri);
                        }
                        case DIPENDENTI -> {
                            GestoreDbThreaded.getInstance().runQuery(4, GestoreDB.entità.Dipendenti, parametri);
                        }
                        case SERVIZI -> {
                            GestoreDbThreaded.getInstance().runQuery(4, GestoreDB.entità.Servizi, parametri);
                        }
                    }
                    reloadInterface(fromView);
                }
            });
        }

        VBox vbox = switch (fromView) {
            case DIPENDENTI -> setComponentsForDipendenti(fromView, exeAction, id);
            case APPUNTAMENTI -> setComponentsForAppuntamenti(fromView, exeAction, id);
            case CLIENTI -> setComponentsForClienti(fromView, exeAction, id);
            case SERVIZI -> setComponentsForServizi(fromView, exeAction, id);
        };

        dialogPane.setContent(vbox);
        dialogPane.getStyleClass().add("external-pane");

        for (String style : Settings.styles)
            dialogPane.getStylesheets().add(Objects.requireNonNull(SceneHandler.class.getResource(style)).toExternalForm());

        dialogPane.getStylesheets().add(Objects.requireNonNull(SceneHandler.class.getResource(Settings.themes[0])).toExternalForm());

    }

    private VBox setComponentsForAppuntamenti(from fromView, actions exeAction, String id) {
        LinkedList<Node> nodes = new LinkedList<>();

        String[] labels = {
                "Data appuntamento",
                "Codice fiscale",
                "Dipendente",
                "Servizio"
        };

        nodes.add(new Label("Data (dd/mm/YYYY hh:mm)"));
        TextField data = new TextField();
        nodes.add(data);
        configTextField(data, "dd/mm/YYYY hh:mm", 0, type.DATE);


        nodes.add(new Label("Cliente"));
        ObservableList<String> optionsClienti = FXCollections.observableArrayList();
        LinkedList<Cliente> res_clienti = ReusableDBResultsConverter.getInstance().getClienti((ArrayList<String>) GestoreDbThreaded.getInstance().runQuery(1, GestoreDB.entità.Clienti, null));
        for (Cliente d : res_clienti) {
            optionsClienti.add(d.getCF() + " - " + d.getNome() + " " + d.getCognome());
        }
        ComboBox idClienti = new ComboBox<>(optionsClienti);
        nodes.add(idClienti);
        idClienti.setPromptText("Scegli un cliente");


        nodes.add(new Label("Dipendente"));
        ObservableList<String> optionsDip = FXCollections.observableArrayList();
        LinkedList<Dipendente> res_dip = ReusableDBResultsConverter.getInstance().getDipendenti((ArrayList<String>) GestoreDbThreaded.getInstance().runQuery(1, GestoreDB.entità.Dipendenti, null));
        for (Dipendente d : res_dip) {
            optionsDip.add(d.getId() + " - " + d.getName() + " " + d.getLastName());
        }
        ComboBox idDip = new ComboBox<>(optionsDip);
        nodes.add(idDip);


        nodes.add(new Label("Servizio"));
        ObservableList<String> optionsServizi = FXCollections.observableArrayList();
        LinkedList<Servizio> res_serv = ReusableDBResultsConverter.getInstance().getServizi((ArrayList<String>) GestoreDbThreaded.getInstance().runQuery(1, GestoreDB.entità.Servizi, null));
        for (Servizio d : res_serv) {
            optionsServizi.add(d.getId() + " - " + d.getTipo() + " " + d.getPrezzo());
        }
        ComboBox idServizio = new ComboBox<>(optionsServizi);
        nodes.add(idServizio);



        initializeClickedList(nodes, nodes.size());
        ifInModificaMode(nodes, fromView, exeAction, id);
        setResultConverter(nodes, fromView, exeAction, id);

        VBox vbox = externalVbox(nodes, exeAction);
        return vbox;

    }

    private VBox setComponentsForClienti(from fromView, actions exeAction, String id) {
        LinkedList<Node> nodes = new LinkedList<>();

        String[] labels = {
                "Codice Fiscale",
                "Nome",
                "Cognome",
                "Numero",
                "Email"
        };


        nodes.add(new Label("Codice Fiscale"));
        TextField cf = new TextField();
        nodes.add(cf);
        configTextField(cf, labels[0], 0, type.LETTERS_NUMBERS);

        nodes.add(new Label("Nome"));
        TextField nome = new TextField();
        nodes.add(nome);
        configTextField(nome, labels[1], 1, type.NAME_LASTNAME);

        nodes.add(new Label("Cognome"));
        TextField cognome = new TextField();
        nodes.add(cognome);
        configTextField(cognome, labels[2], 2, type.NAME_LASTNAME);

        nodes.add(new Label("Numero"));
        TextField numero = new TextField();
        nodes.add(numero);
        configTextField(numero, labels[3], 3, type.INT);

        nodes.add(new Label("Email"));
        TextField email = new TextField();
        nodes.add(email);
        configTextField(email, labels[4], 4, type.EMAIL);


        initializeClickedList(nodes, nodes.size());
        ifInModificaMode(nodes, fromView, exeAction, id);
        setResultConverter(nodes, fromView, exeAction, id);

        VBox vbox = externalVbox(nodes, exeAction);
        return vbox;
    }


    private enum stato { NEVER_CLICKED, CLICKED, FORM_CORRECT}
    private LinkedList<stato> clicked;


    private VBox setComponentsForDipendenti(from fromView, actions exeAction, String id) {
        LinkedList<Node> nodes = new LinkedList<>();

        String[] labels = {
                "Nome",
                "Cognome",
                "Ruolo",
                "Salario"
        };

        nodes.add(new Label("Nome"));
        TextField name = new TextField();
        nodes.add(name);
        configTextField(name, labels[0], 0, type.NAME_LASTNAME);

        nodes.add(new Label("Cognome"));
        TextField lastName = new TextField();
        nodes.add(lastName);
        configTextField(lastName, labels[1], 1, type.NAME_LASTNAME);

        nodes.add(new Label("Ruolo"));
        ObservableList<Settings.roles> options = FXCollections.observableArrayList(Settings.roles.values());
        ComboBox role = new ComboBox<>(options);
        nodes.add(role);
        if (id.equals(String.valueOf(-2))) {
            role.getSelectionModel().selectFirst();
            role.setDisable(true);
        } else
            role.getSelectionModel().selectLast();

        nodes.add(new Label("Salario"));
        TextField salary = new TextField();
        nodes.add(salary);
        configTextField(salary, "Salario", 2, type.FLOAT);

        initializeClickedList(nodes, nodes.size());
        ifInModificaMode(nodes, fromView, exeAction, id);
        setResultConverter(nodes, fromView, exeAction, id);


        VBox vbox = externalVbox(nodes, exeAction);
        return vbox;
    }


    private VBox setComponentsForServizi(from fromView, actions exeAction, String id) {
        LinkedList<Node> nodes = new LinkedList<>();

        String[] labels = {
                "Servizio",
                "Prezzo",
        };

        nodes.add(new Label("Servizio"));
        TextField nomeServizio = new TextField();
        nodes.add(nomeServizio);
        configTextField(nomeServizio, labels[0], 0, type.LETTERS_NUMBERS);

        nodes.add(new Label("Prezzo"));
        TextField prezzo = new TextField();
        nodes.add(prezzo);
        configTextField(prezzo, labels[1], 1, type.FLOAT);

        initializeClickedList(nodes, nodes.size());
        ifInModificaMode(nodes, fromView, exeAction, id);
        setResultConverter(nodes, fromView, exeAction, id);


        VBox vbox = externalVbox(nodes, exeAction);
        return vbox;
    }

    private void ifInModificaMode(LinkedList<Node> nodes, from fromView, actions exeAction, String id) {

        if (exeAction == actions.MODIFICA ) {
            LinkedList res = dbResults(fromView, id);

            if (res.size() != 0) {
                if (res.get(0).getClass().equals(Servizio.class)) {
                    Servizio resServizio = (Servizio) res.get(0);
                    ((TextField) nodes.get(1)).setText(resServizio.getTipo());
                    ((TextField) nodes.get(3)).setText(resServizio.getPrezzo());
                }
                else if (res.get(0).getClass().equals(Dipendente.class)) {
                    Dipendente resDip = (Dipendente) res.get(0);
                    ((TextField) nodes.get(1)).setText(resDip.getName());
                    ((TextField) nodes.get(3)).setText(resDip.getLastName());
                    ((ComboBox)  nodes.get(5)).getSelectionModel().select(resDip.getRole());
                    ((TextField) nodes.get(7)).setText(resDip.getSalary());
                }
                else if (res.get(0).getClass().equals(Cliente.class)) {
                    Cliente resCliente = (Cliente) res.get(0);
                    ((TextField) nodes.get(1)).setText(resCliente.getCF());
                    ((TextField) nodes.get(3)).setText(resCliente.getNome());
                    ((TextField) nodes.get(5)).setText(resCliente.getCognome());
                    ((TextField) nodes.get(7)).setText(resCliente.getNumero());
                    ((TextField) nodes.get(9)).setText(resCliente.getEmail());
                } else if (res.get(0).getClass().equals(Appuntamento.class)) {
                    Appuntamento app = (Appuntamento) res.get(0);
                    ((TextField) nodes.get(1)).setText(app.getData());
                    ((ComboBox) nodes.get(3)).getSelectionModel().select(app.getCf());
                    ((ComboBox) nodes.get(5)).getSelectionModel().select(app.getDipendente());
                    ((ComboBox) nodes.get(7)).getSelectionModel().select(app.getServizio());
                }
            }


        }
    }


    private void setResultConverter(LinkedList<Node> nodes, from fromView, actions exeAction, String id) {
        dialog.setResultConverter((ButtonType bt) -> {

            if (bt == ButtonType.OK) {

                LinkedList<String> res = new LinkedList<>();
                for (Node node : nodes) {
                    if (node.getClass().equals(TextField.class)) {
                        TextField tf = (TextField) node;
                        res.add(tf.getText());
                    } else if (node.getClass().equals(ComboBox.class)) {
                        ComboBox cb = (ComboBox) node;
                        if (cb.getSelectionModel().isEmpty()) {
                            SceneHandler.getInstance().generaAlert("Non hai inserito un parametro", false);
                            return null;
                        }

                        String add = cb.getSelectionModel().getSelectedItem().toString();
                        String[] split = add.split(" - ");
                        if (split.length == 2) {
                            add = split[0];
                        }
                        res.add(add);
                    }
                }

                if (exeAction == actions.MODIFICA) {
                    res.add(String.valueOf(id));
                    switch (fromView) {

                        case APPUNTAMENTI -> {
                            GestoreDbThreaded.getInstance().runQuery(3, GestoreDB.entità.Appuntamenti, res.toArray(new String[res.size()]));
                        }
                        case CLIENTI -> {

                            GestoreDbThreaded.getInstance().runQuery(3, GestoreDB.entità.Clienti, res.toArray(new String[res.size()]));
                        }
                        case DIPENDENTI -> {
                            GestoreDbThreaded.getInstance().runQuery(3, GestoreDB.entità.Dipendenti, res.toArray(new String[res.size()]));
                        }
                        case SERVIZI -> {
                            GestoreDbThreaded.getInstance().runQuery(3, GestoreDB.entità.Servizi, res.toArray(new String[res.size()]));
                        }
                    }
                    res.removeLast();

                }

                if (exeAction == actions.AGGIUNGI) {

                    switch (fromView) {
                        case APPUNTAMENTI -> {
                            GestoreDbThreaded.getInstance().runQuery(2, GestoreDB.entità.Appuntamenti, res.toArray(new String[res.size()]));
                        }
                        case CLIENTI -> {
                            GestoreDbThreaded.getInstance().runQuery(2, GestoreDB.entità.Clienti, res.toArray(new String[res.size()]));
                        }
                        case DIPENDENTI -> {
                            String psw = ""; //password vuota in posizione 0
                            if ((res.get(3).equals(Settings.roles.PROPRIETARIO.toString()) || res.get(3).equals(Settings.roles.ARTISTA.toString()))) {
                                SceneHandler.getInstance().generaAlert("La password predefinita dell'utente è nome.cognome.id\nLa password potrà essere modificata al primo login nella sezione impostazioni", true);
                                psw = res.get(0) + "." + res.get(1) + "." + res.getLast();
                            }
                            res.addFirst(psw);
                            GestoreDbThreaded.getInstance().runQuery(2, GestoreDB.entità.Dipendenti, res.toArray(new String[res.size()]));
                        }
                        case SERVIZI -> {
                            GestoreDbThreaded.getInstance().runQuery(2, GestoreDB.entità.Servizi, res.toArray(new String[res.size()]));
                        }
                    }
                    res.removeLast();

                }

                reloadInterface(fromView);
                return new DialogResponse(res);

            }
            return null;
        });
    }


    private VBox externalVbox(LinkedList<Node> nodes, actions exeActions) {
        VBox vbox = new VBox(8);

        if (exeActions == actions.RIMUOVI)
            doNotAllowChanges(nodes);

        for (Node node : nodes)
            vbox.getChildren().add(node);

        return vbox;
    }

    private LinkedList dbResults(from fromView, String id) {

        LinkedList res = null;
        String [] parametri = {String.valueOf(id)};

        res = switch (fromView) {
            case APPUNTAMENTI ->
                ReusableDBResultsConverter.getInstance().getAppuntamenti(
                    new ArrayList<>(Collections.singleton((String)GestoreDbThreaded.getInstance().runQuery(7, GestoreDB.entità.Appuntamenti, parametri)))
                );
            case CLIENTI ->
                ReusableDBResultsConverter.getInstance().getClienti(
                    new ArrayList<>(Collections.singleton((String)GestoreDbThreaded.getInstance().runQuery(7, GestoreDB.entità.Clienti, parametri)))
                );
            case DIPENDENTI ->
                ReusableDBResultsConverter.getInstance().getDipendenti(
                    new ArrayList<>(Collections.singleton((String)GestoreDbThreaded.getInstance().runQuery(7, GestoreDB.entità.Dipendenti, parametri)))
                );
            case SERVIZI ->
                ReusableDBResultsConverter.getInstance().getServizi(
                    new ArrayList<>(Collections.singleton((String)GestoreDbThreaded.getInstance().runQuery(7, GestoreDB.entità.Servizi, parametri)))
                );
        };

        return res;
    }


    private void configTextField(TextField l, String promptText, int index, type tipo) {
        l.setPromptText(promptText);
        l.getStyleClass().add("generalField");
        l.textProperty().addListener((observableValue, s, t1) -> {
            okButton.setDisable(!checkField(l, index, t1, tipo));
        });


    }


    //prende come parametro l'attuale ancorPane della view
    public Optional<DialogResponse> requestDialog(from fromView, actions exeAction, String id, AnchorPane anchorPane)  { //prende come parametri i nomi in minuscolo delle singole opzioni da richedere all'utente

        if (anchorPaneFather == null || anchorPane == null) {
            SceneHandler.getInstance().generaAlert("Errore Generale. Prova di nuovo", false);
            return null;    //se null, non è stato possibile effettuare la richiesta
        }

        Pane rightHomePane = (Pane) anchorPane.getParent();
        if (!hasBeenSetUp) {
            setUpBlurEffect(rightHomePane);
        }

        setDialog(fromView, exeAction, id);

        Optional<DialogResponse> optionalResult = dialog.showAndWait();
        disableBlurEffect(rightHomePane);

        return optionalResult;
    }





    private void setUpBlurEffect(Pane rightHomePane) {
        hasBeenSetUp = true;
        ColorAdjust adj = new ColorAdjust(0, -0.9, -0.5, 0);
        GaussianBlur blur = new GaussianBlur(55);
        adj.setInput(blur);
        rightHomePane.setEffect(adj);
        rightHomePane.setMouseTransparent(true);
    }

    private void disableBlurEffect(Pane rightHomePane) {
        hasBeenSetUp = false;
        rightHomePane.setEffect(null);
        rightHomePane.setMouseTransparent(false);
    }



    public enum type {FLOAT, NAME_LASTNAME, INT, LETTERS_NUMBERS, EMAIL, DATE}

    private boolean checkField(Node node, int index, String newValue, type tipo) {

        if (clicked.get(index) == stato.NEVER_CLICKED)
            clicked.set(index, stato.CLICKED);

        if (!newValue.toString().equals("")) {
            boolean statoRes = switch (tipo) {
                case FLOAT -> new InputCheck().checkFloat(newValue);
                case NAME_LASTNAME -> new InputCheck().checkNameLastname(newValue);
                case INT -> new InputCheck().checkInt(newValue);
                case LETTERS_NUMBERS -> new InputCheck().checkLettersNumbers(newValue);
                case EMAIL -> new InputCheck().checkEmail(newValue);
                case DATE -> new InputCheck().checkDate(newValue);
            };

            if (statoRes)
                clicked.set(index, stato.FORM_CORRECT);
            else
                clicked.set(index, stato.CLICKED);


            int count = 0;
            for(stato state : clicked) {
                if (state == stato.FORM_CORRECT)
                    count++;
            }

            if (count == clicked.size()) {
                return true;
            }
            
        } else
            clicked.set(index, stato.CLICKED);
        return false;
    }

    private void doNotAllowChanges(LinkedList<Node> nodes) {
        for(Node node : nodes) {
            if (node.getClass().equals(TextField.class)) {
                TextField textField = (TextField) node;
                textField.setEditable(false);
            }

            else if (node.getClass().equals(ComboBox.class)) {
                ComboBox comboBox = (ComboBox) node;
                comboBox.setEditable(false);
            }
        }
    }

    private void initializeClickedList(LinkedList<Node> nodes, int size) {
        clicked = new LinkedList<>();
        for(int i = 0; i < size; i++)
            if (nodes.get(i).getClass().equals(TextField.class))
                clicked.add(stato.NEVER_CLICKED);
    }

    private void reloadInterface(from fromView) {
        FXMLLoader loader = null;
        try {
            switch (fromView) {
                case APPUNTAMENTI -> {
                    loader = SceneHandler.getInstance().creaPane("fxml/Appuntamenti");
                }
                case CLIENTI -> {
                    loader = SceneHandler.getInstance().creaPane("fxml/Clienti");
                }
                case DIPENDENTI -> {
                    loader = SceneHandler.getInstance().creaPane("fxml/Dipendenti");
                }
                case SERVIZI -> {
                    loader = SceneHandler.getInstance().creaPane("fxml/Servizi");

                }
            }
        } catch (IOException e) {

        }

        Pane pane = null;
        try {
            pane = (Pane) loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Pane viewPane = SceneHandler.getInstance().getPaneRightContainerContent();
        viewPane.getChildren().clear();

        pane.setPrefSize(viewPane.getWidth(), viewPane.getHeight());

        Pane finalPane = pane;
        viewPane.layoutBoundsProperty().addListener(obs -> {
            finalPane.setPrefSize(viewPane.getWidth(), viewPane.getHeight());
        });

        Dialog.getInstance().setAnchorPaneFather(anchorPaneFather);
        viewPane.getChildren().add(pane);
    }
}
