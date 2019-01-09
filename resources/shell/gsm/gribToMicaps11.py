# coding=utf-8
# Filename: gribToMicaps11.py
# Description: 将grib转为micaps第11类文件
# Date: 2017-11-22
# Author: songwj
# Version: v1
import traceback
import sys
import time
import os
import math
import datetime

from gribapi import *
from numpy import arange
from scipy.interpolate import griddata
 
INPUT = sys.argv[1] # 原始文件全路径
rootPath = sys.argv[2] # 文件输出的根目录
# 提取的区域范围及经纬度格距
lon_start = float(sys.argv[3]) # 起始经度
lon_end = float(sys.argv[4]) # 终止经度
lat_start = float(sys.argv[5]) # 起始纬度
lat_end = float(sys.argv[6]) # 终止纬度
lon_gap = float(sys.argv[7]) # 经度格距
lat_gap = float(sys.argv[8]) # 纬度格距
VERBOSE = 1 # verbose error reporting

# 获取当前日期的后一天时间，格式：YYYYMMDD
def getNextDay(currentTime): 
	dttime = datetime.datetime.strptime(str(currentTime), "%Y%m%d")
	dttime = dttime + datetime.timedelta(hours=int(24))
	nextDay = datetime.datetime.strftime(dttime, "%Y%m%d")
	return nextDay

def gribToMicaps11():
	timeStart = time.time()
	if not os.path.exists(INPUT):
		print "GRIB file does not exist: [" + INPUT + "]"
		return 3
	
	fileSize = os.path.getsize(INPUT) / 1024
	
	if fileSize <= 10:
		print "The file size is incorrect, the current file [" + INPUT + "] size is " + str(round(fileSize, 2)) + "KB"
		return 4
	
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
		
		# 将原始数据转为二维数组
		values2D = values.reshape(yCount, xCount)
		# 计算提取区域所处的下标范围（提取的原始区域数据大于或等于待插值区域）
		xStart = int(math.floor((lon_start - lonStart) / lonGap))
		yStart = int(math.floor((latStart - lat_start) / latGap))
		xEnd = int(math.ceil((lon_end - lonStart) / lonGap))
		#yEnd = int(math.ceil((latStart - lat_end) / latGap))
		yEnd = int(math.ceil((latStart - (-5)) / latGap)) # 日本的终止纬度只到-5
		
		# 原始区域数据提取
		regionSrcVals = []
		for row in range(yStart, yEnd + 1):
			for col in range(xStart, xEnd + 1):
				regionSrcVals.append(values2D[row][col])
				
		# 计算提取原始区域所处的下标范围
		regionSrcLonStart = lonStart + xStart * lonGap
		regionSrcLatStart = latStart - yStart * latGap
		regionSrcLonEnd = lonStart + xEnd * lonGap
		regionSrcLatEnd = latStart - yEnd * latGap
		
		# 存放原始数据中的经纬度点
		lons = []
		lats = []
		
		for y in arange(regionSrcLatStart, regionSrcLatEnd - latGap, -latGap): # 纬度
			for x in arange(regionSrcLonStart, regionSrcLonEnd + lonGap, lonGap): # 经度
				lons.append(x)
				lats.append(y)
				
		# 存放指定区域待插值点
		regionPoints = []
		
		for y in arange(lat_start, lat_end - lat_gap, -lat_gap): # 纬度
			for x in arange(lon_start, lon_end + lon_gap, lon_gap): # 经度
				regionPoints.append([x, y])
		
		# 插值
		interVals = griddata((lons, lats), regionSrcVals, regionPoints, method="linear", fill_value=9999.0)
		# 计算指定提取的区域经纬向格点数
		x_count = int(abs(lon_end - lon_start) / abs(lon_gap)) + 1
		y_count = int(abs(lat_start - lat_end) / abs(lat_gap)) + 1
		# 一位转二维
		interVals2D = interVals.reshape(y_count, x_count)
		
		if dataTime == 0:
			dataTime = '08'
		elif dataTime == 600:
			dataTime = '14'
		elif dataTime == 1200:
			dataTime = '20'
		elif dataTime == 1800:
			dataTime = '02'
			dataDate = getNextDay(dataDate)
		
		dataDate = str(dataDate)[2:] # 170627
		year = str(dataDate)[:2] # 17
		month = str(dataDate)[2:4] # 06
		day = str(dataDate)[4:] # 27
		
		if count == 1:
			fileName = str(dataDate) + str(dataTime) + "." + str(forecastTime).zfill(3)
			output = rootPath + "/" + fileName
			fout = file(output,'w')
			
			# 写入描述
			global shrtName
			if shortName == "10u" or shortName == "10v":
				shrtName = "10m风场_0.1*0.1"
			elif shortName == "100u" or shortName == "100v":
				shrtName = "100m风场_0.1*0.1"
			fout.write("diamond 11 " + fileName + "_" + shrtName + "\n")
			fout.write(year + "\t" + month + "\t" + day + "\t" + str(dataTime) + "\t" + str(forecastTime).zfill(3) + "\t" + str(level) + "\t" + str(lon_gap) + "\t-" + str(lat_gap) + "\t" + str(lon_start) + "\t" + str(lon_end) + "\t" + str(lat_start) + "\t" + str(lat_end) +"\t" + str(x_count) +"\t" + str(y_count) + "\n")
		
		# 将数据写入文件，每10个一行
		for row in range(0, y_count):
			for col in range(0, x_count):
				val = interVals2D[row][col]
				col = col + 1
				
				if col == x_count:
					fout.write(str(round(val, 2)) + "\n")
				elif col % 10 == 0:	
					fout.write(str(round(val, 2)) + "\n")
				else:
					fout.write(str(round(val, 2)) + "\t")
					
		grib_release(gid)
		count = count + 1
	
	if fout is not None:
		fout.close()
	if fin is not None:
		fin.close()
	
	timeEnd = time.time()
	spendTime = timeEnd - timeStart
	print "GRIB to MICAPS11 successfully, spend " + str(round(spendTime, 3)) + " seconds. File path is:[" + output + "]"
 
def main():
	try:
		gribToMicaps11()
	except GribInternalError,err:
		if VERBOSE:
			traceback.print_exc(file=sys.stderr)
		else:
			print >>sys.stderr,err.msg
 
		return 5
 
if __name__ == "__main__":
	sys.exit(main())
