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

public class JUnitTestUtils {

    @Nested
    @DisplayName("Check Vector2d and Directions work")
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
}
