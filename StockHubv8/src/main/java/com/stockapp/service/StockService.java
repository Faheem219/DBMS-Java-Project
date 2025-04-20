package com.stockapp.service;

import com.stockapp.dao.StockDAO;
import com.stockapp.dao.impl.StockDAOImpl;
import com.stockapp.model.Stock;
import com.stockapp.util.AppException;

import java.util.List;

public class StockService {
    private final StockDAO dao = new StockDAOImpl();
    public List<Stock> getAllStocks() throws AppException {
        return dao.findAll();
    }
}
