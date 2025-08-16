import java.util.List;
import java.util.Scanner;

/**
 * Main Railway Reservation System application
 */
public class RailwayReservationSystem {
    private TrainManager trainManager;
    private PassengerManager passengerManager;
    private BookingManager bookingManager;
    private Scanner scanner;
    
    /**
     * Constructor
     */
    public RailwayReservationSystem() {
        this.trainManager = new TrainManager();
        this.passengerManager = new PassengerManager();
        this.bookingManager = new BookingManager(trainManager, passengerManager);
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Main method
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        RailwayReservationSystem system = new RailwayReservationSystem();
        system.run();
    }
    
    /**
     * Run the application
     */
    public void run() {
        System.out.println("=".repeat(60));
        System.out.println("    WELCOME TO RAILWAY RESERVATION SYSTEM");
        System.out.println("=".repeat(60));
        
        // Load existing data
        loadData();
        
        boolean running = true;
        while (running) {
            try {
                displayMainMenu();
                int choice = getChoice();
                
                switch (choice) {
                    case 1:
                        bookTicket();
                        break;
                    case 2:
                        cancelTicket();
                        break;
                    case 3:
                        checkSeatAvailability();
                        break;
                    case 4:
                        searchBookingByPNR();
                        break;
                    case 5:
                        searchBookingByName();
                        break;
                    case 6:
                        viewAllBookings();
                        break;
                    case 7:
                        displayTrainList();
                        break;
                    case 8:
                        displaySystemStatistics();
                        break;
                    case 9:
                        running = false;
                        exitSystem();
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
                
                if (running) {
                    pressEnterToContinue();
                }
                
            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
                pressEnterToContinue();
            }
        }
    }
    
    /**
     * Display main menu
     */
    private void displayMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("              MAIN MENU");
        System.out.println("=".repeat(50));
        System.out.println("1. Book Ticket");
        System.out.println("2. Cancel Ticket");
        System.out.println("3. Check Seat Availability");
        System.out.println("4. Search Booking by PNR");
        System.out.println("5. Search Booking by Name");
        System.out.println("6. View All Bookings (Admin)");
        System.out.println("7. View Train List");
        System.out.println("8. System Statistics");
        System.out.println("9. Exit & Save");
        System.out.println("=".repeat(50));
        System.out.print("Enter your choice (1-9): ");
    }
    
    /**
     * Get user choice
     * @return User's choice as integer
     */
    private int getChoice() {
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            return choice;
        } catch (NumberFormatException e) {
            return -1; // Invalid choice
        }
    }
    
    /**
     * Book a ticket
     */
    private void bookTicket() {
        System.out.println("\n=== BOOK TICKET ===");
        
        try {
            // Display available trains first
            trainManager.displayAllTrains();
            
            System.out.print("\nEnter passenger name: ");
            String name = scanner.nextLine().trim();
            
            System.out.print("Enter age: ");
            int age = Integer.parseInt(scanner.nextLine().trim());
            
            System.out.print("Enter gender (M/F): ");
            String gender = scanner.nextLine().trim();
            
            System.out.print("Enter train number: ");
            String trainNumber = scanner.nextLine().trim();
            
            System.out.print("Enter class (AC/Sleeper): ");
            String classType = scanner.nextLine().trim();
            
            // Book ticket
            BookingManager.BookingResult result = bookingManager.bookTicket(
                name, age, gender, trainNumber, classType);
            
            System.out.println("\n" + result.getMessage());
            
            if (result.isSuccess() && result.getPassenger() != null) {
                System.out.println(result.getPassenger().getBookingDetails());
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid age format. Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("Booking failed: " + e.getMessage());
        }
    }
    
    /**
     * Cancel a ticket
     */
    private void cancelTicket() {
        System.out.println("\n=== CANCEL TICKET ===");
        
        try {
            System.out.print("Enter PNR number: ");
            String pnr = scanner.nextLine().trim();
            
            // Show current booking details before cancellation
            try {
                Passenger passenger = passengerManager.getPassenger(pnr);
                System.out.println("\nCurrent Booking Details:");
                System.out.println(passenger.getBookingDetails());
                
                System.out.print("Are you sure you want to cancel this booking? (Y/N): ");
                String confirmation = scanner.nextLine().trim();
                
                if (confirmation.equalsIgnoreCase("Y") || confirmation.equalsIgnoreCase("Yes")) {
                    BookingManager.CancellationResult result = bookingManager.cancelTicket(pnr);
                    System.out.println("\n" + result.getMessage());
                    
                    if (result.getPromotedPassenger() != null) {
                        System.out.println("\nPromoted Passenger Details:");
                        System.out.println(result.getPromotedPassenger().getBookingDetails());
                    }
                } else {
                    System.out.println("Cancellation aborted.");
                }
                
            } catch (PassengerNotFoundException e) {
                System.out.println("PNR not found: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.out.println("Cancellation failed: " + e.getMessage());
        }
    }
    
    /**
     * Check seat availability
     */
    private void checkSeatAvailability() {
        System.out.println("\n=== CHECK SEAT AVAILABILITY ===");
        
        try {
            trainManager.displayAllTrains();
            
            System.out.print("\nEnter train number: ");
            String trainNumber = scanner.nextLine().trim();
            
            System.out.print("Enter class (AC/Sleeper): ");
            String classType = scanner.nextLine().trim();
            
            String availability = bookingManager.checkAvailability(trainNumber, classType);
            System.out.println(availability);
            
        } catch (Exception e) {
            System.out.println("Error checking availability: " + e.getMessage());
        }
    }
    
    /**
     * Search booking by PNR
     */
    private void searchBookingByPNR() {
        System.out.println("\n=== SEARCH BY PNR ===");
        
        try {
            System.out.print("Enter PNR number: ");
            String pnr = scanner.nextLine().trim();
            
            passengerManager.displayPassengerDetails(pnr);
            
        } catch (PassengerNotFoundException e) {
            System.out.println("PNR not found: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Search failed: " + e.getMessage());
        }
    }
    
    /**
     * Search booking by name
     */
    private void searchBookingByName() {
        System.out.println("\n=== SEARCH BY NAME ===");
        
        try {
            System.out.print("Enter passenger name (or part of name): ");
            String name = scanner.nextLine().trim();
            
            List<Passenger> passengers = passengerManager.searchByName(name);
            
            if (passengers.isEmpty()) {
                System.out.println("No bookings found for name: " + name);
            } else {
                System.out.println("\nFound " + passengers.size() + " booking(s):");
                System.out.println("-".repeat(60));
                
                for (Passenger passenger : passengers) {
                    System.out.println(passenger.getBookingDetails());
                }
            }
            
        } catch (Exception e) {
            System.out.println("Search failed: " + e.getMessage());
        }
    }
    
    /**
     * View all bookings (Admin function)
     */
    private void viewAllBookings() {
        System.out.println("\n=== ALL BOOKINGS (ADMIN VIEW) ===");
        
        System.out.print("Enter admin password (default: admin123): ");
        String password = scanner.nextLine().trim();
        
        if (password.equals("admin123")) {
            passengerManager.displayAllBookings();
        } else {
            System.out.println("Access denied. Invalid admin password.");
        }
    }
    
    /**
     * Display train list
     */
    private void displayTrainList() {
        System.out.println("\n=== AVAILABLE TRAINS ===");
        trainManager.displayAllTrains();
    }
    
    /**
     * Display system statistics
     */
    private void displaySystemStatistics() {
        System.out.println("\n=== SYSTEM STATISTICS ===");
        System.out.println(passengerManager.getBookingStatistics());
        System.out.println("Total Trains: " + trainManager.getAllTrains().size());
        System.out.println(DataPersistence.getFileInfo());
        System.out.println("=".repeat(50));
    }
    
    /**
     * Load data from files
     */
    private void loadData() {
        try {
            if (DataPersistence.dataFilesExist()) {
                System.out.println("Loading existing data...");
                DataPersistence.loadAllData(trainManager, passengerManager);
            } else {
                System.out.println("No existing data found. Starting with default configuration.");
            }
        } catch (DataPersistenceException e) {
            System.err.println("Warning: " + e.getMessage());
            System.out.println("Starting with default configuration.");
        }
    }
    
    /**
     * Exit system and save data
     */
    private void exitSystem() {
        System.out.println("\nSaving data and exiting...");
        
        try {
            DataPersistence.saveAllData(trainManager, passengerManager);
            System.out.println("Thank you for using Railway Reservation System!");
        } catch (DataPersistenceException e) {
            System.err.println("Error saving data: " + e.getMessage());
            System.out.print("Continue exiting without saving? (Y/N): ");
            String response = scanner.nextLine().trim();
            if (!response.equalsIgnoreCase("Y") && !response.equalsIgnoreCase("Yes")) {
                return; // Don't exit, return to menu
            }
        }
        
        scanner.close();
        System.out.println("Goodbye!");
        System.exit(0);
    }
    
    /**
     * Press enter to continue
     */
    private void pressEnterToContinue() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}
