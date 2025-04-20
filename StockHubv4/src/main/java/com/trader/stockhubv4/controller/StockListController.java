package com.trader.stockhubv4.controller;

import com.trader.stockhubv4.dao.StockDAO;
import com.trader.stockhubv4.dao.impl.StockDAOImpl;
import com.trader.stockhubv4.model.Stock;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Optional;

public class StockListController {
    @FXML private TableView<Stock> tableStocks;
    @FXML private TableColumn<Stock, Integer> colSid;
    @FXML private TableColumn<Stock, String>  colSname;
    @FXML private TableColumn<Stock, Double>  colPrice;
    @FXML private TableColumn<Stock, Double>  colCap;
    @FXML private TableColumn<Stock, Double>  colVol;

    private final StockDAO dao = new StockDAOImpl();

    @FXML
    public void initialize() {
        colSid.setCellValueFactory(d -> new javafx.beans.property.
                SimpleIntegerProperty(d.getValue().getStockId()).asObject());
        colSname.setCellValueFactory(d -> new javafx.beans.property.
                SimpleStringProperty(d.getValue().getName()));
        colPrice.setCellValueFactory(d -> new javafx.beans.property.
                SimpleDoubleProperty(d.getValue().getMarketPrice()).asObject());
        colCap.setCellValueFactory(d -> new javafx.beans.property.
                SimpleDoubleProperty(d.getValue().getMarketCap()).asObject());
        colVol.setCellValueFactory(d -> new javafx.beans.property.
                SimpleDoubleProperty(d.getValue().getVolatility()).asObject());
        onRefresh();
    }

    @FXML
    private void onAdd() {
        Dialog<Stock> dlg = new Dialog<>();
        dlg.setTitle("Add Stock");
        dlg.getDialogPane().getButtonTypes()
                .addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        TextField idF    = new TextField();
        TextField nameF  = new TextField();
        TextField priceF = new TextField();
        TextField capF   = new TextField();
        TextField volF   = new TextField();

        grid.addRow(0, new Label("ID:"), idF);
        grid.addRow(1, new Label("Name:"), nameF);
        grid.addRow(2, new Label("Price:"), priceF);
        grid.addRow(3, new Label("Market Cap:"), capF);
        grid.addRow(4, new Label("Volatility:"), volF);

        dlg.getDialogPane().setContent(grid);
        dlg.setResultConverter(bt -> {
            if (bt == ButtonType.OK) {
                Stock s = new Stock();
                s.setStockId(Integer.parseInt(idF.getText()));
                s.setName(nameF.getText());
                s.setMarketPrice(Double.parseDouble(priceF.getText()));
                s.setMarketCap(Double.parseDouble(capF.getText()));
                s.setVolatility(Double.parseDouble(volF.getText()));
                return s;
            }
            return null;
        });

        Optional<Stock> res = dlg.showAndWait();
        res.ifPresent(s -> {
            try {
                dao.create(s);
                onRefresh();
            } catch (Exception e) {
                showError("Add failed: " + e.getMessage());
            }
        });
    }

    @FXML
    private void onEdit() {
        Stock sel = tableStocks.getSelectionModel().getSelectedItem();
        if (sel == null) { showError("Select one first"); return; }

        Dialog<Stock> dlg = new Dialog<>();
        dlg.setTitle("Edit Stock");
        dlg.getDialogPane().getButtonTypes()
                .addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        TextField idF    = new TextField(String.valueOf(sel.getStockId()));
        idF.setDisable(true);
        TextField nameF  = new TextField(sel.getName());
        TextField priceF = new TextField(String.valueOf(sel.getMarketPrice()));
        TextField capF   = new TextField(String.valueOf(sel.getMarketCap()));
        TextField volF   = new TextField(String.valueOf(sel.getVolatility()));

        grid.addRow(0, new Label("ID:"), idF);
        grid.addRow(1, new Label("Name:"), nameF);
        grid.addRow(2, new Label("Price:"), priceF);
        grid.addRow(3, new Label("Market Cap:"), capF);
        grid.addRow(4, new Label("Volatility:"), volF);

        dlg.getDialogPane().setContent(grid);
        dlg.setResultConverter(bt -> {
            if (bt == ButtonType.OK) {
                sel.setName(nameF.getText());
                sel.setMarketPrice(Double.parseDouble(priceF.getText()));
                sel.setMarketCap(Double.parseDouble(capF.getText()));
                sel.setVolatility(Double.parseDouble(volF.getText()));
                return sel;
            }
            return null;
        });

        Optional<Stock> res = dlg.showAndWait();
        res.ifPresent(s -> {
            try {
                dao.update(s);
                onRefresh();
            } catch (Exception e) {
                showError("Update failed: " + e.getMessage());
            }
        });
    }

    @FXML
    private void onDelete() {
        Stock sel = tableStocks.getSelectionModel().getSelectedItem();
        if (sel == null) { showError("Select one first"); return; }

        Alert c = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete “" + sel.getName() + "”?",
                ButtonType.OK, ButtonType.CANCEL);
        c.showAndWait().ifPresent(b -> {
            if (b == ButtonType.OK) {
                try {
                    dao.delete(sel.getStockId());
                    onRefresh();
                } catch (Exception e) {
                    showError("Delete failed: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void onRefresh() {
        try {
            List<Stock> all = dao.findAll();
            tableStocks.setItems(FXCollections.observableArrayList(all));
        } catch (Exception e) {
            showError("Load failed: " + e.getMessage());
        }
    }

    private void showError(String m) {
        new Alert(Alert.AlertType.ERROR, m, ButtonType.OK)
                .showAndWait();
    }
}
