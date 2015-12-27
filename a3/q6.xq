let $actors := distinct-values(doc("movies.xml")/Movies/Movie/Actors/Actor/@PID)
let $directors := distinct-values(doc("movies.xml")/Movies/Movie/Director/@PID)
for $p in doc("people.xml")/People/Person[@PID=$actors]
where $p/@PID = $directors
return (string($p/@PID), string($p/Name/Last))
