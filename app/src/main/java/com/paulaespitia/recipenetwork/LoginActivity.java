package com.paulaespitia.recipenetwork;

import android.app.Activity;
import android.content.Intent;
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

        final Button loginButton = findViewById(R.id.loginLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginTask loginTask = new LoginTask(LoginActivity.this);
                loginTask.execute(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });

        final TextView register = findViewById(R.id.loginRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try (Connection connection = SQLHelper.getHelper().getConnection()) {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static class LoginTask extends SQLAsyncTask<String, User> {

        private LoginTask(Activity activity) {
            super(activity);
        }

        @Override
        protected User sqlBackground(Connection connection, String... strings) throws SQLException {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT idUsers,username,email FROM Users WHERE username=? AND password=?");
            preparedStatement.setString(1, strings[0]); // username
            preparedStatement.setString(2, strings[1]); // password
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("idUsers");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                return new User(id, username, email);
            }
            return null;
        }

        @Override
        protected void sqlPostExecute(Activity activity, User user) {
            if (user == null) {
                Toast.makeText(activity.getApplicationContext(), R.string.invalid_username_or_password, Toast.LENGTH_SHORT).show();
            } else {
                User.currentUser = user;
                Intent homeIntent = new Intent(activity, HomeActivity.class);
                activity.startActivity(homeIntent);
                Toast.makeText(activity.getApplicationContext(), String.format("Welcome %s!", user.username), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
