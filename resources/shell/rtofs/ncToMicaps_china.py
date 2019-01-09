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
	
	chinaRootPath = outputRootPath + getYesterday() + "/china"
	
	if not os.path.exists(chinaRootPath):
		os.makedirs(chinaRootPath)
	
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
	
	# 输出中国区域数据
	fName = currentTime + "." + tmlimit
	chinaFilePath = chinaRootPath + "/" + fName
	
	# 如果文件已经存在则不再处理
	if os.path.exists(chinaFilePath):
		print "China region file already exists. File name is: [" + chinaFilePath + "]"
		return 3
	
	# 中国区域范围
	chinaLonStart = 90.0 # 起始经度
	chinaLonEnd = 150.0 # 终止经度
	chinaLatStart = 60.0 # 起始纬度
	chinaLatEnd = -10.0 # 终止纬度
	chinaLonGap = 0.125 # 经度格距
	chinaLatGap = -0.125 # 纬度格距
	# 计算中国区域经纬向格点数
	chinaXCount = int(abs(chinaLonEnd - chinaLonStart) / chinaLonGap) + 1
	chinaYCount = int(abs(chinaLatStart - chinaLatEnd) / abs(chinaLatGap)) + 1
	chinaPntNum = chinaXCount * chinaYCount
	
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
	
	# 存放中国区域待插值点
	chinaInterpPnts = []
	# 获取中国区域所有待插值点的经纬度
	for y in arange(60.0, -10.125, -0.125): # 纬度
		for x in arange(90.0, 150.125, 0.125): # 经度
			chinaInterpPnts.append([x, y])
	
	correctLonsChina = []
	latsChina = []
	uVelocitysChina = []
	vVelocitysChina = []
	
	# 获取中国区域原始数据（比提取区域略大）
	for idx in range(0, pointNum):
		lon = correctLons[idx]
		lat = lats[idx]
		if lon >= 89.9 and lon <= 150.1 and lat >= -10.1 and lat <= 60.1:
			correctLonsChina.append(lon)
			latsChina.append(lat)
			uVelocitysChina.append(uVelocitys[idx])
			vVelocitysChina.append(vVelocitys[idx])
	
	#print len(correctLonsChina), len(latsChina), len(uVelocitysChina), len(vVelocitysChina)
	
	# 中国区域插值
	uVelChinaVals = griddata((correctLonsChina, latsChina), uVelocitysChina, chinaInterpPnts, method="linear", fill_value=9999.0)
	vVelChinaVals = griddata((correctLonsChina, latsChina), vVelocitysChina, chinaInterpPnts, method="linear", fill_value=9999.0)
	# 释放内存
	del correctLons[:]
	del lats
	del uVelocitys
	del vVelocitys
	del chinaInterpPnts[:]
	
	# 创建文件对象
	fChinaOut = file(chinaFilePath, "w")
	# ===============================中国区域数据==============================================
	fChinaOut.write("diamond 11 sea_current\n")
	fChinaOut.write(year + "\t" + month + "\t" + day + "\t" + hour + "\t" + tmlimit + "\t" + str(layer) + "\t" + str(chinaLonGap) + "\t" + str(chinaLatGap) + "\t" + str(chinaLonStart) + "\t" + str(chinaLonEnd) + "\t" + str(chinaLatStart) + "\t" + str(chinaLatEnd) +"\t" + str(chinaXCount) +"\t" + str(chinaYCount) + "\n")
	
	L1 = range(0, chinaPntNum, chinaXCount)
	L2 = range(chinaXCount, chinaPntNum + 1, chinaXCount)
	chinaUVelVals = [] # 存放水平流速二维数组数据值
	chinaVVelVals = [] # 存放垂直流速二维数组数据值
	
	# 将一位数组转为二位数组
	for idx in range(0, len(L1)):
		chinaUVelVals.append(uVelChinaVals[L1[idx]:L2[idx]])
		chinaVVelVals.append(vVelChinaVals[L1[idx]:L2[idx]])
	
	# 释放内存
	del uVelChinaVals
	del vVelChinaVals
	
	# 将水平流速数据写入文件，每10个一行
	for row in range(0, chinaYCount):
		for col in range(0, chinaXCount):
			uVal = chinaUVelVals[row][col]
			if not (uVal >= -2.030302 and uVal <= 2.2808383):
				uVal = 9999.0
			if col != 0 and col % 10 == 0:	
				fChinaOut.write("\n" + str(round(uVal, 2)) + "\t")
			else:
				fChinaOut.write(str(round(uVal, 2)) + "\t")

	# 将垂直流速数据写入文件，每10个一行
	for row in range(0, chinaYCount):
		for col in range(0, chinaXCount):
			vVal = chinaVVelVals[row][col]
			if not (vVal >= -2.1668057 and vVal <= 2.6496463):
				vVal = 9999.0
			if col != 0 and col % 10 == 0:	
				fChinaOut.write("\n" + str(round(vVal, 2)) + "\t")
			else:
				fChinaOut.write(str(round(vVal, 2)) + "\t")
	
	# 释放内存
	del chinaUVelVals[:]
	del chinaVVelVals[:]
	
	# 释放资源
	fChinaOut.close()
	nc.close()
 
	timeEnd = time.time()
	spendTime = timeEnd - timeStart
	print "Convert NetCDF to MICAPS successfully, spend " + str(round(spendTime, 3)) + " seconds."
	print "China file path is: [" + chinaFilePath + "]"
	
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
