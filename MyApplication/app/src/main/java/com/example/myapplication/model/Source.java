package com.example.myapplication.model;

public class Source {
    private String id;
    private String name;

    // Default constructor
    public Source() {
    }

    // Parameterized constructor
    public Source(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
