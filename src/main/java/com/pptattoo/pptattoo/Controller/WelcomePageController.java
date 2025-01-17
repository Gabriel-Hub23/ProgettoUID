package com.pptattoo.pptattoo.Controller;

import com.pptattoo.pptattoo.Main;
import com.pptattoo.pptattoo.SceneHandler;
import com.pptattoo.pptattoo.Settings;
import com.pptattoo.pptattoo.View.MyTransition;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class WelcomePageController {
    @FXML
    private Pane pane;
    @FXML
    private Label labelTitle;
    @FXML
    private Button continueButton;
    @FXML
    private StackPane stackPane;


    private SequentialTransition imgTransition;
    private ImageView imageView = new ImageView();
    private List<Image> images = new LinkedList<>();

    private Stage stage;


    @FXML
    void actionContinueButton() {
        if (imgTransition != null && imgTransition.getStatus() == Animation.Status.RUNNING) {
            imgTransition.stop();
        }
        SceneHandler.getInstance().launchLogin();
    }


    public void initialize() {
        continueButton.setDisable(true);
        labelTitle.setText(Settings.INIT_TITLE);
        labelTitle.setTextAlignment(TextAlignment.CENTER);

        configStackPane();
        setImages();
        configContinueButton();
    }


    private void configStackPane() {
        Platform.runLater(() -> setLayoutStackPane());

        pane.layoutBoundsProperty().addListener(observable -> setLayoutStackPane());
        stackPane.setAlignment(Pos.CENTER);
    }


    private void setLayoutStackPane() {
        double width = stackPane.getWidth();
        double height = stackPane.getHeight();
        double x = (pane.getWidth() - width) / 2;
        double y = (pane.getHeight() - height) / 2;
        stackPane.resizeRelocate(x, y, width, height);
    }


    private void configContinueButton() {
        continueButton.setText("Start");
        continueButton.prefWidthProperty().bind(stackPane.widthProperty().divide(3).multiply(2.5));
        continueButton.prefHeightProperty().bind(stackPane.heightProperty().divide(5).multiply(0.3));
    }


    private void setImages() {
        int N_IMAGES = 8;

        for (int i = 1; i <= N_IMAGES; i++) {
            try {
                Image image = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("img/Tattoo-Piercing" + i + ".png")));
                images.add(image);
                if (i == 8) {
                    imageView.setImage(image);
                }
            } catch (Exception e) {
                SceneHandler.getInstance().generaAlert("Immagine non trovata: Tattoo" + i + ".png", false);
            }
        }

        imageView.maxWidth(stackPane.getMaxWidth());
        imageView.maxHeight(210);

        stackPane.layoutBoundsProperty().addListener(observable -> setLayoutImageView());

        setLayoutImageView();

        imgTransition = MyTransition.getInstance().imgFadeTransition(images, imageView);
        imgTransition.setCycleCount(SequentialTransition.INDEFINITE);
        imgTransition.play();

        continueButton.setDisable(false);
    }


    private void setLayoutImageView() {
        imageView.setFitWidth(stackPane.getWidth());
        imageView.setFitHeight(210);
        imageView.setPreserveRatio(true);

        imageView.setLayoutX((stackPane.getWidth() - imageView.getFitWidth()) / 2);
        imageView.setLayoutY(0);


        if (!stackPane.getChildren().contains(imageView)) {
            stackPane.getChildren().add(imageView);
        }

        System.out.println("Image width: " + imageView.getFitWidth() + ", height: " + imageView.getFitHeight());
    }
}
