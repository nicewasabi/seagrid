#!/bin/bash
# Filename: gribProcess.sh
# Description: Process regular grid data
# Date: 2017-11-23
# Author: songwj
# Version: v1
source /etc/profile
export PATH=$PATH:/usr/local/gribapi/bin

srcFilePath=$1 # /space/data2/gribdata/GSM/20171122/06/W_NAFP_C_RJTD_20171121060000_GSM_GPV_Rra2_Gll0p25deg_Lsurf_FD0006_grib2.bin.gz
fileModel=$2 # GSM
datetime=$3 # 20171122
hour=$4 # 06
allShortName=$5 # uv10_fg310_tp_sf_lcc_r2_r1000_p3020
# source file root path
srcRootPath=/space/data2/gribdata
dsmicapsRootPath=/space/data2/dsmicaps/$fileModel # data service output root path
# GRIB temp root path
windUTmpRootPath=/tmp/windUTmp
windVTmpRootPath=/tmp/windVTmp
gribTmpRootPath=/tmp/gribTmp
remoteAddr1=10.28.17.152
remoteAddr2=10.28.17.197

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

	if [ "$shrtName" == "10" -o "$shrtName" == "uv10" -o "$shrtName" == "10u" -o "$shrtName" == "10v" ]; then
		grib_copy -w shortName=10u $gribTmpFile $windUFile
		grib_copy -w shortName=10v $gribTmpFile $windVFile
		cat $windUFile $windVFile > $gribParamExtractFile # merge file in order
	elif [ "$shrtName" == "100" -o "$shrtName" == "uv100" -o "$shrtName" == "100u" -o "$shrtName" == "100v" ]; then
		grib_copy -w shortName=100u $gribTmpFile $windUFile
		grib_copy -w shortName=100v $gribTmpFile $windVFile
		cat $windUFile $windVFile > $gribParamExtractFile # merge file in order
	elif [ "$shrtName" == "r1000" ]; then
		grib_copy -w shortName=r,level=1000 $gribTmpFile $gribParamExtractFile
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
	
	if [ "$shrtName" == "10" -o "$shrtName" == "uv10" -o "$shrtName" == "100" -o "$shrtName" == "uv100" ]; then
		# Convert GRIB to MICAPS11
		python $srcRootPath/$fileModel/bin/gribToMicaps11.py $gribParamExtractFile $dsmicapsRootPath/$shortName 95 142 45 -10 0.1 0.1
		
		# Get 10 meter wind North Indian Ocean and North Pacific Ocean data
		if [ "$shrtName" == "uv10" ]; then
			python $srcRootPath/$fileModel/bin/gribToMicaps11.py $gribParamExtractFile $dsmicapsRootPath/uv10_NP 100 240 60 0 0.25 0.25
			python $srcRootPath/$fileModel/bin/gribToMicaps11.py $gribParamExtractFile $dsmicapsRootPath/uv10_NI 40 100 30 -20 0.25 0.25
		fi
	elif [ "$shrtName" == "swh" -o "$shrtName" == "vis" -o "$shrtName" == "sf" -o "$shrtName" == "tp" -o "$shrtName" == "tcc" -o "$shrtName" == "lcc" ]; then
		# Convert GRIB to MICAPS4
		python $srcRootPath/$fileModel/bin/gribToMicaps4_irregular.py $gribParamExtractFile $dsmicapsRootPath/$shortName 95 142 45 -10 0.1 0.1
	else
		# Convert GRIB to MICAPS4
		python $srcRootPath/$fileModel/bin/gribToMicaps4.py $gribParamExtractFile $dsmicapsRootPath/$shortName 95 142 45 -10 0.1 0.1
	fi
	
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
		ftpRootPath=/data/model_data_0.1/NCEP
		
		if [ "$shrtName" == "uv10" ]; then
status1=$(
ftp -i -v -n<<!
open ${remoteAddr1}
user ecdata ecdata
passive
prompt
mkdir $ftpRootPath/$shortName
put $outputFile $ftpRootPath/$shortName/$fName
mkdir $ftpRootPath/uv10_NP
put $dsmicapsRootPath/uv10_NP/$fName $ftpRootPath/uv10_NP/$fName
mkdir $ftpRootPath/uv10_NI
put $dsmicapsRootPath/uv10_NI/$fName $ftpRootPath/uv10_NI/$fName
close
bye
!
)
status2=$(
ftp -i -v -n<<!
open ${remoteAddr2}
user ecdata ecdata
passive
prompt
mkdir $ftpRootPath/$shortName
put $outputFile $ftpRootPath/$shortName/$fName
mkdir $ftpRootPath/uv10_NP
put $dsmicapsRootPath/uv10_NP/$fName $ftpRootPath/uv10_NP/$fName
mkdir $ftpRootPath/uv10_NI
put $dsmicapsRootPath/uv10_NI/$fName $ftpRootPath/uv10_NI/$fName
close
bye
!
)	
		else
status1=$(
ftp -i -v -n<<!
open ${remoteAddr1}
user ecdata ecdata
passive
prompt
mkdir $ftpRootPath/$shortName
put $outputFile $ftpRootPath/$shortName/$fName
close
bye
!
)
status2=$(
ftp -i -v -n<<!
open ${remoteAddr2}
user ecdata ecdata
passive
prompt
mkdir $ftpRootPath/$shortName
put $outputFile $ftpRootPath/$shortName/$fName
close
bye
!
)
		fi
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