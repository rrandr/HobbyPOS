package hobbypos.ralphfx;

import hobbypos.ralphfx.modal.DataObj;
import hobbypos.ralphfx.model.CurrentLogin;
import hobbypos.ralphfx.model.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class SalesController implements Initializable {

    DataObj jdbc;
    @FXML
    private TableView<Order> tableProducts;
    @FXML
    private TableColumn<Order, String> colTrans;
    @FXML
    private TableColumn<Order, String> colWaiter;
    @FXML
    private TableColumn<Order, String> colTable;
    @FXML
    private TableColumn<Order, Integer> colTotal;
    @FXML
    private TableColumn<Order, String> colDate;
    @FXML
    private TableColumn<Order, String> colTime;
    @FXML
    private Label salesTxt;
    @FXML
    private TextField searchBox;
    @FXML
    private TextField onethou;
    @FXML
    private TextField fivehund;
    @FXML
    private TextField twohund;
    @FXML
    private TextField onehund;
    @FXML
    private TextField fifty;
    @FXML
    private TextField twenty;
    @FXML
    private TextField ten;
    @FXML
    private TextField one;
    @FXML
    private TextField totalTxt;
    @FXML
    private Button recordBtn;
    @FXML
    private Label cashierTxt;

    String name;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        jdbc = new DataObj();
        setCashierInfo();
        showTransactions();
        search();

    }


    public void showTransactions() {
        ObservableList<Order> list = getOrderList();


        colTrans.setCellValueFactory(new PropertyValueFactory<Order, String>("transactionid"));
        colTable.setCellValueFactory(new PropertyValueFactory<Order, String>("tableName"));
        colWaiter.setCellValueFactory(new PropertyValueFactory<Order, String>("waitername"));
        colDate.setCellValueFactory(new PropertyValueFactory<Order, String>("OrderDate"));
        colTime.setCellValueFactory(new PropertyValueFactory<Order, String>("OrderTime"));
        colTotal.setCellValueFactory(new PropertyValueFactory<Order, Integer>("total"));

        tableProducts.setItems(list);

        int Total = 0;
        for (Order o : tableProducts.getItems()) {
            System.err.println(colTotal.getCellData(o));
            Total = Total + colTotal.getCellData(o);
            salesTxt.setText(Integer.toString(Total));
        }
    }

    ObservableList<Order> searchOrderList = FXCollections.observableArrayList();

    private ObservableList<Order> getOrderList() {
        ObservableList<Order> OrdertList = FXCollections.observableArrayList();
        Connection conn = jdbc.getConnection();
        String query = "SELECT transactionid,tableName,waiterName,sum(total),OrderDate,OrderTime FROM orders GROUP BY transactionid ";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Order orders;
            while (rs.next()) {
                orders = new Order(rs.getString("transactionid"), rs.getString("tableName"), rs.getString("waitername"), rs.getInt("sum(total)"), rs.getString("OrderDate"), rs.getString("OrderTime"));
                OrdertList.add(orders);

                System.out.println(rs);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        searchOrderList = OrdertList;


        return OrdertList;
    }

    @FXML
    private void calculates(javafx.event.ActionEvent event) {

        int onet = Integer.parseInt(onethou.getText()) * 1000;
        int fiveh = Integer.parseInt(fivehund.getText()) * 500;
        int twoh = Integer.parseInt(twohund.getText()) * 200;
        int oneh = Integer.parseInt(onehund.getText()) * 100;
        int five = Integer.parseInt(fifty.getText()) * 50;
        int twen = Integer.parseInt(twenty.getText()) * 20;
        int tens = Integer.parseInt(ten.getText()) * 10;
        int ones = Integer.parseInt(one.getText()) * 1;


        totalTxt.setText(Integer.toString(onet + fiveh + twoh + oneh + five + twen + tens + ones));
        recordBtn.setDisable(false);
    }

    @FXML
    private void onBack(javafx.event.ActionEvent event) throws IOException {

        FXMLLoader fxmlLoaders = new FXMLLoader(getClass().getResource("dashboard.fxml"));
        Parent roots = fxmlLoaders.load();
        Stage stages= new Stage();
        stages.setTitle("Hobby Bar POS | Bar Dashboard");
        stages.setScene(new Scene(roots));
        stages.getIcons().add(new Image(String.valueOf(this.getClass().getResource("icon2.png"))));
        stages.show();
        ((Node) (event.getSource())).getScene().getWindow().hide();

    }


    @FXML
    private void insertRecord(javafx.event.ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Record Sales");
        alert.setHeaderText("Are you sure want to record this today's sales?");

        // option != null.
        Optional<ButtonType> option = alert.showAndWait();

        Connection conn = jdbc.getConnection();
        if (option.get() == ButtonType.OK) {


            DateTimeFormatter date = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            DateTimeFormatter times = DateTimeFormatter.ofPattern("hh:mm:ss a");
            LocalDateTime now = LocalDateTime.now();
            String dater = date.format(now);
            String timer = times.format(now);

            String query = "INSERT INTO `sales` (cashonrecord,totalsales,cashier,recordDate,recordTime) VALUES('" + totalTxt.getText() + "','" + salesTxt.getText() + "','" + name + "','" + dater + "','" + timer + "')";
            System.out.println(query);
            executeQuery(query);


        } else if (option.get() == ButtonType.CANCEL) {

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

    private ObservableList<CurrentLogin> setCashierInfo() {
        DataObj jdbcDao = new DataObj();
        ObservableList<CurrentLogin> UserList = FXCollections.observableArrayList();
        Connection conn = jdbcDao.getConnection();
        String query = "SELECT * FROM current_login where id= 1";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            CurrentLogin user;
            while (rs.next()) {
                user = new CurrentLogin(rs.getInt("id"), rs.getString("cashier"));
                name = rs.getString("cashier");
                cashierTxt.setText(name);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return UserList;
    }

    public void search() {

        FilteredList<Order> filteredData = new FilteredList<>(searchOrderList, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(myObject -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name field in your object with filter.
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(myObject.getTransactionid()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                    // Filter matches first name.

                } else if (String.valueOf(myObject.getWaitername()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (String.valueOf(myObject.getWaitername()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }

                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Order> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tableProducts.comparatorProperty());
        // 5. Add sorted (and filtered) data to the table.
        tableProducts.setItems(sortedData);
    }


}
