package domain;

import api.solution.ProblemFactCollectionProperty;
import java.util.Objects;

public class Customer implements ProblemFactCollectionProperty, Cloneable {
    private final long id;
    private final Location location;
    private final int demand;

    public Customer(long id, Location location, int demand) {
        validateInput(location, demand);
        this.id = id;
        this.location = location;
        this.demand = demand;
    }

    private void validateInput(Location location, int demand) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }
        if (demand < 0) {
            throw new IllegalArgumentException("Demand cannot be negative");
        }
    }

    public long getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public int getDemand() {
        return demand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return id == customer.id &&
               demand == customer.demand &&
               Objects.equals(location, customer.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, location, demand);
    }

    @Override
    public String toString() {
        return String.format("Customer{id=%d, location=%s, demand=%d}", id, location, demand);
    }

    @Override
    public Customer clone() {
        try {
            Customer clone = (Customer) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning not supported", e);
        }
    }
}
