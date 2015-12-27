SET search_path TO artistdb;

create view NB as
select distinct Album.album_id 
from Album,Song,BelongsToAlbum
where Album.album_id=BelongsToAlbum.album_id
and BelongsToAlbum.song_id=song.song_id
and Song.songwriter_id != Album.artist_id;

select distinct name as artist_name, title as album_name
from Artist,Album,NB
where Artist.artist_id=Album.artist_id and 
      Album.album_id not in (select distinct album_id from NB)
order by name,title;
Drop view NB;
