#!/bin/bash
# Write by songwj 20170727
source /etc/profile
PATH=$PATH:/usr/local/gribapi/bin
export PATH

srcFilePath=$1 # /root/HRES/20170726/00/W_NAFP_C_ECMF_20170726065730_P_C1D07260000080500001.bz2
fileModel=$2 # HRES
datetime=$3 # 20170726
hour=$4 # 00
shortName=$5 # 10
rootPath=/space/data2/gribdata
finalRootPath=/space/data2/dsgrib
micaps4RootPath=/space/data2/dsmicaps
# GRIB temp root path
wind10uTmpRootPath=$rootPath/wind10uTmp
wind10vTmpRootPath=$rootPath/wind10vTmp
grib1TmpRootPath=$rootPath/hresGrib1Tmp
grib2TmpRootPath=$rootPath/hresGrib2Tmp

if [ ! -f "$srcFilePath" ]; then
	echo "The incoming file does not exist: $srcFilePath"
	exit
fi

# Uncompress source file
bunzip2 -k $srcFilePath

if [ ! -d "$wind10uTmpRootPath" ]; then
	mkdir "$wind10uTmpRootPath"
fi

if [ ! -d "$wind10vTmpRootPath" ]; then
	mkdir "$wind10vTmpRootPath"
fi

if [ ! -d "$grib1TmpRootPath" ]; then
	mkdir "$grib1TmpRootPath"
fi

if [ ! -d "$grib2TmpRootPath" ]; then
	mkdir "$grib2TmpRootPath"
fi

# Get filename, eg. W_NAFP_C_ECMF_20170726065730_P_C1D07260000080500001
fileFullName=${srcFilePath##*/} # W_NAFP_C_ECMF_20170726065730_P_C1D07260000080500001.bz2
fileName=${fileFullName%.*}
grib1TmpFile=$rootPath/$fileModel/$datetime/$hour/$fileName

if [ ! -f "$grib1TmpFile" ]; then
	echo "GRIB1 temp file does not exist: $grib1TmpFile"
	exit
fi

# Parameter extract
typeset -l shrtName
shrtName=$shortName # change to lower case
wind10uFile=$wind10uTmpRootPath/$fileName
wind10vFile=$wind10vTmpRootPath/$fileName
grib1ParamExtractFile=$grib1TmpRootPath/$fileName

if [ "$shrtName" == "10" -o "$shrtName" == "10u" -o "$shrtName" == "10v" ]; then
	grib_copy -w shortName=10u $grib1TmpFile $wind10uFile
	grib_copy -w shortName=10v $grib1TmpFile $wind10vFile
	cat $wind10uFile $wind10vFile > $grib1ParamExtractFile # merge file in order
	# echo "10m wind parameter extract successfully"
else
	grib_copy -w shortName=$shrtName $grib1TmpFile $grib1ParamExtractFile
fi

# Convert GRIB1 to GRIB2
grib2TmpFile=$grib2TmpRootPath/$fileName
grib_set -s edition=2 $grib1ParamExtractFile $grib2TmpFile

if [ ! -f "$grib2TmpFile" ]; then
	echo "GRIB2 temp file does not exist: $grib2TmpFile"
	exit
fi

# GRIB2 region extract
outputPath=$finalRootPath/$fileModel/$datetime/$hour/$shortName
python $rootPath/$fileModel/bin/regionExtract.py $grib2TmpFile $outputPath $fileName $shortName

# Convert GRIB2 to MICAPS4
python $rootPath/$fileModel/bin/grib2ToMicaps4.py $outputPath/$fileName $micaps4RootPath/$fileModel/$datetime/$hour/$shortName

# Remove temp file
if [ -f "$wind10uFile" ]; then
	rm -rf "$wind10uFile"
	# echo "Temp file [$wind10uFile] remove successfully!!!"
fi

if [ -f "$wind10vFile" ]; then
	rm -rf "$wind10vFile"
	# echo "Temp file [$wind10vFile] remove successfully!!!"
fi

if [ -f "$grib1TmpFile" ]; then
	rm -rf "$grib1TmpFile"
	# echo "Temp file [$grib1TmpFile] remove successfully!!!"
fi

if [ -f "$grib1ParamExtractFile" ]; then
	rm -rf "$grib1ParamExtractFile"
	# echo "Temp file [$grib1ParamExtractFile] remove successfully!!!"
fi

if [ -f "$grib2TmpFile" ]; then
	rm -rf "$grib2TmpFile"
	# echo "Temp file [$grib2TmpFile] remove successfully!!!"
fi
