package com.retailanalyticalsystem;

import java.sql.Connection;

public class TestDBConnection {
    public static void main(String[] args) {
        Connection conn = Databaseconnection.getConnection(); // get connection

        if (conn != null) {
            System.out.println("Connection works!");
        } else {
            System.out.println("Connection failed.");
        }

        Databaseconnection.getConnection(); // close it
    }
}
