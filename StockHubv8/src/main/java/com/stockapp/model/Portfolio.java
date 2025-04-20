package com.stockapp.model;

import java.time.LocalDate;

public class Portfolio {
    private int portfolioId;
    private String name;
    private LocalDate creationDate;
    private double totalValue;
    private int userId;

    public Portfolio() {}

    // getters & setters
    public int getPortfolioId() { return portfolioId; }
    public void setPortfolioId(int id) { this.portfolioId = id; }

    public String getName() { return name; }
    public void setName(String n) { this.name = n; }

    public LocalDate getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDate d) { this.creationDate = d; }

    public double getTotalValue() { return totalValue; }
    public void setTotalValue(double v) { this.totalValue = v; }

    public int getUserId() { return userId; }
    public void setUserId(int u) { this.userId = u; }
}
