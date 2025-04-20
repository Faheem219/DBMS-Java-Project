package com.faheemtrading.controller;

import com.faheemtrading.util.AlertUtil;
import com.faheemtrading.util.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainController {
    @FXML private StackPane contentPane;

    @FXML
    public void initialize() {
        showDashboard();
    }

    @FXML private void showDashboard()   { loadView("/fxml/dashboard.fxml"); }
    @FXML private void showPortfolios()  { loadView("/fxml/portfolio.fxml"); }
    @FXML private void showTrades()      { loadView("/fxml/trade.fxml"); }
    @FXML private void showWatchlists(){ loadView("/fxml/watchlist.fxml"); }
    @FXML private void showAlerts(){ loadView("/fxml/alerts.fxml"); }


    @FXML
    private void logout() {
        Session.clear();
        AlertUtil.info("Logged out", "You have been signed out.");
        try {
            contentPane.getScene().getWindow().hide();
            javafx.application.Application.launch(com.faheemtrading.App.class);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadView(String fxml) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(fxml));
            contentPane.getChildren().setAll(node);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML private VBox sideBar;
    @FXML private ToggleButton toggleBtn;
    private boolean collapsed=false;

    @FXML private void toggleSidebar(){
        double from = collapsed? -180 : 0;
        double to   = collapsed? 0    : -180;
        javafx.animation.TranslateTransition tt=new javafx.animation.TranslateTransition(
                javafx.util.Duration.millis(250), sideBar);
        tt.setFromX(from); tt.setToX(to); tt.play();
        collapsed=!collapsed;
    }

    @FXML private ToggleButton darkToggle;

    @FXML private void toggleTheme(){
        Scene scene = darkToggle.getScene();
        if(scene.getRoot().getStyleClass().contains("dark")){
            scene.getRoot().getStyleClass().remove("dark");
        } else {
            scene.getRoot().getStyleClass().add("dark");
        }
    }

}
