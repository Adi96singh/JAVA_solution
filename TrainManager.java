import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/**
 * TrainManager class to manage train operations
 */
public class TrainManager {
    private HashMap<String, Train> trains;
    
    /**
     * Constructor
     */
    public TrainManager() {
        this.trains = new HashMap<>();
        initializeDefaultTrains();
    }
    
    /**
     * Initialize default trains for the system
     */
    private void initializeDefaultTrains() {
        // Add some default trains
        addTrain("12345", "Rajdhani Express");
        addTrain("23456", "Shatabdi Express");
        addTrain("34567", "Duronto Express");
        addTrain("45678", "Garib Rath");
        addTrain("56789", "Jan Shatabdi");
        
        // Customize seat configuration for some trains
        Train rajdhani = trains.get("12345");
        rajdhani.setSeatsForClass(Train.AC_CLASS, 30);
        rajdhani.setSeatsForClass(Train.SLEEPER_CLASS, 60);
        
        Train shatabdi = trains.get("23456");
        shatabdi.setSeatsForClass(Train.AC_CLASS, 40);
        shatabdi.setSeatsForClass(Train.SLEEPER_CLASS, 40);
    }
    
    /**
     * Add a new train to the system
     * @param trainNumber Unique train number
     * @param trainName Name of the train
     * @return true if added successfully, false if train already exists
     */
    public boolean addTrain(String trainNumber, String trainName) {
        if (trains.containsKey(trainNumber)) {
            return false;
        }
        trains.put(trainNumber, new Train(trainNumber, trainName));
        return true;
    }
    
    /**
     * Get a train by train number
     * @param trainNumber Train number to search
     * @return Train object
     * @throws TrainNotFoundException if train not found
     */
    public Train getTrain(String trainNumber) throws TrainNotFoundException {
        Train train = trains.get(trainNumber);
        if (train == null) {
            throw new TrainNotFoundException(trainNumber);
        }
        return train;
    }
    
    /**
     * Check if train exists
     * @param trainNumber Train number to check
     * @return true if exists, false otherwise
     */
    public boolean trainExists(String trainNumber) {
        return trains.containsKey(trainNumber);
    }
    
    /**
     * Get all trains
     * @return List of all trains
     */
    public List<Train> getAllTrains() {
        return new ArrayList<>(trains.values());
    }
    
    /**
     * Get seat availability for a specific train and class
     * @param trainNumber Train number
     * @param classType Class type (AC/Sleeper)
     * @return Availability information
     * @throws TrainNotFoundException if train not found
     * @throws InvalidClassTypeException if invalid class type
     */
    public SeatAvailability getSeatAvailability(String trainNumber, String classType) 
            throws TrainNotFoundException, InvalidClassTypeException {
        
        validateClassType(classType);
        Train train = getTrain(trainNumber);
        
        return new SeatAvailability(
            train.getTotalSeats(classType),
            train.getBookedSeats(classType),
            train.getAvailableSeats(classType),
            train.getWaitlistCount(classType)
        );
    }
    
    /**
     * Validate class type
     * @param classType Class type to validate
     * @throws InvalidClassTypeException if invalid
     */
    public void validateClassType(String classType) throws InvalidClassTypeException {
        if (!classType.equals(Train.AC_CLASS) && !classType.equals(Train.SLEEPER_CLASS)) {
            throw new InvalidClassTypeException(classType);
        }
    }
    
    /**
     * Display all trains with their details
     */
    public void displayAllTrains() {
        System.out.println("\n=== ALL TRAINS ===");
        if (trains.isEmpty()) {
            System.out.println("No trains available.");
            return;
        }
        
        System.out.printf("%-10s %-20s %-15s %-15s\n", 
                         "Train No.", "Train Name", "AC Seats", "Sleeper Seats");
        System.out.println("-".repeat(70));
        
        for (Train train : trains.values()) {
            System.out.printf("%-10s %-20s %-15s %-15s\n",
                train.getTrainNumber(),
                train.getTrainName(),
                train.getBookedSeats(Train.AC_CLASS) + "/" + train.getTotalSeats(Train.AC_CLASS),
                train.getBookedSeats(Train.SLEEPER_CLASS) + "/" + train.getTotalSeats(Train.SLEEPER_CLASS));
        }
        System.out.println("=".repeat(70));
    }
    
    /**
     * Get trains HashMap for persistence
     * @return HashMap of trains
     */
    public HashMap<String, Train> getTrainsMap() {
        return trains;
    }
    
    /**
     * Set trains HashMap from loaded data
     * @param trains HashMap of trains
     */
    public void setTrainsMap(HashMap<String, Train> trains) {
        this.trains = trains;
    }
    
    /**
     * Inner class to represent seat availability information
     */
    public static class SeatAvailability {
        private int totalSeats;
        private int bookedSeats;
        private int availableSeats;
        private int waitlistCount;
        
        public SeatAvailability(int totalSeats, int bookedSeats, 
                              int availableSeats, int waitlistCount) {
            this.totalSeats = totalSeats;
            this.bookedSeats = bookedSeats;
            this.availableSeats = availableSeats;
            this.waitlistCount = waitlistCount;
        }
        
        // Getters
        public int getTotalSeats() { return totalSeats; }
        public int getBookedSeats() { return bookedSeats; }
        public int getAvailableSeats() { return availableSeats; }
        public int getWaitlistCount() { return waitlistCount; }
        
        @Override
        public String toString() {
            return String.format(
                "Total: %d, Booked: %d, Available: %d, Waitlist: %d",
                totalSeats, bookedSeats, availableSeats, waitlistCount
            );
        }
    }
}
