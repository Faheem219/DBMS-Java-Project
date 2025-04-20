package com.faheemtrading.controller;

import com.faheemtrading.dao.AlertsDAO;
import com.faheemtrading.dao.impl.AlertsDAOImpl;
import com.faheemtrading.exception.AppException;
import com.faheemtrading.model.AlertRecord;
import com.faheemtrading.util.AlertUtil;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalTime;

public class AlertsController {

    @FXML private TableView<AlertRecord> alertTable;
    @FXML private TableColumn<AlertRecord,Integer> colId;
    @FXML private TableColumn<AlertRecord,String> colType;
    @FXML private TableColumn<AlertRecord,Double> colThr;
    @FXML private TableColumn<AlertRecord,LocalTime> colTime;
    @FXML private TableColumn<AlertRecord,Integer> colStock;

    private final AlertsDAO dao = new AlertsDAOImpl();
    private final ObservableList<AlertRecord> data = FXCollections.observableArrayList();

    @FXML
    private void initialize(){
        colId   .setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getId()));
        colType .setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getType()));
        colThr  .setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getThreshold()));
        colTime .setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getTime()));
        colStock.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getStockId()));
        alertTable.setItems(data);
        refresh();
    }

    @FXML
    private void refresh(){
        data.clear();
        try { data.addAll(dao.unread()); }
        catch (Exception e){ AlertUtil.error("DB",e.getMessage()); }
        MainController.updateBadge(data.size());         // notify sidebar badge
    }

    @FXML
    private void markRead(){
        AlertRecord sel = alertTable.getSelectionModel().getSelectedItem();
        if(sel==null){ AlertUtil.info("Select","Choose an alert"); return; }
        try { dao.markRead(sel.getId()); refresh(); }
        catch (Exception e){ AlertUtil.error("Mark read failed",e.getMessage()); }
    }
}
