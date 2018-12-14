package com.paulaespitia.recipenetwork;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.paulaespitia.recipenetwork.model.Recipe;
import com.paulaespitia.recipenetwork.model.RecipeDetailed;
import com.paulaespitia.recipenetwork.model.SQLAsyncTask;

import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class HomeActivity extends MenuActivity {

    View viewHighestRatedRecipe;
    View viewMostPopularRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewHighestRatedRecipe = findViewById(R.id.homeHighestRatedRecipe);
        GetHighestRatedRecipeTask getHighestRatedRecipeTask = new GetHighestRatedRecipeTask(this, viewHighestRatedRecipe);
        getHighestRatedRecipeTask.execute();

        viewMostPopularRecipe = findViewById(R.id.homeMostPopularRecipe);
        GetMostPopularRecipeTask getMostPopularRecipeTask = new GetMostPopularRecipeTask(this, viewMostPopularRecipe);
        getMostPopularRecipeTask.execute();

    }

    private static class GetHighestRatedRecipeTask extends SQLAsyncTask<Void, Recipe> {

        final WeakReference<View> weakViewHighestRatedRecipe;

        GetHighestRatedRecipeTask(Activity activity, View viewHighestRatedRecipe) {
            super(activity);
            weakViewHighestRatedRecipe = new WeakReference<>(viewHighestRatedRecipe);
        }

        @Override
        protected Recipe sqlBackground(Connection connection, Void... voids) throws SQLException {
            PreparedStatement preparedStatement = connection.prepareStatement("call RecipeNetwork.get_highest_rated_recipes()");
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Integer> recipes = new ArrayList<>();
            while (resultSet.next()) {
                recipes.add(resultSet.getInt("idRecipes"));
            }
            Random rand = new Random();
            Integer recipeID = recipes.get(rand.nextInt(recipes.size()));
            preparedStatement = connection.prepareStatement("call RecipeNetwork.get_recipe_info(?)");
            preparedStatement.setInt(1, recipeID);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String recipeName = resultSet.getString("name");
                String author = resultSet.getString("author");
                String description = resultSet.getString("description");
                Double estimatedTime = resultSet.getDouble("estimatedTime");
                return new Recipe(recipeID, recipeName, author, description, estimatedTime);
            }
            return null;
        }

        @Override
        protected void sqlPostExecute(final Activity activity, final Recipe recipe) {
            final View viewHighestRatedRecipe = weakViewHighestRatedRecipe.get();
            if (recipe == null) {
                Toast.makeText(activity.getApplicationContext(), "Unable to retrieve recipe information", Toast.LENGTH_SHORT).show();
            } else if (viewHighestRatedRecipe != null){
                final TextView recipeListViewDetailedName = viewHighestRatedRecipe.findViewById(R.id.recipeListViewDetailedName);
                recipeListViewDetailedName.setText(recipe.name);

                final TextView recipeListViewDetailedAuthor = viewHighestRatedRecipe.findViewById(R.id.recipeListViewDetailedAuthor);
                recipeListViewDetailedAuthor.setText(String.format("Created by %s", recipe.author));

                final TextView recipeListViewDetailedDescription = viewHighestRatedRecipe.findViewById(R.id.recipeListViewDetailedDescription);
                recipeListViewDetailedDescription.setText(recipe.description);

                final TextView recipeListViewDetailedEstimatedTime = viewHighestRatedRecipe.findViewById(R.id.recipeListViewDetailedEstimatedTime);
                recipeListViewDetailedEstimatedTime.setText(String.format(Locale.US, "Estimated Time: %.2f hours", recipe.estimatedTime));

                viewHighestRatedRecipe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecipeDetailed.recipeToView  = new RecipeDetailed(recipe);
                        Intent recipeViewIntent = new Intent(activity, RecipeActivity.class);
                        activity.startActivity(recipeViewIntent);
                    }
                });
            }
        }
    }

    private static class GetMostPopularRecipeTask extends SQLAsyncTask<Void, Recipe> {

        final WeakReference<View> weakViewMostPopularRecipe;

        GetMostPopularRecipeTask(Activity activity, View viewMostPopularRecipe) {
            super(activity);
            weakViewMostPopularRecipe = new WeakReference<>(viewMostPopularRecipe);
        }

        @Override
        protected Recipe sqlBackground(Connection connection, Void... voids) throws SQLException {
            PreparedStatement preparedStatement = connection.prepareStatement("call RecipeNetwork.get_most_popular_recipes()");
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Integer> recipes = new ArrayList<>();
            while (resultSet.next()) {
                recipes.add(resultSet.getInt("idRecipes"));
            }
            Random rand = new Random();
            Integer recipeID = recipes.get(rand.nextInt(recipes.size()));
            preparedStatement = connection.prepareStatement("call RecipeNetwork.get_recipe_info(?)");
            preparedStatement.setInt(1, recipeID);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String recipeName = resultSet.getString("name");
                String author = resultSet.getString("author");
                String description = resultSet.getString("description");
                Double estimatedTime = resultSet.getDouble("estimatedTime");
                return new Recipe(recipeID, recipeName, author, description, estimatedTime);
            }
            return null;
        }

        @Override
        protected void sqlPostExecute(final Activity activity, final Recipe recipe) {
            View viewMostPopularRecipe = weakViewMostPopularRecipe.get();
            if (recipe == null) {
                Toast.makeText(activity.getApplicationContext(), "Unable to retrieve recipe information", Toast.LENGTH_SHORT).show();
            } else if (viewMostPopularRecipe != null){
                final TextView recipeListViewDetailedName = viewMostPopularRecipe.findViewById(R.id.recipeListViewDetailedName);
                recipeListViewDetailedName.setText(recipe.name);

                final TextView recipeListViewDetailedAuthor = viewMostPopularRecipe.findViewById(R.id.recipeListViewDetailedAuthor);
                recipeListViewDetailedAuthor.setText(String.format("Created by %s", recipe.author));

                final TextView recipeListViewDetailedDescription = viewMostPopularRecipe.findViewById(R.id.recipeListViewDetailedDescription);
                recipeListViewDetailedDescription.setText(recipe.description);

                final TextView recipeListViewDetailedEstimatedTime = viewMostPopularRecipe.findViewById(R.id.recipeListViewDetailedEstimatedTime);
                recipeListViewDetailedEstimatedTime.setText(String.format(Locale.US, "Estimated Time: %.2f hours", recipe.estimatedTime));

                viewMostPopularRecipe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecipeDetailed.recipeToView = new RecipeDetailed(recipe);
                        Intent recipeViewIntent = new Intent(activity, RecipeActivity.class);
                        activity.startActivity(recipeViewIntent);
                    }
                });
            }
        }
    }
}
