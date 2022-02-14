/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hobbypos.ralphfx.modal;

/**
 * @author Armero
 */

import java.sql.*;


public class DataObj {
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/hobbydb";
    private static final String DATABASE_USERNAME = "root";
    private static final String DATABASE_PASSWORD = "rajv6dyykl";
    private static final String SELECT_QUERY = "SELECT * FROM user WHERE userID = ? AND password = ?";

    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (ex instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState : " + ((SQLException) e).getSQLState());
                System.err.println("Error Code : " + ((SQLException) e).getErrorCode());
                System.err.println("Message : " + ex.getMessage());
                Throwable t = ex.getCause();

                while (t != null) {
                    System.err.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

    public boolean validate(String userID, String password) {

        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY);
            preparedStatement.setString(1, userID);
            preparedStatement.setString(2, password);

            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println(resultSet);

            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return false;
    }

    public Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
            return connection;
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        return null;
    }
}

