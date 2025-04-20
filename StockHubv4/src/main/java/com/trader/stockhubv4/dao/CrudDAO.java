package com.trader.stockhubv4.dao;

import com.trader.stockhubv4.exception.AppException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Generic CRUD interface for all entities.
 * Demonstrates use of interfaces and polymorphism.
 */
public interface CrudDAO<T> {
    void create(T t) throws SQLException, AppException;
    Optional<T> findById(int id) throws SQLException, AppException;
    List<T> findAll() throws SQLException, AppException;
    void update(T t) throws SQLException, AppException;
    void delete(int id) throws SQLException, AppException;
}
