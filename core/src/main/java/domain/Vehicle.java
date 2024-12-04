package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Vehicle implements Cloneable {
    private final long id;
    private int capacity;
    private Depot depot;
    private List<Customer> customerList;

    public Vehicle(long id, int capacity) {
        this.id = id;
        this.capacity = capacity;
        this.depot = null;
        this.customerList = new ArrayList<>();
    }

    public Vehicle(long id, int capacity, Depot depot, List<Customer> customerList) {
        this.id = id;
        this.capacity = capacity;
        this.depot = depot;
        this.customerList = customerList;
    }

    public long getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Depot getDepot() {
        return depot;
    }

    public void setDepot(Depot depot) {
        this.depot = depot;
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void addCustomer(Customer customer) {
        if (canAddCustomer(customer)) {
            this.customerList.add(customer);
        } else {
            throw new IllegalArgumentException("Cannot add customer. Exceeds vehicle capacity.");
        }
    }

    public int getRemainingCapacity() {
        return capacity - customerList.stream().mapToInt(Customer::getDemand).sum();
    }

    public double getTotalDistanceMeters() {
        if (this.customerList.isEmpty() || depot == null) {
            return 0;
        }

        Location previousLocation = depot.getLocation();
        double totalDistance = 0;

        for (Customer customer : customerList) {
            Location currentLocation = customer.getLocation();
            totalDistance += previousLocation.getDistanceTo(currentLocation);
            previousLocation = currentLocation;
        }

        totalDistance += previousLocation.getDistanceTo(depot.getLocation());

        return totalDistance;
    }

    @Override
    public String toString() {
        return String.format("Vehicle{id=%d, capacity=%d, customersCount=%d}", id, capacity, customerList.size());
    }

    @Override
    public Vehicle clone() {
        try {
            Vehicle clone = (Vehicle) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning not supported", e);
        }
    }

    public boolean canAddCustomer(Customer customer) {
        return getRemainingCapacity() >= customer.getDemand();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Vehicle))
            return false;
        Vehicle vehicle = (Vehicle) o;
        return id == vehicle.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
