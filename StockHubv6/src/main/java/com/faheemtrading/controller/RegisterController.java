package com.faheemtrading.controller;

import com.faheemtrading.service.AuthService;
import com.faheemtrading.util.AlertUtil;
import com.faheemtrading.util.ValidationUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField pwd1Field;
    @FXML private PasswordField pwd2Field;

    private final AuthService auth = new AuthService();

    @FXML
    private void onRegister(ActionEvent e) {
        String name  = nameField.getText();
        String email = emailField.getText();
        String p1    = pwd1Field.getText();
        String p2    = pwd2Field.getText();

        if (!ValidationUtil.notEmpty(name, email, p1, p2) ||
                !ValidationUtil.isEmail(email) ||
                !p1.equals(p2)) {
            AlertUtil.error("Error", "Please provide matching passwords & valid email.");
            return;
        }

        if (auth.register(name, email, p1)) {
            AlertUtil.info("Success", "Account created! Please log in.");
            backToLogin(null);
        } else {
            AlertUtil.error("Registration failed", "Email already exists.");
        }
    }

    @FXML
    public void backToLogin(ActionEvent e) {
        try {
            Stage stage = (Stage) nameField.getScene().getWindow();
            Scene scene = new Scene(
                    FXMLLoader.load(getClass().getResource("/fxml/login.fxml")));
            scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());
            stage.setScene(scene);
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}
