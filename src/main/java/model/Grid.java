package model;

import model.entity.*;
import utils.Direction;
import utils.Vector2d;
import java.util.*;

public class Grid {

    private final Map<Vector2d, List<Entity>> entities = new HashMap<>();
    private final int width;
    private final int height;

    public Grid(int width, int height){
        this.width=width;
        this.height=height;
    }

    /**
     * Temporary method, to populate map with some animals
     * @param count - number of animals to place
     */
    public void placeRandomAnimals(int count) {
        var r = new Random();
        for (int i=0; i<count; i++)
            place(new Animal(r.nextInt(width), r.nextInt(height)));
    }

    /**
     * Temporary method, to populate map with some rocks
     * @param count - number of rocks to place
     */
    public void placeRandomRocks(int count) {
        var r = new Random();
        for (int i=0; i<count; i++)
            place(new Rock(r.nextInt(width), r.nextInt(height)));
    }

    public void place (Entity entity){
        var key = entity.getPosition();
        entities.computeIfAbsent(key, value -> new ArrayList<>());
        entities.get(key).add(entity);
    }

    public void remove(Entity entity){
        if (entities.containsKey(entity.getPosition())) {
            entities.get(entity.getPosition()).remove(entity);
        }
    }

    public void moveMovables(){
        // store movableEntities in tmp list
        List<Entity> movableEntities = new ArrayList<>();
        entities.values().stream()
                .flatMap(List::stream)
                .forEach(entity -> { if (entity instanceof Movable) movableEntities.add(entity); });

        // remove all movableEntities from entities
        movableEntities.forEach(this::remove);

        // move them (by a random vector, temporarily)
        var r = new Random();
        var allDirections = Arrays.asList(Direction.values());
        movableEntities.forEach(entity -> {
            var direction = allDirections.get(r.nextInt(Direction.N_DIRECTIONS));
            ((Movable) entity).move(direction.getVector());
        });

        // put stored, moved entities back to hash map
        movableEntities.forEach(this::place);
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

    public Map<Vector2d, List<Entity>> getEntitiesMap() {
        return entities;
    }

    public List<Entity> getEntitiesList() {
        return entities.values().stream().flatMap(List::stream).toList();
    }

}
