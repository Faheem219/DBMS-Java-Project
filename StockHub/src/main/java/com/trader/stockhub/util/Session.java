package com.trader.stockhub.util;

/**
 * A simple session manager that holds the currently logged-in user's details.
 */
public class Session {
    private static Session instance;
    private int currentUserId;
    private String currentUserName; // You can use email or name

    // Private constructor to enforce singleton pattern.
    private Session() {}

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(int currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }
}
