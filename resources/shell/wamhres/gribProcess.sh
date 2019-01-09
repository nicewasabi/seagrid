#!/bin/bash
# Filename: gribProcess.sh
# Description: Process regular grid data
# Date: 2017-11-20
# Author: songwj
# Version: v2
source /etc/profile
PATH=$PATH:/usr/local/gribapi/bin
export PATH

srcFilePath=$1 # /root/W_NAFP_C_ECMF_20170627174225_P_C2P06271200062712001.bz2
fileModel=$2 # WAMHRES
datetime=$3 # 20170627
hour=$4 # 12
allShortName=$5 # MWD_MWP_SHWW_SWH
rootPath=/space/data2/gribdata
finalRootPath=/space/data2/dsgrib
micaps4RootPath=/space/data2/dsmicaps
# GRIB2 temp root path
grib2TmpRootPath=$rootPath/grib2Tmp

if [ ! -f "$srcFilePath" ]; then
	echo "The incoming file does not exist: $srcFilePath"
	exit 1
fi

# Uncompress source file
bunzip2 -k $srcFilePath

if [ ! -d "$grib2TmpRootPath" ]; then
	mkdir "$grib2TmpRootPath"
fi

# Get filename, eg. W_NAFP_C_ECMF_20170627174225_P_C2P06271200062712001
fileFullName=${srcFilePath##*/} # W_NAFP_C_ECMF_20170627174225_P_C2P06271200062712001.bz2
fileName=${fileFullName%.*}
grib1TmpFile=$rootPath/$fileModel/$datetime/$hour/$fileName
grib2TmpFile=$grib2TmpRootPath/$fileName

if [ ! -f "$grib1TmpFile" ]; then
	echo "[$(date +'%F %T')] >>> GRIB1 temp file does not exist: $grib1TmpFile"
	exit 2
fi

# Convert GRIB1 to GRIB2
grib_set -s edition=2 $grib1TmpFile $grib2TmpFile

if [ ! -f "$grib2TmpFile" ]; then
	echo "[$(date +'%F %T')] >>> GRIB2 temp file does not exist: $grib2TmpFile"
	exit 3
fi

fileSize=$(ls -l $grib2TmpFile | awk '{print $5}')
	
if [ $fileSize -eq 0 ]; then
	echo "[$(date +'%F %T')] >>> GRIB parameter [$shrtName] extract file size is incorrent"
	
	if [ -f "$grib2TmpFile" ]; then
		rm -rf "$grib2TmpFile"
		echo "[$(date +'%F %T')] >>> Temp file [$grib2TmpFile] remove successfully!!!"
	fi
	
	exit 4
fi

for shortName in $(echo $allShortName | sed 's/_/\n/g'); do
	# GRIB2 region and parameter extract
	outputPath=$finalRootPath/$fileModel/$datetime/$hour/$shortName
	python $rootPath/regionAndParameterExtract.py $grib2TmpFile $outputPath $fileName $shortName

	# Convert GRIB2 to MICAPS4
	python $rootPath/grib2ToMicaps4.py $outputPath/$fileName $micaps4RootPath/$fileModel/$datetime/$hour/$shortName
done
	
# Remove temp file
if [ -f "$grib1TmpFile" ]; then
	rm -rf "$grib1TmpFile"
	echo "[$(date +'%F %T')] >>> Temp file [$grib1TmpFile] remove successfully!!!"
fi

if [ -f "$grib2TmpFile" ]; then
	rm -rf "$grib2TmpFile"
	echo "[$(date +'%F %T')] >>> Temp file [$grib2TmpFile] remove successfully!!!"
fi
