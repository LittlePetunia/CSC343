SET search_path TO artistdb;

create view Mick as
select artist_id FROM Artist WHERE name='Mick Jagger';

create view Maroon as
select artist_id FROM Artist WHERE name='Maroon 5';

UPDATE WasInBand
SET end_year=2014
WHERE artist_id=(select artist_id from Artist where name='Adam Levine') or artist_id= (select * from Mick);

INSERT INTO WasInBand(artist_id, band_id, start_year, end_year)
VALUES ((select * from Mick),(select * from Maroon), 2014, 2015);

select * from WasInBand order by artist_id;
