#!/bin/bash
# Filename: nc2JsonProcess.sh
# Description: Process NetCDF file and convert to JSON
# Date: 2018-07-19
# Author: songwj
# Version: v1.0

datebeg="2018-06-15 08:00:00" # "2018-06-15 08:00:00"
dateend="2018-06-30 20:00:00" # "2018-06-30 20:00:00"
beg_s=`date -d "$datebeg" +%s`
end_s=`date -d "$dateend" +%s`
rootPath=/root/test/SHOUQI

while [ "$beg_s" -le "$end_s" ]; do
	datetime=$(date -d @$beg_s +"%Y%m%d%H")
	beg_s=$((beg_s+43200))
	path=/root/test/SHOUQI/$datetime
	mkdir -p $path	

status1=$(
ftp -n << !
open 10.28.20.121
user cuace dust123
binary
cd /SHOUQI/$dir
lcd $path
prompt
mget *
close
bye
!
)

	python $rootPath/bin/nc2json.py $rootPath/$datetime/AQI_Grid_$datetime.nc $datetime
	outputFile=$rootPath/$datetime/AQI_Grid_$datetime.json

status2=$(
ftp -i -v -n<<!	
open 10.28.20.121
user cuace dust123
passive
prompt
put $outputFile /SHOUQI/$datetime/AQI_Grid_$datetime.json
close
bye
!
)
	
done