package com.trader.stockhubv4.controller;

import com.trader.stockhubv4.MainApp;
import com.trader.stockhubv4.dao.UserDAO;
import com.trader.stockhubv4.dao.impl.UserDAOImpl;
import com.trader.stockhubv4.exception.AppException;
import com.trader.stockhubv4.model.User;
import com.trader.stockhubv4.util.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    private final UserDAO userDao = new UserDAOImpl();

    @FXML
    private void onLoginClicked() {
        String email = emailField.getText().trim();
        String pwd   = passwordField.getText();

        if (email.isEmpty() || pwd.isEmpty()) {
            showAlert("Validation Error", "Please enter both email and password.");
            return;
        }

        try {
            User user = userDao.authenticate(email, pwd)
                    .orElse(null);
            if (user == null) {
                showAlert("Login Failed", "Invalid email or password.");
            } else {
                Session.setCurrentUser(user);
                MainApp.changeScene("/fxml/Main.fxml");
            }
        } catch (SQLException|AppException e) {
            showAlert("Error", "An error occurred: " + e.getMessage());
        } catch (Exception e) {
            showAlert("Fatal Error", "Unable to load main screen.");
        }
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
