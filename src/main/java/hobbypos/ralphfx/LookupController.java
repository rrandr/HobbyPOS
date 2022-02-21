/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package hobbypos.ralphfx;

import hobbypos.ralphfx.modal.DataObj;
import hobbypos.ralphfx.model.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Armero
 */


public class LookupController implements Initializable {

    DataObj jdbc;
    @FXML
    private TableView<Order> tableProducts;
    @FXML
    private TableColumn<Order, String> colId;
    @FXML
    private TableColumn<Order, String> colDescription;
    @FXML
    private TableColumn<Order, String> colPrice;
    @FXML
    private TableColumn<Order, String> colCategory;
    @FXML
    private TextField searchBox;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        jdbc = new DataObj();
        showTransactions();
        search();
    }

    public void showTransactions() {
        ObservableList<Order> list = getOrderList();


        colId.setCellValueFactory(new PropertyValueFactory<Order, String>("transactionid"));
        colDescription.setCellValueFactory(new PropertyValueFactory<Order, String>("tableName"));
        colPrice.setCellValueFactory(new PropertyValueFactory<Order, String>("waitername"));
        colCategory.setCellValueFactory(new PropertyValueFactory<Order, String>("OrderDate"));

        tableProducts.setItems(list);
    }
    ObservableList<Order> searchOrderList = FXCollections.observableArrayList();
    private ObservableList<Order> getOrderList() {
        ObservableList<Order> OrdertList = FXCollections.observableArrayList();
        Connection conn = jdbc.getConnection();
        String query = "SELECT DISTINCT  transactionid,tableName,waiterName,OrderDate from Orders ";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Order orders;
            while (rs.next()) {
                orders = new Order(rs.getString("transactionid"), rs.getString("tableName"), rs.getString("waitername"), rs.getString("OrderDate"));
                OrdertList.add(orders);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        searchOrderList = OrdertList;
        return OrdertList;
    }

    public void search(){

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
                }
                else if (String.valueOf(myObject.getWaitername()).toLowerCase().contains(lowerCaseFilter)) {
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
