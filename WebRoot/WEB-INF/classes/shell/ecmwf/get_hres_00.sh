#!/bin/bash
# Filename: get_hres_00.sh
# Description: Process hres 00 data
# Date: 2017-11-21
# Author: songwj
# Version: v4
source /etc/profile

#pidList=$(ps aux | grep /space/data2/ecData/bin/get_hres_00.sh | grep -v grep | awk '{print $2}')
#[[ ! -z $pidList ]]
#if [ $? -eq 0 ]; then
#	echo "[$(date +'%F %T')] >>> Process [/space/data2/ecData/bin/get_hres_00.sh] is running, no need to process it again."
#	exit 0
#fi

# Get datetime, date format: yyyymmdd
datetime=$(date +%Y%m%d)
hour=00

fileRootPath=/space/data2/ecData/gribdata/HRES/$datetime/$hour
mkdir -p $fileRootPath

echo "[$(date +'%F %T')] >>> Start to download data from: /NAFP/ECMWF/HRES/$datetime/$hour."
ftp -n << !
open 10.1.72.41
user pub_nwp nafp8nmic
binary
cd /NAFP/ECMWF/HRES/$datetime/$hour
lcd $fileRootPath
prompt
mget *
close
bye
!
echo "[$(date +'%F %T')] >>> Download complete."

echo "===========================$datetime$hour==========================="
cd $fileRootPath
allShortName=uv10_fg310_tp_sf_tcc_lcc_r2_r1000_p3020_sst_t925_t1000_t2_d2_r850_uv100
for filename in *; do
	/space/data2/ecData/bin/gribProcess.sh $fileRootPath/$filename HRES $datetime $hour $allShortName
done

beijingHour=08
rmShortName=sf_tp
# Remove accumulator and generate MICAPS4
/space/data2/ecData/bin/rmAccuProcess.sh $datetime $beijingHour $rmShortName
# Remove source file 8 days ago
datetime=$(date +%Y%m%d -d "-8days")
srcFilePath=/space/data2/ecData/gribdata/HRES/$datetime

if [ -d "$srcFilePath" ]; then
	rm -rf "$srcFilePath"
	echo "[$(date +'%F %T')] >>> Source files [$srcFilePath] remove successfully!!!"
fi

# Remove process file 8 days ago
datetime=$(echo $datetime | cut -c 3-8)
for shortName in $(echo ${allShortName} | sed 's/_/\n/g'); do
	srcFilePath=/space/data2/ecData/dsmicaps/$shortName/$datetime$beijingHour.???
	rm -rf $srcFilePath
	echo "[$(date +'%F %T')] >>> Process files [$srcFilePath] remove successfully!!!"
done

uv10NIFilePath=/space/data2/ecData/dsmicaps/uv10_NI/$datetime$beijingHour.???
rm -rf $uv10NIFilePath
echo "[$(date +'%F %T')] >>> Process files [$uv10NIFilePath] remove successfully!!!"
uv10NPFilePath=/space/data2/ecData/dsmicaps/uv10_NP/$datetime$beijingHour.???
rm -rf $uv10NPFilePath
echo "[$(date +'%F %T')] >>> Process files [$uv10NPFilePath] remove successfully!!!"

for shortName in $(echo ${rmShortName} | sed 's/_/\n/g'); do
	srcFilePath=/space/data2/ecData/rmAccuDsmicaps/$shortName/$datetime$beijingHour.???
	rm -rf $srcFilePath
	echo "[$(date +'%F %T')] >>> Process files [$srcFilePath] remove successfully!!!"
done

# Delete 152 Server files 8 days ago
for endStep in $(seq 0 3 144) $(seq 150 6 240); do
	if [ $endStep -lt 10 ]; then
		endStep=00$endStep
	elif [ $endStep -lt 100 ]; then
		endStep=0$endStep
	fi
	
	fName="$fName $datetime$beijingHour.$endStep" # 18051508.024 18051508.027 ...
done

ftpRootPath=/data/model_data_0.1/ECMWF

for shortName in $(echo $allShortName | sed 's/_/\n/g') uv10_NI uv10_NP; do
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