/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hobbypos.ralphfx.model;

/**
 * @author Armero
 */
public class User {
    private int userID;
    private int userType;
    private String firstName;
    private String lastName;
    private String name;
    private String username;
    private String location;


    public User(int usertype, String firstname, String lastname) {
        this.userType = usertype;
        this.firstName = firstname;
        this.lastName = lastname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserID() {
        return userID;
    }

    public int getUserType() {
        return userType;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

}
