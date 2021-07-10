CREATE OR REPLACE FUNCTION reduceSomeTicketPricesFunction(maxTicketCount INTEGER)

RETURNS INTEGER AS $ticketCount$
DECLARE
	maxCounter INTEGER := 0;
	priceReduction INTEGER;
	totalCount INTEGER;
	tID INTEGER;
	sD DATE;
	sT TIME;
	pC CHAR(1);
	tP NUMERIC(4, 2);
	ticketCursor1 CURSOR FOR
		SELECT t.theaterID, t.showingDate, t.startTime, s.priceCode, t.ticketPrice
		FROM Tickets t, Showings s
                WHERE t.theaterID = s.theaterID
                AND t.showingDate = s.showingDate
                AND t.startTime = s.startTime
		AND s.priceCode IS NOT NULL
		AND t.ticketPrice IS NOT NULL
		AND s.priceCode = 'A'
                ORDER BY t.customerID;
	ticketCursor2 CURSOR FOR
                SELECT t.theaterID, t.showingDate, t.startTime, s.priceCode, t.ticketPrice
                FROM Tickets t, Showings s
                WHERE t.theaterID = s.theaterID
                AND t.showingDate = s.showingDate
                AND t.startTime = s.startTime
                AND s.priceCode IS NOT NULL
                AND t.ticketPrice IS NOT NULL
                AND s.priceCode = 'B'
                ORDER BY t.customerID;
	ticketCursor3 CURSOR FOR
                SELECT t.theaterID, t.showingDate, t.startTime, s.priceCode, t.ticketPrice
                FROM Tickets t, Showings s
                WHERE t.theaterID = s.theaterID
                AND t.showingDate = s.showingDate
                AND t.startTime = s.startTime
                AND s.priceCode IS NOT NULL
                AND t.ticketPrice IS NOT NULL
                AND s.priceCode = 'C'
                ORDER BY t.customerID;
BEGIN
	OPEN ticketCursor1;
		LOOP
			FETCH ticketCursor1 INTO tID, sD, sT, pC, tP;
			EXIT WHEN NOT FOUND OR totalCount = maxTicketCount;
			UPDATE Tickets 
				SET ticketPrice = tP - 3
				WHERE theaterID = tID
				AND showingDate = sD
				AND startTime = sT;
			maxCounter = maxCounter + 3;
			totalCount = totalCount + 1;
			priceReduction = priceReduction + 3;
		END LOOP;
	CLOSE ticketCursor1;
	OPEN ticketCursor2;
                LOOP
                        FETCH ticketCursor2 INTO tID, sD, sT, pC, tP;
                        EXIT WHEN NOT FOUND OR totalCount = maxTicketCount;
                        UPDATE Tickets 
				SET ticketPrice = tP - 2
				WHERE theaterID = tID
                                AND showingDate = sD
                                AND startTime = sT;
			maxCounter = maxCounter + 2;
                        totalCount = totalCount + 1;
                        priceReduction = priceReduction + 2;
                END LOOP;
        CLOSE ticketCursor2;
	OPEN ticketCursor3;
                LOOP
                        FETCH ticketCursor3 INTO tID, sD, sT, pc, tp;
                        EXIT WHEN NOT FOUND OR totalCount = maxTicketCount;
                        UPDATE Tickets 
				SET ticketPrice = tP - 1
				WHERE theaterID = tID
                                AND showingDate = sD
                                AND startTime = sT;
			maxCounter = maxCounter + 1;
                        totalCount = totalCount + 1;
                        priceReduction = priceReduction + 1;
                END LOOP;
        CLOSE ticketCursor3;
	RETURN maxCounter;
END;
$ticketCount$ LANGUAGE plpgsql;
