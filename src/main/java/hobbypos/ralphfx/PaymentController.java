package hobbypos.ralphfx;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.image.*;
import com.github.anastaciocintra.output.PrinterOutputStream;
import hobbypos.ralphfx.modal.DataObj;
import hobbypos.ralphfx.model.PrinterData;
import hobbypos.ralphfx.model.TempOrder;
import hobbypos.ralphfx.model.Voucher;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PaymentController implements Initializable {
    @FXML
    private TableColumn<TempOrder, Integer> itemQuan;
    @FXML
    private TableColumn<TempOrder, String> itemDesc;
    @FXML
    private TableColumn<TempOrder, Integer> itemPrice;
    @FXML
    private TableView<TempOrder> itemOrder;
    @FXML
    private Label totalAmount;
    @FXML
    private TextArea paymentTxt;
    @FXML
    private Button PayBtn;
    @FXML
    private Button cancelBtn;

    @FXML
    private Button seven;
    @FXML
    private Button eight;
    @FXML
    private Button nine;
    @FXML
    private Button four;
    @FXML
    private Button five;
    @FXML
    private Button six;
    @FXML
    private Button one;
    @FXML
    private Button two;
    @FXML
    private Button three;
    @FXML
    private Button zero;
    @FXML
    private Button backs;
    @FXML
    private Label transID;
    @FXML
    private Label totalDuetxt;
    @FXML
    private ComboBox voucherCombo;

    DataObj jbdc = new DataObj();
    String transactionID;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateDiscount();

        addListenerForTable();
        getPrinter();
        updateNumbers();
    }


    public void setTranID(String tranID) {
        transID.setText(tranID);
        transactionID = tranID;
        displayOrder();
        updateNumbers();
    }


    public static String removeLastCharOptional(String s) {
        return Optional.ofNullable(s)
                .filter(str -> str.length() != 0)
                .map(str -> str.substring(0, str.length() - 1))
                .orElse(s);
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

    @FXML
    public void inputData(ActionEvent event) {

        String tempTxt;
        tempTxt = paymentTxt.getText();

        final Node source = (Node) event.getSource();

        if (provideInput(source).contentEquals("123")) {
            paymentTxt.setText(removeLastCharOptional(tempTxt));
            if( paymentTxt.getText().equals("")){
                paymentTxt.setText("0");
            }
        } else {
            if (tempTxt.equals("0")) {
                paymentTxt.setText(provideInput(source));
            } else {
                paymentTxt.setText(tempTxt + provideInput(source));
            }
        }

        updateNumbers();

    }

    ObservableList<TempOrder> retrieveOrder = FXCollections.observableArrayList();

    int ttotalDisplay;
    String waiterName;
    String tableName;

    public void displayOrder() {

        ObservableList<TempOrder> list = getProductList();


        itemQuan.setCellValueFactory(new PropertyValueFactory<TempOrder, Integer>("quantity"));
        itemDesc.setCellValueFactory(new PropertyValueFactory<TempOrder, String>("productname"));
        itemPrice.setCellValueFactory(new PropertyValueFactory<TempOrder, Integer>("price"));

        if (!list.isEmpty()) {
            itemOrder.setItems(list);
            totalDuetxt.setText(Integer.toString(ttotalDisplay));
        }

        System.err.println("Inside 1");
    }

    @FXML
    private Label discountTxt;
    @FXML
    private Label changeTxt;

    String discountData;

    public void updateNumbers(){

        DecimalFormat df = new DecimalFormat("#.##");
        int totalDue = ttotalDisplay;
        int cash = Integer.parseInt(paymentTxt.getText());
        double discount = getdiscount(voucherCombo.getSelectionModel().getSelectedItem().toString());
        double finalT = totalDue * discount;
        double total = totalDue - finalT;
        double change= cash - total;
        if(discount==1){
            change= cash - totalDue;
            discountTxt.setText("N/A");
            changeTxt.setText(Double.toString(Double.parseDouble(df.format(change))));
            totalAmount.setText(Double.toString(Double.parseDouble(df.format(ttotalDisplay))));
            discountData = "0";
        }else if(discount==100){
            discountTxt.setText("Charge Waved");
            changeTxt.setText("0");
            totalAmount.setText("0");
            discountData = "Owner Waived";

        }else{
            discountTxt.setText("P"+Double.toString(Double.parseDouble(df.format(discount)))+"%");
            changeTxt.setText(Double.toString(Double.parseDouble(df.format(change))));
            totalAmount.setText(Double.toString(Double.parseDouble(df.format(total))));
            discountData = df.format(finalT);
        }



    }
    private void addListenerForTable() {
        voucherCombo.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                updateNumbers();
            }
        });


    }
    private ObservableList<TempOrder> getProductList() {
        ObservableList<TempOrder> OrderList = FXCollections.observableArrayList();

        Connection conn = jbdc.getConnection();
        String query = "SELECT * FROM temporder WHERE transactionid='"+ transactionID +"'";
        Statement st;
        ResultSet rs;
        System.err.println("Inside 2: " + query);
        int Total = 0;
        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            System.err.println("Inside 2: " + rs.getFetchSize());
            TempOrder products;
            while (rs.next()) {
                products = new TempOrder(rs.getInt("orderid"), rs.getString("transactionid"), rs.getInt("productid"), rs.getString("productname"), rs.getInt("price"), rs.getInt("quantity"), rs.getString("tableName"), rs.getString("waiterName"), rs.getString("drink"));
                Total = products.getPrice() * products.getQuantity();
                waiterName = products.getWaitername();
                tableName = products.getTableName();
                OrderList.add(products);
                System.err.println("Inside 2");

                ttotalDisplay = ttotalDisplay + Total;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return OrderList;
    }


    @FXML
    private void payBill() throws PrintException, IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Pay Bill");
        alert.setHeaderText("Are you sure want to print Final Bill?");

        // option != null.
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == ButtonType.OK) {
            officialreceipt();
            alert.close();

        } else if (option.get() == ButtonType.CANCEL) {

        }

    }

    @FXML
    private void BackBtn(ActionEvent event) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Close Bill");
            alert.setHeaderText("Are you sure want to close?");

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


    String Cashier;
    String Kitchen;
    String Bar;
    String KTV;


    public void getPrinter() {

        Connection conn = jbdc.getConnection();
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

    String voucherN;
    double content;
    ObservableList<Voucher> voucherList = FXCollections.observableArrayList();

    private void populateDiscount() {
        Connection conn = jbdc.getConnection();
        Statement st;
        //System.out.println(query);
        ObservableList<String> list = FXCollections.observableArrayList();
        Voucher vlist;
        try {
            //st = conn.createStatement();
            //st.executeUpdate(query);

            ResultSet rs = conn.createStatement().executeQuery("select * from voucher");

            while (rs.next()) {
                vlist = new Voucher(rs.getInt("id"), rs.getString("vouchername"), rs.getDouble("content"));
                voucherList.add(vlist);

                list.add(rs.getString("vouchername"));
//              cbCategories.add(rs.getString("name"));
            }

        } catch (Exception ex) {
            System.out.println("error while inserting record.");
            ex.printStackTrace();
        }

        voucherCombo.setItems(null);
        voucherCombo.setItems(list);
        voucherCombo.getSelectionModel().selectFirst();
        voucherCombo.getSelectionModel().selectFirst();


    }

    public void officialreceipt() throws PrintException, IOException {

        PrintService printService = PrinterOutputStream.getPrintServiceByName(Cashier);
        // get the printer service by name passed on command line...
        //this call is slow, try to use it only once and reuse the PrintService variable.
        EscPos escpos;
        openCashDrawer();
        try {
            escpos = new EscPos(new PrinterOutputStream(printService));
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
                    .setBold(true)
                    .setJustification(EscPosConst.Justification.Center);
            Style waiterb = new Style(escpos.getStyle())
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setBold(true)
                    .setJustification(EscPosConst.Justification.Center);
            Style item = new Style(escpos.getStyle())
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setBold(true)
                    .setJustification(EscPosConst.Justification.Left_Default);
            Style totalss = new Style(escpos.getStyle())
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setBold(true)
                    .setJustification(EscPosConst.Justification.Right);

            URL _url = this.getClass().getResource("icon3.png");
            BufferedImage imageBufferedImage = ImageIO.read(_url);
            RasterBitImageWrapper imageWrapper = new RasterBitImageWrapper();


            Bitonal algorithm = new BitonalOrderedDither();
            EscPosImage escposImage = new EscPosImage(new CoffeeImageImpl(imageBufferedImage), algorithm);
            escpos.write(imageWrapper, escposImage);

            escpos.feed(2)
                    .writeLF(totalb, "HOBBY Restobar")
                    .writeLF(totalb, "& KTV")
                    .feed(1)
                    .writeLF(waiterb, "Polog Consolacion   ")
                    .writeLF(waiterb, "6001 Consolacion,Philippines")
                    .feed(2)
                    .writeLF(totalb, "Acknowledgement Receipt")
                    .writeLF("------------------------------------------------")
                    .writeLF(bold, "  Transaction no.   : " + transactionID)
                    .writeLF(bold, "  Waiter            : " + waiterName)
                    .writeLF(bold, "  Table             : " + tableName)
                    .writeLF("------------------------------------------------")
                    .feed(1);


            ObservableList<TempOrder> tempOrderList = FXCollections.observableArrayList();
            Connection conn = jbdc.getConnection();
            String query = "SELECT * FROM temporder WHERE transactionid='" + transactionID + "'";
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
                    .writeLF(bold, "     Total Due                     P" + totalDuetxt.getText())
                    .writeLF(bold, "     Discount                      " + discountData)
                    .feed(1)
                    .writeLF(bold, "     Total(w/ Discount)            P" + totalAmount.getText())
                    .writeLF(bold, "     CASH                          P" + paymentTxt.getText())
                    .writeLF(bold, "     Change                        P" + changeTxt.getText())
                    .writeLF("------------------------------------------------")
                    .feed(1)
                    .writeLF(waiterb, "Thank you and Please come again!")
                    .writeLF(waiterb, "Follow us on Facebook")
                    .writeLF(waiterb, "www.facebook.com/HOBBY2022")
                    .feed(1)
                    .writeLF(waiterb, "CUSTOMER COPY")
                    .feed(7)
                    .cut(EscPos.CutMode.FULL);

            escpos.close();

        } catch (IOException ex) {
            Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
public double getdiscount(String voucher){
    Connection conn = jbdc.getConnection();
    Statement st;
    double discount=0;
    //System.out.println(query);
    try {
        //st = conn.createStatement();
        //st.executeUpdate(query);

        ResultSet rs = conn.createStatement().executeQuery("select * from voucher");

        while (rs.next()) {
            if(rs.getString("vouchername").equals(voucher)){
                discount = rs.getDouble("content");
            }
        }

    } catch (Exception ex) {
        System.out.println("error while inserting record.");
        ex.printStackTrace();
    }

    return discount;
}
    public void openCashDrawer() {

        byte[] open = {27, 112, 0, 100, (byte) 250};
//      byte[] cutter = {29, 86,49};
        PrintService pservice =
                PrintServiceLookup.lookupDefaultPrintService();
        DocPrintJob job = pservice.createPrintJob();
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        Doc doc = new SimpleDoc(open, flavor, null);
        PrintRequestAttributeSet aset = new
                HashPrintRequestAttributeSet();
        try {
            job.print(doc, aset);
        } catch (PrintException ex) {
            System.out.println(ex.getMessage());
        }
    }


}

