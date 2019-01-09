#coding=utf-8
# Filename: nc2json.py
# Description: Process NetCDF file and convert to JSON
# Date: 2018-07-19
# Author: songwj
# Version: v1.0
import traceback
import sys
import time
import os

from netCDF4 import Dataset
from numpy import array, arange
import simplejson as json

inputFilePath = sys.argv[1] # /root/test/SHOUQI/2018061508/AQI_Grid_2018061508.nc
dtime = sys.argv[2] # 2018061508
VERBOSE = 1 # verbose error reporting

def ncToJson():
	timeStart = time.time()
	
	if not os.path.exists(inputFilePath):
		print "nc file does not exist: " + inputFilePath
		return 1
	
	nc = Dataset(inputFilePath, "r")
	
	# 获取基础数据
	hours = nc.dimensions["time"].size
	xCount = nc.dimensions["lat"].size
	yCount = nc.dimensions["lon"].size
	pointNum = xCount * yCount * hours
	lat = nc.variables["lat"]
	lon = nc.variables["lon"]
	pm25 = nc.variables["PM25"][:]
	pm10 = nc.variables["PM10"][:]
	so2 = nc.variables["SO2"][:]
	no2 = nc.variables["NO2"][:]
	co = nc.variables["CO"][:]
	o3 = nc.variables["O3"][:]
	aqi = nc.variables["AQI"][:]
	
	# 将numpy多维数组转为一维数组并转为Python自带list类型
	lat = array(lat).tolist()
	lon = array(lon).tolist()
	pm25 = array(pm25).flatten().tolist()
	pm10 = array(pm10).flatten().tolist()
	so2 = array(so2).flatten().tolist()
	no2 = array(no2).flatten().tolist()
	co = array(co).flatten().tolist()
	o3 = array(o3).flatten().tolist()
	aqi = array(aqi).flatten().tolist()
	
	# 北京区域范围
	lonStart = 115.0 # 起始经度
	lonEnd = 117.5 # 终止经度
	latStart = 39.0 # 起始纬度
	latEnd = 41.5 # 终止纬度
	lonGap = 0.05 # 经度格距
	latGap = 0.05 # 纬度格距
	
	datas = [pm25, pm10, so2, no2, co, o3, aqi]
	params = ["PM25", "PM10", "SO2", "NO2", "CO", "O3", "AQI"]
	
	for param in params:
		finalData = {}
		finalData["time"] = hours
		finalData["latitudeGridNumber"] = xCount
		finalData["longitudeGridNumber"] = yCount
		finalData["startLongitude"] = lonStart
		finalData["endLongitude"] = lonEnd
		finalData["startLatitude"] = latStart
		finalData["endLatitude"] = latEnd
		finalData["latitudeGridSpace"] = latGap
		finalData["longitudeGridSpace"] = lonGap
		finalData["data"] = datas[params.index(param)]
		
		# 将数据转为JSON串
		jsonStr = json.dumps(finalData)
		outputFileRootPath = "/root/test/SHOUQI/" + dtime
		
		if not os.path.exists(outputFileRootPath):
			os.makedirs(outputFileRootPath)
		
		outputFilePath = outputFileRootPath + "/" + param + "_Grid_" + dtime + ".json"
		
		fout = file(outputFilePath, 'w')
		fout.write("[" + jsonStr + "]")
		
		# 释放资源
		fout.close()
		print "File path is: [" + outputFilePath + "]"
		
	nc.close()
 
	timeEnd = time.time()
	spendTime = timeEnd - timeStart
	print "Convert NetCDF to JSON successfully, spend " + str(round(spendTime, 3)) + " seconds."
	
def main():
	try:
		ncToJson()
	except Exception:
		if VERBOSE:
			traceback.print_exc(file=sys.stderr)
		else:
			print >>sys.stderr,err.msg
 
		return 4
 
if __name__ == "__main__":
	sys.exit(main())
