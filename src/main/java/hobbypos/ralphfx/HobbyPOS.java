package hobbypos.ralphfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HobbyPOS extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try{

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent parentRoot = fxmlLoader.load();

            primaryStage.setTitle("Hobby Restobar and KTV");
            primaryStage.setResizable(false);
            primaryStage.setIconified(false);
            primaryStage.setScene(new Scene(parentRoot));
            primaryStage.show();

        }
        catch(IOException ex){
            System.err.println(ex.getMessage());
            System.out.println(ex.getCause());
            System.exit(0);
        }
    }

    public static void main(String[]args){
        launch(args);
    }

}
