SELECT m1.movieID, m1.name, m1.year, m1.length
FROM Movies m1, Movies m2
WHERE m2.name = 'Avengers'
AND m2.year = 2011
AND m1.length > m2.length
ORDER BY year DESC, m2.name;
