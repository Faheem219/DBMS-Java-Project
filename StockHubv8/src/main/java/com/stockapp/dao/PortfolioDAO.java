package com.stockapp.dao;

import com.stockapp.model.Portfolio;
import com.stockapp.util.AppException;
import java.util.List;

public interface PortfolioDAO {
    List<Portfolio> findByUserId(int userId) throws AppException;
    void save(Portfolio p) throws AppException;
    void update(Portfolio p) throws AppException;
    void delete(int portfolioId) throws AppException;
}
