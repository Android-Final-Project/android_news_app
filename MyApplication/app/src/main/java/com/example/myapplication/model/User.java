package com.example.myapplication.model;

public class User {

    public static final String DB_REFERENCE = "users";
    public static final String DB_CHILD_USERNAME = "username";
    public static final String DB_CHILD_PASSWORD = "password";
    private String fullName;
    private String username;
    private String password;
    private String location;

    public static User getLogged(){
        return new User("Iveta Forkk", "iveta.fork", "12345", "Montreal, CA");
    }

    public User() {
    }

    public User(String fullName, String username, String password, String location) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.location = location;
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
}
