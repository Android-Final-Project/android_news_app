package com.example.myapplication.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    public static final String DB_REFERENCE = "users";
    public static final String DB_CHILD_USERNAME = "username";
    public static final String DB_CHILD_PASSWORD = "password";
    private String fullName;
    private String username;
    private String password;
    private String location;

    private boolean admin = false;

    private Language language = Language.EN;

    private List<Article> savedArticles = new ArrayList<>();

    public User() {
    }

    public User(String fullName, String username, String password, String location, Language language) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.location = location;
        this.language = language;
    }


    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getLocation() {
        return location;
    }

    public String getPassword() {
        return password;
    }

    public Language getLanguage() {
        return language;
    }

    public List<Article> getSavedArticles() {
        return savedArticles;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public void setSavedArticles(List<Article> savedArticles) {
        this.savedArticles = savedArticles;
    }
}
