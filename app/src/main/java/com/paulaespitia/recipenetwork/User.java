package com.paulaespitia.recipenetwork;

public class User {

    public static User currentUser;

    public final int userId;
    public final String username;
    public final String email;

    public User(int userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
    }
}
