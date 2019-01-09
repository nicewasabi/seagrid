#!/bin/bash
source /etc/profile
var=`date +%Y%m%d`
set +x
   needdate=`echo $var | cut -c1-8`
   yy=`echo $needdate | cut -c1-4`
   mm=`echo $needdate | cut -c5-6`
   dd=`echo $needdate | cut -c7-8`
if [ $dd = "01" ]
then
 lm=`expr $mm - 1 `
  if [ $lm -eq 0 ]
  then
    
    lm=12
     yy=`expr $yy - 1 `
  fi
  echo lm=$lm
   case $lm in
     1|3|5|7|8|10|12) Yesterday=31 ;;
     4|6|9|11) Yesterday=30 ;;
     2)
     if [ ` expr $yy % 4 ` -eq 0 -a `expr $yy % 100 ` -ne 0 -o ` expr $yy % 400 ` -eq 0 ]
     then Yesterday=29
     else Yesterday=28
     fi ;;
     
   esac 
   mm=$lm
   echo Yesterday=$Yesterday
   echo $mm
 else
   Yesterday=`expr $dd - 1 `
 fi
 case $Yesterday
     in 1|2|3|4|5|6|7|8|9) Yesterday='0'$Yesterday
    esac
  case $mm in
       1|2|3|4|5|6|7|8|9) mm='0'$mm ;;
  esac
 
  Yesterday=$yy$mm$Yesterday
mkdir /space/data2/gribdata/rtofs/$Yesterday
 wget -P /space/data2/gribdata/rtofs/$Yesterday ftp://ftpprd.ncep.noaa.gov/pub/data/nccf/com/rtofs/prod/rtofs.$Yesterday/rtofs_glo_2ds_f001_1hrly_prog.nc
 wget -P /space/data2/gribdata/rtofs/$Yesterday ftp://ftpprd.ncep.noaa.gov/pub/data/nccf/com/rtofs/prod/rtofs.$Yesterday/rtofs_glo_2ds_f024_daily_prog.nc
 wget -P /space/data2/gribdata/rtofs/$Yesterday ftp://ftpprd.ncep.noaa.gov/pub/data/nccf/com/rtofs/prod/rtofs.$Yesterday/rtofs_glo_2ds_f048_daily_prog.nc
 wget -P /space/data2/gribdata/rtofs/$Yesterday ftp://ftpprd.ncep.noaa.gov/pub/data/nccf/com/rtofs/prod/rtofs.$Yesterday/rtofs_glo_2ds_f072_daily_prog.nc
 wget -P /space/data2/gribdata/rtofs/$Yesterday ftp://ftpprd.ncep.noaa.gov/pub/data/nccf/com/rtofs/prod/rtofs.$Yesterday/rtofs_glo_2ds_f096_daily_prog.nc
 wget -P /space/data2/gribdata/rtofs/$Yesterday ftp://ftpprd.ncep.noaa.gov/pub/data/nccf/com/rtofs/prod/rtofs.$Yesterday/rtofs_glo_2ds_f120_daily_prog.nc
exit
