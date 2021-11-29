package model;

import javafx.util.Pair;
import model.entity.Animal;
import model.entity.Entity;
import model.entity.Rock;
import java.util.List;

    /*
        TODO: Fix this collision resolving system, it does not work.
              always default collide(Entity e1, Entity e2) method is called.
     */

public class CollisionResolver {

    private final Grid grid;

    public CollisionResolver(Grid grid) {
        this.grid = grid;
    }

    public void resolve(List<Pair<Entity, Entity>> collisions){
        for (Pair<Entity, Entity> collision: collisions) {
            collide(collision.getKey(), collision.getValue());
        }
    }

    private void collide(Entity e1, Entity e2){
        //some_code
        System.out.println("Solved two undefined entities collision");
    }

    private void collide(Animal a, Rock r){
        //some_code
        System.out.println("Solved an animal and a rock collision");
    }

    private void collide(Animal a1, Animal a2){
        //some_code
        System.out.println("Solved two animals collision");
    }

    private void collide(Rock r1, Rock r2){
        //some_code
        System.out.println("Solved two rocks collision");
    }

}
