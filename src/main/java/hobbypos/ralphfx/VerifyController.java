package hobbypos.ralphfx;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */


import hobbypos.ralphfx.modal.DataObj;
import hobbypos.ralphfx.model.User;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Armero
 */
public class VerifyController implements Initializable {


    public boolean focus;
    public boolean sfocus;
    public Integer userid;
    public String lasttouch;
    public String name;
    @FXML
    private Label dateData;
    @FXML
    private Label timeData;
    @FXML
    private TextField userID;
    @FXML
    private TextField passID;
    @FXML
    private Button loginBtn;
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

    public static String removeLastCharOptional(String s) {
        return Optional.ofNullable(s)
                .filter(str -> str.length() != 0)
                .map(str -> str.substring(0, str.length() - 1))
                .orElse(s);
    }

    public static void infoBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    public static void showAlert(Alert.AlertType alertType, Window owner, String message, String title) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.initOwner(owner);
        alert.show();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO


        init();

    }

    public void init() {

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            String time = "";

            if (currentTime.getHour() < 12) {
                time = "AM";
            } else {
                time = "PM";
            }
            dateData.setText(java.time.LocalDate.now().toString());
            timeData.setText(currentTime.getHour() + ":" + currentTime.getMinute() + ":" + currentTime.getSecond() + " " + time);
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play()

        ;
        userID.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (newPropertyValue) {
                    lasttouch = "user";

                }

            }
        });


        passID.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (newPropertyValue) {
                    lasttouch = "pass";

                }
            }
        });
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


        String tempTxt;

        if (lasttouch.contentEquals("pass")) {
            tempTxt = passID.getText();
        } else {
            tempTxt = userID.getText();
        }
        final Node source = (Node) event.getSource();
        if (lasttouch.contentEquals("pass")) {
            if (provideInput(source).contentEquals("123")) {
                passID.setText(removeLastCharOptional(tempTxt));
            } else {
                passID.setText(tempTxt + provideInput(source));
                passID.setText(tempTxt + provideInput(source));
            }
        } else {
            if (provideInput(source).contentEquals("123")) {
                userID.setText(removeLastCharOptional(tempTxt));
            } else {
                userID.setText(tempTxt + provideInput(source));
                userID.setText(tempTxt + provideInput(source));
            }
        }

    }


    private static boolean AdminAccess;
    public static boolean getSelectedData() {

        return AdminAccess;
    }

    private static String Ausername;

    public static String getVoider() {

        return Ausername;
    }
    public void actionLogin(ActionEvent event) {
        Window owner = userID.getScene().getWindow();
        System.out.println(userID.getText());
        System.out.println(passID.getText());


        if (userID.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Please enter a valid Employee ID", "Form error!");
            return;
        }
        if (passID.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Please enter a valid password", "Form error!");
            return;
        }

        String username = userID.getText();
        String password = passID.getText();

        DataObj jdbcDao = new DataObj();
        getUserInfo(Integer.parseInt(username));

        boolean flag = jdbcDao.validateAdmin(username, password);

        AdminAccess = flag;
        if (!flag) {
            infoBox("Wrong Credentials or Only authorize personnel only", null, "Failed");
        } else {
            Stage stage = (Stage) loginBtn.getScene().getWindow();
            stage.getIcons().add(new Image(String.valueOf(this.getClass().getResource("icon2.png"))));
            // do what you have to do
            stage.close();


        }
    }



    private void saveVoid(String cashier){
        DataObj jdbc = new DataObj();
        Connection conn = jdbc.getConnection();
        try {

            String query = "UPDATE current_login SET cashier = '" + cashier + "' WHERE id = 2";
            executeQuery(query);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void executeQuery(String query) {

        DataObj jdbc = new DataObj();
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

    private ObservableList<User> getUserInfo(int userID) {
        DataObj jdbcDao = new DataObj();
        ObservableList<User> UserList = FXCollections.observableArrayList();
        Connection conn = jdbcDao.getConnection();
        String query = "SELECT * FROM user where userID =" + userID;
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            User user;
            while (rs.next()) {
                user = new User(rs.getInt("userType"), rs.getString("firstName"), rs.getString("lastName"));
                name = "Welcome : " + rs.getString("firstName") + " " + rs.getString("lastName");
                Ausername = rs.getString("firstName") +" "+ rs.getString("lastName");
                saveVoid(Ausername);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return UserList;
    }


}
