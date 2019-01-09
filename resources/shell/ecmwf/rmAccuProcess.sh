#!/bin/bash
# Filename: rmAccuProcess.sh
# Description: Process sf and tp data
# Date: 2017-12-11
# Author: songwj
# Version: v1
source /etc/profile
export PATH=$PATH:/usr/local/gribapi/bin

datetime=$1 # 20170726
hour=$2 # 08 or 20
allShortName=$3 # sf_tp
dsOutputRootPath=/space/data2/ecData # data service output root path
dsmicapsRootPath=$dsOutputRootPath/dsmicaps
rmAccuDsmicapsRootPath=$dsOutputRootPath/rmAccuDsmicaps

for shortName in $(echo $allShortName | sed 's/_/\n/g'); do
	dataDate=$(echo $datetime | cut -c 3-8) # 170726
	dtime=$dataDate$hour # 17072608
	filePath000=$dsmicapsRootPath/$shortName/$dtime.000
	rmAccuDsmicapsParamRootPath=$rmAccuDsmicapsRootPath/$shortName
	flag=true # true: All files are already exist, don't need process again, false: need process
	
	if [ ! -d "$rmAccuDsmicapsParamRootPath" ]; then
		mkdir "$rmAccuDsmicapsParamRootPath"
	fi
	
	for endStep in $(seq 0 3 72) $(seq 78 6 240); do
		if [ $endStep -lt 10 ]; then
			endStep=00$endStep
		elif [ $endStep -lt 100 ]; then
			endStep=0$endStep
		fi
		
		fName=$dtime.$endStep # 17072608.024
		outputFile=$rmAccuDsmicapsParamRootPath/$fName
		
		if [ ! -f "$outputFile" ]; then
			flag=false
			break
		fi
	done
	
	if [ $flag == "true" ]; then
		echo "[$(date +'%F %T')] >>> All files [$rmAccuDsmicapsParamRootPath/$dtime.xxx] are already exist, don't need process again!!!"
		continue
	fi
	
	if [ -f "$filePath000" ]; then
		# Copy 000 timelimit file to new path
		cp $filePath000 $rmAccuDsmicapsParamRootPath
		echo "[$(date +'%F %T')] >>> Copy file [$filePath000] to path [$rmAccuDsmicapsParamRootPath] successfully"
	fi
	
	python $dsOutputRootPath/bin/rmAccuToMicaps4.py $dsmicapsRootPath/$shortName $rmAccuDsmicapsParamRootPath $dtime
	
	for endStep in $(seq 0 3 72) $(seq 78 6 240); do
		if [ $endStep -lt 10 ]; then
			endStep=00$endStep
		elif [ $endStep -lt 100 ]; then
			endStep=0$endStep
		fi
		
		fName=$dtime.$endStep # 17072608.024
		outputFile=$rmAccuDsmicapsParamRootPath/$fName
		
		if [ ! -f "$outputFile" ]; then
			echo "[$(date +'%F %T')] >>> Parameter [$shortName] convert to micaps file does not exist: $outputFile"
			continue
		fi
		
		fileSize=$(ls -l $outputFile | awk '{print $5}')
		
		if [ $fileSize -gt 0 ]; then
			ftpRootPath=/data/model_data_0.1/ECMWF
			
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

			echo "[$(date +'%F %T')] >>> $status1"
			echo "[$(date +'%F %T')] >>> $status2"
		fi
	done
done