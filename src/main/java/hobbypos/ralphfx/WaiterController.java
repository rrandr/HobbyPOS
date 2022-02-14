/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package hobbypos.ralphfx;

import hobbypos.ralphfx.modal.DataObj;
import hobbypos.ralphfx.model.Waiter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
 * @author Admin
 */
public class WaiterController implements Initializable {

    DataObj jdbc;
    @FXML
    private TextField tfTableName;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnAssign;
    @FXML
    private TableView<Waiter> tvTables;
    @FXML
    private TableColumn<Waiter, Integer> colId;
    @FXML
    private TableColumn<Waiter, String> colName;
    @FXML
    private TableColumn<Waiter, String> colLocation;
    private ObservableList<Waiter> waiterList;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO


        jdbc = new DataObj();
        showWaiter();
        addListenerForTable();

    }

    @FXML
    public void showWaiter() {

        ObservableList<Waiter> list = getWaiterList();
        colId.setCellValueFactory(new PropertyValueFactory<Waiter, Integer>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<Waiter, String>("name"));
        colLocation.setCellValueFactory(new PropertyValueFactory<Waiter, String>("location"));
        tvTables.setItems(list);

        System.out.println("pasok" + tvTables);
    }

    private void insertRecord() {
        String name = tfTableName.getText();
        if (!name.isEmpty()) {
            String query = "INSERT INTO `waiter` (name,location) VALUES('" + name + "','1')";
            DashboardController dash = new DashboardController();
            executeQuery(query);
            showWaiter();
            tfTableName.setText("");
        }
    }

    @FXML
    private ObservableList<Waiter> getWaiterList() {
        ObservableList<Waiter> waiterList = FXCollections.observableArrayList();
        Connection conn = jdbc.getConnection();
        String query = "SELECT * FROM waiter WHERE location = '1'";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Waiter waiter;
            while (rs.next()) {
                waiter = new Waiter(rs.getInt("waiterID"), rs.getString("name"), rs.getString("location"));
                waiterList.add(waiter);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println(waiterList);
        return waiterList;
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
    private void saveTable(ActionEvent event) {
        insertRecord();
    }

    private void addListenerForTable() {
        tvTables.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                btnUpdate.setDisable(false);
                btnDelete.setDisable(false);
                tfTableName.setText(newSelection.getName());

            } else {
                tfTableName.setText("");
                btnUpdate.setDisable(true);
                btnDelete.setDisable(true);
            }
        });

    }

    @FXML
    private void editEntry(ActionEvent event) {
        Connection conn = jdbc.getConnection();
        try {
            Waiter waiter = tvTables.getSelectionModel().getSelectedItem();
            String query = "UPDATE waiter SET name = '" + tfTableName.getText() + " ' WHERE waiterid = '" + waiter.getId() + "'";
            executeQuery(query);
            showWaiter();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }


    }


    @FXML
    private void deleteEntry(ActionEvent event) {
        Connection conn = jdbc.getConnection();
        try {
            Waiter waiter = tvTables.getSelectionModel().getSelectedItem();
            String query = "DELETE FROM waiter WHERE waiterid = '" + waiter.getId() + "'";
            executeQuery(query);
            showWaiter();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
