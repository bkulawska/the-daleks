import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.Direction;
import utils.Vector2d;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Check Vector2d and Directions work")
public class JUnitTestUtils {
    @Test
    public void Vector2dTest(){
        // Given, when
        Vector2d vec1 = new Vector2d(4,5);
        Vector2d vec2 = new Vector2d(10,6);
        Vector2d vec3 = new Vector2d(10,16);
        Vector2d vec4 = new Vector2d(5,17);
        // Then
        assertTrue(vec1.precedes(vec4));
        assertFalse(vec2.precedes(vec1));
        assertTrue(vec2.precedes(vec3));
        assertTrue(vec2.follows(vec1));
        assertFalse(vec1.follows(vec2));
        assertFalse(vec4.follows(vec3));
    }

    @Test
    public void DirectionTest(){
        // Given, when, then
        assertEquals(new Vector2d(1,0), Direction.EAST.getVector());
        assertEquals(new Vector2d(-1,-1), Direction.NORTH_WEST.getVector());
    }
}
