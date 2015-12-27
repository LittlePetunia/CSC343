let $nopob := fn:doc("people.xml")/People/Person[not(exists(@pob))]
let $director := fn:doc("movies.xml")/Movies/Movie/Director
for $p in $nopob
where $p/@PID = ($director/@PID)
return (string($p/@PID), string($p/Name/Last))
