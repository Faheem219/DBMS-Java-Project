package com.faheemtrading.service;

import com.faheemtrading.dao.user.UserDAO;
import com.faheemtrading.dao.user.UserDAOImpl;
import com.faheemtrading.model.User;

public class ProfileService {

    private final UserDAO dao = new UserDAOImpl();

    public User load(int id) {
        return dao.get(id).orElseThrow(
                () -> new IllegalArgumentException("User not found"));
    }

    public boolean save(User u) {      // email + name are unchanged
        return dao.update(u);
    }
}
