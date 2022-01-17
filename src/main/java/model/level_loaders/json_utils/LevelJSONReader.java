package model.level_loaders.json_utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LevelJSONReader {

    private static final String LEVELS_PATH = "/campaign/levels.json";

    public static List<Level> readLevels() {
        var gson = new Gson();
        var levelListType = new TypeToken<ArrayList<Level>>() {}.getType();

        List<Level> levelsList = new ArrayList<>();
        try {
            var url = LevelJSONReader.class.getResource(LEVELS_PATH);
            var path = Paths.get(Objects.requireNonNull(url).toURI());
            var reader = new JsonReader(new FileReader(path.toString()));
            levelsList = gson.fromJson(reader, levelListType);
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
        }

        return levelsList;
    }
}
