#coding=utf-8
# Filename: rain_nc2json.py
# Description: Process NetCDF rain 1hr file and convert to JSON
# Date: 2018-07-20
# Author: songwj
# Version: v1.0
import traceback
import sys
import time
import os

from netCDF4 import Dataset
import numpy as np
from scipy import interpolate
import simplejson as json

inputFilePath = sys.argv[1] # /space/data2/tmp/RAIN/Z_SURF_C_BABJ_20180617001147_P_CMPA_NRT_CHN_0P01_HOR-PRE-2018061500.nc
VERBOSE = 1 # verbose error reporting
# 北京区域范围
lonStart = 115.0 # 起始经度
lonEnd = 117.5 # 终止经度
latStart = 39.0 # 起始纬度
latEnd = 41.5 # 终止纬度
lonGap = 0.01 # 经度格距
latGap = 0.01 # 纬度格距

def ncToJson():
	timeStart = time.time()
	
	if not os.path.exists(inputFilePath):
		print "nc file does not exist: " + inputFilePath
		return 1
	
	fileName = os.path.basename(inputFilePath)
	
	if not fileName.endswith(".nc"):
		print "Filename must end with '.nc', current filename is: [" + fileName + "]"
		return 2
	
	nc = Dataset(inputFilePath, "r")
	
	# 获取基础数据
	hours = nc.dimensions["time"].size
	xCount = nc.dimensions["latitude"].size
	yCount = nc.dimensions["longitude"].size
	lats = nc.variables["latitude"]
	lons = nc.variables["longitude"]
	rain = nc.variables["rain"][:]
	rain2D = rain[0]
	
	# 反转数组
	rain2D = rain2D[::-1]
	# 计算中国区域经纬向格点数
	bjXCount = int(abs(lonEnd - lonStart) / lonGap) + 1
	bjYCount = int(abs(latStart - latEnd) / abs(latGap)) + 1
	# 获取指定提取区域纬度和经度一位数组（必须都为升序排列，lat:↑，lon:→）
	interLats = np.linspace(latStart, latEnd, bjYCount)
	interLons = np.linspace(lonStart, lonEnd, bjXCount)
	# 插值
	function = interpolate.RectBivariateSpline(lats, lons, rain2D)
	interVals2D = function(interLats, interLons)
	# 反转数组
	interVals2D = interVals2D[::-1]
	bjInterpData = interVals2D.flatten().tolist()
	pntNums = len(bjInterpData)
	data = []
	
	for idx in range(0, pntNums):
		data.append(abs(round(bjInterpData[idx], 6)))
	
	finalData = {}
	finalData["latitudeGridNumber"] = bjXCount
	finalData["longitudeGridNumber"] = bjYCount
	finalData["startLongitude"] = lonStart
	finalData["endLongitude"] = lonEnd
	finalData["startLatitude"] = latStart
	finalData["endLatitude"] = latEnd
	finalData["latitudeGridSpace"] = latGap
	finalData["longitudeGridSpace"] = lonGap
	finalData["data"] = data
	# 将数据转为JSON串
	jsonStr = json.dumps(finalData)
	outputFileRootPath = "/space/data2/tmp/json/RAIN/"
	
	if not os.path.exists(outputFileRootPath):
		os.makedirs(outputFileRootPath)
	
	fileName = fileName.split(".")[0]
	
	outputFilePath = outputFileRootPath + fileName + ".json"
	
	fout = file(outputFilePath, 'w')
	fout.write("[" + jsonStr + "]")
	
	# 释放资源
	fout.close()
	nc.close()
	
	timeEnd = time.time()
	spendTime = timeEnd - timeStart
	print "Convert NetCDF to JSON successfully, spend " + str(round(spendTime, 3)) + " seconds."
	print "File path is: [" + outputFilePath + "]"
	
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
