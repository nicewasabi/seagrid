#!/bin/bash
# Filename: push.sh
# Description: FTP push file
# Date: 2018-09-26
# Author: songwj
# Version: v1.0.0

datetime=$1 # 18092520
remoteAddr=$2 # eg: 10.28.17.152 or 10.28.17.197
srcRootPath=/space/data2/ecData/dsmicaps/
allShortName=uv10:fg310:tcc:lcc:r2:r1000:p3020:sst:t925:t1000:t2:d2:r850:uv100:swh:shww:uv10_NI:uv10_NP

for shortName in $(echo $allShortName | sed 's/:/\n/g'); do
	cd ${srcRootPath}${shortName}
	for srcFilePath in $(ls ${datetime}.*); do
ftp -i -v -n<<!	
open ${remoteAddr}
user ecdata ecdata
passive
prompt
put ${srcRootPath}${shortName}/${srcFilePath} /data/model_data_0.1/ECMWF/${shortName}/${srcFilePath}
close
bye
!
	done
done

srcRootPath=/space/data2/ecData/rmAccuDsmicaps/
allShortName=tp_sf

for shortName in $(echo $allShortName | sed 's/_/\n/g'); do
	cd ${srcRootPath}${shortName}
	for srcFilePath in $(ls ${datetime}.*); do
ftp -i -v -n<<!	
open ${remoteAddr}
user ecdata ecdata
passive
prompt
put ${srcRootPath}${shortName}/${srcFilePath} /data/model_data_0.1/ECMWF/${shortName}/${srcFilePath}
close
bye
!
	done
done
