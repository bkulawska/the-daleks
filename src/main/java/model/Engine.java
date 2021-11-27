package model;

import java.util.List;

public class Engine {

    public Grid grid;
    public CollisionDetector collisionDetector;
    public CollisionResolver collisionResolver;

    public Engine(Grid grid) {
        this.grid = grid;
        this.collisionDetector = new CollisionDetector();
        this.collisionResolver = new CollisionResolver(this.grid);
    }

    public void run(){
        //changes will be made here according to further instructions about the game
        grid.placeFirstEntities();
        for(int i=0; i<3; i++){
           step();
        }
    }

    public void step(){
        //do one round logic
        //changes will be made here according to further instructions about the game
        grid.moveMovables();
        List<Pair<Entity, Entity>> collisions = collisionDetector.detect(grid.entities);
        collisionResolver.resolve(collisions);
    }

}
