let $CaPID := doc("people.xml")/People/Person[Name/First="James" and Name/Last="Cameron"]/@PID
let $Movies := doc("movies.xml")/Movies/Movie[@year >= 2001]
for $m in $Movies
where $m/Director/@PID = $CaPID
return (string($m/Title), string($m/@year))
