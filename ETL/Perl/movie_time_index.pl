
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


my $ddesc="DBI:mysql:dw:10.60.42.63:30000";
my $usr="root";
my $passwd="mysqlpassword";

my $dbh=DBI->connect($ddesc,$usr,$passwd,{RaiseError=>0,PrintError=>1}) or die "Cannot Open Database!";




$dbh->disconnect();









