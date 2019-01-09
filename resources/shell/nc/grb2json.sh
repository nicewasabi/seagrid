#!/bin/bash
# Filename: grb2json.sh
# Description: Convert grib2 file to JSON
# Date: 2018-07-19
# Author: songwj
# Version: v1.0

source /etc/profile
export GRIB2JSON_HOME=/usr/local/grib2json-0.8.0
PATH=$PATH:$GRIB2JSON_HOME/bin
CLASSPATH=$GRIB2JSON_HOME/lib:$CLASSPATH
export PATH CLASSPATH

fileRootPath=$1 # /root/test/ERO3/
outputRootPath=$2 # /root/test/json/ERO3

cd $fileRootPath
for srcFilePath in *; do
	# Get filename, eg. Z_NWGD_C_BABJ_20180616114414_P_RFFC_SMERGE-ER03_201806160800_24003
	fileFullName=${srcFilePath##*/} # Z_NWGD_C_BABJ_20180616114414_P_RFFC_SMERGE-ER03_201806160800_24003.GRB2
	fileName=${fileFullName%.*}
	outputFile=$outputRootPath/$fileName.json
	# Convert GRIB2 to JSON
	grib2json -d -n -o $outputFile $srcFilePath
	echo "[$(date +'%F %T')] >>> Convert GRIB2 to JSON successfully, file path is: $outputFile"
done
