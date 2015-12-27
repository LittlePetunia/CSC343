SET search_path TO artistdb;

create view CoAlb as
select distinct title,B1.song_id ,B1.album_id as a1,B2.album_id as a2
from BelongsToAlbum as B1,BelongsToAlbum as B2,Song
where B1.song_id=B2.song_id and B1.album_id!=B2.album_id and
      B1.song_id=Song.song_id; 

create view Con as
select CoAlb.title,C1.name as n1, C2.name as n2,C1.artist_id as I1,C2.artist_id as I2
from Album as AL1,Album as AL2, Artist as C1,Artist as C2,CoAlb
where CoAlb.A1=AL1.album_id and
      CoAlb.A2=AL2.album_id and
      AL1.artist_id=C1.artist_id and
      AL2.artist_id=C2.artist_id;
 

select distinct con.title as song_name,year,n1 as artist_name
from con,artist,album,coalb
where con.i1=artist.artist_id and
      artist.artist_id=album.artist_id and
      album.album_id=coalb.a1
order by con.title,year,n1;

Drop view Con Cascade;
Drop view CoAlb Cascade;
