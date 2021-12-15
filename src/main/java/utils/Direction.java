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
}
