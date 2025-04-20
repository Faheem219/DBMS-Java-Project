package com.trader.stockhubv4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    private static Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent login = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        scene = new Scene(login);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        primaryStage.setTitle("StockHub v4 – Login");
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(800);
        primaryStage.show();
    }

    /** Utility to switch scenes (login ↔ main UI) */
    public static void changeScene(String fxmlPath) throws Exception {
        Parent pane = FXMLLoader.load(MainApp.class.getResource(fxmlPath));
        scene.setRoot(pane);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
