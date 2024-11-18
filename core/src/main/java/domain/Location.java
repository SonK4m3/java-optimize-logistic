package domain;

import java.util.Objects;

import domain.geo.EuclideanDistanceCalculator;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Location {
    private final int id;
    private final double x;
    private final double y;
    private static final EuclideanDistanceCalculator DISTANCE_CALCULATOR = new EuclideanDistanceCalculator();

    public Location(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDistanceTo(Location to) {
        return DISTANCE_CALCULATOR.calculateDistance(this, to);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Location))
            return false;
        Location location = (Location) o;
        return id == location.id && Double.compare(location.x, x) == 0 && Double.compare(location.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, x, y);
    }

    @Override
    public String toString() {
        return String.format("Location{id=%d, x=%.2f, y=%.2f}", id, x, y);
    }
}
