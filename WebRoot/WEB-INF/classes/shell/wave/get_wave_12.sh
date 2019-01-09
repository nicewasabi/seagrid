#!/bin/bash
# Filename: get_wave_00.sh
# Description: Get and process wave 12 hour data
# Date: 2018-09-27
# Author: songwj
# Version: v1.0.0

source /etc/profile
# Get datetime, date format: yyyymmdd
datetime=$(date +%Y%m%d -d "-1day")
/space/data2/ecData/bin3/getGribAndProcess.sh $datetime 12

