# Het Downloaden van het gezipte variant_summary.txt, naar variabele $1 een parameter die de locatie meegeeft waar het bestand moet komen te staan voor het downloaden, en waar het staat voor het unzippen.
wget -q --limit-rate=9000k -O $1 ftp://ftp.ncbi.nlm.nih.gov/pub/clinvar/tab_delimited/variant_summary.txt.gz;
gunzip -k $1


