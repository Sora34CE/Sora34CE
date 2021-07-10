SELECT DISTINCT c.customerID, c.name
FROM Customers c, Tickets t, Showings s
WHERE (c.name LIKE '%a%' OR c.name LIKE '%A%')
AND c.customerID = t.customerID
AND t.theaterID = s.theaterID
AND t.showingDate = s.showingDate
AND t.startTime = s.startTime
GROUP BY c.customerID
HAVING COUNT(s.movieID) >= 2;
