# Railway Reservation System

A comprehensive console-based Railway Reservation System implemented in Java with object-oriented programming principles and file-based persistence.

## 🚂 Project Overview

This system simulates a railway ticket booking system with features like seat allocation, waitlist management, cancellation with automatic promotion, and data persistence across program runs.

## ✨ Features

- **Train Management**: Multiple trains with configurable seat capacity per class
- **Passenger Management**: Comprehensive passenger information handling
- **Booking System**: Automatic seat allocation with PNR generation
- **Waitlist Management**: FIFO queue-based waitlist system
- **Cancellation System**: Automatic promotion from waitlist upon cancellation
- **Search Functionality**: Search by PNR or passenger name
- **Data Persistence**: File-based storage using Java serialization
- **Admin Features**: System statistics and complete booking overview

## 🏗️ System Architecture

### Core Classes

1. **Train.java**: Represents a train with seat management capabilities
2. **Passenger.java**: Represents a passenger booking with status tracking
3. **TrainManager.java**: Manages all train operations and validations
4. **PassengerManager.java**: Handles passenger operations and PNR management
5. **BookingManager.java**: Core booking and cancellation logic
6. **DataPersistence.java**: File-based data storage and retrieval
7. **RailwayException.java**: Custom exception classes for error handling
8. **RailwayReservationSystem.java**: Main console interface

## 🚀 Getting Started

### Prerequisites
- Java JDK 8 or higher
- Command line interface (Terminal/CMD/PowerShell)

### Installation & Running

1. **Compile the system:**
```bash
javac *.java
```

2. **Run the main application:**
```bash
java RailwayReservationSystem
```

3. **Run the system test:**
```bash
java SystemTest
```

## 🎮 How to Use

### Main Menu Options

1. **Book Ticket**: Create new passenger booking
   - Enter passenger details (name, age, gender)
   - Select train number and class
   - Get immediate confirmation or waitlist status

2. **Cancel Ticket**: Cancel existing booking
   - Enter PNR number
   - Automatic waitlist promotion if applicable

3. **Check Seat Availability**: View current availability
   - Select train and class
   - See total, booked, available, and waitlist counts

4. **Search Booking by PNR**: Find specific booking
   - Enter PNR number
   - View complete booking details

5. **Search Booking by Name**: Find bookings by passenger name
   - Enter full or partial name
   - View all matching bookings

6. **View All Bookings**: Admin view of all bookings
   - Requires admin password (default: admin123)
   - Shows complete system overview

7. **View Train List**: Display all available trains
   - Shows train numbers, names, and current occupancy

8. **System Statistics**: View system metrics
   - Booking statistics
   - File information
   - System status

9. **Exit & Save**: Safely exit and save data

### Sample Usage Flow

```
1. Start the application
2. Select "7. View Train List" to see available trains
3. Select "1. Book Ticket" to make a reservation
4. Enter passenger details and train information
5. Receive PNR confirmation
6. Use "4. Search Booking by PNR" to verify booking
7. Select "9. Exit & Save" to save and exit
```

## 🏛️ System Design

### Data Structures Used

- **HashMap**: For O(1) lookup of trains and passengers
- **LinkedList**: For FIFO waitlist queue management
- **ArrayList**: For search results and collections
- **Enum**: For booking status management

### Business Rules

- **PNR Generation**: Unique 10-character alphanumeric code
- **Seat Allocation**: Sequential numbering (A01, A02... for AC, S01, S02... for Sleeper)
- **Waitlist Policy**: First-In-First-Out (FIFO)
- **Automatic Promotion**: Immediate upon cancellation
- **Data Persistence**: Automatic save on exit, load on startup

### Default Configuration

**Available Trains:**
- 12345: Rajdhani Express (AC: 30 seats, Sleeper: 60 seats)
- 23456: Shatabdi Express (AC: 40 seats, Sleeper: 40 seats)
- 34567: Duronto Express (AC: 20 seats, Sleeper: 50 seats)
- 45678: Garib Rath (AC: 20 seats, Sleeper: 50 seats)
- 56789: Jan Shatabdi (AC: 20 seats, Sleeper: 50 seats)

## 📁 File Structure

```
Railway/
├── Train.java                    # Train model class
├── Passenger.java                # Passenger model class
├── TrainManager.java             # Train operations manager
├── PassengerManager.java         # Passenger operations manager
├── BookingManager.java           # Booking and cancellation logic
├── DataPersistence.java          # File storage handling
├── RailwayException.java         # Custom exceptions
├── RailwayReservationSystem.java # Main application
├── SystemTest.java               # Automated testing
├── README.md                     # This documentation
├── trains.dat                    # Train data file (generated)
└── passengers.dat                # Passenger data file (generated)
```

## 🔍 Testing

The system includes comprehensive testing via `SystemTest.java` that demonstrates:

- Train display and availability checking
- Successful booking confirmations
- Waitlist functionality when capacity is full
- Search operations by PNR and name
- Cancellation with automatic waitlist promotion
- Data persistence and recovery
- Error handling and validation

**Run tests:**
```bash
java SystemTest
```

## ⚡ Technical Highlights

### Object-Oriented Design
- **Encapsulation**: Private fields with public methods
- **Inheritance**: Exception hierarchy
- **Polymorphism**: Interface-based design patterns
- **Abstraction**: Clear separation of concerns

### Error Handling
- Custom exception classes for different error types
- Input validation with meaningful error messages
- Graceful handling of file I/O operations
- Recovery mechanisms for data corruption

### Performance Optimization
- HashMap for O(1) lookup operations
- Efficient queue operations for waitlist
- Minimal memory footprint with proper object lifecycle
- Optimized file I/O with buffered streams

### Data Integrity
- Atomic booking operations
- Consistent state management
- Backup and recovery mechanisms
- Validation at multiple levels

## 🛠️ Customization Options

### Adding New Trains
```java
trainManager.addTrain("99999", "Express Special");
Train newTrain = trainManager.getTrain("99999");
newTrain.setSeatsForClass("AC", 25);
newTrain.setSeatsForClass("Sleeper", 75);
```

### Modifying Seat Configuration
```java
Train train = trainManager.getTrain("12345");
train.setSeatsForClass("AC", 50); // Increase AC seats to 50
```

### Custom PNR Format
Modify the `generatePNR()` method in `PassengerManager.java` to change PNR format.

## 🐛 Troubleshooting

### Common Issues

1. **Compilation Errors**: Ensure all Java files are in the same directory
2. **File Access Issues**: Check write permissions for data files
3. **Memory Issues**: Increase JVM heap size if handling large datasets
4. **Data Corruption**: Use backup files or clear data files to reset

### Data Recovery
```bash
# Clear corrupted data files
rm trains.dat passengers.dat

# System will start with default configuration
java RailwayReservationSystem
```

## 🔮 Future Enhancements

- **GUI Interface**: JavaFX or Swing implementation
- **Database Integration**: MySQL or SQLite backend
- **Web Interface**: REST API with web frontend
- **Multi-threading**: Concurrent booking support
- **Payment Integration**: Mock payment processing
- **Email Notifications**: Booking confirmations
- **Advanced Search**: Date-based booking, route search
- **Reporting**: Analytics and business intelligence

## 👥 Contributing

1. Fork the repository
2. Create feature branch
3. Implement changes with tests
4. Submit pull request with detailed description

## 📄 License

This project is created for educational purposes. Feel free to use and modify according to your needs.

## 📞 Support

For issues or questions:
1. Check the troubleshooting section
2. Review the system test for usage examples
3. Examine the source code for implementation details

---

**Happy Coding! 🚂**
