package com.stockapp.model;

public class Stock {
    private int stockId;
    private String name;
    public Stock() {}
    public int getStockId() { return stockId; }
    public void setStockId(int id) { this.stockId = id; }
    public String getName() { return name; }
    public void setName(String n) { this.name = n; }
    @Override public String toString() { return name; }
}
