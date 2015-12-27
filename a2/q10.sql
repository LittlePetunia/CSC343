create view albumid as
select album_id
from Album
where title = 'Thriller';

create view songid as
select song_id
from BelongsToAlbum NATURAL JOIN id;

select * into sid from songid;
delete from ProducedBy where album_id in (select album_id from albumid);
delete from Collaboration where song_id in (select song_id from songid);
delete from BelongsToAlbumwhere song_id in (select song_id from sid);
delete from song where song_id in (select song_id from sid);                  
delete from Album where title ='Thriller';
