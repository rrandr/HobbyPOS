/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hobbypos.ralphfx.model;

/**
 * @author Armero
 */

import java.sql.Blob;

/**
 * @author Admin
 */
public class Items {
    private int productid;
    private String barcode;
    private String productname;
    private int price;
    private int quantity;
    private String discount;
    private Blob image;

    public Items(int id, String barcode , String prodname , int prices, int quan , String disc,Blob img) {
        this.productid = id;
        this.barcode = barcode;
        this.productname = prodname;
        this.price = prices;
        this.quantity = quan;
        this.discount = disc;
        this.image = img;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getProductname() {
        return productname;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getPrice() {
        return price;
    }

    public int getProductid() {
        return productid;
    }

    public String getDiscount() {
        return discount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }



    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }
}
