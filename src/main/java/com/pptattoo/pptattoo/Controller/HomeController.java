package com.pptattoo.pptattoo.Controller;

import com.pptattoo.pptattoo.Main;
import com.pptattoo.pptattoo.SceneHandler;
import com.pptattoo.pptattoo.Settings;
import com.pptattoo.pptattoo.View.CustomButton;
import com.pptattoo.pptattoo.View.Dialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Objects;

public class HomeController {

    @FXML
    private AnchorPane anchorPaneParent;

    @FXML
    private AnchorPane anchorPaneImageView;

    @FXML
    private Label labelInfo;

    @FXML
    private ImageView logoView;

    @FXML
    private Pane viewPane;

    @FXML
    private VBox vbox;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button bookNowButton;

    private CustomButton activeButton;

    @FXML
    void initialize() throws IOException {
        impostaTemi();

        initializeLogo();

        configureSidebarButtons();

        configureWelcomeSection();
    }

    private void initializeLogo() {
        Image image = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("img/LogoTattoo.png")));
        logoView.setImage(image);
    }

    private void configureSidebarButtons() {
        int BUTTONS_WIDTH = 140;

        CustomButton dashboard = new CustomButton(anchorPaneParent, BUTTONS_WIDTH, "Dashboard", "img/dashboard.png");
        CustomButton appuntamenti = new CustomButton(anchorPaneParent, BUTTONS_WIDTH, "Appuntamenti", "img/event.png");
        CustomButton statistiche = new CustomButton(anchorPaneParent, BUTTONS_WIDTH, "Statistiche", "img/stats.png");
        CustomButton servizi = new CustomButton(anchorPaneParent, BUTTONS_WIDTH, "Servizi", "img/task.png");
        CustomButton clienti = new CustomButton(anchorPaneParent, BUTTONS_WIDTH, "Clienti", "img/clients.png");
        CustomButton dipendenti = new CustomButton(anchorPaneParent, BUTTONS_WIDTH, "Dipendenti", "img/employee.png");
        CustomButton impostazioni = new CustomButton(anchorPaneParent, BUTTONS_WIDTH, "Impostazioni", "img/settings.png");
        CustomButton logout = new CustomButton(anchorPaneParent, BUTTONS_WIDTH, "Logout", "img/logout.png");

        dashboard.getStyleClass().add("sidebar-button");
        appuntamenti.getStyleClass().add("sidebar-button");
        statistiche.getStyleClass().add("sidebar-button");
        servizi.getStyleClass().add("sidebar-button");
        clienti.getStyleClass().add("sidebar-button");
        dipendenti.getStyleClass().add("sidebar-button");
        impostazioni.getStyleClass().add("sidebar-button");
        logout.getStyleClass().add("sidebar-logout-button");


        vbox.getChildren().addAll(dashboard, appuntamenti, statistiche, servizi, clienti, dipendenti, impostazioni, logout);


        clickButtonAction(dashboard, appuntamenti, statistiche, servizi, clienti, dipendenti, impostazioni, logout);
    }

    private void configureWelcomeSection() {
        // Configura il messaggio di benvenuto
        welcomeLabel.setText("Benvenuto al Tattoo P&P!");
        welcomeLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: white;");

        // Configura il pulsante "Prenota Ora"
        bookNowButton.setText("Prenota Ora");
        bookNowButton.setStyle("-fx-background-color: #b303cd; -fx-text-fill: white; -fx-font-size: 20px;");
        bookNowButton.setOnAction(this::goToBookingPage);
    }

    @FXML
    private void goToBookingPage(ActionEvent event) {
        try {

            CustomButton appuntamentiButton = null;
            for (javafx.scene.Node node : vbox.getChildren()) {
                if (node instanceof CustomButton) {
                    CustomButton button = (CustomButton) node;
                    if (button.getButtonText().equalsIgnoreCase("Appuntamenti")) {
                        appuntamentiButton = button;
                        break;
                    }
                }
            }

            if (appuntamentiButton != null) {
                setActiveButton(appuntamentiButton); // Evidenzia il pulsante "Appuntamenti"
            } else {
                System.out.println("Errore: Pulsante 'Appuntamenti' non trovato.");
            }

            avviaPane("fxml/Appuntamenti");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void impostaTemi() throws IOException {
    }

    private void clickButtonAction(CustomButton... buttons) {
        for (CustomButton btn : buttons) {
            btn.setOnAction(actionEvent -> {
                if (btn.getButtonText().trim().equalsIgnoreCase("Logout")) {
                    SceneHandler.getInstance().launchLogin();
                    return;
                }
                try {
                    setActiveButton(btn);
                    avviaPane("fxml/" + btn.getButtonText().trim());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private void avviaPane(String s) throws IOException {
        viewPane.getChildren().clear();
        FXMLLoader loader = SceneHandler.getInstance().creaPane(s);
        Pane pane = loader.load();

        SceneHandler.getInstance().setRightPaneContainerContent(pane);

        pane.setPrefSize(viewPane.getWidth(), viewPane.getHeight());

        viewPane.layoutBoundsProperty().addListener(obs -> {
            pane.setPrefSize(viewPane.getWidth(), viewPane.getHeight());
        });

        Dialog.getInstance().setAnchorPaneFather(anchorPaneParent);
        viewPane.getChildren().add(pane);
    }

    private void setActiveButton(CustomButton button) {
        if (activeButton == button) return;

        if (activeButton != null) {
            activeButton.getStyleClass().remove("active-button");
        }

        button.getStyleClass().add("active-button");
        activeButton = button;
    }
}
