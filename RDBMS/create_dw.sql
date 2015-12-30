use dw;

#drop table dwDirectorRelation;
#drop table dwStarRelation;
#drop table dwSupportRelation;
#drop table dwReview;
#drop table dwUser;
#drop table dwMovie;
#drop table dwDirector;
#drop table dwActor;


create table dwUser(
	userId char(14) not null primary key,
	userName varchar(64) not null
);


create table dwMovie(
	movieId char(36) not null primary key,
	movieName varchar(255) not null,
	movieStyle mediumint not null,
    movieDuration int, # By Minutes
	movieStudio varchar(50),
    movieTime char(10),
    movieMPAA varchar(100)
);


create table dwReview(
	reviewHelpfulness varchar(10) not null,
    reviewScore float not null,
    reviewTime int not null,
	reviewSummary varchar(255) not null,
    reviewText text not null,
    reviewUser char(14) not null,
    reviewMovie char(36) not null,
	foreign key(reviewUser) references dwUser(userId),
    foreign key(reviewMovie) references dwMovie(movieId)
);



create table dwDirector(
	directorId char(36) primary key,
    directorName varchar(64) not null
);

create table dwActor(
	actorId char(36) primary key,
	actorName varchar(100) not null
);

create table dwDirectorRelation(
	movieId char(36) not null,
    directorId char(36) not null,
	foreign key(movieId) references dwMovie(movieId),
    foreign key(directorId) references dwDirector(directorId)
);

create table dwStarRelation(  		# Star Actor 
	movieId char(36) not null,
    starId char(36) not null,
    foreign key(movieId) references dwMovie(movieId),
    foreign key(starId) references dwActor(actorId) 
);

create table dwSupportRelation( 	# Support Actor
	movieId char(36) not null,
    supportId char(36) not null,
    foreign key(movieId) references dwMovie(movieId),
    foreign key(supportId) references dwActor(actorId)
);









