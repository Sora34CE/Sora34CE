SELECT DISTINCT c.customerID AS custID, c.name AS custName, c.address AS custAddress, th.address AS theaterAddress, th.numSeats AS theaterSeats, s.priceCode
FROM Customers c, Theaters th, Showings s, Tickets t
WHERE t.showingDate BETWEEN 'June 1, 2019' AND 'June 30, 2019'
AND t.customerID = c.customerID
AND c.name LIKE 'D%'
AND t.showingDate = s.showingDate
AND s.priceCode IS NOT NULL
AND t.theaterID = th.theaterID
AND th.numSeats > 5;
