/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hobbypos.ralphfx.model;

/**
 * @author Armero
 */
public class Order {

    private int orderid;
    private String transactionid;
    private int productid;
    private int price;
    private int quantity;
    private String waiterName;
    private String productname;
    private String tableName;
    private String orderDate;
    private String drink;
    private int total;

    public Order(int orderID, String transactionID, int productID, String productName, int pPrice, int qquantity, String tName, String waiterN, String drinks, String dates) {
        this.orderid = orderID;
        this.productid = productID;
        this.productname = productName;
        this.price = pPrice;
        this.transactionid = transactionID;
        this.quantity = qquantity;
        this.tableName = tName;
        this.waiterName = waiterN;
        this.drink=drinks;
        this.orderDate=dates;
    }

    public Order(String transactionID, String tName, String waiterN,String dates){
        this.transactionid = transactionID;
        this.tableName = tName;
        this.waiterName = waiterN;
        this.orderDate= dates;
    }

    public String getDrink() {
        return drink;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }

    public int getId() {
        return orderid;
    }

    public String getTableN() {
        return this.tableName;
    }

    public void setTname(String tablename) {
        this.tableName = tablename;
    }

    public void setProductID(int productID) {
        this.productid = productID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionid = transactionID;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int ptotal) {
        total = ptotal;
    }

    public String getWaitername() {
        return waiterName;
    }

    public int getOrderid() {
        return orderid;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getProductid() {
        return productid;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int qquantity) {
        this.quantity = qquantity;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableN) {
        this.tableName = tableN;
    }

    public String getWaiterid() {
        return waiterName;
    }

    public String getProductname() {
        return productname;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getTablename() {
        return tableName;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}
