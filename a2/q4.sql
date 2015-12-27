SET search_path TO artistdb;
create view S as
select distinct name as name,role,genre_id
from Role,Artist,Album
where Role.artist_id=Artist.artist_id and
      Artist.artist_id=Album.artist_id and
      role!='Songwriter'
order by name;

create view Songwriter as
select distinct name,role,genre_id
from Role,Artist,Album,BelongsToAlbum,Song
where Song.song_id=BelongsToAlbum.song_id and
      BelongsToAlbum.album_id=Album.album_id and
      Song.songwriter_id=Artist.artist_id and
      Artist.artist_id=Role.artist_id and
      role='Songwriter' ;

create view result as
(select name as artist,role as capacity,count(name) as genres
from S
group by name,role
having count(name)>=3 order by name)
UNION
(select name as artist,role as capacity,count(name) as genres
from Songwriter
group by name,role
having count(name)>=3);


select * from result
order by CASE capacity when 'Musician' Then 1
                       when 'Band' Then 2
                       when 'Songwriter' Then 3
                       else 4
                       end, genres DESC, artist ASC;
                       
Drop view S Cascade;
Drop view Songwriter Cascade;

