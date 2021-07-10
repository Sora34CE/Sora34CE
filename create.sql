DROP SCHEMA Lab1 CASCADE;
CREATE SCHEMA Lab1;

-- Table for movies: movie has a name, ID, release date, rating, runtime, box office
CREATE TABLE Movies (
movieID INTEGER,
name VARCHAR(30),
year INTEGER,
rating CHAR(1),
length INTEGER,
totalEarned NUMERIC(7, 2),
PRIMARY KEY (movieID)
);

-- Table for theaters: ID, address, number of seats
CREATE TABLE Theaters (
theaterID INTEGER,
address VARCHAR(40),
numSeats INTEGER,
PRIMARY KEY(theaterID)
);

-- Theater seats: takes theaterID from theater, number of seats, and if seat is broken
CREATE TABLE TheaterSeats (
theaterID INTEGER,
seatNum INTEGER,
brokenSeat BOOLEAN,
PRIMARY KEY(theaterID, seatNum),
FOREIGN KEY (theaterID) REFERENCES Theaters
);

-- Showings: takes movieID from Movies, theaterID from Theaters
CREATE TABLE Showings (
theaterID INTEGER,
showingDate DATE,
startTime TIME,
movieID INTEGER,
priceCode CHAR(1),
PRIMARY KEY(theaterID, showingDate, startTime),
FOREIGN KEY (movieID) REFERENCES Movies,
FOREIGN KEY (theaterID) REFERENCES Theaters
);

-- Customers needs ID, name, address, joinDate, status
CREATE TABLE Customers (
customerID INTEGER,
name VARCHAR(30),
address VARCHAR(40),
joinDate DATE,
status CHAR(1),
PRIMARY KEY(customerID)
);

-- Tickets: takes customerID from Customers, theaterID, showDate, and startTime from Showings, and theaterID and seatNum from TheaterSeats
CREATE TABLE Tickets (
theaterID INTEGER,
seatNum INTEGER,
showingDate DATE,
startTime TIME,
customerID INTEGER,
ticketPrice NUMERIC(4, 2),
PRIMARY KEY(theaterID, seatNum, showingDate, startTime),
FOREIGN KEY (customerID) REFERENCES Customers,
FOREIGN KEY (theaterID, showingDate, startTime) REFERENCES Showings,
FOREIGN KEY (theaterID, seatNum) REFERENCES TheaterSeats
);
