package model;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import model.entity.*;
import model.memento.GridSnapshot;
import model.memento.SnapshotHistory;
import utils.Direction;
import utils.Vector2d;

import java.util.*;

public class Grid {
    private Map<Vector2d, List<Dalek>> daleks = new HashMap<>();
    private Map<Vector2d, PileOfCrap> pilesOfCrap = new HashMap<>();
    private Map<Vector2d, Teleport> teleports = new HashMap<>();
    private Map<Vector2d, TimeTurner> timeTurners = new HashMap<>();
    private Doctor doctor;

    private SnapshotHistory snapshotHistory;

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
        pilesOfCrap.putIfAbsent(key, pileOfCrap);
    }

    public void placeTeleport(Teleport teleport) {
        var key = teleport.getPosition();
        teleports.putIfAbsent(key, teleport);
    }

    public void placeTimeTurner(TimeTurner timeTurner) {
        var key = timeTurner.getPosition();
        timeTurners.putIfAbsent(key, timeTurner);
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
        this.teleports = new HashMap<>();
        this.timeTurners = new HashMap<>();
        this.snapshotHistory = null;
    }

    public void initialiseSnapshotHistory(int maxSnapshots) {
        snapshotHistory = new SnapshotHistory(maxSnapshots);
    }

    public void createGridSnapshot() {
        snapshotHistory.addSnapshot(new GridSnapshot(daleks, pilesOfCrap, doctor));
    }

    public void restoreLatestSnapshot() {
        Optional<GridSnapshot> latestGridSnapshot = snapshotHistory.getMostRecentSnapshot();
        if (latestGridSnapshot.isPresent()) {
            this.daleks = latestGridSnapshot.get().getDaleks();
            this.pilesOfCrap = latestGridSnapshot.get().getPilesOfCrap();
            this.doctor = latestGridSnapshot.get().getDoctor();
        }
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
        teleports.keySet().forEach(position -> entities.put(position, new ArrayList<>()));
        timeTurners.keySet().forEach(position -> entities.put(position, new ArrayList<>()));

        for (var daleksEntry : daleks.entrySet()) {
            entities.get(daleksEntry.getKey()).addAll(daleksEntry.getValue());
        }
        for (var pilesOfCrapEntry : pilesOfCrap.entrySet()) {
            entities.get(pilesOfCrapEntry.getKey()).add(pilesOfCrapEntry.getValue());
        }
        for (var teleportsEntry : teleports.entrySet()) {
            entities.get(teleportsEntry.getKey()).add(teleportsEntry.getValue());
        }
        for (var timeTurnersEntry : timeTurners.entrySet()) {
            entities.get(timeTurnersEntry.getKey()).add(timeTurnersEntry.getValue());
        }

        var key = this.doctor.getPosition();
        entities.computeIfAbsent(key, value -> new ArrayList<>());
        entities.get(key).add(doctor);
        return entities;
    }

    public boolean isFree(Vector2d position) {
        if(doctor != null && doctor.position.equals(position)) return false;

        return  !(daleks.containsKey(position)
                || pilesOfCrap.containsKey(position)
                || timeTurners.containsKey(position)
                || teleports.containsKey(position));
    }

    public List<Entity> getEntitiesList() {
        return getEntitiesMap()
                .values()
                .stream()
                .flatMap(List::stream)
                .toList();
    }

    public List<Dalek> getDaleksList() {
        return daleks.values()
                .stream()
                .flatMap(List::stream)
                .toList();
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

    public Map<Vector2d, Teleport> getTeleportsMap() {
        return teleports;
    }

    public Map<Vector2d, TimeTurner> getTimeTurnersMap() {
        return timeTurners;
    }

    public void setDaleks(Map<Vector2d, List<Dalek>> daleks) {
        this.daleks = daleks;
    }

    public void setPilesOfCrap(Map<Vector2d, PileOfCrap> pilesOfCrap) {
        this.pilesOfCrap = pilesOfCrap;
    }

    public List<Entity> getMovablesThatCouldNotMove() {
        return movablesThatCouldNotMove;
    }

    public void setMovablesThatCouldNotMove(List<Entity> movablesThatCouldNotMove) {
        this.movablesThatCouldNotMove = movablesThatCouldNotMove;
    }

    public void removePowerUp(TimeTurner timeTurner) {
        timeTurners.remove(timeTurner.position);
    }

    public void removePowerUp(Teleport teleport) {
        teleports.remove(teleport.position);
    }
}
