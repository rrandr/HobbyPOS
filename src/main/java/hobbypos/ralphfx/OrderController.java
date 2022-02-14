/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package hobbypos.ralphfx;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.image.*;
import com.github.anastaciocintra.output.PrinterOutputStream;
import hobbypos.ralphfx.modal.DataObj;
import hobbypos.ralphfx.model.Order;
import hobbypos.ralphfx.model.PrinterData;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.print.PrintException;
import javax.print.PrintService;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.sql.Types.NULL;

/**
 * FXML Controller class
 *
 * @author Armero
 */
public class OrderController implements Initializable {

    private static Stage pStage;
    @FXML
    public Text waiterName;
    @FXML
    public Label totalDisplay;
    @FXML
    public Button addToOrderBtn;
    @FXML
    public ComboBox<String> mywaiterList;
    @FXML
    public ComboBox<String> myTableList;
    Scene fxmlFile;
    Parent root;
    Stage window;
    int pID;
    String pName;
    String drinks;
    int pQuantity;
    int pPrice;
    int ttotalDisplay;
    TempOrder tempOrder;
    Order newOrder;
    String transactionids;
    String tranOrderID = "";
    String oldT;
    DataObj jdbc;
    ObservableList<TempOrder> finalOrder = FXCollections.observableArrayList();
    private Label tableNum;
    @FXML
    private TilePane catTile;
    @FXML
    private TilePane productBox;
    @FXML
    private Text tableNumtxt;
    @FXML
    private Label lblFoodName;
    @FXML
    private Text transID;
    @FXML
    private ScrollPane scrollbox;
    @FXML
    private Button oMinusQuant;
    @FXML
    private Button oAddQuant;
    @FXML
    private Button saveOrdBtn;
    @FXML
    private TextField oQuantity;
    @FXML
    private TableView<TempOrder> tableOrder;
    @FXML
    private TableColumn<TempOrder, Integer> orderQnt;
    @FXML
    private TableColumn<TempOrder, Integer> orderTotal;
    @FXML
    private TableColumn<TempOrder, String> orderDesc;
    @FXML
    private TableColumn<TempOrder, Integer> orderPrice;
    @FXML
    private Button DeleteItemBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        jdbc = new DataObj();
        getTransactionID();
        addListenerForTable();
        populateWaiterList();
        populateTableList();
        getPrinter();
        try {
            getCategories();
        } catch (SQLException ex) {
            Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setTranID(String tranID) {
        transID.setText(tranID);
        tranOrderID = tranID;
        displayTempOrder();
        oldT = tableNumtxt.getText();
    }

    private void populateWaiterList() {
        Connection conn = jdbc.getConnection();
        Statement st;
        //System.out.println(query);
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            //st = conn.createStatement();
            //st.executeUpdate(query);

            ResultSet rs = conn.createStatement().executeQuery("select * from waiter where location='1'");
            while (rs.next()) {
                list.add(rs.getString("name"));
//              cbCategories.add(rs.getString("name"));
            }

        } catch (Exception ex) {
            System.out.println("error while inserting record.");
            ex.printStackTrace();
        }

        mywaiterList.setItems(null);
        mywaiterList.setItems(list);

    }

    private void populateTableList() {
        Connection conn = jdbc.getConnection();
        Statement st;
        //System.out.println(query);
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            //st = conn.createStatement();
            //st.executeUpdate(query);

            ResultSet rs = conn.createStatement().executeQuery("select * from tbltables where tableavail='0'");
            while (rs.next()) {
                list.add(rs.getString("name"));
//              cbCategories.add(rs.getString("name"));
            }

        } catch (Exception ex) {
            System.out.println("error while inserting record.");
            ex.printStackTrace();
        }

        myTableList.setItems(null);
        myTableList.setItems(list);

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

    private void getTransactionID() {

        if (transID.getText().contains("Text")) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime now = LocalDateTime.now();
            transID.setText(dtf.format(now));
            transactionids = (dtf.format(now));
        }
    }

    private void SaveOrderDB(ActionEvent event) {

        try {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Order");
            alert.setHeaderText("Confirm Order?");

            // option != null.
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get() == ButtonType.OK) {


                insertRecord();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
                Parent root = fxmlLoader.load();

                Stage stage = new Stage();
                stage.setTitle("Hobby Bar POS | Dashboard");
                stage.setScene(new Scene(root));
                stage.show();
                ((Node) (event.getSource())).getScene().getWindow().hide();

            } else if (option.get() == ButtonType.CANCEL) {

            }


        } catch (Exception ex) {
            System.out.println("" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void saveOrder(ActionEvent event) throws PrintException, IOException {

        Connection conn = jdbc.getConnection();
        Statement st;
        //System.out.println(query);
        ObservableList<String> list = FXCollections.observableArrayList();
        System.err.println("Awa ragud ni : " + tranOrderID);
        try {
            if (!tranOrderID.isEmpty()) {
                ResultSet rs = conn.createStatement().executeQuery("select * from temporder where transactionid='" + tranOrderID + "'");

                System.err.println("ari ko sulod :  " + tranOrderID);
                deleteEntry();
                SaveOrderDB(event);
            } else {
                System.err.println("diri ko sulod :  " + transactionids);
                SaveOrderDB(event);
            }
        } catch (Exception ex) {
            System.out.println("error while inserting record.");
            ex.printStackTrace();
        }
        sendKitchen();
        sendDrink();
       // officialreceipt();
    }


    private void deleteEntry() {
        Connection conn = jdbc.getConnection();
        try {
            String query = "DELETE FROM temporder WHERE transactionid = '" + tranOrderID + "'";
            executeQuery(query);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


    @FXML
    private void removeOrderItem(ActionEvent event) {
        int index = tableOrder.getSelectionModel().getSelectedIndex();
        TempOrder getTempData = tableOrder.getSelectionModel().getSelectedItem();
        if (index >= 0) {
            tableOrder.getItems().remove(index);

            int removed = getTempData.getPrice();
            ttotalDisplay = ttotalDisplay - removed;
            totalDisplay.setText("₱ " + Integer.toString(ttotalDisplay));

        }


    }

    @FXML
    private void addtoOrder(ActionEvent event) {
        saveOrdBtn.setDisable(false);
        displayOrder();
    }

    private ObservableList<TempOrder> getTempList() {

        int quantity = Integer.parseInt(oQuantity.getText());
        int Total = pPrice * quantity;
        TempOrder temporderD;
        ObservableList<TempOrder> displayOrderList = FXCollections.observableArrayList();
        String waiterName = mywaiterList.getSelectionModel().getSelectedItem().toString();
        temporderD = new TempOrder(NULL, transactionids, pID, pName, pPrice, quantity, tableNumtxt.getText(), waiterName, drinks);
        displayOrderList.add(temporderD);

        if (displayOrderList.isEmpty()) {

            saveOrdBtn.setDisable(true);

        }

        ttotalDisplay = ttotalDisplay + Total;

        return displayOrderList;
    }

    public void displayOrder() {

        ObservableList<TempOrder> list = getTempList();


        orderQnt.setCellValueFactory(new PropertyValueFactory<TempOrder, Integer>("quantity"));
        orderDesc.setCellValueFactory(new PropertyValueFactory<TempOrder, String>("productname"));
        orderPrice.setCellValueFactory(new PropertyValueFactory<TempOrder, Integer>("price"));
        orderTotal.setCellValueFactory(new PropertyValueFactory<TempOrder, Integer>("price"));

        if (list.isEmpty()) {
            tableOrder.setItems(list);
            totalDisplay.setText("₱ " + Integer.toString(ttotalDisplay));

        } else {
            saveOrdBtn.setDisable(false);
            tableOrder.getItems().addAll(list);
            finalOrder.addAll(list);
            totalDisplay.setText("₱ " + Integer.toString(ttotalDisplay));

        }

    }

    private void addListenerForTable() {
        tableOrder.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                DeleteItemBtn.setDisable(false);

            } else {
                DeleteItemBtn.setDisable(true);

            }

        });


    }


    @FXML
    private void manageWaiter(ActionEvent event) {
        try {
            openModalWindow("waiter.fxml", "Waiter Management");
        } catch (Exception ex) {
            System.out.println("" + ex.getMessage());
            ex.printStackTrace();
        }

    }


    public void displayTempOrder() {

        ObservableList<TempOrder> list = getTempOrderList();

        orderQnt.setCellValueFactory(new PropertyValueFactory<TempOrder, Integer>("quantity"));
        orderDesc.setCellValueFactory(new PropertyValueFactory<TempOrder, String>("productname"));
        orderPrice.setCellValueFactory(new PropertyValueFactory<TempOrder, Integer>("price"));
        orderTotal.setCellValueFactory(new PropertyValueFactory<TempOrder, Integer>("price"));

        if (list.isEmpty()) {
            tableOrder.setItems(list);
            totalDisplay.setText("₱ " + Integer.toString(ttotalDisplay));

        } else {
            saveOrdBtn.setDisable(false);
            tableOrder.getItems().addAll(list);
            finalOrder.addAll(list);
            totalDisplay.setText("₱ " + Integer.toString(ttotalDisplay));

        }

    }

    private ObservableList<TempOrder> getTempOrderList() {
        ObservableList<TempOrder> tempOrderList = FXCollections.observableArrayList();
        Connection conn = jdbc.getConnection();
        String query = "SELECT * FROM temporder WHERE transactionid='" + tranOrderID + "'";
        Statement st;
        ResultSet rs;
        int Total = 0;
        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            TempOrder temporder;

            while (rs.next()) {
                temporder = new TempOrder(rs.getInt("orderid"), rs.getString("transactionid"), rs.getInt("productid"), rs.getString("productname"), rs.getInt("price"), rs.getInt("quantity"), rs.getString("tableName"), rs.getString("waiterName"), rs.getString("drink"));
                tempOrderList.add(temporder);
                Total = Total + (temporder.getPrice() * temporder.getQuantity());
                mywaiterList.getSelectionModel().select(temporder.getWaitername());
                myTableList.getSelectionModel().select(temporder.getTableName());
                tableNumtxt.setText(temporder.getTableName());
            }
            ttotalDisplay = Total;

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }


        return tempOrderList;
    }


    private void insertRecord() {

        if (!finalOrder.isEmpty()) {
            try {
                Connection conn = jdbc.getConnection();
                conn.setAutoCommit(false);

                String waiterName = mywaiterList.getSelectionModel().getSelectedItem().toString();
                String tableName = myTableList.getSelectionModel().getSelectedItem().toString();

                System.err.println(" old table : " + oldT + " New table: " + tableName);

                if (!oldT.equals(tableName)) {
                    updateTable(oldT, tableName);
                }
                PreparedStatement prepStmt = conn.prepareStatement("insert into temporder(orderid,transactionid,productid,productname,price,quantity,tableName,waiterName,drink) values('" + NULL + "',?,?,?,?,?,?,?,?)");
                Iterator<TempOrder> tp = finalOrder.iterator();

                while (tp.hasNext()) {

                    TempOrder tempO = tp.next();
                    if (tranOrderID.isEmpty()) {

                        prepStmt.setString(1, transactionids);
                    } else {
                        prepStmt.setString(1, tranOrderID);
                    }
                    prepStmt.setInt(2, tempO.getProductid());
                    prepStmt.setString(3, tempO.getProductname());
                    prepStmt.setInt(4, tempO.getPrice());
                    prepStmt.setInt(5, tempO.getQuantity());
                    prepStmt.setString(6, tableName);
                    prepStmt.setString(7, waiterName);
                    prepStmt.setString(8, tempO.getDrink());
                    prepStmt.addBatch();

                }
                int[] numUpdates = prepStmt.executeBatch();
                for (int i = 0; i < numUpdates.length; i++) {
                    if (numUpdates[i] == -2)
                        System.out.println("Execution " + i +
                                ": unknown number of rows updated");
                    else
                        System.out.println("Execution " + i +
                                "successful: " + numUpdates[i] + " rows updated");
                }
                conn.commit();

                updateTableAvail(tableName);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void updateTableAvail(String tablen) {


        Connection conn = jdbc.getConnection();
        try {

            String query = "UPDATE tbltables SET tableavail = '1' WHERE name = '" + tablen + "'";
            executeQuery(query);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void executeQuery(String query) {
        Connection conn = jdbc.getConnection();
        Statement st;
        System.out.println(query);
        try {
            st = conn.createStatement();
            st.executeUpdate(query);
        } catch (Exception ex) {
            System.out.println("error while inserting record.");
            ex.printStackTrace();
        }
    }


    @FXML
    public void quantityHandler(ActionEvent event) {

        final Node Source = (Node) event.getSource();

        int quantity = Integer.parseInt(oQuantity.getText());
        if (Source.getId().equals("oAddQuant")) {
            if (quantity > 0) {
                ++quantity;
                oQuantity.setText(Integer.toString(quantity));

            }
        }
        if (Source.getId().equals("oMinusQuant")) {

            if (quantity > 1) {
                --quantity;
                oQuantity.setText(Integer.toString(quantity));

            }
        }

    }

    @FXML
    private void handleKeyPressed(KeyEvent event) throws IOException {
        System.out.println("Key pressed: " + event.getCode());
        switch (event.getCode()) {
            case F1:
                System.out.println("Waiter");
                try {
                    openModalWindow("waiter.fxml", "Waiter Management");
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

    public void updateTable(String oldTs, String newTs) {

        Connection conn = jdbc.getConnection();
        try {

            String query = "UPDATE tbltables SET tableavail = '1' WHERE name = '" + newTs + "'";
            executeQuery(query);

            String querys = "UPDATE tbltables SET tableavail = '0' WHERE name = '" + oldTs + "'";
            executeQuery(querys);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout");
            alert.setHeaderText("Are you sure want to cancel the Order?");

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

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    private void getProduct(String category) throws SQLException, IOException {
        DataObj jdbcDao = new DataObj();
        Connection conn = jdbcDao.getConnection();
        Statement statement = conn.createStatement();
        String sqlQuery = "SELECT * FROM products WHERE category='" + category + "';";
        System.err.printf(sqlQuery);
        ResultSet resultSet = statement.executeQuery(sqlQuery);

        List<StackPane> buttonlist = new ArrayList<>(); //our Collection to hold newly created Buttons


        try {

            while (resultSet.next()) { //iterate over every row returned

                StackPane stackPane = new StackPane();
                String productname = resultSet.getString("description");
                String productid = resultSet.getString("pluID");
                String drink = resultSet.getString("weight");
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

                    pID = Integer.parseInt(productid);
                    pName = productname;
                    drinks = drink;
                    pPrice = Integer.parseInt(price);
                    oQuantity.setText("1");
                    System.out.println(pName + " - " + pID);
                    lblFoodName.setText(productname);
                    addToOrderBtn.setDisable(false);
                });
                stackPane.getChildren().add(btn);

                Label background = new Label("");
                background.setTranslateX(20);
                background.setTranslateY(85);
                background.setPrefSize(130, 20);
                background.setStyle("-fx-background-color: #989898; -fx-opacity: 80%;");
                stackPane.getChildren().add(background);

                Label Total = new Label("Price : ₱" + price);
                Total.setTranslateX(10);
                Total.setTranslateY(85);
                Total.setStyle("-fx-font-color:white; -fx-font-weight:bold;");
                stackPane.getChildren().add(Total);

                Label pbackground = new Label("");
                pbackground.setTranslateX(20);
                pbackground.setTranslateY(-25);
                pbackground.setPrefSize(130, 20);
                pbackground.setStyle("-fx-background-color: #0277A6; -fx-opacity: 80%;");
                stackPane.getChildren().add(pbackground);

                Label productName = new Label(productname);
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
            productBox.getChildren().addAll(buttonlist);
            scrollbox.setContent(productBox);//then add all your Buttons that you just created
            System.out.println("Pasok Ako: " + resultSet);


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            resultSet.close();
            statement.close();
            conn.close();
        }
    }

    private void getCategories() throws SQLException {

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
                    System.out.println(category + " - " + categoryid);
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

    String Bar;
    String Kitchen;
    String Drinks;
    String KTV;


    public void getPrinter() {

        Connection conn = jdbc.getConnection();
        String query = "SELECT * FROM printer";
        Statement st;
        ResultSet rs;
        System.err.println("kasulod ko");
        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            PrinterData printer;

            while (rs.next()) {
                printer = new PrinterData(rs.getInt("printerID"), rs.getString("printername"), rs.getString("assign"));


                if (rs.getString("assign").equals("BAR")) {
                    Bar = rs.getString("printername");
                }
                if (rs.getString("assign").equals("KITCHEN")) {
                    Kitchen = rs.getString("printername");
                }
                if (rs.getString("assign").equals("DRINK")) {
                    Drinks = rs.getString("printername");
                }
                if (rs.getString("assign").equals("KTV")) {
                    KTV = rs.getString("printername");
                }
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }


    }

    public void sendCashier() throws PrintException, IOException {


        PrintService printService = PrinterOutputStream.getPrintServiceByName(Bar);
        // get the printer service by name passed on command line...
        //this call is slow, try to use it only once and reuse the PrintService variable.
        EscPos escpos;
        try {
            escpos = new EscPos(new PrinterOutputStream(printService));
            String waiterName = mywaiterList.getSelectionModel().getSelectedItem().toString();
            String tableName = myTableList.getSelectionModel().getSelectedItem().toString();
            Style title = new Style()
                    .setFontSize(Style.FontSize._3, Style.FontSize._3)
                    .setJustification(EscPosConst.Justification.Center);

            Style subtitle = new Style(escpos.getStyle())
                    .setBold(true)
                    .setUnderline(Style.Underline.OneDotThick);
            Style bold = new Style(escpos.getStyle())
                    .setBold(true);
            Style totalb = new Style(escpos.getStyle())
                    .setFontSize(Style.FontSize._2, Style.FontSize._2)
                    .setBold(true);
            Style waiterb = new Style(escpos.getStyle())
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setBold(true);

            escpos.writeLF(title,"BAR")
                    .writeLF(title,tableNumtxt.getText())
                    .feed(1)
                    .writeLF(waiterb,"       Waiter: " + waiterName)
                    .feed(2)
                    .writeLF(waiterb,"          Cashier Copy        ")
                    .feed(2)
                    .writeLF("--------------------------------")
                    .writeLF(bold,"Transcation no. : " + tranOrderID)
                    .writeLF("--------------------------------")
                    .feed(2);


            ObservableList<TempOrder> tempOrderList = FXCollections.observableArrayList();
            Connection conn = jdbc.getConnection();
            String query = "SELECT * FROM temporder WHERE transactionid='" + tranOrderID + "'";
            Statement st;
            ResultSet rs;
            int Total = 0;
            try {
                st = conn.createStatement();
                rs = st.executeQuery(query);
                TempOrder temporder;

                while (rs.next()) {
                    temporder = new TempOrder(rs.getInt("orderid"), rs.getString("transactionid"), rs.getInt("productid"), rs.getString("productname"), rs.getInt("price"), rs.getInt("quantity"), rs.getString("tableName"), rs.getString("waiterName"),rs.getString("drink"));
                    int total= rs.getInt("quantity") * rs.getInt("price");

                    escpos.write(rs.getInt("quantity")+" - "+rs.getString("productname") +" " + rs.getInt("price") + "@each" + "    - P" + total)
                            .feed(1);


                }

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            escpos.feed(1)
                    .writeLF("--------------------------------")
                    .writeLF(totalb, "Total :  P" + ttotalDisplay)
                    .writeLF("--------------------------------")
                    .feed(5)
                    .cut(EscPos.CutMode.FULL);
            escpos.close();

        } catch (IOException ex) {
            Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public void sendKitchen() throws PrintException, IOException {

        PrintService printService = PrinterOutputStream.getPrintServiceByName(Kitchen);
        // get the printer service by name passed on command line...
        //this call is slow, try to use it only once and reuse the PrintService variable.
        EscPos escpos;
        try {
            escpos = new EscPos(new PrinterOutputStream(printService));
            String waiterName = mywaiterList.getSelectionModel().getSelectedItem().toString();
            String tableName = myTableList.getSelectionModel().getSelectedItem().toString();
            Style title = new Style()
                    .setFontSize(Style.FontSize._3, Style.FontSize._3)
                    .setJustification(EscPosConst.Justification.Center);

            Style subtitle = new Style(escpos.getStyle())
                    .setBold(true)
                    .setUnderline(Style.Underline.OneDotThick);
            Style bold = new Style(escpos.getStyle())
                    .setBold(true);
            Style totalb = new Style(escpos.getStyle())
                    .setFontSize(Style.FontSize._2, Style.FontSize._2)
                    .setBold(true);
            Style waiterb = new Style(escpos.getStyle())
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setBold(true);

            escpos.writeLF(title,"BAR")
                    .writeLF(title,tableNumtxt.getText())
                    .feed(1)
                    .writeLF(waiterb,"       Waiter: " + waiterName)
                    .feed(2)
                    .writeLF(waiterb,"          Kitchen Copy        ")
                    .feed(2)
                    .writeLF("--------------------------------")
                    .writeLF(bold,"Transaction no. : " + tranOrderID)
                    .writeLF("--------------------------------")
                    .feed(2);


            ObservableList<TempOrder> tempOrderList = FXCollections.observableArrayList();
            Connection conn = jdbc.getConnection();
            String query = "SELECT * FROM temporder WHERE transactionid='" + tranOrderID + "'";
            Statement st;
            ResultSet rs;
            int Total = 0;
            try {
                st = conn.createStatement();
                rs = st.executeQuery(query);
                TempOrder temporder;

                while (rs.next()) {
                    temporder = new TempOrder(rs.getInt("orderid"), rs.getString("transactionid"), rs.getInt("productid"), rs.getString("productname"), rs.getInt("price"), rs.getInt("quantity"), rs.getString("tableName"), rs.getString("waiterName"),rs.getString("drink"));
                    int total= rs.getInt("quantity") * rs.getInt("price");

                    escpos.write(rs.getInt("quantity")+" - "+rs.getString("productname") +" " + rs.getInt("price") + "@each" + "    - P" + total)
                            .feed(1);


                }

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            escpos.feed(1)
                    .writeLF("--------------------------------")
                    .writeLF(totalb, "Total :  P" + ttotalDisplay)
                    .writeLF("--------------------------------")
                    .feed(5)
                    .cut(EscPos.CutMode.FULL);
            escpos.close();

        } catch (IOException ex) {
            Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void sendDrink() throws PrintException, IOException {
        PrintService printService = PrinterOutputStream.getPrintServiceByName(Drinks);
        // get the printer service by name passed on command line...
        //this call is slow, try to use it only once and reuse the PrintService variable.
        EscPos escpos;
        try {
            escpos = new EscPos(new PrinterOutputStream(printService));
            String waiterName = mywaiterList.getSelectionModel().getSelectedItem().toString();
            String tableName = myTableList.getSelectionModel().getSelectedItem().toString();
            Style title = new Style()
                    .setFontSize(Style.FontSize._3, Style.FontSize._3)
                    .setJustification(EscPosConst.Justification.Center);

            Style subtitle = new Style(escpos.getStyle())
                    .setBold(true)
                    .setUnderline(Style.Underline.OneDotThick);
            Style bold = new Style(escpos.getStyle())
                    .setBold(true);
            Style totalb = new Style(escpos.getStyle())
                    .setFontSize(Style.FontSize._2, Style.FontSize._2)
                    .setBold(true);
            Style waiterb = new Style(escpos.getStyle())
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setBold(true);

            escpos.writeLF(title,"BAR")
                    .writeLF(title,tableNumtxt.getText())
                    .feed(1)
                    .writeLF(waiterb,"     Waiter: " + waiterName)
                    .feed(2)
                    .writeLF(waiterb,"          Drink Copy        ")
                    .feed(2)
                    .writeLF("--------------------------------")
                    .writeLF(bold,"Transaction no. : " + tranOrderID)
                    .writeLF("--------------------------------")
                    .feed(2);


            ObservableList<TempOrder> tempOrderList = FXCollections.observableArrayList();
            Connection conn = jdbc.getConnection();
            String query = "SELECT * FROM temporder WHERE transactionid='" + tranOrderID + "'";
            Statement st;
            ResultSet rs;
            int Total = 0;
            try {
                st = conn.createStatement();
                rs = st.executeQuery(query);
                TempOrder temporder;

                while (rs.next()) {
                    temporder = new TempOrder(rs.getInt("orderid"), rs.getString("transactionid"), rs.getInt("productid"), rs.getString("productname"), rs.getInt("price"), rs.getInt("quantity"), rs.getString("tableName"), rs.getString("waiterName"),rs.getString("drink"));
                    int total= rs.getInt("quantity") * rs.getInt("price");
                    if(rs.getString("drink").equals("YES")) {
                        escpos.write(rs.getInt("quantity") + " - " + rs.getString("productname") + " " + rs.getInt("price") + "@each" + "    - P" + total)
                                .feed(1);
                    }


                }

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            escpos.feed(1)
                    .writeLF("--------------------------------")
                    .feed(5)
                    .cut(EscPos.CutMode.FULL);
            escpos.close();

        } catch (IOException ex) {
            Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void officialreceipt() throws PrintException, IOException {

        PrintService printService = PrinterOutputStream.getPrintServiceByName(Kitchen);
        // get the printer service by name passed on command line...
        //this call is slow, try to use it only once and reuse the PrintService variable.
        EscPos escpos;
        try {
            escpos = new EscPos(new PrinterOutputStream(printService));
            String waiterName = mywaiterList.getSelectionModel().getSelectedItem().toString();
            String tableName = myTableList.getSelectionModel().getSelectedItem().toString();


            Style title = new Style()
                    .setFontSize(Style.FontSize._3, Style.FontSize._3)
                    .setJustification(EscPosConst.Justification.Center);

            Style subtitle = new Style(escpos.getStyle())
                    .setBold(true)
                    .setUnderline(Style.Underline.OneDotThick);
            Style bold = new Style(escpos.getStyle())
                    .setBold(true);
            Style totalb = new Style(escpos.getStyle())
                    .setFontSize(Style.FontSize._2, Style.FontSize._2)
                    .setBold(true);
            Style waiterb = new Style(escpos.getStyle())
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setBold(true);

            URL _url = this.getClass().getResource("icon2.png");
            BufferedImage imageBufferedImage = ImageIO.read(_url);
            RasterBitImageWrapper imageWrapper = new RasterBitImageWrapper();
            Bitonal algorithm = new BitonalOrderedDither();
            EscPosImage escposImage = new EscPosImage(new CoffeeImageImpl(imageBufferedImage), algorithm);
            escpos.write(imageWrapper,escposImage);

            escpos.feed(3)
                    .writeLF(totalb,"  HOBBY Restobar")
                    .writeLF(totalb,"       & KTV")
                    .writeLF(waiterb,"      Polog Consolacion   ")
                    .writeLF(waiterb, " 6001 Consolacion,Philippines")
                    .feed(1)
                    .writeLF(waiterb,"        Waiter: " + waiterName)
                    .feed(2)
                    .writeLF(totalb,"   Customer Copy")
                    .feed(1)
                    .writeLF("--------------------------------")
                    .writeLF(bold,"Transaction no. : " + tranOrderID)
                    .writeLF("--------------------------------")
                    .feed(1);


            ObservableList<TempOrder> tempOrderList = FXCollections.observableArrayList();
            Connection conn = jdbc.getConnection();
            String query = "SELECT * FROM temporder WHERE transactionid='" + tranOrderID + "'";
            Statement st;
            ResultSet rs;
            int Total = 0;
            try {
                st = conn.createStatement();
                rs = st.executeQuery(query);
                TempOrder temporder;

                while (rs.next()) {
                    temporder = new TempOrder(rs.getInt("orderid"), rs.getString("transactionid"), rs.getInt("productid"), rs.getString("productname"), rs.getInt("price"), rs.getInt("quantity"), rs.getString("tableName"), rs.getString("waiterName"),rs.getString("drink"));
                    int total= rs.getInt("quantity") * rs.getInt("price");

                    escpos.write(rs.getInt("quantity")+" - "+rs.getString("productname") +" " + rs.getInt("price") + "@each" + "    - P" + total)
                            .feed(1);


                }

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            escpos.feed(1)
                    .writeLF("--------------------------------")
                    .writeLF(totalb, "Total :  P" + ttotalDisplay)
                    .writeLF("--------------------------------")
                    .writeLF("-----Follow us on Facebook------")
                    .writeLF("----www.facebook.com/HOBBY2022--")
                    .feed(5)
                    .cut(EscPos.CutMode.FULL);
            escpos.close();
        } catch (IOException ex) {
            Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


}


