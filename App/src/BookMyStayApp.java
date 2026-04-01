import java.util.*;

// Reservation
class Reservation {
    String guestName;
    String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// Room Inventory
class RoomInventory {

    private Map<String, Integer> availability = new HashMap<>();
    private Map<String, Integer> roomCounter = new HashMap<>();

    public RoomInventory() {
        availability.put("Single", 5);
        availability.put("Double", 3);
        availability.put("Suite", 2);

        roomCounter.put("Single", 1);
        roomCounter.put("Double", 1);
        roomCounter.put("Suite", 1);
    }

    // 🔒 synchronized for thread safety
    public synchronized String allocateRoom(String roomType) {

        int available = availability.getOrDefault(roomType, 0);

        if (available > 0) {
            availability.put(roomType, available - 1);

            int roomNumber = roomCounter.get(roomType);
            roomCounter.put(roomType, roomNumber + 1);

            return roomType + "-" + roomNumber;
        }

        return null;
    }

    public void display() {
        System.out.println("\nRemaining Inventory:");
        for (String type : availability.keySet()) {
            System.out.println(type + ": " + availability.get(type));
        }
    }
}

// Booking Processor
class BookingProcessor implements Runnable {

    private Queue<Reservation> queue;
    private RoomInventory inventory;

    public BookingProcessor(Queue<Reservation> queue, RoomInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {

        while (true) {

            Reservation request;

            synchronized (queue) {
                if (queue.isEmpty()) break;
                request = queue.poll();
            }

            String roomId = inventory.allocateRoom(request.roomType);

            if (roomId != null) {
                System.out.println("Booking confirmed for Guest: "
                        + request.guestName + ", Room ID: " + roomId);
            }
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Concurrent Booking Simulation");

        Queue<Reservation> queue = new LinkedList<>();

        // Same as screenshot
        queue.offer(new Reservation("Abhi", "Single"));
        queue.offer(new Reservation("Vanmathi", "Double"));
        queue.offer(new Reservation("Kural", "Suite"));
        queue.offer(new Reservation("Subha", "Single"));

        RoomInventory inventory = new RoomInventory();

        Thread t1 = new Thread(new BookingProcessor(queue, inventory));
        Thread t2 = new Thread(new BookingProcessor(queue, inventory));

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        inventory.display();
    }
}