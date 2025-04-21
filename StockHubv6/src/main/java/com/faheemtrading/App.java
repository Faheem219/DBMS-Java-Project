package com.faheemtrading;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

public class App extends Application {
    @Override public void start(Stage stage) throws Exception {
        Scene scene = new Scene(
                FXMLLoader.load(getClass().getResource("/fxml/login.fxml")));
        scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());
        stage.setTitle("Stock Trading & Portfolio Manager");
//        scene.getStylesheets().add(
//                org.kordamp.bootstrapfx.BootstrapFX.class
//                        .getResource("bootstrapfx.css").toExternalForm());
        stage.setScene(scene);
        stage.setMinHeight(300);
        stage.setMinWidth(400);
        stage.show();
    }
    public static void main(String[] args) { launch(args); }
}
