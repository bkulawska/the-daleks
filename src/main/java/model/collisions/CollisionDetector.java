package model.collisions;

import com.google.inject.Singleton;
import javafx.util.Pair;
import model.entity.Entity;
import utils.Vector2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Singleton
public class CollisionDetector {

    public CollisionDetector() {
    }

    public List<Pair<Entity, Entity>> detect(Map<Vector2d, List<Entity>> entities) {
        List<Pair<Entity, Entity>> collisions = new ArrayList<>();

        entities.values()
                .stream()
                .filter(list -> list.size() > 1)
                .forEach(list -> insertCollisionsFromList(list, collisions));

        return collisions;
    }

    private void insertCollisionsFromList(List<Entity> list, List<Pair<Entity, Entity>> collisions) {
        for (var entity : list) {
            for (var otherEntity : list) {
                if (entity == otherEntity) break;
                collisions.add(new Pair<>(entity, otherEntity));
            }
        }
    }
}
