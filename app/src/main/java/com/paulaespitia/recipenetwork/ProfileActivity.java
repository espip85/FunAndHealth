package com.paulaespitia.recipenetwork;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.paulaespitia.recipenetwork.model.User;

public class ProfileActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final TextView username = findViewById(R.id.profileUsername);
        username.setText(User.currentUser.username);
    }

    public void openMyFavorites(View view) {
        Intent myFavoritesIntent = new Intent(this, FavoritesRecipesActivity.class);
        startActivity(myFavoritesIntent);
    }

    public void openMyRecipes(View view) {
        Intent myRecipesIntent = new Intent(this, MyRecipesActivity.class);
        startActivity(myRecipesIntent);
    }

}
