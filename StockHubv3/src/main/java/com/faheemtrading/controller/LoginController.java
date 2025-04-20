package com.faheemtrading.controller;

import com.faheemtrading.dao.UserDAO;
import com.faheemtrading.dao.impl.UserDAOImpl;
import com.faheemtrading.exception.AppException;
import com.faheemtrading.model.User;
import com.faheemtrading.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final UserDAO userDAO = new UserDAOImpl();

    @FXML
    private void handleLogin(){
        String email = emailField.getText();
        String pass  = passwordField.getText();

        try {
            User user = userDAO.authenticate(email, pass);
            if (user != null){
                Stage stage = new Stage();
                Scene scene = new Scene(
                        FXMLLoader.load(getClass().getResource("/fxml/main_layout.fxml")),
                        1000, 650
                );
                stage.setTitle("Dashboard - " + user.getName());
                stage.setScene(scene);
                stage.show();
                // close login window
                ((Stage) emailField.getScene().getWindow()).close();
            } else {
                errorLabel.setText("Invalid credentials!");
            }
        } catch (AppException | IOException e){
            AlertUtil.error("Error", e.getMessage());
        }
    }

}
