package com.paulaespitia.recipenetwork.model;

import java.util.ArrayList;

public class RecipeDetailed extends Recipe {

    public static RecipeDetailed recipeToView;

    public final ArrayList<Ingredient> ingredients;
    public final ArrayList<Step> steps;

    public RecipeDetailed(int id, String name, String author, String description, Double estimatedTime, ArrayList<Ingredient> ingredients, ArrayList<Step> steps) {
        super(id, name, author, description, estimatedTime);
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public RecipeDetailed(int id, String name, String author, String description, Double estimatedTime) {
        super(id, name, author, description, estimatedTime);
        ingredients = new ArrayList<>();
        steps = new ArrayList<>();
    }

    public RecipeDetailed(Recipe recipe) {
        super(recipe.id, recipe.name, recipe.author, recipe.description, recipe.estimatedTime);
        ingredients = new ArrayList<>();
        steps = new ArrayList<>();
    }
}
