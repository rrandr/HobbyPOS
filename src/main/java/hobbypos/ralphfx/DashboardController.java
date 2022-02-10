/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package hobbypos.ralphfx;

import hobbypos.ralphfx.model.Order;
import hobbypos.ralphfx.model.Products;
import java.io.IOException;
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

import hobbypos.ralphfx.model.TempOrder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import hobbypos.ralphfx.modal.DataObj;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class DashboardController implements Initializable {

    @FXML
    private Label lblUsername;
    @FXML
    private Button btnManageTable;
    @FXML
    private Button newOrder;
    @FXML
    private Button addOrder;
    @FXML
    private Button modifyT;

    Scene fxmlFile;
    Parent root;
    Stage window;

    private static Stage pStage;
    @FXML
    private Button btnLookup;
    @FXML
    private TilePane availTable;
    @FXML
    private TilePane tableUsed;

    @FXML
    private TableColumn<TempOrder, Integer> itemQuan;

    @FXML
    private TableColumn<TempOrder, String> itemDesc;

    @FXML
    private TableColumn<TempOrder, Integer> itemPrice;

    @FXML
    private  ScrollPane scrollpane1;

    @FXML
    private  ScrollPane scrollpane2;

    @FXML
    private Label totalDisplay;

    @FXML
    private TableView<TempOrder> itemOrder;
    DataObj jbdc;
    Order orderData;
    String TableName;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            jdbc = new DataObj();

            retrieveTable();
            seeAvailableTable();
        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setUsername(String username) {
        lblUsername.setText(username);
    }

    private void setPrimaryStage(Stage pStage) {
        DashboardController.pStage = pStage;
    }

    public static Stage getPrimaryStage() {
        return pStage;
    }

    @FXML
    private void manageTable(ActionEvent event) {
        try {
            openModalWindow("Tables.fxml", "Manage Tables");
        } catch (Exception ex) {
            System.out.println("" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void manageLookup(ActionEvent event) {
        try {
            openModalWindow("Lookup.fxml", "Product Lookup");
        } catch (Exception ex) {
            System.out.println("" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    public void orderProp(ActionEvent event){
        newOrder.setDisable(true);
        addOrder.setDisable(true);
    }

    @FXML
    private void newOrder(ActionEvent event) {
        try {
                   FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Order.fxml"));
                        Parent root = fxmlLoader.load();
                        OrderController controller = (OrderController) fxmlLoader.getController();
                        controller.setUsername(TableName);
                        Stage stage = new Stage();
                        stage.setTitle("Hobby Bar POS | New Order");
                        stage.setScene(new Scene(root));
                        stage.show();
                        ((Node) (event.getSource())).getScene().getWindow().hide();

        } catch (Exception ex) {
            System.out.println("" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        try {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Logout");
            alert.setHeaderText("Are you sure want to Logout?");

            // option != null.
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get() == ButtonType.OK) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
                Parent root = fxmlLoader.load();

                Stage stage = new Stage();
                stage.setTitle("Hobby Bar POS | Login");
                stage.setScene(new Scene(root));
                stage.show();
                ((Node) (event.getSource())).getScene().getWindow().hide();

            } else if (option.get() == ButtonType.CANCEL) {

            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
    private void actionManageProduct(ActionEvent event) {
        try {
            openModalWindow("Products.fxml", "Manage Products");
        } catch (Exception ex) {

        }
    }

    @FXML
    private void handleKeyPressed(KeyEvent event) {
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
                System.out.println("Payment Triggered.");
                break;
            case F3:
                System.out.println("Cancel order.");
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
                try {
                openModalWindow("Lookup.fxml", "Product Lookup");
            } catch (Exception ex) {
                System.out.println("" + ex.getMessage());
                ex.printStackTrace();
            }
            break;
            case F8:
                System.out.println("Logout.");
                try {
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Logout");
                    alert.setHeaderText("Are you sure want to Logout?");

                    // option != null.
                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.get() == ButtonType.OK) {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
                        Parent root = fxmlLoader.load();

                        Stage stage = new Stage();
                        stage.setTitle("Hobby Bar POS | Login");
                        stage.setScene(new Scene(root));
                        stage.show();
                        ((Node) (event.getSource())).getScene().getWindow().hide();

                    } else if (option.get() == ButtonType.CANCEL) {

                    }

                } catch (Exception ex) {
                    System.out.println("" + ex.getMessage());
                    ex.printStackTrace();
                }
                break;

        }
    }


    private void seeAvailableTable() throws SQLException {

        DataObj jdbcDao = new DataObj();
        Connection conn = jdbcDao.getConnection();
        Statement statement = conn.createStatement();
        String sqlQuery = "SELECT * FROM tbltables WHERE tableavail = 0";
        ResultSet resultSet = statement.executeQuery(sqlQuery);

        List<StackPane> buttonlist = new ArrayList<>(); //our Collection to hold newly created Buttons

        try {
            while (resultSet.next()) { //iterate over every row returned
                StackPane stackPane = new StackPane();
                String tablename = resultSet.getString("name");
                String tableid = resultSet.getString("id");//extract button text, adapt the String to the columnname that you are interested in
                Button btn = new Button(tablename);
                btn.setTranslateX(8);
                btn.setPrefSize(150, 40);
                btn.setOnMousePressed(mouseEvent -> {
                    newOrder.setDisable(false);
                    modifyT.setDisable(true);
                    addOrder.setDisable(true);
                });

                btn.setId(tableid);
                btn.setOnAction(event -> {
                                TableName = tablename;
                                System.err.println(TableName);
                            });
                stackPane.getChildren().add(btn);
                buttonlist.add(stackPane);
            }


            availTable.getChildren().clear(); //remove all Buttons that are currently in the container
            availTable.setPadding(new Insets(10, 10, 10, 10));
            availTable.setHgap(10);
            availTable.setVgap(10);
            availTable.getChildren().addAll(buttonlist); //then add all your Buttons that you just created

            scrollpane1.setContent(availTable);
            System.out.println("Pasok diri: " + resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            resultSet.close();
            statement.close();
            conn.close();
        }

    }

    private void retrieveTable() throws SQLException {
        DataObj jdbcDao = new DataObj();
        Connection conn = jdbcDao.getConnection();
        Statement statement = conn.createStatement();
        String sqlQuery = "SELECT * FROM tbltables WHERE tableavail = 1";
        ResultSet resultSet = statement.executeQuery(sqlQuery);

        List<StackPane> buttonlist = new ArrayList<>(); //our Collection to hold newly created Buttons

        try {

            while (resultSet.next()) { //iterate over every row returned
                StackPane stackPane = new StackPane();
                String tablename = resultSet.getString("name");
                String tableid = resultSet.getString("id");//extract button text, adapt the String to the columnname that you are interested in
                URL _url = this.getClass().getResource("table.png");
                Image img = new Image((_url.toExternalForm()));
                ImageView view = new ImageView(img);
                view.setFitHeight(90);
                view.setPreserveRatio(true);
                Button btn = new Button();
                btn.setTranslateX(20);
                btn.setTranslateY(30);
                btn.setPrefSize(80, 100);
                btn.setMaxHeight(80);
                btn.setMaxWidth(100);
                btn.setGraphic(view);
                btn.setId(tableid);
                btn.setOnMousePressed(mouseEvent -> {
                    newOrder.setDisable(true);
                    addOrder.setDisable(false);
                    modifyT.setDisable(false);
                });

                btn.setOnAction(event -> {

                    TableName = tablename;
                    getOrderList(tablename);
                    System.err.println(TableName);

                });

                stackPane.getChildren().add(btn);
                Label label = new Label(tablename);
                label.setTranslateX(18);
                label.setTranslateY(3);
                label.getClass().getResource("style.css");

                stackPane.getChildren().add(label);
                Label Total = new Label("Total : ₱" + tableid);
                Total.setTranslateX(10);
                Total.setTranslateY(70);
                stackPane.getChildren().add(Total);
                buttonlist.add(stackPane);

            }

            tableUsed.getChildren().clear(); //remove all Buttons that are currently in the container
            tableUsed.setPadding(new Insets(10, 10, 10, 10));
            tableUsed.setHgap(10);
            tableUsed.setVgap(10);
            tableUsed.getChildren().addAll(buttonlist); //then add all your Buttons that you just created
            scrollpane2.setContent(tableUsed);


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            resultSet.close();
            statement.close();
            conn.close();
        }
    }



    DataObj jdbc;
    ObservableList<TempOrder> retrieveOrder = FXCollections.observableArrayList();
    int ttotalDisplay;

    private ObservableList<TempOrder> getOrderList(String tableN) {

        System.out.println("Pasok Ako sa getOrderList");
        ObservableList<TempOrder> tempOrderList = FXCollections.observableArrayList();
        Connection conn = jdbc.getConnection();
        String query = "SELECT * FROM temporder WHERE tableName='" + tableN + "'";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            TempOrder temporder;
            while (rs.next()) {
                temporder = new TempOrder(rs.getInt("orderid"), rs.getString("transactionid"), rs.getInt("productid"), rs.getString("productname"), rs.getInt("price"), rs.getInt("quantity"), rs.getString("tableName"), rs.getString("waiterName"));
                int Total = temporder.getPrice() * temporder.getQuantity();
                ttotalDisplay = ttotalDisplay + Total ;
                tempOrderList.add(temporder);


            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("Total: " + ttotalDisplay);
        retrieveOrder = tempOrderList;
        totalDisplay.setText("₱ " + ttotalDisplay);
        displayOrder();
        return tempOrderList;
    }



    public void displayOrder() {

        ObservableList<TempOrder> list = retrieveOrder;


        itemQuan.setCellValueFactory(new PropertyValueFactory<TempOrder, Integer>("quantity"));
        itemDesc.setCellValueFactory(new PropertyValueFactory<TempOrder, String>("productname"));
        itemPrice.setCellValueFactory(new PropertyValueFactory<TempOrder, Integer>("price"));

        if (!list.isEmpty()) {


            itemOrder.setItems(list);
            itemOrder.getItems().addAll(list);

            System.out.println("is empty: " +  itemOrder.getItems().addAll(list));

        }

    }
}
