package com.paulaespitia.recipenetwork;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        final EditText sender = findViewById(R.id.messageWindowSender);
        final EditText recipient = findViewById(R.id.messageWindowRecipient);
        final EditText subject = findViewById(R.id.messageWindowSubject);
        final EditText message = findViewById(R.id.messageWindowMessage);
        final Button send = findViewById(R.id.messageWindowSendMessage);

        send.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                try  (Connection connection = SQLHelper.getHelper().getConnection()) {
                    //VERIFY IF RECIPIENT EXISTS!
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT 1 FROM Users WHERE username =?");
                    preparedStatement.setString(1, recipient.getText().toString());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()){
                        if (resultSet.getBoolean(1)) {
                            if (recipient.getText().toString().equalsIgnoreCase(resultSet.getString("username"))) {
                                preparedStatement = connection.prepareStatement("INSERT INTO Messages(sender,receiver, subject, message) VALUES (?,?,?,?)");
                                //???? sender and receiver are as int in the db

                                preparedStatement.setString(1, sender.getText().toString()); // i create the sender in the layout, how to read the user of the account in use??
                                preparedStatement.setString(2, recipient.getText().toString());
                                preparedStatement.setString(3, subject.getText().toString());
                                preparedStatement.setString(4, message.getText().toString());
                                Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Problem sending the message", Toast.LENGTH_SHORT);
                            }
                        }
                    }
                } catch (ClassNotFoundException | SQLException e) {
                    Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                }

            }
        });




    }
}
