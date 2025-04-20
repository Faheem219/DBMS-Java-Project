package com.faheemtrading.dao;

import com.faheemtrading.exception.AppException;
import com.faheemtrading.model.User;

public interface UserDAO extends BaseDAO<User> {
    User authenticate(String email, String password) throws AppException;
}
