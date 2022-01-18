import javafx.util.Pair;
import model.Grid;
import model.collisions.CollisionDetector;
import model.collisions.CollisionResolver;
import model.entity.Entity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.Vector2d;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@DisplayName("Collision detection and resolving tests")
public class TestCollisions {
    private final ExampleEntities exampleEntities = new ExampleEntities();

    @Test
    @DisplayName("Compare expected collisions from ExampleEntities with those detected by CollisionDetector")
    void collisionDetectorTest() {
        // Given
        Map<Vector2d, List<Entity>> entitiesMap = exampleEntities.getEntitiesMap();
        List<Pair<Entity, Entity>> expectedCollisions = exampleEntities.getExpectedCollisions();

        // When
        CollisionDetector collisionDetector = new CollisionDetector();
        List<Pair<Entity, Entity>> detectedCollisions = collisionDetector.detect(entitiesMap);

        // Then
        for (var pair : detectedCollisions) {
            Pair<Entity, Entity> pairOrder1 = new Pair<>(pair.getKey(), pair.getValue());
            Pair<Entity, Entity> pairOrder2 = new Pair<>(pair.getValue(), pair.getKey());
            assertTrue(expectedCollisions.contains(pairOrder1) || expectedCollisions.contains(pairOrder2));
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    public class collisionResolverTestMock {
        @Test
        @DisplayName("Check whether collisions have been resolved by comparing flags from CollisionResolver")
        public void collisionResolverTest() {
            // Given
            Map<Vector2d, List<Entity>> entitiesMap = exampleEntities.getEntitiesMap();
            Grid grid = Mockito.mock(Grid.class);

            // When
            when(grid.getDoctor()).thenReturn(exampleEntities.getDoctor());

            CollisionDetector collisionDetector = new CollisionDetector();
            List<Pair<Entity, Entity>> detectedCollisions = collisionDetector.detect(entitiesMap);

            Map<Pair<Entity, Entity>, Boolean> collisionFlags = new HashMap<>();
            for (Pair<Entity, Entity> collision : detectedCollisions) {
                collisionFlags.put(collision, false);
            }

            CollisionResolver collisionResolver = new CollisionResolver(grid);
            collisionResolver.setCollisionFlags(collisionFlags);
            collisionResolver.resolve(detectedCollisions);

            // Then
            for (Pair<Entity, Entity> collision : detectedCollisions) {
                var isCollisionSolved =
                        collisionFlags.get(collision) || collisionFlags.get(reverseCollision(collision));
                assertTrue(isCollisionSolved);
            }
        }

        private Pair<Entity, Entity> reverseCollision(Pair<Entity, Entity> collision) {
            return new Pair<>(collision.getValue(), collision.getKey());
        }
    }
}
