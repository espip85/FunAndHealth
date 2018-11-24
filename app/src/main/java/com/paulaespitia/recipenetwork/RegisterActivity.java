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

        final EditText usernameEditText = findViewById(R.id.registerUsername);
        final EditText emailEditText = findViewById(R.id.registerEmail);
        final EditText passwordEditText = findViewById(R.id.registerPassword);
        final EditText verifyPasswordEditText = findViewById(R.id.registerVerifyPassword);
        final Button registerButton = findViewById(R.id.registerRegister);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!emailEditText.getText().toString().matches("([\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4})")) {
                    Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
                } else if (!passwordEditText.getText().toString().equals(verifyPasswordEditText.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    passwordEditText.setText("");
                    verifyPasswordEditText.setText("");
                } else {
                    RegisterTask registerTask = new RegisterTask(RegisterActivity.this);
                    registerTask.execute(usernameEditText.getText().toString(), passwordEditText.getText().toString(), emailEditText.getText().toString());
                }
            }
        });
    }

    private static class RegisterTask extends SQLAsyncTask<String, Void> {

        boolean usernameInUse = false;
        boolean emailInUse = false;

        private RegisterTask(Activity activity) {
            super(activity);
        }

        @Override
        protected Void sqlBackground(Connection connection, String... strings) throws SQLException {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT username,email FROM Users WHERE username=? OR email=?");
            preparedStatement.setString(1, strings[0]); // username
            preparedStatement.setString(2, strings[2]); // email
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (strings[0].equalsIgnoreCase(resultSet.getString("username"))) {
                    usernameInUse = true;
                }
                if (strings[1].equalsIgnoreCase(resultSet.getString("email"))) {
                    emailInUse = true;
                }
            }
            if (!usernameInUse && !emailInUse) {
                preparedStatement = connection.prepareStatement("INSERT INTO Users(username,password,email) VALUES (?,?,?)");
                preparedStatement.setString(1, strings[0]); // username
                preparedStatement.setString(2, strings[1]); // password
                preparedStatement.setString(3, strings[2]); // email
                preparedStatement.executeUpdate();
            }
            return null;
        }

        @Override
        protected void sqlPostExecute(Activity activity, Void aVoid) {
            if (usernameInUse && emailInUse) {
                Toast.makeText(activity.getApplicationContext(), "Username and Email in use", Toast.LENGTH_SHORT).show();
            } else if (usernameInUse) {
                Toast.makeText(activity.getApplicationContext(), "Username in use", Toast.LENGTH_SHORT).show();
            } else if (emailInUse) {
                Toast.makeText(activity.getApplicationContext(), "Email in use", Toast.LENGTH_SHORT).show();
            } else {
                activity.finish();
            }
        }
    }
}
