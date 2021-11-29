package model;

import com.google.inject.Inject;
import model.collisions.CollisionDetector;
import model.collisions.CollisionResolver;
import model.entity.Entity;

import java.util.List;

public class Engine {

    private final Grid grid;
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

    /**
     * Do one round logic
     * Changes will be made here according to further instructions about the game
     */
    public void step() {
        grid.moveMovables();
        var collisions = collisionDetector.detect(grid.getEntitiesMap());
        collisionResolver.resolve(collisions);
    }

}
