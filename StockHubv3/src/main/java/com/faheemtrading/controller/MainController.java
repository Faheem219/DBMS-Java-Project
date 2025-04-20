package com.faheemtrading.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import com.faheemtrading.util.AlertUtil;

public class MainController {

    /* ---------- FXML controls ---------- */
    @FXML private StackPane contentPane;
    @FXML private Label      badge;          // <Label fx:id="badge" ...> in main_layout.fxml

    /* ---------- initialise ---------- */
    @FXML
    public void initialize() {
        showDashboard();          // default view
        attachBadge(badge);       // hook badge label
    }

    /* ---------- navigation ---------- */
    @FXML private void showDashboard()  { loadView("/fxml/dashboard.fxml"); }
    @FXML private void showPortfolios() { loadView("/fxml/portfolio.fxml"); }
    @FXML private void showTrades()     { loadView("/fxml/trade.fxml"); }
    @FXML private void showAlerts()     { loadView("/fxml/alerts.fxml"); }

    private void loadView(String fxml) {
        try {
            Node view = FXMLLoader.load(getClass().getResource(fxml));
            contentPane.getChildren().setAll(view);
        } catch (Exception e) {
            AlertUtil.error("UI Error", e.getMessage());
        }
    }

    @FXML
    private void logout() {
        Stage stage = (Stage) contentPane.getScene().getWindow();
        stage.close();
    }

    /* ===================================== *
     *      Sidebar badge (unread alerts)
     * ===================================== */
    private static Label badgeLbl;
    public static void attachBadge(Label lbl){ badgeLbl = lbl; }
    public static void updateBadge(int cnt){
        if (badgeLbl != null)
            badgeLbl.setText(cnt == 0 ? "" : String.valueOf(cnt));
    }
}
