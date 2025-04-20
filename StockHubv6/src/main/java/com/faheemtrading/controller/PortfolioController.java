package com.faheemtrading.controller;

import com.faheemtrading.model.Portfolio;
import com.faheemtrading.service.PortfolioService;
import com.faheemtrading.util.AlertUtil;
import com.faheemtrading.util.PdfUtil;
import com.faheemtrading.util.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PortfolioController {
    @FXML private TextField pfNameField;
    @FXML private TableView<Portfolio> pfTable;
    @FXML private TableColumn<Portfolio,Integer> colId;
    @FXML private TableColumn<Portfolio,String> colName;
    @FXML private TableColumn<Portfolio,Double> colVal;

    private final PortfolioService svc=new PortfolioService();
    private ObservableList<Portfolio> data= FXCollections.observableArrayList();

    @FXML
    public void initialize(){
        colId.setCellValueFactory(c-> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getPortfolioId()));
        colName.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getName()));
        colVal.setCellValueFactory(c-> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getTotalValue()));
        pfTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        refresh();
    }

    private void refresh(){
        data.setAll(svc.userPortfolios(Session.getUser().getUserId()));
        pfTable.setItems(data);
    }

    @FXML
    private void createPortfolio(){
        String name=pfNameField.getText();
        if(name.isBlank()){ AlertUtil.error("Empty","Enter a name"); return; }
        if(svc.createPortfolio(name)){
            AlertUtil.info("Success","Portfolio created");
            pfNameField.clear();
            refresh();
        }
    }

    @FXML
    private void deleteSelected(){
        Portfolio p=pfTable.getSelectionModel().getSelectedItem();
        if(p==null) return;
        if(svc.deletePortfolio(p.getPortfolioId())){
            AlertUtil.info("Deleted","Portfolio removed");
            refresh();
        }
    }

    @FXML
    private void openTrades(){
        Portfolio p=pfTable.getSelectionModel().getSelectedItem();
        if(p==null){ AlertUtil.error("Select","Choose a portfolio"); return; }
        try{
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/fxml/trade.fxml"));
            Node view=loader.load();
            TradeController tc=loader.getController();
            tc.setPortfolio(p);
            StackPane root=(StackPane)pfTable.getScene().lookup("#contentPane");
            root.getChildren().setAll(view);
        }catch(Exception e){e.printStackTrace();}
    }

    @FXML private void exportCsv(){
        try{
            javafx.stage.FileChooser fc=new javafx.stage.FileChooser();
            fc.setInitialFileName("portfolios.csv");
            java.nio.file.Path pth=fc.showSaveDialog(pfTable.getScene().getWindow()).toPath();
            java.util.List<String[]> rows=new java.util.ArrayList<>();
            rows.add(new String[]{"ID","Name","Value"});
            for(var pf:data){
                rows.add(new String[]{String.valueOf(pf.getPortfolioId()),pf.getName(),
                        String.valueOf(pf.getTotalValue())});
            }
            com.faheemtrading.util.CsvUtil.write(pth,rows);
            com.faheemtrading.util.AlertUtil.info("Exported","CSV saved");
        }catch(Exception ex){ex.printStackTrace();}
    }

    @FXML private void exportPdf(){
        try{
            javafx.stage.FileChooser fc=new javafx.stage.FileChooser();
            fc.setInitialFileName("portfolios.pdf");
            File f=fc.showSaveDialog(pfTable.getScene().getWindow());
            if(f==null) return;
            List<String[]> rows=new ArrayList<>();
            rows.add(new String[]{"ID","Name","Value"});
            for(var pf:data) rows.add(new String[]{
                    String.valueOf(pf.getPortfolioId()),pf.getName(),String.valueOf(pf.getTotalValue())});
            PdfUtil.exportTable(f,"Portfolios",rows);
            AlertUtil.info("Exported","PDF saved");
        }catch(Exception e){e.printStackTrace();}
    }

}
