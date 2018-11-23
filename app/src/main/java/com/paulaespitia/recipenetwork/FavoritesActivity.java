package com.paulaespitia.recipenetwork;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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


