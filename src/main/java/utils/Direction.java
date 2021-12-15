package utils;

import java.util.Random;

public enum Direction {

    NORTH (new Vector2d(0, -1)),
    NORTH_EAST (new Vector2d(1, -1)),
    EAST (new Vector2d(1, 0)),
    SOUTH_EAST (new Vector2d(1, 1)),
    SOUTH (new Vector2d(0, 1)),
    SOUTH_WEST (new Vector2d(-1, 1)),
    WEST (new Vector2d(-1, 0)),
    NORTH_WEST (new Vector2d(-1, -1));

    private final Vector2d vector;
    public static int N_DIRECTIONS = 8;

    Direction(Vector2d vector) {
        this.vector = vector;
    }

    public Vector2d getVector() {
        return this.vector;
    }

    public static Direction getDirection(Vector2d vec) throws Exception {
        int x = vec.x();
        int y = vec.y();
        int[] xDirectionsInOrder = new int[]{0, 1, 1, 1, 0, -1, -1, -1};
        int[] yDirectionsInOrder = new int[]{-1, -1, 0, 1, 1, 1, 0, -1};
        for(int i = 0; i < 8; i++){
            if(x == xDirectionsInOrder[i] && y == yDirectionsInOrder[i]){
                return values()[i];
            }
        }
        throw new Exception("[Direction.getDirection] ERROR: no direction matches this vector: " + x + " " + y);
    }

    public static Direction getRandomDirection() { return values()[new Random().nextInt(8)]; }
}
