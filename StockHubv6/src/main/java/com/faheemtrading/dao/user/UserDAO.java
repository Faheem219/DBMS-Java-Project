package com.faheemtrading.dao.user;

import com.faheemtrading.dao.DAO;
import com.faheemtrading.model.User;

import java.util.Optional;

public interface UserDAO extends DAO<User, Integer> {
    Optional<User> findByEmail(String email);
    boolean emailExists(String email);
}
