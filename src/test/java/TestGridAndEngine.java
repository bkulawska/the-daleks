import guice.GuiceModule;
import model.Engine;
import model.Grid;
import model.collisions.CollisionDetector;
import model.collisions.CollisionResolver;
import model.entity.Animal;
import model.entity.Entity;
import model.entity.Movable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import utils.Direction;
import utils.Vector2d;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TestGridAndEngine {

    @Nested
    class TestGrid {
        private final Grid grid = new Grid(30, 20);

        @Test
        @DisplayName("Checking grid boundaries")
        public void canMoveTest() {
            grid.place(new Animal(0, 1));
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
    class TestEngine {
        private ExampleEntities exampleEntities = new ExampleEntities();

        @Mock
        private Grid grid;
        @Mock
        CollisionDetector collisionDetector = new CollisionDetector();
        @Mock
        CollisionResolver collisionResolver = new CollisionResolver(grid);

        @InjectMocks
        private Engine engine;

        public void initialiseMock() {
            MockitoAnnotations.openMocks(this);
            grid = mock(Grid.class, CALLS_REAL_METHODS);
        }

        public void setMockParameters() {
            grid.setEntities(exampleEntities.getEntities());
            grid.setMovablesThatCouldNotMove(new ArrayList<>());
        }

        public void provideEngineWithMockedGrid() {
            engine.setGrid(grid);
        }

        /**
         * Stub Grid.performMoveOnGrid with Grid.performMoveOnGridTestMode
         */
        public void setStubbedPerformMoveOnGrid() {
            doAnswer(new Answer() {
                public Object answer(InvocationOnMock invocation) {
                    Object[] args = invocation.getArguments();
                    Entity entity = (Entity) args[0];
                    Direction direction = (Direction) args[1];
                    grid.performMoveOnGridTestMode(entity, direction);
                    return null;
                }
            }).when(grid).performMoveOnGrid(any(Entity.class), any(Direction.class));
        }

        @BeforeEach
        public void setUpTest() {
            initialiseMock();
            setMockParameters();
            setStubbedPerformMoveOnGrid();
            provideEngineWithMockedGrid();
        }

        @Test
        @DisplayName("If movables can move (Grid.canMove), they should. Unmovables should stay.")
        public void moveMovablesTest() {

            class Cell {
                List<Entity> list = new ArrayList<>();
            }

            // Memorise previous positions
            int width = GuiceModule.provideGridWidth();
            int height = GuiceModule.provideGridHeight();
            Cell[][] memorisedPreviousPositions = new Cell[width][height];
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    memorisedPreviousPositions[i][j] = new Cell();
                }
            }

            for (var entityList : exampleEntities.getEntities().values()) {
                for (var entity : entityList) {
                    memorisedPreviousPositions[entity.getPosition().x()][entity.getPosition().y()].list.add(entity);
                }
            }

            // Move movables
            engine.moveMovables();

            /* All the movables that could have changed their position should have,
            all of the unmovables should have stayed at their previous positions */
            grid.getEntitiesMap().values().stream()
                    .flatMap(List::stream)
                    .forEach(entity -> {
                        var entityChangedItsPosition = !memorisedPreviousPositions[entity.getPosition().x()][entity.getPosition().y()].list.contains(entity);
                        if (entity instanceof Movable) {
                            var movableEntityHadToStay = grid.getMovablesThatCouldNotMove().contains(entity);
                            // !canMove(entity)
                            if (movableEntityHadToStay) {
                                assertFalse(entityChangedItsPosition);
                            }
                            // canMove(entity)
                            else {
                                assertTrue(entityChangedItsPosition);
                            }
                        } else {
                            assertFalse(entityChangedItsPosition);
                        }
                    });

            grid.setMovablesThatCouldNotMove(null);
        }
    }
}

