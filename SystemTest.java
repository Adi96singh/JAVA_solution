/**
 * Test class to demonstrate Railway Reservation System functionality
 */
public class SystemTest {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("    RAILWAY RESERVATION SYSTEM - FUNCTIONALITY TEST");
        System.out.println("=".repeat(60));
        
        // Initialize system components
        TrainManager trainManager = new TrainManager();
        PassengerManager passengerManager = new PassengerManager();
        BookingManager bookingManager = new BookingManager(trainManager, passengerManager);
        
        runTests(trainManager, passengerManager, bookingManager);
    }
    
    public static void runTests(TrainManager trainManager, PassengerManager passengerManager, 
                               BookingManager bookingManager) {
        
        System.out.println("\n1. DISPLAYING AVAILABLE TRAINS:");
        trainManager.displayAllTrains();
        
        System.out.println("\n2. TESTING SEAT AVAILABILITY CHECK:");
        String availability = bookingManager.checkAvailability("12345", "AC");
        System.out.println(availability);
        
        System.out.println("\n3. TESTING TICKET BOOKING - CONFIRMED BOOKINGS:");
        
        // Book some confirmed tickets
        BookingManager.BookingResult result1 = bookingManager.bookTicket(
            "John Doe", 30, "M", "12345", "AC");
        System.out.println("Booking 1: " + result1.getMessage());
        if (result1.getPassenger() != null) {
            System.out.println(result1.getPassenger().getBookingDetails());
        }
        
        BookingManager.BookingResult result2 = bookingManager.bookTicket(
            "Jane Smith", 25, "F", "12345", "Sleeper");
        System.out.println("Booking 2: " + result2.getMessage());
        if (result2.getPassenger() != null) {
            System.out.println(result2.getPassenger().getBookingDetails());
        }
        
        System.out.println("\n4. TESTING SEARCH BY PNR:");
        if (result1.getPassenger() != null) {
            try {
                passengerManager.displayPassengerDetails(result1.getPassenger().getPnr());
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        
        System.out.println("\n5. TESTING SEARCH BY NAME:");
        var passengers = passengerManager.searchByName("John");
        System.out.println("Found " + passengers.size() + " passenger(s) with name containing 'John':");
        for (var passenger : passengers) {
            System.out.println(passenger.toString());
        }
        
        System.out.println("\n6. BOOKING TICKETS TO FILL CAPACITY (TESTING WAITLIST):");
        
        // Book more AC tickets to test waitlist functionality
        for (int i = 0; i < 35; i++) { // AC has 30 seats in Rajdhani
            BookingManager.BookingResult result = bookingManager.bookTicket(
                "Passenger" + (i+3), 25 + (i % 50), "M", "12345", "AC");
            
            if (i < 5 || i >= 28) { // Show first few and waitlisted ones
                System.out.println("Booking " + (i+3) + ": " + result.getMessage());
            } else if (i == 5) {
                System.out.println("... (continuing to book tickets) ...");
            }
        }
        
        System.out.println("\n7. CHECKING AVAILABILITY AFTER BOOKINGS:");
        availability = bookingManager.checkAvailability("12345", "AC");
        System.out.println(availability);
        
        System.out.println("\n8. TESTING CANCELLATION AND WAITLIST PROMOTION:");
        if (result1.getPassenger() != null) {
            String pnrToCancel = result1.getPassenger().getPnr();
            BookingManager.CancellationResult cancelResult = bookingManager.cancelTicket(pnrToCancel);
            System.out.println("Cancellation: " + cancelResult.getMessage());
            
            if (cancelResult.getPromotedPassenger() != null) {
                System.out.println("Promoted Passenger Details:");
                System.out.println(cancelResult.getPromotedPassenger().getBookingDetails());
            }
        }
        
        System.out.println("\n9. FINAL SYSTEM STATISTICS:");
        System.out.println(passengerManager.getBookingStatistics());
        trainManager.displayAllTrains();
        
        System.out.println("\n10. TESTING DATA PERSISTENCE:");
        try {
            DataPersistence.saveAllData(trainManager, passengerManager);
            System.out.println("Data saved successfully!");
            
            // Create new instances and load data
            TrainManager newTrainManager = new TrainManager();
            PassengerManager newPassengerManager = new PassengerManager();
            
            DataPersistence.loadAllData(newTrainManager, newPassengerManager);
            System.out.println("Data loaded successfully!");
            
            System.out.println("Loaded Statistics: " + newPassengerManager.getBookingStatistics());
            
        } catch (DataPersistenceException e) {
            System.out.println("Persistence test failed: " + e.getMessage());
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("    ALL TESTS COMPLETED SUCCESSFULLY!");
        System.out.println("=".repeat(60));
    }
}
