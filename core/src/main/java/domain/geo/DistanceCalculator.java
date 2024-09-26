package domain.geo;

import domain.Location;

/**
 * Interface for calculating distances between locations.
 * Implementations of this interface should provide specific distance calculation algorithms.
 */
public interface DistanceCalculator {

    /**
     * Calculates the distance between two locations.
     *
     * @param from The starting location
     * @param to The destination location
     * @return The calculated distance between the two locations in meters
     * @throws IllegalArgumentException if either location is null
     */
    double calculateDistance(Location from, Location to);

    /**
     * Calculates the distances from one location to multiple destinations.
     * This method can be optimized in implementations for bulk calculations.
     *
     * @param from The starting location
     * @param to An array of destination locations
     * @return An array of distances corresponding to each destination
     * @throws IllegalArgumentException if the starting location or the destination array is null
     */
    default double[] calculateDistances(Location from, Location[] to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Locations cannot be null");
        }
        double[] distances = new double[to.length];
        for (int i = 0; i < to.length; i++) {
            distances[i] = calculateDistance(from, to[i]);
        }
        return distances;
    }
}
