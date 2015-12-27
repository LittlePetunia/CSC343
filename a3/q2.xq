let $movies := fn:doc("movies.xml")
for $m in $movies/Movies/Movie
return ( string($m/@MID), count($m/Actors/Actor))
