package com.pptattoo.pptattoo.Controller;

import com.pptattoo.pptattoo.Model.GestoreDB;
import com.pptattoo.pptattoo.Model.GestoreDbThreaded;
import com.pptattoo.pptattoo.SceneHandler;
import com.pptattoo.pptattoo.View.Dialog;
import com.pptattoo.pptattoo.View.MyInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private AnchorPane ancorPane;
    @FXML
    private Pane pane;
    @FXML
    private Label loginLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;
    @FXML
    private Button accediButton;

    @FXML
    private TextField textPasswordField;

    @FXML
    private Button togglePasswordButton;

    private boolean isPasswordVisible = false;

    @FXML
    private void togglePasswordVisibility() {
        ImageView eyeIcon;

        if (isPasswordVisible) {

            passwordField.setText(textPasswordField.getText());
            textPasswordField.setVisible(false);
            textPasswordField.setManaged(false);
            passwordField.setVisible(true);
            passwordField.setManaged(true);


            eyeIcon = new ImageView(new Image(getClass().getResource("/com/pptattoo/pptattoo/img/eye.png").toExternalForm()));
        } else {

            textPasswordField.setText(passwordField.getText());
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            textPasswordField.setVisible(true);
            textPasswordField.setManaged(true);


            eyeIcon = new ImageView(new Image(getClass().getResource("/com/pptattoo/pptattoo/img/eye-closed.png").toExternalForm()));
        }

        eyeIcon.setFitWidth(16);
        eyeIcon.setFitHeight(16);
        togglePasswordButton.setGraphic(eyeIcon);

        isPasswordVisible = !isPasswordVisible;
    }

    @FXML
    void accedi(ActionEvent event) {
        String[] parametri = {"", "1"};
        while ((int) GestoreDbThreaded.getInstance().runQuery(8, null, parametri) == 0) {
            Dialog.getInstance().requestDialog(Dialog.from.DIPENDENTI, Dialog.actions.AGGIUNGI, "-2", ancorPane).isPresent();
        }

        String[] parametriPass = {usernameField.getText(), passwordField.getText()};
        boolean query = (boolean) GestoreDbThreaded.getInstance().runQuery(12, null, parametriPass);

        if (query) {
            String[] info = {"Username", usernameField.getText()};
            String riga = (String) GestoreDbThreaded.getInstance().runQuery(6, GestoreDB.entità.Dipendenti, info);
            MyInfo.getInstance().setId(Integer.parseInt(riga.split(";")[0]));
            SceneHandler.getInstance().launchDashboard();
        } else {
            showAlert();
        }
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore di Login");
        alert.setHeaderText("Username o Password errati, controlla e riprova");
        alert.showAndWait();
    }

    private void impostaTemi() throws IOException {
        if (MyInfo.getInstance().getFont().equals("Dyslexie")) {
            loginLabel.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(), loginLabel.getFont().getSize() - 2));
            passwordField.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(), passwordField.getFont().getSize() - 3));
            usernameField.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(), usernameField.getFont().getSize() - 3));
            usernameLabel.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(), usernameLabel.getFont().getSize() - 3));
            passwordLabel.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(), passwordLabel.getFont().getSize() - 3));
            accediButton.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(), accediButton.getFont().getSize() - 3));
        } else {
            loginLabel.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), loginLabel.getFont().getSize()));
            passwordField.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), passwordField.getFont().getSize()));
            usernameField.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), usernameField.getFont().getSize()));
            usernameLabel.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), usernameLabel.getFont().getSize()));
            passwordLabel.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), passwordLabel.getFont().getSize()));
            accediButton.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), accediButton.getFont().getSize()));
        }
    }

    @FXML
    void initialize() throws IOException {
        impostaTemi();
        usernameField.setOnKeyReleased(this::handleKeyReleasedUsernameField);
        passwordField.setOnKeyReleased(this::handleKeyReleasedPasswordField);

        textPasswordField.setVisible(false);
        textPasswordField.setManaged(false);
        textPasswordField.textProperty().bindBidirectional(passwordField.textProperty());
    }

    public void init(Stage stage) {
        configPane(stage);
    }

    private void handleKeyReleasedUsernameField(KeyEvent event) {
        canBeAccediButtonVisible();
    }

    private void handleKeyReleasedPasswordField(KeyEvent event) {
        canBeAccediButtonVisible();
    }

    private void canBeAccediButtonVisible() {
        accediButton.setDisable(usernameField.getText().isEmpty() || passwordField.getText().isEmpty());
    }

    private void configPane(Stage stage) {
        if (pane.getWidth() == 0 && pane.getHeight() == 0) {
            AnchorPane.setLeftAnchor(pane, (ancorPane.getWidth() - pane.getPrefWidth()) / 2);
            AnchorPane.setTopAnchor(pane, (ancorPane.getHeight() - pane.getPrefHeight()) / 2);
        }

        ancorPane.boundsInLocalProperty().addListener((observable, oldValue, newValue) -> {
            AnchorPane.setLeftAnchor(pane, (ancorPane.getWidth() - pane.getWidth()) / 2);
            AnchorPane.setTopAnchor(pane, (ancorPane.getHeight() - pane.getHeight()) / 2);
        });
    }
}
