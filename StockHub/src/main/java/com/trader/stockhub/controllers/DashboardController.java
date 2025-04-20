package com.trader.stockhub.controllers;

import com.trader.stockhub.base.BaseController;
import com.trader.stockhub.dbms.DBConnection;
import com.trader.stockhub.util.Session;
import com.trader.stockhub.util.ExportUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DashboardController extends BaseController {

    @FXML
    private PieChart assetPieChart;

    @FXML
    private LineChart<Number, Number> performanceLineChart;

    @FXML
    public void initialize() {
        // Use Session to fetch data for the currently logged-in user.
        loadAssetDistribution();
        loadPerformanceData();
    }

    // Loads portfolio data for the current user into the asset distribution PieChart.
    private void loadAssetDistribution() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        int currentUserId = Session.getInstance().getCurrentUserId();
        String query = "SELECT Portfolio_Name, Total_Value FROM Portfolio WHERE User_ID = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, currentUserId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("Portfolio_Name");
                double value = rs.getDouble("Total_Value");
                pieChartData.add(new PieChart.Data(name, value));
            }
            assetPieChart.setData(pieChartData);
        } catch (SQLException | ClassNotFoundException e) {
            showError("Error loading asset distribution: " + e.getMessage());
        }
    }

    // Loads performance data (line chart) for the current user's portfolios.
    // We use the Creation_Date to calculate an x-axis value (days offset from the first portfolio's date).
    private void loadPerformanceData() {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Portfolio Performance");
        int currentUserId = Session.getInstance().getCurrentUserId();
        String query = "SELECT Creation_Date, Total_Value FROM Portfolio WHERE User_ID = ? ORDER BY Creation_Date";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, currentUserId);
            ResultSet rs = ps.executeQuery();

            LocalDate baseDate = null;
            int pointIndex = 0;
            while (rs.next()) {
                String dateStr = rs.getString("Creation_Date");
                double totalValue = rs.getDouble("Total_Value");
                LocalDate currentDate = LocalDate.parse(dateStr);
                if (baseDate == null) {
                    baseDate = currentDate;
                }
                // Compute offset days from base date.
                long daysOffset = ChronoUnit.DAYS.between(baseDate, currentDate);
                // If no variation (e.g., only one portfolio), we use the index as an alternative.
                series.getData().add(new XYChart.Data<>((daysOffset == 0 ? pointIndex : daysOffset), totalValue));
                pointIndex++;
            }
            performanceLineChart.getData().clear();
            performanceLineChart.getData().add(series);
        } catch (SQLException | ClassNotFoundException e) {
            showError("Error loading performance data: " + e.getMessage());
        }
    }

    // Refreshes dashboard charts.
    @FXML
    public void handleRefresh(ActionEvent event) {
        assetPieChart.getData().clear();
        performanceLineChart.getData().clear();
        loadAssetDistribution();
        loadPerformanceData();
    }

    // Navigation handler: If Dashboard button is clicked.
    @FXML
    public void handleDashboard(ActionEvent event) {
        showInfo("You are already on the Dashboard.");
    }

    // Navigation handler: Load the Portfolio Management screen.
    @FXML
    public void handlePortfolio(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PortfolioManagement.fxml"));
            Parent portfolioRoot = loader.load();
            Scene portfolioScene = new Scene(portfolioRoot);
            portfolioScene.getStylesheets().add(getClass().getResource("/css/portfolio.css").toExternalForm());
            Stage stage = (Stage) assetPieChart.getScene().getWindow();
            stage.setScene(portfolioScene);
            stage.setTitle("Portfolio Management");
        } catch (Exception e) {
            showError("Error loading Portfolio Management screen: " + e.getMessage());
        }
    }

    @FXML
    public void handleTrade(ActionEvent event) {
        showInfo("Trade Execution screen is under development.");
    }

    @FXML
    public void handleExport(ActionEvent event) {
        ExportUtil exportUtil = new ExportUtil();
        if (exportUtil.exportData()) {
            showInfo("Portfolio data exported successfully to 'portfolio_export.csv'.");
        } else {
            showError("Export failed.");
        }
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        showInfo("Logout functionality is under development.");
    }
}
