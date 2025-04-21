package com.faheemtrading.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class User {
    private int    userId;
    private String name;
    private String email;
    private String password;
    private LocalDate createdDate;
    private String contactInfo;
    private BigDecimal annualIncome;

    /* full‑args constructor for prepared‑statement mapping */
    public User(int userId, String name, String email, String password, LocalDate createdDate) {
        this.userId      = userId;
        this.name        = name;
        this.email       = email;
        this.password    = password;
        this.createdDate = createdDate;
    }

    public User(int userId, String name, String email, String password, LocalDate createdDate, String contactInfo, BigDecimal annualIncome) {
        this.userId      = userId;
        this.name        = name;
        this.email       = email;
        this.password    = password;
        this.createdDate = createdDate;
        this.contactInfo = contactInfo;
        this.annualIncome = annualIncome;
    }

    /* getters & setters */
    public int getUserId()           { return userId; }
    public void setUserId(int id)    { this.userId = id; }
    public String getName()          { return name; }
    public String getEmail()         { return email; }
    public String getPassword()      { return password; }
    public LocalDate getCreatedDate(){ return createdDate; }
    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }
    public BigDecimal getAnnualIncome() { return annualIncome; }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public void setAnnualIncome(BigDecimal annualIncome) {
        this.annualIncome = annualIncome;
    }
}
