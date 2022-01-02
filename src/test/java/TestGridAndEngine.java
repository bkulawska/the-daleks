import guice.GuiceModule;
import model.Engine;
import model.Grid;
import model.entity.Dalek;
import model.entity.Doctor;
import model.entity.Entity;
import model.entity.Movable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import utils.Direction;
import utils.Vector2d;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

public class TestGridAndEngine {

    @Nested
    class TestGrid {
        private final Grid grid = new Grid(GuiceModule.provideGridWidth(), GuiceModule.provideGridHeight());

        @Test
        @DisplayName("Checking grid boundaries")
        public void canMoveTest() {
            grid.setDoctor(new Doctor(2, 2));
            grid.placeDalek(new Dalek(0, 1));
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
    @RunWith(MockitoJUnitRunner.class)
    class TestEngine {

        private final ExampleEntities exampleEntities = new ExampleEntities();

        class Cell {
            List<Entity> list = new ArrayList<>();
        }

        @Spy
        private final Grid grid = new Grid(GuiceModule.provideGridWidth(), GuiceModule.provideGridHeight());

        @InjectMocks
        private Engine engine;

        public void initialiseMock() { MockitoAnnotations.openMocks(this); }

        public void setMockParameters() {
            grid.setDoctor(exampleEntities.getDoctor());
            grid.setDaleks(exampleEntities.getDaleks());
            grid.setPilesOfCrap(exampleEntities.getPilesOfCrap());
            grid.setMovablesThatCouldNotMove(new ArrayList<>());
        }

        public void provideEngineWithMockedGrid() { engine.setGrid(grid); }

        /**
         * Stub Grid.performMoveOnGrid with Grid.performMoveOnGridTestMode
         */
        public void setStubbedPerformMoveOnGrid() {
            doAnswer(invocation -> {
                Object[] args = invocation.getArguments();
                Entity entity = (Entity) args[0];
                Direction direction = (Direction) args[1];
                grid.performMoveOnGridTestMode(entity, direction);
                return null;
            }).when(grid).performMoveOnGrid(any(Entity.class), any(Direction.class));
        }

        @BeforeEach
        public void setUpTest() {
            initialiseMock();
            setMockParameters();
            setStubbedPerformMoveOnGrid();
            provideEngineWithMockedGrid();
        }

        public Cell[][] initialiseTestAttributes() {
            int width = GuiceModule.provideGridWidth();
            int height = GuiceModule.provideGridHeight();
            Cell[][] memorisedPreviousPositions = new Cell[width][height];
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    memorisedPreviousPositions[i][j] = new Cell();
                }
            }
            return memorisedPreviousPositions;
        }

        @Test
        @DisplayName("If movables can move (Grid.canMove), they should. Unmovables should stay.")
        public void moveMovablesTest() {
            Cell[][] memorisedPreviousPositions = initialiseTestAttributes();

            // Given
            for (var entityList : exampleEntities.getEntitiesMap().values()) {
                for (var entity : entityList) {
                    // Memorise previous positions
                    memorisedPreviousPositions[entity.getPosition().x()][entity.getPosition().y()].list.add(entity);
                }
            }

            // When
            // Move movables
            Direction exampleDoctorsMove = Direction.SOUTH;
            engine.moveMovables(exampleDoctorsMove);

            // Then
            /* All the movables that could have changed their position should have,
            all the unmovables should have stayed at their previous positions */
            grid.getEntitiesMap().values()
                    .stream()
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

