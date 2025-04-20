package com.faheemtrading.util;

import com.faheemtrading.model.User;

public final class Session {
    private static User current;

    private Session() {}

    public static void setUser(User u) { current = u; }
    public static User getUser()       { return current; }
    public static void clear()         { current = null; }
    public static boolean isLoggedIn() { return current != null; }
}
