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

public class JUnitTest {

    @Nested
    @DisplayName("Collision detection and resolving tests")
    class ModelNestedTests {
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
            Rock r = new Rock(2,1);
            exampleRocks.add(r);
            grid.place(r);
            expectedCollisions.add(new Pair<>(exampleRocks.get(1), r));
        }

        @Test
        @DisplayName("Compare expected collisions with those detected by CollisionDetector")
        void collisionDetectorTest() {
            // Detect collisions using CollisionDetector
            CollisionDetector collisionDetector = new CollisionDetector();
            List<Pair<Entity, Entity>> detectedCollisions = collisionDetector.detect(grid.getEntitiesMap());

            // Compare expected and actual results
            for (var pair : detectedCollisions) {
                assertTrue(expectedCollisions.contains(pair));
            }
        }

        @Test
        @DisplayName("Check whether runnable CollisionResolver's function handlers have completed their runtime")
        public void collisionResolverTest() throws InterruptedException, ExecutionException {
            // Detect collisions using CollisionDetector
            CollisionDetector collisionDetector = new CollisionDetector();
            List<Pair<Entity, Entity>> detectedCollisions = collisionDetector.detect(grid.getEntitiesMap());

            int collisionsResolved = 0;

            /* Check whether each runnable collision-resolving function handler have completed its runtime
            * - whether collisions have been solved */
            for (var runnable : new CollisionResolver(grid).getConflictSolutionsHandlers(detectedCollisions)) {
                ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
                ScheduledFuture future = scheduledExecutorService.schedule(runnable, 0, TimeUnit.SECONDS);
                future.get();
                assertTrue(future.isDone());
                collisionsResolved++;
                scheduledExecutorService.shutdown();
            }

            // Check whether each and every collision has been resolved
            assertEquals(detectedCollisions.size(), collisionsResolved);
        }

        @Test
        @DisplayName("Make sure all the movables have moved and the unmovables have stayed")
        public void moveMovablesTest() throws CloneNotSupportedException {
            class Cell{
                List<Entity> list = new ArrayList<>();
            }
            // Memoize previous positions
            Cell[][] refs = new Cell[grid.getWidth()][grid.getHeight()];
            for (int i = 0; i < grid.getWidth(); i++){
                for (int j = 0; j < grid.getHeight(); j++){
                    refs[i][j] = new Cell();
                }
            }
            for (var a : exampleAnimals){
                refs[a.getPosition().x][a.getPosition().y].list.add(a);
            }
            for (var r : exampleRocks){
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
                        }
                        else {
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
    class UtilsNestedTests {
        @Test
        public void Vector2dTest(){
            Vector2d vec1 = new Vector2d(4,5);
            Vector2d vec2 = new Vector2d(10,6);
            Vector2d vec3 = new Vector2d(10,16);
            Vector2d vec4 = new Vector2d(5,17);
            assertTrue(vec1.precedes(vec4));
            assertFalse(vec2.precedes(vec1));
            assertTrue(vec2.precedes(vec3));
            assertTrue(vec2.follows(vec1));
            assertFalse(vec1.follows(vec2));
            assertFalse(vec4.follows(vec3));
        }

        @Test
        public void DirectionTest(){
            assertEquals(new Vector2d(1,0), Direction.EAST.getVector());
            assertEquals(new Vector2d(-1,-1), Direction.NORTH_WEST.getVector());
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    class NestedTestsWithMock{
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
