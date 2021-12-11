package model;

import com.google.inject.Inject;
import model.collisions.CollisionDetector;
import model.collisions.CollisionResolver;
import model.entity.Entity;
import model.entity.Movable;
import utils.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Engine {

    /* grid not final for test purposes - needed to set grid to test Engine.moveMovables() */
    private Grid grid;
    private final CollisionDetector collisionDetector;
    private final CollisionResolver collisionResolver;

    @Inject
    public Engine(Grid grid, CollisionDetector collisionDetector, CollisionResolver collisionResolver) {
        this.grid = grid;
        this.collisionDetector = collisionDetector;
        this.collisionResolver = collisionResolver;
        grid.placeRandomAnimals(20);
        grid.placeRandomRocks(10);
    }

    public Grid getGrid() {
        return grid;
    }

    public List<Entity> getEntitiesList() {
        return grid.getEntitiesList();
    }

    public void setGrid(Grid grid) { this.grid = grid; }

    /**
     * Do one round logic
     * Changes will be made here according to further instructions about the game
     */
    public void step() {
        moveMovables();
        var collisions = collisionDetector.detect(grid.getEntitiesMap());
        collisionResolver.resolve(collisions);
    }

    public void moveMovables() {
        // store movableEntities in tmp list
        List<Entity> movableEntities = new ArrayList<>();
        grid.getEntitiesList()
                .forEach(entity -> {
                    if (entity instanceof Movable) movableEntities.add(entity);
                });

        // remove all movableEntities from entities
        movableEntities.forEach(grid::remove);

        // move them (by a random vector, temporarily)
        var r = new Random();
        var allDirections = Arrays.asList(Direction.values());
        movableEntities.forEach(entity -> {
            var direction = allDirections.get(r.nextInt(Direction.N_DIRECTIONS));
            grid.performMoveOnGrid(entity, direction);
        });

        // put stored, moved entities back to hash map
        movableEntities.forEach(grid::place);
    }
}
