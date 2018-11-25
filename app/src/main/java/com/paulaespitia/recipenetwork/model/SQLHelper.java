package com.paulaespitia.recipenetwork.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

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

}
