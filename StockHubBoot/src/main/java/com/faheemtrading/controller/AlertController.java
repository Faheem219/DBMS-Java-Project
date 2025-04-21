package com.faheemtrading.controller;

import com.faheemtrading.dao.alert.AlertDAOImpl;
import com.faheemtrading.model.Alert;
import com.faheemtrading.util.Session;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AlertController {
    @FXML private TableView<Alert> alertTable;
    @FXML private TableColumn<Alert,Integer> aId;
    @FXML private TableColumn<Alert,String> aType;
    @FXML private TableColumn<Alert,Double> aThr;
    @FXML private TableColumn<Alert,String> aTime;
    @FXML private TableColumn<Alert,Integer> aStock;

    @FXML public void initialize(){
        aId.setCellValueFactory(c-> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getAlertId()));
        aType.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getAlertType()));
        aThr.setCellValueFactory(c-> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getThreshold()));
        aTime.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getAlertTime().toString()));
        aStock.setCellValueFactory(c-> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getStockId()));

        var alerts=new AlertDAOImpl().findByUser(Session.getUser().getUserId());
        alertTable.setItems(FXCollections.observableArrayList(alerts));
    }
}
