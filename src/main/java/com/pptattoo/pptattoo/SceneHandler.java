package com.pptattoo.pptattoo;

import com.pptattoo.pptattoo.Controller.LoginController;
import com.pptattoo.pptattoo.Model.GestoreDB;
import com.pptattoo.pptattoo.View.MyInfo;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.System.exit;

public class SceneHandler {
    private static final SceneHandler instance = new SceneHandler();
    private Stage stage;
    private static Scene scene;
    private FXMLLoader loader;

    private DialogPane dialog;
    private static final Logger LOGGER = Logger.getLogger(SceneHandler.class.getName());

    public static SceneHandler getInstance() {
        return instance;
    }

    private SceneHandler() {
    }

    public void init(Stage stage) throws SQLException {
        if (this.stage == null) {
            this.stage = stage;
            stage.setTitle(Settings.INIT_TITLE);
            launchWelcomeFirstOpening();
            stage.setScene(scene);
            GestoreDB.getInstance().createConnection();
            MyInfo.getInstance().prendiDati();
            loadStyle();
            stage.show();
            stage.setOnCloseRequest(event -> {
                try {
                    GestoreDB.getInstance().closeConnection();
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Error closing DB connection", e);
                }
                System.exit(0);
            });
        }
    }

    private void loadFXML(String resourceName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SceneHandler.class.getResource(resourceName));

            if (scene == null)
                scene = new Scene(fxmlLoader.load());
            else
                scene.setRoot(fxmlLoader.load());

            loader = fxmlLoader;

        } catch (IOException e) {
            generaAlert("Qualcosa è andato storto!", false);
            LOGGER.log(Level.SEVERE, "Error loading FXML", e);
        }
    }

    public void launchWelcomeFirstOpening() {
        loadFXML("fxml/WelcomePage.fxml");
        setWindowLoginDimension();
    }

    Pane paneRightContainerContent = null;

    public void setRightPaneContainerContent(Pane paneRightContainerContent) {
        this.paneRightContainerContent = paneRightContainerContent;
    }

    public Pane getPaneRightContainerContent() {
        return paneRightContainerContent;
    }

    public void launchLogin() {
        loadFXML("fxml/LoginView.fxml");
        LoginController controller = loader.getController();
        controller.init(stage);
        setWindowLoginDimension();
    }

    public void launchDashboard() {
        setWindowAppDimension();
        loadFXML("fxml/HomeView.fxml");
    }

    public FXMLLoader creaPane(String s) throws IOException {
        return new FXMLLoader(SceneHandler.class.getResource(s + "View.fxml"));
    }

    private void setWindowLoginDimension() {
        stage.setMinHeight(Settings.MIN_WINDOW_HEIGHT);
        stage.setMinWidth(Settings.MIN_WINDOW_WIDTH_LOGIN);
        stage.setHeight(Settings.DEFAULT_WINDOW_HEIGHT);
        stage.setWidth(Settings.DEFAULT_WINDOW_WIDTH);
        stage.setResizable(true);
    }

    private void setWindowAppDimension() {
        stage.setMinHeight(Settings.MIN_WINDOW_HEIGHT);
        stage.setMinWidth(Settings.MIN_WINDOW_WIDTH_APP);
        stage.setHeight(Settings.DEFAULT_WINDOW_HEIGHT);
        stage.setWidth(Settings.DEFAULT_WINDOW_WIDTH);
        stage.setResizable(true);
    }

    private void loadStyle() {
        for (String font : Settings.fonts)
            Font.loadFont(Objects.requireNonNull(SceneHandler.class.getResource(font)).toExternalForm(), 10);

        for (String style : Settings.styles)
            scene.getStylesheets().add(Objects.requireNonNull(SceneHandler.class.getResource(style)).toExternalForm());

        setTheme();
    }

    public void setTheme() {
        String tema = MyInfo.getInstance().getTema();
        if (tema == null) {
            tema = "DARK"; // Default theme if null
            LOGGER.warning("Tema non trovato, impostato il tema di default: DARK");
        }

        String pathTheme = switch (tema) {
            case "DARK" -> Settings.themes[0];
            case "LIGHT" -> Settings.themes[1];
            case "BLU" -> Settings.themes[2];
            default -> {
                LOGGER.severe("Tema non valido: " + tema);
                exit(11);
                yield Settings.themes[0];
            }
        };

        if (scene.getStylesheets().contains(Objects.requireNonNull(SceneHandler.class.getResource(pathTheme)).toExternalForm())) {
            return;
        }

        scene.getStylesheets().clear();
        scene.getStylesheets().add(Objects.requireNonNull((SceneHandler.class.getResource(Settings.styles[0])).toExternalForm()));
        scene.getStylesheets().add(Objects.requireNonNull(SceneHandler.class.getResource(pathTheme)).toExternalForm());
    }

    public String apriDirectoryChooser() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Scegli cartella");
        String name = "";
        try {
            name = chooser.showDialog(stage).getAbsolutePath();
        } catch (NullPointerException e) {
            LOGGER.log(Level.WARNING, "DirectoryChooser returned null", e);
        }
        return name;
    }

    public void generaAlert(String testo, boolean notifica) {
        Alert alert;
        if (notifica) {
            alert = new Alert(Alert.AlertType.INFORMATION);
        } else {
            alert = new Alert(Alert.AlertType.WARNING);
        }

        String tema = MyInfo.getInstance().getTema();
        if (tema == null) {
            tema = "DARK";
        }

        String pathTheme = switch (tema) {
            case "DARK" -> Settings.themes[0];
            case "LIGHT" -> Settings.themes[1];
            case "BLU" -> Settings.themes[2];
            default -> {
                LOGGER.severe("Tema non valido: " + tema);
                exit(11);
                yield Settings.themes[0];
            }
        };

        dialog = alert.getDialogPane();
        dialog.getStylesheets().add(Objects.requireNonNull((SceneHandler.class.getResource(Settings.styles[0])).toExternalForm()));
        dialog.getStylesheets().add(Objects.requireNonNull(SceneHandler.class.getResource(pathTheme)).toExternalForm());
        dialog.getStyleClass().add("alert");

        if (notifica) {
            alert.setTitle("Notifica");
            alert.setHeaderText("Ecco i tuoi appuntamenti giornalieri");
            alert.setContentText(testo);
        } else {
            alert.setTitle("Attenzione");
            alert.setHeaderText("Qualcosa è andato storto.");
            alert.setContentText(testo);
        }

        alert.setResizable(false);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.showAndWait();
    }

    public boolean generaAlertConfirm() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        dialog = alert.getDialogPane();
        String pathTheme = switch (MyInfo.getInstance().getTema()) {
            case "DARK" -> Settings.themes[0];
            case "LIGHT" -> Settings.themes[1];
            case "BLU" -> Settings.themes[2];
            default -> {
                LOGGER.severe("Tema non valido: " + MyInfo.getInstance().getTema());
                exit(11);
                yield Settings.themes[0];
            }
        };
        dialog.getStylesheets().add(Objects.requireNonNull((SceneHandler.class.getResource(Settings.styles[0])).toExternalForm()));
        dialog.getStylesheets().add(Objects.requireNonNull(SceneHandler.class.getResource(pathTheme)).toExternalForm());
        dialog.getStyleClass().add("alert");
        alert.setTitle("Conferma");
        alert.setHeaderText("Confermi di voler eliminare i dati?");
        alert.setContentText("Nota: Dovrai inserire il proprietario.\n(Con username e password \n uguale a Nome.Cognome.1)");
        alert.setResizable(false);
        alert.initStyle(StageStyle.UNDECORATED);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }
}
