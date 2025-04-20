package com.stockapp.dao;

import com.stockapp.model.Stock;
import com.stockapp.util.AppException;

import java.util.List;

public interface StockDAO {
    List<Stock> findAll() throws AppException;
}
