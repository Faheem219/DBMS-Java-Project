package com.faheemtrading.model;

import java.time.LocalDate;

public class User implements Identifiable {
    private int    userId;
    private String name;
    private String email;
    private String password;
    private LocalDate createdDate;
    private String contactInfo;
    private String userType;
    private double annualIncome;

    /* Constructors */
    public User() {}
    public User(int id, String name, String email, String password, LocalDate date){
        this.userId=id; this.name=name; this.email=email; this.password=password; this.createdDate=date;
    }

    /* Getters & Setters */
    @Override public int getId(){ return userId; }
    public void setUserId(int userId){ this.userId = userId; }

    // (other getters/setters )

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public double getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(double annualIncome) {
        this.annualIncome = annualIncome;
    }
}
