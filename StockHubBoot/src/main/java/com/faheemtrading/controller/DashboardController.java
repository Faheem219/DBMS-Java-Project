package com.faheemtrading.controller;

import com.faheemtrading.model.Portfolio;
import com.faheemtrading.service.AnalyticsService;
import com.faheemtrading.service.PortfolioService;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;

public class DashboardController {
    @FXML private PieChart portfolioChart;
    @FXML private LineChart<String,Number> roiChart;

//    @FXML
//    public void initialize() {
//        PortfolioService ps = new PortfolioService();
//        var list = ps.userPortfolios(com.com.faheemtrading.util.Session.getUser().getUserId());
//
//        var series = new javafx.scene.chart.XYChart.Series<String,Number>();
//        series.setName("ROI");
//
//        try (var conn = com.com.faheemtrading.util.DBUtil.getConnection()) {
//            String sql = "SELECT calculatePortfolioROI(?)";
//            try (var psmt = conn.prepareStatement(sql)) {
//                for (var p : list) {
//                    psmt.setInt(1, p.getPortfolioId());
//                    try (var rs = psmt.executeQuery()) {
//                        rs.next();
//                        double roi = rs.getDouble(1);
//                        series.getData().add(new javafx.scene.chart.XYChart.Data<>(p.getName(), roi));
//                    }
//                }
//            }
//        } catch (Exception e) { e.printStackTrace(); }
//
//        roiChart.getData().add(series);
//    }

    private void plotRoi(){
        roiChart.getData().clear();
        PortfolioService ps=new PortfolioService();
        AnalyticsService as=new AnalyticsService();

        for(var pf:ps.userPortfolios(com.faheemtrading.util.Session.getUser().getUserId())){
            var map=as.roi(pf.getPortfolioId());
            javafx.scene.chart.XYChart.Series<String,Number> series=new javafx.scene.chart.XYChart.Series<>();
            series.setName(pf.getName());
            map.forEach((d,v)-> series.getData().add(new javafx.scene.chart.XYChart.Data<>(d.toString(),v)));
            roiChart.getData().add(series);
        }
    }

    private void loadPie(){
        portfolioChart.getData().clear();
        PortfolioService ps = new PortfolioService();
        var list = ps.userPortfolios(com.faheemtrading.util.Session.getUser().getUserId());
        double total = list.stream().mapToDouble(Portfolio::getTotalValue).sum();
        for(Portfolio p : list){
            double val = p.getTotalValue();
            portfolioChart.getData().add(
                    new PieChart.Data(p.getName(), (total==0?0:val*100/total)));
        }
    }

    @FXML public void initialize(){
        loadPie(); plotRoi();
    }
}
