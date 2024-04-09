package com.example.myapplication.model;

import java.util.ArrayList;
import java.util.List;

public class BanedSources {

    public static final String DB_REFERENCE = "baned_sources";

    private String id;

    public BanedSources() {
    }

    public BanedSources(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
