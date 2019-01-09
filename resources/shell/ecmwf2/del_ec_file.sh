#!/bin/bash
# Filename: del_ec_file.sh
# Description: Delete ec data 8 days ago
# Date: 2018-08-21
# Author: songwj
# Version: v1.0
source /etc/profile

# Remove source file 8 days ago
datetime=$(date +%Y%m%d -d "-8days")
srcFilePath1=/space/data2/ecData/gribdata/HRES/${datetime}
srcFilePath2=/space/data2/ecData/gribdata/WAMHRES/${datetime}

if [ -d "${srcFilePath1}" ]; then
	rm -rf "${srcFilePath1}"
	echo "[$(date +'%F %T')] >>> Source files [${srcFilePath1}] remove successfully!!!"
fi

if [ -d "${srcFilePath2}" ]; then
	rm -rf "${srcFilePath2}"
	echo "[$(date +'%F %T')] >>> Source files [${srcFilePath2}] remove successfully!!!"
fi

# Remove process file 8 days ago
datetime=$(echo ${datetime} | cut -c 3-8)
allShortName=uv10_fg310_tp_sf_tcc_lcc_r2_r1000_p3020_sst_t925_t1000_t2_d2_r850_uv100_swh_shww
for shortName in $(echo ${allShortName} | sed 's/_/\n/g'); do
	srcFilePath=/space/data2/ecData/dsmicaps/$shortName/${datetime}??.???
	rm -rf $srcFilePath
	echo "[$(date +'%F %T')] >>> Process files [$srcFilePath] remove successfully!!!"
done

uv10NIFilePath=/space/data2/ecData/dsmicaps/uv10_NI/${datetime}??.???
rm -rf $uv10NIFilePath
echo "[$(date +'%F %T')] >>> Process files [$uv10NIFilePath] remove successfully!!!"
uv10NPFilePath=/space/data2/ecData/dsmicaps/uv10_NP/${datetime}??.???
rm -rf $uv10NPFilePath
echo "[$(date +'%F %T')] >>> Process files [$uv10NPFilePath] remove successfully!!!"

rmShortName=sf_tp
for shortName in $(echo ${rmShortName} | sed 's/_/\n/g'); do
	srcFilePath=/space/data2/ecData/rmAccuDsmicaps/$shortName/${datetime}??.???
	rm -rf $srcFilePath
	echo "[$(date +'%F %T')] >>> Process files [$srcFilePath] remove successfully!!!"
done

# Delete 197 Server files 8 days ago
for hour in 08 20; do
	for endStep in $(seq 0 1 120) $(seq 123 3 180) $(seq 186 6 240); do
		if [ $endStep -lt 10 ]; then
			endStep=00$endStep
		elif [ $endStep -lt 100 ]; then
			endStep=0$endStep
		fi
		
		fName="$fName ${datetime}${hour}.${endStep}" # 18051508.024 18051508.027 ...
	done
done

ftpRootPath=/data/model_data_0.1/ECMWF

for shortName in $(echo $allShortName | sed 's/_/\n/g') uv10_NI uv10_NP current swDir1 swDir2 swHgt1 swHgt2; do
ftp -i -v -n<<!
open 10.28.17.197
user ecdata ecdata
binary
cd $ftpRootPath/$shortName
mdelete $fName 
close
bye
!
done