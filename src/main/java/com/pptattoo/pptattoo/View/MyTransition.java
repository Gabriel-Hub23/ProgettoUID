package com.pptattoo.pptattoo.View;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.List;

public class MyTransition {

    private static final MyTransition instance = new MyTransition();
    private MyTransition() {}

    public static MyTransition getInstance() {
        return instance;
    }


    public SequentialTransition fadeInOBJ() {

        FadeTransition fadeIn = new FadeTransition(Duration.millis(2500));
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        SequentialTransition seqTransition = new SequentialTransition();
        seqTransition.setDelay(Duration.seconds(2));
        seqTransition.getChildren().addAll(fadeIn);

        return seqTransition;
    }



    private int indexImage = 0;
    public SequentialTransition imgFadeTransition(List<Image> images, ImageView imageView) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), imageView);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished((ActionEvent ae) -> {
            imageView.setImage(images.get(indexImage));
            indexImage++;
            if (indexImage > images.size()-1)
                indexImage = 0;
        });

        FadeTransition fadeIn = new FadeTransition(Duration.millis(2500), imageView);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        SequentialTransition seqTransition = new SequentialTransition();
        seqTransition.setDelay(Duration.seconds(2));
        seqTransition.getChildren().addAll(fadeOut, fadeIn);
        return seqTransition;
    }

}
