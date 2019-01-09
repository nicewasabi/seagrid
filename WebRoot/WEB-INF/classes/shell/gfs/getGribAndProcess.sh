#!/bin/bash
# Filename: getGribAndProcess.sh
# Description: Get and process GFS 00,06,12,18 hour data
# Date: 2017-11-23
# Author: songwj
# Version: v1

source /etc/profile
datetime=$1
hour=$2
echo "=================================$datetime$hour================================="
model=GFS
rootPath=/space/data2/gribdata/$model
fileRootPath=$rootPath/$datetime/$hour
ftpRootPath=/NAFP/NCEP/GFS/0p25/$datetime/$hour

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

allShortName=uv10_fg310_tp_sf_lcc_r2_r1000_p3020
echo $status | grep "Failed to open file."
if [ $? -eq 0 ]; then
	echo "[$(date +'%F %T')] >>> Fail to get files from FTP path: [$ftpRootPath]"
else
	echo "[$(date +'%F %T')] >>> Get files from FTP successfully, local storage path is: [$fileRootPath]"

	cd $fileRootPath
	for filename in *; do
		# Remove parameter tcc
		$rootPath/bin/gribProcess.sh $fileRootPath/$filename $model $datetime $hour $allShortName
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
	srcFilePath=/space/data2/dsmicaps/$model/$shortName/$datetime??.???
	rm -rf $srcFilePath
	echo "[$(date +'%F %T')] >>> Process files [$srcFilePath] remove successfully!!!"
done

uv10NIFilePath=/space/data2/dsmicaps/$model/uv10_NI/$datetime??.???
rm -rf $uv10NIFilePath
echo "[$(date +'%F %T')] >>> Process files [$uv10NIFilePath] remove successfully!!!"
uv10NPFilePath=/space/data2/dsmicaps/$model/uv10_NP/$datetime??.???
rm -rf $uv10NPFilePath
echo "[$(date +'%F %T')] >>> Process files [$uv10NPFilePath] remove successfully!!!"

# Delete 152 Server files 8 days ago
for endStep in $(seq 0 1 99); do
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

ftpRootPath=/data/model_data_0.1/NCEP

for shortName in $(echo $allShortName | sed 's/_/\n/g') uv10_NI uv10_NP; do
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