#coding=utf-8
import traceback
import sys
import time
import os
 
from gribapi import *
 
INPUT = sys.argv[1] + ".grib2"
rootPath = sys.argv[2]
VERBOSE = 1 # verbose error reporting

def grib2ToMicaps4():
	if not os.path.exists(INPUT):
		print "GRIB2 file does not exist: " + INPUT
		return

	if not os.path.exists(rootPath):
		os.makedirs(rootPath)
		
	fin = open(INPUT)
	global fout
	global output
	count = 1;
	
	while 1: 
		gid = grib_new_from_file(fin)	
		
		if gid is None:
			break
        
		# 获取网格基础数据信息
		shortName = grib_get(gid,'shortName')
		dataDate = grib_get(gid,'dataDate') # 日期 20170627
		dataTime = grib_get(gid,'dataTime') # 时次 0或1200
		forecastTime = grib_get(gid,'forecastTime') # 时效 0
		level = grib_get(gid,'level') # 层级 0
		xCount = grib_get(gid,'Ni')
		yCount = grib_get(gid,'Nj')
		lonStart = grib_get(gid, "longitudeOfFirstGridPointInDegrees")
		latStart = grib_get(gid, "latitudeOfFirstGridPointInDegrees")
		lonEnd = grib_get(gid, "longitudeOfLastGridPointInDegrees")
		latEnd = grib_get(gid, "latitudeOfLastGridPointInDegrees")
		lonGap = grib_get(gid, "iDirectionIncrementInDegrees")
		latGap = grib_get(gid, "jDirectionIncrementInDegrees")
		values = grib_get_values(gid)
		
		year = str(dataDate)[0:4]
		month = str(dataDate)[4:6]
		day = str(dataDate)[6:8]
		
		if dataTime == 0:
			dataTime = '00'
		elif dataTime == 1200:
			dataTime = '12' 
		
		if count == 1:
			fileName = str(dataDate) + str(dataTime) + "." + str(forecastTime).zfill(3)
			output = rootPath + "/" + fileName
			fout = file(output,'w')
		
			# 写入描述
			global shrtName
			if shortName == "10u" or shortName == "10v":
				shrtName = "wind10m"
			fout.write("diamond 11 " + fileName + "_" + shrtName + "\n")
			fout.write(year + "\t" + month + "\t" + day + "\t" + str(dataTime) + "\t" + str(forecastTime) + "\t" + str(level) + "\t" + str(lonGap) + "\t-" + str(latGap) + "\t" + str(lonStart) + "\t" + str(lonEnd) + "\t" + str(latStart) + "\t" + str(latEnd) +"\t" + str(xCount) +"\t" + str(yCount) + "\n")
		
		L1 = []
		L2 = []
		L3 = [] # 存放网格二位数组数据值
		
		pointNum = len(values)
		for i in range(0, pointNum, xCount):
			L1.append(i)
			
		for j in range(xCount, pointNum + 1, xCount):
			L2.append(j)
		
		# 将一位数组转为二位数组
		for idx in range(0, len(L1)):
			L3.append(values[L1[idx]:L2[idx]])
		
		# 将数据写入文件，每10个一行
		for row in range(0, yCount):
			for col in range(0, xCount):
				if col != 0 and col % 10 == 0:	
					fout.write("\n" + str(round(L3[row][col], 2)) + "\t")
				else:
					fout.write(str(round(L3[row][col], 2)) + "\t")
					
		grib_release(gid)
		count = count + 1
 
	fout.close()
	fin.close()
 
def main():
	try:
		timeStart = time.time()
		grib2ToMicaps4()
		timeEnd = time.time()
		spendTime = timeEnd - timeStart
		print "GRIB2 to MICAPS4 successfully, spend " + str(round(spendTime, 3)) + " seconds. File path is:[" + output + "]"
	except GribInternalError,err:
		if VERBOSE:
			traceback.print_exc(file=sys.stderr)
		else:
			print >>sys.stderr,err.msg
 
		return 1
 
if __name__ == "__main__":
	sys.exit(main())
