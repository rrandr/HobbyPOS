package hobbypos.ralphfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HobbyPOS extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent parentRoot = fxmlLoader.load();

            primaryStage.setTitle("Hobby Restobar and KTV");
            primaryStage.setResizable(false);
            primaryStage.setIconified(false);
            primaryStage.setScene(new Scene(parentRoot));
            primaryStage.getIcons().add(new Image(String.valueOf(this.getClass().getResource("icon2.png"))));
            primaryStage.show();

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            System.out.println(ex.getCause());
            System.exit(0);
        }
    }

}
