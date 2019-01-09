#!/bin/bash
# Filename: getgrib.sh
# Description: Process wamhres data
# Date: 2017-11-21
# Author: songwj
# Version: v2
source /etc/profile
localRootPath=/space/data2/gribdata/WAMHRES/$1/$2

if [ ! -d "$localRootPath" ]; then
	mkdir -p "$localRootPath"
fi

# Get source file from FTP
ftpRootPath=/NAFP/ECMWF/WAMHRES/$1/$2
echo "[$(date +'%F %T')] >>> Start to download files from FTP [$ftpRootPath]"
ftp -n << !
open 10.1.72.41
user pub_nwp nafp8nmic
binary
cd $ftpRootPath
lcd $localRootPath
prompt
mget *
close
bye
!
echo "[$(date +'%F %T')] >>> Files download from FTP successfully!!! the storage path is : [$localRootPath]"

echo "=================================$1$2================================="
cd $localRootPath
for filename in *; do
	/space/data2/gribdata/gribProcess.sh $localRootPath/$filename WAMHRES $1 $2 MWD_MWP_SHWW_SWH
done

# Remove source file 8 days ago
datetime=$(date +%Y%m%d -d "-8days")
srcFilePath=/space/data2/gribdata/WAMHRES/$datetime

if [ -d "$srcFilePath" ]; then
	rm -rf "$srcFilePath"
	echo "[$(date +'%F %T')] >>> Source files [$srcFilePath] remove successfully!!!"
fi
