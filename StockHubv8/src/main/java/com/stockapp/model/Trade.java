package com.stockapp.model;

import java.time.LocalDate;

public class Trade {
    private int tradeId;
    private double price;
    private LocalDate tradeDate;
    private int tradeQty;
    private int userId;
    private int portfolioId;
    private int stockId;

    public Trade() {}

    // getters & setters
    public int getTradeId() { return tradeId; }
    public void setTradeId(int id) { this.tradeId = id; }

    public double getPrice() { return price; }
    public void setPrice(double p) { this.price = p; }

    public LocalDate getTradeDate() { return tradeDate; }
    public void setTradeDate(LocalDate d) { this.tradeDate = d; }

    public int getTradeQty() { return tradeQty; }
    public void setTradeQty(int q) { this.tradeQty = q; }

    public int getUserId() { return userId; }
    public void setUserId(int u) { this.userId = u; }

    public int getPortfolioId() { return portfolioId; }
    public void setPortfolioId(int p) { this.portfolioId = p; }

    public int getStockId() { return stockId; }
    public void setStockId(int s) { this.stockId = s; }
}
