#!/bin/bash
# Filename: push.sh
# Description: FTP push file
# Date: 2018-07-25
# Author: songwj
# Version: v1.0

srcRootPath=$1 #/root/test/json/ER03/
dir=$2 # ER03

cd $srcRootPath
for srcFilePath in *; do
ftp -i -v -n<<!	
open 47.92.112.104
user envser servi09P
passive
prompt
mkdir ${dir}
put $srcRootPath$srcFilePath /${dir}/$srcFilePath
close
bye
!
done
