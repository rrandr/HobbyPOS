/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hobbypos.ralphfx.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author Admin
 */
public class Waiter {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty location;

    public Waiter(int wid, String wname, String wlocation) {
        this.id = new SimpleIntegerProperty(wid);
        this.name = new SimpleStringProperty(wname);
        this.location = new SimpleStringProperty(wlocation);
    }


    public int getId() {
        return id.get();
    }

    public void setId(int ID) {
        id.set(ID);
    }


    public String getName() {
        return name.get();
    }

    public void setName(String nme) {
        name.set(nme);
    }


    public String getLocation() {
        return location.get();
    }

    public void setLocation(String nme) {
        location.set(nme);
    }


}
