SET search_path TO artistdb;

create view YueduiID as
select artist_id
from Artist
where Artist.name='AC/DC';

insert into WasInBand(artist_id, band_id, start_year, end_year)
(select artist_id, band_id, 2014, 2015
from WasInBand
where band_id=(select * from YueduiID));

Drop view yueduiID;

