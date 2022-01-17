package model.level_loaders;

import model.Grid;
import model.entity.Dalek;
import model.entity.PileOfCrap;
import model.entity.Teleport;
import model.entity.TimeTurner;
import model.level_loaders.json_utils.EntityCode;
import model.level_loaders.json_utils.JSONEntity;
import model.level_loaders.json_utils.Level;
import model.level_loaders.json_utils.LevelJSONReader;

import java.util.List;

public class CampaignLevelLoader implements LevelLoader {
    private final List<Level> levels;
    private int currentLevelIndex = 0;

    public CampaignLevelLoader() {
        this.levels = LevelJSONReader.readLevels();
    }

    @Override
    public boolean hasNextLevel() {
        return currentLevelIndex + 1 < levels.size();
    }

    @Override
    public void loadLevel(Grid grid, boolean previousLevelWon) {
        if (previousLevelWon) {
            currentLevelIndex++;
        }

        load(grid, levels.get(currentLevelIndex));
    }

    private void load(Grid grid, Level level) {
        level.entities.forEach((jsonEntity) -> creteEntity(jsonEntity, grid));
    }

    private void creteEntity(JSONEntity jsonEntity, Grid grid) {
        var position = jsonEntity.position.toVector2d();

        switch (EntityCode.values()[jsonEntity.code]) {
            case DOCTOR -> grid.giveBirthToDoctor(position);
            case DALEK -> grid.placeDalek(new Dalek(position));
            case PILE_OF_CRAP -> grid.placePileOfCrap(new PileOfCrap(position));
            case TELEPORT -> grid.placeTeleport(new Teleport(position));
            case TIME_TURNER -> grid.placeTimeTurner(new TimeTurner(position));
        }
    }
}
