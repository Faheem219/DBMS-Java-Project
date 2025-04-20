package com.faheemtrading.model;

import java.time.LocalDate;

public class Portfolio implements Identifiable {
    private int portfolioId;
    private String name;
    private LocalDate creationDate;
    private double totalValue;
    private int userId;

    public Portfolio() {}
    public Portfolio(int id, String name){ this.portfolioId=id; this.name=name; }

    /* Getters / setters */
    @Override public int getId(){ return portfolioId; }
    public void setId(int id){ this.portfolioId=id; }
    public String getName(){ return name; }
    public void setName(String n){ this.name=n; }
    public LocalDate getCreationDate(){ return creationDate; }
    public void setCreationDate(LocalDate d){ this.creationDate=d; }
    public double getTotalValue(){ return totalValue; }
    public void setTotalValue(double v){ this.totalValue=v; }
    public int getUserId(){ return userId; }
    public void setUserId(int uid){ this.userId=uid; }

    public int getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(int portfolioId) {
        this.portfolioId = portfolioId;
    }
}
