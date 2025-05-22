package org.example;

import java.sql.*;


public class Main {
    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/user_service_db", "user_service", "1");
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT 1;");
        while (rs.next()) {
            System.out.println("Result: " + rs.getInt(1));
        }
    }

}