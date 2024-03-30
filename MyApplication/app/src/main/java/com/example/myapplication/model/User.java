package com.example.myapplication.model;

public class User {

    public static final String DB_REFERENCE = "users";
    public static final String DB_CHILD_USERNAME = "username";
    public static final String DB_CHILD_PASSWORD = "password";
    private String fullName;
    private String username;
    private String password;
    private String location;

    private Language language = Language.EN;

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

    public void setLanguage(Language language) {
        this.language = language;
    }
}
