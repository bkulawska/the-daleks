package model;

import model.entity.Entity;

import java.util.List;

public class Engine {

    public Grid grid;
    public CollisionDetector collisionDetector;
    public CollisionResolver collisionResolver;

    public Engine(Grid grid) {
        this.grid = grid;
        grid.placeRandomAnimals(20);
        grid.placeRandomRocks(10);
        this.collisionDetector = new CollisionDetector();
        this.collisionResolver = new CollisionResolver(this.grid);
    }

    /**
     *  Do one round logic
     *  Changes will be made here according to further instructions about the game
     */
    public void step(){
        grid.moveMovables();
        var collisions = collisionDetector.detect(grid.getEntitiesMap());
        collisionResolver.resolve(collisions);
    }

    public List<Entity> getEntitiesList() {
        return grid.getEntitiesList();
    }

}
