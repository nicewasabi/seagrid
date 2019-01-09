#!/bin/bash
# Filename: get_18.sh
# Description: Get and process GSM 18 hour data
# Date: 2017-11-23
# Author: songwj
# Version: v1

source /etc/profile
# Get yesterday, date format: yyyymmdd
yesterday=$(date +%Y%m%d -d "-1day")
/space/data2/gribdata/GSM/bin/getGribAndProcess.sh $yesterday 18

