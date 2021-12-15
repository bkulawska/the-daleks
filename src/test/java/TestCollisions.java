import javafx.util.Pair;
import model.Grid;
import model.collisions.CollisionDetector;
import model.collisions.CollisionResolver;
import model.entity.Entity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.Vector2d;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
        @Mock
        private Grid grid;

        @Test
        @DisplayName("Check whether collisions have been resolved by comparing flags from CollisionResolver")
        public void collisionResolverTest() {
            // Given
            Map<Vector2d, List<Entity>> entitiesMap = exampleEntities.getEntitiesMap();

            // When
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
            Map<Pair<Entity, Entity>, Boolean> updatedCollisionFlags = collisionResolver.getCollisionFlags();
            for (Pair<Entity, Entity> collision : detectedCollisions) {
                assertTrue(updatedCollisionFlags.get(collision));
            }
        }
    }
}
