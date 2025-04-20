package com.trader.stockhubv4.controller;

import com.trader.stockhubv4.dao.TradeDAO;
import com.trader.stockhubv4.dao.impl.TradeDAOImpl;
import com.trader.stockhubv4.model.Trade;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TradeListController {
    @FXML private TableView<Trade> tableTrades;
    @FXML private TableColumn<Trade, Integer> colTid;
    @FXML private TableColumn<Trade, Double>  colPrice;
    @FXML private TableColumn<Trade, LocalDate> colDate;
    @FXML private TableColumn<Trade, Integer> colQty;
    @FXML private TableColumn<Trade, Integer> colUser;
    @FXML private TableColumn<Trade, Integer> colPort;
    @FXML private TableColumn<Trade, Integer> colStock;

    private final TradeDAO dao = new TradeDAOImpl();

    @FXML
    public void initialize() {
        colTid.setCellValueFactory(d -> new javafx.beans.property.
                SimpleIntegerProperty(d.getValue().getTradeId()).asObject());
        colPrice.setCellValueFactory(d -> new javafx.beans.property.
                SimpleDoubleProperty(d.getValue().getPrice()).asObject());
        colDate.setCellValueFactory(d -> new javafx.beans.property.
                SimpleObjectProperty<>(d.getValue().getTradeDate()));
        colQty.setCellValueFactory(d -> new javafx.beans.property.
                SimpleIntegerProperty(d.getValue().getQuantity()).asObject());
        colUser.setCellValueFactory(d -> new javafx.beans.property.
                SimpleIntegerProperty(d.getValue().getUserId()).asObject());
        colPort.setCellValueFactory(d -> new javafx.beans.property.
                SimpleIntegerProperty(d.getValue().getPortfolioId()).asObject());
        colStock.setCellValueFactory(d -> new javafx.beans.property.
                SimpleIntegerProperty(d.getValue().getStockId()).asObject());
        onRefresh();
    }

    @FXML
    private void onAdd() {
        Dialog<Trade> dlg = new Dialog<>();
        dlg.setTitle("Add Trade");
        dlg.getDialogPane().getButtonTypes()
                .addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        TextField idF    = new TextField();
        TextField priceF = new TextField();
        DatePicker dateF = new DatePicker(LocalDate.now());
        TextField qtyF   = new TextField();
        TextField userF  = new TextField();
        TextField portF  = new TextField();
        TextField stockF = new TextField();

        grid.addRow(0, new Label("ID:"), idF);
        grid.addRow(1, new Label("Price:"), priceF);
        grid.addRow(2, new Label("Date:"), dateF);
        grid.addRow(3, new Label("Qty:"), qtyF);
        grid.addRow(4, new Label("User ID:"), userF);
        grid.addRow(5, new Label("Port ID:"), portF);
        grid.addRow(6, new Label("Stock ID:"), stockF);

        dlg.getDialogPane().setContent(grid);
        dlg.setResultConverter(bt -> {
            if (bt == ButtonType.OK) {
                Trade t = new Trade();
                t.setTradeId(Integer.parseInt(idF.getText()));
                t.setPrice(Double.parseDouble(priceF.getText()));
                t.setTradeDate(dateF.getValue());
                t.setQuantity(Integer.parseInt(qtyF.getText()));
                t.setUserId(Integer.parseInt(userF.getText()));
                t.setPortfolioId(Integer.parseInt(portF.getText()));
                t.setStockId(Integer.parseInt(stockF.getText()));
                return t;
            }
            return null;
        });

        Optional<Trade> res = dlg.showAndWait();
        res.ifPresent(t -> {
            try {
                dao.create(t);
                onRefresh();
            } catch (Exception e) {
                showError("Add failed: " + e.getMessage());
            }
        });
    }

    @FXML
    private void onEdit() {
        Trade sel = tableTrades.getSelectionModel().getSelectedItem();
        if (sel == null) { showError("Select one first"); return; }

        Dialog<Trade> dlg = new Dialog<>();
        dlg.setTitle("Edit Trade");
        dlg.getDialogPane().getButtonTypes()
                .addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        TextField idF    = new TextField(String.valueOf(sel.getTradeId()));
        idF.setDisable(true);
        TextField priceF = new TextField(String.valueOf(sel.getPrice()));
        DatePicker dateF = new DatePicker(sel.getTradeDate());
        TextField qtyF   = new TextField(String.valueOf(sel.getQuantity()));
        TextField userF  = new TextField(String.valueOf(sel.getUserId()));
        TextField portF  = new TextField(String.valueOf(sel.getPortfolioId()));
        TextField stockF = new TextField(String.valueOf(sel.getStockId()));

        grid.addRow(0, new Label("ID:"), idF);
        grid.addRow(1, new Label("Price:"), priceF);
        grid.addRow(2, new Label("Date:"), dateF);
        grid.addRow(3, new Label("Qty:"), qtyF);
        grid.addRow(4, new Label("User ID:"), userF);
        grid.addRow(5, new Label("Port ID:"), portF);
        grid.addRow(6, new Label("Stock ID:"), stockF);

        dlg.getDialogPane().setContent(grid);
        dlg.setResultConverter(bt -> {
            if (bt == ButtonType.OK) {
                sel.setPrice(Double.parseDouble(priceF.getText()));
                sel.setTradeDate(dateF.getValue());
                sel.setQuantity(Integer.parseInt(qtyF.getText()));
                sel.setUserId(Integer.parseInt(userF.getText()));
                sel.setPortfolioId(Integer.parseInt(portF.getText()));
                sel.setStockId(Integer.parseInt(stockF.getText()));
                return sel;
            }
            return null;
        });

        Optional<Trade> res = dlg.showAndWait();
        res.ifPresent(t -> {
            try {
                dao.update(t);
                onRefresh();
            } catch (Exception e) {
                showError("Update failed: " + e.getMessage());
            }
        });
    }

    @FXML
    private void onDelete() {
        Trade sel = tableTrades.getSelectionModel().getSelectedItem();
        if (sel == null) { showError("Select one first"); return; }

        Alert c = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete trade " + sel.getTradeId() + "?",
                ButtonType.OK, ButtonType.CANCEL);
        c.showAndWait().ifPresent(b -> {
            if (b == ButtonType.OK) {
                try {
                    dao.delete(sel.getTradeId());
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
            List<Trade> all = dao.findAll();
            tableTrades.setItems(FXCollections.observableArrayList(all));
        } catch (Exception e) {
            showError("Load failed: " + e.getMessage());
        }
    }

    private void showError(String m) {
        new Alert(Alert.AlertType.ERROR, m, ButtonType.OK)
                .showAndWait();
    }
}
