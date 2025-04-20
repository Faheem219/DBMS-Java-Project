package com.trader.stockhubv4.controller;

import com.trader.stockhubv4.dao.PortfolioDAO;
import com.trader.stockhubv4.dao.TradeDAO;
import com.trader.stockhubv4.dao.impl.PortfolioDAOImpl;
import com.trader.stockhubv4.dao.impl.TradeDAOImpl;
import com.trader.stockhubv4.exception.AppException;
import com.trader.stockhubv4.model.Portfolio;
import com.trader.stockhubv4.model.Trade;
import com.trader.stockhubv4.util.ReportUtil;
import com.trader.stockhubv4.util.Session;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.sql.SQLException;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class DashboardController {
    @FXML private PieChart pieChartAssets;
    @FXML private BarChart<String, Number> barChartTrades;

    private final PortfolioDAO portfolioDao = new PortfolioDAOImpl();
    private final TradeDAO tradeDao       = new TradeDAOImpl();

    @FXML
    public void initialize() {
        loadAssetDistribution();
        loadTradesPerMonth();
    }

    private void loadAssetDistribution() {
        try {
            int uid = Session.getCurrentUser().getUserId();
            List<Portfolio> all = portfolioDao.findAll();
            List<Portfolio> mine = all.stream()
//                    .filter(p->p.getUserId()==uid)
                    .collect(Collectors.toList());

            List<PieChart.Data> data = mine.stream()
                    .map(p-> new PieChart.Data(p.getPortfolioName(), p.getTotalValue()))
                    .collect(Collectors.toList());

            pieChartAssets.setData(FXCollections.observableArrayList(data));
        } catch (SQLException|AppException e) {
            // silently fail chart
        }
    }

    private void loadTradesPerMonth() {
        try {
            int uid = Session.getCurrentUser().getUserId();
            List<Trade> all = tradeDao.findAll();
            List<Trade> mine = all.stream()
//                    .filter(t->t.getUserId()==uid)
                    .collect(Collectors.toList());

            Map<String, Long> counts = mine.stream()
                    .collect(Collectors.groupingBy(
                            t-> t.getTradeDate()
                                    .getMonth()
                                    .getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                            LinkedHashMap::new,
                            Collectors.counting()));

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            counts.forEach((month, cnt)-> series.getData().add(new XYChart.Data<>(month, cnt)));

            barChartTrades.getData().setAll(series);
        } catch (SQLException|AppException e) {
            // silently fail chart
        }
    }

    @FXML
    private void onExportCsv() {
        Window win = pieChartAssets.getScene().getWindow();
        FileChooser fc = new FileChooser();
        fc.setInitialFileName("portfolios.csv");
        File file = fc.showSaveDialog(win);
        if (file != null) {
            try {
                int uid = Session.getCurrentUser().getUserId();
                List<Portfolio> mine = portfolioDao.findAll().stream()
//                        .filter(p->p.getUserId()==uid)
                        .collect(Collectors.toList());
                ReportUtil.exportPortfoliosToCSV(mine, file);
            } catch (Exception e) {
                // ignore or show alert
            }
        }
    }

    @FXML
    private void onExportPdf() {
        Window win = pieChartAssets.getScene().getWindow();
        FileChooser fc = new FileChooser();
        fc.setInitialFileName("portfolios.pdf");
        File file = fc.showSaveDialog(win);
        if (file != null) {
            try {
                int uid = Session.getCurrentUser().getUserId();
                List<Portfolio> mine = portfolioDao.findAll().stream()
//                        .filter(p->p.getUserId()==uid)
                        .collect(Collectors.toList());
                ReportUtil.exportPortfoliosToPDF(mine, file);
            } catch (Exception e) {
                // ignore or show alert
            }
        }
    }
}
