ALTER TABLE Tickets
ADD CHECK(ticketPrice > 0);

ALTER TABLE Customers
ADD CHECK(joinDate >= DATE '2015-11-27');

ALTER TABLE Showings
ADD CHECK(CASE 
	WHEN movieID IS NULL 
	THEN priceCode IS NULL
	ELSE priceCode IS NOT NULL
END);
