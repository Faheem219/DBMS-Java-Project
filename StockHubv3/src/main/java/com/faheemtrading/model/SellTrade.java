package com.faheemtrading.model;

public final class SellTrade extends Trade {
    private double sellCharges;
    public SellTrade(int id){ super(id); }
    @Override public String getType(){ return "SELL"; }
    // getters / setters


    public double getSellCharges() {
        return sellCharges;
    }

    public void setSellCharges(double sellCharges) {
        this.sellCharges = sellCharges;
    }
}
