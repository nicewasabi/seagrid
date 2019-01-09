#!/bin/bash
source /etc/profile
yesterday=$(date +%Y%m%d -d "-1day")
/space/data2/gribdata/HRES/bin/getgribhres.sh $Yesterday 12
