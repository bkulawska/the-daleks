package model;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import model.entity.Animal;
import model.entity.Entity;
import model.entity.Movable;
import model.entity.Rock;
import utils.Direction;
import utils.Vector2d;

import java.util.*;

public class Grid {
    /* entities not final for test purposes - needed to set example entities to test Engine.moveMovables() */
    private Map<Vector2d, List<Entity>> entities = new HashMap<>();
    private final int width;
    private final int height;

    /**
     * Temporary attributes for Grid tests
     */
    private List<Entity> movablesThatCouldNotMove;

    @Inject
    public Grid(@Named("gridWidth") int width, @Named("gridHeight") int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Temporary method, to populate map with some animals
     *
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

    public boolean canMove(Entity entity, Direction direction) {
        Vector2d vec = direction.getVector();
        return entity.getPosition().x() + vec.x() >= 0 && entity.getPosition().x() + vec.x() < getWidth() &&
                entity.getPosition().y() + vec.y() >= 0 && entity.getPosition().y() + vec.y() < getHeight();
    }

    public void performMoveOnGrid(Entity entity, Direction direction) {
        if (canMove(entity, direction)) {
            ((Movable) entity).move(direction.getVector());
        }
    }

    public void performMoveOnGridTestMode(Entity entity, Direction direction) {
        if (canMove(entity, direction)) {
            ((Movable) entity).move(direction.getVector());
        }
        else {
            movablesThatCouldNotMove.add(entity);
        }
    }

    public int getWidth() { return this.width; }

    public int getHeight() { return this.height; }

    public Map<Vector2d, List<Entity>> getEntitiesMap() { return entities; }

    public List<Entity> getEntitiesList() { return entities.values().stream().flatMap(List::stream).toList(); }

    public void setEntities(Map<Vector2d, List<Entity>> entities) { this.entities = entities; }

    public List<Entity> getMovablesThatCouldNotMove() { return movablesThatCouldNotMove; }

    public void setMovablesThatCouldNotMove(List<Entity> movablesThatCouldNotMove) { this.movablesThatCouldNotMove = movablesThatCouldNotMove; }
}
