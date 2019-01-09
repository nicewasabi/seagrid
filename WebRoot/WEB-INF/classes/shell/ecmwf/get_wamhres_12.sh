#!/bin/bash
# Filename: get_wamhres_12.sh
# Description: Process wamhres 12 data
# Date: 2017-11-21
# Author: songwj
# Version: v4
source /etc/profile

#pidList=$(ps aux | grep /space/data2/ecData/bin/get_wamhres_12.sh | grep -v grep | awk '{print $2}')
#[[ ! -z $pidList ]]
#if [ $? -eq 0 ]; then
#	echo "[$(date +'%F %T')] >>> Process [/space/data2/ecData/bin/get_wamhres_12.sh] is running, no need to process it again."
#	exit 0
#fi

# Get datetime, date format: yyyymmdd
datetime=$(date +%Y%m%d -d "-1day")
hour=12

fileRootPath=/space/data2/ecData/gribdata/WAMHRES/$datetime/$hour
mkdir -p $fileRootPath

echo "[$(date +'%F %T')] >>> Start to download data from: /NAFP/ECMWF/WAMHRES/$datetime/$hour."
ftp -n << !
open 10.1.72.41
user pub_nwp nafp8nmic
binary
cd /NAFP/ECMWF/WAMHRES/$datetime/$hour
lcd $fileRootPath
prompt
mget *
close
bye
!
echo "[$(date +'%F %T')] >>> Download complete."

echo "===========================$datetime$hour==========================="
cd $fileRootPath
allShortName=swh_shww
for filename in *; do
	/space/data2/ecData/bin/gribProcess.sh $fileRootPath/$filename WAMHRES $datetime $hour $allShortName
done

# Remove source file 8 days ago
datetime=$(date +%Y%m%d -d "-8days")
srcFilePath=/space/data2/ecData/gribdata/WAMHRES/$datetime

if [ -d "$srcFilePath" ]; then
	rm -rf "$srcFilePath"
	echo "[$(date +'%F %T')] >>> Source files [$srcFilePath] remove successfully!!!"
fi

# Remove process file 8 days ago
datetime=$(echo $datetime | cut -c 3-8)
for shortName in $(echo ${allShortName} | sed 's/_/\n/g'); do
	srcFilePath=/space/data2/ecData/dsmicaps/$shortName/$datetime??.???
	rm -rf $srcFilePath
	echo "[$(date +'%F %T')] >>> Process files [$srcFilePath] remove successfully!!!"
done

# Delete 152 Server files 8 days ago
for endStep in $(seq 0 3 72) $(seq 78 6 240); do
	if [ $endStep -lt 10 ]; then
		endStep=00$endStep
	elif [ $endStep -lt 100 ]; then
		endStep=0$endStep
	fi
	
	fName="$fName ${datetime}20.$endStep" # 18051508.024 18051508.027 ...
done

ftpRootPath=/data/model_data_0.1/ECMWF

for shortName in $(echo ${allShortName} | sed 's/_/\n/g'); do
ftp -i -v -n<<!
open 10.28.17.152
user ecdata ecdata
binary
cd $ftpRootPath/$shortName
mdelete $fName 
close
bye
!
done