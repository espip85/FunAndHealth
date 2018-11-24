package com.paulaespitia.recipenetwork;

public class Recipe {

    public final String name;
    public final String author;
    public final String description;
    public final Double estimatedTime;

    public Recipe(String name, String author, String description, Double estimatedTime) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.estimatedTime = estimatedTime;
    }
}
