package com.example.project001.database;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class DBConnection {

    //variables
    private Statement statement;
    private Connection connection;
    private ResultSet resultSet;
    private String url = "jdbc:mysql://194.47.41.87:3306/moviedatabase";
    private String username = "root";
    private String password = "root";


    public DBConnection() {
        Log.e("SQL", "Before");
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            Log.e("SQL", "Connected!");

        }catch(Exception e) {
            Log.e("---------------", e.toString());
        }
    }

    public void getName() {
        try {
            String query = "select * from actor";
            resultSet = statement.executeQuery(query);
            System.out.println("Records from database: ");
            while(resultSet.next()) {
                String name = resultSet.getString("name");
                System.out.println("Name: " + name);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

    }

}