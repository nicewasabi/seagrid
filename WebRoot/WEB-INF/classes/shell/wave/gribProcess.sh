#!/bin/bash
# Filename: gribProcess.sh
# Description: Process regular grid data
# Date: 2017-11-23
# Author: songwj
# Version: v2
source /etc/profile
export PATH=/usr/local/eccodes/bin:$PATH

srcFilePath=$1 # /space/data2/ecData/gribdata/WAVE_MULTI/20180926/00/W_NAFP_C_KWBC_20180926030000_P_multi_1.glo_30mext.t00z.f000.grib2
fileModel=$2 # WAVE_MULTI
datetime=$3 # 20180926
hour=$4 # 00
allShortName=$5 # swDir1_swDir2_swHgt1_swHgt2
# source file root path
srcRootPath=/space/data2/ecData/gribdata
dsmicapsRootPath=/space/data2/ecData/dsmicaps # data service output root path
# GRIB temp root path
windUTmpRootPath=/tmp/windUTmp
windVTmpRootPath=/tmp/windVTmp
gribTmpRootPath=/tmp/gribTmp

if [ ! -f "$srcFilePath" ]; then
	echo "The incoming file does not exist: $srcFilePath"
	exit 1
fi

fileFullName=${srcFilePath##*/} # W_NAFP_C_RJTD_20171121060000_GSM_GPV_Rra2_Gll0p25deg_Lsurf_FD0006_grib2.bin.gz
fileSuffix=${fileFullName##*.} # gz

if [ $fileSuffix == "gz" ]; then
	# Uncompress source file
	gunzip $srcFilePath
	# Get filename, eg. W_NAFP_C_RJTD_20171121060000_GSM_GPV_Rra2_Gll0p25deg_Lsurf_FD0006_grib2.bin
	fileName=${fileFullName%.*}
else
	fileName=$fileFullName
fi

if [ ! -d "$windUTmpRootPath" ]; then
	mkdir "$windUTmpRootPath"
fi

if [ ! -d "$windVTmpRootPath" ]; then
	mkdir "$windVTmpRootPath"
fi

if [ ! -d "$gribTmpRootPath" ]; then
	mkdir "$gribTmpRootPath"
fi

gribTmpFile=$srcRootPath/$fileModel/$datetime/$hour/$fileName

if [ ! -f "$gribTmpFile" ]; then
	echo "[$(date +'%F %T')] >>> GRIB temp file does not exist: $gribTmpFile"
	exit 2
fi

for shortName in $(echo $allShortName | sed 's/_/\n/g'); do
	# Parameter extract
	typeset -l shrtName
	shrtName=$shortName # change to lower case
	
	if [ "$shrtName" == "p3020" ]; then
		shrtName="vis"
	elif [ "$shrtName" == "fg310" ]; then
		shrtName="10fg3"
	fi
	
	windUFile=$windUTmpRootPath/$fileName
	windVFile=$windVTmpRootPath/$fileName
	gribParamExtractFile=$gribTmpRootPath/$fileName

	if [ "$shrtName" == "swdir1" ]; then
		grib_copy -w shortName=swdir,level=1 $gribTmpFile $gribParamExtractFile
	elif [ "$shrtName" == "swdir2" ]; then
		grib_copy -w shortName=swdir,level=2 $gribTmpFile $gribParamExtractFile
	elif [ "$shrtName" == "swhgt1" ]; then
		grib_copy -w shortName=swell,level=1 $gribTmpFile $gribParamExtractFile
	elif [ "$shrtName" == "swhgt2" ]; then
		grib_copy -w shortName=swell,level=2 $gribTmpFile $gribParamExtractFile
	else
		grib_copy -w shortName=$shrtName $gribTmpFile $gribParamExtractFile
	fi
	
	if [ -f "$windUFile" ]; then
		rm -rf "$windUFile"
		echo "[$(date +'%F %T')] >>> Temp file [$windUFile] remove successfully!!!"
	fi

	if [ -f "$windVFile" ]; then
		rm -rf "$windVFile"
		echo "[$(date +'%F %T')] >>> Temp file [$windVFile] remove successfully!!!"
	fi
	
	if [ ! -f "$gribParamExtractFile" ]; then
		echo "[$(date +'%F %T')] >>> GRIB parameter [$shrtName] extract file does not exist: $gribParamExtractFile"
		continue
	fi
	
	fileSize=$(ls -l $gribParamExtractFile | awk '{print $5}')
	
	if [ $fileSize -eq 0 ]; then
		echo "[$(date +'%F %T')] >>> GRIB parameter [$shrtName] extract file size is incorrent"
		
		if [ -f "$gribParamExtractFile" ]; then
			rm -rf "$gribParamExtractFile"
			echo "[$(date +'%F %T')] >>> Temp file [$gribParamExtractFile] remove successfully!!!"
		fi
		
		continue
	fi
	
	# Convert GRIB to MICAPS4
	python /space/data2/ecData/bin3/gribToMicaps4_irregular.py $gribParamExtractFile $dsmicapsRootPath/$shortName 95 142 45 -10 0.5 0.5
	
	# Get output filename, final filename format is 17072608.024
	dataDate=$(grib_get -p dataDate $gribParamExtractFile | head -1) # 20170726
	dataTime=$(grib_get -p dataTime $gribParamExtractFile | head -1) # 0 or 1200
	forecastTime=$(grib_get -p forecastTime $gribParamExtractFile | head -1) # 24
	
	if [ $dataTime -eq 0 ]; then
		dataTime="08"
	elif [ $dataTime -eq 600 ]; then
		dataTime="14"
	elif [ $dataTime -eq 1200 ]; then
		dataTime="20"
	elif [ $dataTime -eq 1800 ]; then
		# Add one day
		seconds=$(date -d $dataDate +%s)
		secondsNew=$(expr $seconds + 86400)
		dataDate=$(date -d @$secondsNew "+%Y%m%d")
		dataTime="02"
	fi
	
	dataDate=$(echo $dataDate | cut -c 3-8) # 170726
	
	if [ $forecastTime -lt 10 ]; then
		forecastTime=00$forecastTime
	elif [ $forecastTime -lt 100 ]; then
		forecastTime=0$forecastTime
	fi
	
	fName=$dataDate$dataTime.$forecastTime # 17072608.024
	outputFile=$dsmicapsRootPath/$shortName/$fName
	
	if [ ! -f "$outputFile" ]; then
		echo "[$(date +'%F %T')] >>> Parameter [$shrtName] convert to micaps file does not exist: $outputFile"
		
		if [ -f "$gribParamExtractFile" ]; then
			rm -rf "$gribParamExtractFile"
			echo "[$(date +'%F %T')] >>> Temp file [$gribParamExtractFile] remove successfully!!!"
		fi
		
		continue
	fi
	
	fileSize=$(ls -l $outputFile | awk '{print $5}')
	
	if [ $fileSize -gt 0 ]; then
status1=$(
ftp -i -v -n << !
open 10.28.17.152
user ecdata ecdata
passive
prompt
mkdir /data/model_data_0.1/ECMWF/$shortName
put $outputFile /data/model_data_0.1/ECMWF/$shortName/$fName
close
bye
!
)
status2=$(
ftp -i -v -n << !
open 10.28.17.197
user ecdata ecdata
passive
prompt
mkdir /data/model_data_0.1/ECMWF/$shortName
put $outputFile /data/model_data_0.1/ECMWF/$shortName/$fName
close
bye
!
)
		echo "[$(date +'%F %T')] >>> $status1"
		echo "[$(date +'%F %T')] >>> $status2"
	fi
	
	if [ -f "$gribParamExtractFile" ]; then
		rm -rf "$gribParamExtractFile"
		echo "[$(date +'%F %T')] >>> Temp file [$gribParamExtractFile] remove successfully!!!"
	fi
done
	
# Remove temp file
if [ -f "$windUFile" ]; then
	rm -rf "$windUFile"
	echo "[$(date +'%F %T')] >>> Temp file [$windUFile] remove successfully!!!"
fi

if [ -f "$windVFile" ]; then
	rm -rf "$windVFile"
	echo "[$(date +'%F %T')] >>> Temp file [$windVFile] remove successfully!!!"
fi

if [ -f "$gribParamExtractFile" ]; then
	rm -rf "$gribParamExtractFile"
	echo "[$(date +'%F %T')] >>> Temp file [$gribParamExtractFile] remove successfully!!!"
fi