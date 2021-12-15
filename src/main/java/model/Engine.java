package model;

import com.google.inject.Inject;
import model.collisions.CollisionDetector;
import model.collisions.CollisionResolver;
import model.entity.Dalek;
import model.entity.Entity;
import model.entity.PileOfCrap;
import utils.Direction;
import utils.GameComputations;
import utils.GameStatus;
import utils.Vector2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Engine {

    /* grid not final for test purposes - needed to set grid to test Engine.moveMovables() */
    private Grid grid;
    private final CollisionDetector collisionDetector;
    private final CollisionResolver collisionResolver;
    private GameStatus gameStatus;

    @Inject
    public Engine(Grid grid, CollisionDetector collisionDetector, CollisionResolver collisionResolver) {
        this.grid = grid;
        this.collisionDetector = collisionDetector;
        this.collisionResolver = collisionResolver;
        this.gameStatus = GameStatus.GAME_IN_PROGRESS;

        setUpGrid();
    }

    public void placeRandomDaleks(int count) {
        var r = new Random();
        for (int i=0; i<count; i++) {
            int x = r.nextInt(grid.getWidth());
            int y = r.nextInt(grid.getHeight());
            if (x != grid.getDoctor().getPosition().x() && y != grid.getDoctor().getPosition().y()) {
                grid.placeDalek(new Dalek(x, y));
            } else {
                i -= 1;
            }
        }
    }

    public void placeRandomPilesOfCrap(int count) {
        var r = new Random();
        for (int i=0; i<count; i++) {
            int x = r.nextInt(grid.getWidth());
            int y = r.nextInt(grid.getHeight());
            if (x != grid.getDoctor().getPosition().x() && y != grid.getDoctor().getPosition().y()) {
                grid.placePileOfCrap(new PileOfCrap(x, y));
            } else {
                i -= 1;
            }
        }
    }

    public void setUpGrid() {
        var r = new Random();
        var randomDoctorPos = new Vector2d(r.nextInt(grid.getWidth()), r.nextInt(grid.getHeight()));
        grid.giveBirthToDoctor(randomDoctorPos);
        placeRandomDaleks((int) (grid.getWidth() / 5));
        placeRandomPilesOfCrap((int) (grid.getWidth() / 5));
    }

    public void updateGameStatus() {
        boolean doctorDead = grid.getDoctor().isDead;
        boolean allDaleksDead = grid.getDaleksMap().isEmpty();

        if (!doctorDead && allDaleksDead) {
            gameStatus = GameStatus.DOCTOR_WON;
        }
        if (doctorDead && !allDaleksDead) {
            gameStatus = GameStatus.DOCTOR_LOST;
        }
        if (doctorDead && allDaleksDead) {
            gameStatus = GameStatus.EVERYBODY_DEAD;
        }
    }

    /**
     * Do one round logic
     * Changes will be made here according to further instructions about the game
     */
    public void step(Vector2d doctorsMove) {
        moveMovables(doctorsMove);
        var collisions = collisionDetector.detect(grid.getEntitiesMap());
        collisionResolver.resolve(collisions);
        updateGameStatus();
    }

    public Direction getDalekMoveDirection(Dalek dalek) {
        return GameComputations.getDalekDoctorDirection(dalek.getPosition(), grid.getDoctor().getPosition());
    }

    public void moveDoctor(Vector2d doctorsMove) {
        try {
            grid.performMoveOnGrid(grid.getDoctor(), Direction.getDirection(doctorsMove));
        } catch(Exception e) {
            System.out.println("Doctor going in a random direction instead of performing the chosen move! "
                    + e.getMessage());
            grid.performMoveOnGrid(grid.getDoctor(), Direction.getRandomDirection());
        }
    }

    public void moveDaleks() {
        // store daleks to move in tmp list
        List<Dalek> daleksToMove = new ArrayList<>();
        grid.getDaleksList()
                .forEach(entity -> {
                    if (entity instanceof Dalek) daleksToMove.add((Dalek) entity);
                });

        // remove all daleks to move from daleks entities map
        daleksToMove.forEach(grid::removeDalek);

        // move each dalek one step closer to the doctor
        daleksToMove.forEach(entity -> {
            var direction = getDalekMoveDirection(entity);
            if (direction != null) {
                grid.performMoveOnGrid(entity, direction);
            }
        });

        // put stored, moved dalek entities back to hash map
        daleksToMove.forEach(grid::placeDalek);
    }

    public void moveMovables(Vector2d doctorsMove) {
        moveDoctor(doctorsMove);
        moveDaleks();
    }

    public Grid getGrid() { return grid; }

    public List<Entity> getEntitiesList() { return grid.getEntitiesList(); }

    public void setGrid(Grid grid) { this.grid = grid; }

    public GameStatus getGameStatus() { return gameStatus; }

    public void setGameStatus(GameStatus gameStatus) { this.gameStatus = gameStatus; }
}
