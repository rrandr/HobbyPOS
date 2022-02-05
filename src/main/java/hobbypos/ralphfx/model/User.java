/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hobbypos.ralphfx.model;

/**
 *
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
    

    public User(int usertype, String firstname,String lastname) {
        this.userType = usertype;
        this.firstName = firstname;
        this.lastName = lastname;
    }

  
    
    public int getUserType(int usertype){
        return userType;
    }
    
    public String userLocation(String loc){
        return location;
    }
    
    public int getId() {
        return userID;
    }

    public String getName() {
        return name;
    }
    
}
