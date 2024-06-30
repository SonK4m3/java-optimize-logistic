package domain;

import java.util.ArrayList;
import java.util.List;

public class Vehicle implements Cloneable {
    private long id;
    private int capacity;
    private Depot depot;
    private List<Customer> customerList;

    public Vehicle() {

    }

    public Vehicle(long id, int capacity) {
        this.id = id;
        this.capacity = capacity;
        this.customerList = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }

    public void addCustomer(Customer customer) {
        this.customerList.add(customer);
    }

    public int getRemainingCapacity() {
        int totalDemand = 0;
        for (Customer customer : customerList) {
            totalDemand += customer.getDemand();
        }
        return capacity - totalDemand;
    }

    public double getTotalDistanceMeters() {
        if (this.customerList.isEmpty()) {
            return 0;
        }

        double totalDistance = 0;
        Location previousLocation = depot.getLocation();

        for (Customer customer : customerList) {
            totalDistance += previousLocation.getDistanceTo(customer.getLocation());
            previousLocation = customer.getLocation();
        }
        totalDistance += previousLocation.getDistanceTo(depot.getLocation());
        return totalDistance;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                '}';
    }

    @Override
    public Vehicle clone() {
        try {
            Vehicle clone = (Vehicle) super.clone();

            if (depot != null) {
                clone.depot = new Depot(depot.getId(), depot.getLocation());
            }

            clone.customerList = new ArrayList<>();
            for (Customer customer : customerList) {
                clone.customerList.add(new Customer(customer.getId(), customer.getLocation(), customer.getDemand()));
            }

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public boolean canAddCustomer(Customer customer) {
        int totalDemand = customer.getDemand();
        for (Customer c : this.customerList) {
            totalDemand += c.getDemand();
        }
        return totalDemand <= capacity;
    }
}
