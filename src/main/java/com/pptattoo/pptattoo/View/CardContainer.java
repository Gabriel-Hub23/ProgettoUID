package com.pptattoo.pptattoo.View;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.LinkedList;

public class CardContainer {
    private static CardContainer istance = new CardContainer();

    private CardContainer() { }

    public static CardContainer getInstance() {
        return istance;
    }


    public <T> void setCardContainer(LinkedList<T> res, VBox vboxEsterno) {
        if (res.size() != 0)
            for (int i = 0; i < res.size(); i = i+2) {
                Card obj1 = new Card(res.get(i), vboxEsterno);

                HBox hbox = new HBox(10, obj1);
                hbox.setFillHeight(true);
                hbox.setAlignment(Pos.BASELINE_LEFT);

                Card obj2;
                if (i+1 < res.size()) {
                    obj2 = new Card(res.get(i + 1), vboxEsterno);
                    hbox.getChildren().add(obj2);
                }

                vboxEsterno.getChildren().add(hbox);
            }

    }





}
