import java.util.*;

// Custom Exception
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation class
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Inventory
class RoomInventory {
    private Set<String> validRooms;

    public RoomInventory() {
        validRooms = new HashSet<>();
        validRooms.add("Single");
        validRooms.add("Double");
        validRooms.add("Suite");
    }

    public boolean isValidRoomType(String roomType) {
        return validRooms.contains(roomType); // CASE-SENSITIVE
    }
}

// Validator
class BookingValidator {
    public static void validate(Reservation reservation, RoomInventory inventory)
            throws InvalidBookingException {

        if (!inventory.isValidRoomType(reservation.getRoomType())) {
            throw new InvalidBookingException("Invalid room type selected.");
        }
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        RoomInventory inventory = new RoomInventory();

        System.out.println("Booking Validation");

        System.out.print("Enter guest name: ");
        String name = sc.nextLine();

        System.out.print("Enter room type (Single/Double/Suite): ");
        String room = sc.nextLine();

        Reservation reservation = new Reservation(name, room);

        try {
            BookingValidator.validate(reservation, inventory);
            System.out.println("Booking successful!");
        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        }

        sc.close();
    }
}