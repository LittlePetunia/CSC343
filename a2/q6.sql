SET search_path TO artistdb;

create view Cani as
select distinct Artist.artist_id,name
from Artist
where nationality='Canada';

create view Canial as
select Cani.artist_id,Cani.name,album_id,year
from Cani,Album
where Cani.artist_id=Album.artist_id;

create view Minyear as
select artist_id,min(year)
from Canial
group by artist_id;

create view minresult as
select Minyear.artist_id,name,album_id,Minyear.min
from Minyear natural join Canial;

create view chujuanji as
select artist_id,minresult.album_id,name
from minresult
where minresult.album_id not in (select album_id from ProducedBy);

select distinct name as artist_name
from chujuanji,Album,ProducedBy,RecordLabel
where chujuanji.artist_id=Album.artist_id and
      Album.album_id=ProducedBy.album_id and
      ProducedBy.label_id=RecordLabel.label_id and
      RecordLabel.country ='America'
order by name;

Drop view chujuanji Cascade;
Drop view minresult Cascade;
Drop view Minyear Cascade;
Drop view Canial Cascade;
Drop view Cani Cascade;


