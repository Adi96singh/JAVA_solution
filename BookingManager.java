/**
 * BookingManager class to handle booking and cancellation operations
 */
public class BookingManager {
    private TrainManager trainManager;
    private PassengerManager passengerManager;
    
    /**
     * Constructor
     * @param trainManager TrainManager instance
     * @param passengerManager PassengerManager instance
     */
    public BookingManager(TrainManager trainManager, PassengerManager passengerManager) {
        this.trainManager = trainManager;
        this.passengerManager = passengerManager;
    }
    
    /**
     * Book a ticket for a passenger
     * @param name Passenger name
     * @param age Passenger age
     * @param gender Passenger gender
     * @param trainNumber Train number
     * @param classType Class type (AC/Sleeper)
     * @return BookingResult with booking details
     */
    public BookingResult bookTicket(String name, int age, String gender, 
                                  String trainNumber, String classType) {
        try {
            // Validate inputs
            validateBookingInputs(name, age, gender, trainNumber, classType);
            
            // Get train
            Train train = trainManager.getTrain(trainNumber);
            
            // Generate PNR
            String pnr = passengerManager.generatePNR();
            
            // Create passenger
            Passenger passenger = new Passenger(pnr, name, age, gender, trainNumber, classType);
            
            // Try to book seat
            boolean seatBooked = train.bookSeat(classType);
            
            if (seatBooked) {
                // Seat available - confirm booking
                passenger.confirmBooking();
                int seatIndex = calculateSeatIndex(train, classType);
                passenger.assignSeat(seatIndex);
                
                passengerManager.addPassenger(passenger);
                
                return new BookingResult(true, passenger, 
                    "Booking confirmed! Your PNR: " + pnr);
            } else {
                // No seat available - add to waitlist
                train.addToWaitlist(passenger, classType);
                passengerManager.addPassenger(passenger);
                
                int waitlistPosition = train.getWaitlistCount(classType);
                
                return new BookingResult(false, passenger, 
                    "Added to waitlist. Your PNR: " + pnr + 
                    ". Waitlist position: " + waitlistPosition);
            }
            
        } catch (Exception e) {
            return new BookingResult(false, null, "Booking failed: " + e.getMessage());
        }
    }
    
    /**
     * Cancel a booking
     * @param pnr PNR to cancel
     * @return CancellationResult with cancellation details
     */
    public CancellationResult cancelTicket(String pnr) {
        try {
            // Get passenger
            Passenger passenger = passengerManager.getPassenger(pnr);
            
            if (passenger.isCancelled()) {
                return new CancellationResult(false, null, null,
                    "Ticket is already cancelled.");
            }
            
            // Get train
            Train train = trainManager.getTrain(passenger.getTrainNumber());
            
            Passenger promotedPassenger = null;
            String message = "Ticket cancelled successfully.";
            
            if (passenger.isConfirmed()) {
                // Cancel confirmed seat and check for waitlist promotion
                promotedPassenger = train.cancelSeat(passenger.getClassType());
                
                if (promotedPassenger != null) {
                    // Promote waitlisted passenger
                    promotedPassenger.confirmBooking();
                    int seatIndex = calculateSeatIndex(train, promotedPassenger.getClassType());
                    promotedPassenger.assignSeat(seatIndex);
                    passengerManager.updatePassenger(promotedPassenger);
                    
                    message += " Passenger " + promotedPassenger.getName() + 
                              " (PNR: " + promotedPassenger.getPnr() + 
                              ") has been promoted from waitlist.";
                }
            }
            
            // Mark passenger as cancelled
            passenger.cancelBooking();
            passengerManager.updatePassenger(passenger);
            
            return new CancellationResult(true, passenger, promotedPassenger, message);
            
        } catch (Exception e) {
            return new CancellationResult(false, null, null, 
                "Cancellation failed: " + e.getMessage());
        }
    }
    
    /**
     * Check seat availability for a train and class
     * @param trainNumber Train number
     * @param classType Class type
     * @return Availability information
     */
    public String checkAvailability(String trainNumber, String classType) {
        try {
            trainManager.validateClassType(classType);
            TrainManager.SeatAvailability availability = 
                trainManager.getSeatAvailability(trainNumber, classType);
            
            Train train = trainManager.getTrain(trainNumber);
            
            StringBuilder info = new StringBuilder();
            info.append("\n=== SEAT AVAILABILITY ===\n");
            info.append("Train: ").append(train.getTrainName())
                .append(" (").append(trainNumber).append(")\n");
            info.append("Class: ").append(classType).append("\n");
            info.append("Total Seats: ").append(availability.getTotalSeats()).append("\n");
            info.append("Booked Seats: ").append(availability.getBookedSeats()).append("\n");
            info.append("Available Seats: ").append(availability.getAvailableSeats()).append("\n");
            info.append("Waitlist Count: ").append(availability.getWaitlistCount()).append("\n");
            info.append("========================\n");
            
            return info.toString();
            
        } catch (Exception e) {
            return "Error checking availability: " + e.getMessage();
        }
    }
    
    /**
     * Validate booking inputs
     * @param name Passenger name
     * @param age Passenger age
     * @param gender Passenger gender
     * @param trainNumber Train number
     * @param classType Class type
     * @throws BookingException if validation fails
     */
    private void validateBookingInputs(String name, int age, String gender, 
                                     String trainNumber, String classType) 
            throws BookingException, TrainNotFoundException, InvalidClassTypeException {
        
        if (name == null || name.trim().isEmpty()) {
            throw new BookingException("Passenger name cannot be empty.");
        }
        
        if (age < 1 || age > 120) {
            throw new BookingException("Age must be between 1 and 120.");
        }
        
        if (gender == null || (!gender.equalsIgnoreCase("M") && 
                              !gender.equalsIgnoreCase("F") && 
                              !gender.equalsIgnoreCase("Male") && 
                              !gender.equalsIgnoreCase("Female"))) {
            throw new BookingException("Gender must be M/F or Male/Female.");
        }
        
        if (trainNumber == null || trainNumber.trim().isEmpty()) {
            throw new BookingException("Train number cannot be empty.");
        }
        
        // Validate train exists
        trainManager.getTrain(trainNumber);
        
        // Validate class type
        trainManager.validateClassType(classType);
    }
    
    /**
     * Calculate seat index for seat assignment
     * @param train Train object
     * @param classType Class type
     * @return Seat index
     */
    private int calculateSeatIndex(Train train, String classType) {
        int bookedSeats = train.getBookedSeats(classType);
        return bookedSeats; // Simple sequential numbering
    }
    
    /**
     * Inner class to represent booking result
     */
    public static class BookingResult {
        private boolean success;
        private Passenger passenger;
        private String message;
        
        public BookingResult(boolean success, Passenger passenger, String message) {
            this.success = success;
            this.passenger = passenger;
            this.message = message;
        }
        
        public boolean isSuccess() { return success; }
        public Passenger getPassenger() { return passenger; }
        public String getMessage() { return message; }
    }
    
    /**
     * Inner class to represent cancellation result
     */
    public static class CancellationResult {
        private boolean success;
        private Passenger cancelledPassenger;
        private Passenger promotedPassenger;
        private String message;
        
        public CancellationResult(boolean success, Passenger cancelledPassenger, 
                                Passenger promotedPassenger, String message) {
            this.success = success;
            this.cancelledPassenger = cancelledPassenger;
            this.promotedPassenger = promotedPassenger;
            this.message = message;
        }
        
        public boolean isSuccess() { return success; }
        public Passenger getCancelledPassenger() { return cancelledPassenger; }
        public Passenger getPromotedPassenger() { return promotedPassenger; }
        public String getMessage() { return message; }
    }
}
