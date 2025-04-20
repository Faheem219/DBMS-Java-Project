package com.trader.stockhubv4.controller;

import com.trader.stockhubv4.dao.PortfolioDAO;
import com.trader.stockhubv4.dao.impl.PortfolioDAOImpl;
import com.trader.stockhubv4.exception.AppException;
import com.trader.stockhubv4.model.Portfolio;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PortfolioListController {
    @FXML private TableView<Portfolio> tablePortfolios;
    @FXML private TableColumn<Portfolio, Integer>    colPid;
    @FXML private TableColumn<Portfolio, String>     colPname;
    @FXML private TableColumn<Portfolio, LocalDate>  colPdate;
    @FXML private TableColumn<Portfolio, Double>     colPvalue;
    @FXML private TableColumn<Portfolio, Integer>    colPuser;

    private final PortfolioDAO dao = new PortfolioDAOImpl();

    @FXML
    public void initialize() {
        colPid.setCellValueFactory(d -> new javafx.beans.property.
                SimpleIntegerProperty(d.getValue().getPortfolioId()).asObject());
        colPname.setCellValueFactory(d -> new javafx.beans.property.
                SimpleStringProperty(d.getValue().getPortfolioName()));
        colPdate.setCellValueFactory(d -> new javafx.beans.property.
                SimpleObjectProperty<>(d.getValue().getCreationDate()));
        colPvalue.setCellValueFactory(d -> new javafx.beans.property.
                SimpleDoubleProperty(d.getValue().getTotalValue()).asObject());
        colPuser.setCellValueFactory(d -> new javafx.beans.property.
                SimpleIntegerProperty(d.getValue().getUserId()).asObject());
        onRefresh();
    }

    @FXML
    private void onAdd() {
        Dialog<Portfolio> dlg = new Dialog<>();
        dlg.setTitle("Add Portfolio");
        dlg.getDialogPane().getButtonTypes()
                .addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        TextField idF = new TextField();
        TextField nameF = new TextField();
        DatePicker dateF = new DatePicker(LocalDate.now());
        TextField valF = new TextField();
        TextField userF = new TextField();

        grid.addRow(0, new Label("ID:"), idF);
        grid.addRow(1, new Label("Name:"), nameF);
        grid.addRow(2, new Label("Date:"), dateF);
        grid.addRow(3, new Label("Value:"), valF);
        grid.addRow(4, new Label("User ID:"), userF);

        dlg.getDialogPane().setContent(grid);
        dlg.setResultConverter(bt -> {
            if (bt == ButtonType.OK) {
                Portfolio p = new Portfolio();
                p.setPortfolioId(Integer.parseInt(idF.getText()));
                p.setPortfolioName(nameF.getText());
                p.setCreationDate(dateF.getValue());
                p.setTotalValue(Double.parseDouble(valF.getText()));
                p.setUserId(Integer.parseInt(userF.getText()));
                return p;
            }
            return null;
        });

        Optional<Portfolio> res = dlg.showAndWait();
        res.ifPresent(p -> {
            try {
                dao.create(p);
                onRefresh();
            } catch (Exception e) {
                showError("Add failed: " + e.getMessage());
            }
        });
    }

    @FXML
    private void onEdit() {
        Portfolio sel = tablePortfolios.getSelectionModel().getSelectedItem();
        if (sel == null) { showError("Select one first"); return; }

        Dialog<Portfolio> dlg = new Dialog<>();
        dlg.setTitle("Edit Portfolio");
        dlg.getDialogPane().getButtonTypes()
                .addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        TextField idF = new TextField(String.valueOf(sel.getPortfolioId()));
        idF.setDisable(true);
        TextField nameF = new TextField(sel.getPortfolioName());
        DatePicker dateF = new DatePicker(sel.getCreationDate());
        TextField valF = new TextField(String.valueOf(sel.getTotalValue()));
        TextField userF = new TextField(String.valueOf(sel.getUserId()));

        grid.addRow(0, new Label("ID:"), idF);
        grid.addRow(1, new Label("Name:"), nameF);
        grid.addRow(2, new Label("Date:"), dateF);
        grid.addRow(3, new Label("Value:"), valF);
        grid.addRow(4, new Label("User ID:"), userF);

        dlg.getDialogPane().setContent(grid);
        dlg.setResultConverter(bt -> {
            if (bt == ButtonType.OK) {
                sel.setPortfolioName(nameF.getText());
                sel.setCreationDate(dateF.getValue());
                sel.setTotalValue(Double.parseDouble(valF.getText()));
                sel.setUserId(Integer.parseInt(userF.getText()));
                return sel;
            }
            return null;
        });

        Optional<Portfolio> res = dlg.showAndWait();
        res.ifPresent(p -> {
            try {
                dao.update(p);
                onRefresh();
            } catch (Exception e) {
                showError("Update failed: " + e.getMessage());
            }
        });
    }

    @FXML
    private void onDelete() {
        Portfolio sel = tablePortfolios.getSelectionModel().getSelectedItem();
        if (sel == null) { showError("Select one first"); return; }

        Alert c = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete “" + sel.getPortfolioName() + "”?",
                ButtonType.OK, ButtonType.CANCEL);
        c.showAndWait().ifPresent(b -> {
            if (b == ButtonType.OK) {
                try {
                    dao.delete(sel.getPortfolioId());
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
            List<Portfolio> all = dao.findAll();
            tablePortfolios.setItems(FXCollections.observableArrayList(all));
        } catch (Exception e) {
            showError("Load failed: " + e.getMessage());
        }
    }

    private void showError(String m) {
        new Alert(Alert.AlertType.ERROR, m, ButtonType.OK)
                .showAndWait();
    }
}
