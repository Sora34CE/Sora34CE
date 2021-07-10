SELECT m.rating, COUNT(ev.computedEarnings) as misreportCount
FROM earningsView ev, Movies m
WHERE ev.movieID = m.movieID
AND m.totalEarned <> ev.computedEarnings
GROUP BY m.rating
HAVING COUNT(m.year) < 2019;

-- Write the results of the query
-- rating | misreportcount 
----------+----------------
-- R      |              3
-- P      |              1
-- G      |              1
-- (3 rows)
DELETE FROM Tickets
	WHERE (Tickets.theaterID = 111 AND Tickets.seatNum = 1 AND Tickets.showingDate = DATE'2009-06-23' AND Tickets.startTime = TIME'11:00:00')
	OR (Tickets.theaterID = 444 AND Tickets.seatNum = 5 AND Tickets.showingDate = DATE'2020-06-24' AND Tickets.startTime = TIME'15:00:00');
-- rating | misreportcount 
----------+----------------
-- R      |              3
-- P      |              2
-- G      |              1
--(3 rows)
