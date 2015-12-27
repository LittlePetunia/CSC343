let $ppl := fn:doc("people.xml")
let $co := count($ppl//Oscar/@OID)
let $cp := count($ppl/People/Person)
return $co div $cp
