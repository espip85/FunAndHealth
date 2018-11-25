package com.paulaespitia.recipenetwork;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.paulaespitia.recipenetwork.model.Recipe;
import com.paulaespitia.recipenetwork.model.RecipeDetailed;
import com.paulaespitia.recipenetwork.model.SQLAsyncTask;
import com.paulaespitia.recipenetwork.model.User;

import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class UserActivity extends MenuActivity {

    TextView usernameTextView;
    LinearLayout recipeList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        usernameTextView = findViewById(R.id.viewUserUsername);
        recipeList = findViewById(R.id.viewUserRecipeList);

        GetUsernameTask getUsernameTask = new GetUsernameTask(this, usernameTextView);
        getUsernameTask.execute(getIntent().getIntExtra("idUsers", 0));

        GetUsersRecipesTask getUsersRecipesTask = new GetUsersRecipesTask(this, recipeList);
        getUsersRecipesTask.execute(getIntent().getIntExtra("idUsers", 0));

    }

    private static class GetUsersRecipesTask extends SQLAsyncTask<Integer, ArrayList<Recipe>> {

        WeakReference<LinearLayout> weakRecipesList;

        public GetUsersRecipesTask(Activity activity, LinearLayout recipesList) {
            super(activity);
            weakRecipesList = new WeakReference<>(recipesList);
        }

        @Override
        protected ArrayList<Recipe> sqlBackground(Connection connection, Integer... integers) throws SQLException {
            PreparedStatement preparedStatement = connection.prepareStatement("call RecipeNetwork.get_users_recipes(?)");
            preparedStatement.setInt(1, integers[0]);   // userID
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Recipe> recipes = new ArrayList<>();
            while (resultSet.next()) {
                Integer recipeID = resultSet.getInt("idRecipes");
                String recipeName = resultSet.getString("name");
                String author = resultSet.getString("author");
                String description = resultSet.getString("description");
                Double estimatedTime = resultSet.getDouble("estimatedTime");
                recipes.add(new Recipe(recipeID, recipeName, author, description, estimatedTime));
            }
            return recipes;
        }

        @Override
        protected void sqlPostExecute(final Activity activity, ArrayList<Recipe> recipes) {
            final LinearLayout recipesList = weakRecipesList.get();
            if (recipes == null) {
                Toast.makeText(activity.getApplicationContext(), "Unable to retrieve recipe information", Toast.LENGTH_SHORT).show();
            } else if (recipesList != null) {
                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                for (final Recipe recipe : recipes) {
                    final View recipeView = inflater.inflate(R.layout.recipe_list_view_detailed, null);

                    final TextView recipeListViewDetailedName = recipeView.findViewById(R.id.recipeListViewDetailedName);
                    recipeListViewDetailedName.setText(recipe.name);

                    final TextView recipeListViewDetailedAuthor = recipeView.findViewById(R.id.recipeListViewDetailedAuthor);
                    recipeListViewDetailedAuthor.setText(String.format("Created by %s", recipe.author));

                    final TextView recipeListViewDetailedDescription = recipeView.findViewById(R.id.recipeListViewDetailedDescription);
                    recipeListViewDetailedDescription.setText(recipe.description);

                    final TextView recipeListViewDetailedEstimatedTime = recipeView.findViewById(R.id.recipeListViewDetailedEstimatedTime);
                    recipeListViewDetailedEstimatedTime.setText(String.format(Locale.US, "Estimated Time: %.2f hours", recipe.estimatedTime));

                    recipeView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RecipeDetailed.recipeToView = new RecipeDetailed(recipe);
                            Intent recipeViewIntent = new Intent(activity, RecipeActivity.class);
                            activity.startActivity(recipeViewIntent);
                        }
                    });

                    recipesList.addView(recipeView);
                }
            }
        }
    }

    private static class GetUsernameTask extends SQLAsyncTask<Integer, String> {

        WeakReference<TextView> weakUsernameTextView;

        public GetUsernameTask(Activity activity, TextView usernameTextView) {
            super(activity);
            weakUsernameTextView = new WeakReference<>(usernameTextView);
        }

        @Override
        protected String sqlBackground(Connection connection, Integer... integers) throws SQLException {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT username FROM Users WHERE idUsers=?");
            preparedStatement.setInt(1, integers[0]);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("username");
            }
            return null;
        }

        @Override
        protected void sqlPostExecute(Activity activity, String string) {
            if (weakUsernameTextView.get() != null) {
                weakUsernameTextView.get().setText(string);
            }
        }
    }
}
