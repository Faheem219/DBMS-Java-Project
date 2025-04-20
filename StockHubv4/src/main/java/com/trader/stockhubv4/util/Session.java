package com.trader.stockhubv4.util;

import com.trader.stockhubv4.model.User;

/**
 * Simple in‑memory session to hold the logged‑in user.
 */
public class Session {
    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }
}
