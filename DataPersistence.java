import java.io.*;
import java.util.HashMap;

/**
 * DataPersistence class to handle file-based data storage and retrieval
 */
public class DataPersistence {
    private static final String TRAINS_FILE = "trains.dat";
    private static final String PASSENGERS_FILE = "passengers.dat";
    
    /**
     * Save train data to file
     * @param trains HashMap of trains to save
     * @throws DataPersistenceException if save fails
     */
    public static void saveTrains(HashMap<String, Train> trains) throws DataPersistenceException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(TRAINS_FILE))) {
            oos.writeObject(trains);
            System.out.println("Train data saved successfully to " + TRAINS_FILE);
        } catch (IOException e) {
            throw new DataPersistenceException("Failed to save train data: " + e.getMessage(), e);
        }
    }
    
    /**
     * Load train data from file
     * @return HashMap of trains loaded from file
     * @throws DataPersistenceException if load fails
     */
    @SuppressWarnings("unchecked")
    public static HashMap<String, Train> loadTrains() throws DataPersistenceException {
        File file = new File(TRAINS_FILE);
        if (!file.exists()) {
            System.out.println("Train data file not found. Starting with empty data.");
            return new HashMap<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(TRAINS_FILE))) {
            HashMap<String, Train> trains = (HashMap<String, Train>) ois.readObject();
            System.out.println("Train data loaded successfully from " + TRAINS_FILE);
            return trains;
        } catch (IOException | ClassNotFoundException e) {
            throw new DataPersistenceException("Failed to load train data: " + e.getMessage(), e);
        }
    }
    
    /**
     * Save passenger data to file
     * @param passengers HashMap of passengers to save
     * @throws DataPersistenceException if save fails
     */
    public static void savePassengers(HashMap<String, Passenger> passengers) throws DataPersistenceException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(PASSENGERS_FILE))) {
            oos.writeObject(passengers);
            System.out.println("Passenger data saved successfully to " + PASSENGERS_FILE);
        } catch (IOException e) {
            throw new DataPersistenceException("Failed to save passenger data: " + e.getMessage(), e);
        }
    }
    
    /**
     * Load passenger data from file
     * @return HashMap of passengers loaded from file
     * @throws DataPersistenceException if load fails
     */
    @SuppressWarnings("unchecked")
    public static HashMap<String, Passenger> loadPassengers() throws DataPersistenceException {
        File file = new File(PASSENGERS_FILE);
        if (!file.exists()) {
            System.out.println("Passenger data file not found. Starting with empty data.");
            return new HashMap<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(PASSENGERS_FILE))) {
            HashMap<String, Passenger> passengers = (HashMap<String, Passenger>) ois.readObject();
            System.out.println("Passenger data loaded successfully from " + PASSENGERS_FILE);
            return passengers;
        } catch (IOException | ClassNotFoundException e) {
            throw new DataPersistenceException("Failed to load passenger data: " + e.getMessage(), e);
        }
    }
    
    /**
     * Save all data (trains and passengers)
     * @param trainManager TrainManager instance
     * @param passengerManager PassengerManager instance
     * @throws DataPersistenceException if save fails
     */
    public static void saveAllData(TrainManager trainManager, PassengerManager passengerManager) 
            throws DataPersistenceException {
        try {
            saveTrains(trainManager.getTrainsMap());
            savePassengers(passengerManager.getPassengersMap());
            System.out.println("All data saved successfully!");
        } catch (DataPersistenceException e) {
            System.err.println("Error saving data: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Load all data (trains and passengers)
     * @param trainManager TrainManager instance
     * @param passengerManager PassengerManager instance
     * @throws DataPersistenceException if load fails
     */
    public static void loadAllData(TrainManager trainManager, PassengerManager passengerManager) 
            throws DataPersistenceException {
        try {
            HashMap<String, Train> trains = loadTrains();
            HashMap<String, Passenger> passengers = loadPassengers();
            
            if (!trains.isEmpty()) {
                trainManager.setTrainsMap(trains);
            }
            
            if (!passengers.isEmpty()) {
                passengerManager.setPassengersMap(passengers);
            }
            
            System.out.println("All data loaded successfully!");
        } catch (DataPersistenceException e) {
            System.err.println("Error loading data: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Create backup of data files
     */
    public static void createBackup() {
        try {
            File trainsFile = new File(TRAINS_FILE);
            File passengersFile = new File(PASSENGERS_FILE);
            
            if (trainsFile.exists()) {
                copyFile(trainsFile, new File(TRAINS_FILE + ".backup"));
            }
            
            if (passengersFile.exists()) {
                copyFile(passengersFile, new File(PASSENGERS_FILE + ".backup"));
            }
            
            System.out.println("Backup created successfully!");
        } catch (IOException e) {
            System.err.println("Failed to create backup: " + e.getMessage());
        }
    }
    
    /**
     * Copy file utility method
     * @param source Source file
     * @param destination Destination file
     * @throws IOException if copy fails
     */
    private static void copyFile(File source, File destination) throws IOException {
        try (FileInputStream fis = new FileInputStream(source);
             FileOutputStream fos = new FileOutputStream(destination)) {
            
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
        }
    }
    
    /**
     * Check if data files exist
     * @return true if both files exist, false otherwise
     */
    public static boolean dataFilesExist() {
        File trainsFile = new File(TRAINS_FILE);
        File passengersFile = new File(PASSENGERS_FILE);
        return trainsFile.exists() && passengersFile.exists();
    }
    
    /**
     * Get file information
     * @return String with file information
     */
    public static String getFileInfo() {
        StringBuilder info = new StringBuilder();
        info.append("=== DATA FILES INFO ===\n");
        
        File trainsFile = new File(TRAINS_FILE);
        File passengersFile = new File(PASSENGERS_FILE);
        
        if (trainsFile.exists()) {
            info.append("Trains file: ").append(TRAINS_FILE)
                .append(" (").append(trainsFile.length()).append(" bytes)\n");
        } else {
            info.append("Trains file: Not found\n");
        }
        
        if (passengersFile.exists()) {
            info.append("Passengers file: ").append(PASSENGERS_FILE)
                .append(" (").append(passengersFile.length()).append(" bytes)\n");
        } else {
            info.append("Passengers file: Not found\n");
        }
        
        info.append("========================");
        return info.toString();
    }
    
    /**
     * Clear all data files (for testing purposes)
     */
    public static void clearDataFiles() {
        File trainsFile = new File(TRAINS_FILE);
        File passengersFile = new File(PASSENGERS_FILE);
        
        if (trainsFile.exists()) {
            trainsFile.delete();
            System.out.println("Trains data file deleted.");
        }
        
        if (passengersFile.exists()) {
            passengersFile.delete();
            System.out.println("Passengers data file deleted.");
        }
    }
}
