#!/bin/bash
# Filename: get_00.sh
# Description: Get and process GSM 00 hour data
# Date: 2017-11-23
# Author: songwj
# Version: v1

source /etc/profile
# Get datetime, date format: yyyymmdd
datetime=$(date +%Y%m%d)
/space/data2/gribdata/GSM/bin/getGribAndProcess.sh $datetime 00

