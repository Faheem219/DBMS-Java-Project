package com.faheemtrading.model;

import java.time.LocalTime;

public class AlertRecord implements Identifiable {
    private int alertId;
    private String type;
    private double threshold;
    private LocalTime time;
    private boolean read;
    private int stockId;

    @Override public int getId(){ return alertId; }
    public void setId(int id){ this.alertId=id; }
    // getters / setters for other fields...


    public int getAlertId() {
        return alertId;
    }

    public void setAlertId(int alertId) {
        this.alertId = alertId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }
}
