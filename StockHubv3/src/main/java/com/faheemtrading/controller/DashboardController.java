package com.faheemtrading.controller;

import com.faheemtrading.dao.PortfolioDAO;
import com.faheemtrading.dao.TradeDAO;
import com.faheemtrading.dao.impl.PortfolioDAOImpl;
import com.faheemtrading.dao.impl.TradeDAOImpl;
import com.faheemtrading.exception.AppException;
import com.faheemtrading.model.Portfolio;
import com.faheemtrading.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.chart.XYChart.Series;
import java.util.Map;

public class DashboardController {

    @FXML private PieChart allocationChart;
    @FXML private BarChart<String,Number> volumeChart;

    private final PortfolioDAO portfolioDAO = new PortfolioDAOImpl();
    private final TradeDAO tradeDAO         = new TradeDAOImpl();

    @FXML
    private void initialize() {
        loadAllocationPie();
        loadVolumeBar();
    }

    private void loadAllocationPie(){
        try {
            allocationChart.getData().clear();
            for (Portfolio p : portfolioDAO.findAll()){
                allocationChart.getData().add(
                        new PieChart.Data(p.getName(), p.getTotalValue())
                );
            }
        } catch (AppException e){
            AlertUtil.error("Chart Error", e.getMessage());
        }
    }

    private void loadVolumeBar(){
        try {
            volumeChart.getData().clear();
            Series<String,Number> s = new Series<>();
            s.setName("Trade Volume");
            Map<Integer,Integer> map = tradeDAO.aggregateVolume();
            map.forEach((stockId, qty) ->
                    s.getData().add(new XYChart.Data<>(stockId.toString(), qty))
            );
            volumeChart.getData().add(s);
        } catch (Exception e){
            AlertUtil.error("Chart Error", e.getMessage());
        }
    }
}
