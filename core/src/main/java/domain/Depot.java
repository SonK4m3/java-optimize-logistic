package domain;

import java.util.Objects;

public class Depot {
    private final long id;
    private final Location location;

    public Depot(long id, Location location) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }
        this.id = id;
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Depot))
            return false;
        Depot depot = (Depot) o;
        return id == depot.id && Objects.equals(location, depot.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, location);
    }

    @Override
    public String toString() {
        return String.format("Depot{id=%d, location=%s}", id, location);
    }
}
