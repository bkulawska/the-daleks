import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import utils.Direction;
import utils.Vector2d;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Check Vector2d and Directions work")
public class TestUtils {

    @Nested
    public class DirectionTest {
        @Test
        public void testGetVector() {
            // Given, when, then
            assertEquals(new Vector2d(1, 0), Direction.EAST.getVector());
            assertEquals(new Vector2d(-1, -1), Direction.NORTH_WEST.getVector());
        }
    }

    @Nested
    public class Vector2dTest {

        public double roundDecimals(double d, int digits) {
            double scale = Math.pow(10, digits);
            return Math.round(d * scale) / scale;
        }

        @Test
        public void testPrecedesAndFollows() {
            // Given, when
            Vector2d vec1 = new Vector2d(4, 5);
            Vector2d vec2 = new Vector2d(10, 6);
            Vector2d vec3 = new Vector2d(10, 16);
            Vector2d vec4 = new Vector2d(5, 17);
            // Then
            assertTrue(vec1.precedes(vec4));
            assertFalse(vec2.precedes(vec1));
            assertTrue(vec2.precedes(vec3));
            assertTrue(vec2.follows(vec1));
            assertFalse(vec1.follows(vec2));
            assertFalse(vec4.follows(vec3));
        }

        @Test
        public void testDistanceBetween(){
            assertEquals(5, new Vector2d(4, 0).distanceBetween(new Vector2d(0,3)));
            assertEquals(Math.sqrt(32), new Vector2d(4, 0).distanceBetween(new Vector2d(0,4)));
            assertEquals(Math.sqrt(5), new Vector2d(0, 4).distanceBetween(new Vector2d(1,2)));
        }

        @Test
        public void testOwnLength() {
            assertEquals(Math.sqrt(73), new Vector2d(3, 8).ownLength());
            assertEquals(Math.sqrt(73), new Vector2d(8, 3).ownLength());
            assertEquals(Math.sqrt(5), new Vector2d(1, 2).ownLength());
        }

        @Test
        public void testGetSelfOpposite() {
            assertEquals(new Vector2d(-1, -1), new Vector2d(1, 1).getSelfOpposite());
            assertEquals(new Vector2d(10, -13), new Vector2d(-10, 13).getSelfOpposite());
            assertEquals(new Vector2d(-1, 0), new Vector2d(1, 0).getSelfOpposite());
        }

        @Test
        public void testDotProduct() {
            assertEquals(342, new Vector2d(10, 12).dotProduct(new Vector2d(15, 16)));
            assertEquals(264, new Vector2d(12, 0).dotProduct(new Vector2d(22, 1)));
        }

        @Test
        public void testGetCosinusBetween() throws Exception {
            assertEquals(0.96, new Vector2d(3, 4).getCosinusBetween(new Vector2d(4, 3)));
            assertEquals(0.80,
                    roundDecimals(new Vector2d(5, 5).getCosinusBetween(new Vector2d(7, 1)), 2)
            );
        }

        @Test
        public void getConvexAngleBetween() {
            assertEquals(26.57, roundDecimals(
                    new Vector2d(4, 3).getConvexAngleBetween(new Vector2d(3, 6)), 2
            ));
            assertEquals(35.18, roundDecimals(
                    new Vector2d(14, 19).getConvexAngleBetween(new Vector2d(3, 1)), 2
            ));
            assertEquals(180.0, roundDecimals(
                    new Vector2d(0, 1).getConvexAngleBetween(new Vector2d(0, -1)), 1
            ));
            assertEquals(180.0, roundDecimals(
                    new Vector2d(0, 3).getConvexAngleBetween(new Vector2d(0, -3)), 1
            ));
            assertEquals(178.1, roundDecimals(
                    new Vector2d(0, 30).getConvexAngleBetween(new Vector2d(-1, -30)), 1
            ));
        }

        @Test
        public void getConcaveAngleBetween() {
            assertEquals(181.9, roundDecimals(
                    new Vector2d(0, 30).getConcaveAngleBetween(new Vector2d(-1, -30)), 1
            ));
        }
    }
}
