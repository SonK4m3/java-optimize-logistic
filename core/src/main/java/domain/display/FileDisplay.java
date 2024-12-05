package domain.display;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import domain.Customer;
import domain.Depot;
import domain.Vehicle;

public class FileDisplay implements Display {
    private final String FOLDER_PATH = "core/src/main/java/logs/";
    private String filePath;

    public FileDisplay(String filePath, boolean overwrite) {
        this.filePath = filePath;
        if (overwrite) {
            clearFile();
        }
    }

    private void clearFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FOLDER_PATH + filePath))) {
            // Clear the file by writing nothing
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedWriter getWriter() throws IOException {
        return new BufferedWriter(new FileWriter(FOLDER_PATH + filePath, true));
    }

    @Override
    public void displayCustomers(List<Customer> customers) {
        try (BufferedWriter writer = getWriter()) {
            for (Customer customer : customers) {
                writer.write("Customer ID: " + customer.getId() + "\n");
                writer.write(
                        "Location: (" + customer.getLocation().getX() + ", " + customer.getLocation().getY() + ")\n");
                writer.write("Demand: " + customer.getDemand() + "\n");
                writer.write("--------------------\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void displayVehicles(List<Vehicle> vehicles) {
        try (BufferedWriter writer = getWriter()) {
            for (Vehicle vehicle : vehicles) {
                writer.write("Vehicle ID: " + vehicle.getId() + "\n");
                writer.write("Capacity: " + vehicle.getCapacity() + "\n");
                writer.write("--------------------\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void displayLocations(List<Depot> depots, List<Customer> customers, List<Vehicle> vehicles) {
        try (BufferedWriter writer = getWriter()) {
            writer.write("--------------------\n");
            for (Depot depot : depots) {
                writer.write("Depot ID: " + depot.getId() + " at (" + depot.getLocation().getX() + ", "
                        + depot.getLocation().getY() + ")\n");
            }
            writer.write("--------------------\n");
            for (Customer customer : customers) {
                writer.write("Customer ID: " + customer.getId() + " at (" + customer.getLocation().getX() + ", "
                        + customer.getLocation().getY() + ")\n");
            }
            writer.write("--------------------\n");
            for (Vehicle vehicle : vehicles) {
                writer.write("Vehicle ID: " + vehicle.getId() + " with capacity: " + vehicle.getCapacity() + "\n");
            }
            writer.write("--------------------\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void displayMessage(String message) {
        try (BufferedWriter writer = getWriter()) {
            writer.write(message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
