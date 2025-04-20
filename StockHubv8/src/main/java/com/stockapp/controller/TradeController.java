package com.stockapp.controller;

import com.stockapp.model.Portfolio;
import com.stockapp.model.Stock;
import com.stockapp.model.Trade;
import com.stockapp.model.User;
import com.stockapp.service.PortfolioService;
import com.stockapp.service.StockService;
import com.stockapp.service.TradeService;
import com.stockapp.util.AppException;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TradeController implements UserAware {
    private User currentUser;
    private final TradeService tradeSvc = new TradeService();
    private final PortfolioService portSvc = new PortfolioService();
    private final StockService stockSvc = new StockService();
    private final ObservableList<Trade> data = FXCollections.observableArrayList();

    @FXML private TableView<Trade> table;
    @FXML private TableColumn<Trade,Integer>    colId;
    @FXML private TableColumn<Trade,LocalDate>  colDate;
    @FXML private TableColumn<Trade,String>     colPortfolio;
    @FXML private TableColumn<Trade,String>     colStock;
    @FXML private TableColumn<Trade,Integer>    colQty;
    @FXML private TableColumn<Trade,Double>     colPrice;

    @Override
    public void initData(User user) {
        this.currentUser = user;
        colId.setCellValueFactory(new PropertyValueFactory<>("tradeId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("tradeDate"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("tradeQty"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        colPortfolio.setCellValueFactory(cell -> {
            try {
                int pid = cell.getValue().getPortfolioId();
                List<Portfolio> ports = portSvc.getUserPortfolios(currentUser.getUserId());
                Portfolio p = ports.stream()
                        .filter(x -> x.getPortfolioId() == pid)
                        .findFirst()
                        .orElse(null);
                return new ReadOnlyStringWrapper(p != null ? p.getName() : "");
            } catch (AppException e) {
                return new ReadOnlyStringWrapper("");
            }
        });

        colStock.setCellValueFactory(cell -> {
            try {
                int sid = cell.getValue().getStockId();
                List<Stock> stocks = stockSvc.getAllStocks();
                Stock s = stocks.stream()
                        .filter(x -> x.getStockId() == sid)
                        .findFirst()
                        .orElse(null);
                return new ReadOnlyStringWrapper(s != null ? s.getName() : "");
            } catch (AppException e) {
                return new ReadOnlyStringWrapper("");
            }
        });

        table.setItems(data);
        loadData();
    }

    private void loadData() {
        try {
            List<Trade> list = tradeSvc.getUserTrades(currentUser.getUserId());
            data.setAll(list);
        } catch (AppException e) {
            alert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML private void onRefresh() {
        loadData();
    }

    @FXML private void onAddTrade() {
        try {
            // 1) choose portfolio
            List<Portfolio> ports = portSvc.getUserPortfolios(currentUser.getUserId());
            ChoiceDialog<Portfolio> portDlg = new ChoiceDialog<>(ports.get(0), ports);
            portDlg.setHeaderText("Select Portfolio:");
            Optional<Portfolio> pOpt = portDlg.showAndWait();
            if (pOpt.isEmpty()) return;

            // 2) choose stock
            List<Stock> stocks = stockSvc.getAllStocks();
            ChoiceDialog<Stock> stockDlg = new ChoiceDialog<>(stocks.get(0), stocks);
            stockDlg.setHeaderText("Select Stock:");
            Optional<Stock> sOpt = stockDlg.showAndWait();
            if (sOpt.isEmpty()) return;

            // 3) quantity
            TextInputDialog qtyDlg = new TextInputDialog();
            qtyDlg.setHeaderText("Enter Quantity:");
            Optional<String> qtyStr = qtyDlg.showAndWait();
            if (qtyStr.isEmpty()) return;
            int qty = Integer.parseInt(qtyStr.get());

            // 4) price
            TextInputDialog priceDlg = new TextInputDialog();
            priceDlg.setHeaderText("Enter Price:");
            Optional<String> priceStr = priceDlg.showAndWait();
            if (priceStr.isEmpty()) return;
            double price = Double.parseDouble(priceStr.get());

            // build & save
            Trade t = new Trade();
            t.setUserId(currentUser.getUserId());
            t.setPortfolioId(pOpt.get().getPortfolioId());
            t.setStockId(sOpt.get().getStockId());
            t.setTradeQty(qty);
            t.setPrice(price);
            t.setTradeDate(LocalDate.now());

            tradeSvc.placeTrade(t);
            loadData();
        } catch (AppException ex) {
            alert("Error", ex.getMessage(), Alert.AlertType.ERROR);
        } catch (NumberFormatException nf) {
            alert("Input Error", "Invalid number format", Alert.AlertType.ERROR);
        }
    }

    private void alert(String header, String text, Alert.AlertType type) {
        Alert a = new Alert(type, text, ButtonType.OK);
        a.setHeaderText(header);
        a.showAndWait();
    }
}
