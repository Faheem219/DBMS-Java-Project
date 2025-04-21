package com.faheemtrading.controller;

import com.faheemtrading.dao.stock.StockDAOImpl;
import com.faheemtrading.model.*;
import com.faheemtrading.service.PortfolioService;
import com.faheemtrading.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;

public class TradeController {
    @FXML private Label pfLabel;
    @FXML private ChoiceBox<Stock> stockChoice;
    @FXML private TextField qtyField;
    @FXML private ChoiceBox<String> typeChoice;
    @FXML private TableView<AbstractTrade> tradeTable;
    @FXML private TableColumn<AbstractTrade,Integer> tId;
    @FXML private TableColumn<AbstractTrade,String> tStock;
    @FXML private TableColumn<AbstractTrade,Integer> tQty;
    @FXML private TableColumn<AbstractTrade,Double> tPrice;
    @FXML private TableColumn<AbstractTrade,LocalDate> tDate;

    private Portfolio portfolio;
    private final PortfolioService svc=new PortfolioService();
    private ObservableList<AbstractTrade> trades= FXCollections.observableArrayList();

    public void setPortfolio(Portfolio p){
        this.portfolio=p;
        pfLabel.setText("Portfolio: "+p.getName());
        loadTrades();
    }

    @FXML
    public void initialize(){
        stockChoice.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(Stock s){ return s==null?"":s.getName()+" ("+s.getStockId()+")"; }
            @Override public Stock fromString(String s){ return null; }
        });
        stockChoice.getItems().setAll(new StockDAOImpl().getAll());
        typeChoice.getItems().addAll("Buy","Sell");
        typeChoice.setValue("Buy");

        tId.setCellValueFactory(c-> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getTradeId()));
        tStock.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getStockId()+""));
        tQty.setCellValueFactory(c-> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getQuantity()));
        tPrice.setCellValueFactory(c-> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getPrice()));
        tDate.setCellValueFactory(c-> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getTradeDate()));
        tradeTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    }

    private void loadTrades(){
        List<AbstractTrade> l=svc.trades(portfolio.getPortfolioId());
        trades.setAll(l);
        tradeTable.setItems(trades);
    }

    @FXML
    private void makeTrade(){
        Stock s=stockChoice.getValue();
        if(s==null){ AlertUtil.error("Select","Choose stock"); return; }
        int qty;
        try{ qty=Integer.parseInt(qtyField.getText()); }catch(Exception e){ AlertUtil.error("Qty","Invalid"); return; }
        double price=s.getMarketPrice();
        int nextId=(int)(System.currentTimeMillis()%1_000_000);
        AbstractTrade trade= typeChoice.getValue().equals("Buy") ?
                new BuyTrade(nextId,price,qty,LocalDate.now(),s.getStockId(),portfolio.getPortfolioId())
                : new SellTrade(nextId,price,qty,LocalDate.now(),s.getStockId(),portfolio.getPortfolioId());

        if(svc.executeTrade(trade)){
            AlertUtil.info("Done","Trade executed");
            qtyField.clear();
            loadTrades();   // table refresh
        }else AlertUtil.error("Error","DB operation failed");
    }
}
