package com.stockapp.controller;

import com.stockapp.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    private User currentUser;

    @FXML private StackPane contentPane;

    /** Called by LoginController after login */
    public void initUserSession(User user) {
        this.currentUser = user;
        showDashboard();
    }

    @FXML private void showDashboard()     { loadView("/com/stockapp/view/Dashboard.fxml"); }
    @FXML private void showPortfolioView() { loadView("/com/stockapp/view/PortfolioView.fxml"); }
    @FXML private void showWatchlistView() { loadView("/com/stockapp/view/WatchlistView.fxml"); }

    @FXML private void onLogout() {
        try {
            Parent login = FXMLLoader.load(getClass().getResource("/com/stockapp/view/Login.fxml"));
            Stage stage = (Stage) contentPane.getScene().getWindow();
            stage.setScene(new Scene(login));
        } catch (IOException ignored) {}
    }

    private void loadView(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent view = loader.load();
            Object ctrl = loader.getController();
            if (ctrl instanceof UserAware) {
                ((UserAware) ctrl).initData(currentUser);
            }
            contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
