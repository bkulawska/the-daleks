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

public class JUnitTestCollisions {

    @Nested
    @DisplayName("Collision detection and resolving tests")
    class CollisionsNestedTests {
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
    }
}
