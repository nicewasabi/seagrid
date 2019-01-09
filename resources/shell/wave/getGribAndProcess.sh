#!/bin/bash
# Filename: getGribAndProcess.sh
# Description: Get and process wave 00,06,12,18 hour data
# Date: 2018-09-27
# Author: songwj
# Version: v1.0.0

source /etc/profile
datetime=$1
hour=$2

echo "=================================$datetime$hour================================="
model=WAVE_MULTI
rootPath=/space/data2/ecData/gribdata/$model
fileRootPath=$rootPath/$datetime/$hour
ftpRootPath=/NAFP/NCEP/$model/$datetime/$hour

if [ ! -d $fileRootPath ]; then
	mkdir -p $fileRootPath
fi

echo "[$(date +'%F %T')] >>> Begin to get files from FTP path: [$ftpRootPath]"

status=$(
ftp -n << !
open 10.1.72.41
user pub_nwp nafp8nmic
binary
cd $ftpRootPath
lcd $fileRootPath
prompt
mget *
close
bye
!
)

allShortName=swDir1_swDir2_swHgt1_swHgt2
echo $status | grep "Failed to open file."
if [ $? -eq 0 ]; then
	echo "[$(date +'%F %T')] >>> Fail to get files from FTP path: [$ftpRootPath]"
else
	echo "[$(date +'%F %T')] >>> Get files from FTP successfully, local storage path is: [$fileRootPath]"
	cd $fileRootPath
	for filename in *; do
		/space/data2/ecData/bin3/gribProcess.sh $fileRootPath/$filename $model $datetime $hour $allShortName
	done
fi

# Remove source file 8 days ago
datetime=$(date +%Y%m%d -d "-8days")
srcFilePath=$rootPath/$datetime

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
for endStep in $(seq 0 3 84); do
	if [ $endStep -lt 10 ]; then
		endStep=00$endStep
	elif [ $endStep -lt 100 ]; then
		endStep=0$endStep
	fi
	
	fName02="${fName02} ${datetime}02.$endStep" # 18051508.024 18051508.027 ...
	fName08="${fName08} ${datetime}08.$endStep"
	fName14="${fName14} ${datetime}14.$endStep"
	fName20="${fName20} ${datetime}20.$endStep"
done

for endStep in $(seq 90 6 264); do
	if [ $endStep -lt 10 ]; then
		endStep=00$endStep
	elif [ $endStep -lt 100 ]; then
		endStep=0$endStep
	fi
	
	fName20="${fName20} ${datetime}20.$endStep"
done

ftpRootPath=/data/model_data_0.1/ECMWF

for shortName in $(echo $allShortName | sed 's/_/\n/g') current; do
ftp -i -v -n<<!
open 10.28.17.152
user ecdata ecdata
binary
cd $ftpRootPath/$shortName
mdelete ${fName02}
mdelete ${fName08}
mdelete ${fName14}
mdelete ${fName20}
close
bye
!
done