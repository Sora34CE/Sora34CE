INSERT INTO TICKETS(theaterID, seatNum, showingDate, startTime, customerID, ticketPrice)
VALUES(111, 2, DATE'2009-06-23', TIME'16:00:00', 2, 8.50);

INSERT INTO TICKETS(theaterID, seatNum, showingDate, startTime, customerID, ticketPrice)
VALUES(111, 2, DATE'2009-06-23', TIME'15:00:00', 5, 9.00);

INSERT INTO TICKETS(theaterID, seatNum, showingDate, startTime, customerID, ticketPrice)
VALUES(222, 3, TIME'2015-04-22', TIME'18:00:00', 3, 10:50);

UPDATE TICKETS
	SET ticketPrice = 10.00;
UPDATE TICKETS t2
	SET ticketPrice = -9.99;
UPDATE CUSTOMERS c1
	SET joinDate = DATE'2016-09-23';
UPDATE CUSTOMERS c2
	SET joinDate = DATE'2014-08-22';
UPDATE SHOWINGS s1
	SET priceCode = NULL
	WHERE movieID IS NULL;
UPDATE SHOWINGS s2
	SET priceCode = NULL
	WHERE movieID IS NOT NULL;
