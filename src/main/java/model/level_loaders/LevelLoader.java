package model.level_loaders;

import model.Grid;

public interface LevelLoader {
    void loadLevel(Grid grid, boolean previousLevelWon);
    boolean hasNextLevel();
}
