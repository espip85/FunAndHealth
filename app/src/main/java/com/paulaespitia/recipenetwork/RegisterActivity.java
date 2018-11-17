package com.paulaespitia.recipenetwork;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterActivity extends Activity {

    @Override
    protected void onCreate (Bundle savedInstanceState){
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_register);

     final EditText username = findViewById(R.id.registerUsername);
     final EditText email = findViewById(R.id.registerEmail);
     final EditText password = findViewById(R.id.registerPassword);
     final EditText verifyPassword = findViewById(R.id.registerVerifyPassword);
     final Button register = findViewById(R.id.registerRegister);
     register.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v){
             try (Connection connection = SQLHelper.getHelper().getConnection()) {
                 // VERIFY USER OR EMAIL DOESNT EXIST ALREADY
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT EXIST (SELECT 1 FROM Users WHERE username =? OR email =?)");
                 preparedStatement.setString(1, username.getText().toString());
                 preparedStatement.setString(3, email.getText().toString());
                 ResultSet resultSet = preparedStatement.executeQuery();
                 if (resultSet.next()){
                    if(resultSet.getBoolean(1)) {
                        username.setText("Username already exists");
                    }
                    if (resultSet.getBoolean(3)){
                        email.setText("Email is in use");
                    }
                 } else {
                     username.setText("Valid");
                 }
                connection.close();
             }

             catch (ClassNotFoundException e) {
                 e.printStackTrace();
             } catch (SQLException e) {
                 e.printStackTrace();
             }

         }
        });



    }

}
