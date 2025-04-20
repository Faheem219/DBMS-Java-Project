package com.stockapp.model;

import javafx.beans.value.ObservableValue;

import java.time.LocalDate;

public class Watchlist {
    private int watchlistId;
    private String name;
    private String notes;
    private LocalDate addedDate;
    private int stockId;
    private int userId;

    public Watchlist() {}

    // getters & setters
    public int getWatchlistId() { return watchlistId; }
    public void setWatchlistId(int id) { this.watchlistId = id; }

    public String getName() { return name; }
    public void setName(String n) { this.name = n; }

    public String getNotes() { return notes; }
    public void setNotes(String n) { this.notes = n; }

    public LocalDate getAddedDate() { return addedDate; }
    public void setAddedDate(LocalDate d) { this.addedDate = d; }

    public int getStockId() { return stockId; }
    public void setStockId(int id) { this.stockId = id; }

    public int getUserId() { return userId; }
    public void setUserId(int u) { this.userId = u; }


}
