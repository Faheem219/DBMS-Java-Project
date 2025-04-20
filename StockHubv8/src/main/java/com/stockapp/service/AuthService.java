package com.stockapp.service;

import com.stockapp.dao.UserDAO;
import com.stockapp.dao.impl.UserDAOImpl;
import com.stockapp.model.User;
import com.stockapp.util.AppException;

public class AuthService {
    private final UserDAO userDAO = new UserDAOImpl();

    public User login(String email, String password) throws AppException {
        if (email == null || password == null || email.isBlank() || password.isBlank()) {
            throw new AppException("Email and password must be provided");
        }
        User user = userDAO.findByEmailAndPassword(email, password);
        if (user == null) {
            throw new AppException("Invalid credentials");
        }
        return user;
    }

    public void register(User user) throws AppException {
        if (userDAO.existsByEmail(user.getEmail())) {
            throw new AppException("Email already in use");
        }
        userDAO.save(user);
    }
}
