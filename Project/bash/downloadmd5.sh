# Downloaden van het md5 bestand. Naar $1, een variabele die meegeven wordt bij het aanroepen van dit bash-script.
wget -q --limit-rate=10000k -O $1 ftp://ftp.ncbi.nlm.nih.gov/pub/clinvar/tab_delimited/variant_summary.txt.gz.md5

