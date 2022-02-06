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

    private  int orderid;
    private int transactionid;
    private int productid;
    private int quantity;
    private  String tablename;
    private String waitername;
    private String productname;
    private  String tableName;

    public Order(int orderID, int transactionID, int productID ,String productName ,int qquantity, String tName, String waiterN) {
        this.orderid = orderID;
        this.productid = productID;
        this.productname = productName;
        this.transactionid = transactionID;
        this.quantity = qquantity;
        this.tablename = tName;
        this.waitername = waiterN;
    }


    public int getId() {
        return orderid;
    }

    public String getTableN() {
        return this.tableName;
    }

    public void setTname(String tablename){
        this.tableName = tablename;
    }
    public void setTableName(String tableN){
        this.tablename = tableN;
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
    public String getWaitername() {
        return waitername;
    }

    public int getOrderid() {
        return orderid;
    }

    public int getProductid() {
        return productid;
    }

    public int getTransactionid() {
        return transactionid;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getTableName() {
        return tablename;
    }

    public String getWaiterid() {
        return waitername;
    }

    public String getProductname() {
        return productname;
    }
}
