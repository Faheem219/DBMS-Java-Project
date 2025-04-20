package com.trader.stockhub.controllers;

import com.trader.stockhub.base.BaseController;
import com.trader.stockhub.dbms.DBConnection;
import com.trader.stockhub.models.Portfolio;
import com.trader.stockhub.util.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PortfolioManagementController extends BaseController {

    @FXML
    private TableView<Portfolio> portfolioTable;
    @FXML
    private TableColumn<Portfolio, Integer> idColumn;
    @FXML
    private TableColumn<Portfolio, String> nameColumn;
    @FXML
    private TableColumn<Portfolio, String> dateColumn;
    @FXML
    private TableColumn<Portfolio, Double> valueColumn;
    @FXML
    private TextField nameField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField valueField;

    // Use the currently logged in user's id from Session.
    private final int currentUserId = Session.getInstance().getCurrentUserId();
    private final ObservableList<Portfolio> portfolioList = FXCollections.observableArrayList();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    public void initialize(){
        // Set up table columns using the Portfolio model properties.
        idColumn.setCellValueFactory(new PropertyValueFactory<>("portfolioId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("portfolioName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("totalValue"));

        loadPortfolios();

        // When a row is selected, populate the fields.
        portfolioTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(newSelection != null){
                nameField.setText(newSelection.getPortfolioName());
                datePicker.setValue(LocalDate.parse(newSelection.getCreationDate(), dateFormatter));
                valueField.setText(String.valueOf(newSelection.getTotalValue()));
            }
        });
    }

    // Fetches all portfolios for the current user from the database.
    private void loadPortfolios(){
        portfolioList.clear();
        String query = "SELECT * FROM Portfolio WHERE User_ID = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, currentUserId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id = rs.getInt("Portfolio_ID");
                String name = rs.getString("Portfolio_Name");
                String creationDate = rs.getString("Creation_Date");
                double totalValue = rs.getDouble("Total_Value");
                portfolioList.add(new Portfolio(id, name, creationDate, totalValue));
            }
            portfolioTable.setItems(portfolioList);
        } catch (SQLException | ClassNotFoundException ex){
            showError("Error loading portfolios: " + ex.getMessage());
        }
    }

    // Generates the next Portfolio_ID by taking the current maximum and adding 1.
    private int getNextPortfolioId(){
        String query = "SELECT COALESCE(MAX(Portfolio_ID), 0) + 1 AS NextID FROM Portfolio";
        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if(rs.next()){
                return rs.getInt("NextID");
            }
        } catch (SQLException | ClassNotFoundException ex){
            showError("Error generating next Portfolio ID: " + ex.getMessage());
        }
        return 1;
    }

    // Adds a new portfolio record to the database.
    @FXML
    public void handleAddPortfolio(){
        String name = nameField.getText().trim();
        LocalDate date = datePicker.getValue();
        String valueStr = valueField.getText().trim();
        if(name.isEmpty() || date == null || valueStr.isEmpty()){
            showError("Please fill in all fields.");
            return;
        }
        double totalValue;
        try {
            totalValue = Double.parseDouble(valueStr);
        } catch(NumberFormatException ex){
            showError("Total Value must be a number.");
            return;
        }
        int newId = getNextPortfolioId();
        String query = "INSERT INTO Portfolio (Portfolio_ID, Portfolio_Name, Creation_Date, Total_Value, User_ID) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, newId);
            ps.setString(2, name);
            ps.setString(3, date.format(dateFormatter));
            ps.setDouble(4, totalValue);
            ps.setInt(5, currentUserId);
            int result = ps.executeUpdate();
            if(result > 0){
                showInfo("Portfolio added successfully.");
                loadPortfolios();
                clearFields();
            } else {
                showError("Failed to add portfolio.");
            }
        } catch (SQLException | ClassNotFoundException ex){
            showError("Error adding portfolio: " + ex.getMessage());
        }
    }

    // Updates the selected portfolio.
    @FXML
    public void handleUpdatePortfolio(){
        Portfolio selected = portfolioTable.getSelectionModel().getSelectedItem();
        if(selected == null){
            showError("Please select a portfolio to update.");
            return;
        }
        String name = nameField.getText().trim();
        LocalDate date = datePicker.getValue();
        String valueStr = valueField.getText().trim();
        if(name.isEmpty() || date == null || valueStr.isEmpty()){
            showError("Please fill in all fields.");
            return;
        }
        double totalValue;
        try {
            totalValue = Double.parseDouble(valueStr);
        } catch(NumberFormatException ex){
            showError("Total Value must be a number.");
            return;
        }
        String query = "UPDATE Portfolio SET Portfolio_Name = ?, Creation_Date = ?, Total_Value = ? WHERE Portfolio_ID = ? AND User_ID = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setString(2, date.format(dateFormatter));
            ps.setDouble(3, totalValue);
            ps.setInt(4, selected.getPortfolioId());
            ps.setInt(5, currentUserId);
            int result = ps.executeUpdate();
            if(result > 0){
                showInfo("Portfolio updated successfully.");
                loadPortfolios();
                clearFields();
            } else {
                showError("Failed to update portfolio.");
            }
        } catch (SQLException | ClassNotFoundException ex){
            showError("Error updating portfolio: " + ex.getMessage());
        }
    }

    // Deletes the selected portfolio from the database.
    @FXML
    public void handleDeletePortfolio(){
        Portfolio selected = portfolioTable.getSelectionModel().getSelectedItem();
        if(selected == null){
            showError("Please select a portfolio to delete.");
            return;
        }
        String query = "DELETE FROM Portfolio WHERE Portfolio_ID = ? AND User_ID = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, selected.getPortfolioId());
            ps.setInt(2, currentUserId);
            int result = ps.executeUpdate();
            if(result > 0){
                showInfo("Portfolio deleted successfully.");
                loadPortfolios();
                clearFields();
            } else {
                showError("Failed to delete portfolio.");
            }
        } catch (SQLException | ClassNotFoundException ex){
            showError("Error deleting portfolio: " + ex.getMessage());
        }
    }

    // Refreshes the portfolio list and clears input fields.
    @FXML
    public void handleRefresh(){
        loadPortfolios();
        clearFields();
    }

    // Clears input fields in the form.
    private void clearFields(){
        nameField.clear();
        datePicker.setValue(null);
        valueField.clear();
    }
}
