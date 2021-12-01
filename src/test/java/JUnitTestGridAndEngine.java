import java.io.IOException;
import java.sql.Array;
import java.util.*;
import java.util.concurrent.*;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import guice.GuiceModule;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.util.Pair;
import model.Engine;
import model.Grid;
import model.collisions.CollisionDetector;
import model.collisions.CollisionResolver;
import model.entity.Animal;
import model.entity.Entity;
import model.entity.Movable;
import model.entity.Rock;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.Direction;
import utils.Vector2d;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JUnitTestGridAndEngine {

    @Nested
    @DisplayName("Testing Movable entities movement on the grid")
    class MovementOnGridNestedTests {
        private Grid grid = new Grid(30, 20);
        private List<Animal> exampleAnimals;
        private List<Rock> exampleRocks;
        private List<Pair<Entity, Entity>> expectedCollisions;

        @BeforeEach
        @DisplayName("Place sample entities and create some collisions between them")
        public void setUp() {
            exampleAnimals = new ArrayList<>();
            exampleRocks = new ArrayList<>();
            expectedCollisions = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                // Create sample animals
                exampleAnimals.add(new Animal(i, i));
                // Create sample rocks
                if (i % 2 == 0) {
                    // Some of them collide with other animals
                    exampleRocks.add(new Rock(i, i));
                    expectedCollisions.add(new Pair<>(exampleAnimals.get(i), exampleRocks.get(i)));
                } else {
                    // Some of them do not
                    exampleRocks.add(new Rock(2 * i, i));
                }

                // Place those entities on the grid
                grid.place(exampleAnimals.get(i));
                grid.place(exampleRocks.get(i));
            }

            // Add some more collisions
            int[] nums = {1, 5, 7};
            for (var i : nums) {
                Animal a = new Animal(i, i);
                exampleAnimals.add(a);
                grid.place(a);
                expectedCollisions.add(new Pair<>(exampleAnimals.get(i), a));
            }
            Rock r = new Rock(2, 1);
            exampleRocks.add(r);
            grid.place(r);
            expectedCollisions.add(new Pair<>(exampleRocks.get(1), r));
        }

        @Test
        @DisplayName("Make sure all the movables have moved and the unmovables have stayed")
        public void moveMovablesTest() throws CloneNotSupportedException {
            class Cell {
                List<Entity> list = new ArrayList<>();
            }
            // Memoize previous positions
            Cell[][] refs = new Cell[grid.getWidth()][grid.getHeight()];
            for (int i = 0; i < grid.getWidth(); i++) {
                for (int j = 0; j < grid.getHeight(); j++) {
                    refs[i][j] = new Cell();
                }
            }
            for (var a : exampleAnimals) {
                refs[a.getPosition().x][a.getPosition().y].list.add(a);
            }
            for (var r : exampleRocks) {
                refs[r.getPosition().x][r.getPosition().y].list.add(r);
            }

            // Make a move
            grid.moveMovables();

            /* All the animals should have changed their positions,
            all the rocks should have stayed at their previous positions */
            grid.getEntitiesMap().values().stream()
                    .flatMap(List::stream)
                    .forEach(entity -> {
                        if (entity instanceof Movable) {
                            assertFalse(refs[entity.getPosition().x][entity.getPosition().y].list.contains(entity));
                        } else {
                            assertTrue(refs[entity.getPosition().x][entity.getPosition().y].list.contains(entity));
                        }
                    });

        }

        @Test
        @DisplayName("Checking grid boundaries")
        public void canMoveTest() {
            grid.place(new Animal(0,1));
            grid.getEntitiesMap().get(new Vector2d(0, 1))
                    .forEach(entity -> {
                        assertTrue(grid.canMove(entity, Direction.NORTH));
                        assertTrue(grid.canMove(entity, Direction.NORTH_EAST));
                        assertTrue(grid.canMove(entity, Direction.EAST));
                        assertTrue(grid.canMove(entity, Direction.SOUTH_EAST));
                        assertTrue(grid.canMove(entity, Direction.SOUTH));
                        assertFalse(grid.canMove(entity, Direction.SOUTH_WEST));
                        assertFalse(grid.canMove(entity, Direction.WEST));
                        assertFalse(grid.canMove(entity, Direction.NORTH_WEST));
                    });
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @DisplayName("Check engine work with mocked grid")
    class GridEngineTestsMock {
        private Map<Vector2d, List<Entity>> entities = new HashMap<>();
        @InjectMocks
        private Engine engine;

        @Mock
        private Grid grid;

        @Spy CollisionDetector collisionDetector = new CollisionDetector();
        @Spy CollisionResolver collisionResolver = new CollisionResolver(grid);


        // Given entities on the grid
        @BeforeEach
        void setUp(){
            for (int i = 0; i < 10; i++){
                var key = new Vector2d(i,i);
                var animal = new Animal(key.x, key.y);
                entities.computeIfAbsent(key, value -> new ArrayList<>());
                entities.get(key).add(animal);
                if (i % 2 == 0) {
                    var rock = new Rock(key.x, key.y);
                    entities.get(key).add(rock);
                }
            }
        }

        @Test
        @DisplayName("Engine - grid mocked tests")
        void step() {
            when(grid.getEntitiesMap()).thenReturn(entities);
            engine.step();
        }
    }
}
