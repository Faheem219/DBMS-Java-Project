// src/main/java/com/faheemtrading/model/AbstractTrade.java
package com.faheemtrading.model;

import java.time.LocalDate;

public abstract class AbstractTrade {
    protected int       tradeId;
    protected double    price;
    protected int       quantity;
    protected LocalDate tradeDate;
    protected int       stockId;
    protected int       portfolioId;

    public AbstractTrade(int tradeId, double price, int quantity,
                         LocalDate tradeDate, int stockId, int portfolioId) {
        this.tradeId     = tradeId;
        this.price       = price;
        this.quantity    = quantity;
        this.tradeDate   = tradeDate;
        this.stockId     = stockId;
        this.portfolioId = portfolioId;
    }
    public int    getTradeId()   { return tradeId; }
    public double getPrice()     { return price; }
    public int    getQuantity()  { return quantity; }
    public LocalDate getTradeDate() { return tradeDate; }
    public int    getStockId()   { return stockId; }
    public int    getPortfolioId(){ return portfolioId; }

    /** Implemented differently by BuyTrade / SellTrade for DAO routing. */
    public abstract boolean persist();
}
