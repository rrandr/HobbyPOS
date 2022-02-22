
package hobbypos.ralphfx.model;

/**
 * @author Armero
 */
public class KTVTempOrder {

    private int orderid;
    private String transactionid;
    private int productid;
    private int price;
    private int quantity;
    private String tablename;
    private String waitername;
    private String productname;
    private String tableName;
    private String drink;
    private String timer;
    private int total;

    public KTVTempOrder(int orderID, String transactionID, int productID, String productName, int pPrice, int qquantity, String tName, String waiterN, String drinks,String ttimer) {
        this.orderid = orderID;
        this.productid = productID;
        this.productname = productName;
        this.price = pPrice;
        this.transactionid = transactionID;
        this.quantity = qquantity;
        this.tablename = tName;
        this.waitername = waiterN;
        this.drink = drinks;
        this.timer = ttimer;
    }

    public KTVTempOrder(String productname, int pPrice, int pQuantity, int ptotal) {
        this.productname = productname;
        this.price = pPrice;
        this.quantity = pQuantity;
        this.total = ptotal;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getTimer() {
        return timer;
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
        return waitername;
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
        return tablename;
    }

    public void setTableName(String tableN) {
        this.tablename = tableN;
    }

    public String getWaiterid() {
        return waitername;
    }

    public String getProductname() {
        return productname;
    }
}
