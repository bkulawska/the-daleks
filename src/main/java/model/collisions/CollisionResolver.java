package model.collisions;

import com.google.inject.Inject;
import javafx.util.Pair;
import model.Grid;
import model.entity.Animal;
import model.entity.Entity;
import model.entity.Rock;

import java.util.ArrayList;
import java.util.List;

public class CollisionResolver {
    private final CollisionHandlersMap handlersMap;
    private final Grid grid;

    @Inject
    public CollisionResolver(Grid grid) {
        this.grid = grid;
        this.handlersMap = new CollisionHandlersMap();

        this.handlersMap.putHandler(new Pair<>(Animal.class, Rock.class),
                (animal, rock) -> collideAnimalRock((Animal) animal, (Rock) rock));

        this.handlersMap.putHandler(new Pair<>(Animal.class, Animal.class),
                (animal, otherAnimal) -> collideAnimals((Animal) animal, (Animal) otherAnimal));

        this.handlersMap.putHandler(new Pair<>(Rock.class, Rock.class),
                (rock, otherRock) -> collideRocks((Rock) rock, (Rock) otherRock));

        this.handlersMap.setDefaultHandler((e1, e2) ->
                System.out.println("Handler for " + e1.getClass().getName() + " and " + e2.getClass().getName() + " not found")
        );
    }

    public void resolve(List<Pair<Entity, Entity>> collisions) {
        for (Pair<Entity, Entity> collision : collisions) {
            handlersMap.getHandler(collision).run();
        }
    }

    public ArrayList<Runnable> getConflictSolutionsHandlers(List<Pair<Entity, Entity>> collisions){
        ArrayList<Runnable> conflictSolutionsHandlers = new ArrayList<>();
        for (Pair<Entity, Entity> collision : collisions) {
            conflictSolutionsHandlers.add(handlersMap.getHandler(collision));
        }
        return conflictSolutionsHandlers;
    }


    public void collideAnimalRock(Animal a, Rock r) {
        //some_code
        System.out.println("Solved an animal and a rock collision");
    }

    public void collideAnimals(Animal a1, Animal a2) {
        //some_code
        System.out.println("Solved two animals collision");
    }

    public void collideRocks(Rock r1, Rock r2) {
        //some_code
        System.out.println("Solved two rocks collision");
    }

}
