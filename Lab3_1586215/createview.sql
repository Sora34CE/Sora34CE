DROP VIEW earningsView CASCADE;

CREATE VIEW earningsView AS
SELECT DISTINCT s.movieID, CASE
	WHEN SUM(t.ticketPrice) IS NOT NULL THEN SUM(t.ticketPrice)
	ELSE 0
	END AS computedEarnings
FROM Showings s LEFT OUTER JOIN Tickets t
ON((s.theaterID, s.showingDate, s.startTime) = (t.theaterID, t.showingDate, t.startTime))
GROUP BY s.movieID
HAVING s.movieID IS NOT NULL;
