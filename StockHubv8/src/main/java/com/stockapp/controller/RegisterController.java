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

public class RegisterController {
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private final AuthService authService = new AuthService();

    @FXML
    private void onRegister() {
        // Basic input validation
        String idText = idField.getText().trim();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        int userId;
        try {
            userId = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            messageLabel.setText("Invalid User ID");
            return;
        }
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("All fields are required");
            return;
        }

        try {
            User user = new User(userId, name, email, password);
            authService.register(user);
            messageLabel.setText("Registration successful! Please login.");
        } catch (AppException e) {
            messageLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void onBackToLogin() {
        try {
            Parent loginRoot = FXMLLoader.load(
                    getClass().getResource("/com/stockapp/view/Login.fxml"));
            Stage stage = (Stage) idField.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
        } catch (Exception e) {
            messageLabel.setText("Cannot load login screen");
        }
    }
}
