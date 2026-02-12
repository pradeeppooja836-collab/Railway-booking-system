# Railway-booking-system

Team Members

* Pooja Pradeep
* Ahalya K

Problem Statement

Manual railway ticket booking and seat tracking can be time-consuming and error-prone. This project aims to develop a computerized system that simplifies ticket reservation, cancellation, and booking management through an interactive graphical interface.

Objective

To design and implement a Railway Booking System using Java that demonstrates:

Object-Oriented Programming concepts
GUI development
Database connectivity using JDBC
Real-time seat allocation and management

Features
* Book Ticket

Enter passenger details (Name, Age, Gender, Source, Destination).
Select seats visually from the seat map.
Confirm booking with one click.

* Cancel Ticket

Cancel booked tickets using passenger/seat details.
Seat becomes available again automatically.

* View Bookings

Displays all confirmed reservations.
Shows passenger details and seat numbers.

*Seat Map Layout

Interactive seat selection interface.

Visual seat status representation:

 Green → Available seats
 Red → Booked seats
 Yellow → Currently selected seat
 
 Technologies Used

Programming Language: Java
GUI Framework: Java Swing
Database: SQLite
Connectivity: JDBC Driver
IDE/Editor: VS Code / NetBeans / IntelliJ IDEA

 OOP Concepts Implemented

Classes & Objects
Inheritance
Polymorphism
Abstraction
Encapsulation

Database Integration

SQLite database used for storing booking records.
JDBC driver enables Java–Database connectivity.
Automatic table creation for bookings.

 Steps to Run the Program

Install JDK (Java Development Kit).
Install VS Code / NetBeans.
Download SQLite JDBC Driver.
Place the driver .jar file inside the lib folder.

Compile the program:

javac -cp ".;lib/sqlite-jdbc-3.51.2.0.jar" src/RailwayBookingSystem.java

Run the program:

java -cp ".;lib/sqlite-jdbc-3.51.2.0.jar;src" RailwayBookingSystem
