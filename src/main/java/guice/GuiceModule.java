package guice;

import com.google.inject.AbstractModule;
import guice.provider.FXMLLoaderProvider;
import javafx.fxml.FXMLLoader;

public class GuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(FXMLLoader.class).toProvider(FXMLLoaderProvider.class);
    }
}
