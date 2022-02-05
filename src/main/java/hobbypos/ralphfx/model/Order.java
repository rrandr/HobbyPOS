/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hobbypos.ralphfx.model;

/**
 *
 * @author Armero
 */
public class Order {
    
     private  int id;
     private  String tableName;

 

    public int getId() {
        return id;
    }

    public String getTableName() {
        return this.tableName;
    }
    
    public void setTablename(String tablename){
        this.tableName = tablename;
    } 
    public void setTableID(int tableID){
        this.id = tableID;
    }
    
}
