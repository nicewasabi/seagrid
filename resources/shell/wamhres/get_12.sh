#!/bin/bash
# Filename: get_12.sh
# Description: Get and process wamhres 12 batch data
# Date: 2017-11-21
# Author: songwj
# Version: v2

# Get yesterday, date format: yyyymmdd
yesterday=$(date +%Y%m%d -d "-1day")
/space/data2/gribdata/WAMHRES/bin/getgrib.sh $yesterday 12
