package com.paulaespitia.recipenetwork;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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

public class FavoritesActivity extends ListActivity {
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        FavoritesListAdapter.ListItem item = (FavoritesListAdapter.ListItem) getListView().getAdapter().getItem(position);
        Toast.makeText(this, item.toString(), Toast.LENGTH_SHORT).show();
    }

    /*
    private TableLayout favoriteUsersTableLayout;
    private TableLayout favoriteRecipesTableLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        favoriteUsersTableLayout = findViewById(R.id.favoritesTableFavoriteUsers);
        favoriteRecipesTableLayout = findViewById(R.id.favoritesTableFavoriteRecipes);
    }

    public void addUser(View v){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.activity_favorites, null);
        favoriteUsersTableLayout.addView(rowView, favoriteUsersTableLayout.indexOfChild((View) v.getParent()) + 1); // why v.getParent?
    }


    public void removeUser(View v){

        if (favoriteUsersTableLayout.getChildCount() > 2){
            favoriteUsersTableLayout.removeView((View) v.getParent());
        } else {
            TableRow tableRow =  (TableRow) favoriteUsersTableLayout.getChildAt(1);
            TextView username = (TextView) tableRow.getChildAt(0);
            username.setText("");
        }
    }

    public void removeRecipe (View v){
        if(favoriteRecipesTableLayout.getChildCount() > 2){
            favoriteRecipesTableLayout.removeView((View)v.getParent());
        } else {
            TableRow tableRow = (TableRow) favoriteRecipesTableLayout.getChildAt(1);
            TextView recipeName =  (TextView) favoriteRecipesTableLayout.getChildAt(0);
            recipeName.setText("");
        }
    }

    public void addRecipe(View v){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.activity_favorites, null);
        favoriteRecipesTableLayout.addView(rowView, favoriteRecipesTableLayout.indexOfChild((View) v.getParent()) + 1);
    }



    public void getSearchUsername(View v){


//        Connection conn = null;
//        try {
//            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/RecipeNetwork", "root", "student");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        EditText searchUser = (EditText) findViewById(R.id.favoritesSearchUser);
//        String username = searchUser.getText().toString();
//        PreparedStatement pStatement = null;
//        try {
//            pStatement = conn.prepareStatement ("SELECT username FROM  Users  WHERE TRIM (username) = '"+username.trim()+"'");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        //clear the view of the table to display just one user?
//        TableRow tableRow = (TableRow) favoriteUsersTableLayout.getChildAt(1);
//        TextView userToDisplay = (TextView) tableRow.getChildAt(0);
//        userToDisplay.setText(username); // if username is valid works what to do if its not valid
//
//        // CLOSE CONECTION WHERE??
    }

*/
}


