#!/bin/bash
# Filename: gribProcess.sh
# Description: Process regular grid data
# Date: 2017-11-21
# Author: songwj
# Version: v4
source /etc/profile
PATH=$PATH:/usr/local/gribapi/bin
export PATH

srcFilePath=$1 # /space/data2/gribdata/HRES/20170726/00/W_NAFP_C_ECMF_20170726065730_P_C1D07260000080500001.bz2
fileModel=$2 # HRES
datetime=$3 # 20170726
hour=$4 # 00
allShortName=$5 # uv10_fg310_tp_sf_tcc_lcc_r2_r1000_p3020_sst_t925_t1000_t2_d2_r850
# source file root path
srcRootPath=/space/data2/ecData/gribdata
dsOutputRootPath=/space/data2/ecData # data service output root path
dsmicapsRootPath=$dsOutputRootPath/dsmicaps
# GRIB temp root path
windUTmpRootPath=/tmp/windUTmp
windVTmpRootPath=/tmp/windVTmp
grib1TmpRootPath=/tmp/grib1Tmp

if [ ! -f "$srcFilePath" ]; then
	echo "The incoming file does not exist: $srcFilePath"
	exit 1
fi

# Uncompress source file
bunzip2 -k $srcFilePath

if [ ! -d "$windUTmpRootPath" ]; then
	mkdir "$windUTmpRootPath"
fi

if [ ! -d "$windVTmpRootPath" ]; then
	mkdir "$windVTmpRootPath"
fi

if [ ! -d "$grib1TmpRootPath" ]; then
	mkdir "$grib1TmpRootPath"
fi

# Get filename, eg. W_NAFP_C_ECMF_20170726065730_P_C1D07260000080500001
fileFullName=${srcFilePath##*/} # W_NAFP_C_ECMF_20170726065730_P_C1D07260000080500001.bz2
fileName=${fileFullName%.*}
grib1TmpFile=$srcRootPath/$fileModel/$datetime/$hour/$fileName

if [ ! -f "$grib1TmpFile" ]; then
	echo "[$(date +'%F %T')] >>> GRIB temp file does not exist: $grib1TmpFile"
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
	grib1ParamExtractFile=$grib1TmpRootPath/$fileName

	if [ "$shrtName" == "10" -o "$shrtName" == "uv10" -o "$shrtName" == "10u" -o "$shrtName" == "10v" ]; then
		grib_copy -w shortName=10u $grib1TmpFile $windUFile
		grib_copy -w shortName=10v $grib1TmpFile $windVFile
		cat $windUFile $windVFile > $grib1ParamExtractFile # merge file in order
	elif [ "$shrtName" == "100" -o "$shrtName" == "uv100" -o "$shrtName" == "100u" -o "$shrtName" == "100v" ]; then
		grib_copy -w shortName=100u $grib1TmpFile $windUFile
		grib_copy -w shortName=100v $grib1TmpFile $windVFile
		cat $windUFile $windVFile > $grib1ParamExtractFile # merge file in order
	elif [ "$shrtName" == "r850" ]; then
		grib_copy -w shortName=r,level=850 $grib1TmpFile $grib1ParamExtractFile
	elif [ "$shrtName" == "r1000" ]; then
		grib_copy -w shortName=r,level=1000 $grib1TmpFile $grib1ParamExtractFile
	elif [ "$shrtName" == "t925" ]; then
		grib_copy -w shortName=t,level=925 $grib1TmpFile $grib1ParamExtractFile
	elif [ "$shrtName" == "t1000" ]; then
		grib_copy -w shortName=t,level=1000 $grib1TmpFile $grib1ParamExtractFile
	elif [ "$shrtName" == "t2" ]; then
		grib_copy -w shortName=2t $grib1TmpFile $grib1ParamExtractFile
	elif [ "$shrtName" == "d2" ]; then
		grib_copy -w shortName=2d $grib1TmpFile $grib1ParamExtractFile
	elif [ "$shrtName" == "r2" ]; then
		grib_copy -w shortName=2d $grib1TmpFile $windUFile
		grib_copy -w shortName=2t $grib1TmpFile $windVFile
		cat $windUFile $windVFile > $grib1ParamExtractFile # merge file in order
	else
		grib_copy -w shortName=$shrtName $grib1TmpFile $grib1ParamExtractFile
	fi
	
	if [ -f "$windUFile" ]; then
		rm -rf "$windUFile"
		echo "[$(date +'%F %T')] >>> Temp file [$windUFile] remove successfully!!!"
	fi

	if [ -f "$windVFile" ]; then
		rm -rf "$windVFile"
		echo "[$(date +'%F %T')] >>> Temp file [$windVFile] remove successfully!!!"
	fi
	
	if [ ! -f "$grib1ParamExtractFile" ]; then
		echo "[$(date +'%F %T')] >>> GRIB parameter [$shrtName] extract file does not exist: $grib1ParamExtractFile"
		continue
	fi
	
	fileSize=$(ls -l $grib1ParamExtractFile | awk '{print $5}')
	
	if [ $fileSize -eq 0 ]; then
		echo "[$(date +'%F %T')] >>> GRIB parameter [$shrtName] extract file size is incorrent"
		
		if [ -f "$grib1ParamExtractFile" ]; then
			rm -rf "$grib1ParamExtractFile"
			echo "[$(date +'%F %T')] >>> Temp file [$grib1ParamExtractFile] remove successfully!!!"
		fi
		
		continue
	fi
	
	# Get output filename, final filename format is 17072608.024
	dataDate=$(grib_get -p dataDate $grib1ParamExtractFile | head -1) # 20170726
	dataTime=$(grib_get -p dataTime $grib1ParamExtractFile | head -1) # 0 or 1200
	endStep=$(grib_get -p endStep $grib1ParamExtractFile | head -1) # 24
	dataDate=$(echo $dataDate | cut -c 3-8) # 170726
	
	if [ $dataTime -eq 0 ]; then
		dataTime="08"
	else
		dataTime="20"
	fi
	
	if [ $endStep -lt 10 ]; then
		endStep=00$endStep
	elif [ $endStep -lt 100 ]; then
		endStep=0$endStep
	fi
	
	fName=$dataDate$dataTime.$endStep # 17072608.024
	outputFile=$dsmicapsRootPath/$shortName/$fName
	
	if [ -f "$outputFile" ]; then
		echo "[$(date +'%F %T')] >>> File [$outputFile] is already exist, don't need process it again!!!"
		
		if [ -f "$grib1ParamExtractFile" ]; then
			rm -rf "$grib1ParamExtractFile"
			echo "[$(date +'%F %T')] >>> Temp file [$grib1ParamExtractFile] remove successfully!!!"
		fi
		
		continue
	fi
	
	if [ "$shrtName" == "10" -o "$shrtName" == "uv10" -o "$shrtName" == "100" -o "$shrtName" == "uv100" ]; then
		# Convert GRIB to MICAPS11
		python $dsOutputRootPath/bin2/gribToMicaps11.py $grib1ParamExtractFile $dsmicapsRootPath/$shortName 95 142 45 -10 0.1 0.1
		
		# Get 10 meter wind North Indian Ocean and North Pacific Ocean data
		if [ "$shrtName" == "uv10" ]; then
			python $dsOutputRootPath/bin2/gribToMicaps11.py $grib1ParamExtractFile $dsmicapsRootPath/uv10_NP 100 240 60 0 0.25 0.25
			python $dsOutputRootPath/bin2/gribToMicaps11.py $grib1ParamExtractFile $dsmicapsRootPath/uv10_NI 40 100 30 -20 0.25 0.25
		fi
	elif [ "$shrtName" == "swh" -o "$shrtName" == "vis" -o "$shrtName" == "sf" -o "$shrtName" == "tp" -o "$shrtName" == "tcc" -o "$shrtName" == "lcc" -o "$shrtName" == "shww" ]; then
		# Convert GRIB to MICAPS4
		python $dsOutputRootPath/bin2/gribToMicaps4_irregular.py $grib1ParamExtractFile $dsmicapsRootPath/$shortName 95 142 45 -10 0.1 0.1
	elif [ "$shrtName" == "r2" ]; then
		# Convert GRIB to MICAPS4
		python $dsOutputRootPath/bin2/gribToMicaps4_r2.py $grib1ParamExtractFile $dsmicapsRootPath/$shortName 95 142 45 -10 0.1 0.1
	else
		# Convert GRIB to MICAPS4
		python $dsOutputRootPath/bin2/gribToMicaps4.py $grib1ParamExtractFile $dsmicapsRootPath/$shortName 95 142 45 -10 0.1 0.1
	fi
	
	if [ ! -f "$outputFile" ]; then
		echo "[$(date +'%F %T')] >>> Parameter [$shrtName] convert to micaps file does not exist: $outputFile"
		
		if [ -f "$grib1ParamExtractFile" ]; then
			rm -rf "$grib1ParamExtractFile"
			echo "[$(date +'%F %T')] >>> Temp file [$grib1ParamExtractFile] remove successfully!!!"
		fi
		
		continue
	fi
	
	fileSize=$(ls -l $outputFile | awk '{print $5}')
	
	if [ $fileSize -gt 0 ]; then
		ftpRootPath=/data/model_data_0.1/ECMWF
		
		if [ "$shrtName" == "uv10" ]; then
status1=$(
ftp -i -v -n<<!
open 10.28.17.152
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
open 10.28.17.197
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
		elif [ "$shrtName" != "sf" -a "$shrtName" != "tp" ]; then
status1=$(
ftp -i -v -n<<!
open 10.28.17.152
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
open 10.28.17.197
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
	
	if [ -f "$grib1ParamExtractFile" ]; then
		rm -rf "$grib1ParamExtractFile"
		echo "[$(date +'%F %T')] >>> Temp file [$grib1ParamExtractFile] remove successfully!!!"
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

if [ -f "$grib1TmpFile" ]; then
	rm -rf "$grib1TmpFile"
	echo "[$(date +'%F %T')] >>> Temp file [$grib1TmpFile] remove successfully!!!"
fi

if [ -f "$grib1ParamExtractFile" ]; then
	rm -rf "$grib1ParamExtractFile"
	echo "[$(date +'%F %T')] >>> Temp file [$grib1ParamExtractFile] remove successfully!!!"
fi