package com.stockapp.model;

public class User {
    private int userId;
    private String name;
    private String email;
    private String password;

    public User() {}

    public User(int userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // getters & setters
    public int getUserId() { return userId; }
    public void setUserId(int id) { this.userId = id; }

    public String getName() { return name; }
    public void setName(String n) { this.name = n; }

    public String getEmail() { return email; }
    public void setEmail(String e) { this.email = e; }

    public String getPassword() { return password; }
    public void setPassword(String p) { this.password = p; }
}
