import javafx.util.Pair;
import model.Engine;
import model.Grid;
import model.collisions.CollisionDetector;
import model.collisions.CollisionResolver;
import model.entity.Animal;
import model.entity.Entity;
import model.entity.Movable;
import model.entity.Rock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.Direction;
import utils.Vector2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class JUnitTestGridAndEngine {

    @Nested
    @DisplayName("Testing Movable entities movement on the grid")
    class MovementOnGridNestedTests {
        private Grid grid = new Grid(30, 20);

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
            for (int i = 0; i < 10; i++) {
                var key = new Vector2d(i, i);
                var animal = new Animal(key.x(), key.y());
                entities.computeIfAbsent(key, value -> new ArrayList<>());
                entities.get(key).add(animal);
                if (i % 2 == 0) {
                    var rock = new Rock(key.x(), key.y());
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
