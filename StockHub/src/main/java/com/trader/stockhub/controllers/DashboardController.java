package com.trader.stockhub.controllers;

import com.trader.stockhub.base.BaseController;
import com.trader.stockhub.dbms.DBConnection;
import com.trader.stockhub.util.ExportUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardController extends BaseController {

    @FXML
    private PieChart assetPieChart;

    @FXML
    private LineChart<Number, Number> performanceLineChart;

    // Called automatically when the FXML is loaded.
    @FXML
    public void initialize() {
        loadAssetDistribution();
        loadPerformanceData();
    }

    // Loads portfolio data into the asset distribution pie chart.
    private void loadAssetDistribution() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        String query = "SELECT Portfolio_Name, Total_Value FROM Portfolio";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while(rs.next()){
                String name = rs.getString("Portfolio_Name");
                double value = rs.getDouble("Total_Value");
                pieChartData.add(new PieChart.Data(name, value));
            }
            assetPieChart.setData(pieChartData);
        } catch(SQLException | ClassNotFoundException e){
            showError("Error loading asset distribution: " + e.getMessage());
        }
    }

    // Loads a simple performance data set into the line chart.
    private void loadPerformanceData() {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Portfolio Performance");
        // Simple simulation: use Portfolio_ID as time axis, Total_Value as the Y value.
        String query = "SELECT Portfolio_ID, Total_Value FROM Portfolio ORDER BY Portfolio_ID";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            int time = 1;
            while(rs.next()){
                double value = rs.getDouble("Total_Value");
                series.getData().add(new XYChart.Data<>(time++, value));
            }
            performanceLineChart.getData().clear();
            performanceLineChart.getData().add(series);
        } catch(SQLException | ClassNotFoundException e){
            showError("Error loading performance data: " + e.getMessage());
        }
    }

    // Refreshes the data in both charts.
    @FXML
    public void handleRefresh(ActionEvent event) {
        assetPieChart.getData().clear();
        performanceLineChart.getData().clear();
        loadAssetDistribution();
        loadPerformanceData();
    }

    // Sample navigation handlers.
    @FXML
    public void handleDashboard(ActionEvent event) {
        showInfo("You are already on the Dashboard.");
    }

    @FXML
    public void handlePortfolio(ActionEvent event) {
        showInfo("Portfolio Management screen is under development.");
    }

    @FXML
    public void handleTrade(ActionEvent event) {
        showInfo("Trade Execution screen is under development.");
    }

    @FXML
    public void handleExport(ActionEvent event) {
        ExportUtil exportUtil = new ExportUtil(); // Using polymorphism via the Exportable interface.
        if(exportUtil.exportData()){
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
