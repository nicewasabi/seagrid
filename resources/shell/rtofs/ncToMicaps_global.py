#coding=utf-8
import traceback
import sys
import time
import os
import datetime

from netCDF4 import Dataset
from numpy import array, arange
from scipy.interpolate import griddata

timelimit = sys.argv[1] # 时效
dayOrHour = sys.argv[2] # 日或小时(daily, 1hrly)
inputRootPath = "/space/data2/gribdata/rtofs/"
outputRootPath = "/space/data2/dsmicaps/rtofs/"
VERBOSE = 1 # verbose error reporting

# 获取昨天日期
def getYesterday(): 
	today = datetime.date.today() 
	oneday = datetime.timedelta(days=1)
	yesterday = datetime.datetime.strftime(today - oneday, "%Y%m%d")
	return yesterday

def ncToMicaps():
	timeStart = time.time()
	inputFilePath = inputRootPath + getYesterday() + "/rtofs_glo_2ds_f" + timelimit + "_" + dayOrHour + "_prog.nc"
	
	if not os.path.exists(inputFilePath):
		print "nc file does not exist: " + inputFilePath
		return 1
	
	# 获取文件大小，正常文件的大小为415581844字节，约合396.33MB
	fileSize = os.path.getsize(inputFilePath) / 1024 / 1024
	
	if fileSize < 396:
		print "The file size is incorrect, the current file [" + inputFilePath + "] size is " + str(round(fileSize, 2)) + "MB"
		return 2
	
	globalRootPath = outputRootPath + getYesterday() + "/global"
	
	if not os.path.exists(globalRootPath):
		os.makedirs(globalRootPath)	
		
	nc = Dataset(inputFilePath, "r")
	
	# 获取基础数据
	xCount = nc.dimensions["X"].size
	yCount = nc.dimensions["Y"].size
	pointNum = xCount * yCount
	layer = nc.dimensions["Layer"].size
	date = int(nc.variables["Date"][0])
	lons = nc.variables["Longitude"][:]
	lats = nc.variables["Latitude"][:]
	uVelocitys = nc.variables["u_velocity"][:]
	vVelocitys = nc.variables["v_velocity"][:]
	# 二维转一维数组
	lons = array(lons).flatten()
	lats = array(lats).flatten()
	uVelocitys = array(uVelocitys).flatten()
	vVelocitys = array(vVelocitys).flatten()
	# 计算获取文件起报时间
	global dttime, tmlimit
	dttime = datetime.datetime.strptime(str(date), "%Y%m%d")
	if dayOrHour == "1hrly":
		tmlimit = "000"
	else:
		dttime = dttime + datetime.timedelta(hours=-int(timelimit))
		tmlimit = timelimit
	currentTime = dttime.strftime("%Y%m%d%H") # 2017072700
	year = currentTime[:4]
	month = currentTime[4:6]
	day = currentTime[6:8]
	hour = currentTime[8:]
	
	# 输出全球数据
	fName = currentTime + "." + tmlimit
	globalFilePath = globalRootPath + "/" + fName
	
	# 如果文件已经存在则不再处理
	if os.path.exists(globalFilePath):
		print "Global region file already exists. File name is: [" + globalFilePath + "]"
		return 3
	
	# 全球范围
	globalLonStart = 0.0 # 起始经度
	globalLonEnd = 360.0 # 终止经度
	globalLatStart = 90.0 # 起始纬度
	globalLatEnd = -78.0 # 终止纬度
	globalLonGap = 0.125 # 经度格距
	globalLatGap = -0.125 # 纬度格距	
	# 计算全球经纬向格点数
	globalXCount = int(abs(globalLonEnd - globalLonStart) / globalLonGap) + 1
	globalYCount = int(abs(globalLatStart - globalLatEnd) / abs(globalLatGap)) + 1
	globalPntNum = globalXCount * globalYCount
	
	#print xCount, yCount, layer, date, currentTime, tmlimit
	
	# 存放矫正后的经度值
	correctLons = []
	# 经度超过360的循环减去360，直到值小于360为止
	for idx in range(0, pointNum):
		lon = lons[idx]
		
		while lon > 360:
			lon = lon - 360
		
		correctLons.append(lon)
	
	# 释放内存
	del lons
	
	# 存放全球待插值点
	globalInterpPnts = []
	# 获取全球所有待插值点的经纬度
	for y in arange(90.0, -78.125, -0.125): # 纬度
		for x in arange(0.0, 360.125, 0.125): # 经度
			globalInterpPnts.append([x, y])
			
	# 全球插值
	uVelGlobalVals = griddata((correctLons, lats), uVelocitys, globalInterpPnts, method="linear", fill_value=9999.0)
	vVelGlobalVals = griddata((correctLons, lats), vVelocitys, globalInterpPnts, method="linear", fill_value=9999.0)
	# 释放内存
	del globalInterpPnts[:]
	del correctLons[:]
	del lats
	del uVelocitys
	del vVelocitys
	
	# 创建文件对象
	fGlobalOut = file(globalFilePath, "w")
	# ===============================全球数据==============================================
	fGlobalOut.write("diamond 11 sea_current\n")
	fGlobalOut.write(year + "\t" + month + "\t" + day + "\t" + hour + "\t" + tmlimit + "\t" + str(layer) + "\t" + str(globalLonGap) + "\t" + str(globalLatGap) + "\t" + str(globalLonStart) + "\t" + str(globalLonEnd) + "\t" + str(globalLatStart) + "\t" + str(globalLatEnd) +"\t" + str(globalXCount) +"\t" + str(globalYCount) + "\n")
	
	L1 = range(0, globalPntNum, globalXCount)
	L2 = range(globalXCount, globalPntNum + 1, globalXCount)
	globalUVelVals = [] # 存放全球水平流速二维数组数据值
	globalVVelVals = [] # 存放全球垂直流速二维数组数据值
	
	# 将一位数组转为二位数组
	for idx in range(0, len(L1)):
		globalUVelVals.append(uVelGlobalVals[L1[idx]:L2[idx]])
		globalVVelVals.append(vVelGlobalVals[L1[idx]:L2[idx]])
	
	# 释放内存
	del uVelGlobalVals
	del vVelGlobalVals
	
	# 将水平流速数据写入文件，每10个一行
	for row in range(0, globalYCount):
		for col in range(0, globalXCount):
			uVal = globalUVelVals[row][col]
			if not (uVal >= -2.030302 and uVal <= 2.2808383):
				uVal = 9999.0
			if col != 0 and col % 10 == 0:	
				fGlobalOut.write("\n" + str(round(uVal, 2)) + "\t")
			else:
				fGlobalOut.write(str(round(uVal, 2)) + "\t")

	# 将垂直流速数据写入文件，每10个一行
	for row in range(0, globalYCount):
		for col in range(0, globalXCount):
			vVal = globalVVelVals[row][col]
			if not (vVal >= -2.1668057 and vVal <= 2.6496463):
				vVal = 9999.0
			if col != 0 and col % 10 == 0:	
				fGlobalOut.write("\n" + str(round(vVal, 2)) + "\t")
			else:
				fGlobalOut.write(str(round(vVal, 2)) + "\t")
	
	# 释放内存
	del globalUVelVals[:]
	del globalVVelVals[:]
	
	# 释放资源
	fGlobalOut.close()
	nc.close()
 
	timeEnd = time.time()
	spendTime = timeEnd - timeStart
	print "Convert NetCDF to MICAPS successfully, spend " + str(round(spendTime, 3)) + " seconds."
	print "Global file path is: [" + globalFilePath + "]"
	
def main():
	try:
		ncToMicaps()
	except Exception:
		if VERBOSE:
			traceback.print_exc(file=sys.stderr)
		else:
			print >>sys.stderr,err.msg
 
		return 4
 
if __name__ == "__main__":
	sys.exit(main())
