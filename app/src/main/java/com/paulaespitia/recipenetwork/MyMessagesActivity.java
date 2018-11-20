package com.paulaespitia.recipenetwork;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class MyMessagesActivity extends Activity{

    private TableLayout messagesTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_messages);

        final TextView subject = findViewById(R.id.myMessagesSubject);
        final TextView date = findViewById(R.id.myMessagesDate); // ??? dont have a date in the db
        final EditText search = findViewById(R.id.myMessagesSearch);
        messagesTable = (TableLayout) findViewById(R.id.myMessagesTableLayout);


        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override


            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                try (Connection connection = SQLHelper.getHelper().getConnection()) {
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT subject FROM Messages WHERE subject LIKE =?");
                    preparedStatement.setString(1, v.getText().toString());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    ArrayList<Message> messages = new ArrayList<>();

                    while (resultSet.next()) {
                        if(subject.getText().toString().equalsIgnoreCase(resultSet.getString("subject")))
                        {
                            Message message = new Message();
                            message.setSubject(resultSet.getString("subject"));
//                            message.setDate(resultSet.getString("date")); ??
                            messages.add(message);
                            addRowForDisplayingFoundMessage(v, message);
                        }

                        else {
                            Toast.makeText(getApplicationContext(), "Subject not found", Toast.LENGTH_SHORT);
                        }
                    }


                } catch (ClassNotFoundException | SQLException e) {
                    Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                }
                return false;
            }



        });

}

    //??Can i put this method here??

    public void addRowForDisplayingFoundMessage (View v, Message message) {
        //Create a new table row
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.editor_messages, null);
        messagesTable.addView(rowView, messagesTable.indexOfChild((View) v.getParent()) + 1);

        //Insert message date and subject into table
        TableRow tableRow = (TableRow) messagesTable.getChildAt(0);
        TextView subject = (TextView) tableRow.getChildAt(0);
        TextView date = (TextView) tableRow.getChildAt(1);
        subject.setText(message.getSubject());
//        date.setText(message.getDate());  include date in the message class // date what data type?

    }

}
