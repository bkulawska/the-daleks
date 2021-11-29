package model;

import javafx.util.Pair;
import model.entity.Entity;
import utils.Vector2d;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CollisionDetector {

    public CollisionDetector() {
    }

    public List<Pair<Entity, Entity>> detect(Map<Vector2d, List<Entity>> entities){
        List<Pair<Entity, Entity>> collisions = new ArrayList<>();
        for (var list: entities.values()) {
            if (list.size()==2) {
                Entity e1 = list.get(0);
                Entity e2 = list.get(1);
                collisions.add(new Pair<>(e1, e2));
            }
        }
        return collisions;
    }

}
