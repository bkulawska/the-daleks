package model.level_loaders;

import model.Grid;

public interface LevelLoader {
    void loadLevel(Grid grid, boolean previousLevelWon);
    int getLevelNumber();
    int getMaxLevelNumber();
    boolean hasNextLevel();
}
