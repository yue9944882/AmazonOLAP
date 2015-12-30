
use dw;

# drop table dwTimeIndex;

create table dwTimeIndex(
    # 3 Salt - 10 Time - 36 MoviePrimary Key
	timeRowkey char(47) primary key not null,
    timeDummy bit
);


# Dimension Table

drop table dwFactTable;
#drop table dwDimDate;
create table dwDimDate(
	dimDateId char(10) primary key not null,
    dimDateYear smallint not null,
    dimDateMonth tinyint not null,
    dimDateDay tinyint not null
);

create table dwDimDirector(
	dimDirectorId varchar(36) not null primary key,
    dimDirectorName varchar(64) not null
);

create table dwDimDirectorBridge(
	dimDBId int auto_increment not null primary key,
    dimDBDId varchar(36) not null,
    foreign key(dimDBDId) references dwDimDirector(dimDirectorId)
);


create table dwDimActor(
	dimActorId varchar(36) not null primary key,
    dimActorName varchar(100) not null
);

create table dwDimStarBridge(
	dimSBId int auto_increment not null primary key,
    dimSBAId varchar(36) not null,
	foreign key(dimSBAId) references dwDimActor(dimActorId)
);

create table dwDimSupportBridge(
	dimSBId int auto_increment not null primary key,
    dimSBAId varchar(36) not null,
	foreign key(dimSBAId) references dwDimActor(dimActorId)
);

create table dwDimStudio(
	studioId int auto_increment primary key,
    studioName varchar(50) not null
);

#drop table dwDimMPAA;

create table dwDimMPAA(
	mpaaId int auto_increment primary key,
    mpaaName varchar(100) not null
);

# Fact Table
# 

create table dwFactTable(
	factDimDate char(10) not null,
	#factDimStudio int not null,
	factDimMPAA int not null,
    factDimDirectorBridge int not null,
    factDimStarBridge int not null,
    #factDimSupportBridge int not null,
    factDimStyle mediumint not null,
	factMovieRef char(36) not null,
	foreign key(factMovieRef) references dwMovie(movieId),
    foreign key(factDimDate) references dwDimDate(dimDateId),
	#foreign key(factDimStudio) references dwDimStudio(studioId),
	foreign key(factDimMPAA) references dwDimMPAA(mpaaId),
    foreign key(factDimDirectorBridge) references dwDimDirectorBridge(dimDBId),
    foreign key(factDimStarBridge) references dwDimStarBridge(dimSBId)
	#foreign key(factDimSupportBridge) references dwDimSupportBridge(dimSBId)
);
 

#create table dwStyleIndex(
	# Salt - 
#	
#);


