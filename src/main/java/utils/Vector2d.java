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

    public double distanceBetween(Vector2d other) {
        return Math.sqrt(Math.pow(this.x-other.x, 2) + Math.pow(this.y-other.y, 2));
    }

    public double ownLength() {
        return distanceBetween(new Vector2d(0, 0));
    }

    public Vector2d getSelfOpposite() { return new Vector2d(-1 * x, -1 * y); }

    public int dotProduct(Vector2d other) {
        return this.x * other.x + this.y * other.y;
    }

    public double getCosinusBetween(Vector2d other) throws Exception {
        double result = dotProduct(other) / (ownLength() * other.ownLength());
        if (result < -1 || result > 1) {
            throw new Exception("[Vector2d] [getCosinusBetween] ERROR: cosinus value out of range");
        }
        return result;
    }

    public double getArccosinusInDegrees(Vector2d other) {
        try {
            return Math.toDegrees(Math.acos(getCosinusBetween(other)));
        }
        catch(Exception e) {
            return 0;
        }
    }

    public double getConvexAngleBetween(Vector2d other) {
        return Math.round(getArccosinusInDegrees(other) * 100.0) / 100.0;
    }

    public double getConcaveAngleBetween(Vector2d other) {
        return 360.0 - getConvexAngleBetween(other);
    }

}
