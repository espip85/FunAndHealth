package com.paulaespitia.recipenetwork;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.paulaespitia.recipenetwork.model.SQLHelper;
import com.paulaespitia.recipenetwork.model.User;
import com.paulaespitia.recipenetwork.model.UserID;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class FavoritesUsersActivity extends Activity {

    TableLayout favoritesUsersTable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_users);

        favoritesUsersTable = findViewById(R.id.favoritesUsersTable);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        List<UserID> favorites = getFavorites(User.currentUser.userId);
        for (UserID favorite : favorites) {
            final View rowView = inflater.inflate(R.layout.favorites_users_user, null);
            rowView.setTag(favorite.userID);
            TextView usernameTextView = rowView.findViewById(R.id.favoritesUsersUserUsername);
            usernameTextView.setText(favorite.username);
            favoritesUsersTable.addView(rowView);
        }
    }

    public void removeUser(View view) {
        View tableRow = (View) view.getParent();
        favoritesUsersTable.removeView(tableRow);
        Integer favoriteID = (Integer) tableRow.getTag();

        try (Connection conn = SQLHelper.getHelper().getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM FavoriteUsers WHERE idUsers=? AND favoriteID=?");
            preparedStatement.setInt(1, User.currentUser.userId);
            preparedStatement.setInt(2, favoriteID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<UserID> getFavorites(int id) {
        List<UserID> favorites = new LinkedList<>();
        try (Connection conn = SQLHelper.getHelper().getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT FavoriteUsers.favoriteID,Users.username FROM FavoriteUsers,Users WHERE FavoriteUsers.idUsers = ? AND FavoriteUsers.favoriteID=Users.idUsers");
            preparedStatement.setInt(1, User.currentUser.userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                favorites.add(new UserID(resultSet.getInt(1), resultSet.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return favorites;
    }
}
