package com.example.myapplication.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Language {
    EN("English", "en"),
    FR("French", "fr");

    String description;
    String abbreviation;

    Language(String description, String abbreviation){
        this.description = description;
        this.abbreviation = abbreviation;
    }

    public String getDescription() {
        return description;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public static List<String> getAllDescriptions(){
        return Arrays.stream(Language.values()).map(Language::getDescription).collect(Collectors.toList());
    }

    public static Language findByDescription(String description){
        return Arrays.stream(Language.values()).filter(l -> l.getDescription().equals(description)).findFirst().orElse(Language.EN);
    }
}
