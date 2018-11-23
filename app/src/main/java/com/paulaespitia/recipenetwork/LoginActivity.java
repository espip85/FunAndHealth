package com.paulaespitia.recipenetwork;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.loginUsername);
        final EditText passwordEditText = findViewById(R.id.loginPassword);

        final Button login = findViewById(R.id.loginLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try (Connection connection = SQLHelper.getHelper().getConnection()) {
                    // I realize this is an incredible bad way of implementing this but it's here for simplicities sake.
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT idUsers FROM Users WHERE username=? AND password=?");
                    String username = usernameEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, password);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        usernameEditText.setText("Valid");
                        int id = resultSet.getInt("idUsers");
                        User.currentUser = new User(id, username, password);
                    } else {
                        usernameEditText.setText("Invalid Password");
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        final TextView register = findViewById(R.id.loginRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
