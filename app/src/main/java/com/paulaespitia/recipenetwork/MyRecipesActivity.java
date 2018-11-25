package com.paulaespitia.recipenetwork;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MyRecipesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);

        final EditText search = findViewById(R.id.myRecipesSearchRecipe);



        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                try (Connection connection = SQLHelper.getHelper().getConnection()) {
                    PreparedStatement preparedStatement =  connection.prepareStatement("SELECT 1 FROM Recipes WHERE name LIKE ?" ); //%%signs
                    preparedStatement.setString(1, v.getText().toString());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    ArrayList<Recipe> recipes = new ArrayList<>();

                    while (resultSet.next()) {
                        if (search.getText().toString().equalsIgnoreCase(resultSet.getString("name"))) {
                            search.setText("Valid search!");
//                            Recipe displayRecipe = new Recipe();
//                            displayRecipe.setName(resultSet.getString("name"));
//                            displayRecipe.setEstimatedTime(resultSet.getString("estimatedTime"));
//                            displayRecipe.setPicture(resultSet.getString("picture")); // ????? picture path
//                            recipes.add(displayRecipe); //????? ADD TO LIST VIEW
                        } else {
                            Toast.makeText(getApplicationContext(),"Invalid recipe search!", Toast.LENGTH_LONG).show();
                        }
                    }

                } catch (SQLException e) {
                    Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

    }
}
