package hobbypos.ralphfx;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class PriceController implements Initializable {

    @FXML
    private TextField bardcodeTxt;
    @FXML
    private Label itemN;
    @FXML
    private Label itemP;
    @FXML
    private Label itemDisc;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                bardcodeTxt.requestFocus();
            }
        });

    }
}
