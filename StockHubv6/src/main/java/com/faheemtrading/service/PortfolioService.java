// src/main/java/com/faheemtrading/service/PortfolioService.java
package com.faheemtrading.service;

import com.faheemtrading.dao.portfolio.PortfolioDAOImpl;
import com.faheemtrading.dao.trade.TradeDAO;
import com.faheemtrading.dao.trade.TradeDAOImpl;
import com.faheemtrading.model.*;

import java.time.LocalDate;
import java.util.List;

public class PortfolioService {
    private final PortfolioDAOImpl pfDao = new PortfolioDAOImpl();
    private final TradeDAO         trDao = new TradeDAOImpl();

    public List<Portfolio> userPortfolios(int uid){ return pfDao.findByUser(uid); }

    public boolean createPortfolio(String name){
        int nextId = (int)(System.currentTimeMillis() % 1_000_000);   // quick unique id
        Portfolio p = new Portfolio(nextId, name, LocalDate.now(), 0);
        return pfDao.insert(p);
    }

    public boolean deletePortfolio(int id){ return pfDao.delete(id); }

    public boolean executeTrade(AbstractTrade t){ return t.persist(); }

    public List<AbstractTrade> trades(int pfId){ return trDao.findByPortfolio(pfId); }
}
