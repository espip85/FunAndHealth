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

        final EditText username = findViewById(R.id.loginUsername);
        final EditText password = findViewById(R.id.loginPassword);

        final Button login = findViewById(R.id.loginLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Connection connection = SQLHelper.getHelper().getConnection();
                    // I realize this is an incredible bad way of implementing this but it's here for simplicities sake.
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT EXISTS(SELECT 1 FROM Users WHERE username=? AND password=?)");
                    preparedStatement.setString(1, username.getText().toString());
                    preparedStatement.setString(2, password.getText().toString());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        if (resultSet.getInt(1) == 1) {
                            username.setText("Valid!");
                        } else {
                            username.setText("Invalid!");
                        }
                    }
                    connection.close();
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
