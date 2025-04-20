package com.stockapp.dao;

import com.stockapp.model.Watchlist;
import com.stockapp.util.AppException;
import java.util.List;

public interface WatchlistDAO {
    List<Watchlist> findByUserId(int uid) throws AppException;
    void save(Watchlist w) throws AppException;
    void delete(int watchlistId) throws AppException;
}
