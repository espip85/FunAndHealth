package com.paulaespitia.recipenetwork;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SQLHelper {

    private static SQLHelper sqlHelper;
    private static Properties info;

    private SQLHelper() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static SQLHelper getHelper() {
        if (sqlHelper == null) {
            sqlHelper = new SQLHelper();
            info = new Properties();
            info.put("user", "root");
            info.put("password","student");
        }
        return sqlHelper;
    }

    public Connection getConnection() throws SQLException {
        //10.0.2.2 refers to machine emulator is running on
        return DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/RecipeNetwork", info);
    }

    public boolean testConnection() {
        try {
            getConnection();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

}
