package com.paulaespitia.recipenetwork.model;

public class Recipe {

    public final int id;
    public final String name;
    public final String author;
    public final String description;
    public final Double estimatedTime;

    public Recipe(int id, String name, String author, String description, Double estimatedTime) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.description = description;
        this.estimatedTime = estimatedTime;
    }
}
