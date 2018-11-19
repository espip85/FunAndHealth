package com.paulaespitia.recipenetwork;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mysql.cj.protocol.Resultset;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class MessageWindowActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_window);

        final EditText recipient = findViewById(R.id.messageWindowRecipient);
        final EditText subject = findViewById(R.id.messageWindowSubject);
        final EditText message = findViewById(R.id.messageWindowMessage);
        final Button send = findViewById(R.id.messageWindowSendMessage);

        send.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                try {
                    Connection connection = SQLHelper.getHelper().getConnection();
                    //VERIFY IF RECIPIENT EXISTS!
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT 1 FROM Users WHERE username =?");
                    preparedStatement.setString(1, recipient.getText().toString());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()){
                        if (resultSet.getBoolean(1)){
                            recipient.setText("Message sent");
                        } else {
                            recipient.setText("Invalid user");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });




    }
}
