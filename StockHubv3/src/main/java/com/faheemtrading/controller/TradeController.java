package com.faheemtrading.controller;

import com.faheemtrading.dao.TradeDAO;
import com.faheemtrading.dao.impl.TradeDAOImpl;
import com.faheemtrading.exception.AppException;
import com.faheemtrading.model.BuyTrade;
import com.faheemtrading.model.SellTrade;
import com.faheemtrading.model.Trade;
import com.faheemtrading.model.enums.TradeType;
import com.faheemtrading.util.AlertUtil;
import com.faheemtrading.util.CsvUtil;
import com.faheemtrading.util.PdfUtil;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class TradeController {

    @FXML private TableView<Trade> tradeTable;
    @FXML private TableColumn<Trade,Integer> colId;
    @FXML private TableColumn<Trade,String>  colType;
    @FXML private TableColumn<Trade,LocalDate> colDate;
    @FXML private TableColumn<Trade,Integer> colQty;
    @FXML private TableColumn<Trade,Double>  colPrice;
    @FXML private TableColumn<Trade,Double>  colValue;
    @FXML private TableColumn<Trade,Integer> colStock;
    @FXML private ChoiceBox<String> filterChoice;

    private final TradeDAO dao = new TradeDAOImpl();
    private final ObservableList<Trade> data = FXCollections.observableArrayList();

    @FXML
    private void initialize(){
        colId   .setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getId()));
        colType .setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getType()));
        colDate .setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getTradeDate()));
        colQty  .setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getQuantity()));
        colPrice.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getPrice()));
        colValue.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getTradeValue()));
        colStock.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getStockId()));

        tradeTable.setItems(data);

        filterChoice.getItems().addAll("ALL","BUY","SELL");
        filterChoice.setValue("ALL");
        filterChoice.getSelectionModel().selectedItemProperty().addListener((obs,o,n)->refresh());

        refresh();
    }

    @FXML
    private void refresh(){
        data.clear();
        try{
            switch(filterChoice.getValue()){
                case "BUY" -> data.addAll(dao.findByType(TradeType.BUY));
                case "SELL"-> data.addAll(dao.findByType(TradeType.SELL));
                default     -> data.addAll(dao.findAll());
            }
        }catch(Exception e){ AlertUtil.error("DB Error",e.getMessage()); }
    }

    @FXML
    private void addTrade(){ showTradeDialog(null); }

    @FXML
    private void editTrade(){
        Trade sel = tradeTable.getSelectionModel().getSelectedItem();
        if(sel==null){ AlertUtil.info("Select","Choose a trade first"); return; }
        showTradeDialog(sel);
    }

    @FXML
    private void deleteTrade(){
        Trade sel = tradeTable.getSelectionModel().getSelectedItem();
        if(sel==null){ AlertUtil.info("Select","Choose a trade first"); return; }
        try{
            dao.delete(sel.getId()); refresh();
        }catch(AppException e){ AlertUtil.error("Delete failed",e.getMessage()); }
    }

    /* --------- Export --------- */
    @FXML
    private void exportData() {
        DialogPane pane;
        ExportController ctrl;
        try {
            // 1) Instantiate FXMLLoader and point it at your FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/export_dialog.fxml"));
            // 2) Load the DialogPane
            pane = loader.load();
            // 3) Retrieve the controller from the loader
            ctrl = loader.getController();
        } catch (IOException e) {
            AlertUtil.error("UI", e.getMessage());
            return;
        }

        // 4) Build and show your Dialog
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setDialogPane(pane);
        dlg.setTitle("Export Trades");

        Optional<ButtonType> res = dlg.showAndWait();
        if (res.isEmpty() || res.get() != ctrl.getOkButton()) return;

        String fmt = ctrl.getFormat();
        FileChooser chooser = new FileChooser();
        chooser.setInitialFileName("trades." + fmt.toLowerCase());
        File f = chooser.showSaveDialog(tradeTable.getScene().getWindow());
        if (f == null) return;

        try {
            if (fmt.equals("CSV")) {
                List<String[]> rows = new ArrayList<>();
                rows.add(new String[]{"ID","Type","Date","Qty","Price","Value","Stock"});
                data.forEach(t -> rows.add(new String[]{
                        t.getId()+"", t.getType(), t.getTradeDate()+"", t.getQuantity()+"",
                        t.getPrice()+"", t.getTradeValue()+"", t.getStockId()+""
                }));
                CsvUtil.write(f.getAbsolutePath(), rows);
            } else {
                List<String> headers = List.of("ID","Type","Date","Qty","Price","Value","Stock");
                List<List<String>> rows = new ArrayList<>();
                data.forEach(t -> rows.add(List.of(
                        t.getId()+"", t.getType(), t.getTradeDate()+"", t.getQuantity()+"",
                        t.getPrice()+"", t.getTradeValue()+"", t.getStockId()+""
                )));
                PdfUtil.table(f.getAbsolutePath(), "Trades Export", headers, rows);
            }
            AlertUtil.info("Exported", "File saved to " + f.getAbsolutePath());
        } catch (Exception e) {
            AlertUtil.error("Export failed", e.getMessage());
        }
    }


    /* --------- Trade dialog (quick inline prompts for demo) --------- */
    private void showTradeDialog(Trade existing){
        Dialog<Trade> dlg = new Dialog<>();
        dlg.setTitle(existing==null?"New Trade":"Edit Trade");

        GridPane gp = new GridPane(); gp.setHgap(10); gp.setVgap(10);
        ChoiceBox<String> type = new ChoiceBox<>(FXCollections.observableArrayList("BUY","SELL"));
        DatePicker date  = new DatePicker(LocalDate.now());
        TextField qty    = new TextField();
        TextField price  = new TextField();
        TextField stock  = new TextField();
        TextField fee    = new TextField(); // broker fee or sell charges label later

        gp.addRow(0, new Label("Type:"), type);
        gp.addRow(1, new Label("Date:"), date);
        gp.addRow(2, new Label("Quantity:"), qty);
        gp.addRow(3, new Label("Price:"), price);
        gp.addRow(4, new Label("Stock ID:"), stock);
        gp.addRow(5, new Label("Fee/Charges:"), fee);

        dlg.getDialogPane().setContent(gp);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        if(existing!=null){
            type.setValue(existing.getType());
            date.setValue(existing.getTradeDate());
            qty.setText(existing.getQuantity()+"");
            price.setText(existing.getPrice()+"");
            stock.setText(existing.getStockId()+"");
            fee.setText(existing instanceof BuyTrade ?
                    ((BuyTrade) existing).getBrokerFee()+"" :
                    ((SellTrade) existing).getSellCharges()+"");
        }

        dlg.setResultConverter(btn -> {
            if(btn==ButtonType.OK){
                try{
                    TradeType tp = TradeType.valueOf(type.getValue());
                    Trade t = (tp==TradeType.BUY)? new BuyTrade(getOrGenerateId(existing)):
                            new SellTrade(getOrGenerateId(existing));
                    t.setTradeDate(date.getValue());
                    t.setQuantity(Integer.parseInt(qty.getText()));
                    t.setPrice(Double.parseDouble(price.getText()));
                    t.setStockId(Integer.parseInt(stock.getText()));
                    t.setUserId(1);      // demo user
                    t.setPortfolioId(1); // demo portfolio
                    if(t instanceof BuyTrade) ((BuyTrade)t).setBrokerFee(Double.parseDouble(fee.getText()));
                    else ((SellTrade)t).setSellCharges(Double.parseDouble(fee.getText()));
                    return t;
                }catch(Exception ex){
                    AlertUtil.error("Validation","Please enter valid numeric values");
                }
            }
            return null;
        });

        Optional<Trade> result = dlg.showAndWait();
        result.ifPresent(tr -> {
            try{ dao.save(tr); refresh();}
            catch(AppException e){ AlertUtil.error("Save failed",e.getMessage()); }
        });
    }

    private int getOrGenerateId(Trade existing){
        return existing!=null ? existing.getId() : (int)(System.currentTimeMillis()%1_000_000);
    }
}
