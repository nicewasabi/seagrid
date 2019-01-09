#!/bin/bash
# Filename: tmpNc2JsonProcess.sh
# Description: Process NetCDF file and convert to JSON
# Date: 2018-07-24
# Author: songwj
# Version: v1.0

srcRootPath=/space/data2/tmp/HRCLDAS-TMP/

cd $srcRootPath
for srcFilePath in *; do
	python /space/data2/tmp/bin/hrcldastmp_nc2json.py $srcRootPath$srcFilePath
done