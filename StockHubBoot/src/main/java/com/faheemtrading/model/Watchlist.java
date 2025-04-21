// src/main/java/com/com.faheemtrading/model/Watchlist.java
package com.faheemtrading.model;

import java.time.LocalDate;

public class Watchlist {
    private int watchlistId;
    private String name;
    private String notes;
    private LocalDate addedDate;
    private int stockId;
    private String stockName;
    private int userId;

    public Watchlist(int id,String n,String notes,LocalDate d,int sid,String sname){
        this.watchlistId=id;this.name=n;this.notes=notes;this.addedDate=d;
        this.stockId=sid;this.stockName=sname;
    }

    public Watchlist(int id,String n,String notes,LocalDate d,int sid,String sname, int userId){
        this.watchlistId=id;this.name=n;this.notes=notes;this.addedDate=d;
        this.stockId=sid;this.stockName=sname;this.userId=userId;
    }

    public int getWatchlistId(){ return watchlistId; }
    public String getName(){ return name; }
    public String getNotes(){ return notes; }
    public LocalDate getAddedDate(){ return addedDate; }
    public int getStockId(){ return stockId; }
    public String getStockName(){ return stockName; }
    public int getUserId(){ return userId; }

    public void setName(String name) {
        this.name = name;
    }

    public void setWatchlistId(int watchlistId) {
        this.watchlistId = watchlistId;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }
}
