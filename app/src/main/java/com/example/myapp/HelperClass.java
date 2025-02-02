package com.example.myapp;

public class HelperClass {
    private String name;
    private String email;
    private String password;
    private String android;
    private String userAuId;
    private int id;
    private String role;

    // Default constructor for Firebase
    public HelperClass() {
    }

    // Parameterized constructor
    public HelperClass(String name, String email, String password, String android, String userAuId, int id, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.android = android;
        this.userAuId = userAuId;
        this.id = id;
        this.role = role;
    }

    public HelperClass(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
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

    public String getAndroid() {
        return android;
    }

    public void setAndroid(String android) {
        this.android = android;
    }

    public String getUserAuId() {
        return userAuId;
    }

    public void setUserAuId(String userAuId) {
        this.userAuId = userAuId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "HelperClass{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", android='" + android + '\'' +
                ", userAuId='" + userAuId + '\'' +
                ", id=" + id +
                ", role='" + role + '\'' +
                '}';
    }
}
