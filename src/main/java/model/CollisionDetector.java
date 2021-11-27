package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CollisionDetector {

    public CollisionDetector() {
    }

    public List<Pair<Entity, Entity>> detect(HashMap<Vector2d, ArrayList<Entity>> entities){
        List<Pair<Entity, Entity>> collisions = new ArrayList<>();
        for (ArrayList<Entity> list: entities.values()) {
            if (list.size()==2) {
                Entity e1 = list.get(0);
                Entity e2 = list.get(1);
                collisions.add(new Pair<>(e1, e2));
            }
        }
        return collisions;
    }

}
