package com.pptattoo.pptattoo.View;

import com.pptattoo.pptattoo.Main;
import com.pptattoo.pptattoo.Settings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

import java.util.Objects;

public class CustomButton extends Button {
    private String buttonText;
    private String imagePath;
    private String[] buttonDescription;

    private Node parent;
    private int buttonWidth;

    // Solo immagine
    public CustomButton(String buttonText, String imagePath, String... buttonDescription) {
        super();
        this.buttonText = buttonText;
        this.imagePath = imagePath;
        this.buttonDescription = buttonDescription;

        setButtonImageOnly();
    }

    // Basato sulla grandezza del nodo padre
    public CustomButton(Node parent, int buttonWidth, String buttonText, String imagePath, String... buttonDescription) {
        super();
        this.parent = parent;
        this.buttonWidth = buttonWidth;
        this.buttonText = " " + buttonText;
        this.imagePath = imagePath;
        this.buttonDescription = buttonDescription;

        setButtonBasedOnWindowSize();
    }

    private void setButtonBasedOnWindowSize() {
        setImageInButton();
        this.setAlignment(Pos.BASELINE_LEFT);

        // Sempre mostra il testo
        this.setText(buttonText);
        this.setPrefWidth(buttonWidth);
        this.setPadding(new Insets(
                this.getGraphic().getBoundsInLocal().getHeight() / 3,
                10,
                this.getGraphic().getBoundsInLocal().getHeight() / 3,
                10
        ));

        setButtonDescription(); // Imposta la descrizione del pulsante
    }

    private void setButtonImageOnly() {
        setImageInButton();
        setButtonDescription();
    }

    private void setButtonDescription() {
        StringBuilder s = new StringBuilder(buttonText.toUpperCase());

        for (String value : buttonDescription) s.append("\n").append(value);

        Tooltip tooltip = new Tooltip(s.toString());
        tooltip.setFont(new Font(14));
        this.setTooltip(tooltip);
    }

    private void setImageInButton() {
        ImageView imageView = new ImageView();
        try {
            Image image = new Image(Objects.requireNonNull(Main.class.getResourceAsStream(imagePath)));
            imageView.setImage(image);
            imageView.setFitWidth(25);
            imageView.setFitHeight(25);
            imageView.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Errore nel caricamento dell'immagine per il pulsante: " + buttonText);
            e.printStackTrace();
        }
        this.setGraphic(imageView);
    }

    public String getButtonText() {
        return buttonText.trim(); // Rimuove eventuali spazi
    }
}
