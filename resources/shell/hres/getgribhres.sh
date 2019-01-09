#!/bin/bash
source /etc/profile
mkdir /space/data2/gribdata/HRES
mkdir /space/data2/gribdata/HRES/$1
mkdir /space/data2/gribdata/HRES/$1/$2
ftp -n << !
open 10.1.72.41
user pub_nwp nafp8nmic
binary
cd /NAFP/ECMWF/HRES/$1/$2/
lcd /space/data2/gribdata/HRES/$1/$2/
prompt
mget *
close
bye
!
cd /space/data2/gribdata/HRES/$1/$2;
  for filename in *;
    do         #??¡¤¦̡À???¦Ì?t
      array[$x]=$filename   #??t????¨°?x¦Ìa?
      let "x+=1"      #???¨°??¡ê?3¡è     
      for surface in  10;
	 do
		/space/data2/gribdata/HRES/bin/gribProcess.sh /space/data2/gribdata/HRES/$1/$2/$filename HRES $1 $2 $surface ;
	done
 done
 
# Remove source file 8 days ago
datetime=$(date +%Y%m%d -d "-8days")
srcFilePath=/space/data2/gribdata/HRES/$datetime

if [ -d "$srcFilePath" ]; then
	rm -rf "$srcFilePath"
	echo "[$(date +'%F %T')] >>> Source files [$srcFilePath] remove successfully!!!"
fi
