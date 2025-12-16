package com.retailanalyticalsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Databaseconnection {

    private static final String URL = "jdbc:mysql://localhost:3308/retailer_system"; // change 'retaildb' to your DB name
    private static final String USER = "root"; 
    private static final String PASSWORD = ""; 

    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database connected successfully!");
            } catch (ClassNotFoundException e) {
                System.out.println("MySQL JDBC Driver not found.");
                e.printStackTrace();
            } catch (SQLException e) {
                System.out.println("Failed to connect to the database.");
                e.printStackTrace();
            }
        }
        return connection;
    }
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("Connection test successful!");
        } else {
            System.out.println("Connection test failed.");
        }
        closeConnection();
    }
}
