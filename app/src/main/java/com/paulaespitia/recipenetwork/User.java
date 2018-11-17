package com.paulaespitia.recipenetwork;

public class User {

    private String username;
    private String password;
    private String verifyPassword;
    private String email;
    private String picture;

    public User() {

    }

    public User(String username, String password, String verifyPassword, String email) {
        this.username = username;
        this.password = password;
        this.verifyPassword = verifyPassword;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public String getEmail() {
        return email;
    }

    public String getPicture(){
        return picture;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPicture(String picture){
        this.picture = picture;
    }
}
