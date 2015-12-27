<Stats>
	{for $category in distinct-values(fn:doc("movies.xml")/Movies/Movie/Genre/Category)
	 let $count := count(fn:doc("movies.xml")/Movies/Movie/Genre[Category=$category])
	 return (<Bar category="{$category}" count="{$count}" />)
	}
</Stats>
