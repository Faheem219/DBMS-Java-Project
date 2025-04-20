package com.faheemtrading.controller;

import com.faheemtrading.dao.PortfolioDAO;
import com.faheemtrading.dao.impl.PortfolioDAOImpl;
import com.faheemtrading.exception.AppException;
import com.faheemtrading.model.Portfolio;
import com.faheemtrading.util.AlertUtil;
import com.faheemtrading.util.DBConnection;
import com.faheemtrading.util.ReportUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PortfolioController {

    @FXML private TableView<Portfolio> portfolioTable;
    @FXML private TableColumn<Portfolio,Integer> colId;
    @FXML private TableColumn<Portfolio,String> colName;
    @FXML private TableColumn<Portfolio,LocalDate> colDate;
    @FXML private TableColumn<Portfolio,Double> colValue;

    private final PortfolioDAO dao = new PortfolioDAOImpl();
    private final ObservableList<Portfolio> data = FXCollections.observableArrayList();

    @FXML
    private void initialize(){
        colId   .setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getId()));
        colName .setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getName()));
        colDate .setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getCreationDate()));
        colValue.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getTotalValue()));

        portfolioTable.setItems(data);
        refresh();
    }

    @FXML
    private void refresh(){
        data.clear();
        try { data.addAll(dao.findAll()); }
        catch (AppException e){ AlertUtil.error("DB Error", e.getMessage()); }
    }

    @FXML
    private void addPortfolio(){
        Portfolio p = promptPortfolio(null);
        if (p != null){
            try { dao.save(p); refresh(); }
            catch (AppException e){ AlertUtil.error("Insert failed", e.getMessage()); }
        }
    }

    @FXML
    private void editPortfolio(){
        Portfolio selected = portfolioTable.getSelectionModel().getSelectedItem();
        if (selected==null){ AlertUtil.info("No Selection", "Select a portfolio first"); return; }
        Portfolio updated = promptPortfolio(selected);
        if (updated != null){
            try { dao.save(updated); refresh(); }
            catch (AppException e){ AlertUtil.error("Update failed", e.getMessage()); }
        }
    }

    @FXML
    private void deletePortfolio(){
        Portfolio selected = portfolioTable.getSelectionModel().getSelectedItem();
        if (selected==null){ AlertUtil.info("No Selection", "Select a portfolio first"); return; }
        try {
            dao.delete(selected.getId());
            refresh();
        } catch (AppException e){ AlertUtil.error("Delete failed", e.getMessage()); }
    }

    @FXML
    private void exportReport(){
        Portfolio sel = portfolioTable.getSelectionModel().getSelectedItem();
        if(sel==null){ AlertUtil.info("Select","Choose a portfolio"); return; }

        FileChooser fc = new FileChooser();
        fc.setInitialFileName(sel.getName().replace(" ","_")+"_report.pdf");
        File f = fc.showSaveDialog(portfolioTable.getScene().getWindow());
        if(f==null) return;

        try{
            // very light query – fetch Portfolio_Stock rows
            String sql = "SELECT Stock_ID,Quantity,Purchase_Price,Current_Value FROM Portfolio_Stock WHERE Portfolio_ID="+sel.getId();
            List<String[]> rows = new ArrayList<>();
            try (Statement st = DBConnection.get().createStatement();
                 ResultSet rs = st.executeQuery(sql)){
                while(rs.next()){
                    rows.add(new String[]{
                            rs.getInt(1)+"", rs.getInt(2)+"", rs.getDouble(3)+"", rs.getDouble(4)+""
                    });
                }
            }
            ReportUtil.portfolioPdf(f.getAbsolutePath(), sel, rows);
            AlertUtil.info("Report", "Saved to "+f.getAbsolutePath());
        }catch(Exception e){ AlertUtil.error("Report failed",e.getMessage()); }
    }

    /* ----- Simple TextInputDialog for demo purposes ----- */
    private Portfolio promptPortfolio(Portfolio existing){
        TextInputDialog dlg = new TextInputDialog(existing!=null?existing.getName():"");
        dlg.setHeaderText("Portfolio Name");
        dlg.setContentText("Enter name:");
        var res = dlg.showAndWait();
        if (res.isEmpty()) return null;

        String name = res.get().trim();
        if (name.isEmpty()){ AlertUtil.info("Validation","Name cannot be empty"); return null; }

        if (existing==null){
            Portfolio p = new Portfolio();
            p.setId((int)(System.currentTimeMillis()%1_000_000)); // quick random ID for demo
            p.setName(name);
            p.setCreationDate(LocalDate.now());
            p.setTotalValue(0);
            p.setUserId(1); // for demo, assign to first user
            return p;
        } else {
            existing.setName(name);
            return existing;
        }
    }
}
