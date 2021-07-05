main :-
read(X),
read(Y),
fly(X, Y).   

:- use_module(library(lists)).

:- style_check(-discontiguous).
:- style_check(-singleton).
airport( atl, 'Atlanta         ', degmin(  33,39 ), degmin(  84,25 ) ).
airport( bos, 'Boston-Logan    ', degmin(  42,22 ), degmin(  71, 2 ) ).
airport( chi, 'Chicago         ', degmin(  42, 0 ), degmin(  87,53 ) ).
airport( den, 'Denver-Stapleton', degmin(  39,45 ), degmin( 104,52 ) ).
airport( dfw, 'Dallas-Ft.Worth ', degmin(  32,54 ), degmin(  97, 2 ) ).
airport( lax, 'Los Angeles     ', degmin(  33,57 ), degmin( 118,24 ) ).
airport( mia, 'Miami           ', degmin(  25,49 ), degmin(  80,17 ) ).
airport( nyc, 'New York City   ', degmin(  40,46 ), degmin(  73,59 ) ).
airport( sea, 'Seattle-Tacoma  ', degmin(  47,27 ), degmin( 122,17 ) ).
airport( sfo, 'San Francisco   ', degmin(  37,37 ), degmin( 122,23 ) ).
airport( sjc, 'San Jose        ', degmin(  37,22 ), degmin( 121,56 ) ).

flight( bos, nyc, time( 7,30 ) ).
flight( dfw, den, time( 8, 0 ) ).
flight( atl, lax, time( 8,30 ) ).
flight( chi, den, time( 8,45 ) ).
flight( mia, atl, time( 9, 0 ) ).
flight( sfo, lax, time( 9, 0 ) ).
flight( sea, den, time( 10, 0 ) ).
flight( nyc, chi, time( 11, 0 ) ).
flight( sea, lax, time( 11, 0 ) ).
flight( den, dfw, time( 11,15 ) ).
flight( sjc, lax, time( 11,15 ) ).
flight( atl, lax, time( 11,30 ) ).
flight( atl, mia, time( 11,30 ) ).
flight( chi, nyc, time( 12, 0 ) ).
flight( lax, atl, time( 12, 0 ) ).
flight( lax, sfo, time( 12, 0 ) ).
flight( lax, sjc, time( 12, 15 ) ).
flight( nyc, bos, time( 12,15 ) ).
flight( bos, nyc, time( 12,30 ) ).
flight( den, chi, time( 12,30 ) ).
flight( dfw, den, time( 12,30 ) ).
flight( mia, atl, time( 13, 0 ) ).
flight( sjc, lax, time( 13,15 ) ).
flight( lax, sea, time( 13,30 ) ).
flight( chi, den, time( 14, 0 ) ).
flight( lax, nyc, time( 14, 0 ) ).
flight( sfo, lax, time( 14, 0 ) ).
flight( atl, lax, time( 14,30 ) ).
flight( lax, atl, time( 15, 0 ) ).
flight( nyc, chi, time( 15, 0 ) ).
flight( nyc, lax, time( 15, 0 ) ).
flight( den, dfw, time( 15,15 ) ).
flight( lax, sjc, time( 15,30 ) ).
flight( chi, nyc, time( 18, 0 ) ).
flight( lax, atl, time( 18, 0 ) ).
flight( lax, sfo, time( 18, 0 ) ).
flight( nyc, bos, time( 18, 0 ) ).
flight( sfo, lax, time( 18, 0 ) ).
flight( sjc, lax, time( 18,15 ) ).
flight( atl, mia, time( 18,30 ) ).
flight( den, chi, time( 18,30 ) ).
flight( lax, sjc, time( 19,30 ) ).
flight( lax, sfo, time( 20, 0 ) ).
flight( lax, sea, time( 22,30 ) ).


calcDistance(Airport1, Airport2, Distance) :-
	airport(Airport1, _, degmin(LatDeg1, LatMin1), degmin(LongDeg1, LongMin1)),
	airport(Airport2, _, degmin(LatDeg2, LatMin2), degmin(LongDeg2, LongMin2)),
	radians(LatDeg1, LatMin1, LatRad1),
	radians(LongDeg1, LongMin1, LongRad1),
	radians(LatDeg2, LatMin2, LatRad2),
	radians(LongDeg2, LongMin2, LongRad2),
	A is (0.5 - cos((LatRad2 - LatRad1)) / 2 + cos(LatRad1) * cos(LatRad2) * (1 - cos((LongRad2 - LongRad1))) / 2), 
	Distance is (12742 * asin(sqrt(A))*0.621371).

radians(Deg,Min,Rad) :- Rad is ((Deg + Min/60) * (pi/180)).

calcArrivalTime(Airport1, Airport2, time(DepartHour, DepartMin), time(ArrivalHour, ArrivalMin)) :-
	Depart is DepartHour + (DepartMin / 60),
	calcDistance(Airport1, Airport2, Dist),
	Arrival is ((Dist / 500) + Depart),
	toHourAndMin(Arrival, ArrivalHour, ArrivalMin).

toHourAndMin(Time, Hour, Min) :-
Hour is floor(Time),
Min is round((Time - Hour) * 60).


toHour(Hour, Min, Time) :-
Time is (Hour+ Min/60).

isEarlier(time(AH1, AM1), time(DH2, DM2)):-
toHour(AH1, AM1, Time1),
toHour(DH2, DM2, Time2),
((Time1 + 0.5) < Time2;  
(Time1 + 0.5) == Time2).

print_trip( Action, Code, Name, time( Hour, Minute)) :-
   upcase_atom( Code, Upper_code),
   format( "~6s  ~3s  ~s~26|  ~`0t~d~30|:~`0t~d~33|",
           [Action, Upper_code, Name, Hour, Minute]),
   nl.

print_all([]).
print_all([A, B, C, D|T]) :-
	airport(A, Name1, _, _),
 	airport(C, Name2, _, _),
	print_trip('depart', A, Name1, B),
	print_trip('arrive', C, Name2, D),
	print_all(T).

fly(Start, Destination) :-
connExists3(Start, Destination, time(0, 0), [], [Start]).

connExists3(Start, Destination, time(ArrivalHour, ArrivalMin), StopList, [Start|Rest]):-
 	Start \== Destination,
 	flight(Start, Destination, time(DHour, DMin)),
 	isEarlier(time(ArrivalHour, ArrivalMin), time(DHour, DMin)),
 	calcArrivalTime(Start, Destination, time(DHour, DMin), time(AHour, AMin)),
 	append(StopList, [Start, time(DHour, DMin), Destination, time(AHour, AMin)], StopListX),
 	print_all(StopListX).
connExists3(Start,Destination, time(ArrivalHour, ArrivalMin), StopList, Visited):-
 	Start \== Destination,
	flight(Start, X, time(DHour, DMin)),
	not(member(X,Visited)),
	X \== Destination,
	append([X], Visited, VisitedX),
	isEarlier(time(ArrivalHour, ArrivalMin), time(DHour, DMin)),
	calcArrivalTime(Start, X, time(DHour, DMin), time(TargetHour, TargetMin)),
	append(StopList, [Start, time(DHour, DMin), X, time(TargetHour, TargetMin)], StopListX),
	connExists3(X,Destination, time(TargetHour, TargetMin), StopListX, VisitedX).
