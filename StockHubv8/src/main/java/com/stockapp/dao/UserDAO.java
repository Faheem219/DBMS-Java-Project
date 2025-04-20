package com.stockapp.dao;

import com.stockapp.model.User;
import com.stockapp.util.AppException;

public interface UserDAO {
    User findByEmailAndPassword(String email, String password) throws AppException;
    boolean existsByEmail(String email) throws AppException;
    void save(User user) throws AppException;
}
