package com.paulaespitia.recipenetwork;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {

    private static SQLHelper sqlHelper;

    private SQLHelper() throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
    }

    public static SQLHelper getHelper() throws ClassNotFoundException {
        if (sqlHelper == null) {
            sqlHelper = new SQLHelper();
        }
        return sqlHelper;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/RecipeNetwork", "root", "student");
    }

}
