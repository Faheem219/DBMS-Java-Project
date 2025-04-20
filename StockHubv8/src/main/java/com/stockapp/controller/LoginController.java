package com.stockapp.controller;

import com.stockapp.model.User;
import com.stockapp.service.AuthService;
import com.stockapp.util.AppException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

// ← Add this import:
import com.stockapp.controller.MainController;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private final AuthService authService = new AuthService();

    @FXML
    private void onLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        try {
            User user = authService.login(email, password);
            openMainApp(user);
        } catch (AppException e) {
            messageLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void onRegister() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/stockapp/view/Register.fxml"));
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            messageLabel.setText("Cannot load register screen");
        }
    }

    private void openMainApp(User user) throws AppException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/stockapp/view/Main.fxml"));
            Parent root = loader.load();
            MainController mainCtrl = loader.getController();
            mainCtrl.initUserSession(user);

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            throw new AppException("Error opening main window", e);
        }
    }
}
