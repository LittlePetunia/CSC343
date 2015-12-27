SET search_path TO artistdb;

create view HaveColla as
select BelongsToAlbum.album_id,title,artist_id,genre_id,year,sales
from Collaboration,BelongsToAlbum,Album
where Collaboration.song_id=BelongsToAlbum.song_id and 
      BelongsToAlbum.album_id = Album.album_id;

create view NotHave as
select album_id,title,artist_id,genre_id,year,sales
from Album 
where album_id not in (select album_id from HaveColla);

select distinct name as artists, avg(havecolla.sales) as avg_collab_sales
from HaveColla,NotHave,Artist
group by havecolla.artist_id,nothave.sales,nothave.artist_id,Artist.artist_id
having havecolla.artist_id=nothave.artist_id and havecolla.artist_id=Artist.artist_id and
       avg(havecolla.sales)>nothave.sales;
       
Drop view NotHave Cascade;
Drop view HaveColla Cascade;
       
       
       

