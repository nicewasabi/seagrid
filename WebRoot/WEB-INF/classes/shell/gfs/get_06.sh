#!/bin/bash
# Filename: get_06.sh
# Description: Get and process GFS 06 hour data
# Date: 2017-11-23
# Author: songwj
# Version: v1

source /etc/profile
# Get datetime, date format: yyyymmdd
datetime=$(date +%Y%m%d -d "-1day")
/space/data2/gribdata/GFS/bin/getGribAndProcess.sh $datetime 06

