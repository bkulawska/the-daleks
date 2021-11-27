package model;

import java.util.List;

public class CollisionResolver {

    private Grid grid;

    public CollisionResolver(Grid grid) {
        this.grid = grid;
    }

    public void resolve(List<Pair<Entity, Entity>> collisions){
        for (Pair<Entity, Entity> collision: collisions){
            collide(collision.getFirst(), collision.getSecond());
        }
    }

    public void collide(Entity e1, Entity e2){
        //some_code
        System.out.println("Solved two undefined entities collision");
    }

    public void collide(Animal a, Rock r){
        //some_code
        System.out.println("Solved an animal and a rock collision");
    }

    public void collide(Animal a1, Animal a2){
        //some_code
        System.out.println("Solved two animals collision");
    }

    public void collide(Rock r1, Rock r2){
        //some_code
        System.out.println("Solved two rocks collision");
    }

}
