/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hobbypos.ralphfx.model;

/**
 *
 * @author Armero
 */
public class TempOrder {

    private  int orderid;
    private int transactionid;
    private int productid;
    private int waiterid;
    private int quantity;
    private  int tableid;
    private  String tableName;

    public TempOrder(int orderID, int productID , int transactionID ,int qquantity,   int tableID, int waiterID ) {
        this.orderid = orderID;
        this.productid = productID;
        this.transactionid = transactionID;
        this.quantity = qquantity;
        this.tableid = tableID;
        this.waiterid = waiterID;
    }


    public int getId() {
        return orderid;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTablename(String tablename){
        this.tableName = tablename;
    }
    public void setTableID(int tableID){
        this.orderid = tableID;
    }
    public void setProductID(int productID){
        this.productid = productID;
    }
    public void setTransactionID(int transactionID){
        this.transactionid = transactionID;
    }
    public void setQuantity(int qquantity){
        this.quantity = qquantity;
    }


}
