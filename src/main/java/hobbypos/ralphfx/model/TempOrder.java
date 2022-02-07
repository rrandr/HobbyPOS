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
    private int price;
    private int quantity;
    private  String tablename;
    private String waitername;
    private String productname;
    private  String tableName;
    private int total;

    public TempOrder(int orderID, int transactionID, int productID ,String productName,int pPrice ,int qquantity, String tName, String waiterN) {
        this.orderid = orderID;
        this.productid = productID;
        this.productname = productName;
        this.price=pPrice;
        this.transactionid = transactionID;
        this.quantity = qquantity;
        this.tablename = tName;
        this.waitername = waiterN;
    }

    public TempOrder(String productname , int pPrice , int pQuantity , int ptotal){
        this.productname = productname;
        this.price=pPrice;
        this.quantity = pQuantity;
        this.total = ptotal;
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
    public void setPrice(int price) {
        this.price = price;
    }

    public void setTotal(int ptotal) {
        total = ptotal;
    }
    public int getTotal() {
        return total;
    }

    public String getWaitername() {
        return waitername;
    }

    public int getOrderid() {
        return orderid;
    }
    public int getPrice() {
        return price;
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