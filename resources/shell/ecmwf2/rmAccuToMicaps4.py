# coding=utf-8
# Filename: rmAccuToMicaps4.py
# Description: 将sf（降雪）、tp（降水）等累加数据处理为非累加数据并转为micaps第4类文件输出
# Date: 2018-08-21
# Author: songwj
# Version: v1.0
import traceback
import sys
import time
import os
import math

from gribapi import *
from numpy import array

INPUT = sys.argv[1] # 原始MICAPS文件根目录：/space/data2/ecData/dsmicaps/sf
rootPath = sys.argv[2] # 文件输出的根目录：/space/data2/ecData/rmAccuDsmicaps/sf
datetime = sys.argv[3] # 文件起报时间，eg：18081908
endStep = sys.argv[4] # 处理的时效，eg：003
VERBOSE = 1 # verbose error reporting
	
def rmAccuToMicaps4():
	if not os.path.exists(rootPath):
		os.makedirs(rootPath)
	
	if endStep == '000':
		print "End step is 000, no need to process."
		return
	
	timeStart = time.time()
	# 存放所有时效
	timelimits = []
	
	# 获取所有时效000-072为逐3小时，078-240为逐24小时
	for timelimit in range(0, 75, 3):
		timelimits.append(str(timelimit).zfill(3))
		
	for timelimit in range(78, 246, 6):
		timelimits.append(str(timelimit).zfill(3))
	
	# 获取指定时效所在集合下标，并获取上一个下标时效值
	idx = timelimits.index(endStep)
	endStep0 = timelimits[idx - 1]
	
	# 将相连下一个时效对应格点数据减去上一个时效对应格点数据，输出为MICAPS第4类，如：003 - 000，006 - 003，依此类推
	fileName1 = datetime + "." + endStep0 # 17072608.000
	fileName2 = datetime + "." + endStep # 17072608.003
	srcFilePath1 = INPUT + "/" + fileName1
	srcFilePath2 = INPUT + "/" + fileName2
	
	if not os.path.exists(srcFilePath1):
		print "MICAPS4 file does not exist: [" + srcFilePath1 + "]" 
		return
		
	if not os.path.exists(srcFilePath2):
		print "MICAPS4 file does not exist: [" + srcFilePath2 + "]" 
		return
		
	file1 = open(srcFilePath1)
	file2 = open(srcFilePath2)
	
	output = rootPath + "/" + fileName2
	fout = file(output,'w')

	# 写入描述，输出的文件名和头描述信息和后一时效的一致
	fout.write(file2.readline())
	line2 = file2.readline()
	fout.write(line2)
	fout.write(file2.readline())
	# 获取纬向和经向格点数
	strDatas = line2.replace("\n", '').split("\t")
	xCount = int(strDatas[12])
	yCount = int(strDatas[13])
	# 跳过前一个时效文件的前两行
	file1.readline()
	file1.readline()
	file1.readline()
	# 将数据存为一位数组
	dataArray1 = []
	dataArray2 = []
	
	for line in file1.readlines():
		datas = line.replace("\n", '').split("\t")
		for idx in range(0, len(datas)):
			dataArray1.append(datas[idx])
			
	for line in file2.readlines():
		datas = line.replace("\n", '').split("\t")
		for idx in range(0, len(datas)):
			dataArray2.append(datas[idx])
			
	# 存放后时效减前时效的结果值
	finalArray = []
	
	for idx in range(0, len(dataArray1)):
		finalArray.append(float(dataArray2[idx]) - float(dataArray1[idx]))
		
	finalArray2D = array(finalArray).reshape(yCount, xCount)

			# 将数据写入文件，每10个一行
	for row in range(0, yCount):
		for col in range(0, xCount):
			val = finalArray2D[row][col]
			col = col + 1
			
			if col == xCount:
				fout.write(str(round(val, 2)) + "\n")
			elif col % 10 == 0:	
				fout.write(str(round(val, 2)) + "\n")
			else:
				fout.write(str(round(val, 2)) + "\t")
	
	fout.close()
	file1.close()
	file2.close()
	timeEnd = time.time()
	spendTime = timeEnd - timeStart
	print "Remove accumulator and generate MICAPS4 successfully, spend " + str(round(spendTime, 3)) + " seconds. File path is:[" + output + "]"
		
def main():
	try:
		rmAccuToMicaps4()
	except GribInternalError,err:
		if VERBOSE:
			traceback.print_exc(file=sys.stderr)
		else:
			print >>sys.stderr,err.msg
 
		return 10
 
if __name__ == "__main__":
	sys.exit(main())
