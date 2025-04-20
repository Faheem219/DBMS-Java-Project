package com.stockapp.dao;

import com.stockapp.model.Trade;
import com.stockapp.util.AppException;

import java.util.List;

public interface TradeDAO {
    List<Trade> findByUserId(int userId) throws AppException;
    void save(Trade t) throws AppException;
}
