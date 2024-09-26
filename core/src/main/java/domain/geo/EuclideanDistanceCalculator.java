package domain.geo;

import domain.Location;

import static java.lang.Math.sqrt;
import static java.lang.Math.pow;

public class EuclideanDistanceCalculator implements DistanceCalculator {
    public static final long METERS_PER_DEGREE = 111_000;

    /**
     * Calculates the Euclidean distance between two locations.
     *
     * @param from The starting location
     * @param to The ending location
     * @return The distance in meters between the two locations
     */
    @Override
    public double calculateDistance(Location from, Location to) {
        // If the locations are the same, return 0
        if (from.equals(to)) {
            return 0.0;
        }

        // Calculate the differences in latitude and longitude
        double latitudeDiff = to.getX() - from.getX();
        double longitudeDiff = to.getY() - from.getY();

        // Calculate the Euclidean distance
        double distance = sqrt(pow(latitudeDiff, 2) + pow(longitudeDiff, 2));

        // Convert the distance from degrees to meters
        return distance * METERS_PER_DEGREE;
    }
}
