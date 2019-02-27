package com.example.project001.message.DatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ForConnecting {


    Statement st;
    public Connection connection;


    public void ThisIsForConnecting1() {


        String url = "jdbc:mysql://127.0.0.1:3306/travel";
        String username = "root1@localhost";
        String password = "Password1";


        try {
            connection = DriverManager.getConnection(url, username, password);
            st = connection.createStatement();
            System.out.println("Works");


            System.out.println("Connection Established");

        } catch (SQLException e) {
            System.out.println("No internet");
        } catch (Exception e){
            System.out.println("No internet");
        }


    }

}

