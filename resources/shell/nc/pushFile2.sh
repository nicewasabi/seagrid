#!/bin/bash
# Filename: pushFile2.sh
# Description: FTP push file
# Date: 2018-07-25
# Author: songwj
# Version: v1.0

datebeg="2018-06-15 08:00:00" # "2018-06-15 08:00:00"
dateend="2018-06-30 20:00:00" # "2018-06-30 20:00:00"
beg_s=`date -d "$datebeg" +%s`
end_s=`date -d "$dateend" +%s`

srcRootPath=/root/test/SHOUQI

while [ "$beg_s" -le "$end_s" ]; do
	datetime=$(date -d @$beg_s +"%Y%m%d%H") # 2018061508
	beg_s=$((beg_s+43200))
	path=$srcRootPath/$datetime/
	
	cd $path
	for srcFilePath in *; do
		suffix=${srcFilePath##*.} # json
		if [ ${suffix} == "json" ]; then
ftp -i -v -n<<!	
open 47.92.112.104
user envser servi09P
passive
prompt
mkdir /SHOUQI/${datetime}
put $path$srcFilePath /SHOUQI/${datetime}/$srcFilePath
close
bye
!
		fi
	done	
done	

