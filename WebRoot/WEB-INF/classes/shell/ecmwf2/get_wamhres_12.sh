#!/bin/bash
# Filename: get_wamhres_12.sh
# Description: Process wamhres 12 data
# Date: 2018-08-17
# Author: songwj
# Version: v1.0
source /etc/profile

# Get datetime, date format: yyyymmdd
datetime=$(date +%Y%m%d -d "-1day")
year=$(echo $datetime | cut -c 1-4)
dtime=$(echo $datetime | cut -c 1-6)
hour=12
model=WAMHRES
ftp41Addr=10.1.72.41
ftp41Username=pub_nwp
ftp41Pwd=nafp8nmic
# Local ftp record file 
localFileRecordFile=/space/data2/ecData/bin2/wamhres_${hour}_ftp_record
# Storage have been processed file record
localProcessedRecordFileDir=/space/data2/ecData/record/$year/$dtime/$datetime/$hour
localProcessedRecordFile=${localProcessedRecordFileDir}/wamhres_${hour}_process_record
fileRootPath=/space/data2/ecData/gribdata/${model}/$datetime/$hour

if [ ! -f $fileRootPath ]; then
	mkdir -p $fileRootPath
fi

if [ ! -f $localProcessedRecordFile ]; then
	mkdir -p $localProcessedRecordFileDir
	touch $localProcessedRecordFile
fi

echo "===========================$datetime$hour==========================="
# Get ftp all files info
ftp -i -v -n<<!
open ${ftp41Addr}
user ${ftp41Username} ${ftp41Pwd}
passive
prompt
ls /NAFP/ECMWF/${model}/$datetime/$hour/ ${localFileRecordFile}
close
bye
!

# Start to read local record file and process
while read -r line; do
	# Get line fileSize, updateTime and filename info
	fileSize=$(echo ${line} | awk '{print $5}')
	updateTime=$(echo ${line} | awk '{print $8}')
	filename=$(echo ${line} | awk '{print $9}')
	needProcess=true
	
	if [ ! $(grep -c ${filename} ${localProcessedRecordFile}) -eq 0 ]; then
		# Get the row contents of the record
		record=$(grep ${filename} ${localProcessedRecordFile} | head -1)
		updateTime1=$(echo ${record} | awk '{print $2}')
		fileSize1=$(echo ${record} | awk '{print $3}')
		ifProcessing=$(echo ${record} | awk '{print $4}')
		
		if [ ${ifProcessing} == "processing" ]; then
			needProcess=false
		elif [ ${updateTime} == ${updateTime1} -a ${fileSize} == ${fileSize1} ]; then
			needProcess=false
		fi
	fi
	
	if $needProcess; then
		echo "[$(date +'%F %T')] >>> Start to process file: [${filename}]"
		recordInfo1="${filename} ${updateTime} ${fileSize} processing"
		echo ${recordInfo1} >> ${localProcessedRecordFile}
		ftpFileFullPath=/NAFP/ECMWF/${model}/${datetime}/${hour}/${filename}
		
		echo "[$(date +'%F %T')] >>> Start to download file [${ftpFileFullPath}] from: [${ftp41Addr}]."
ftp -n << !
open ${ftp41Addr}
user ${ftp41Username} ${ftp41Pwd}
binary
prompt
get ${ftpFileFullPath} ${fileRootPath}/${filename}
close
bye
!
		echo "[$(date +'%F %T')] >>> Download file [${ftpFileFullPath}] complete."
		
		allShortName=swh_shww
		/space/data2/ecData/bin2/gribProcess.sh ${fileRootPath}/${filename} ${model} $datetime $hour $allShortName
		
		# Get record row num then repalce it
		rowNum=$(sed -n -e "/${filename}/=" ${localProcessedRecordFile})
		recordInfo2="${filename} ${updateTime} ${fileSize} done"
		
		if [ -z $rowNum ]; then
			echo ${recordInfo2} >> ${localProcessedRecordFile}
		else
			sed -i "${rowNum}s/${recordInfo1}/${recordInfo2}/g" ${localProcessedRecordFile}
		fi
		
		echo "[$(date +'%F %T')] >>> Add new record [${recordInfo2}] to file [${localProcessedRecordFile}]."
	else
		echo "[$(date +'%F %T')] >>> File [${filename}] has already processed, no need to process it again."
	fi
done < ${localFileRecordFile}