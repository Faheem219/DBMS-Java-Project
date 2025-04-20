package com.stockapp.service;

import com.stockapp.dao.PortfolioDAO;
import com.stockapp.dao.impl.PortfolioDAOImpl;
import com.stockapp.model.Portfolio;
import com.stockapp.util.AppException;

import java.util.List;

public class PortfolioService {
    private final PortfolioDAO dao = new PortfolioDAOImpl();
    public List<Portfolio> getUserPortfolios(int uid) throws AppException {
        return dao.findByUserId(uid);
    }
    public void addPortfolio(Portfolio p) throws AppException {
        dao.save(p);
    }
    public void updatePortfolio(Portfolio p) throws AppException {
        dao.update(p);
    }
    public void deletePortfolio(int id) throws AppException {
        dao.delete(id);
    }
}
