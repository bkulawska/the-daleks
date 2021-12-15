package utils;

public class GameComputations {

    public GameComputations() { }

    public static boolean valueIsBetween(double x, double lower, double upper) {
        return lower <= x && x < upper;
    }

    public static Vector2d getTranslationVector(Vector2d from, Vector2d to) {
        return new Vector2d(to.x() - from.x(), to.y() - from.y());
    }

    public static double getConvexAngleWithVerticalAxis(Vector2d vec) {
        Vector2d verticalAxisVector = new Vector2d(0, 1);
        return vec.getConvexAngleBetween(verticalAxisVector);
    }

    public static double getRelativeAngleWithVerticalAxis(Vector2d translationVectorToDoctor) {
        Vector2d opposite = translationVectorToDoctor.getSelfOpposite();
        double angle = getConvexAngleWithVerticalAxis(opposite);
        if (opposite.x() > 0) {
            return (double) (360 - angle);
        }
        return angle;
    }

    public static Direction getAngleCorrespondingDirection(double angle) {
        double[] lowerBounds = { 0.0, 22.5, 67.5, 112.5, 157.5, 202.5, 247.5, 292.5, 337.5 };
        double[] upperBounds = { 22.5, 67.5, 112.5, 157.5, 202.5, 247.5, 292.5, 337.5, 360.0 };
        Direction[] dirs = {
                Direction.NORTH,
                Direction.NORTH_EAST,
                Direction.EAST,
                Direction.SOUTH_EAST,
                Direction.SOUTH,
                Direction.SOUTH_WEST,
                Direction.WEST,
                Direction.NORTH_WEST,
                Direction.NORTH
        };

        for (int i = 0; i < lowerBounds.length; i++) {
            if (valueIsBetween(angle, lowerBounds[i], upperBounds[i])) {
                return dirs[i];
            }
        }
        return null;
    }

    public static Direction getDalekDoctorDirection(Vector2d dalekPosition, Vector2d doctorPosition) {
        Vector2d translationVectorToDoctor = getTranslationVector(dalekPosition, doctorPosition);
        double relativeAngle = getRelativeAngleWithVerticalAxis(translationVectorToDoctor);
        return getAngleCorrespondingDirection(relativeAngle);
    }
}
