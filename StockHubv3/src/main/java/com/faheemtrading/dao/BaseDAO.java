package com.faheemtrading.dao;

import com.faheemtrading.exception.AppException;
import java.util.List;

public interface BaseDAO<T> {
    T       findById(int id)        throws AppException;
    List<T> findAll()               throws AppException;
    void    save(T entity)          throws AppException;  // insert or update
    void    delete(int id)          throws AppException;
}
