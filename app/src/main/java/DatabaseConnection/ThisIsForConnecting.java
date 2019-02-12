package DatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ThisIsForConnecting {


    Statement st;
    public Connection connection;










    public void ThisIsForConnecting1() {


        String url = "ecarte.mysql.database.azure.com";
        String username = "masm@ecarte";
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
