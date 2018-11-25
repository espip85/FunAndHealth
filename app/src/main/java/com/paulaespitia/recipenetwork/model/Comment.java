package com.paulaespitia.recipenetwork.model;

public class Comment {

    public final String username;
    public final String text;
    public final Double score;

    public Comment(String username, String text, Double score) {
        this.username = username;
        this.text = text;
        this.score = score;
    }

}
