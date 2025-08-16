/**
 * Custom exception classes for Railway Reservation System
 */

/**
 * Base exception class for railway operations
 */
class RailwayException extends Exception {
    public RailwayException(String message) {
        super(message);
    }
    
    public RailwayException(String message, Throwable cause) {
        super(message, cause);
    }
}

/**
 * Exception thrown when train is not found
 */
class TrainNotFoundException extends RailwayException {
    public TrainNotFoundException(String trainNumber) {
        super("Train with number '" + trainNumber + "' not found.");
    }
}

/**
 * Exception thrown when passenger/PNR is not found
 */
class PassengerNotFoundException extends RailwayException {
    public PassengerNotFoundException(String pnr) {
        super("Passenger with PNR '" + pnr + "' not found.");
    }
}

/**
 * Exception thrown when invalid class type is provided
 */
class InvalidClassTypeException extends RailwayException {
    public InvalidClassTypeException(String classType) {
        super("Invalid class type '" + classType + "'. Valid types are: AC, Sleeper");
    }
}

/**
 * Exception thrown when booking cannot be processed
 */
class BookingException extends RailwayException {
    public BookingException(String message) {
        super(message);
    }
}

/**
 * Exception thrown when cancellation cannot be processed
 */
class CancellationException extends RailwayException {
    public CancellationException(String message) {
        super(message);
    }
}

/**
 * Exception thrown during file operations
 */
class DataPersistenceException extends RailwayException {
    public DataPersistenceException(String message) {
        super(message);
    }
    
    public DataPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
