import com.google.inject.Guice;
import com.google.inject.Injector;
import guice.GuiceModule;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Injector injector = Guice.createInjector(new GuiceModule());
        FXMLLoader loader = injector.getInstance(FXMLLoader.class);
        loader.setLocation(getClass().getResource("view/mainView.fxml"));

        BorderPane rootLayout = loader.load();

        primaryStage.setScene(new Scene(rootLayout));
        primaryStage.show();

        //just some quick temporary tests
        //Grid grid = new Grid(20, 20);
        //Engine engine = new Engine(grid);
        //engine.run();
        //end
    }
}
