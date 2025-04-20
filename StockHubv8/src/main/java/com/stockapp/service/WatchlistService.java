package com.stockapp.service;

import com.stockapp.dao.WatchlistDAO;
import com.stockapp.dao.impl.WatchlistDAOImpl;
import com.stockapp.model.Watchlist;
import com.stockapp.util.AppException;

import java.util.List;

public class WatchlistService {
    private final WatchlistDAO dao = new WatchlistDAOImpl();

    public List<Watchlist> getUserWatchlist(int uid) throws AppException {
        return dao.findByUserId(uid);
    }
    public void addWatchlist(Watchlist w) throws AppException {
        dao.save(w);
    }
    public void deleteWatchlist(int id) throws AppException {
        dao.delete(id);
    }

}
