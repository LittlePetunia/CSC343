SET search_path TO artistdb;
Select distinct name, nationality
From Artist natural join Role
Where role!='Band' and Extract(year From birthdate)=
(Select min(year) 
From Album natural join Artist
Where name = 'Steppenwolf')
Order by name;
