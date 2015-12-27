let $Oscar := doc("oscars.xml")
let $Movies := doc("movies.xml")/Movies/Movie
for $type in distinct-values($Oscar/Oscars/Oscar/Type)
let $Minyear := min($Oscar/Oscars/Oscar[Type=$type]/@year)
let $minOID := $Oscar/Oscars/Oscar[@year=$Minyear]/@OID
for $m in $Movies
where $m/Oscar/@OID = $minOID
return (string($type), $Minyear, string($m/Title))
