#!/bin/bash
# Filename: rmAccuProcess.sh
# Description: Process sf and tp data
# Date: 2017-12-11
# Author: songwj
# Version: v1
source /etc/profile
export PATH=$PATH:/usr/local/gribapi/bin

datetime=$1 # 20180819
hour=$2 # 08 or 20
allShortName=$3 # sf_tp
dsOutputRootPath=/space/data2/ecData # data service output root path
dsmicapsRootPath=$dsOutputRootPath/dsmicaps
rmAccuDsmicapsRootPath=$dsOutputRootPath/rmAccuDsmicaps
remoteAddr1=10.28.17.152
remoteAddr2=10.28.17.197
username=ecdata
password=ecdata
ftpRootPath=/data/model_data_0.1/ECMWF

for shortName in $(echo $allShortName | sed 's/_/\n/g'); do
	dataDate=$(echo $datetime | cut -c 3-8) # 170726
	dtime=$dataDate$hour # 17072608
	filePath000=$dsmicapsRootPath/$shortName/$dtime.000
	rmAccuDsmicapsParamRootPath=$rmAccuDsmicapsRootPath/$shortName
	
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
		
		if [ -f $outputFile ]; then
			echo "[$(date +'%F %T')] >>> File [$outputFile] has already exist, no need to process it again!!!"
			continue
		elif [ $endStep == "000" ]; then
			if [ -f ${filePath000} ]; then
				if [ $(ls -l ${filePath000} | awk '{print $5}') -gt 0 ]; then 
					# Copy 000 timelimit file to new path and push to 10.28.17.152、10.28.17.4
					cp ${filePath000} ${outputFile}
					echo "[$(date +'%F %T')] >>> Copy file [${filePath000}] to path [${outputFile}] successfully"
				fi
			fi
		else
			python $dsOutputRootPath/bin2/rmAccuToMicaps4.py $dsmicapsRootPath/$shortName $rmAccuDsmicapsParamRootPath $dtime $endStep
		fi
		
		if [ -f $outputFile ]; then
			if [ $(ls -l $outputFile | awk '{print $5}') -gt 0 ]; then
status1=$(
ftp -i -v -n<<!
open ${remoteAddr1}
user ${username} ${password}
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
user ${username} ${password}
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
		else
			echo "[$(date +'%F %T')] >>> File [${outputFile}] does not exist!!!"
		fi
	done
done

exit 0