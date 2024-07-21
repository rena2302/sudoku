package com.mycompany.project.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {

    private static final String URL = "jdbc:mysql://kil9uzd3tgem3naa.cbetxkdyhwsb.us-east-1.rds.amazonaws.com:3306/csfs5xyvewup926h";
    private static final String USER = "j6un19x3xeluji1r";
    private static final String PASSWORD = "nrdw6w8h933zikkr";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
