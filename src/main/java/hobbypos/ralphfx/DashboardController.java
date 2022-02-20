/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package hobbypos.ralphfx;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.print.PrintException;
import javax.print.PrintService;
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

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class DashboardController implements Initializable {

    private static Stage pStage;
    Scene fxmlFile;
    Parent root;
    Stage window;
    DataObj jbdc;
    Order orderData;
    String TableName;
    String WaiterN;
    String transID;
    DataObj jdbc;
    ObservableList<TempOrder> retrieveOrder = FXCollections.observableArrayList();
    int ttotalDisplay;
    Boolean check;
    String Cashier;
    String Kitchen;
    String Bar;
    String KTV;
    @FXML
    private Label lblUsername;
    @FXML
    private Button btnManageTable;
    @FXML
    private Button newOrder;
    @FXML
    private Button addOrder;
    @FXML
    private Button checkoutBtn;
    @FXML
    private Button printBtn;
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
    private ScrollPane scrollpane1;
    @FXML
    private ScrollPane scrollpane2;
    @FXML
    private Label totalDisplay;
    @FXML
    private TableView<TempOrder> itemOrder;

    public static Stage getPrimaryStage() {
        return pStage;
    }

    private void setPrimaryStage(Stage pStage) {
        DashboardController.pStage = pStage;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            jdbc = new DataObj();

            retrieveTable();
            seeAvailableTable();
            getPrinter();

            checkoutBtn.setDisable(true);
        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setUsername(String username) {
        lblUsername.setText(username);
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
    public void orderProp(ActionEvent event) {
        newOrder.setDisable(true);
        addOrder.setDisable(true);
    }

    @FXML
    private void newOrder(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Order.fxml"));
            Parent root = fxmlLoader.load();
            OrderController controller = (OrderController) fxmlLoader.getController();
            System.err.println(" newOrder : " + TableName);
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
    private void addtoOrder(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Order.fxml"));
            Parent root = fxmlLoader.load();
            OrderController controller = (OrderController) fxmlLoader.getController();
            System.err.println(transID);
            controller.setTranID(transID);
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
    private void payBill(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("payment.fxml"));
            Parent root = fxmlLoader.load();
            PaymentController payment = (PaymentController) fxmlLoader.getController();
            System.err.println(transID);
            payment.setTranID(transID);
            Stage stage = new Stage();
            stage.setTitle("Hobby Bar POS | Pay Bill");
            stage.setScene(new Scene(root));
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();

        } catch (Exception ex) {
            System.out.println("" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void getBill() throws PrintException, IOException {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Bill Out");
        alert.setHeaderText("Are you sure want to print Bill?");

        // option != null.
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == ButtonType.OK) {
           // sendBill();
            checkoutBtn.setDisable(false);
            alert.close();

        } else if (option.get() == ButtonType.CANCEL) {

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
    private void actionPrint(ActionEvent event) {
        try {
            openModalWindow("Receipt.fxml", "print");
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
                try {
                    openModalWindow("Receipt.fxml", "Manage Products");
                } catch (Exception ex) {
                    System.out.println("" + ex.getMessage());
                    ex.printStackTrace();
                }
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
                    printBtn.setDisable(true);
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
                    printBtn.setDisable(false);
                    addOrder.setDisable(false);

                });

                btn.setOnAction(event -> {
                    getTableData(tablename);
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
                Label Total = new Label("Waiter : " + getTableData(tablename));
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

        ttotalDisplay = 0;
    }

    @FXML
    private void setupPrinter() {
        try {
            openModalWindow("printer.fxml", "Setup Printer");
        } catch (Exception ex) {
            System.out.println("" + ex.getMessage());
            ex.printStackTrace();
        }

    }

    private String getTableData(String tableName) {
        ObservableList<TempOrder> tempOrderList = FXCollections.observableArrayList();
        Connection conn = jdbc.getConnection();
        String query = "SELECT * FROM temporder o WHERE o.tableName = '" + tableName + "'";
        Statement st;
        ResultSet rs;
        String waiter = null;
        int Total = 0;
        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            TempOrder temporder;

            while (rs.next()) {
                temporder = new TempOrder(rs.getInt("orderid"), rs.getString("transactionid"), rs.getInt("productid"), rs.getString("productname"), rs.getInt("price"), rs.getInt("quantity"), rs.getString("tableName"), rs.getString("waiterName"), rs.getString("drink"));
                Total = temporder.getPrice() * temporder.getQuantity();

                tempOrderList.add(temporder);
                waiter = temporder.getWaitername();
                transID = temporder.getTransactionid();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        ttotalDisplay = ttotalDisplay + Total;
        return waiter;
    }

    private ObservableList<TempOrder> getOrderList(String tableN) {

        System.out.println("Pasok Ako sa getOrderList");
        ObservableList<TempOrder> tempOrderList = FXCollections.observableArrayList();
        Connection conn = jdbc.getConnection();
        String query = "SELECT * FROM temporder WHERE tableName='" + tableN + "'";
        Statement st;
        ResultSet rs;
        int Total = 0;
        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            TempOrder temporder;
            while (rs.next()) {
                temporder = new TempOrder(rs.getInt("orderid"), rs.getString("transactionid"), rs.getInt("productid"), rs.getString("productname"), rs.getInt("price"), rs.getInt("quantity"), rs.getString("tableName"), rs.getString("waiterName"), rs.getString("drink"));
                Total = Total + (temporder.getPrice() * temporder.getQuantity());
                tempOrderList.add(temporder);
                WaiterN = temporder.getWaitername();
                TableName = temporder.getTableName();
                transID = temporder.getTransactionid();


            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        retrieveOrder = tempOrderList;
        totalDisplay.setText("₱ " + Total);

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

        }

    }

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


                if (rs.getString("assign").equals("CASHIER")) {
                    Cashier = rs.getString("printername");
                }
                if (rs.getString("assign").equals("KITCHEN")) {
                    Kitchen = rs.getString("printername");
                }
                if (rs.getString("assign").equals("BAR")) {
                    Bar = rs.getString("printername");
                }
                if (rs.getString("assign").equals("KTV")) {
                    KTV = rs.getString("printername");
                }
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }


    }

    public void sendBill() throws PrintException, IOException {


        PrintService printService = PrinterOutputStream.getPrintServiceByName(Cashier);
        // get the printer service by name passed on command line...
        //this call is slow, try to use it only once and reuse the PrintService variable.
        EscPos escpos;

        try {
            escpos = new EscPos(new PrinterOutputStream(printService));
            Style title = new Style()
                    .setFontSize(Style.FontSize._3, Style.FontSize._3)
                    .setJustification(EscPosConst.Justification.Center);


            Style subtitle = new Style(escpos.getStyle())
                    .setBold(true)
                    .setUnderline(Style.Underline.OneDotThick);
            Style bold = new Style(escpos.getStyle())
                    .setBold(true)
                    .setFontSize(Style.FontSize._1, Style.FontSize._1);
            Style totalb = new Style(escpos.getStyle())
                    .setFontSize(Style.FontSize._2, Style.FontSize._2)
                    .setBold(true)
                    .setJustification(EscPosConst.Justification.Center);
            Style waiterb = new Style(escpos.getStyle())
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setBold(true)
                    .setJustification(EscPosConst.Justification.Center);
            Style item = new Style(escpos.getStyle())
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setBold(false)
                    .setJustification(EscPosConst.Justification.Left_Default);
            Style totalss = new Style(escpos.getStyle())
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setBold(true)
                    .setJustification(EscPosConst.Justification.Right);

            escpos.feed(2)
                    .writeLF(totalb, "HOBBY Restobar")
                    .writeLF(totalb, "& KTV")
                    .feed(1)
                    .writeLF(waiterb, "Polog Consolacion   ")
                    .writeLF(waiterb, "6001 Consolacion,Philippines")
                    .feed(2)
                    .writeLF(totalb, "Bill Receipt")
                    .writeLF("------------------------------------------------")
                    .writeLF(bold, "  Transaction no.   : " + transID)
                    .writeLF(bold, "  Waiter            : " + WaiterN)
                    .writeLF(bold, "  Table             : " + TableName)
                    .writeLF("------------------------------------------------")
                    .feed(1);


            ObservableList<TempOrder> tempOrderList = FXCollections.observableArrayList();
            Connection conn = jdbc.getConnection();
            String query = "SELECT * FROM temporder WHERE transactionid='" + transID + "'";
            Statement st;
            ResultSet rs;
            int Total = 0;
            try {
                st = conn.createStatement();
                rs = st.executeQuery(query);
                TempOrder temporder;

                while (rs.next()) {
                    temporder = new TempOrder(rs.getInt("orderid"), rs.getString("transactionid"), rs.getInt("productid"), rs.getString("productname"), rs.getInt("price"), rs.getInt("quantity"), rs.getString("tableName"), rs.getString("waiterName"), rs.getString("drink"));
                    int total = rs.getInt("quantity") * rs.getInt("price");

                    escpos.writeLF(item, rs.getInt("quantity") + " X " + rs.getInt("price") + ".0           " + rs.getString("productname")).write(totalss, " P" + total)
                            .writeLF(totalss, "");


                }

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            escpos.feed(1)
                    .writeLF("------------------------------------------------")
                    .writeLF(bold, "     Total                         P" + totalDisplay.getText())
                    .writeLF("------------------------------------------------")
                    .feed(1)
                    .writeLF(waiterb, "WAITER COPY")
                    .feed(7)
                    .cut(EscPos.CutMode.FULL);

            escpos.close();

        } catch (IOException ex) {
            Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
        }


    }


}
