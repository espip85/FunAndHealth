package com.paulaespitia.recipenetwork;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText username = findViewById(R.id.registerUsername);
        final EditText email = findViewById(R.id.registerEmail);
        final EditText password = findViewById(R.id.registerPassword);
        final EditText verifyPassword = findViewById(R.id.registerVerifyPassword);
        final Button register = findViewById(R.id.registerRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!email.getText().toString().matches("([\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4})")) {
                    Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(verifyPassword)) {
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    password.setText("");
                    verifyPassword.setText("");
                } else {
                    try (Connection connection = SQLHelper.getHelper().getConnection()) {
                        // VERIFY USER OR EMAIL DOESNT EXIST ALREADY
                        PreparedStatement preparedStatement = connection.prepareStatement("SELECT username,email FROM Users WHERE username=? OR email=?");
                        preparedStatement.setString(1, username.getText().toString());
                        preparedStatement.setString(2, email.getText().toString());
                        ResultSet resultSet = preparedStatement.executeQuery();
                        if (resultSet.next()) {
                            if (username.getText().toString().equalsIgnoreCase(resultSet.getString("username"))) {
                                Toast.makeText(getApplicationContext(), "Username is already in use", Toast.LENGTH_SHORT).show();
                            }
                            if (email.getText().toString().equalsIgnoreCase(resultSet.getString("email"))) {
                                Toast.makeText(getApplicationContext(), "Email is already in use", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            preparedStatement = connection.prepareStatement("INSERT INTO Users(username,password,email) VALUES (?,?,?)");
                            preparedStatement.setString(1, username.getText().toString());
                            preparedStatement.setString(2, password.getText().toString());
                            preparedStatement.setString(3, email.getText().toString());
                            preparedStatement.execute();
                        }
                    } catch (ClassNotFoundException | SQLException e) {
                        Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
