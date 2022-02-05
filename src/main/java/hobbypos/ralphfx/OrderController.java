/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package hobbypos.ralphfx;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javax.imageio.ImageIO;
import hobbypos.ralphfx.modal.DataObj;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Armero
 */
public class OrderController implements Initializable {

    private Label tableNum;
    @FXML
    private TilePane catTile;
    @FXML
    private TilePane productBox;
    @FXML
    private Text tableNumtxt;
    @FXML
    private Label lblUsername;


    Scene fxmlFile;
    Parent root;
    Stage window;

    private static Stage pStage;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            getCategories();
          
        } catch (SQLException ex) {
            Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    private void setPrimaryStage(Stage pStage) {
       OrderController.pStage = pStage;
    }
    private void openModalWindow(String resource, String title) throws IOException {
        root = FXMLLoader.load(getClass().getResource(resource));
        fxmlFile = new Scene(root);
        window = new Stage();
        window.setScene(fxmlFile);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setAlwaysOnTop(true);
        window.setIconified(false);
//        window.initStyle(StageStyle.UNDECORATED);
        window.setTitle(title);
        setPrimaryStage(window);
        window.showAndWait();
    }

    @FXML
    private void handleKeyPressed(KeyEvent event) throws IOException {
        System.out.println("Key pressed: " + event.getCode());
        switch (event.getCode()) {
            case F1:
                System.out.println("New Order");
                try {
                    openModalWindow("Order.fxml", "New Order");
                } catch (Exception ex) {
                    System.out.println("" + ex.getMessage());
                    ex.printStackTrace();
                }
                break;
            case F2:
                System.out.println("---");
                break;
            case F3:
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Cancel Order");
                alert.setHeaderText("Are you sure want to cancel Order?");

                // option != null.
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get() == ButtonType.OK) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
                    Parent root = fxmlLoader.load();

                    Stage stage = new Stage();
                    stage.setTitle("Hobby Bar POS | Dashboard");
                    stage.setScene(new Scene(root));
                    stage.show();
                    ((Node) (event.getSource())).getScene().getWindow().hide();

                } else if (option.get() == ButtonType.CANCEL) {

                }
                break;
            case F4:
                System.out.println("Manage products.");
                try {
                    openModalWindow("Products.fxml", "Manage Products");
                } catch (Exception ex) {
                    System.out.println("" + ex.getMessage());
                    ex.printStackTrace();
                }
                break;
            case F5:
                System.out.println("Manage Table");
                try {
                    openModalWindow("Tables.fxml", "Manage Tables");
                } catch (Exception ex) {
                    System.out.println("" + ex.getMessage());
                    ex.printStackTrace();
                }
                break;
            case F6:
                System.out.println("Sales Report");
                break;
            case F7:
                System.out.println("Logout.");
                try {

                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
                        Parent root = fxmlLoader.load();

                        Stage stage = new Stage();
                        stage.setTitle("Hobby Bar POS | Dashboard");
                        stage.setScene(new Scene(root));
                        stage.show();
                        ((Node) (event.getSource())).getScene().getWindow().hide();


                } catch (Exception ex) {
                    System.out.println("" + ex.getMessage());
                    ex.printStackTrace();
                }
                break;

        }
    }
    
     public void setUsername(String username) {
        tableNumtxt.setText(username);
    }
    
  

    
    private void getProduct(String category) throws SQLException, IOException {
        DataObj jdbcDao = new DataObj();
        Connection conn = jdbcDao.getConnection();
        Statement statement = conn.createStatement();
        String sqlQuery = "SELECT * FROM products WHERE category='"+ category +"';";
        System.err.printf(sqlQuery);
        ResultSet resultSet = statement.executeQuery(sqlQuery);

        List<StackPane> buttonlist = new ArrayList<>(); //our Collection to hold newly created Buttons
     
     
        try {

            while (resultSet.next()) { //iterate over every row returned
                
                StackPane stackPane = new StackPane();
                String productname = resultSet.getString("description");
                String productid = resultSet.getString("pluID");
                String price = resultSet.getString("price");//extract button text, adapt the String to the columnname that you are interested in
                java.sql.Blob blob = resultSet.getBlob("image");
                
                 
                InputStream in = blob.getBinaryStream();  
                BufferedImage images = ImageIO.read(in);
                Image image = javafx.embed.swing.SwingFXUtils.toFXImage(images, null);
                ImageView view = new ImageView(image);
                view.setFitHeight(100);
                view.setPreserveRatio(true);
                Button btn = new Button();
                btn.setTranslateX(20);
                btn.setTranslateY(30);
                btn.setPrefSize(130, 130);
                btn.setMaxHeight(130);
                btn.setMaxWidth(130);
                btn.setGraphic(view);
                btn.setId(productid);
                btn.setOnAction(event -> {
                   
                    System.out.println(productname +" - " + productid);
                     });
              stackPane.getChildren().add(btn);
              
              Label background = new Label ("");
              background.setTranslateX(20);
              background.setTranslateY(85);
              background.setPrefSize(130, 20);
              background.setStyle("-fx-background-color: #989898; -fx-opacity: 80%;");
              stackPane.getChildren().add(background);
              
              Label Total = new Label ("Price : ₱" + price);
              Total.setTranslateX(10);
              Total.setTranslateY(85);
              Total.setStyle("-fx-font-color:white; -fx-font-weight:bold;");
              stackPane.getChildren().add(Total);
              
              Label pbackground = new Label ("");
              pbackground.setTranslateX(20);
              pbackground.setTranslateY(-25);
              pbackground.setPrefSize(130, 20);
              pbackground.setStyle("-fx-background-color: #0277A6; -fx-opacity: 80%;");
              stackPane.getChildren().add(pbackground);
              
              Label productName = new Label (productname);
              productName.setTranslateX(0);
              productName.setTranslateY(-25);
              productName.setStyle("-fx-text-align: center; -fx-font-color:white; -fx-font-weight:bold;");
              stackPane.getChildren().add(productName);
              buttonlist.add(stackPane); 
                
              
            }
            
            productBox.getChildren().clear(); //remove all Buttons that are currently in the container
            productBox.setPadding(new Insets(10, 10, 10, 10));
            productBox.setHgap(10);
            productBox.setVgap(10);
            productBox.getChildren().addAll(buttonlist); //then add all your Buttons that you just created
            System.out.println("Pasok Ako: " + resultSet);
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            resultSet.close();
            statement.close();
            conn.close();
        }
    }
    private void getCategories() throws SQLException{
        
    DataObj jdbcDao = new DataObj();
        Connection conn = jdbcDao.getConnection();
        Statement statement = conn.createStatement();
        String sqlQuery = "SELECT * FROM categories;";
        ResultSet resultSet = statement.executeQuery(sqlQuery);

        List<StackPane> buttonlist = new ArrayList<>(); //our Collection to hold newly created Buttons
     
     
        try {

            while (resultSet.next()) { //iterate over every row returned
                StackPane stackPane = new StackPane();
                String category = resultSet.getString("name");
                String categoryid = resultSet.getString("id");//extract button text, adapt the String to the columnname that you are interested in
                Button btn = new Button(category);
                btn.setTranslateX(8);
               // btn.setTranslateY(20);
                btn.setPrefSize(150, 40);
                //btn.setMaxHeight(30);
               // btn.setMaxWidth(250);
                btn.setId(categoryid);
                btn.setOnAction(event -> {  
                    try {
                        getProduct(category);
                    } catch (SQLException ex) {
                        Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println(category +" - " + categoryid);
                     });
                stackPane.getChildren().add(btn);
                buttonlist.add(stackPane); 
            }
            
            catTile.getChildren().clear(); //remove all Buttons that are currently in the container
            catTile.setPadding(new Insets(10, 10, 10, 10));
            catTile.setHgap(10);
            catTile.setVgap(10);
            catTile.getChildren().addAll(buttonlist); //then add all your Buttons that you just created
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            resultSet.close();
            statement.close();
            conn.close();
        }

}
    
    
}