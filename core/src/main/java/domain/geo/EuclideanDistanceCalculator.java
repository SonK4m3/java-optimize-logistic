package domain.geo;

import domain.Location;

import static java.lang.Math.sqrt;

public class EuclideanDistanceCalculator implements DistanceCalculator {
    public static final long METERS_PER_DEGREE = 111_000;

    @Override
    public double calculateDistance(Location from, Location to) {
        if (from.equals(to)) {
            return 0L;
        }
        double latitudeDiff = to.getX() - from.getX();
        double longitudeDiff = to.getY() - from.getY();
        return (sqrt(latitudeDiff * latitudeDiff + longitudeDiff * longitudeDiff));
    }
}
