package com.stockapp.controller;

import com.stockapp.model.Portfolio;
import com.stockapp.model.User;
import com.stockapp.service.PortfolioService;
import com.stockapp.util.AppException;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

public class PortfolioController implements UserAware {
    private User currentUser;
    private final PortfolioService svc = new PortfolioService();
    private final ObservableList<Portfolio> data = FXCollections.observableArrayList();

    @FXML private TableView<Portfolio> table;
    @FXML private TableColumn<Portfolio,Integer> colId;
    @FXML private TableColumn<Portfolio,String> colName;
    @FXML private TableColumn<Portfolio,LocalDate> colDate;
    @FXML private TableColumn<Portfolio,Double> colValue;

    @Override
    public void initData(User user) {
        this.currentUser = user;
        colId.setCellValueFactory(new PropertyValueFactory<>("portfolioId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        colValue.setCellValueFactory(new PropertyValueFactory<>("totalValue"));
        table.setItems(data);
        loadData();
    }

    private void loadData() {
        try {
            List<Portfolio> list = svc.getUserPortfolios(currentUser.getUserId());
            data.setAll(list);
        } catch (AppException e) {
            alert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML private void onRefresh() { loadData(); }

    @FXML private void onAdd() {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setHeaderText("New Portfolio Name:");
        dlg.setContentText("Name:");
        dlg.showAndWait().ifPresent(name -> {
            Portfolio p = new Portfolio();
            p.setName(name);
            p.setCreationDate(LocalDate.now());
            p.setTotalValue(0);
            p.setUserId(currentUser.getUserId());
            try {
                svc.addPortfolio(p);
                loadData();
            } catch (AppException ex) {
                alert("Error", ex.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    @FXML private void onEdit() {
        Portfolio sel = table.getSelectionModel().getSelectedItem();
        if (sel==null) { alert("Info","Select one",Alert.AlertType.INFORMATION); return; }
        TextInputDialog dlg = new TextInputDialog(sel.getName());
        dlg.setHeaderText("Edit Name:");
        dlg.setContentText("Name:");
        dlg.showAndWait().ifPresent(name -> {
            sel.setName(name);
            try {
                svc.updatePortfolio(sel);
                loadData();
            } catch (AppException ex) {
                alert("Error", ex.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    @FXML private void onDelete() {
        Portfolio sel = table.getSelectionModel().getSelectedItem();
        if (sel==null) { alert("Info","Select one",Alert.AlertType.INFORMATION); return; }
        Alert c = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete '"+sel.getName()+"'?", ButtonType.YES, ButtonType.NO);
        c.showAndWait().ifPresent(b -> {
            if (b==ButtonType.YES) {
                try {
                    svc.deletePortfolio(sel.getPortfolioId());
                    loadData();
                } catch (AppException ex) {
                    alert("Error", ex.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    private void alert(String h, String t, Alert.AlertType type) {
        Alert a = new Alert(type,t,ButtonType.OK);
        a.setHeaderText(h);
        a.showAndWait();
    }
}
