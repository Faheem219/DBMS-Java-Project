// src/main/java/com/com.faheemtrading/model/Stock.java
package com.faheemtrading.model;

public class Stock {
    private int    stockId;
    private String name;
    private double marketPrice;

    public Stock(int id,String n,double mp){ this.stockId=id;this.name=n;this.marketPrice=mp; }
    public int getStockId()      { return stockId; }
    public String getName()      { return name; }
    public double getMarketPrice(){ return marketPrice; }
}
