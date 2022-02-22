package hobbypos.ralphfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminController  implements Initializable {
    private static Stage pStage;
    Scene fxmlFile;
    Parent root;
    Stage window;
    public static Stage getPrimaryStage() {
        return pStage;
    }

    private void setPrimaryStage(Stage pStage) {
        AdminController.pStage = pStage;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
    private void openKTV(ActionEvent event) {
        try {
            openModalWindow("KTV.fxml", "Hobby KTV");
        } catch (Exception ex) {
            System.out.println("" + ex.getMessage());
            ex.printStackTrace();
        }
    }
    @FXML
    private void openBAR(ActionEvent event) {
        try {
            openModalWindow("dashboard.fxml", "Hobby RestoBar");
        } catch (Exception ex) {
            System.out.println("" + ex.getMessage());
            ex.printStackTrace();
        }
    }
    @FXML
    private void openRetail(ActionEvent event) {
        try {
            openModalWindow("Lookup.fxml", "Hobby Retail Store");
        } catch (Exception ex) {
            System.out.println("" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void openProduct(ActionEvent event) {
        try {
            openModalWindow("Products.fxml", "Item Management");
        } catch (Exception ex) {
            System.out.println("" + ex.getMessage());
            ex.printStackTrace();
        }
    }
    @FXML
    private void openTable(ActionEvent event) {
        try {
            openModalWindow("Tables.fxml", "Table Management");
        } catch (Exception ex) {
            System.out.println("" + ex.getMessage());
            ex.printStackTrace();
        }
    }
    @FXML
    private void openWaiter(ActionEvent event) {
        try {
            openModalWindow("waiter.fxml", "Waiter Management");
        } catch (Exception ex) {
            System.out.println("" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void openRetailItems(ActionEvent event) {
        try {
            openModalWindow("retail.fxml", "Retail Management");
        } catch (Exception ex) {
            System.out.println("" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void getTransaction(ActionEvent event) {
        try {
            openModalWindow("Lookup.fxml", "Transactions");
        } catch (Exception ex) {
            System.out.println("" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
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


}
