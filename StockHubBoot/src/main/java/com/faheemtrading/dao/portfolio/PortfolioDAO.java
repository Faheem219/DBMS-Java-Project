// src/main/java/com/com.faheemtrading/dao/portfolio/PortfolioDAO.java
package com.faheemtrading.dao.portfolio;

import com.faheemtrading.dao.DAO;
import com.faheemtrading.model.Portfolio;

import java.util.List;

public interface PortfolioDAO extends DAO<Portfolio,Integer> {
    List<Portfolio> findByUser(int userId);
}
