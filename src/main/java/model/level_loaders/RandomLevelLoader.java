package model.level_loaders;

import model.Grid;
import model.entity.Dalek;
import model.entity.Teleport;
import model.entity.TimeTurner;
import utils.Vector2d;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomLevelLoader implements LevelLoader{
    private static final double DALEKS_INITIAL_DENSITY = 13e-3;
    private static final double TELEPORTS_INITIAL_DENSITY = 5e-3;
    private static final double TIME_TURNERS_INITIAL_DENSITY = 5e-3;

    @Override
    public boolean hasNextLevel() {
        return true;
    }

    @Override
    public void loadLevel(Grid grid, boolean previousLevelWon) {
        var height = grid.getHeight();
        var width = grid.getWidth();
        var nDaleks = (int) (grid.getHeight() * grid.getWidth() * DALEKS_INITIAL_DENSITY);
        var nTeleports = (int) (grid.getHeight() * grid.getWidth() * TELEPORTS_INITIAL_DENSITY);
        var nTimeTurners = (int) (grid.getHeight() * grid.getWidth() * TIME_TURNERS_INITIAL_DENSITY);
        List<Vector2d> freePositions = IntStream
                .rangeClosed(0, width * height - 1)
                .mapToObj(i -> new Vector2d(i % width, i / width))
                .collect(Collectors.toCollection(LinkedList::new));
        Collections.shuffle(freePositions, new Random(System.currentTimeMillis()));
        // spawn doctor
        grid.giveBirthToDoctor(freePositions.remove(0));

        for (int dalekIdx = 0; dalekIdx < nDaleks; dalekIdx ++) {
            var position = freePositions.remove(0);
            grid.placeDalek(new Dalek(position));
        }

        //spawn teleports
        for (int teleportIdx = 0; teleportIdx < nTeleports; teleportIdx ++) {
            var position = freePositions.remove(0);
            grid.placeTeleport(new Teleport(position));
        }

        //spawn time turners
        for (int timeTurnerIdx = 0; timeTurnerIdx < nTimeTurners; timeTurnerIdx ++) {
            var position = freePositions.remove(0);
            grid.placeTimeTurner(new TimeTurner(position));
        }

        // initialise gridSnapshotHistory with proper snapshots capacity
        grid.initialiseSnapshotHistory(nTimeTurners);
    }

    @Override
    public int getLevelNumber() {
        return -1;
    }
}
