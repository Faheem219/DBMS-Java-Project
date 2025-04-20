package com.trader.stockhub.models;

import javafx.beans.property.*;

public class Portfolio {
    private final IntegerProperty portfolioId;
    private final StringProperty portfolioName;
    private final StringProperty creationDate;
    private final DoubleProperty totalValue;

    public Portfolio(int portfolioId, String portfolioName, String creationDate, double totalValue) {
        this.portfolioId = new SimpleIntegerProperty(portfolioId);
        this.portfolioName = new SimpleStringProperty(portfolioName);
        this.creationDate = new SimpleStringProperty(creationDate);
        this.totalValue = new SimpleDoubleProperty(totalValue);
    }

    public int getPortfolioId() {
        return portfolioId.get();
    }
    public void setPortfolioId(int id) {
        this.portfolioId.set(id);
    }
    public IntegerProperty portfolioIdProperty() {
        return portfolioId;
    }

    public String getPortfolioName() {
        return portfolioName.get();
    }
    public void setPortfolioName(String name) {
        this.portfolioName.set(name);
    }
    public StringProperty portfolioNameProperty() {
        return portfolioName;
    }

    public String getCreationDate() {
        return creationDate.get();
    }
    public void setCreationDate(String date) {
        this.creationDate.set(date);
    }
    public StringProperty creationDateProperty() {
        return creationDate;
    }

    public double getTotalValue() {
        return totalValue.get();
    }
    public void setTotalValue(double value) {
        this.totalValue.set(value);
    }
    public DoubleProperty totalValueProperty() {
        return totalValue;
    }
}
