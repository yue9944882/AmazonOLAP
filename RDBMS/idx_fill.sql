
use dw;

# select  movieTime into dwTimeIndex(); 

# insert into dwTimeIndex(timeRowkey) 
# select concat((round(rand()*999),3,'0'),"-",movieTime,"-",movieId) from dwMovie limit 0,10;

#insert into dwTimeIndex(timeRowkey) select concat(movieTime,"-",movieId) from dwMovie where movieTime != "n/a"; #limit 0,10;


# lpad(round(rand()*999),3,"0"),"-"


# insert into dwDimActor(dimActorId,dimActorName) select actorId,actorName from dwActor where actorName!="n/a";

# insert into dwDimDirector(dimDirectorId,dimDirectorName) select directorId,directorName from dwDirector where directorName!="n/a";

# insert into dwDimStarBridge(dimSBAId) select dimActorId from dwDimActor;

# insert into dwDimMPAA(mpaaName) select distinct(movieMPAA) from dwMovie where movieMPAA!="n/a";

# update dwDimActor set dimActorName=trim(dimActorName) where right(dimActorName,1)=" ";

#insert into dwDimDate(dimDateId,dimDateYear,dimDateMonth,dimDateDay) values ("n/a",0,0,0);


# select CONVERT("123213",SIGNED);

# insert into dwDimDate(dimDateId,dimDateYear,dimDateMonth,dimDateDay) select distinct(movieTime),CONVERT(substring(movieTime,1,4),SIGNED),CONVERT(substring(movieTime,6,2),SIGNED),CONVERT(substring(movieTime,9,2),SIGNED) from dwMovie where movieTime!="n/a"; 

#insert into dwDimDirectorBridge(dimDBDId) select dwDimDirector.dimDirectorId from dwDimDirector;

#insert into dwDimStarBridge(dimSBAId) select dimActorId from dwDimActor;


insert into dwFactTable(factDimDate,factDimDirectorBridge,factDimStarBridge,factDimMPAA,factMovieRef,factDimStyle) 
select dwDimDate.dimDateId,dwDimDirectorBridge.dimDBId,dwDimStarBridge.dimSBId,dwDimMPAA.mpaaId,dwMovie.movieId,dwMovie.movieStyle
from dwMovie,dwDimDate,dwDimDirectorBridge,dwDirectorRelation,dwDimStarBridge,dwStarRelation,dwDimMPAA
where dwMovie.movieTime=dwDimDate.dimDateId 
and dwDirectorRelation.directorId=dwDimDirectorBridge.dimDBDId 
and dwStarRelation.starId=dwDimStarBridge.dimSBAId 
and dwDimMPAA.mpaaName=dwMovie.movieMPAA
and dwStarRelation.movieId=dwMovie.movieId
and dwDirectorRelation.movieId=dwMovie.movieId;

