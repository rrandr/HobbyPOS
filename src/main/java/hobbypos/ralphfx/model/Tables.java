/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hobbypos.ralphfx.model;

/**
 * @author Admin
 */
public class Tables {
    private int id;
    private int tableavail;
    private String name;

    public Tables(int id, String name, int avail) {
        this.id = id;
        this.name = name;
        this.tableavail = avail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTableavail() {
        return tableavail;
    }

    public void setTableavail(int tableavail) {
        this.tableavail = tableavail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
