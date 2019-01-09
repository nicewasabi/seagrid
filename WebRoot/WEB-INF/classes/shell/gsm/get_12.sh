#!/bin/bash
# Filename: get_12.sh
# Description: Get and process GSM 12 hour data
# Date: 2017-11-23
# Author: songwj
# Version: v1

source /etc/profile
# Get datetime, date format: yyyymmdd
datetime=$(date +%Y%m%d -d "-1day")
/space/data2/gribdata/GSM/bin/getGribAndProcess.sh $datetime 12

