package com.faheemtrading.model;

public final class BuyTrade extends Trade {
    private double brokerFee;
    public BuyTrade(int id){ super(id); }
    @Override public String getType(){ return "BUY"; }
    // getters / setters


    public double getBrokerFee() {
        return brokerFee;
    }

    public void setBrokerFee(double brokerFee) {
        this.brokerFee = brokerFee;
    }
}
