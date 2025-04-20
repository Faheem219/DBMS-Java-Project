package com.faheemtrading.controller;

import com.faheemtrading.dao.stock.StockDAOImpl;
import com.faheemtrading.model.Stock;
import com.faheemtrading.model.Watchlist;
import com.faheemtrading.service.WatchlistService;
import com.faheemtrading.util.AlertUtil;
import com.faheemtrading.util.CsvUtil;
import com.faheemtrading.util.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class WatchlistController {
    @FXML private TextField nameField, notesField;
    @FXML private ChoiceBox<Stock> stockChoice;
    @FXML private TableView<Watchlist> watchTable;
    @FXML private TableColumn<Watchlist,Integer> wId;
    @FXML private TableColumn<Watchlist,String> wName;
    @FXML private TableColumn<Watchlist,String> wStock;
    @FXML private TableColumn<Watchlist,String> wNotes;
    @FXML private TableColumn<Watchlist,String> wDate;

    private final WatchlistService svc=new WatchlistService();
    private ObservableList<Watchlist> data= FXCollections.observableArrayList();

    @FXML public void initialize(){
        stockChoice.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(Stock s){ return s==null?"" : s.getName(); }
            @Override public Stock fromString(String str){ return null; }
        });
        stockChoice.getItems().setAll(new StockDAOImpl().getAll());

        wId.setCellValueFactory(c-> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getWatchlistId()));
        wName.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getName()));
        wStock.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getStockName()));
        wNotes.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getNotes()));
        wDate.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getAddedDate().format(DateTimeFormatter.ISO_DATE)));
        watchTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        refresh();
    }

    private void refresh(){
        data.setAll(svc.userWatchlists(Session.getUser().getUserId()));
        watchTable.setItems(data);
    }

    @FXML
    private void addWatchlist(){
        Stock s=stockChoice.getValue();
        if(s==null){ AlertUtil.error("Select","Pick stock"); return; }
        if(svc.create(nameField.getText(),notesField.getText(),s.getStockId())){
            AlertUtil.info("Added","Watchlist entry created");
            nameField.clear();notesField.clear();
            refresh();
        }
    }

    @FXML
    private void deleteSelected(){
        Watchlist w=watchTable.getSelectionModel().getSelectedItem();
        if(w!=null && svc.delete(w.getWatchlistId())){
            AlertUtil.info("Deleted","Removed");
            refresh();
        }
    }

    @FXML
    private void exportCsv(){
        try{
            FileChooser fc=new FileChooser();
            fc.setInitialFileName("watchlists.csv");
            Path path=fc.showSaveDialog(watchTable.getScene().getWindow()).toPath();
            List<String[]> rows=new ArrayList<>();
            rows.add(new String[]{"ID","Name","Stock","Notes","Date"});
            for(Watchlist w:data){
                rows.add(new String[]{
                        String.valueOf(w.getWatchlistId()),w.getName(),w.getStockName(),
                        w.getNotes(),w.getAddedDate().toString()});
            }
            CsvUtil.write(path,rows);
            AlertUtil.info("Exported","CSV saved");
        }catch(Exception e){e.printStackTrace();}
    }
}
