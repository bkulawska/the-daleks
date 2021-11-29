import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Engine;
import model.Grid;

import java.io.IOException;

public class Main extends Application {

    private final String APP_TITLE = "IT-Toads Project";
    private final String MAIN_VIEW_PATH = "view/mainView.fxml";

    @Override
    public void start(Stage primaryStage)  {
        try {
            // load layout from FXML file
            var loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(MAIN_VIEW_PATH));
            Parent rootLayout = loader.load();

            // load main controller and initialize business logic
            var engine = new Engine(new Grid(35, 20));
            MainController mainController = loader.getController();
            mainController.initialSetup(engine);

            // setup main window and run it
            primaryStage.setTitle(APP_TITLE);
            primaryStage.setMinWidth(1400);
            primaryStage.setMinHeight(1000);
            primaryStage.setResizable(true);
            primaryStage.setScene(new Scene(rootLayout));
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Loading FXML file '" + MAIN_VIEW_PATH + "' failed.");
            System.exit(0);
        }
    }
}
