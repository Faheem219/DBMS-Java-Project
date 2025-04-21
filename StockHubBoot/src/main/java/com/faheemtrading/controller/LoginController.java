package com.faheemtrading.controller;

import com.faheemtrading.service.AuthService;
import com.faheemtrading.util.AlertUtil;
import com.faheemtrading.util.Session;
import com.faheemtrading.util.ValidationUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    private final AuthService auth = new AuthService();

    @FXML
    private void onLogin(ActionEvent e) {
        String email = emailField.getText();
        String pass  = passwordField.getText();

        if (!ValidationUtil.notEmpty(email, pass) || !ValidationUtil.isEmail(email)) {
            AlertUtil.error("Invalid input", "Please enter a valid email & password.");
            return;
        }

        auth.login(email, pass).ifPresentOrElse(user -> {
            Session.setUser(user);
            openMainStage();
        }, () -> AlertUtil.error("Login failed", "Incorrect credentials."));
    }

    private void openMainStage() {
        try {
            Stage stage = (Stage) emailField.getScene().getWindow();
            Scene scene = new Scene(
                    FXMLLoader.load(getClass().getResource("/fxml/main.fxml")));
            scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());
            stage.setScene(scene);
        } catch (Exception ex) {
            ex.printStackTrace();
            AlertUtil.error("Fatal", "Cannot load main view.");
        }
    }

    @FXML
    private void openRegister(ActionEvent e) {
        try {
            Stage stage = (Stage) emailField.getScene().getWindow();
            Scene scene = new Scene(
                    FXMLLoader.load(getClass().getResource("/fxml/register.fxml")));
            scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());
            stage.setScene(scene);
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}
