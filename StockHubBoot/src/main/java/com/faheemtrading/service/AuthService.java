package com.faheemtrading.service;

import com.faheemtrading.dao.user.UserDAO;
import com.faheemtrading.dao.user.UserDAOImpl;
import com.faheemtrading.model.User;

import java.time.LocalDate;
import java.util.Optional;

public class AuthService {
    private final UserDAO dao = new UserDAOImpl();

    public Optional<User> login(String email, String password) {
        return dao.findByEmail(email)
                .filter(u -> u.getPassword().equals(password));
    }

    public boolean register(String name, String email, String password) {
        if (dao.emailExists(email)) return false;
        int nextId = dao.getAll().size() + 10;      // simplistic id strategy
        User u = new User(nextId, name, email, password, LocalDate.now());
        return dao.insert(u);
    }
}
