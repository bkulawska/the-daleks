package model;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import model.entity.*;
import utils.Direction;
import utils.Vector2d;

import java.util.*;

public class Grid {
    private Map<Vector2d, List<Dalek>> daleks = new HashMap<>();
    private Map<Vector2d, List<PileOfCrap>> pilesOfCrap = new HashMap<>();
    private Doctor doctor;

    private final int width;
    private final int height;

    /**
     * Temporary attribute for Grid and Engine tests
     */
    private List<Entity> movablesThatCouldNotMove;

    @Inject
    public Grid(@Named("gridWidth") int width, @Named("gridHeight") int height) {
        this.width = width;
        this.height = height;
    }

    public void giveBirthToDoctor(Vector2d initialPosition) {
        this.doctor = new Doctor(initialPosition.x(), initialPosition.y());
    }

    public void placeDalek(Dalek dalek) {
        var key = dalek.getPosition();
        daleks.computeIfAbsent(key, value -> new ArrayList<>());
        daleks.get(key).add(dalek);
    }

    public void removeDalek(Dalek dalek) {
        if (daleks.containsKey(dalek.getPosition())) {
            daleks.get(dalek.getPosition()).remove(dalek);
        }
    }

    public void placePileOfCrap(PileOfCrap pileOfCrap) {
        var key = pileOfCrap.getPosition();
        pilesOfCrap.computeIfAbsent(key, value -> new ArrayList<>());
        pilesOfCrap.get(key).add(pileOfCrap);
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

    public void reset() {
        this.doctor = null;
        this.daleks = new HashMap<>();
        this.pilesOfCrap = new HashMap<>();
    }

    /**
     * For Grid and Engine test purposes only
     */
    public void performMoveOnGridTestMode(Entity entity, Direction direction) {
        if (canMove(entity, direction)) {
            ((Movable) entity).move(direction.getVector());
        } else {
            movablesThatCouldNotMove.add(entity);
        }
    }

    public Map<Vector2d, List<Entity>> getEntitiesMap() {
        Map<Vector2d, List<Entity>> entities = new HashMap<>();

        daleks.keySet().forEach(position -> entities.put(position, new ArrayList<>()));
        pilesOfCrap.keySet().forEach(position -> entities.put(position, new ArrayList<>()));

        for (var daleksEntry : daleks.entrySet()) {
            entities.get(daleksEntry.getKey()).addAll(daleksEntry.getValue());
        }
        for (var pilesOfCrapEntry : pilesOfCrap.entrySet()) {
            entities.get(pilesOfCrapEntry.getKey()).addAll(pilesOfCrapEntry.getValue());
        }

        var key = this.doctor.getPosition();
        entities.computeIfAbsent(key, value -> new ArrayList<>());
        entities.get(key).add(doctor);
        return entities;
    }

    public List<Entity> getEntitiesList() {
        return getEntitiesMap().values().stream().flatMap(List::stream).toList();
    }

    public List<Dalek> getDaleksList() {
        return daleks.values().stream().flatMap(List::stream).toList();
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Map<Vector2d, List<Dalek>> getDaleksMap() {
        return daleks;
    }

    public void setDaleks(Map<Vector2d, List<Dalek>> daleks) {
        this.daleks = daleks;
    }

    public void setPilesOfCrap(Map<Vector2d, List<PileOfCrap>> pilesOfCrap) {
        this.pilesOfCrap = pilesOfCrap;
    }

    public List<Entity> getMovablesThatCouldNotMove() {
        return movablesThatCouldNotMove;
    }

    public void setMovablesThatCouldNotMove(List<Entity> movablesThatCouldNotMove) {
        this.movablesThatCouldNotMove = movablesThatCouldNotMove;
    }
}
