package com.paulaespitia.recipenetwork;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final ImageView picture = findViewById(R.id.profilePicture); //?????? how user updates picture
        final TextView username = findViewById(R.id.profileUsername); //?? how do i get the username of the person logged in
        final Button favorites = findViewById(R.id.profileFavorites);
        final Button followers = findViewById(R.id.profileFollowers);
        final Button following = findViewById(R.id.profileFollowing);
        final Button myRecipes = findViewById(R.id.profileMyRecipes);
        final Button myMessages = findViewById(R.id.profileMyMessages);




    }

}
