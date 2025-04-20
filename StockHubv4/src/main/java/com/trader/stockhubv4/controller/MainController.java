package com.trader.stockhubv4.controller;

import com.trader.stockhubv4.MainApp;
import com.trader.stockhubv4.util.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;

public class MainController {
    @FXML private StackPane contentPane;

    @FXML
    public void initialize() {
        // Load dashboard by default
        try { showDashboard(); }
        catch (Exception e) { showError("Failed to load Dashboard"); }
    }

    @FXML public void showDashboard() {
        loadView("/fxml/Dashboard.fxml");
    }
    @FXML public void showUsers() {
        loadView("/fxml/UserList.fxml");
    }
    @FXML public void showPortfolios() {
        loadView("/fxml/PortfolioList.fxml");
    }
    @FXML public void showStocks() {
        loadView("/fxml/StockList.fxml");
    }
    @FXML public void showTrades() {
        loadView("/fxml/TradeList.fxml");
    }

    @FXML
    public void onLogout() {
        Session.setCurrentUser(null);
        try {
            MainApp.changeScene("/fxml/Login.fxml");
        } catch (Exception e) {
            showError("Unable to return to login screen");
        }
    }

    /** Utility: load an FXML into the content pane */
    private void loadView(String fxmlPath) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentPane.getChildren().setAll(node);
        } catch (Exception e) {
            showError("Error loading view: " + fxmlPath);
//              e.printStackTrace();
        }
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
