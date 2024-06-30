package domain.geo;

import domain.Location;

public interface DistanceCalculator {
    double calculateDistance(Location from, Location to);
}
