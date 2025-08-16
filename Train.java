import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Train class representing a railway train with seat management capabilities
 */
public class Train implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String trainNumber;
    private String trainName;
    private HashMap<String, Integer> totalSeatsPerClass;
    private HashMap<String, Integer> availableSeatsPerClass;
    private HashMap<String, Queue<Passenger>> waitlistQueuePerClass;
    
    // Default class types
    public static final String AC_CLASS = "AC";
    public static final String SLEEPER_CLASS = "Sleeper";
    
    /**
     * Constructor for Train
     * @param trainNumber Unique identifier for the train
     * @param trainName Name of the train
     */
    public Train(String trainNumber, String trainName) {
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.totalSeatsPerClass = new HashMap<>();
        this.availableSeatsPerClass = new HashMap<>();
        this.waitlistQueuePerClass = new HashMap<>();
        
        // Initialize with default seat configuration
        initializeSeats();
    }
    
    /**
     * Initialize seats for different classes
     */
    private void initializeSeats() {
        // Default configuration: AC - 20 seats, Sleeper - 50 seats
        totalSeatsPerClass.put(AC_CLASS, 20);
        totalSeatsPerClass.put(SLEEPER_CLASS, 50);
        
        availableSeatsPerClass.put(AC_CLASS, 20);
        availableSeatsPerClass.put(SLEEPER_CLASS, 50);
        
        waitlistQueuePerClass.put(AC_CLASS, new LinkedList<>());
        waitlistQueuePerClass.put(SLEEPER_CLASS, new LinkedList<>());
    }
    
    /**
     * Set custom seat configuration
     * @param classType Type of class (AC/Sleeper)
     * @param totalSeats Total number of seats for this class
     */
    public void setSeatsForClass(String classType, int totalSeats) {
        totalSeatsPerClass.put(classType, totalSeats);
        availableSeatsPerClass.put(classType, totalSeats);
        waitlistQueuePerClass.put(classType, new LinkedList<>());
    }
    
    /**
     * Book a seat in the specified class
     * @param classType Type of class
     * @return true if seat booked, false if added to waitlist
     */
    public boolean bookSeat(String classType) {
        if (!availableSeatsPerClass.containsKey(classType)) {
            return false;
        }
        
        int availableSeats = availableSeatsPerClass.get(classType);
        if (availableSeats > 0) {
            availableSeatsPerClass.put(classType, availableSeats - 1);
            return true;
        }
        return false;
    }
    
    /**
     * Cancel a seat in the specified class
     * @param classType Type of class
     * @return Passenger from waitlist if any, null otherwise
     */
    public Passenger cancelSeat(String classType) {
        if (!availableSeatsPerClass.containsKey(classType)) {
            return null;
        }
        
        int availableSeats = availableSeatsPerClass.get(classType);
        int totalSeats = totalSeatsPerClass.get(classType);
        
        if (availableSeats < totalSeats) {
            availableSeatsPerClass.put(classType, availableSeats + 1);
            
            // Check if anyone is in waitlist
            Queue<Passenger> waitlist = waitlistQueuePerClass.get(classType);
            if (!waitlist.isEmpty()) {
                Passenger waitlistedPassenger = waitlist.poll();
                availableSeatsPerClass.put(classType, availableSeats); // Keep it same as someone got confirmed
                return waitlistedPassenger;
            }
        }
        return null;
    }
    
    /**
     * Add passenger to waitlist
     * @param passenger Passenger to add to waitlist
     * @param classType Type of class
     */
    public void addToWaitlist(Passenger passenger, String classType) {
        if (waitlistQueuePerClass.containsKey(classType)) {
            waitlistQueuePerClass.get(classType).offer(passenger);
        }
    }
    
    /**
     * Get available seats for a class
     * @param classType Type of class
     * @return Number of available seats
     */
    public int getAvailableSeats(String classType) {
        return availableSeatsPerClass.getOrDefault(classType, 0);
    }
    
    /**
     * Get total seats for a class
     * @param classType Type of class
     * @return Total number of seats
     */
    public int getTotalSeats(String classType) {
        return totalSeatsPerClass.getOrDefault(classType, 0);
    }
    
    /**
     * Get booked seats for a class
     * @param classType Type of class
     * @return Number of booked seats
     */
    public int getBookedSeats(String classType) {
        int total = getTotalSeats(classType);
        int available = getAvailableSeats(classType);
        return total - available;
    }
    
    /**
     * Get waitlist count for a class
     * @param classType Type of class
     * @return Number of passengers in waitlist
     */
    public int getWaitlistCount(String classType) {
        Queue<Passenger> waitlist = waitlistQueuePerClass.get(classType);
        return waitlist != null ? waitlist.size() : 0;
    }
    
    // Getters and Setters
    public String getTrainNumber() {
        return trainNumber;
    }
    
    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }
    
    public String getTrainName() {
        return trainName;
    }
    
    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }
    
    public HashMap<String, Integer> getTotalSeatsPerClass() {
        return new HashMap<>(totalSeatsPerClass);
    }
    
    public HashMap<String, Integer> getAvailableSeatsPerClass() {
        return new HashMap<>(availableSeatsPerClass);
    }
    
    @Override
    public String toString() {
        return String.format("Train{Number='%s', Name='%s', AC Seats: %d/%d, Sleeper Seats: %d/%d}", 
                           trainNumber, trainName,
                           getBookedSeats(AC_CLASS), getTotalSeats(AC_CLASS),
                           getBookedSeats(SLEEPER_CLASS), getTotalSeats(SLEEPER_CLASS));
    }
}
