SELECT DISTINCT t.theaterID, t.address
FROM Theaters t, TheaterSeats ts
WHERE t.theaterID = ts.theaterID
AND ts.brokenSeat = TRUE;
