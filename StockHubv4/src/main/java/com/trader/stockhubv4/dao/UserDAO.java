package com.trader.stockhubv4.dao;

import com.trader.stockhubv4.exception.AppException;
import com.trader.stockhubv4.model.User;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Extends generic CRUD with an authentication method.
 */
public interface UserDAO extends CrudDAO<User> {
    /**
     * Attempt to authenticate a user by email and password.
     * @return Optional.of(user) if credentials match; Optional.empty() otherwise.
     */
    Optional<User> authenticate(String email, String password)
            throws SQLException, AppException;
}
