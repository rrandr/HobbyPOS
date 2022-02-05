/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hobbypos.ralphfx.model;

/**
 *
 * @author Armero
 */
import java.sql.Blob;
/**
 *
 * @author Admin
 */
public class Products {
    private Integer pluID;
    private String barcode;
    private String description;
    private String price;
    private String category;
    private Blob image;
    private String status;

    public Products(Integer id, String barcode, String description, String price, String category, Blob image, String status) {
        this.pluID = id;
        this.barcode = barcode;
        this.description = description;
        this.price = price;
        this.category = category;
        this.image = image;
        this.status = status;
    }

    public Integer getId() {
        return this.pluID;
    }
    
    public String getBarcode(){
        return barcode;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public Blob getImage() {
        return image;
    }

    public String getStatus() {
        return status;
    }
    
    
}
