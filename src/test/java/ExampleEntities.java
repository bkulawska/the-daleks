import guice.GuiceModule;
import javafx.util.Pair;
import model.entity.Animal;
import model.entity.Entity;
import model.entity.Rock;
import utils.Vector2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExampleEntities {
    private Map<Vector2d, List<Entity>> entities;
    private List<Pair<Entity, Entity>> expectedCollisions;

    public ExampleEntities() {
        this.entities = new HashMap<>();
        this.expectedCollisions = new ArrayList<>();
        createSample();
    }

    public void placeEntity(Entity entity){
        var key = entity.getPosition();
        entities.computeIfAbsent(key, value -> new ArrayList<>());
        entities.get(key).add(entity);
    }

    public void createSample(){
        int smallerDimension = Math.min(GuiceModule.provideGridWidth(),GuiceModule.provideGridHeight());
        for (int i = 0; i < smallerDimension; i++) {
            // Create animal-animal collisions
            if (i % 2 == 0 && i % 3 != 0 && i % 5 != 0) {
                Animal a1 = new Animal(i, i);
                placeEntity(a1);
                Animal a2 = new Animal(i, i);
                placeEntity(a2);
                expectedCollisions.add(new Pair<>(a1, a2));
            }

            // Create animal-rock collisions
            if (i % 2 != 0 && i % 3 == 0 && i % 5 != 0) {
                Animal a1 = new Animal(i, i);
                placeEntity(a1);
                Rock r1 = new Rock(i, i);
                placeEntity(r1);
                expectedCollisions.add(new Pair<>(a1, r1));
            }

            // Create rock-rock collisions
            if (i % 2 != 0 && i % 3 != 0 && i % 5 == 0) {
                Rock r2 = new Rock(i, i);
                placeEntity(r2);
                Rock r1 = new Rock(i, i);
                placeEntity(r1);
                expectedCollisions.add(new Pair<>(r1, r2));
            }
        }
    }

    public Map<Vector2d, List<Entity>> getEntities() {
        return entities;
    }

    public void setEntities(Map<Vector2d, List<Entity>> entities) {
        this.entities = entities;
    }

    public List<Pair<Entity, Entity>> getExpectedCollisions() {
        return expectedCollisions;
    }

    public void setExpectedCollisions(List<Pair<Entity, Entity>> expectedCollisions) {
        this.expectedCollisions = expectedCollisions;
    }
}
