/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hobbypos.ralphfx.modal;

/**
 * @author Armero
 */

import hobbypos.ralphfx.model.User;

import java.sql.*;


public class DataObj {
   //private static final String DATABASE_URL = "jdbc:mysql://192.168.1.7:3306/hobbydb";
   private static final String DATABASE_URL = "jdbc:mysql://127.0.0.1:3306/hobbydb";
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
            User user;
            if (resultSet.next()) {
                user = new User(resultSet.getInt("userType"),resultSet.getString("firstName"),resultSet.getString("lastName"));
                userType = user.getUserType();
                return true;
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return false;
    }
    int userType;
    public boolean validateAdmin(String userID, String password) {

        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY);
            preparedStatement.setString(1, userID);
            preparedStatement.setString(2, password);

            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println(resultSet);
            User user;
            if (resultSet.next()) {
                user = new User(resultSet.getInt("userType"),resultSet.getString("firstName"),resultSet.getString("lastName"));
                    userType = user.getUserType();
                if(user.getUserType()==1){
                        return true;
                    }

            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return false;
    }


    public Integer getUserType(){
        return userType;
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

