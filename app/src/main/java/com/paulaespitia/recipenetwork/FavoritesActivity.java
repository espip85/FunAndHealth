package com.paulaespitia.recipenetwork;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FavoritesActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        final Button usersButton = findViewById(R.id.favoritesUsersButton);
        usersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favoritesUsersIntent = new Intent(FavoritesActivity.this, FavoritesUsersActivity.class);
                FavoritesActivity.this.startActivity(favoritesUsersIntent);
            }
        });

        final Button recipesButton = findViewById(R.id.favoritesRecipesButton);
        recipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favoritesRecipesIntent = new Intent(FavoritesActivity.this, FavoritesRecipesActivity.class);
                FavoritesActivity.this.startActivity(favoritesRecipesIntent);
            }
        });
    }
}


