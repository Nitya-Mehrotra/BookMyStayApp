import java.io.*;
import java.util.*;

// Inventory (Serializable)
class RoomInventory implements Serializable {
    Map<String, Integer> availability = new LinkedHashMap<>();

    public RoomInventory() {
        availability.put("Single", 5);
        availability.put("Double", 3);
        availability.put("Suite", 2);
    }

    public void display() {
        System.out.println("\nCurrent Inventory:");
        for (String type : availability.keySet()) {
            System.out.println(type + ": " + availability.get(type));
        }
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "inventory.ser";

    // Save inventory
    public static void save(RoomInventory inventory) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(inventory);
            System.out.println("\nInventory saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving inventory.");
        }
    }

    // Load inventory
    public static RoomInventory load() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            return (RoomInventory) ois.readObject();

        } catch (Exception e) {
            return null;
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("System Recovery");

        RoomInventory inventory = PersistenceService.load();

        if (inventory == null) {
            System.out.println("No valid inventory data found. Starting fresh.");

            // Create fresh inventory
            inventory = new RoomInventory();
        }

        // Display inventory
        inventory.display();

        // Save inventory
        PersistenceService.save(inventory);
    }
}