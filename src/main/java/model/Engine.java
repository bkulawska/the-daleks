package model;

import com.google.inject.Inject;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import model.collisions.CollisionDetector;
import model.collisions.CollisionResolver;
import model.entity.Dalek;
import model.entity.Entity;
import utils.Direction;
import utils.GameComputations;
import utils.GameStatus;
import utils.Vector2d;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Engine {

    /* grid not final for test purposes - needed to set grid to test Engine.moveMovables() */
    private Grid grid;
    private final CollisionDetector collisionDetector;
    private final CollisionResolver collisionResolver;
    private final Property<GameStatus> gameStatus = new SimpleObjectProperty<>(GameStatus.GAME_IN_PROGRESS);



    @Inject
    public Engine(Grid grid, CollisionDetector collisionDetector, CollisionResolver collisionResolver) {
        this.grid = grid;
        this.collisionDetector = collisionDetector;
        this.collisionResolver = collisionResolver;
        setUpGrid();
    }

    private void setUpGrid() {

    }

    public void updateGameStatus() {
        boolean doctorDead = grid.getDoctor().dead;
        boolean allDaleksDead = grid.getDaleksList().isEmpty();

        if (!doctorDead && allDaleksDead) {
            gameStatus.setValue(GameStatus.DOCTOR_WON);
        }
        if (doctorDead && !allDaleksDead) {
            gameStatus.setValue(GameStatus.DOCTOR_LOST);
        }
        if (doctorDead && allDaleksDead) {
            gameStatus.setValue(GameStatus.EVERYBODY_DEAD);
        }
    }

    /**
     * Do one round logic
     * Changes will be made here according to further instructions about the game
     */
    public void step(Direction doctorsMove) {
        if (gameStatusProperty().getValue() == GameStatus.GAME_IN_PROGRESS) {
            moveMovables(doctorsMove);
            var collisions = collisionDetector.detect(grid.getEntitiesMap());
            collisionResolver.resolve(collisions);
            updateGameStatus();
        }
    }

    public Optional<Direction> getDalekMoveDirection(Dalek dalek) {
        return GameComputations.getDirectionToTarget(dalek.getPosition(), grid.getDoctor().getPosition());
    }

    public void moveDoctor(Direction doctorsMove) {
            grid.performMoveOnGrid(grid.getDoctor(), doctorsMove);
    }

    public void teleportDoctor() {
        if (grid.getNumberOfTeleportsAvailableToDoctor()>0){
            var height = grid.getHeight();
            var width = grid.getWidth();
            List<Vector2d> freePositions = IntStream
                    .rangeClosed(0, width * height - 1)
                    .mapToObj(i -> new Vector2d(i % width, i / width))
                    .collect(Collectors.toCollection(ArrayList::new));
            Collections.shuffle(freePositions, new Random(System.currentTimeMillis()));
            grid.teleportDoctor(freePositions.get(0));
        }
    }
    public void turnBackTime() {
        if (grid.getNumberOfTimeTurnersAvailableToDoctor()>0){

            //CODE NEEDED to turn back time

            grid.decrementNumberOfTimeTurnersAvailableToDoctor();
        }
    }


    public void moveDaleks() {
        List<Dalek> daleksToMove = grid.getDaleksList();

        // remove all daleks to move from daleks entities map
        daleksToMove.forEach(grid::removeDalek);

        // move each dalek one step closer to the doctor
        daleksToMove.forEach(entity -> getDalekMoveDirection(entity)
                .ifPresent(direction -> grid.performMoveOnGrid(entity, direction)));

        // put stored, moved dalek entities back to hash map
        daleksToMove.forEach(grid::placeDalek);
    }

    public void moveMovables(Direction doctorsMove) {
        moveDoctor(doctorsMove);
        moveDaleks();
    }

    public void reset() {
        grid.reset();
        gameStatus.setValue(GameStatus.GAME_IN_PROGRESS);
    }

    public Grid getGrid() {
        return grid;
    }

    public List<Entity> getEntitiesList() {
        return grid.getEntitiesList();
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public Property<GameStatus> gameStatusProperty() {
        return gameStatus;
    }
}
