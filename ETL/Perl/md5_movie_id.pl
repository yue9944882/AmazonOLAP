
use Data::Dumper;
#use diagnostics;
#use warnings;
#use strict;

use Digest::MD5 qw/md5 md5_hex/;
use DBI;

# Testing for a md5 digest function

@ARGV=("C:/Users/guguli/Desktop/DataHouse/records.json");

# 24 Bits Types Mask


my $cnt=0;

my %movie_hash;
my %actor_hash;
my %director_hash;


my %typehash=(
	'Comedy'=>				0x000001,
	'Kids & Family'=>		0x000002,
	'Science Fiction'=>		0x000004,
	'Drama'=>					0x000008,
	'Thriller'=>				0x000010,
	'Mystery'=>				0x000020,
	'Horror'=>				0x000040,
	'Adventure'=>			0x000080,
	'Action'=>					0x000100,
	'Documentary'=>		0x000200,
	'Fantasy'=>				0x000400,
	'Military & War'=>		0x000800,
	'Western'=>				0x001000,
	'Romance'=>				0x002000,
	'International'=>		0x004000,
	'Music'=>					0x008000,
	'Reality TV'=>			0x010000,
	'Musical'=>				0x020000,
	'Sports'=>					0x040000,
	'Gay & Lesbian'=>		0x080000,
	'TV Game Shows'=>	0x100000,
	'TV Talk Shows'=>		0x200000,
	'Other'=>					0x400000
);


my %monthhash=(
	'January'=>				1,
	'February'=>				2,
	'March'=>					3,
	'April'=>					4,
	'May'=>					5,
	'June'=>					6,
	'July'=>						7,
	'August'=>				8,
	'September'=>			9,
	'October'=>				10,
	'November'=>			11,
	'December'=>			12	
);

open TXT,">pid2md5mix.txt";

my $ddesc="DBI:mysql:dw:10.60.42.63:30000";
my $usr="root";
my $passwd="mysqlpassword";

my $dbh=DBI->connect($ddesc,$usr,$passwd,{RaiseError=>0,PrintError=>1}) or die "Cannot Open Database!";

my $movie_sql="insert into dwMovie(movieId,movieName,movieStyle,movieDuration,movieStudio,movieTime,movieMPAA) values (?,?,?,?,?,?,?)";
my $actor_sql="insert into dwActor(actorId,actorName) values (?,?)";
my $director_sql="insert into dwDirector(directorId,directorName) values (?,?)";
my $rsupport_sql="insert into dwSupportRelation(movieId,supportId) values (?,?)";
my $rstar_sql="insert into dwStarRelation(movieId,starId) values (?,?)";
my $rdirect_sql="insert into dwDirectorRelation(movieId,directorId) values (?,?)";

my $movie_sqlh=$dbh->prepare($movie_sql);
my $actor_sqlh=$dbh->prepare($actor_sql);
my $director_sqlh=$dbh->prepare($director_sql);
my $rsupport_sqlh=$dbh->prepare($rsupport_sql);
my $rdirect_sqlh=$dbh->prepare($rdirect_sql);
my $rstar_sqlh=$dbh->prepare($rstar_sql);	

while(<>){
	
	my $movieType=0;
	my $movieTitle;
	my $primeKey;
	my $movieRuntime=0;
	my $movieStudio=0;
	my $movieMPAA="n/a";
	my $movieTime="n/a";
	my $pid;
	my $directorName="n/a";
	my $actorName="n/a";
	
	
	if(/"movie_productId": "(.*?)"/){
		$pid=$1;
	}
	
	if(/"movie_genres": "(.*?)"/){
		# Movie Style	
		my @types=split /\|/,$1;
		foreach (@types){
			my $type_mask=$typehash{$_};
			if($type_mask){
				$movieType=$movieType|$type_mask;						
			}
		}
	}
	
	if(/"movie_title": "(.*?)"/){		
		# Movie Title
		my $prefix=int(rand(1000));
		$primeKey=$prefix.'-'.md5_hex($1);
		$movieTitle=$1;		
	}
	
	if(/"movie_runtime": "(.*?)"/){		
		# Movie Runtime
		my $tmp=$1;
		if($tmp=~/(\d*?) hours? (\d*?) minutes/){
			$movieRuntime=$1*60+$2;	
		}elsif($tmp=~/(\d*?) minutes/){
			$movieRuntime=$1;
		}
	}
	
	if(/"movie_studio": "(.*?)"/){		
		# Movie Studio
		$movieStudio=$1;
	}
	
	if(/"movie_MPAA": "(.*?)"/){		
		# Movie MPAA
		$movieMPAA=$1;	
	}
	
	if(/"movie_release_time": "(.*?)"/){		
		# Movie Release
		my $tmpTime=$1;
		if($tmpTime=~/(\w*?) (\d*?), (\d{4})/){
			$movieTime=sprintf('%4d-%02d-%02d',$3,$monthhash{$1},$2);
		}
	}
	
	
	#Do inserts here into the movie table
	print "\rMOVIE NUMBER: $cnt";
	$cnt++;
	
	if($movie_hash{$movieTitle}){
		$primeKey=$movie_hash{$movieTitle};
	}else{
		$movie_sqlh->execute($primeKey,$movieTitle,$movieType,$movieRuntime,$movieStudio,$movieTime,$movieMPAA);
		$movie_hash{$movieTitle}=$primeKey;
	}
#	print "======================\n";
#	print "NAME:\t\t".$movieTitle."\n";
#	print "TYPE:\t\t". $movieType."\n";
#	print "DURATION:\t\t".$movieRuntime."\n";
#	print "RELEASE:\t\t".$movieTime."\n";
#	print "STUDIO:\t\t".$movieStudio."\n";
#	print "MPAA:\t\t".$movieMPAA."\n";
#	print "--- PRIME KEY ---  ".$primeKey."\n";
	
	if(/"movie_directors": "(.*?)"/){		
		# Movie Directors
		my @directors=split /\|/,$1;
		foreach (@directors){
			# Do inserts here into director table
			my $prefix=int(rand(1000));
			my $md5key=$prefix.'-'.md5_hex($_);
			$directorName=$_;	
			if($director_hash{$directorName}){
				;
			}else{
				$director_sqlh->execute($md5key,$directorName);	
				$director_hash{$directorName}=$md5key;
			}
			# Do inserts here into relation table
			$rdirect_sqlh->execute($primeKey,$director_hash{$directorName});
		}
	}
	
	if(/"movie_stars": "(.*?)"/){		
		# Movie Stars
		my @stars=split /\|/,$1;
		foreach (@stars){
			#Do inserts here into the actor table
			my $prefix=int(rand(1000));
			my $md5key=$prefix.'-'.md5_hex($_);
			$actorName=$_;
			if($actor_hash{$actorName}){
				;
			}else{
				$actor_sqlh->execute($md5key,$actorName);	
				$actor_hash{$actorName}=$md5key;
			}
			#Do inserts here into the star relation table
			$rstar_sqlh->execute($primeKey,$actor_hash{$actorName});
		}
	}
	
	if(/"movie_supporting_actors": "(.*?)"/){		
		# Movie Supporting Actors
		my @sups=split /\|/,$1;
		foreach (@sups){
			#Do inserts here into the actor table
			my $prefix=int(rand(1000));
			my $md5key=$prefix.'-'.md5_hex($_);
			$actorName=$_;
			if($actor_hash{$actorName}){
				;
			}else{
				$actor_sqlh->execute($md5key,$actorName);	
				$actor_hash{$actorName}=$md5key;
			}
			# Do inserts here into the support relation table
			$rsupport_sqlh->execute($primeKey,$actor_hash{$actorName});
		}
	}
	
	print TXT $pid."|".$primeKey."\n";
}

close TXT;









