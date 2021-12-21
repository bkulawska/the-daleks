package guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import guice.provider.FXMLLoaderProvider;
import javafx.fxml.FXMLLoader;
import model.Engine;
import model.Grid;

public class GuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(FXMLLoader.class).toProvider(FXMLLoaderProvider.class);
        bind(Engine.class).in(Singleton.class);
        bind(Grid.class).in(Singleton.class);
    }

    @Provides
    @Named("gridWidth")
    public static Integer provideGridWidth() {
        return 30;
    }

    @Provides
    @Named("gridHeight")
    public static Integer provideGridHeight() {
        return 20;
    }
}
