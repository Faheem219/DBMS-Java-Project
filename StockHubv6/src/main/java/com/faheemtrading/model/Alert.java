// src/main/java/com/faheemtrading/model/Alert.java
package com.faheemtrading.model;

import java.time.LocalTime;

public class Alert {
    private int alertId;
    private String alertType;
    private double threshold;
    private LocalTime alertTime;
    private boolean read;
    private int stockId;

    public Alert(int id,String type,double thr,LocalTime t,boolean r,int s){
        this.alertId=id;this.alertType=type;this.threshold=thr;
        this.alertTime=t;this.read=r;this.stockId=s;
    }
    public int getAlertId(){ return alertId; }
    public String getAlertType(){ return alertType; }
    public double getThreshold(){ return threshold; }
    public LocalTime getAlertTime(){ return alertTime; }
    public boolean isRead(){ return read; }
    public int getStockId(){ return stockId; }
}
