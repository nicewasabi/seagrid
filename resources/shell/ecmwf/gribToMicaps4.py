# coding=utf-8
# Filename: gribToMicaps4.py
# Description: 将grib转为micaps第4类文件
# Date: 2017-11-21
# Author: songwj
# Version: v4
import traceback
import sys
import time
import os
import math

from gribapi import *
import numpy as np
from scipy import interpolate
 
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

def getChineseByShortName(shortName, level):
	if shortName == 'swh':
		return "有效波高"
	elif shortName == 'shww':
		return "有效风浪高度"
	elif shortName == '10fg3':
		return "阵风10m3小时"
	elif shortName == 'lcc':
		return "低云量"
	elif shortName == 'vis':
		return "能见度"
	elif shortName == 'r' and level == 850:
		return "相对湿度850hPa"
	elif shortName == 'r' and level == 1000:
		return "相对湿度1000hPa"
	elif shortName == 't' and level == 925:
		return "气温925hPa"
	elif shortName == 't' and level == 1000:
		return "气温1000hPa"
	elif shortName == 'sf':
		return "降雪量"
	elif shortName == 'tcc':
		return "总云量"
	elif shortName == 'tp':
		return "总降水量"
	elif shortName == 'r2':
		return "相对湿度2m"
	elif shortName == 'sst':
		return "海表面温度"
	elif shortName == '2t':
		return "2m温度"
	elif shortName == '2d':
		return "2m露点温度"
	else:
		return shortName

def getVarOutByShortName(shortName, level):
	if shortName == 'swh':
		return [0.500, 0.000, 20.000, 0.000, 5.000]
	elif shortName == 'shww':
		return [1.0, 0.0, 12.0, 1.0, 5.0]
	elif shortName == '10fg3':
		return [2.000, 0.000, 70.000, 0.000, 10.800]	
	elif shortName == 'lcc':
		return [1.000, 0.000, 10.000, 1.000, 5.000]
	elif shortName == 'vis':
		return [2.000, 0.000, 50.000, 0.000, 5.000]
	elif shortName == 'r' and level == 850:
		return [5.000, 0.000, 100.000, 1.000, 80.000]
	elif shortName == 'r' and level == 1000:
		return [5.000, 0.000, 100.000, 1.000, 80.000]
	elif shortName == 'sf':
		return [1.000, 0.000, 100.000, 1.000, 5.000]
	elif shortName == 'tcc':
		return [1.000, 0.000, 10.000, 1.000, 5.000]
	elif shortName == 'tp':
		return [5.000, 0.000, 1000.000, 1.000, 50.000]
	elif shortName == 'r2':
		return [0.500, 0.000, 20.000, 0.000, 5.000]
	elif shortName == 'sst' or shortName == 't' or shortName == '2t' or shortName == '2d':
		return [2.0, -40.0, 40.0, 0.0, 20.0]
	else:
		return [1, 0, 10, 0, 1]
	
def gribToMicaps4():
	if not os.path.exists(INPUT):
		print "GRIB file does not exist: [" + INPUT + "]" 
		return 6

	if not os.path.exists(rootPath):
		os.makedirs(rootPath)
		
	fin = open(INPUT)
	
	while 1: 
		timeStart = time.time()
		gid = grib_new_from_file(fin)
 	
		if gid is None:
			break
        
		# 获取网格基础数据信息
		shortName = grib_get(gid,'shortName')
		dataDate = grib_get(gid,'dataDate') # 日期 20170627
		dataTime = grib_get(gid,'dataTime') # 时次 0或1200
		forecastTime = grib_get(gid,'endStep') # 时效，如：240
		level = grib_get(gid,'level') # 层级 0
		xCount = grib_get(gid,'Ni')
		yCount = grib_get(gid,'Nj')
		lonStart = grib_get(gid, "longitudeOfFirstGridPointInDegrees")
		latStart = grib_get(gid, "latitudeOfFirstGridPointInDegrees")
		lonEnd = grib_get(gid, "longitudeOfLastGridPointInDegrees")
		latEnd = grib_get(gid, "latitudeOfLastGridPointInDegrees")
		lonGap = grib_get(gid, "iDirectionIncrementInDegrees")
		latGap = grib_get(gid, "jDirectionIncrementInDegrees")
		maximum = grib_get(gid, "maximum")
		minimum = grib_get(gid, "minimum")
		values = grib_get_values(gid)
		
		# 将原始数据转为二维数组
		values2D = values.reshape(yCount, xCount)
		# 计算提取区域所处的下标范围（提取的原始区域数据大于或等于待插值区域）
		xStart = int(math.floor((lon_start - lonStart) / lonGap))
		yStart = int(math.floor((latStart - lat_start) / latGap))
		xEnd = int(math.ceil((lon_end - lonStart) / lonGap))
		yEnd = int(math.ceil((latStart - lat_end) / latGap))
		# 提取原始区域的的终止行列
		regionSrcRowEnd = yEnd + 1
		regionSrcColEnd = xEnd + 1
		# 获取原始提取区域的数据（二维数组）
		regionSrcVals2D = values2D[yStart:regionSrcRowEnd, xStart:regionSrcColEnd]
		
		# 计算提取原始区域所处的下标范围
		regionSrcLonStart = lonStart + xStart * lonGap
		regionSrcLatStart = latStart - yStart * latGap
		regionSrcLonEnd = lonStart + xEnd * lonGap
		regionSrcLatEnd = latStart - yEnd * latGap
		# 提取的原始数据区域的纬向和经向格点数
		regionSrcXCount = xEnd - xStart + 1
		regionSrcYCount = yEnd - yStart + 1
		# 获取原始区域数据的纬度和经度一位数组（必须都为升序排列，lat:↑，lon:→）
		lats = np.linspace(regionSrcLatEnd, regionSrcLatStart, regionSrcYCount)
		lons = np.linspace(regionSrcLonStart, regionSrcLonEnd, regionSrcXCount)
		# 计算指定提取区域经纬向格点数
		x_count = int(abs(lon_end - lon_start) / abs(lon_gap)) + 1
		y_count = int(abs(lat_start - lat_end) / abs(lat_gap)) + 1
		# 获取指定提取区域纬度和经度一位数组（必须都为升序排列，lat:↑，lon:→）
		interLats = np.linspace(lat_end, lat_start, y_count)
		interLons = np.linspace(lon_start, lon_end, x_count)
		# 插值
		function = interpolate.RectBivariateSpline(lats, lons, regionSrcVals2D)
		interVals2D = function(interLats, interLons)
		
		dataDate = str(dataDate)[2:] # 170627
		year = str(dataDate)[:2] # 17
		month = str(dataDate)[2:4] # 06
		day = str(dataDate)[4:] # 27
		
		if dataTime == 0:
			dataTime = '08'
		elif dataTime == 1200:
			dataTime = '20' 
		
		filename = str(dataDate) + str(dataTime) + "." + str(forecastTime).zfill(3)
		output = rootPath + "/" + filename
		fout = file(output,'w')
		
		# 写入描述
		fout.write("diamond 4 " + filename + "_" + getChineseByShortName(shortName, level) + "_0.1*0.1\n")
		fout.write(year + "\t" + month + "\t" + day + "\t" + str(dataTime) + "\t" + str(forecastTime).zfill(3) + "\t" + str(level) + "\t" + str(lon_gap) + "\t-" + str(lat_gap) + "\t" + str(lon_start) + "\t" + str(lon_end) + "\t" + str(lat_start) + "\t" + str(lat_end) +"\t" + str(x_count) +"\t" + str(y_count) + "\n")
		varout = getVarOutByShortName(shortName, level)
		fout.write(str(varout[0]) + "\t" + str(varout[1]) + "\t" + str(varout[2]) + "\t" + str(varout[3]) + "\t" + str(varout[4]) + "\n")
		
		# 将数据写入文件，每10个一行
		for row in range(0, y_count):
			for col in range(0, x_count):
				val = interVals2D[row][col]
				
				if shortName == 'swh' or shortName == 'shww':
					if not (val >= minimum and val <= maximum):
						val = 9999.0
				if shortName == 'vis':
					if val != 9999.0:
						val = val / 1000
				if shortName == 'tp' or shortName == 'sf':
					if val != 9999.0:
						val = val * 1000
				if shortName == 'sst':
					if val > 500:
						val = 9999.0
					elif val > maximum:
						val = maximum
					elif val < minimum:
						val = minimum
						
				col = col + 1
				
				if col == x_count:
					fout.write(str(round(val, 2)) + "\n")
				elif col % 10 == 0:	
					fout.write(str(round(val, 2)) + "\n")
				else:
					fout.write(str(round(val, 2)) + "\t")
					
		grib_release(gid)
		fout.close()
		timeEnd = time.time()
		spendTime = timeEnd - timeStart
		print "GRIB to MICAPS4 successfully, spend " + str(round(spendTime, 3)) + " seconds. File path is:[" + output + "]"
 
	fin.close()
 
def main():
	try:
		gribToMicaps4()
	except GribInternalError,err:
		if VERBOSE:
			traceback.print_exc(file=sys.stderr)
		else:
			print >>sys.stderr,err.msg
 
		return 7
 
if __name__ == "__main__":
	sys.exit(main())
