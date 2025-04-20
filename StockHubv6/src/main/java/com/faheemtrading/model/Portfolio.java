// src/main/java/com/faheemtrading/model/Portfolio.java
package com.faheemtrading.model;

import java.time.LocalDate;

public class Portfolio {
    private int        portfolioId;
    private String     name;
    private LocalDate  creationDate;
    private double     totalValue;

    public Portfolio(int id,String name,LocalDate date,double value){
        this.portfolioId=id;this.name=name;this.creationDate=date;this.totalValue=value;
    }
    public int getPortfolioId()       { return portfolioId; }
    public String getName()           { return name; }
    public LocalDate getCreationDate(){ return creationDate; }
    public double getTotalValue()     { return totalValue; }
    public void   setTotalValue(double v){ this.totalValue=v; }
}
