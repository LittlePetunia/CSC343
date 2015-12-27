SET search_path TO artistdb;

select label_name as record_lable,year,sum(sales) as total_sales
from Album,RecordLabel,ProducedBy
group by label_name,year,RecordLabel.label_id,ProducedBy.album_id,ProducedBy.label_id,Album.album_id
having RecordLabel.label_id = ProducedBy.label_id and
      ProducedBy.album_id = Album.album_id 
order by label_name,year;

