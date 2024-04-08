package com.example.myapplication.model;


import java.util.Date;

public class Comment {

    public static final String DB_REFERENCE = "comments";
    private String url;
    private String text;

    private String author;

    private Date createdAt = new Date();

    public Comment() {
    }

    public Comment(String url, String text, String author) {
        this.url = url;
        this.text = text;
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
