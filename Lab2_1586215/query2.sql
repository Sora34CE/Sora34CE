SELECT DISTINCT m.name, m.year
FROM Movies m, Showings s, Customers c, Tickets t
WHERE c.name = 'Donald Duck'
AND c.customerID = t.customerID
AND t.theaterID = s.theaterID
AND t.showingDate = s.showingDate
AND t.startTime = s.startTime
AND s.movieID = m.movieID;
