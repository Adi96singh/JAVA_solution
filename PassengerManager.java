import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * PassengerManager class to manage passenger operations and PNR generation
 */
public class PassengerManager {
    private HashMap<String, Passenger> passengers;
    private Random random;
    private static final String PNR_PREFIX = "PNR";
    
    /**
     * Constructor
     */
    public PassengerManager() {
        this.passengers = new HashMap<>();
        this.random = new Random();
    }
    
    /**
     * Generate unique PNR
     * @return Unique PNR string
     */
    public String generatePNR() {
        String pnr;
        do {
            // Generate 7-digit random number for PNR
            int randomNumber = 1000000 + random.nextInt(9000000);
            pnr = PNR_PREFIX + randomNumber;
        } while (passengers.containsKey(pnr));
        
        return pnr;
    }
    
    /**
     * Add a new passenger
     * @param passenger Passenger to add
     * @return true if added successfully
     */
    public boolean addPassenger(Passenger passenger) {
        if (passengers.containsKey(passenger.getPnr())) {
            return false;
        }
        passengers.put(passenger.getPnr(), passenger);
        return true;
    }
    
    /**
     * Get passenger by PNR
     * @param pnr PNR to search
     * @return Passenger object
     * @throws PassengerNotFoundException if passenger not found
     */
    public Passenger getPassenger(String pnr) throws PassengerNotFoundException {
        Passenger passenger = passengers.get(pnr);
        if (passenger == null) {
            throw new PassengerNotFoundException(pnr);
        }
        return passenger;
    }
    
    /**
     * Check if passenger exists
     * @param pnr PNR to check
     * @return true if exists, false otherwise
     */
    public boolean passengerExists(String pnr) {
        return passengers.containsKey(pnr);
    }
    
    /**
     * Search passengers by name
     * @param name Name to search (case-insensitive)
     * @return List of passengers with matching name
     */
    public List<Passenger> searchByName(String name) {
        List<Passenger> matchingPassengers = new ArrayList<>();
        
        for (Passenger passenger : passengers.values()) {
            if (passenger.getName().toLowerCase().contains(name.toLowerCase()) &&
                !passenger.isCancelled()) {
                matchingPassengers.add(passenger);
            }
        }
        
        return matchingPassengers;
    }
    
    /**
     * Get all active passengers (not cancelled)
     * @return List of all active passengers
     */
    public List<Passenger> getAllActivePassengers() {
        List<Passenger> activePassengers = new ArrayList<>();
        
        for (Passenger passenger : passengers.values()) {
            if (!passenger.isCancelled()) {
                activePassengers.add(passenger);
            }
        }
        
        return activePassengers;
    }
    
    /**
     * Get all passengers including cancelled ones
     * @return List of all passengers
     */
    public List<Passenger> getAllPassengers() {
        return new ArrayList<>(passengers.values());
    }
    
    /**
     * Remove passenger (for cancellation)
     * @param pnr PNR of passenger to remove
     * @return Removed passenger or null if not found
     */
    public Passenger removePassenger(String pnr) {
        return passengers.remove(pnr);
    }
    
    /**
     * Update passenger information
     * @param passenger Updated passenger object
     */
    public void updatePassenger(Passenger passenger) {
        passengers.put(passenger.getPnr(), passenger);
    }
    
    /**
     * Display passenger booking details
     * @param pnr PNR to display
     * @throws PassengerNotFoundException if passenger not found
     */
    public void displayPassengerDetails(String pnr) throws PassengerNotFoundException {
        Passenger passenger = getPassenger(pnr);
        System.out.println(passenger.getBookingDetails());
    }
    
    /**
     * Display all bookings with summary
     */
    public void displayAllBookings() {
        System.out.println("\n=== ALL BOOKINGS ===");
        
        if (passengers.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }
        
        System.out.printf("%-12s %-20s %-10s %-8s %-12s %-10s %-12s %-10s\n",
                         "PNR", "Name", "Train", "Class", "Seat", "Status", "Age", "Gender");
        System.out.println("-".repeat(100));
        
        int confirmedCount = 0;
        int waitlistedCount = 0;
        int cancelledCount = 0;
        
        for (Passenger passenger : passengers.values()) {
            System.out.printf("%-12s %-20s %-10s %-8s %-12s %-10s %-12d %-10s\n",
                passenger.getPnr(),
                passenger.getName().length() > 20 ? passenger.getName().substring(0, 17) + "..." : passenger.getName(),
                passenger.getTrainNumber(),
                passenger.getClassType(),
                passenger.getSeatNumber() != null ? passenger.getSeatNumber() : "N/A",
                passenger.getStatus(),
                passenger.getAge(),
                passenger.getGender());
            
            // Count status
            switch (passenger.getStatus()) {
                case CONFIRMED:
                    confirmedCount++;
                    break;
                case WAITLISTED:
                    waitlistedCount++;
                    break;
                case CANCELLED:
                    cancelledCount++;
                    break;
            }
        }
        
        System.out.println("=".repeat(100));
        System.out.println("Summary: Total: " + passengers.size() + 
                          ", Confirmed: " + confirmedCount + 
                          ", Waitlisted: " + waitlistedCount + 
                          ", Cancelled: " + cancelledCount);
    }
    
    /**
     * Get booking statistics
     * @return Booking statistics as a formatted string
     */
    public String getBookingStatistics() {
        int total = passengers.size();
        int confirmed = 0;
        int waitlisted = 0;
        int cancelled = 0;
        
        for (Passenger passenger : passengers.values()) {
            switch (passenger.getStatus()) {
                case CONFIRMED:
                    confirmed++;
                    break;
                case WAITLISTED:
                    waitlisted++;
                    break;
                case CANCELLED:
                    cancelled++;
                    break;
            }
        }
        
        return String.format("Total Bookings: %d | Confirmed: %d | Waitlisted: %d | Cancelled: %d",
                           total, confirmed, waitlisted, cancelled);
    }
    
    /**
     * Get passengers HashMap for persistence
     * @return HashMap of passengers
     */
    public HashMap<String, Passenger> getPassengersMap() {
        return passengers;
    }
    
    /**
     * Set passengers HashMap from loaded data
     * @param passengers HashMap of passengers
     */
    public void setPassengersMap(HashMap<String, Passenger> passengers) {
        this.passengers = passengers;
    }
}
