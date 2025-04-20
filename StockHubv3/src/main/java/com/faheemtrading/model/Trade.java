package com.faheemtrading.model;

import java.time.LocalDate;

public abstract class Trade implements Identifiable {
    protected int tradeId;
    protected double price;
    protected LocalDate tradeDate;
    protected int quantity;
    protected int userId;
    protected int portfolioId;
    protected int stockId;

    public Trade() {}
    public Trade(int tradeId){ this.tradeId=tradeId; }

    @Override public int getId(){ return tradeId; }

    /* Common behaviour */
    public double getTradeValue(){ return price * quantity; }

    /* Abstract polymorphic hook */
    public abstract String getType();

    public int getTradeId() {
        return tradeId;
    }

    public void setTradeId(int tradeId) {
        this.tradeId = tradeId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(LocalDate tradeDate) {
        this.tradeDate = tradeDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(int portfolioId) {
        this.portfolioId = portfolioId;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }
}
