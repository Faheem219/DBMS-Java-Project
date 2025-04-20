// src/main/java/com/faheemtrading/model/Watchlist.java
package com.faheemtrading.model;

import java.time.LocalDate;

public class Watchlist {
    private int watchlistId;
    private String name;
    private String notes;
    private LocalDate addedDate;
    private int stockId;
    private String stockName;

    public Watchlist(int id,String n,String notes,LocalDate d,int sid,String sname){
        this.watchlistId=id;this.name=n;this.notes=notes;this.addedDate=d;
        this.stockId=sid;this.stockName=sname;
    }
    public int getWatchlistId(){ return watchlistId; }
    public String getName(){ return name; }
    public String getNotes(){ return notes; }
    public LocalDate getAddedDate(){ return addedDate; }
    public int getStockId(){ return stockId; }
    public String getStockName(){ return stockName; }
}
