BEGIN TRANSACTION;
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
	UPDATE Showings s
	SET movieID = ms.movieID
	FROM ModifyShowings ms
        WHERE s.theaterID = ms.theaterID
        AND s.showingDate = ms.showingDate
        AND s.startTime = ms.startTime;

	INSERT INTO Showings(theaterID, showingDate, startTime, movieID, priceCode)
	SELECT ms.theaterID, ms.showingDate, ms.startTime, ms.movieID, NULL
	FROM ModifyShowings ms
	WHERE (theaterID, showingDate, startTime) NOT IN 
	(SELECT theaterID, showingDate, startTime 
	FROM Showings);
COMMIT;
