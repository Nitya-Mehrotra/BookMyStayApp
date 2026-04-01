import java.util.*;

// Reservation
class Reservation {
    String reservationId;
    String guestName;
    String roomType;
    String roomId;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }
}

// Inventory
class RoomInventory {
    private Map<String, Integer> availability = new HashMap<>();

    public RoomInventory() {
        availability.put("Single", 5); // important for output (5 → 6)
        availability.put("Double", 3);
    }

    public void increaseAvailability(String roomType) {
        availability.put(roomType, availability.get(roomType) + 1);
    }

    public int getAvailability(String roomType) {
        return availability.get(roomType);
    }
}

// Booking History
class BookingHistory {
    private Map<String, Reservation> bookings = new HashMap<>();

    public void add(Reservation r) {
        bookings.put(r.reservationId, r);
    }

    public Reservation get(String id) {
        return bookings.get(id);
    }

    public void remove(String id) {
        bookings.remove(id);
    }
}

// Cancellation Service
class CancellationService {

    private RoomInventory inventory;
    private BookingHistory history;
    private Stack<String> stack = new Stack<>();

    public CancellationService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void cancel(String reservationId) {

        System.out.println("Booking Cancellation");

        Reservation r = history.get(reservationId);

        if (r == null) {
            System.out.println("Cancellation failed: Reservation not found");
            return;
        }

        // Push to stack
        stack.push(r.roomId);

        // Restore inventory
        inventory.increaseAvailability(r.roomType);

        // Remove booking
        history.remove(reservationId);

        // Output format EXACT
        System.out.println("Booking cancelled successfully. Inventory restored for room type: " + r.roomType);
        System.out.println();

        System.out.println("Rollback History (Most Recent First):");
        for (int i = stack.size() - 1; i >= 0; i--) {
            System.out.println("Released Reservation ID: " + stack.get(i));
        }

        System.out.println();
        System.out.println("Updated " + r.roomType + " Room Availability: " +
                inventory.getAvailability(r.roomType));
    }
}

// Main
public class BookMyStayApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();

        // Match expected output
        history.add(new Reservation("R101", "Amit", "Single", "Single-1"));

        CancellationService service = new CancellationService(inventory, history);

        service.cancel("R101");
    }
}