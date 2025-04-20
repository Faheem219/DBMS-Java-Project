// src/main/java/com/faheemtrading/model/SellTrade.java
package com.faheemtrading.model;

import com.faheemtrading.dao.trade.TradeDAOImpl;

import java.time.LocalDate;

public class SellTrade extends AbstractTrade {
    public SellTrade(int id,double p,int q,LocalDate d,int s,int pf){
        super(id,p,q,d,s,pf);
    }
    @Override public boolean persist() { return new TradeDAOImpl().insertSell(this); }
}
