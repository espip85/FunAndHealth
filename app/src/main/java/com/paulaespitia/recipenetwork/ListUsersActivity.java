package com.paulaespitia.recipenetwork;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.paulaespitia.recipenetwork.model.SQLHelper;
import com.paulaespitia.recipenetwork.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ListUsersActivity extends Activity {

    ListView searchListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);

//        final ImageView userPicture = findViewById(R.id.listUsersPicture);
//
//        final TextView username = findViewById(R.id.listUsersSearch);
        final EditText search = findViewById(R.id.listUsersSearch);
        searchListView = (ListView) findViewById(R.id.listUsersListView);

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                try (Connection connection = SQLHelper.getHelper().getConnection()) {
                    PreparedStatement preparedStatement =  connection.prepareStatement("SELECT 1 FROM Users WHERE username LIKE ?" ); //%%signs
                    preparedStatement.setString(1, v.getText().toString());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    ArrayList<User> users = new ArrayList<>();

                    while (resultSet.next()) {
                        if (search.getText().toString().equalsIgnoreCase(resultSet.getString("username"))) {
                            search.setText("Valid search!");
                            //User displayUser = new User();
                            //displayUser.setUsername(resultSet.getString("username"));
                            //displayUser.setPicture(resultSet.getString("picture")); // ????? picture path
                            //users.add(displayUser); //????? ADD TO LIST VIEW
                        } else {
                            Toast.makeText(getApplicationContext(),"Invalid user search!", Toast.LENGTH_LONG).show();
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
