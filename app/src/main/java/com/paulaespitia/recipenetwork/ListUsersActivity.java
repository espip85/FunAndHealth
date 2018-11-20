package com.paulaespitia.recipenetwork;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ListUsersActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);

        final ImageView userPicture = findViewById(R.id.listUsersPicture);
        final TextView username = findViewById(R.id.listUsersUsername);
        final EditText search = findViewById(R.id.listUsersSearch);

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                try (Connection connection = SQLHelper.getHelper().getConnection()) {
                    PreparedStatement preparedStatement =  connection.prepareStatement("SELECT 1 FROM Users WHERE username =?" );
                    preparedStatement.setString(1, v.getText().toString());
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        if (resultSet.getBoolean(1)) {
                            username.setText("Valid search!");
                        } else {
                            Toast.makeText(getApplicationContext(),"Invalid user search!", Toast.LENGTH_LONG).show();
                        }
                    }

                    while (resultSet.next()){
                        User displayUser = new User();
                        displayUser.setUsername(resultSet.getString("username"));
                        displayUser.setPicture(resultSet.getString("picture")); // ????? picture path
                    }
                } catch (ClassNotFoundException | SQLException e) {
                    Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

    }
}
