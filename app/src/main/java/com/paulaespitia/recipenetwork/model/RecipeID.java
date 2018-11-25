package com.paulaespitia.recipenetwork.model;

public class RecipeID {

    public final int recipeID;
    public final String name;
    public final String author;

    public RecipeID(int recipeID, String name, String author) {
        this.recipeID = recipeID;
        this.name = name;
        this.author = author;
    }
}
