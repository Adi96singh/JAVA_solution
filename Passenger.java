import java.io.Serializable;

/**
 * Passenger class representing a railway passenger booking
 */
public class Passenger implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String pnr;
    private String name;
    private int age;
    private String gender;
    private String trainNumber;
    private String classType;
    private String seatNumber;
    private BookingStatus status;
    
    // Enum for booking status
    public enum BookingStatus {
        CONFIRMED, WAITLISTED, CANCELLED
    }
    
    /**
     * Constructor for Passenger
     */
    public Passenger(String pnr, String name, int age, String gender, 
                    String trainNumber, String classType) {
        this.pnr = pnr;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.trainNumber = trainNumber;
        this.classType = classType;
        this.status = BookingStatus.WAITLISTED; // Default status
        this.seatNumber = null; // Assigned only when confirmed
    }
    
    /**
     * Generate seat number based on class and booking sequence
     * @param seatIndex Index for seat assignment
     */
    public void assignSeat(int seatIndex) {
        if (status == BookingStatus.CONFIRMED) {
            String classPrefix = classType.equals(Train.AC_CLASS) ? "A" : "S";
            this.seatNumber = classPrefix + String.format("%02d", seatIndex);
        }
    }
    
    /**
     * Confirm the booking and assign seat
     */
    public void confirmBooking() {
        this.status = BookingStatus.CONFIRMED;
    }
    
    /**
     * Cancel the booking
     */
    public void cancelBooking() {
        this.status = BookingStatus.CANCELLED;
        this.seatNumber = null;
    }
    
    /**
     * Check if passenger is confirmed
     * @return true if confirmed, false otherwise
     */
    public boolean isConfirmed() {
        return status == BookingStatus.CONFIRMED;
    }
    
    /**
     * Check if passenger is waitlisted
     * @return true if waitlisted, false otherwise
     */
    public boolean isWaitlisted() {
        return status == BookingStatus.WAITLISTED;
    }
    
    /**
     * Check if booking is cancelled
     * @return true if cancelled, false otherwise
     */
    public boolean isCancelled() {
        return status == BookingStatus.CANCELLED;
    }
    
    // Getters and Setters
    public String getPnr() {
        return pnr;
    }
    
    public void setPnr(String pnr) {
        this.pnr = pnr;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getTrainNumber() {
        return trainNumber;
    }
    
    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }
    
    public String getClassType() {
        return classType;
    }
    
    public void setClassType(String classType) {
        this.classType = classType;
    }
    
    public String getSeatNumber() {
        return seatNumber;
    }
    
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
    
    public BookingStatus getStatus() {
        return status;
    }
    
    public void setStatus(BookingStatus status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return String.format("Passenger{PNR='%s', Name='%s', Age=%d, Gender='%s', " +
                           "Train='%s', Class='%s', Seat='%s', Status='%s'}", 
                           pnr, name, age, gender, trainNumber, classType, 
                           seatNumber != null ? seatNumber : "Not Assigned", status);
    }
    
    /**
     * Get formatted booking details for display
     * @return Formatted string with booking details
     */
    public String getBookingDetails() {
        StringBuilder details = new StringBuilder();
        details.append("=== BOOKING DETAILS ===\n");
        details.append("PNR: ").append(pnr).append("\n");
        details.append("Passenger Name: ").append(name).append("\n");
        details.append("Age: ").append(age).append("\n");
        details.append("Gender: ").append(gender).append("\n");
        details.append("Train Number: ").append(trainNumber).append("\n");
        details.append("Class: ").append(classType).append("\n");
        details.append("Seat Number: ").append(seatNumber != null ? seatNumber : "Not Assigned").append("\n");
        details.append("Status: ").append(status).append("\n");
        details.append("========================\n");
        return details.toString();
    }
}
