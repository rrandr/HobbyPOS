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
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.TilePane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.print.PrintException;
import javax.print.PrintService;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class RetailController implements Initializable {

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
    @FXML
    private Button manageProduct;

    @FXML
    private TextField ItemField;

    public static Stage getPrimaryStage() {
        return pStage;
    }

    private void setPrimaryStage(Stage pStage) {
        RetailController.pStage = pStage;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                ItemField.requestFocus();
            }
        });


    }


    @FXML
    public void refresh() {

            getPrinter();

            checkoutBtn.setDisable(true);

    }

    public void setUsername(String username) {
        lblUsername.setText(username);
    }

    @FXML
    private void getPrice(ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("price.fxml"));
            Parent root = loader.load();
            // I guess you forgot this line????
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(btnManageTable.getScene().getWindow());
            stage.getIcons().add(new Image(String.valueOf(this.getClass().getResource("icon2.png"))));
            stage.showAndWait();

        } catch (Exception ex) {
            System.out.println("" + ex.getMessage());
            ex.printStackTrace();
        }
    }
    public String provideInput(Node Source) {

        if (Source.getId().equals("one")) {
            return "1";
        } else if (Source.getId().equals("two")) {
            return "2";
        } else if (Source.getId().equals("three")) {
            return "3";
        }
        if (Source.getId().equals("four")) {
            return "4";
        } else if (Source.getId().equals("five")) {
            return "5";
        } else if (Source.getId().equals("six")) {
            return "6";
        } else if (Source.getId().equals("seven")) {
            return "7";
        } else if (Source.getId().equals("eight")) {
            return "8";
        } else if (Source.getId().equals("nine")) {
            return "9";
        } else if (Source.getId().equals("zero")) {
            return "0";
        } else {
            return "123";
        }


    }

    public void inputData(ActionEvent event) {




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
            stage.getIcons().add(new Image(String.valueOf(this.getClass().getResource("icon2.png"))));
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
            stage.getIcons().add(new Image(String.valueOf(this.getClass().getResource("icon2.png"))));
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
            stage.getIcons().add(new Image(String.valueOf(this.getClass().getResource("icon2.png"))));
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
            sendBill();
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
                stage.getIcons().add(new Image(String.valueOf(this.getClass().getResource("icon2.png"))));
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
        window.getIcons().add(new Image(String.valueOf(this.getClass().getResource("icon2.png"))));
//        window.initStyle(StageStyle.UNDECORATED);
        window.setTitle(title);
        setPrimaryStage(window);
        window.showAndWait();
    }

    @FXML
    private void actionManageProduct(ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("verify.fxml"));
            Parent root = loader.load();
            // I guess you forgot this line????
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(manageProduct.getScene().getWindow());
            stage.getIcons().add(new Image(String.valueOf(this.getClass().getResource("icon2.png"))));
            stage.showAndWait();

            VerifyController dashboard = loader.getController();
            Boolean selectedData = dashboard.getSelectedData();

            if (selectedData.equals(true)) {
                openModalWindow("Products.fxml", "Manage Products");
            } else {
                Alert alertz = new Alert(Alert.AlertType.ERROR);
                alertz.setTitle("Error");
                alertz.setHeaderText("Only Authorized Person Only");
            }
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
                System.out.println("Cancel order.");
                break;
            case F3:
                System.out.println("Manage products.");
                try {
                    openModalWindow("Products.fxml", "Manage Products");
                } catch (Exception ex) {
                    System.out.println("" + ex.getMessage());
                    ex.printStackTrace();
                }
                break;
            case F4:
                System.out.println("Manage Table");
                try {
                    openModalWindow("Tables.fxml", "Manage Tables");
                } catch (Exception ex) {
                    System.out.println("" + ex.getMessage());
                    ex.printStackTrace();
                }
                break;
            case F5:
                System.out.println("Sales Report");
                break;
            case F6:
                try {
                    openModalWindow("Lookup.fxml", "Product Lookup");
                } catch (Exception ex) {
                    System.out.println("" + ex.getMessage());
                    ex.printStackTrace();
                }
                break;
            case F7:
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
                        stage.getIcons().add(new Image(String.valueOf(this.getClass().getResource("icon2.png"))));
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

    @FXML
    private void setupPrinter() {
        try {
            openModalWindow("printer.fxml", "Setup Printer");
        } catch (Exception ex) {
            System.out.println("" + ex.getMessage());
            ex.printStackTrace();
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
                    .writeLF(bold, "     Total Due                     " + totalDisplay.getText())
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


    public void voidBill() throws PrintException, IOException {


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
                    .writeLF(totalb, "VOID ORDER RECEIPT")
                    .writeLF("------------------------------------------------")
                    .writeLF(bold, "  Transaction no.   : " + transID)
                    .writeLF(bold, "  Waiter            : " + WaiterN)
                    .writeLF(bold, "  Table             : " + TableName)
                    .writeLF(bold, "  VOID BY           : " + WaiterN)
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
                    .writeLF(bold, "     Total Due                     " + totalDisplay.getText())
                    .writeLF("------------------------------------------------")
                    .feed(1)
                    .writeLF(waiterb, "CASHIER COPY")
                    .feed(7)
                    .cut(EscPos.CutMode.FULL);

            escpos.close();

        } catch (IOException ex) {
            Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    @FXML
    private Button cancelBtn;


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


    public void initClock(){

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {

            DateTimeFormatter date = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            DateTimeFormatter times = DateTimeFormatter.ofPattern("hh:mm:ss a");
            LocalDateTime now = LocalDateTime.now();
            String dater = date.format(now);
            String timer = times.format(now);


        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }


}
