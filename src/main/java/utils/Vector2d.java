package utils;

public record Vector2d(int x, int y) {

    public boolean precedes(Vector2d other) {
        if (this.x <= other.x) {
            return this.y <= other.y;
        }
        return false;
    }

    public boolean follows(Vector2d other) {
        if (this.x >= other.x) {
            return this.y >= other.y;
        }
        return false;
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

}
