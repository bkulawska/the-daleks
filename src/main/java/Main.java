import com.google.inject.Guice;
import com.google.inject.Injector;
import controller.KeyboardController;
import guice.GuiceModule;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static final String APP_TITLE = "IT TOADS: DALEKS!";
    private static final String MAIN_VIEW_PATH = "view/mainView.fxml";
    private static final int WINDOW_WIDTH = 1110;
    private static final int WINDOW_HEIGHT = 640;

    @Override
    public void start(Stage primaryStage)  {
        try {
            // load layout from FXML file
            Injector injector = Guice.createInjector(new GuiceModule());
            var keyboardController = injector.getInstance(KeyboardController.class);
            var loader = injector.getInstance(FXMLLoader.class);
            loader.setLocation(Main.class.getResource(MAIN_VIEW_PATH));

            Parent rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);

            keyboardController.attachToScene(scene);
            // setup main window and run it
            primaryStage.setTitle(APP_TITLE);
            primaryStage.setMinWidth(WINDOW_WIDTH);
            primaryStage.setMinHeight(WINDOW_HEIGHT);
            primaryStage.setResizable(true);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Loading FXML file '" + MAIN_VIEW_PATH + "' failed. Probably some problems in Controller's constructor.");
            System.exit(0);
        }
    }
}
