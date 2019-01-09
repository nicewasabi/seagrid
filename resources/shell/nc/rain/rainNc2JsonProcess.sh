#!/bin/bash
# Filename: rainNc2JsonProcess.sh
# Description: Process NetCDF file and convert to JSON
# Date: 2018-07-22
# Author: songwj
# Version: v1.0

srcRootPath=/space/data2/tmp/RAIN/

cd $srcRootPath
for srcFilePath in *; do
	python /space/data2/tmp/bin/rain_nc2json.py $srcRootPath$srcFilePath
done