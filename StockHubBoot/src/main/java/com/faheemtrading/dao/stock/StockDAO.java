// src/main/java/com/com.faheemtrading/dao/stock/StockDAO.java
package com.faheemtrading.dao.stock;

import com.faheemtrading.model.Stock;

import java.util.List;
import java.util.Optional;

public interface StockDAO {
    List<Stock> getAll();
    Optional<Stock> getById(int id);
}
