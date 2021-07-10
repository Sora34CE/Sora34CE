ALTER TABLE Tickets
ADD CONSTRAINT newDate 
FOREIGN KEY (theaterID, showingDate, startTime) REFERENCES Showings(theaterID, showingDate, startTime);

ALTER TABLE Tickets
ADD CONSTRAINT newCustomer 
FOREIGN KEY (customerID) REFERENCES Customers(customerID) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE Tickets
ADD CONSTRAINT newSeat
FOREIGN KEY (theaterID, seatNum) REFERENCES TheaterSeats(theaterID, seatNum) ON DELETE CASCADE ON UPDATE CASCADE; 
