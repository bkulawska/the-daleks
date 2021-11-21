import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage)  {
        try {
            // load layout from FXML file
            var loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/mainView.fxml"));
            BorderPane rootLayout = loader.load();

            primaryStage.setScene(new Scene(rootLayout));
            primaryStage.show();

        } catch (IOException e) {
            // don't do this in common apps
            e.printStackTrace();
        }
    }
}
