package domain;

import domain.geo.EuclideanDistanceCalculator;

public class Location {
    int id;
    double x, y;

    public Location(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getDistanceTo(Location to) {
        return new EuclideanDistanceCalculator().calculateDistance(this, to);
    }
}
