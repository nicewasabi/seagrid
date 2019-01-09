#!/bin/bash
source /etc/profile
datetime=$(date +%Y%m%d)
/space/data2/gribdata/HRES/bin/getgribhres.sh $datetime 00
