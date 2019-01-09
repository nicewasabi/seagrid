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
 echo  $Yesterday
for HH in  00; do
/space/data2/gribdata/WAMHRES/bin/getgrib.sh $Yesterday  $HH
done
/space/data2/gribdata/rtofs/bin/getncdata.sh
exit
