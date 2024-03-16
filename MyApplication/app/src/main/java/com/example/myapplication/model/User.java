package com.example.myapplication.model;

public class User {
    private Long id;
    private String fullName;
    private String username;
    private String password;
    private String location;

    public static User getLogged(){
        return new User(1L, "Iveta Forkk", "iveta.fork", "12345", "Montreal, CA");
    }

    public User(Long id, String fullName, String username, String password, String location) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.location = location;
    }

    public Long getId() {
        return id;
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
}
