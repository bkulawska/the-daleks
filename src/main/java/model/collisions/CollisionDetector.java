package model.collisions;

import com.google.inject.Singleton;
import javafx.util.Pair;
import model.entity.Entity;
import utils.Vector2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Singleton
public class CollisionDetector {

    public CollisionDetector() {
    }

    public List<Pair<Entity, Entity>> detect(Map<Vector2d, List<Entity>> entities) {
        return entities.values()
                .stream()
                .filter(list -> list.size() > 1)
                .flatMap(this::insertCollisionsFromList)
                .toList();
    }

    private Stream<Pair<Entity, Entity>> insertCollisionsFromList(List<Entity> list) {
        List<Pair<Entity, Entity>> collisions = new ArrayList<>();
        for (var entity : list) {
            for (var otherEntity : list) {
                if (entity != otherEntity) {
                    collisions.add(new Pair<>(entity, otherEntity));
                }
            }
        }
        return collisions.stream();
    }
}
