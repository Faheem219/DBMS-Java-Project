// src/main/java/com/faheemtrading/dao/trade/TradeDAO.java
package com.faheemtrading.dao.trade;

import com.faheemtrading.model.AbstractTrade;

import java.util.List;

public interface TradeDAO {
    boolean insertBuy(com.faheemtrading.model.BuyTrade bt);
    boolean insertSell(com.faheemtrading.model.SellTrade st);
    List<AbstractTrade> findByPortfolio(int pfId);
}
