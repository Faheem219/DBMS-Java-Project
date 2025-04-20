package com.trader.stockhub.controllers;

import com.trader.stockhub.dbms.DBConnection;
import com.trader.stockhub.base.BaseController;
import com.trader.stockhub.util.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController extends BaseController {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    @FXML
    public void handleLogin(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        if(email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both email and password.");
            return;
        }
        try (Connection con = DBConnection.getConnection()){
            String query = "SELECT * FROM User WHERE Email = ? AND Password = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                // Retrieve logged in user's ID and name
                int userId = rs.getInt("User_ID");
                String userName = rs.getString("Name");
                // Store in the session so that other controllers know the logged in user.
                Session session = Session.getInstance();
                session.setCurrentUserId(userId);
                session.setCurrentUserName(userName);

                // Successful login. Load dashboard.
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
                Parent dashboardRoot = loader.load();
                Scene dashboardScene = new Scene(dashboardRoot);
                dashboardScene.getStylesheets().add(getClass().getResource("/css/dashboard.css").toExternalForm());
                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(dashboardScene);
                stage.setTitle("Dashboard");
            } else {
                errorLabel.setText("Invalid login credentials.");
            }
        } catch(Exception e){
            e.printStackTrace();
            errorLabel.setText("An error occurred: " + e.getMessage());
        }
    }
}
