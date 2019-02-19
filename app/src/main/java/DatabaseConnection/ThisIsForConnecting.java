package DatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ThisIsForConnecting {


    Statement st;
    public Connection connection;










    public void ThisIsForConnecting1() {


        String url = "ecarte.mysql.database.azure.com/drive";
        String username = "masm@ecarte";
        String password = "Password1";


        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(url, username, password);
            st = connection.createStatement();
            System.out.println("Works");


            System.out.println("Connection Established");

        } catch (SQLException e) {
            System.out.println("No internet1");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("No internet2");
            e.printStackTrace();
        }


    }

}
