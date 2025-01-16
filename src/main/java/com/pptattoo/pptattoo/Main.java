package com.pptattoo.pptattoo;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        SceneHandler.getInstance().init(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}