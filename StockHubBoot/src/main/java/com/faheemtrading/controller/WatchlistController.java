package com.faheemtrading.controller;

import com.faheemtrading.dao.stock.StockDAOImpl;
import com.faheemtrading.model.Stock;
import com.faheemtrading.model.Watchlist;
import com.faheemtrading.service.WatchlistService;
import com.faheemtrading.util.AlertUtil;
import com.faheemtrading.util.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class WatchlistController {

    /* ---------- FXML ---------- */
    @FXML private TextField nameField, notesField;
    @FXML private ChoiceBox<Stock> stockChoice;
    @FXML private TableView<Watchlist> watchTable;
    @FXML private TableColumn<Watchlist,Integer> wId;
    @FXML private TableColumn<Watchlist,String>  wName,wStock,wNotes,wDate;
    @FXML private Button addBtn;

    /* ---------- members ---------- */
    private final WatchlistService svc = new WatchlistService();
    private ObservableList<Watchlist> data = FXCollections.observableArrayList();
    private Watchlist editing = null;

    @FXML public void initialize() {

        /* ChoiceBox render */
        stockChoice.setConverter(new StringConverter<>() {
            @Override public String toString(Stock s){ return s==null?"":s.getName(); }
            @Override public Stock fromString(String s){ return null; }
        });
        stockChoice.getItems().setAll(new StockDAOImpl().getAll());

        /* Table columns */
        wId   .setCellValueFactory(c-> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getWatchlistId()));
        wName .setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getName()));
        wStock.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getStockName()));
        wNotes.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getNotes()));
        wDate .setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getAddedDate().format(DateTimeFormatter.ISO_DATE)));

        refresh();
    }

    private void refresh() {
        List<Watchlist> l = svc.userWatchlists(Session.getUser().getUserId());
        data.setAll(l);
        watchTable.setItems(data);
    }

    /* -------- create | save -------- */
    @FXML private void addOrSave() {
        Stock s = stockChoice.getValue();
        if (s == null) { AlertUtil.error("Pick stock",""); return; }

        if (editing == null) {             // CREATE
            boolean ok = svc.create(
                    nameField.getText(), notesField.getText(),
                    s.getStockId(), Session.getUser().getUserId());
            if (ok) AlertUtil.info("Added","Watchlist created");
        } else {                           // UPDATE
            editing.setName(nameField.getText());
            editing.setNotes(notesField.getText());
            editing.setStockId(s.getStockId());
            editing.setStockName(s.getName());
            if (svc.update(editing)) AlertUtil.info("Saved","Watchlist updated");
            editing = null;
            addBtn.setText("Add");
        }
        clear(); refresh();
    }

    /* -------- start editing -------- */
    @FXML private void startEdit() {
        Watchlist w = watchTable.getSelectionModel().getSelectedItem();
        if (w == null) return;
        editing = w;
        nameField.setText(w.getName());
        notesField.setText(w.getNotes());
        stockChoice.setValue(
                stockChoice.getItems().stream()
                        .filter(st->st.getStockId()==w.getStockId()).findFirst().orElse(null));
        addBtn.setText("Save");
    }

    @FXML private void cancelEdit() { editing = null; clear(); addBtn.setText("Add"); }

    /* -------- delete -------- */
    @FXML private void deleteSelected() {
        Watchlist w = watchTable.getSelectionModel().getSelectedItem();
        if (w==null) return;
        if (svc.delete(w.getWatchlistId())) {
            AlertUtil.info("Deleted","Watchlist removed"); refresh();
        }
    }

    private void clear() { nameField.clear(); notesField.clear(); stockChoice.setValue(null); }
}
