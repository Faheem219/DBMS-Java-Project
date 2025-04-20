package com.stockapp.controller;

import com.stockapp.model.Stock;
import com.stockapp.model.User;
import com.stockapp.model.Watchlist;
import com.stockapp.service.StockService;
import com.stockapp.service.WatchlistService;
import com.stockapp.util.AppException;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class WatchlistController implements UserAware {
    private User currentUser;
    private final WatchlistService svc = new WatchlistService();
    private final StockService stockSvc = new StockService();
    private final ObservableList<Watchlist> data = FXCollections.observableArrayList();

    @FXML private TableView<Watchlist> table;
    @FXML private TableColumn<Watchlist,Integer>    colId;
    @FXML private TableColumn<Watchlist,String>     colName;
    @FXML private TableColumn<Watchlist,String>     colNotes;
    @FXML private TableColumn<Watchlist,String>     colStock;
    @FXML private TableColumn<Watchlist,LocalDate>  colDate;

    @Override
    public void initData(User user) {
        this.currentUser = user;
        // Use PropertyValueFactory for simple getters:
        colId.setCellValueFactory(new PropertyValueFactory<>("watchlistId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colNotes.setCellValueFactory(new PropertyValueFactory<>("notes"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("addedDate"));

        // Stock name via a lambda
        colStock.setCellValueFactory(cell -> {
            try {
                int sid = cell.getValue().getStockId();
                Stock s = stockSvc.getAllStocks()
                        .stream()
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
            List<Watchlist> list = svc.getUserWatchlist(currentUser.getUserId());
            data.setAll(list);
        } catch (AppException e) {
            alert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML private void onRefresh() { loadData(); }

    @FXML private void onDelete() {
        Watchlist sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) {
            alert("Info", "Select one", Alert.AlertType.INFORMATION);
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete '" + sel.getName() + "'?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                try {
                    svc.deleteWatchlist(sel.getWatchlistId());
                    loadData();
                } catch (AppException ex) {
                    alert("Error", ex.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    @FXML private void onAdd() {
        // Name dialog
        TextInputDialog nameDlg = new TextInputDialog();
        nameDlg.setHeaderText("New Watchlist Name:");
        Optional<String> nameOpt = nameDlg.showAndWait();
        if (nameOpt.isEmpty() || nameOpt.get().isBlank()) return;

        // Notes dialog
        TextInputDialog notesDlg = new TextInputDialog();
        notesDlg.setHeaderText("Notes (optional):");
        String notes = notesDlg.showAndWait().orElse("");

        try {
            // Choose stock
            List<Stock> stocks = stockSvc.getAllStocks();
            ChoiceDialog<Stock> stockDlg = new ChoiceDialog<>(stocks.get(0), stocks);
            stockDlg.setHeaderText("Select Stock:");
            Optional<Stock> sOpt = stockDlg.showAndWait();
            if (sOpt.isEmpty()) return;

            // Build and save
            Watchlist w = new Watchlist();
            w.setName(nameOpt.get());
            w.setNotes(notes);
            w.setAddedDate(LocalDate.now());
            w.setStockId(sOpt.get().getStockId());
            w.setUserId(currentUser.getUserId());
            svc.addWatchlist(w);
            loadData();
        } catch (AppException e) {
            alert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void alert(String header, String text, Alert.AlertType type) {
        Alert a = new Alert(type, text, ButtonType.OK);
        a.setHeaderText(header);
        a.showAndWait();
    }
}
