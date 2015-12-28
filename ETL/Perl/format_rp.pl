


#@ARGV=("C:/Users/guguli/Desktop/DataHouse/movies.txt");


open (IN,"<C:/Users/guguli/Desktop/DataHouse/movies.txt") or die "Cannot Open File!";
open (RP,"<C:/Users/guguli/Desktop/DataHouse/pid2md5mix.txt") or die "Cannot Open File!";
open (OUT,">tmpmovie.txt");

my $cnt=0;

my %pid_hash;

print "Generating Hash","\n";

while($line=<RP>){
	chomp $line;
	my @str=split /\|/,$line;
	$pid_hash{$str[0]}=$str[1];
}



print "Generating File","\n";

my $flag=1;


while($line=<IN>){
	chomp $line;
	if($line=~/product\/productId: (.*)/){
		$pid=$1;
		my $md5=$pid_hash{$pid};
		if($md5){
			$flag=1;
			print OUT "product/productId: ".$md5,"\n";
			next;
		}else{
			$flag=undef;	
		}
	}	
	if($flag){
		print OUT $line,"\n";
	}else{
		next;
	}
	# $cnt++;
	# last if($cnt>100);	
}

print "Okay!","\n";

close IN;
close RP;
close OUT;