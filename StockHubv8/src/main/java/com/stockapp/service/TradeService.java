package com.stockapp.service;

import com.stockapp.dao.TradeDAO;
import com.stockapp.dao.impl.TradeDAOImpl;
import com.stockapp.model.Trade;
import com.stockapp.util.AppException;

import java.util.List;

public class TradeService {
    private final TradeDAO dao = new TradeDAOImpl();

    public List<Trade> getUserTrades(int uid) throws AppException {
        return dao.findByUserId(uid);
    }

    public void placeTrade(Trade t) throws AppException {
        dao.save(t);
    }
}
