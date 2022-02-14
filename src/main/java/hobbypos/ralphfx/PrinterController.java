package hobbypos.ralphfx;

import com.github.anastaciocintra.output.PrinterOutputStream;
import hobbypos.ralphfx.modal.DataObj;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import static java.sql.Types.NULL;


public class PrinterController implements Initializable {


    @FXML
    private ComboBox barCombo;

    @FXML
    private ComboBox drinkCombo;

    @FXML
    private ComboBox kCombo;

    @FXML
    private ComboBox ktvCombo;

    @FXML
    private Label successTxt;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

            System.out.println("Usage: java -jar getstart.jar (\"printer name\")");
            System.out.println("Printer list to use:");
            String[] printServicesNames = PrinterOutputStream.getListPrintServicesNames();
        ObservableList<String> list = FXCollections.observableArrayList();
            for(String printServiceName: printServicesNames){
                System.out.println(printServiceName);
                list.add(printServiceName);
            }

        barCombo.setItems(null);
        barCombo.setItems(list);
        drinkCombo.setItems(null);
        drinkCombo.setItems(list);
        kCombo.setItems(null);
        kCombo.setItems(list);
        ktvCombo.setItems(null);
        ktvCombo.setItems(list);


    }
    @FXML
    public void saveSettings(ActionEvent event){
        deletePrinter();

        String bar = barCombo.getSelectionModel().getSelectedItem().toString();;
        String drink = drinkCombo.getSelectionModel().getSelectedItem().toString();;
        String kitchen = kCombo.getSelectionModel().getSelectedItem().toString();;
        String ktv = ktvCombo.getSelectionModel().getSelectedItem().toString();;

        insert(bar,"BAR");
        insert(drink, "DRINK");
        insert(kitchen, "KITCHEN");
        insert(ktv,"KTV");

        successTxt.setText("Printer Assignment has been set!");
    }
    private void insert(String printerName, String assign){

        DataObj  jdbc = new DataObj();
        Connection conn = jdbc.getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement prepStmt = conn.prepareStatement("insert into printer(printerID,printername,assign) values('" + NULL + "',?,?)");
            prepStmt.setString(1, printerName);
            prepStmt.setString(2, assign);
            prepStmt.addBatch();

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

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void deletePrinter() {

        DataObj  jdbc = new DataObj();
        Connection conn = jdbc.getConnection();
        try {
            String query = "DELETE FROM printer";
            executeQuery(query);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void executeQuery(String query) {

        DataObj  jdbc = new DataObj();
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

}
