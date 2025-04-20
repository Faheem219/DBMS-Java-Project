package com.faheemtrading.model;

import java.time.LocalDate;

public class User {
    private int    userId;
    private String name;
    private String email;
    private String password;
    private LocalDate createdDate;

    /* full‑args constructor for prepared‑statement mapping */
    public User(int userId, String name, String email, String password, LocalDate createdDate) {
        this.userId      = userId;
        this.name        = name;
        this.email       = email;
        this.password    = password;
        this.createdDate = createdDate;
    }

    /* getters & setters */
    public int getUserId()           { return userId; }
    public void setUserId(int id)    { this.userId = id; }
    public String getName()          { return name; }
    public String getEmail()         { return email; }
    public String getPassword()      { return password; }
    public LocalDate getCreatedDate(){ return createdDate; }
}
