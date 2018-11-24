package com.paulaespitia.recipenetwork;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class FavoritesRecipesActivity extends Activity {

    TableLayout favoritesRecipesTable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_recipes);

        favoritesRecipesTable = findViewById(R.id.favoritesRecipesTable);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        List<RecipeID> favorites = getFavorites(User.currentUser.userId);
        for (RecipeID favorite : favorites) {
            final View rowView = inflater.inflate(R.layout.favorites_recipes_recipe, null);
            rowView.setTag(favorite.recipeID);
            TextView nameTextView = rowView.findViewById(R.id.favoritesRecipesRecipeName);
            nameTextView.setText(favorite.name);
            TextView authorTextView = rowView.findViewById(R.id.favoritesRecipesRecipeAuthor);
            authorTextView.setText(String.format("%s %s", getResources().getString(R.string.created_by), favorite.author));
            favoritesRecipesTable.addView(rowView);
        }
    }

    public void removeRecipe(View view) {
        View tableRow = (View) view.getParent();
        favoritesRecipesTable.removeView(tableRow);
        Integer idRecipes = (Integer) tableRow.getTag();

        try (Connection conn = SQLHelper.getHelper().getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM FavoriteRecipes WHERE idUsers=? AND idRecipes=?");
            preparedStatement.setInt(1, User.currentUser.userId);
            preparedStatement.setInt(2, idRecipes);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<RecipeID> getFavorites(int id) {
        List<RecipeID> favorites = new LinkedList<>();
        try (Connection conn = SQLHelper.getHelper().getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT FavoriteRecipes.idRecipes,Recipes.name,Users.username FROM FavoriteRecipes,Recipes,Users WHERE FavoriteRecipes.idUsers=? AND Recipes.idRecipes=FavoriteRecipes.idRecipes AND Users.idUsers=Recipes.idUsers;");
            preparedStatement.setInt(1, User.currentUser.userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                favorites.add(new RecipeID(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return favorites;
    }
}
