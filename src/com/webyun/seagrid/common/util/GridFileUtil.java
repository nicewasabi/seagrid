package com.webyun.seagrid.common.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.webyun.seagrid.common.config.BuoyStationInfoConfig;
import com.webyun.seagrid.common.config.WindStationConfig;
import com.webyun.seagrid.common.model.AccumulateErrorModel;
import com.webyun.seagrid.common.model.BuoyStationInfo;
import com.webyun.seagrid.common.model.Entry;
import com.webyun.seagrid.common.model.ErrorModel;
import com.webyun.seagrid.common.model.GridModel;
import com.webyun.seagrid.common.model.Micaps11GridModel;
import com.webyun.seagrid.common.model.Micaps2Point;
import com.webyun.seagrid.common.model.Palette;
import com.webyun.seagrid.common.model.Point;
import com.webyun.seagrid.common.model.StationErrorModel;
import com.webyun.seagrid.common.model.StationPoint;
import com.webyun.seagrid.common.model.WindSpdAndDirModel;

/**
 * @Project: seaGrid
 * @Title: GridFileUtil
 * @Description: 格点文件处理工具
 * @author: songwj
 * @date: 2017-8-15 下午4:04:58
 * @company: webyun
 * @Copyright: Copyright (c) 2017 Webyun. All Rights Reserved.
 * @version v1.0
 */
public class GridFileUtil {
	private static Logger log = Logger.getLogger(GridFileUtil.class.getName());
	private static DecimalFormat df = new DecimalFormat("###.0000000");
	// 数值匹配正则模版
	private static String numericPattern = FileConfigUtil.getProperty("regex.numeric");
	// 所有浮标站点信息
	private static List<BuoyStationInfo> stationInfos = BuoyStationInfoConfig.buoyStationInfos;
	
	/**
	 * @Description: 解析Micaps第2类格点文件
	 * @author: songwj
	 * @date: 2017-9-14 下午5:35:16
	 * @param micaps2FilePath
	 * @param charset 解码方式，如：utf-8
	 * @param forceReverve 是否强制将网格的起止终止经纬度处理左上角和右下角，true为是
	 * @return
	 */
	public static GridModel micaps2ToGridModel(String micaps2FilePath, String charset, boolean forceReverve) {
		// *****************文件校验开始*****************
		// 预留文件校验
		// *****************文件校验结束*****************

		GridModel gridModel = new GridModel();
		// 存储每行拆分后的字符串
		List<String> strData = new ArrayList<String>();
		BufferedReader br = null;
		
		try {
			File file = new File(micaps2FilePath);
			if (!file.exists()) {
				log.error("传入的文件不存在，该文件为：" + micaps2FilePath);
				return null;
			}
			
			// 读取文件
			br = new BufferedReader(new InputStreamReader(new FileInputStream(micaps2FilePath), charset));
			// 第一行
			String row1 = FileOperationUtil.readLine(br);
			// 描述信息
			gridModel.setDescription(row1);
			// 第二行
			String row2 = FileOperationUtil.readLine(br);
			StringUtils.split(strData, row2, StringUtils.DELIM_TAB);
			// 年
			gridModel.setYear(strData.get(0));
			// 月
			gridModel.setMonth(strData.get(1));
			// 日
			gridModel.setDay(strData.get(2));
			// 时次
			gridModel.setBatch(strData.get(3));
			// 时效
			String timelimit = StringUtils.split(file.getName(), StringUtils.DOT).get(1);
			gridModel.setTimelimit(timelimit);
			// 层级
			gridModel.setLayer(strData.get(4));
			// 获取网格总的点数
			int totalPoints = Integer.parseInt(strData.get(5));
			// 存储数据点
			Micaps2Point[] points = new Micaps2Point[totalPoints];
			// 记录下标值
			int index = 0;
			// 缺报值
			int missing = 9999;
			
			while(true){
				String row = FileOperationUtil.readLine(br);
				if(row == null){
					break;
				}
				strData = new ArrayList<String>();
				StringUtils.split(strData, row, StringUtils.DELIM_TAB);
				// 装入每行数据值
				String lon = strData.get(1);// 经度
				double x = lon.matches(numericPattern) ? Double.parseDouble(lon) : missing;
				String lat = strData.get(2);// 纬度
				double y = lat.matches(numericPattern) ? Double.parseDouble(lat) : missing;
				String temp = strData.get(6);// 温度
				double temperature = temp.matches(numericPattern) ? Double.parseDouble(temp) : missing;
				String dewPntDep = strData.get(7);// 温度露点差
				double dewPointDepression = dewPntDep.matches(numericPattern) ? Double.parseDouble(dewPntDep) : missing;
				String windDir = strData.get(8);// 风向
				double windDirection = windDir.matches(numericPattern) ? Double.parseDouble(windDir) : missing;
				String windSpd = strData.get(9);// 风速
				double windSpeed = windSpd.matches(numericPattern) ? Double.parseDouble(windSpd) : missing;
				points[index] = new Micaps2Point(x, y, temperature, dewPointDepression, windDirection, windSpeed);
				index++;
			}
			
			// 获取网格的起始终止经纬度
			double lonStart = points[0].getX();
			double latStart = points[0].getY();
			double lonEnd = points[points.length - 1].getX();
			double latEnd = points[points.length - 1].getY();
			gridModel.setLonStart(lonStart);
			gridModel.setLatStart(latStart);
			gridModel.setLonEnd(lonEnd);
			gridModel.setLatEnd(latEnd);
			// 计算经度格距
			double lonGap = formatDouble(Math.abs(points[1].getX() - lonStart), df);
			gridModel.setLonGap(lonGap);
			// 计算经纬向格点数
			int xCount = (int) (Math.abs(lonEnd - lonStart) / lonGap + 1);
			int yCount = totalPoints / xCount;
			gridModel.setxCount(xCount);
			gridModel.setyCount(yCount);
			// 计算纬向格距
			double latGap = formatDouble(Math.abs(latStart - latEnd) / (yCount - 1), df);
			gridModel.setLatGap(latGap);
			
			// 将数据点一维转为二维数组
			Micaps2Point[][] pnts = new Micaps2Point[yCount][xCount];
			
			// 强制将网格处理为左上角和右下角
			if (forceReverve && latStart < latEnd) {
				gridModel.setLatStart(latEnd);
				gridModel.setLatEnd(latStart);
				
				int rowLen = pnts.length;
				for (int r = rowLen - 1, i = 0; r >= 0; r--, i++) {
					int colLen = pnts[i].length;
					for (int j = 0; j < colLen; j++) {
						pnts[i][j] = points[r * colLen + j];
					}
				}
			} else {
				index = 0;
				for (int i = 0; i < pnts.length; i++) {
					for (int j = 0; j < pnts[i].length; j++) {
						pnts[i][j] = points[index];
						index++;
					}
				}
			}
			gridModel.setPoints(pnts);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(br != null){
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return gridModel;
	}
	
	/**
	 * @Description: 将Micaps4文件解析为格点模型
	 * @author: songwj
	 * @date: 2017-9-5 下午2:49:33
	 * @param micapsFilePath micaps4文件路径
	 * @param charset 解码方式，如：UTF-8
	 * @return
	 */
	public static GridModel micaps4ToGridModel(String micaps4FilePath, String charset, boolean forceReverve){
		if (!new File(micaps4FilePath).exists()) {
			log.error("指定路径的文件[" + micaps4FilePath + "]不存在!!!");
			return null;
		}
		// *****************文件校验开始*****************
		// 预留文件校验
		// *****************文件校验结束*****************
		
		GridModel gridModel = new GridModel();
		// 存储每行拆分后的字符串
		List<String> strData = new ArrayList<String>();
		BufferedReader br = null;
		
		try {
			// 读取文件
			br = new BufferedReader(new InputStreamReader(new FileInputStream(micaps4FilePath), charset));
			// 第一行
			String row1 = br.readLine();
			// 描述信息
			gridModel.setDescription(row1);
			// 第二行
			String row2 = br.readLine();
			StringUtils.split(strData, row2, StringUtils.DELIM_TAB);
			// 年
			gridModel.setYear(strData.get(0));
			// 月
			gridModel.setMonth(strData.get(1));
			// 日
			gridModel.setDay(strData.get(2));
			// 时次
			gridModel.setBatch(strData.get(3));
			// 时效
			gridModel.setTimelimit(strData.get(4));
			// 层级
			gridModel.setLayer(strData.get(5));
			// 经度格距
			double lonGap = Math.abs(Double.valueOf(strData.get(6)));
			gridModel.setLonGap(lonGap);
			// 纬度格距
			double latGap = Math.abs(Double.valueOf(strData.get(7)));
			gridModel.setLatGap(latGap);
			// 起始经度
			double lonStart = Double.valueOf(strData.get(8));
			gridModel.setLonStart(lonStart);
			// 终止经度
			double lonEnd = Double.valueOf(strData.get(9));
			gridModel.setLonEnd(lonEnd);
			// 起始纬度
			double latStart = Double.valueOf(strData.get(10));
			gridModel.setLatStart(latStart);
			// 终止纬度
			double latEnd = Double.valueOf(strData.get(11));
			gridModel.setLatEnd(latEnd);
			// 经纬向格点数
			int xCount = Integer.valueOf(strData.get(12));
			int yCount = Integer.valueOf(strData.get(13));
			gridModel.setxCount(xCount);
			gridModel.setyCount(yCount);
			// 存储数据点
			Point[][] points = new Point[yCount][xCount];
			// 存储数据值
			double[] values = new double[xCount * yCount];
			// 记录下标值
			int index = 0;
			
			while(true){
				String row = br.readLine();
				if(row == null){
					break;
				}
				strData = new ArrayList<String>();
				StringUtils.split(strData, row, StringUtils.DELIM_TAB);
				// 装入每行数据值
				for(String str : strData){
					values[index] = Double.valueOf(str);
					index++;
				}
			}
			
			// 重新归零起算
			index = 0;
			
			// 强制将网格处理为左上角和右下角
			if (forceReverve && latStart < latEnd) {
				gridModel.setLatStart(latEnd);
				gridModel.setLatEnd(latStart);
				for (int i = yCount - 1; i >= 0; i--) {
					for (int j = 0; j < xCount; j++) {
						double val = values[index];
						// 判断是否是数值类型，如果不是则用9999.0缺报替代
						if ((val + "").matches(numericPattern)) {
							points[i][j] = new Point(formatDouble(lonStart + j * lonGap, df), formatDouble(latStart + (yCount - i - 1) * latGap, df), formatDouble(val, df));
						} else {
							log.error("文件[" + micaps4FilePath + "]网格第" + (i + 1) + "行" + (j + 1) + "列数据为非数值类型，该值为：" + val);
							points[i][j] = new Point(formatDouble(lonStart + j * lonGap, df), formatDouble(latStart + (yCount - i - 1) * latGap, df), formatDouble(9999.0, df));
						}
						index++;
					}
				}
			} else {
				for (int i = 0; i < yCount; i++) {
					for (int j = 0; j < xCount; j++) {
						double val = values[index];
						// 判断是否是数值类型，如果不是则用9999.0缺报替代
						if ((val + "").matches(numericPattern)) {
							points[i][j] = new Point(formatDouble(lonStart + j * lonGap, df), formatDouble(latStart + i * latGap, df), formatDouble(val, df));
						} else {
							log.error("文件[" + micaps4FilePath + "]网格第" + (i + 1) + "行" + (j + 1) + "列数据为非数值类型，该值为：" + val);
							points[i][j] = new Point(formatDouble(lonStart + j * lonGap, df), formatDouble(latStart + i * latGap, df), formatDouble(9999.0, df));
						}
						index++;
					}
				}
			}
			
			gridModel.setPoints(points);
		} catch (Exception e) {
			log.error("MICAPS第4类文件解析失败：" + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if(br != null){
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return gridModel;
	}
	
	/**
	 * @Description: 将MICAPS第11类文件解析为格点模型
	 * @author: songwj
	 * @date: 2017-9-10 下午12:56:57
	 * @param micaps11FilePath
	 * @param charset
	 * @return
	 */
	public static Micaps11GridModel micaps11ToGridModel(String micaps11FilePath, String charset){
		if (!new File(micaps11FilePath).exists()) {
			log.error("指定路径的文件[" + micaps11FilePath + "]不存在!!!");
			return null;
		}
		// *****************文件校验开始*****************
		// 预留文件校验
		// *****************文件校验结束*****************
		
		Micaps11GridModel gridModel = new Micaps11GridModel();
		// 存储每行拆分后的字符串
		List<String> strData = new ArrayList<String>();
		BufferedReader br = null;
		
		try {
			// 读取文件
			br = new BufferedReader(new InputStreamReader(new FileInputStream(micaps11FilePath), charset));
			// 第一行
			String row1 = br.readLine();
			// 描述信息
			gridModel.setDescription(row1);
			// 第二行
			String row2 = br.readLine();
			StringUtils.split(strData, row2, StringUtils.DELIM_TAB);
			// 年
			gridModel.setYear(strData.get(0));
			// 月
			gridModel.setMonth(strData.get(1));
			// 日
			gridModel.setDay(strData.get(2));
			// 时次
			gridModel.setBatch(strData.get(3));
			// 时效
			gridModel.setTimelimit(strData.get(4));
			// 层级
			gridModel.setLayer(strData.get(5));
			// 经度格距
			double lonGap = Double.valueOf(strData.get(6));
			gridModel.setLonGap(lonGap);
			// 纬度格距
			double latGap = Double.valueOf(strData.get(7));
			gridModel.setLatGap(latGap);
			// 起始经度
			double lonStart = Double.valueOf(strData.get(8));
			gridModel.setLonStart(lonStart);
			// 终止经度
			double lonEnd = Double.valueOf(strData.get(9));
			gridModel.setLonEnd(lonEnd);
			// 起始纬度
			double latStart = Double.valueOf(strData.get(10));
			gridModel.setLatStart(latStart);
			// 终止纬度
			double latEnd = Double.valueOf(strData.get(11));
			gridModel.setLatEnd(latEnd);
			// 纬向格点数
			int xCount = Integer.valueOf(strData.get(12));
			// 经向格点数
			int yCount = Integer.valueOf(strData.get(13));
			gridModel.setxCount(xCount);
			gridModel.setyCount(yCount);
			// 存储U和V数据点
			Point[][] uPoints = new Point[yCount][xCount];
			Point[][] vPoints = new Point[yCount][xCount];
			// 存储数据值(u和v两个方向数据)
			double[] values = new double[xCount * yCount * 2];
			// 记录下标值
			int index = 0;
			
			while(true){
				String row = br.readLine();
				if(row == null){
					break;
				}
				strData = new ArrayList<String>();
				StringUtils.split(strData, row, StringUtils.DELIM_TAB);
				// 装入每行数据值
				for(String str : strData){
					values[index] = Double.valueOf(str);
					index++;
				}
			}
			
			// 重新归零起算
			index = 0;
			// 获取u方向数据 
			for (int i = 0; i < yCount; i++) {
				for (int j = 0; j < xCount; j++) {
					double val = values[index];
					// 判断是否是数值类型（包含科学技术法），如果不是则用9999.0缺报替代
					if ((val + "").matches(numericPattern)) {
						uPoints[i][j] = new Point(formatDouble(lonStart + j * lonGap, df), formatDouble(latStart + i * latGap, df), formatDouble(val, df));
					} else {
						log.error("文件[" + micaps11FilePath + "]网格第" + (i + 1) + "行" + (j + 1) + "列数据为非数值类型，该值为：" + val);
						uPoints[i][j] = new Point(formatDouble(lonStart + j * lonGap, df), formatDouble(latStart + i * latGap, df), formatDouble(9999.0, df));
					}
					index++;
				}
			}
			
			// 获取v方向数据
			for (int i = 0; i < yCount; i++) {
				for (int j = 0; j < xCount; j++) {
					double val = values[index];
					// 判断是否是数值类型（包含科学技术法），如果不是则用9999.0缺报替代
					if ((val + "").matches(numericPattern)) {
						vPoints[i][j] = new Point(formatDouble(lonStart + j * lonGap, df), formatDouble(latStart + i * latGap, df), formatDouble(val, df));
					} else {
						log.error("文件[" + micaps11FilePath + "]网格第" + (i + 1) + "行" + (j + 1) + "列数据为非数值类型，该值为：" + val);
						vPoints[i][j] = new Point(formatDouble(lonStart + j * lonGap, df), formatDouble(latStart + i * latGap, df), formatDouble(9999.0, df));
					}
					index++;
				}
			}
			
			gridModel.setPoints(uPoints);
			gridModel.setvPoints(vPoints);
		} catch (Exception e) {
			log.error("MICAPS第11类文件解析失败：" + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if(br != null){
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return gridModel;
	}
	
	/**
	 * 将10米风GrADS二进制格点文件转为格点模型
	 * @param gradsFilePath GrADS二进制格点文件路径
	 * @param charset 解码方式，如：utf-8
	 * @return
	 */
	public static List<GridModel> gradsWindToGridModel(String gradsFilePath, String charset) {
		List<GridModel> gridModels = new ArrayList<GridModel>();
		DecimalFormat df = new DecimalFormat("###.000000");
		BufferedReader br = null;
		DataInputStream in = null;
		// 存储每行拆分后的字符串
		List<String> strData = new ArrayList<String>();
		
		try {
			File file = new File(gradsFilePath);
			if (!file.exists()) {
				log.error("指定路径文件不存在，该文件为：" + gradsFilePath);
				return null;
			}
			// 读取GrADS二进制格点文件数据
			in = new DataInputStream(new FileInputStream(file));
			// 获取ctl文件路径
			String ctlPath = file.getParent();
			// 读取ctl文件
			br = new BufferedReader(new InputStreamReader(new FileInputStream(ctlPath + "/ecmf_short3h_10m_uv-component_of_wind.ctl"), charset));
			// 跳过前面三行
			FileOperationUtil.readLine(br);
			FileOperationUtil.readLine(br);
			FileOperationUtil.readLine(br);
			// 读取第四行
			String row = FileOperationUtil.readLine(br);
			StringUtils.split(strData, row, StringUtils.DELIM_TAB);
			// 纬向格点数
			int xCount = Integer.parseInt(strData.get(1));
			// 起始经度
			double lonStart = formatDouble(Double.parseDouble(strData.get(3)), df);
			// 经度格距
			double lonGap = formatDouble(Double.parseDouble(strData.get(4)), df);
			// 终止经度
			double lonEnd = formatDouble(lonStart + (xCount - 1) * lonGap, df);
			// 读取第五行
			row =  FileOperationUtil.readLine(br);
			strData = new ArrayList<String>();
			StringUtils.split(strData, row, StringUtils.DELIM_TAB);
			// 经向格点数
			int yCount = Integer.parseInt(strData.get(1));
			// 起始纬度
			double latStart = formatDouble(Double.parseDouble(strData.get(3)), df);
			// 纬度格距
			double latGap = formatDouble(Double.parseDouble(strData.get(4)), df);
			// 终止纬度
			double latEnd = formatDouble(latStart + (yCount - 1) * latGap, df);
			// 2016030100
			String datetime = file.getParentFile().getName();
			String year = datetime.substring(0, 4);
			String month = datetime.substring(4, 6);
			String day = datetime.substring(6, 8);
			String batch = datetime.substring(8, 10);
			// 第6行
			FileOperationUtil.readLine(br);
			// 第7行
			row =  FileOperationUtil.readLine(br);
			strData = new ArrayList<String>();
			StringUtils.split(strData, row, StringUtils.DELIM_TAB);
			// 时效个数
			int timelimitNum = Integer.parseInt(strData.get(1));
			// 小时间隔数
			String hr = strData.get(4);
			int timeInterval = Integer.parseInt(hr.substring(0, hr.indexOf("hr")));
			// 第8行
			row =  FileOperationUtil.readLine(br);
			strData = new ArrayList<String>();
			StringUtils.split(strData, row, StringUtils.DELIM_TAB);
			// 成员（要素）个数
			int memberNum = Integer.parseInt(strData.get(1));
			// 所有成员名
			List<String> members = new ArrayList<String>();
			for (int i = 0; i < memberNum; i++) {
				members.add(strData.get(3 + i));
			}
			// 第9行
			row =  FileOperationUtil.readLine(br);
			strData = new ArrayList<String>();
			StringUtils.split(strData, row, StringUtils.DELIM_TAB);
			// 变量个数
			int varNum = Integer.parseInt(strData.get(1));
			// 提取GrADS二进制文件中的格点数据值
			int length = (int)file.length();
			byte[] buf = new byte[length];
			float[] values = new float[length/4];// 存放格点数据值，float类型数据占4字节
			
			while (true) {
				int len = in.read(buf, 0, length);
				if (len == -1) {
					break;
				}
			}
			
			int index = 0;
			for (int start = 0; start < length; start = start + 4) {
				values[index] = arr2Float(buf, start);
				index++;
			}
			
			// 重新归零起算
			index = 0;
			// 遍历所有成员（要素）
			for (String member : members) {
				// 遍历所有时效（共25个，逐3小时，从0起算，即：000~072）
				for (int n = 0; n < timelimitNum; n++) {
					if (n == 0) {// 跳过000时效
						index = index + xCount * yCount * 2;
					} else {
						GridModel gridModel = new GridModel();
						
						gridModel.setYear(year);// 年
						gridModel.setMonth(month);// 月
						gridModel.setDay(day);// 日
						gridModel.setBatch(batch);// 时次
						gridModel.setTimelimit(StringUtils.addZero(n * timeInterval, 3));// 时效
						gridModel.setParameter(member);// 成员名（要素）
						gridModel.setxCount(xCount);// 纬向格点数
						gridModel.setyCount(yCount);// 经向格点数
						gridModel.setLonStart(lonStart);// 起始经度
						gridModel.setLatStart(latStart);// 起始纬度
						gridModel.setLonEnd(lonEnd);// 终止经度
						gridModel.setLatEnd(latEnd);// 终止纬度
						gridModel.setLonGap(lonGap);// 经度格距
						gridModel.setLatGap(latGap);// 纬度格距
						
						// 存储数据点
						Point[][] points = new Point[yCount][xCount];
						// 水平和垂直两个方向
						for (int k = 0; k < varNum; k++) {
							// 经过以下处理后，网格数据统一为左上角和右下角
							// 起始点位于左下角，终止点位于右上角
							if (latStart < latEnd) {
								for (int i = yCount - 1; i >= 0; i--) {
									for (int j = 0; j < xCount; j++) {
										points[i][j] = new Point(formatDouble(lonStart + j * lonGap, df), formatDouble(latStart + (yCount - i - 1) * latGap, df), formatDouble(values[index], df));
										index++;
									}
								}
								// 起始点位于左上角，终止点位于右下角
							} else {
								for (int i = 0; i < yCount; i++) {
									for (int j = 0; j < xCount; j++) {
										// 从右上角开始计算
										points[i][j] = new Point(formatDouble(lonStart + j * lonGap, df), formatDouble(latStart - i * latGap, df), formatDouble(values[index], df));
										index++;
									}
								}
							}
							
							// 将起始终止点统一为左上角和右下角
							if (latStart < latEnd) {
								gridModel.setLatStart(latEnd);
								gridModel.setLatEnd(latStart);
							}
							
							gridModel.setPoints(points);
							
							gridModels.add(gridModel);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return gridModels;
	}
	
	/**
	 * 插值（通过解析格点文件获取格点模型，利用插值算法计算出指定点的值）
	 * @param gridModel 格点模型
	 * @param lon 点的经度值
	 * @param lat 点的纬度值
	 * @param decimalFormat 输出数据值的格式化模版
	 * @return 返回该点的值
	 */
	public static double getValByInterpolation(GridModel gridModel, double lon, double lat, DecimalFormat decimalFormat) {
		double value = 9999.00;
		
		try {
			DecimalFormat df = new DecimalFormat("###.00000");
			// 获取与指定点临近的4个点
			Point p = new Point(lon, lat, 0.0);
			List<Point> pnts = getNearFourPoints(p, gridModel, df);
			// 利用插值算法计算指定点的值
			if (pnts != null) {
				// 标记临近4个点是否有缺报值9999.0，true表示含有
				boolean flag = false;
				
				for (Point pnt : pnts) {
					if (pnt.getValue() == 9999.0) {
						flag = true;
						break;
					}
				}
				
				// 不含缺报值则使用插值算法
				if (!flag) {
					value = calcPointValue(pnts.get(0), pnts.get(1), pnts.get(2), pnts.get(3), p, decimalFormat);
					// 含缺报值则使用最近格点算法
				} else {
					value = getNearestPointValue(pnts.get(0), pnts.get(1), pnts.get(2), pnts.get(3), p, decimalFormat);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return value;
	}
	
	
	/**
	 * 最近格点（通过解析格点文件获取格点模型，找出与指定点临近的四个点，取距离最近点的值）
	 * @param gridModel 格点模型
	 * @param lon 点的经度值
	 * @param lat 点的纬度值
	 * @param decimalFormat 输出数据值的格式化模版
	 * @return 返回该点的值
	 */
	public static double getValByNearestGridPoint(GridModel gridModel, double lon, double lat, DecimalFormat decimalFormat) {
		double value = 9999.00;

		try {
			if (gridModel == null) {
				return value;
			}
			
			DecimalFormat df = new DecimalFormat("###.00000");
			// 获取与指定点临近的4个点
			Point p = new Point(lon, lat, 0.0);
			List<Point> pnts = getNearFourPoints(p, gridModel, df);
			// 取距离最近点的值
			if (pnts != null) {
				value = getNearestPointValue(pnts.get(0), pnts.get(1), pnts.get(2), pnts.get(3), p, decimalFormat);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return value;
	}
	
	/**
	 * 从GridModel中取出与Point p点周围的四个点，取点顺序：左下 左上  右下 右上 
	 * 以便传入方法calcPointValue(Point p11,Point p12,Point p21,Point p22,Point p)四个点使用
	 * 把经度和纬度想象成一个二纬坐标系
	 * x轴方向表示经度，从左至右经度由小到大演变
	 * y轴方向表示纬度，从上至下纬度由大到小演变
	 * @param p	待插值的点
	 * @param bean GridModel对象，包含格点所有信息
	 * @return
	 */
	public static List<Point> getNearFourPoints(Point p, GridModel gm, DecimalFormat df){
		List<Point> fourPnts = new ArrayList<Point>();
		
		Point[][] points = gm.getPoints();
		if(points == null){
			log.error("传入的GridModel为空!!!");
			return null;
		}
		double lon = formatDouble(p.getX(), df);
		double lat = formatDouble(p.getY(), df);
		double lat_start = formatDouble(gm.getLatStart(), df);
		double lon_start = formatDouble(gm.getLonStart(), df);
		double lat_end = formatDouble(gm.getLatEnd(), df);
		double lon_end = formatDouble(gm.getLonEnd(), df);
		double lat_gap = formatDouble(gm.getLatGap(), df);
		double lon_gap = formatDouble(gm.getLonGap(), df);
		
		//判断该点是否在网格范围内，网格的起始终止点分为左上角右下角和左下角右上角两种情况
		if(!(((lat_start>=lat && lat>=lat_end) || (lat_start<=lat && lat<=lat_end)) && (lon_start<=lon && lon<=lon_end))){
			log.error("该点("+ lon + ", "+ lat +")不在网格范围内!!!");
			return null;
		}
		
		// 获取与该点临近的经纬度坐标
		int x = (int)formatDouble(Math.abs((lat - lat_start) / lat_gap), df);
		int y = (int)formatDouble(Math.abs((lon - lon_start) / lon_gap), df);
		Point p11 = null;
		Point p12 = null;
		Point p21 = null;
		Point p22 = null;
		int rowNum = gm.getPoints().length;
		int colNum = gm.getPoints()[0].length;
		
		//该算法针对起始和终止经纬度在左上角右下角和左下角右上角两种情况都通用
		if(x < (rowNum - 1) && y < (colNum - 1)){
			p11 = points[x][y];//左上
			p12 = points[x][y + 1];//右上
			p21 = points[x + 1][y];//左下
			p22 = points[x + 1][y + 1];//右下
		}else if(x < (rowNum - 1) && y >= (colNum - 1)){
			p11 = points[x][y - 1];//左上
			p12 = points[x][y];//右上
			p21 = points[x + 1][y - 1];//左下
			p22 = points[x + 1][y];//右下
		}else if(x >= (rowNum - 1) && y < (colNum - 1)){
			p11 = points[x - 1][y];//左上
			p12 = points[x - 1][y + 1];//右上
			p21 = points[x][y];//左下
			p22 = points[x][y + 1];//右下
		}else if(x >= (rowNum - 1) && y >= (colNum - 1)){
			p11 = points[x - 1][y - 1];//左上
			p12 = points[x - 1][y];//右上
			p21 = points[x][y - 1];//左下
			p22 = points[x][y];//右下
		}
		fourPnts.add(p21);
		fourPnts.add(p11);
		fourPnts.add(p22);
		fourPnts.add(p12);
		
		return fourPnts;
	}

	/**
	 * 已知在一个矩形四个点上的坐标和值，求这个矩形范围内任意一点的值（插值算法）
	 * @param p11	左下
	 * @param p12	左上
	 * @param p21	右下
	 * @param p22	右上 
	 * @param p		待插值点
	 * @param df	数据格式化模版	
	 * @return
	 */
	public static double calcPointValue(Point p11,Point p12,Point p21,Point p22, Point p, DecimalFormat df){
		double value = 9999.00;
		double x1 = p11.getX();
		double x2 = p22.getX();
		double y1 = p11.getY();
		double y2 = p22.getY();
		double x = p.getX();
		double y = p.getY();
		double v11 = p11.getValue();
		double v12 = p12.getValue();
		double v21 = p21.getValue();
		double v22 = p22.getValue();
		//根据公式求值
		// 在x方向上进行线性插值f(r1)=(x2-x)/(x2-x1)*f(Q11)+(x-x1)/(x2-x1)*f(Q21)
		// 在x方向上进行线性插值f(r2)=(x2-x)/(x2-x1)*f(Q12)+(x-x1)/(x2-x1)*f(Q22)
		// 在y方向上进行线性插值f(p)=(y2-y)/(y2-y1)f(r1)+(y-y1)/(y2-y1)*f(r2)
		value = ((y2-y)/(y2-y1))*((x2-x)/(x2-x1)*v11+(x-x1)/(x2-x1)*v21)+((y-y1)/(y2-y1))*((x2-x)/(x2-x1)*v12+(x-x1)/(x2-x1)*v22);
		// 格式化成两位小数
		value = formatDouble(value, df);
		return value;
	}
	
	/**
	 * 利用插值算法对网格进行升降尺度
	 * @param gm 格点模型
	 * @param lon_gap 升降尺度后的经度格距
	 * @param lat_gap 升降尺度后的纬度格距（一般和经度格距相等）
	 * @return 返回升降尺度后的格点模型
	 */
	public static GridModel liftScale(GridModel gm, double lon_gap, double lat_gap) {
		GridModel gridModel = new GridModel();
		
		DecimalFormat df = new DecimalFormat("###.0000000");
		String description = gm.getDescription();
		String parameter = gm.getParameter();
		String year = gm.getYear();
		String month = gm.getMonth();
		String day = gm.getDay();
		String batch = gm.getBatch();
		String timelimit = gm.getTimelimit();
		double lonStart = formatDouble(gm.getLonStart(), df);
		double latStart = formatDouble(gm.getLatStart(), df);
		double lonEnd = formatDouble(gm.getLonEnd(), df);
		double latEnd = formatDouble(gm.getLatEnd(), df);
		double lonGap = formatDouble(gm.getLonGap(), df);
		double latGap = formatDouble(gm.getLatGap(), df);
		double lonSub = formatDouble(Math.abs(lonEnd - lonStart), df);
		double latSub = formatDouble(Math.abs(latStart - latEnd), df);
		
		if (lonGap == lon_gap && latGap == lat_gap) {
			return gm;
		}
		
		if (Math.round(lonSub * Math.pow(10, 6)) % Math.round(lon_gap * Math.pow(10, 6)) != 0) {
			log.error("传入的经度格距有误，必须能被起始终止经度差值" + lonSub + "整除，而传入的经度格距为：" + lon_gap);
			return gm;
		}
		
		if (Math.round(latSub * Math.pow(10, 6)) % Math.round(lat_gap * Math.pow(10, 6)) != 0) {
			log.error("传入的纬度格距有误，必须能被起始终止纬度差值" + latSub + "整除，而传入的纬度格距为：" + lat_gap);
			return gm;
		}
		
		// 升降尺度后的经向纬向格点数
		int xCount = (int) (lonSub / lon_gap + 1);
		int yCount = (int) (latSub / lat_gap + 1);
		// 尺度降低1倍后的数据点
		Point[][] points = new Point[yCount][xCount];
		
		// 起始终止点在左上角和右下角
		if (latStart > latEnd) {
			for (int i = 0; i < points.length; i++) {
				for (int j = 0; j < points[i].length; j++) {
					double x = lonStart + j * lon_gap;
					x = formatDouble(x, df);
					double y = latStart - i * lat_gap;
					y = formatDouble(y, df);
					// 为每个点赋上初始经纬度值
					points[i][j] = new Point(x, y, 0.0);
					// 找出与该点临近的四个点
					List<Point> fourPnts = getNearFourPoints(points[i][j], gm, df);
					// 计算该点的值
					double value = calcPointValue(fourPnts.get(0), fourPnts.get(1), fourPnts.get(2), fourPnts.get(3), points[i][j], df);
					points[i][j].setValue(value);
				}
			}
			// 起始终止点在左下角和右上角
		} else {
			for (int i = 0; i < points.length; i++) {
				for (int j = 0; j < points[i].length; j++) {
					double x = lonStart + j * lon_gap;
					x = formatDouble(x, df);
					double y = latStart + i * lat_gap;
					y = formatDouble(y, df);
					// 为每个点赋上初始经纬度值
					points[i][j] = new Point(x, y, 0.0);
					// 找出与该点临近的四个点
					List<Point> fourPnts = getNearFourPoints(points[i][j], gm, df);
					// 计算该点的值
					double value = calcPointValue(fourPnts.get(0), fourPnts.get(1), fourPnts.get(2), fourPnts.get(3), points[i][j], df);
					points[i][j].setValue(value);
				}
			}
		}
		
		gridModel.setDescription(description);
		gridModel.setParameter(parameter);
		gridModel.setYear(year);
		gridModel.setMonth(month);
		gridModel.setDay(day);
		gridModel.setBatch(batch);
		gridModel.setTimelimit(timelimit);
		gridModel.setLonStart(lonStart);
		gridModel.setLatStart(latStart);
		gridModel.setLonEnd(lonEnd);
		gridModel.setLatEnd(latEnd);
		gridModel.setLonGap(lon_gap);
		gridModel.setLatGap(lat_gap);
		gridModel.setxCount(xCount);
		gridModel.setyCount(yCount);
		gridModel.setPoints(points);
		
		return gridModel;
	}
	
	/**
	 * @Description: 根据水平和垂直风向值计算叠加后的风速和风向（北风为0度，顺时针计算，东风为90度）
	 * @author: songwj
	 * @date: 2017-8-18 下午4:05:49
	 * @param u 水平风向值
	 * @param v 垂直风向值
	 * @return
	 */
	public static WindSpdAndDirModel getWindSpeedAndDirection(double u, double v) {
		WindSpdAndDirModel model = new WindSpdAndDirModel();
		// 风速
		double speed = 9999.0;
		// 风向
		double direction = 9999.0;
		
		// 当水平和垂直风不同时为0时做如下处理
		if (!(u == 0 && v == 0)) {
			speed = Math.sqrt(u * u + v * v);
			
			if (u >= 0) {
				direction = Math.PI * 3 / 2 - Math.atan(v / u);
			} else {
				direction = Math.PI * 1 / 2 - Math.atan(v / u);
			}
			
			direction = direction * 180 / Math.PI;
		}
		
		model.setWindSpeed(speed);
		model.setWindDirection(direction);
		
		return model;
	}
	
	/**
	 * 区域提取
	 * @param gridModel
	 * @param lonStart
	 * @param latStart
	 * @param lonEnd
	 * @param latEnd
	 * @return
	 */
	public static GridModel regionExtract(GridModel gridModel, double lonStart, double latStart, double lonEnd, double latEnd) {
		GridModel gm = new GridModel();
		
		if (gridModel != null) {
			String year = gridModel.getYear();// 年
			String month = gridModel.getMonth();// 月
			String day = gridModel.getDay();// 日
			String batch = gridModel.getBatch();// 时次
			String timeTimelimit = gridModel.getTimelimit();// 时效
			double lon_start = gridModel.getLonStart();// 起始经度
			double lat_start = gridModel.getLatStart();// 起始纬度
			double lon_gap = gridModel.getLonGap();// 经度格距
			double lat_gap = gridModel.getLatGap();// 纬度格距
			Point[][] pnts = gridModel.getPoints();
			
			//****************************************************
			// 数据校验
			//****************************************************
			
			// 提取下标范围
			int xStart = (int)(Math.round(Math.abs((lonStart - lon_start) / lon_gap)));
			int yStart = (int)(Math.round(Math.abs((lat_start - latStart) / lat_gap)));
			int xEnd = (int)(Math.round(Math.abs((lonEnd - lon_start) / lon_gap)));
			int yEnd = (int)(Math.round(Math.abs((lat_start - latEnd) / lat_gap)));
			// 计算提取下标范围所处的经纬度范围
			double newLonStart = NumberFormatUtil.numberFormat(lon_start + xStart * lon_gap, 6);
			double newLatStart = NumberFormatUtil.numberFormat(lat_start + yStart * lat_gap, 6);
			double newLonEnd = NumberFormatUtil.numberFormat(lon_start + xEnd * lon_gap, 6);
			double newLatEnd = NumberFormatUtil.numberFormat(lat_start + yEnd * lat_gap, 6);
			// 存放提取的区域数据
			int xCount = xEnd - xStart + 1;
			int yCount = yEnd - yStart + 1;
			Point[][] points = new Point[yCount][xCount];
			
			for (int i=yStart; i<=yEnd; i++) {
				for (int j=xStart; j<=xEnd; j++) {
					points[i - yStart][j - xStart] = pnts[i][j];
				}
			}
			
			gm.setYear(year);
			gm.setMonth(month);
			gm.setDay(day);
			gm.setBatch(batch);
			gm.setTimelimit(timeTimelimit);
			gm.setLonStart(newLonStart);
			gm.setLatStart(newLatStart);
			gm.setLonEnd(newLonEnd);
			gm.setLatEnd(newLatEnd);
			gm.setLonGap(lon_gap);
			gm.setLatGap(lat_gap);
			gm.setxCount(xCount);
			gm.setyCount(yCount);
			gm.setPoints(points);
		}
		
		return gm;
	}
	
	/**
	 * @Description: 累加统计指定时间段的10m平均风平均误差、平均绝对误差、偏小概率、正确概率、偏大概率、偏大、正确和偏小区间
	 * @Author: songwj
	 * @Date: 2017-11-30 上午11:11:35
	 * @param timeStart
	 * @param timeEnd
	 */
	public static void get10mMeanWindAccumulateResult(String timeStart, String timeEnd) {
		// 10m风预报最终存放数据根目录
		String finalDataOutputRootPath = FileConfigUtil.getProperty("file.finalDataOutputRootPath");
		// 10米平均风最终累积数据输出根目录
		String finalAccOutputRootPath = FileConfigUtil.getProperty("file.finalAccumulateOutputRootPath");
		// 10米平均风统计结果输出根目录
		String finalStatisticOutputRootPath = FileConfigUtil.getProperty("file.finalStatisticOutputRootPath");
		int timelimitNum = 25;
		int timeInterval = 3;
		accumulate10mWind(timeStart, timeEnd, finalDataOutputRootPath, finalAccOutputRootPath, finalStatisticOutputRootPath, timelimitNum, timeInterval);
	}
	
	/**
	 * @Description: 累加统计指定时间段的10m风平均误差、平均绝对误差、偏小概率、正确概率、偏大概率、偏大、正确和偏小区间
	 * @Author: songwj
	 * @Date: 2017-11-30 下午8:11:07
	 * @param timeStart
	 * @param timeEnd
	 * @param finalDataOutputRootPath
	 * @param finalAccOutputRootPath
	 * @param finalStatisticOutputRootPath
	 * @param timelimitNum
	 * @param timeInterval
	 */
	private static void accumulate10mWind(String timeStart, String timeEnd, String finalDataOutputRootPath, String finalAccOutputRootPath, String finalStatisticOutputRootPath, int timelimitNum, int timeInterval) {
		// 存放指定时间段内的所有累加数据（key：2016030108_006_0~3）
		Map<String, ErrorModel> map = new HashMap<String, ErrorModel>();
		// 存放制定个时间段内的所有累加数据（key：006_0~3）
		Map<String, AccumulateErrorModel> finalAccumulateMap = new HashMap<String, AccumulateErrorModel>();
		BufferedReader br = null;
		BufferedWriter out = null;
		
		try {
			// 获取起止时间段内逐12小时的所有时间序列
			List<String> timeSequence = DatetimeUtil.getTimeSequence(timeStart, timeEnd, DatetimeUtil.YYYYMMDDHH, 12);
			
			for (String tmSeq : timeSequence) {
				String path = finalDataOutputRootPath + tmSeq + ".txt";
				
				if (!new File(path).exists()) {
					log.error("指定的数据文件不存在，指定的文件为：" + path);
					continue;
				}
				
				br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
				
				while (true) {
					String row = FileOperationUtil.readLine(br);
					
					if (row == null) {
						break;
					}
					
					// 存储每行拆分后的字符串
					List<String> strData = new ArrayList<String>();
					StringUtils.split(strData, row, StringUtils.DELIM_TAB);
					
					String key = strData.get(0);
					double avgError = Double.parseDouble(strData.get(1));// 累加平均误差
					double avgAbsError = Double.parseDouble(strData.get(2));// 累加平均绝对误差
					int smallCount = Integer.parseInt(strData.get(3));// 偏小次数
					int rightCount = Integer.parseInt(strData.get(4));// 正确次数
					int largeCount = Integer.parseInt(strData.get(5));// 偏大次数
					int count = Integer.parseInt(strData.get(6));// 累加次数
					
					map.put(key, new ErrorModel(avgError, avgAbsError, smallCount, rightCount, largeCount, count));
				}
			}
			
			// 风速区间中的最大值
			int maxWindSpd = 24;
			// 统计最终的累加数据
			// 时间（世界时）
			for (String tmSeq : timeSequence) {
				tmSeq = DatetimeUtil.universalTime2beijingTime(tmSeq, DatetimeUtil.YYYYMMDDHH);
				// 时效
				for (int i = 1; i < timelimitNum; i++) {
					String timelimit = StringUtils.addZero(i * timeInterval, 3);
					// 风速段
					for (int n = 0; n <= maxWindSpd; n+=3) {
						String key = n == maxWindSpd ? (tmSeq + "_" + timelimit + "_" + maxWindSpd) : (tmSeq + "_" + timelimit + "_" + n + "~" + (n + 3));
						String finalKey = n == maxWindSpd ? (timelimit + "_" + maxWindSpd) : (timelimit + "_" + n + "~" + (n + 3));
						
						if (map.containsKey(key)) {
							ErrorModel errModel = map.get(key);
							double avgErr = errModel.getAvgError();
							double avgAbsErr = errModel.getAvgAbsError();
							int smallCount = errModel.getSmallCount();
							int rightCount = errModel.getRightCount();
							int largeCount = errModel.getLargeCount();
							int count = errModel.getCount();
							
							if (!((avgErr + "").matches(numericPattern) && (avgAbsErr + "").matches(numericPattern))) {
								log.error("误差模型出现非数值类型，该误差模型内容为：[" + key + ", " + avgErr + ", " + avgAbsErr + ", " + smallCount + ", " + rightCount + ", " + largeCount + ", " + count + "]");
								continue;
							}
							
							BigDecimal avgError = new BigDecimal(avgErr);
							BigDecimal avgAbsError = new BigDecimal(avgAbsErr);
							
							// 包含则继续累加
							if (finalAccumulateMap.containsKey(finalKey)) {
								AccumulateErrorModel model = finalAccumulateMap.get(finalKey);
								
								BigDecimal accAvgError = model.getAvgError();
								BigDecimal accAvgAbsError = model.getAvgAbsError();
								int smallAccCount = model.getSmallCount();
								int rightAccCount = model.getRightCount();
								int largeAccCount = model.getLargeCount();
								int accCount = model.getCount();
								
								finalAccumulateMap.put(finalKey, new AccumulateErrorModel(accAvgError.add(avgError), accAvgAbsError.add(avgAbsError), smallAccCount + smallCount, 
										rightAccCount + rightCount, largeAccCount + largeCount, accCount + count));
							} else {
								finalAccumulateMap.put(finalKey, new AccumulateErrorModel(avgError, avgAbsError, smallCount, rightCount, largeCount, count));
							}
						}
					}
				}
			}
			log.info("10m风逐日累加数据加载完成!!!");
			
			if (!new File(finalAccOutputRootPath).exists()) {
				new File(finalAccOutputRootPath).mkdirs();
			}
			
			finalAccOutputRootPath = finalAccOutputRootPath + timeStart + "_" + timeEnd + "_accumulate.txt";
			out = new BufferedWriter(new FileWriter(new File(finalAccOutputRootPath)));
			
			for (int i = 1; i < timelimitNum; i++) {
				String timelimit = StringUtils.addZero(i * timeInterval, 3);
				// 风速段
				for (int n = 0; n <= maxWindSpd; n+=3) {
					String key = n == maxWindSpd ? (timelimit + "_" + maxWindSpd) : (timelimit + "_" + n + "~" + (n + 3));
					
					if (finalAccumulateMap.containsKey(key)) {
						AccumulateErrorModel model = finalAccumulateMap.get(key);
						out.write(key + "\t" + model.getAvgError().doubleValue() + "\t" + model.getAvgAbsError().doubleValue() + "\t" + model.getSmallCount() + "\t" + model.getRightCount() + "\t" + model.getLargeCount() + "\t" + model.getCount() + "\n");
					} else {
						out.write(key + "\n");
					}
				}
			}
			// 输出缓冲区中残留的内容
			out.flush();
			log.info("最终累积数据输出成功，文件存放路径为：" + finalAccOutputRootPath);
			
			if (!new File(finalStatisticOutputRootPath).exists()) {
				new File(finalStatisticOutputRootPath).mkdirs();
			}
			
			// 输出最终误差处理结果(横向风速段，纵向时效)
			String path2 = finalStatisticOutputRootPath + timeStart + "_" + timeEnd + "_error.txt";
			out = new BufferedWriter(new FileWriter(new File(path2)));
			out.write("\t\t0~3\t\t\t\t3~6\t\t\t\t6~9\t\t\t\t9~12\t\t\t12~15\t\t\t15~18\t\t\t18~21\t\t\t21~24\t\t\t24\n");
			for (int i = 1; i < timelimitNum; i++) {
				String timelimit = StringUtils.addZero(i * timeInterval, 3);
				out.write(timelimit + "\t\t");
				
				for (int n = 0; n <= maxWindSpd; n+=3) {
					String windSpdRange = n == maxWindSpd ? (maxWindSpd + "") : (n + "~" + (n + 3));
					String key = timelimit + "_" + windSpdRange;
					AccumulateErrorModel model = finalAccumulateMap.get(key);
					
					BigDecimal accAvgError = model.getAvgError();
					BigDecimal accAvgAbsError = model.getAvgAbsError();
					int accCount = model.getCount();
					double finalAvgError = 0.0;
					double finalAvgAbsError = 0.0;
					
					if (accCount != 0) {
						finalAvgError = accAvgError.divide(new BigDecimal(accCount), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
						finalAvgAbsError = accAvgAbsError.divide(new BigDecimal(accCount), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
					}
					
					out.write(finalAvgError + ", " + finalAvgAbsError + "\t\t");
				}
				
				out.write("\n");
			}
			out.flush();
			log.info("最终误差统计数据输出成功，文件存放路径为：" + path2);
			
			// 输出最终概率处理结果(横向风速段，纵向时效)
			DecimalFormat df = new DecimalFormat("###.00");
			String path3 = finalStatisticOutputRootPath + timeStart + "_" + timeEnd + "_probability.txt";
			out = new BufferedWriter(new FileWriter(new File(path3)));
			out.write("\t\t0~3\t\t\t\t\t3~6\t\t\t\t\t6~9\t\t\t\t\t\t9~12\t\t\t\t12~15\t\t\t\t15~18\t\t\t\t18~21\t\t\t\t21~24\t\t\t\t24\n");
			for (int i = 1; i < timelimitNum; i++) {
				String timelimit = StringUtils.addZero(i * timeInterval, 3);
				out.write(timelimit + "\t\t");
				
				for (int n = 0; n <= maxWindSpd; n+=3) {
					String windSpdRange = n == maxWindSpd ? maxWindSpd + "" : (n + "~" + (n + 3));
					String key = timelimit + "_" + windSpdRange;
					AccumulateErrorModel model = finalAccumulateMap.get(key);
					
					int smallCount = model.getSmallCount();
					int rightCount = model.getRightCount();
					int largeCount = model.getLargeCount();
					int accCount = model.getCount();
					double smallProbability = 0.0;
					double rightProbability = 0.0;
					double largeProbability = 0.0;
					
					if (accCount != 0) {
						smallProbability = formatDouble((double)smallCount / accCount, df);
						rightProbability = formatDouble((double)rightCount / accCount, df);
						largeProbability = formatDouble((double)largeCount / accCount, df);
					}
					
					out.write(smallProbability + ", " + rightProbability + ", " + largeProbability + "\t\t");
				}
				
				out.write("\n");
			}
			out.flush();
			log.info("最终概率统计数据输出成功，文件存放路径为：" + path3);
			
			// 输出最终偏大、正确和偏小区间结果(横向风速段，纵向时效)
			String path4 = finalStatisticOutputRootPath + timeStart + "_" + timeEnd + "_range.txt";
			out = new BufferedWriter(new FileWriter(new File(path4)));
			out.write("\t\t0~3\t\t\t\t\t3~6\t\t\t\t\t6~9\t\t\t\t\t\t9~12\t\t\t\t12~15\t\t\t\t15~18\t\t\t\t\t18~21\t\t\t\t\t21~24\t\t\t\t\t24\n");
			int K = 3; // 风速间隔3m/s
			for (int i = 1; i < timelimitNum; i++) {
				String timelimit = StringUtils.addZero(i * timeInterval, 3);
				out.write(timelimit + "\t\t");
				
				for (int n = 0; n <= maxWindSpd; n+=K) {
					String windSpdRange = n == maxWindSpd ? (maxWindSpd + "") : (n + "~" + (n + 3));
					String key = timelimit + "_" + windSpdRange;
					AccumulateErrorModel model = finalAccumulateMap.get(key);
					
					int smallCount = model.getSmallCount();
					int largeCount = model.getLargeCount();
					int accCount = model.getCount();
					
					if (accCount != 0) {
						if (n == maxWindSpd) {
							out.write(maxWindSpd + "_" + maxWindSpd + "_" + maxWindSpd + "_" + maxWindSpd + "\n");
						} else {
							double largeProbability = formatDouble((double)largeCount / accCount, df);
							double smallProbability = formatDouble((double)smallCount / accCount, df);
							double rightProbability = formatDouble(1.0 - largeProbability - smallProbability, df);
							double largeUpperLimit = formatDouble(n + K * largeProbability, df);// 偏大的上限值
							double rightUpperLimit = formatDouble(largeUpperLimit + K * rightProbability, df);// 正确的上限值
							
							out.write(n + "_" + largeUpperLimit + "_" + rightUpperLimit + "_" + (n + 3) + "\t\t");
						}
					} else {
						out.write("0_0_0_0\t\t");
					}
				}
			}
			log.info("最终偏大、正确和偏小区间统计数据输出成功，文件存放路径为：" + path4);
		} catch (Exception e) {
			log.error("统计指定时间段的10m风平均误差和平均绝对误差失败：" + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @Description: 获取指定时间段内的10m平均风平均偏大、偏小误差
	 * @Author: songwj
	 * @Date: 2017-11-30 下午12:44:45
	 * @param timeStart
	 * @param timeEnd
	 */
	public static void get10mMeanWindAvgLargeAndSmallError(String timeStart, String timeEnd) {
		// 10m风预报最终累加数据存放根目录
		String finalAccumulateOutputRootPath = FileConfigUtil.getProperty("file.finalAccumulateOutputRootPath");
		int timelimitNum = 25;// 时效个数
		int timeInterval = 3;// 时效间隔
		getAvgLargeAndSmallError(timeStart, timeEnd, finalAccumulateOutputRootPath, timelimitNum, timeInterval);
	}
	
	/**
	 * @Description: 获取指定时间段内的10m风平均偏大、偏小误差
	 * @Author: songwj
	 * @Date: 2017-11-30 上午11:34:01
	 * @param timeStart
	 * @param timeEnd
	 * @param finalAccumulateOutputRootPath
	 * @param timelimitNum
	 * @param timeInterval
	 */
	private static void getAvgLargeAndSmallError(String timeStart, String timeEnd, String finalAccumulateOutputRootPath, int timelimitNum, int timeInterval) {
		// 存放指定时间段内的所有累加数据（key：003_0~3）
		Map<String, AccumulateErrorModel> finalAccumulateMap = new HashMap<String, AccumulateErrorModel>();
		BufferedReader br = null;
		BufferedWriter out = null;
		
		try {
			// 累加结果数据文件
			String path = finalAccumulateOutputRootPath + timeStart + "_" + timeEnd + "_accumulate.txt";
			
			if (!new File(path).exists()) {
				log.error("最终的累加结果数据文件不存在，指定的文件为：" + path);
				return;
			}
			
			br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			while (true) {
				String row = FileOperationUtil.readLine(br);
				
				if (row == null) {
					break;
				}
				
				// 存储每行拆分后的字符串
				List<String> strData = new ArrayList<String>();
				StringUtils.split(strData, row, StringUtils.DELIM_TAB);
				
				String key = strData.get(0);
				BigDecimal avgError = new BigDecimal(strData.get(1));// 累加平均误差
				BigDecimal avgAbsError = new BigDecimal(strData.get(2));// 累加平均绝对误差
				int smallCount = Integer.parseInt(strData.get(3));// 偏小次数
				int rightCount = Integer.parseInt(strData.get(4));// 正确次数
				int largeCount = Integer.parseInt(strData.get(5));// 偏大次数
				int count = Integer.parseInt(strData.get(6));// 累加次数
				
				finalAccumulateMap.put(key, new AccumulateErrorModel(avgError, avgAbsError, smallCount, rightCount, largeCount, count));
			}
			
			// 风速区间中的最大值
			int maxWindSpd = 24;
			// 输出最终平均偏大、偏小误差处理结果(横向风速段，纵向时效)
			DecimalFormat df = new DecimalFormat("###.00");
			path = finalAccumulateOutputRootPath + timeStart + "_" + timeEnd + "_avg_deviation.txt";
			out = new BufferedWriter(new FileWriter(new File(path)));
			out.write("\t\t0~3\t\t\t\t3~6\t\t\t\t6~9\t\t\t\t9~12\t\t\t12~15\t\t\t15~18\t\t\t18~21\t\t\t21~24\t\t\t24\n");
			for (int i = 1; i < timelimitNum; i++) {
				String timelimit = StringUtils.addZero(i * timeInterval, 3);
				out.write(timelimit + "\t\t");
				
				for (int n = 0; n <= maxWindSpd; n+=3) {
					String windSpdRange = n == maxWindSpd ? (maxWindSpd + "") : (n + "~" + (n + 3));
					String key = timelimit + "_" + windSpdRange;
					AccumulateErrorModel model = finalAccumulateMap.get(key);
					
					double accAvgError = model.getAvgError().doubleValue();
					double accAvgAbsError = model.getAvgAbsError().doubleValue();
					int accCount = model.getCount();
					double finalAvgLargeError = 0.0;// 平均偏大误差
					double finalAvgSmallError = 0.0;// 平均偏小误差
					
					if (accCount != 0) {
						double finalAvgError = accAvgError/ accCount;// 平均误差
						double finalAvgAbsError = accAvgAbsError / accCount;// 平均绝对误差
						// 平均偏大误差计算公式：ed = (b + |b|) / 2
						finalAvgLargeError = formatDouble((finalAvgError + finalAvgAbsError) / 2, df);
						// 平均偏小误差计算公式：ex = |b| - ed = (|b| - b) / 2
						finalAvgSmallError = formatDouble((finalAvgAbsError - finalAvgError) / 2, df);
					}
					
					out.write(finalAvgLargeError + ", " + finalAvgSmallError + "\t\t");
				}
				
				out.write("\n");
			}
			log.info("最终平均偏大、偏小误差数据文件输出成功，文件存放路径为：" + path);
		} catch (Exception e) {
			log.error("统计10m风指定时间段的平均偏大、偏小误差失败：" + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @Description: 处理指定时间段的10m风数据
	 * @author: songwj
	 * @date: 2017-8-23 下午3:36:21
	 * @param timeStart yyyyMMddHH，世界时
	 * @param timeEnd yyyyMMddHH，世界时
	 */
	public static void grads10mWindProcessByTimeRange(String timeStart, String timeEnd) {
		// 10m风预报原始数据根目录
		String rootPath = FileConfigUtil.getProperty("file.10mWindRootPath");
		// 获取起止时间段内逐12小时的所有时间序列
		List<String> timeSequence = DatetimeUtil.getTimeSequence(timeStart, timeEnd, DatetimeUtil.YYYYMMDDHH, 12);
		for (String tmSeq : timeSequence) {
			String path = rootPath + tmSeq + "/ecmf_short3h_10m_uv-component_of_wind.dat";
			grads10mWindProcess(path, "utf-8");
		}
	}
	
	/**
	 * @Description: 10米平均风数据处理
	 * @author: songwj
	 * @date: 2017-8-17 下午7:39:16
	 * @param gradsFilePath GrADS二进制格点文件路径
	 * @param charset 解码方式，如：utf-8
	 */
	public static void grads10mWindProcess(String gradsFilePath, String charset) {
		BufferedReader br = null;
		DataInputStream in = null;
		BufferedWriter out1 = null;
		BufferedWriter out2 = null;
		// 存储每行拆分后的字符串
		List<String> strData = new ArrayList<String>();
		DecimalFormat df = new DecimalFormat("###.000000");
		
		try {
			File file = new File(gradsFilePath);
			if (!file.exists()) {
				log.error("指定路径文件不存在，指定的文件为：" + gradsFilePath);
				return;
			}
			// 读取GrADS二进制格点文件数据
			in = new DataInputStream(new FileInputStream(file));
			// 获取ctl文件路径
			String ctlRootPath = file.getParent();
			String ctlPath = ctlRootPath + "/ecmf_short3h_10m_uv-component_of_wind.ctl";
			if (!new File(ctlPath).exists()) {
				log.error("指定的文件不存在，指定的文件为：" + ctlPath);
				return;
			}
			// 读取ctl文件
			br = new BufferedReader(new InputStreamReader(new FileInputStream(ctlPath), charset));
			// 跳过前面三行
			FileOperationUtil.readLine(br);
			FileOperationUtil.readLine(br);
			FileOperationUtil.readLine(br);
			// 读取第四行
			String row = FileOperationUtil.readLine(br);
			StringUtils.split(strData, row, StringUtils.DELIM_TAB);
			// 纬向格点数
			int xCount = Integer.parseInt(strData.get(1));
			// 起始经度
			double lonStart = formatDouble(Double.parseDouble(strData.get(3)), df);
			// 经度格距
			double lonGap = formatDouble(Double.parseDouble(strData.get(4)), df);
			// 终止经度
			double lonEnd = formatDouble(lonStart + (xCount - 1) * lonGap, df);
			// 读取第五行
			row = FileOperationUtil.readLine(br);
			strData = new ArrayList<String>();
			StringUtils.split(strData, row, StringUtils.DELIM_TAB);
			// 经向格点数
			int yCount = Integer.parseInt(strData.get(1));
			// 起始纬度
			double latStart = formatDouble(Double.parseDouble(strData.get(3)), df);
			// 纬度格距
			double latGap = formatDouble(Double.parseDouble(strData.get(4)), df);
			// 终止纬度
			double latEnd = formatDouble(latStart + (yCount - 1) * latGap, df);
			// 2016030100
			String datetime = file.getParentFile().getName();
			String year = datetime.substring(0, 4);
			String month = datetime.substring(4, 6);
			String day = datetime.substring(6, 8);
			String batch = datetime.substring(8, 10);
			// 世界时转北京时（+8）
			String bjDatetime = DatetimeUtil.universalTime2beijingTime(datetime, DatetimeUtil.YYYYMMDDHH);
			// 第6行
			FileOperationUtil.readLine(br);
			// 第7行
			row =  FileOperationUtil.readLine(br);
			strData = new ArrayList<String>();
			StringUtils.split(strData, row, StringUtils.DELIM_TAB);
			// 时效个数
			int timelimitNum = Integer.parseInt(strData.get(1));
			// 小时间隔数
			String hr = strData.get(4);
			int timeInterval = Integer.parseInt(hr.substring(0, hr.indexOf("hr")));
			// 第8行
			row =  FileOperationUtil.readLine(br);
			strData = new ArrayList<String>();
			StringUtils.split(strData, row, StringUtils.DELIM_TAB);
			// 成员（要素）个数
			int memberNum = Integer.parseInt(strData.get(1));
			// 所有成员名
			List<String> members = new ArrayList<String>();
			for (int i = 0; i < memberNum; i++) {
				members.add(strData.get(3 + i));
			}
			// 第9行
			row =  FileOperationUtil.readLine(br);
			strData = new ArrayList<String>();
			StringUtils.split(strData, row, StringUtils.DELIM_TAB);
			// 变量个数
			int varNum = Integer.parseInt(strData.get(1));
			// 提取GrADS二进制文件中的格点数据值
			int length = (int)file.length();
			byte[] buf = new byte[length];
			float[] values = new float[length/4];// 存放格点数据值，float类型数据占4字节
			
			while (true) {
				int len = in.read(buf, 0, length);
				if (len == -1) {
					break;
				}
			}
			
			int index = 0;
			for (int start = 0; start < length; start = start + 4) {
				values[index] = arr2Float(buf, start);
				index++;
			}
			
			GridModel gridModel = new GridModel();
			
			gridModel.setYear(year);// 年
			gridModel.setMonth(month);// 月
			gridModel.setDay(day);// 日
			gridModel.setBatch(batch);// 时次
			gridModel.setTimelimit(StringUtils.addZero(0, 3));// 时效
			gridModel.setParameter(members.get(0));// 成员名（要素）
			gridModel.setxCount(xCount);// 纬向格点数
			gridModel.setyCount(yCount);// 经向格点数
			gridModel.setLonStart(lonStart);// 起始经度
			gridModel.setLatStart(latStart);// 起始纬度
			gridModel.setLonEnd(lonEnd);// 终止经度
			gridModel.setLatEnd(latEnd);// 终止纬度
			gridModel.setLonGap(lonGap);// 经度格距
			gridModel.setLatGap(latGap);// 纬度格距
			// 存储数据点
			Point[][] points = new Point[yCount][xCount];
			// 重新归零起算
			index = 0;
			
			for (int i = 0; i < yCount; i++) {
				for (int j = 0; j < xCount; j++) {
					points[i][j] = new Point(formatDouble(lonStart + j * lonGap, df), formatDouble(latStart + i * latGap, df), formatDouble(values[index], df));
					index++;
				}
			}
			
			gridModel.setPoints(points);
			
			// 存储每个站点每个时间的众数（key: 2016030108_069_706040, value: 1.772727）
			Map<String, Double> modeVals = new HashMap<String, Double>();
			// 存放所有的站点编号
			List<String> stationCodes = new ArrayList<String>();
			// 获取风所有的站点信息
			Map<String, String> windStationConfig = WindStationConfig.windStationConfig;
			Set<Map.Entry<String, String>> entrySet = windStationConfig.entrySet();
			
			for (Map.Entry<String, String> me : entrySet) {
				// 站点编号
				String stationCode = me.getKey();
				stationCodes.add(stationCode);
				// 站点所对应的经纬度
				List<String> lonLat = StringUtils.split(me.getValue(), StringUtils.DELIM_COMMA);
				double lon = Double.parseDouble(lonLat.get(1));
				double lat = Double.parseDouble(lonLat.get(0));
				
				Point pnt = new Point(lon, lat);
				// 计算获取指定站点的临近四个点以及这个四个点分别在网格中位置下标
				Map<String, List<Point>> pointMap = getNearFourPointsAndIndex(pnt, gridModel, df);
				// 获取指定站点所有成员从003~072时效的每个方向的临近4个点，并插值出指定站点的值（key：成员_时效_风向，value：分量风速）
				Map<String, Double> perDirVals = getAllMembersPerDirVals(pointMap, values, members, timelimitNum, timeInterval, varNum, xCount, yCount, pnt);
				// 将每个成员相同时效的U和V分量数据合成，获取合成后的站点风速和风向值（key：成员_时效）
				Map<String, WindSpdAndDirModel> megerVals = getAllMembersMegerVals(perDirVals, members, timelimitNum, timeInterval, varNum);
				// 计算获取每个站点每个时效的众数（key: 2016030108_069_706040, value: 1.772727）
				getPerTimelimitMode(modeVals, megerVals, members, timelimitNum, timeInterval, bjDatetime, stationCode);
			}
			
			// 计算获取每个站点每个时效的平均误差和平均绝对误差（key: 2016030108_069_706040）
			// 输出误差数据，含指定时间指定站点的：众数、实况数据、平均误差和平均绝对误差
			Map<String, StationErrorModel> stationErrModelMap = getPerStationAndPerTimeErrorData(modeVals, stationCodes, bjDatetime, timelimitNum, timeInterval);
			String baseDataOutputRootPath = FileConfigUtil.getProperty("file.baseDataOutputRootPath");
			
			if (!new File(baseDataOutputRootPath).exists()) {
				new File(baseDataOutputRootPath).mkdirs();
			}
			
			String baseDataOutputPath = baseDataOutputRootPath + datetime + ".txt";
			out1 = new BufferedWriter(new FileWriter(new File(baseDataOutputPath)));
			for (int i = 1; i < timelimitNum; i++) {
				String tmlimit = StringUtils.addZero(i * timeInterval, 3);
				for (String stcode : stationCodes) {
					String key = bjDatetime + "_" + tmlimit + "_" + stcode;
					if (stationErrModelMap.containsKey(key)) {
						StationErrorModel model = stationErrModelMap.get(key);
						out1.write(key + "\t" + model.getMode() + "\t" + model.getActualWindSpeed() + "\t" + model.getAvgError() + "\t" + model.getAvgAbsError() + "\n");
					} else {
						out1.write(key + "\n");
					}
				}
			}
			log.info("站点的基础数据输出成功，数据存放路径为：" + baseDataOutputPath);
			
			// 计算获取每个时效不同风速区间的累加平均误差和平均绝对误差数据（key：2016030108_069_0~3）
			Map<String, ErrorModel> errModelMap = getAccumulationErrorData(stationErrModelMap, stationCodes, bjDatetime, timelimitNum, timeInterval);
			// 输出累加后的最终数据，含：累加后的平均误差、平均绝对误差和累加的次数
			String finalDataOutputRootPath = FileConfigUtil.getProperty("file.finalDataOutputRootPath");
			
			if (!new File(finalDataOutputRootPath).exists()) {
				new File(finalDataOutputRootPath).mkdirs();
			}
			
			String finalDataOutputPath = finalDataOutputRootPath + datetime + ".txt";
			out2 = new BufferedWriter(new FileWriter(new File(finalDataOutputPath)));
			int maxSpeed = 24;// 风俗段的最大值
			for (int i = 1; i < timelimitNum; i++) {
				String tmlimit = StringUtils.addZero(i * timeInterval, 3);
				for (int j = 0; j <= maxSpeed; j+=3 ) {
					String key = j == maxSpeed ? (bjDatetime + "_" + tmlimit + "_" + maxSpeed) : (bjDatetime + "_" + tmlimit + "_" + j + "~" + (j + 3));
					if (errModelMap.containsKey(key)) {
						ErrorModel model = errModelMap.get(key);
						// 输出依次为：yyyyMMddHH_时效_风速段、平均误差、平均绝对误差、偏小次数、正确次数、偏大次数、累积总次数
						out2.write(key + "\t" + model.getAvgError() + "\t" + model.getAvgAbsError() + "\t" + model.getSmallCount() + "\t" + model.getRightCount() + "\t" + model.getLargeCount() + "\t" + model.getCount() + "\n");
					}
				}
			}
			log.info("文件日累积数据输出成功，数据存放路径为：" + finalDataOutputPath);
		} catch(Exception e) {
			log.error("10m风数据处理失败：" + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (out1 != null) {
					out1.flush();
					out1.close();
				}
				if (out2 != null) {
					out2.flush();
					out2.close();
				}
				if (br != null) {
					br.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @Description: 将10m风数据文件依次插值为0.1公里场、求众数、订正、获取集合预报平均风向、最后处理为MICAPS文件
	 * @author: songwj
	 * @date: 2017-9-7 下午6:41:49
	 * @param gradsFilePath GrADS文件全路径
	 * @param avgDeviationFilePath 平均偏大、偏小误差文件全路径
	 * @param rangeFilePath 偏大、正确、偏小区间文件全路径
	 * @param charset
	 */
	public static void grads10mWindToMicapsProcess(String gradsFilePath, String avgDeviationFilePath, String rangeFilePath, String charset) {
		BufferedReader br = null;
		DataInputStream in = null;
		BufferedWriter out = null;
		BufferedWriter out1 = null;
		// 存储每行拆分后的字符串
		List<String> strData = new ArrayList<String>();
		DecimalFormat df = new DecimalFormat("###.000000");
		
		try {
			File file = new File(gradsFilePath);
			if (!file.exists()) {
				log.error("指定路径文件不存在，指定的文件为：" + gradsFilePath);
				return;
			}
			// 数据存放的根目录
			String rootPath = FileConfigUtil.getProperty("file.actualDataOutputRootPath");
			// 剔除陆地点的MICAPS文件存放路径
			String wavePath = rootPath + "wave";
			
			// 读取GrADS二进制格点文件数据
			in = new DataInputStream(new FileInputStream(file));
			// 获取ctl文件路径
			String ctlRootPath = file.getParent();
			String ctlPath = ctlRootPath + "/ecmf_short3h_10m_uv-component_of_wind.ctl";
			if (!new File(ctlPath).exists()) {
				log.error("指定的文件不存在，指定的文件为：" + ctlPath);
				return;
			}
			// 读取ctl文件
			br = new BufferedReader(new InputStreamReader(new FileInputStream(ctlPath), charset));
			// 跳过前面三行
			FileOperationUtil.readLine(br);
			FileOperationUtil.readLine(br);
			FileOperationUtil.readLine(br);
			// 读取第四行
			String row = FileOperationUtil.readLine(br);
			StringUtils.split(strData, row, StringUtils.DELIM_TAB);
			// 纬向格点数
			int xCount = Integer.parseInt(strData.get(1));
			// 起始经度
			double lonStart = formatDouble(Double.parseDouble(strData.get(3)), df);
			// 经度格距
			double lonGap = formatDouble(Double.parseDouble(strData.get(4)), df);
			// 终止经度
			double lonEnd = formatDouble(lonStart + (xCount - 1) * lonGap, df);
			// 读取第五行
			row = FileOperationUtil.readLine(br);
			strData = new ArrayList<String>();
			StringUtils.split(strData, row, StringUtils.DELIM_TAB);
			// 经向格点数
			int yCount = Integer.parseInt(strData.get(1));
			// 起始纬度
			double latStart = formatDouble(Double.parseDouble(strData.get(3)), df);
			// 纬度格距
			double latGap = formatDouble(Double.parseDouble(strData.get(4)), df);
			// 终止纬度
			double latEnd = formatDouble(latStart + (yCount - 1) * latGap, df);
			// 2016030100
			String datetime = file.getParentFile().getName();
			String year = datetime.substring(0, 4);
			String month = datetime.substring(4, 6);
			String day = datetime.substring(6, 8);
			String batch = datetime.substring(8, 10);
			// 第6行
			FileOperationUtil.readLine(br);
			// 第7行
			row =  FileOperationUtil.readLine(br);
			strData = new ArrayList<String>();
			StringUtils.split(strData, row, StringUtils.DELIM_TAB);
			// 时效个数
			int timelimitNum = Integer.parseInt(strData.get(1));
			// 小时间隔数
			String hr = strData.get(4);
			int timeInterval = Integer.parseInt(hr.substring(0, hr.indexOf("hr")));
			// 第8行
			row =  FileOperationUtil.readLine(br);
			strData = new ArrayList<String>();
			StringUtils.split(strData, row, StringUtils.DELIM_TAB);
			// 成员（要素）个数
			int memberNum = Integer.parseInt(strData.get(1));
			// 所有成员名
			List<String> members = new ArrayList<String>();
			for (int i = 0; i < memberNum; i++) {
				members.add(strData.get(3 + i));
			}
			// 第9行
			row =  FileOperationUtil.readLine(br);
			strData = new ArrayList<String>();
			StringUtils.split(strData, row, StringUtils.DELIM_TAB);
			// 变量个数
			int varNum = Integer.parseInt(strData.get(1));
			
			// 浮标站点提取的逐12小时订正前和订正后数据存放根目录
			String beforeCorrectDataOutputPath = rootPath + "errorCheck/beforeCorrect/" + datetime + "/";
			String afterCorrectDataOutputPath = rootPath + "errorCheck/afterCorrect/" + datetime + "/";
			// 浮标站点实况数据存放根目录
			String liveDataOutputPath = rootPath + "errorCheck/liveData/";
			// MICAPS第2类文件输出的根目录
			String micaps2OutputPath = rootPath + "micaps2/" + datetime + "/";
			// MICAPS第11类文件输出的根目录
			String micaps11OutputPath = rootPath + "micaps11/" + datetime + "/";
			// 图像输出的根目录
			String imageOutputPath = rootPath + "image/" + datetime + "/";
			// 最大风输出的根目录
			String maxWindOutputPath = rootPath + "maxWind/" + datetime + "/";
			// 指定站点提取的逐小时数据存放根目录
			String stationDataOutputPath = rootPath + "stationData/" + datetime + "/";
			
			if (!new File(beforeCorrectDataOutputPath).exists())
				new File(beforeCorrectDataOutputPath).mkdirs();
			
			if (!new File(afterCorrectDataOutputPath).exists())
				new File(afterCorrectDataOutputPath).mkdirs();
			
			if (!new File(liveDataOutputPath).exists())
				new File(liveDataOutputPath).mkdirs();
			
			if (!new File(micaps2OutputPath).exists())
				new File(micaps2OutputPath).mkdirs();
			
			if (!new File(micaps11OutputPath).exists())
				new File(micaps11OutputPath).mkdirs();
			
			if (!new File(imageOutputPath).exists())
				new File(imageOutputPath).mkdirs();
			
			if (!new File(maxWindOutputPath).exists())
				new File(maxWindOutputPath).mkdirs();
			
			// 如果原先存在则先删除再创建
			FileUtils.deleteDirectory(new File(stationDataOutputPath));
			
			if (!new File(stationDataOutputPath).exists())
				new File(stationDataOutputPath).mkdirs();
			
			// 获取GrADS二进制文件中所有格点的数据值
			int length = (int)file.length();
			byte[] buf = new byte[length];
			float[] values = new float[length/4];// 存放格点数据值，float类型数据占4字节
			
			while (true) {
				int len = in.read(buf, 0, length);
				if (len == -1) {
					break;
				}
			}
			
			int index = 0;
			for (int start = 0; start < length; start = start + 4) {
				values[index] = arr2Float(buf, start);
				index++;
			}
			
			GridModel gridModel = new GridModel();
			
			gridModel.setYear(year);// 年
			gridModel.setMonth(month);// 月
			gridModel.setDay(day);// 日
			gridModel.setBatch(batch);// 时次
			gridModel.setTimelimit(StringUtils.addZero(0, 3));// 时效
			gridModel.setParameter(members.get(0));// 成员名（要素）
			gridModel.setxCount(xCount);// 纬向格点数
			gridModel.setyCount(yCount);// 经向格点数
			gridModel.setLonStart(lonStart);// 起始经度
			gridModel.setLatStart(latStart);// 起始纬度
			gridModel.setLonEnd(lonEnd);// 终止经度
			gridModel.setLatEnd(latEnd);// 终止纬度
			gridModel.setLonGap(lonGap);// 经度格距
			gridModel.setLatGap(latGap);// 纬度格距
			// 存储数据点
			Point[][] points = new Point[yCount][xCount];
			// 重新归零起算
			index = 0;
			
			for (int i = 0; i < yCount; i++) {
				for (int j = 0; j < xCount; j++) {
					points[i][j] = new Point(formatDouble(lonStart + j * lonGap, df), formatDouble(latStart + i * latGap, df), formatDouble(values[index], df));
					index++;
				}
			}
			
			gridModel.setPoints(points);
			
			// 1.获取并输出所有浮标站点12小时间隔原始数据值
			getBuoySrcDataAndOutput(gridModel, datetime, values, members, timelimitNum, timeInterval, varNum, xCount, yCount, beforeCorrectDataOutputPath, out);
			// 2.获取从003~072时效的众数和集合平均风向、对众数订正、最终将订正后众数与集合平均风向合成输出为MICAP第2类文件
			getModeAndWindDirOutputMicaps2(gridModel, datetime, timelimitNum, timeInterval, varNum, members, values, year, month, day, batch, xCount, yCount, 
					lonStart, latStart, lonEnd, latEnd, avgDeviationFilePath, rangeFilePath, wavePath, micaps2OutputPath, out);
			// 3.提取MICAPS第2类文件中逐12小时的所有浮标站点数据并输出，并将所有第2类文件的风向风速拆分成UV矢量输出为MICAPS第11类格式文件
			micaps2ToMicaps11(micaps2OutputPath, afterCorrectDataOutputPath, micaps11OutputPath, out);
			// 4.将MICAPS第11类逐3小时文件插值为逐小时文件
			micaps11ToHourly(micaps11OutputPath, datetime, timelimitNum, timeInterval, out, out1);
			// 5.将MICAP第2类逐3小时文件的风速转为图像
			meanWindMicaps2ToImage(micaps2OutputPath, imageOutputPath);
			// 6.提取最大风，输出为MICAPS第2类文件
			getMaxWindOutputMicaps2(micaps2OutputPath, datetime, timelimitNum, timeInterval, maxWindOutputPath, out);
			// 7.将提取的最大风数据文件转为图像
			maxWindMicaps2ToImage(maxWindOutputPath, imageOutputPath);
			// 8.提取指定站点从003~072逐小时的风速风向数据
			getStationHourlyData(micaps2OutputPath, micaps11OutputPath, datetime, timelimitNum, timeInterval, stationDataOutputPath, out);
			// 9.获取所有浮标站点的实况数据
			getBuoyStationLiveData(datetime, liveDataOutputPath, out);
		} catch(Exception e) {
			log.error("10m风数据处理失败：" + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
				if (out1 != null) {
					out1.flush();
					out1.close();
				}
				if (br != null) {
					br.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @Description: 获取并输出所有浮标站点12小时间隔原始数据值
	 * @Author: songwj
	 * @Date: 2017-12-6 下午8:06:17
	 * @param gridModel
	 * @param datetime
	 * @param values
	 * @param members
	 * @param timelimitNum
	 * @param timeInterval
	 * @param varNum
	 * @param xCount
	 * @param yCount
	 * @param beforeCorrectDataOutputPath
	 * @param out
	 */
	private static void getBuoySrcDataAndOutput(GridModel gridModel, String datetime, float[] values, List<String> members, int timelimitNum, int timeInterval, 
			int varNum, int xCount, int yCount, String beforeCorrectDataOutputPath, BufferedWriter out) {
		try {
			// 存储每个站点逐12小时的众数（key: 2016030100_012_54558, value: 1.772727）
			Map<String, Double> modeVals = new HashMap<String, Double>();
			
			for (BuoyStationInfo stationInfo : stationInfos) {
				// 站点编号
				String stationCode = stationInfo.getStationCode();
				double lon = stationInfo.getLon();
				double lat = stationInfo.getLat();
				
				Point pnt = new Point(lon, lat);
				// 计算获取指定站点的临近四个点以及这个四个点分别在网格中位置下标
				Map<String, List<Point>> pointMap = getNearFourPointsAndIndex(pnt, gridModel, df);
				// 获取指定站点所有成员从012~072时效逐12小时的每个方向的临近4个点，并插值出指定站点的值（key：成员_时效_风向，value：分量风速）
				Map<String, Double> perDirVals = getAllMembers12HrPerDirVals(pointMap, values, members, timelimitNum, timeInterval, varNum, xCount, yCount, pnt);
				// 将每个成员相同时效的U和V分量数据合成，获取合成后的站点风速和风向值（key：成员_时效）
				Map<String, WindSpdAndDirModel> megerVals = getAllMembers12HrMegerVals(perDirVals, members, timelimitNum, timeInterval, varNum);
				// 计算获取每个站点每个时效的众数（key: 2016030100_012_54558, value: 1.772727）
				get12HrPerTimelimitMode(modeVals, megerVals, members, timelimitNum, timeInterval, datetime, stationCode);
			}
			System.out.println(modeVals);
			// 输出各个站点订正前的原始数据
			for (int n = 1; n <= timelimitNum; n++) {
				if (n % 4 == 0) {
					String timelimit = StringUtils.addZero(n * timeInterval, 3);
					String filePath = beforeCorrectDataOutputPath + datetime + "." + timelimit;
					out = new BufferedWriter(new FileWriter(new File(filePath)));
					
					for (BuoyStationInfo stationInfo : stationInfos) {
						String stationCode = stationInfo.getStationCode();
						String key = datetime + "_" + timelimit + "_" + stationCode;
						if (modeVals.containsKey(key))
							out.write(stationCode + "\t" + NumberFormatUtil.numberFormat(modeVals.get(key), 2) + "\n");
					}
					out.flush();
					log.info("10米风订正前站点数据提取成功，文件存放路径：" + filePath);
				}
			}
		} catch (Exception e) {
			log.error("浮标站点原始数据提取失败：" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * @Description: 获取指定站点所有成员从012~072时效逐12小时的每个方向的临近4个点，并插值出指定站点的值（key：成员_时效_风向，value：分量风速）
	 * @Author: songwj
	 * @Date: 2017-12-6 下午8:06:42
	 * @param pointMap
	 * @param values
	 * @param members
	 * @param timelimitNum
	 * @param timeInterval
	 * @param varNum
	 * @param xCount
	 * @param yCount
	 * @param pnt
	 * @return
	 */
	private static Map<String, Double> getAllMembers12HrPerDirVals(Map<String, List<Point>> pointMap, float[] values, List<String> members, 
			int timelimitNum, int timeInterval, int varNum, int xCount, int yCount, Point pnt) {
		Map<String, Double> map = new HashMap<String, Double>();
		
		if (pointMap != null && members != null && members.size() > 0) {
			List<Point> points = pointMap.get("fourPoints");
			List<Point> index = pointMap.get("fourIndex");
			
			Point p11 = points.get(0);// 左下
			Point p12 = points.get(1);// 左上
			Point p21 = points.get(2);// 右下
			Point p22 = points.get(3);// 右上
			Point idx11 = index.get(0);// 左下
			Point idx12 = index.get(1);// 左上
			Point idx21 = index.get(2);// 右下
			Point idx22 = index.get(3);// 右上
			// 获取临近四个点在网格的行列下标值（从0起算）
			int x11 = (int) idx11.getX();// 行
			int y11 = (int) idx11.getY();// 列
			int x12 = (int) idx12.getX();// 行
			int y12 = (int) idx12.getY();// 列
			int x21 = (int) idx21.getX();// 行
			int y21 = (int) idx21.getY();// 列
			int x22 = (int) idx22.getX();// 行
			int y22 = (int) idx22.getY();// 列
			
			int idx = 0;
			int len = xCount * yCount;
			// 遍历所有成员
			for (String member : members) {
				// 25个时效，跳过000时效
				for (int i = 0; i < timelimitNum; i++) {
					if (i == 0 || i % 4 != 0) {
						idx = idx + len * 2;// 每个时效有U和V两个方向数据
					} else {
						String timelimit = StringUtils.addZero(i * timeInterval, 3);
						// 0 → U, 1 → V
						for (int j = 0; j < varNum; j++) {
							Point pnt11 = new Point(p11.getX(), p11.getY(), values[idx + xCount * x11 + y11]);
							Point pnt12 = new Point(p12.getX(), p12.getY(), values[idx + xCount * x12 + y12]);
							Point pnt21 = new Point(p21.getX(), p21.getY(), values[idx + xCount * x21 + y21]);
							Point pnt22 = new Point(p22.getX(), p22.getY(), values[idx + xCount * x22 + y22]);
							
							// 插值
							double value = calcPointValue(pnt11, pnt12, pnt21, pnt22, pnt, new DecimalFormat("###.00000000"));
							
							// key → c00_003_u
							map.put(member + "_" + timelimit + "_" + (j == 0 ? "u" : "v"), value);
							
							idx = idx + len;
						}
					}
				}
			}
		}
		
		return map;
	}
	
	/**
	 * @Description: 将每个成员相同时效的U和V分量数据合成，获取合成后的站点风速和风向值（key：成员_时效）
	 * @Author: songwj
	 * @Date: 2017-12-6 下午8:06:54
	 * @param perDirVals
	 * @param members
	 * @param timelimitNum
	 * @param timeInterval
	 * @param varNum
	 * @return
	 */
	private static Map<String, WindSpdAndDirModel> getAllMembers12HrMegerVals(Map<String, Double> perDirVals, List<String> members, 
			int timelimitNum, int timeInterval, int varNum) {
		Map<String, WindSpdAndDirModel> map = new HashMap<String, WindSpdAndDirModel>();
		
		if (perDirVals != null) {
			// 遍历所有成员
			for (String member : members) {
				// 25个时效，只取逐12小时，跳过000时效
				for (int i = 1; i < timelimitNum; i++) {
					if (i % 4 == 0) {
						String timelimit = StringUtils.addZero(i * timeInterval, 3);
						double u = perDirVals.get(member + "_" + timelimit + "_" + "u");
						double v = perDirVals.get(member + "_" + timelimit + "_" + "v");
						// 风合成
						WindSpdAndDirModel model = getWindSpeedAndDirection(u, v);
						map.put(member + "_" + timelimit, model);
					}
				}
			}
		}
		
		return map;
	}

	/**
	 * @Description: 计算获取每个站点每个时效的众数（key: 2016030100_012_54558, value: 1.772727）
	 * @Author: songwj
	 * @Date: 2017-12-6 下午8:07:08
	 * @param modeVals
	 * @param megerVals
	 * @param members
	 * @param timelimitNum
	 * @param timeInterval
	 * @param datetime
	 * @param stationCode
	 */
	private static void get12HrPerTimelimitMode(Map<String, Double> modeVals, Map<String, WindSpdAndDirModel> megerVals, List<String> members, 
			int timelimitNum, int timeInterval, String datetime, String stationCode) {
		if (megerVals != null && members != null && members.size() > 0) {
			for (int i = 1; i < timelimitNum; i++) {
				if (i % 4 != 0)
					continue;
				// 时效
				String timelimit = StringUtils.addZero(i * timeInterval, 3);
				int sum0_3 = 0;
				int sum3_6 = 0;
				int sum6_9 = 0;
				int sum9_12 = 0;
				int sum12_15 = 0;
				int sum15_18 = 0;
				int sum18_21 = 0;
				int sum21_24 = 0;
				int sum24 = 0;
				
				// 计算每个时效的众数
				for (String member : members) {
					// 风速（单位：m/s）
					double speed = megerVals.get(member + "_" + timelimit).getWindSpeed();
					// 判断各个成员的风速所处的区间，统计各个在各个区间的次数
					if (0 <= speed && speed <= 3) {
						sum0_3 += 1;
					} else if (3 < speed && speed <= 6) {
						sum3_6 += 1;
					} else if (6 < speed && speed <= 9) {
						sum6_9 += 1;
					} else if (9 < speed && speed <= 12) {
						sum9_12 += 1;
					} else if (12 < speed && speed <= 15) {
						sum12_15 += 1;
					} else if (15 < speed && speed <= 18) {
						sum15_18 += 1;
					} else if (18 < speed && speed <= 21) {
						sum18_21 += 1;
					} else if (21 < speed && speed <= 24) {
						sum21_24 += 1;
					} else {
						sum24 += 1;
					}
				}
				
				// 存放每个区间的次数
				Map<String, Integer> perTimes = new HashMap<String, Integer>();
				perTimes.put("0_3", sum0_3);
				perTimes.put("3_6", sum3_6);
				perTimes.put("6_9", sum6_9);
				perTimes.put("9_12", sum9_12);
				perTimes.put("12_15", sum12_15);
				perTimes.put("15_18", sum15_18);
				perTimes.put("18_21", sum18_21);
				perTimes.put("21_24", sum21_24);
				perTimes.put("24", sum24);
				// 存放频数最高的区间名和对应值
				String maxRangeName = null;
				int maxRangeNum = 0;
				// 获取频数最高的区间及对应数值
				Set<Map.Entry<String, Integer>> entrySet = perTimes.entrySet();
				for (Map.Entry<String, Integer> me : entrySet) {
					if (me.getValue() > maxRangeNum) {
						maxRangeName = me.getKey();
						maxRangeNum = me.getValue();
					}
				}
				// 众数组下限值
				int L = 0;
				int maxSpeed = 24;// 风速段中的最大值
				if ((maxSpeed + "").equals(maxRangeName)) {
					L = maxSpeed;
				} else {
					L = Integer.parseInt(StringUtils.split(maxRangeName, StringUtils.UNDERLINE).get(0));
				}
				// 计算众数组相连的前一组和后一组频数
				int f1 = 0;
				int f2 = 0;
				int K = 3;// 组距（单位：m/s）
				if (L == 0) {
					f1 = 0;
					f2 = perTimes.get("3_6");
				} else if (L == maxSpeed) {
					f1 = perTimes.get((maxSpeed - 3) + "_" + maxSpeed);
					f2 = 0;
				} else {
					f1 = perTimes.get((L - K) + "_" + L);
					int floor = L + K;
					f2 = perTimes.get((floor == maxSpeed ? (maxSpeed + "") : (L + K) + "_" + (L + K * 2)));
				}
				// 计算获取众数
				double mode = calcMode(L, maxRangeNum, f1, f2, K);
				
				modeVals.put(datetime + "_" + timelimit + "_" + stationCode, mode);
			}
		}
	}
	
	/**
	 * @Description: 获取从003~072时效的众数和集合平均风向、对众数订正、最终将订正后众数与集合平均风向合成输出为MICAP第2类文件
	 * @author: songwj
	 * @date: 2017-9-14 下午4:39:07
	 * @param gridModel
	 * @param datetime
	 * @param timelimitNum
	 * @param timeInterval
	 * @param varNum
	 * @param members
	 * @param values
	 * @param year
	 * @param month
	 * @param day
	 * @param batch
	 * @param xCount
	 * @param yCount
	 * @param lonStart
	 * @param latStart
	 * @param lonEnd
	 * @param latEnd
	 * @param avgDeviationFilePath
	 * @param rangeFilePath
	 * @param wavePath
	 * @param micaps2OutputPath
	 * @param out
	 */
	private static void getModeAndWindDirOutputMicaps2(GridModel gridModel, String datetime, int timelimitNum, int timeInterval, int varNum, 
			List<String> members, float[] values, String year, String month, String day, String batch, int xCount, int yCount, double lonStart, 
			double latStart, double lonEnd, double latEnd, String avgDeviationFilePath, String rangeFilePath, String wavePath, String micaps2OutputPath, 
			BufferedWriter out) {
		try {
			if (!new File(avgDeviationFilePath).exists()) {
				log.error("指定的平均偏大、偏小误差数据文件不存在，指定文件为：" + avgDeviationFilePath);
				return;
			}
			
			if (!new File(rangeFilePath).exists()) {
				log.error("指定的偏大、正确、偏小区间数据文件不存在，指定文件为：" + rangeFilePath);
				return;
			}
			
			if (!new File(wavePath).exists()) {
				log.error("指定位置的剔除陆地点模版文件不存在，指定路径为：" + wavePath);
				return;
			}
			
			DecimalFormat dcmFmt = new DecimalFormat("###.00");
			// 加载剔除陆地点的模版文件
			GridModel waveGridModel = micaps4ToGridModel(wavePath, "utf-8", false);
			log.info("剔除陆地点的模版文件[" + wavePath + "]加载成功!!!");
			Point[][] wavePoints = waveGridModel.getPoints();
			
			// 获取平均偏大、偏小误差
			Map<String, String> avgDeviationMap = file2Map(avgDeviationFilePath);
			// 获取偏大、正确、偏小区间
			Map<String, String> rangeMap = file2Map(rangeFilePath);
			
			// 降完尺度后的经纬度格距及经纬向格点数
			double lon_gap = 0.1;
			double lat_gap = 0.1;
			double lonSub = formatDouble(Math.abs(lonEnd - lonStart), df);
			double latSub = formatDouble(Math.abs(latStart - latEnd), df);
			int x_count = (int) (lonSub / lon_gap + 1);
			int y_count = (int) (latSub / lat_gap + 1);
			
			int len = xCount * yCount;// 单个网格数据点个数
			int newLen = x_count * y_count;// 降尺度后网格总数据点数
			int timelimitSpan = len * varNum;// 时效跨度
			int memberSpan = timelimitSpan * timelimitNum;// 成员跨度
			int memberNum = members.size();// 成员数
			// 时效
			for (int n = 1; n < timelimitNum; n++) {
				String timelimit = StringUtils.addZero(n * timeInterval, 3);
				String fileName = datetime + "." + timelimit;
				String outputFile = micaps2OutputPath + fileName;
				out = new BufferedWriter(new FileWriter(new File(outputFile)));
				out.write("diamond 2 " + fileName + "_10mMeanWind\n");
				out.write(year + "\t" + month + "\t" + day + "\t" + batch + "\t10\t" + newLen + "\n");
				
				for (int i = 0; i < y_count; i++) {
					for (int j = 0; j < x_count; j++) {
						// 集合平均风向
						double windDir = 9999.0;
						// 订正后的众数值
						double correntMode = 9999.0;
						double lon = formatDouble(wavePoints[i][j].getX(), dcmFmt);
						double lat = formatDouble(wavePoints[i][j].getY(), dcmFmt);
						
						// 如果为陆地点则直接处理为缺报
						if (wavePoints[i][j].getValue() != 9999.0) {
							// 待插值点
							Point pnt = new Point(lon, lat);
							// 计算获取指定点的临近四个点以及这个四个点分别在网格中下标位置
							Map<String, List<Point>> pointMap = getNearFourPointsAndIndex(pnt, gridModel, df);
							
							List<Point> points = pointMap.get("fourPoints");
							List<Point> index = pointMap.get("fourIndex");
							
							Point p11 = points.get(0);// 左下
							Point p12 = points.get(1);// 左上
							Point p21 = points.get(2);// 右下
							Point p22 = points.get(3);// 右上
							Point idx11 = index.get(0);// 左下
							Point idx12 = index.get(1);// 左上
							Point idx21 = index.get(2);// 右下
							Point idx22 = index.get(3);// 右上
							// 获取临近四个点在网格的行列下标值（从0起算）
							int x11 = (int) idx11.getX();// 行
							int y11 = (int) idx11.getY();// 列
							int x12 = (int) idx12.getX();// 行
							int y12 = (int) idx12.getY();// 列
							int x21 = (int) idx21.getX();// 行
							int y21 = (int) idx21.getY();// 列
							int x22 = (int) idx22.getX();// 行
							int y22 = (int) idx22.getY();// 列
							// 所有成员u和v分量分别总和及总次数
							double sum_u = 0.0;
							double sum_v = 0.0;
							int count = 0;
							// 统计落在各个风速段的次数
							int sum0_3 = 0;
							int sum3_6 = 0;
							int sum6_9 = 0;
							int sum9_12 = 0;
							int sum12_15 = 0;
							int sum15_18 = 0;
							int sum18_21 = 0;
							int sum21_24 = 0;
							int sum24 = 0;
							
							for (int k = 0; k < memberNum; k++) {
								// u方向临近4个点，values中依次对应时效跨度、本网格中的位置、成员跨度
								Point pnt11_u = new Point(p11.getX(), p11.getY(), values[n * timelimitSpan + xCount * x11 + y11 + k * memberSpan]);
								Point pnt12_u = new Point(p12.getX(), p12.getY(), values[n * timelimitSpan + xCount * x12 + y12 + k * memberSpan]);
								Point pnt21_u = new Point(p21.getX(), p21.getY(), values[n * timelimitSpan + xCount * x21 + y21 + k * memberSpan]);
								Point pnt22_u = new Point(p22.getX(), p22.getY(), values[n * timelimitSpan + xCount * x22 + y22 + k * memberSpan]);
								// v方向临近4个点，values中依次对应时效跨度、单网格长度、本网格中的位置、成员跨度
								Point pnt11_v = new Point(p11.getX(), p11.getY(), values[n * timelimitSpan + len + xCount * x11 + y11 + k * memberSpan]);
								Point pnt12_v = new Point(p12.getX(), p12.getY(), values[n * timelimitSpan + len + xCount * x12 + y12 + k * memberSpan]);
								Point pnt21_v = new Point(p21.getX(), p21.getY(), values[n * timelimitSpan + len + xCount * x21 + y21 + k * memberSpan]);
								Point pnt22_v = new Point(p22.getX(), p22.getY(), values[n * timelimitSpan + len + xCount * x22 + y22 + k * memberSpan]);
								
								// 插值出u和v分量
								double u = calcPointValue(pnt11_u, pnt12_u, pnt21_u, pnt22_u, pnt, new DecimalFormat("###.0000000"));
								double v = calcPointValue(pnt11_v, pnt12_v, pnt21_v, pnt22_v, pnt, new DecimalFormat("###.0000000"));
								
								// 剔除异常值
								if (u <= 40 && v <= 40) {
									sum_u += u;
									sum_v += v;
									count++;
									// 风速
									double speed = getWindSpeedAndDirection(u, v).getWindSpeed();
									
									// 判断各个成员的风速所处的区间，统计各个在各个区间的次数
									if (0 <= speed && speed <= 3) {
										sum0_3 += 1;
									} else if (3 < speed && speed <= 6) {
										sum3_6 += 1;
									} else if (6 < speed && speed <= 9) {
										sum6_9 += 1;
									} else if (9 < speed && speed <= 12) {
										sum9_12 += 1;
									} else if (12 < speed && speed <= 15) {
										sum12_15 += 1;
									} else if (15 < speed && speed <= 18) {
										sum15_18 += 1;
									} else if (18 < speed && speed <= 21) {
										sum18_21 += 1;
									}  else if (21 < speed && speed <= 24) {
										sum21_24 += 1;
									} else {
										sum24 += 1;
									}
								}
							}
							
							// 存放每个区间的次数
							Map<String, Integer> perTimes = new HashMap<String, Integer>();
							perTimes.put("0_3", sum0_3);
							perTimes.put("3_6", sum3_6);
							perTimes.put("6_9", sum6_9);
							perTimes.put("9_12", sum9_12);
							perTimes.put("12_15", sum12_15);
							perTimes.put("15_18", sum15_18);
							perTimes.put("18_21", sum18_21);
							perTimes.put("21_24", sum21_24);
							perTimes.put("24", sum24);
							// 存放频数最高的区间名和对应值
							String maxRangeName = null;
							int maxRangeNum = 0;
							// 获取频数最高的区间及对应数值
							Set<Map.Entry<String, Integer>> entrySet = perTimes.entrySet();
							for (Map.Entry<String, Integer> me : entrySet) {
								if (me.getValue() > maxRangeNum) {
									maxRangeName = me.getKey();
									maxRangeNum = me.getValue();
								}
							}
							// 众数组下限值
							int L = 0;
							// 风速区间中的最大值
							int maxWindSpd = 24;
							if ((maxWindSpd + "").equals(maxRangeName)) {
								L = maxWindSpd;
							} else {
								L = Integer.parseInt(StringUtils.split(maxRangeName, StringUtils.UNDERLINE).get(0));
							}
							// 计算众数组相连的前一组和后一组频数
							int f1 = 0;
							int f2 = 0;
							int K = 3;// 组距（单位：m/s）
							if (L == 0) {
								f1 = 0;
								f2 = perTimes.get("3_6");
							} else if (L == maxWindSpd) {
								f1 = perTimes.get((maxWindSpd - K) + "_" + maxWindSpd);
								f2 = 0;
							} else {
								f1 = perTimes.get((L - K) + "_" + L);
								int floor = L + K;
								f2 = perTimes.get((floor == maxWindSpd ? (maxWindSpd + "") : (L + K) + "_" + (L + K * 2)));
							}
							// 计算获取众数
							double speed = calcMode(L, maxRangeNum, f1, f2, K);
							// 众数订正
							// 缺报不订正
							if (speed == 9999.0) {
								correntMode = speed;
								// 众数在0~3之间不订正
							} else if (0 <= speed && speed <= 3) {
								correntMode = speed;
							} else if (3 < speed && speed <= 6) {
								correntMode = getCorrentMode(avgDeviationMap, rangeMap, speed, timelimit + "_3~6", timelimit + "_0~3", timelimit + "_6~9");
							} else if (6 < speed && speed <= 9) {
								correntMode = getCorrentMode(avgDeviationMap, rangeMap, speed, timelimit + "_6~9", timelimit + "_3~6", timelimit + "_9~12");
							} else if (9 < speed && speed <= 12) {
								correntMode = getCorrentMode(avgDeviationMap, rangeMap, speed, timelimit + "_9~12", timelimit + "_6~9", timelimit + "_12~15");
							} else if (12 < speed && speed <= 15) {
								correntMode = getCorrentMode(avgDeviationMap, rangeMap, speed, timelimit + "_12~15", timelimit + "_9~12", timelimit + "_15~18");
							} else if (15 < speed && speed <= 18) {
								correntMode = getCorrentMode(avgDeviationMap, rangeMap, speed, timelimit + "_15~18", timelimit + "_12~15", timelimit + "_18~21");
							} else if (18 < speed && speed <= 21) {
								correntMode = getCorrentMode(avgDeviationMap, rangeMap, speed, timelimit + "_18~21", timelimit + "_15~18", timelimit + "_21~24");
							} else if (21 < speed && speed <= 24) {
								correntMode = getCorrentMode(avgDeviationMap, rangeMap, speed, timelimit + "_21~24", timelimit + "_18~21", timelimit + "_24");
							} else {
								// 24以上则订正数据 = 众数 + 本级的平均偏小误差
								String smallError = StringUtils.split(avgDeviationMap.get(timelimit + "_24"), StringUtils.DELIM_COMMA).get(1);// 平均偏小误差
								correntMode = speed + Double.parseDouble(smallError);
							}
							
							// 计算集合平均风向
							if (count != 0) {
								double uAvg = formatDouble(sum_u / count, df);
								double vAvg = formatDouble(sum_v / count, df);
								windDir = getWindSpeedAndDirection(uAvg, vAvg).getWindDirection();
							}
						}
						
						if ((windDir + "").matches(numericPattern) && (correntMode + "").matches(numericPattern)) {
							windDir = formatDouble(windDir, dcmFmt);
							correntMode = formatDouble(correntMode, dcmFmt);
						} else {
							log.error("文件[" + fileName + "]经纬度位置[" + lon + ", " + lat + "]合成的最终风速风向中出现非数值类型，合成的风向为：" + windDir + "，合成的风速为：" + correntMode);
							windDir = 9999.0;
							correntMode = 9999.0;
						}
						out.write((j + 1) + "\t" + lon + "\t" + lat + "\t" + (j + 1) + "\t1\t9999\t9999\t9999\t" + windDir + "\t" + correntMode + "\n");
					}
				}
				out.flush();
				log.info("风速风向合成MICAPS第2类成功，文件存放路径为：" + outputFile);
			}
		} catch(Exception e) {
			log.error("订正后的风速风向合成处理失败：" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description: 提取MICAPS第2类文件中逐12小时的所有浮标站点数据并输出，并将所有第2类文件的风向风速拆分成UV矢量输出为MICAPS第11类格式文件
	 * @Author: songwj
	 * @Date: 2017-12-7 下午2:44:01
	 * @param micaps2OutputPath
	 * @param afterCorrectDataOutputPath
	 * @param micaps11OutputPath
	 * @param out
	 */
	private static void micaps2ToMicaps11(String micaps2OutputPath, String afterCorrectDataOutputPath, String micaps11OutputPath, BufferedWriter out) {
		try {
			File micaps2File = new File(micaps2OutputPath);
			// 过滤获取所有MICAPS第2类文件名
			String[] fileNames = micaps2File.list(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					if (name.matches("\\d{10}\\.\\d{3}")) {
						return true;
					}
					return false;
				}
			});
			
			// 将MICAPS2转MICAPS11
			if (fileNames.length > 0) {
				for (String fileName : fileNames) {
					String filePath = micaps2OutputPath + fileName;
					// 读取MICAPS第2类文件
					GridModel gridModel = micaps2ToGridModel(filePath, "utf-8", false);
					
					String year = gridModel.getYear();
					String month = gridModel.getMonth();
					String day = gridModel.getDay();
					String batch = gridModel.getBatch();
					String timelimit = gridModel.getTimelimit();
					String layer = gridModel.getLayer();
					double lonStart = gridModel.getLonStart();
					double lonEnd = gridModel.getLonEnd();
					double latStart = gridModel.getLatStart();
					double latEnd = gridModel.getLatEnd();
					double lonGap = gridModel.getLonGap();
					double latGap = gridModel.getLatGap();
					int xCount = gridModel.getxCount();
					int yCount = gridModel.getyCount();
					Micaps2Point[][] points = (Micaps2Point[][]) gridModel.getPoints();
					
					// 插值出所有浮标站点逐12小时风速值
					if (Integer.parseInt(timelimit) % 12 == 0) {
						String correctfilePath = afterCorrectDataOutputPath + fileName;
						out = new BufferedWriter(new FileWriter(new File(correctfilePath)));
						
						for (BuoyStationInfo stationInfo : stationInfos) {
							String stationCode = stationInfo.getStationCode();// 站点编号
							double lon = stationInfo.getLon();// 站点经度
							double lat = stationInfo.getLat();// 站点纬度
							double stationVal = getValByInterpolation(gridModel, lon, lat, new DecimalFormat("###.00"));
							out.write(stationCode + "\t" + stationVal + "\n");
						}
						out.flush();
						log.info("订正后的浮标站点数据提取成功，文件存放路径：" + correctfilePath);
					}
					
					String outputFilePath = micaps11OutputPath + fileName;
					out = new BufferedWriter(new FileWriter(outputFilePath));
					// 输出MICAPS第11类头信息
					out.write("diamond 11 " + fileName + "_10mMeanWind\n");
					out.write(year + "\t" + month + "\t" + day + "\t" + batch + "\t" + timelimit + "\t" + layer + "\t" + lonGap + "\t" + latGap + "\t"
							+ lonStart + "\t" + lonEnd + "\t" + latStart + "\t" + latEnd + "\t" + xCount + "\t" + yCount + "\n");
					
					// 输出u分量数据
					for (int i = 0; i < yCount; i++) {
						for (int j = 0; j < xCount; j++) {
							double windSpeed = points[i][j].getWindSpeed();
							double windDir = points[i][j].getWindDirection();
							double u = 9999.0;
							
							if (windSpeed != 9999.0 && windDir != 9999.0) {
								// u分量 = -风速 * sin(π * 风向/180)
								u = -windSpeed * Math.sin(Math.PI * windDir / 180);
								u = formatDouble(u, df);
							}
							
							int col = j + 1;
							
							if (col == xCount)
								out.write(u + "\n");
							else if (col % 10 == 0)	
								out.write(u + "\n");
							else
								out.write(u + "\t");
						}
					}
					
					// 输出v分量数据
					for (int i = 0; i < yCount; i++) {
						for (int j = 0; j < xCount; j++) {
							double windSpeed = points[i][j].getWindSpeed();
							double windDir = points[i][j].getWindDirection();
							double v = 9999.0;
							
							if (windSpeed != 9999.0 && windDir != 9999.0) {
								// v分量 = -风速 * cos(π * 风向/180)
								v = -windSpeed * Math.cos(Math.PI * windDir / 180);
								v = formatDouble(v, df);
							}
							
							int col = j + 1;
							
							if (col == xCount)
								out.write(v + "\n");
							else if (col % 10 == 0)	
								out.write(v + "\n");
							else
								out.write(v + "\t");
						}
					}
					out.flush();
					log.info("MICAPS第2类文件转MICAPS第11类文件成功，文件存放路径为：" + outputFilePath);
				}
			}
		} catch(Exception e) {
			log.error("MICAPS第2类文件转MICAPS第11类文件失败：" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * @Description: 将MICAPS第11类逐3小时文件插值为逐小时文件
	 * @author: songwj
	 * @date: 2017-9-20 下午8:08:16
	 * @param micaps11OutputPath
	 * @param datetime
	 * @param timelimitNum
	 * @param timeInterval
	 * @param out
	 * @param out1
	 */
	private static void micaps11ToHourly(String micaps11OutputPath, String datetime, int timelimitNum, int timeInterval, BufferedWriter out, BufferedWriter out1) {
		try {
			for (int n = 1; n < timelimitNum; n++) {
				if (n == timelimitNum - 1) {
					break;
				}
				
				// 从003开始，获取相连两个时效数据文件数据
				String timelimit1 = StringUtils.addZero(n * timeInterval, 3);
				String timelimit2 = StringUtils.addZero((n + 1) * timeInterval, 3);
				String fileName1 = datetime + "." + timelimit1;
				String fileName2 = datetime + "." + timelimit2;
				String micaps11FilePath1 = micaps11OutputPath + fileName1;
				String micaps11FilePath2 = micaps11OutputPath + fileName2;
				Micaps11GridModel gridModel_1 = micaps11ToGridModel(micaps11FilePath1, "utf-8");
				
				if (gridModel_1 != null) {
					log.info("文件[" + micaps11FilePath1 + "]加载成功!!!");
				} else {
					continue;
				}
				
				Micaps11GridModel gridModel_2 = micaps11ToGridModel(micaps11FilePath2, "utf-8");
				
				if (gridModel_2 != null) {
					log.info("文件[" + micaps11FilePath2 + "]加载成功!!!");
				} else {
					continue;
				}
				
				String year = gridModel_1.getYear();
				String month = gridModel_1.getMonth();
				String day = gridModel_1.getDay();
				String batch = gridModel_1.getBatch();
				String layer = gridModel_1.getLayer();
				double lonStart = gridModel_1.getLonStart();
				double lonEnd = gridModel_1.getLonEnd();
				double latStart = gridModel_1.getLatStart();
				double latEnd = gridModel_1.getLatEnd();
				double lonGap = gridModel_1.getLonGap();
				double latGap = gridModel_1.getLatGap();
				int xCount = gridModel_1.getxCount();
				int yCount = gridModel_1.getyCount();
				Point[][] uPoints1 = gridModel_1.getPoints();
				Point[][] vPoints1 = gridModel_1.getvPoints();
				Point[][] uPoints2 = gridModel_2.getPoints();
				Point[][] vPoints2 = gridModel_2.getvPoints();
				
				// 输出文件
				String outTimelimit1 = StringUtils.addZero(n * timeInterval + 1, 3);
				String outTimelimit2 = StringUtils.addZero(n * timeInterval + 2, 3);
				String outfileName1 = datetime + "." + outTimelimit1;
				String outfileName2 = datetime + "." + outTimelimit2;
				String outputFilePath1 = micaps11OutputPath + outfileName1;
				String outputFilePath2 = micaps11OutputPath + outfileName2;
				out = new BufferedWriter(new FileWriter(outputFilePath1));
				out1 = new BufferedWriter(new FileWriter(outputFilePath2));
				// 输出MICAPS第11类头信息
				out.write("diamond 11 " + outfileName1 + "_10mMeanWind\n");
				out.write(year + "\t" + month + "\t" + day + "\t" + batch + "\t" + outTimelimit1 + "\t" + layer + "\t" + lonGap + "\t" + latGap + "\t"
						+ lonStart + "\t" + lonEnd + "\t" + latStart + "\t" + latEnd + "\t" + xCount + "\t" + yCount + "\n");
				out1.write("diamond 11 " + outfileName2 + "_10mMeanWind\n");
				out1.write(year + "\t" + month + "\t" + day + "\t" + batch + "\t" + outTimelimit2 + "\t" + layer + "\t" + lonGap + "\t" + latGap + "\t"
						+ lonStart + "\t" + lonEnd + "\t" + latStart + "\t" + latEnd + "\t" + xCount + "\t" + yCount + "\n");
				
				// 输出u方向数据
				for (int i = 0; i < yCount; i++) {
					for (int j = 0; j < xCount; j++) {
						double u_1 = uPoints1[i][j].getValue();
						double u_2 = uPoints2[i][j].getValue();
						double u1 = 9999.0;
						double u2 = 9999.0;
						
						// 只要有一个值为缺报，则时间插值结果就处理为缺报
						if (u_1 != 9999.0 && u_2 != 9999.0) {
							double delta = (u_2 - u_1) / 3;
							u1 = u_1 + delta;
							u2 = u_1 + delta * 2;
							u1 = formatDouble(u1, df);
							u2 = formatDouble(u2, df);
						}
						
						int col = j + 1;
						
						if (col == xCount) {
							out.write(u1 + "\n");
							out1.write(u2 + "\n");
						} else if (col % 10 == 0) {
							out.write(u1 + "\n");
							out1.write(u2 + "\n");
						} else {
							out.write(u1 + "\t");
							out1.write(u2 + "\t");
						}
					}
				}
				
				// 输出v方向数据
				for (int i = 0; i < yCount; i++) {
					for (int j = 0; j < xCount; j++) {
						double v_1 = vPoints1[i][j].getValue();
						double v_2 = vPoints2[i][j].getValue();
						double v1 = 9999.0;
						double v2 = 9999.0;
						
						// 只要有一个值为缺报，则时间插值结果就处理为缺报
						if (v_1 != 9999.0 && v_2 != 9999.0) {
							double delta = (v_2 - v_1) / 3;
							v1 = v_1 + delta;
							v2 = v_1 + delta * 2;
							v1 = formatDouble(v1, df);
							v2 = formatDouble(v2, df);
						}
						
						int col = j + 1;
						
						if (col == xCount) {
							out.write(v1 + "\n");
							out1.write(v2 + "\n");
						} else if (col % 10 == 0) {
							out.write(v1 + "\n");
							out1.write(v2 + "\n");
						} else {
							out.write(v1 + "\t");
							out1.write(v2 + "\t");
						}
					}
				}
				out.flush();
				out1.flush();
				log.info("MICAPS第11类文件插值成逐小时成功，文件存放路径为：" + outputFilePath1);
				log.info("MICAPS第11类文件插值成逐小时成功，文件存放路径为：" + outputFilePath2);
			}
		} catch(Exception e) {
			log.error("MICAPS第11类文件插值成逐小时数据文件失败：" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description: 将MICAP第2类文件的风速转为图像
	 * @author: songwj
	 * @date: 2017-9-17 下午11:22:20
	 * @param micaps2OutputPath
	 * @param imageOutputPath 图像输出根路径
	 */
	private static void meanWindMicaps2ToImage(String micaps2OutputPath, String imageOutputPath) {
		try {
			File micaps2File = new File(micaps2OutputPath);
			// 过滤获取所有MICAPS第2类文件名
			String[] fileNames = micaps2File.list(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					if (name.matches("\\d{10}\\.\\d{3}")) {
						return true;
					}
					return false;
				}
			});
			
			if (fileNames.length > 0) {
				for (String fileName : fileNames) {
					String filePath = micaps2OutputPath + fileName;
					String imagePath = imageOutputPath + fileName + ".png";
					micapsToImage(filePath, "utf-8", 2, "wind", new FileOutputStream(imagePath));
					log.info("文件[" + filePath + "]转图像成功，图像存放路径为：" + imagePath);
				}
			}
		} catch(Exception e) {
			log.error("MICAPS第2类文件转图像失败：" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description: 提取最大风，从003时效开始，每4个一组，取最大风，输出为MICAPS第2类文件
	 * @author: songwj
	 * @date: 2017-9-21 下午8:00:01
	 * @param micaps2OutputPath
	 * @param datetime
	 * @param timelimitNum
	 * @param timeInterval
	 * @param maxWindOutputPath
	 * @param out
	 */
	private static void getMaxWindOutputMicaps2(String micaps2OutputPath, String datetime, int timelimitNum, int timeInterval, String maxWindOutputPath, BufferedWriter out) {
		try {
			for (int n = 1; n < timelimitNum; n+=4) {
				List<GridModel> gridModels = new ArrayList<GridModel>();
				
				// 加载逐3小时MICAPS第2类文件，从003时效开始，每4个一组，取最大风
				for (int k = n; k < n + 4; k++) {
					String timelimit = StringUtils.addZero(k * timeInterval, 3);
					String filePath = micaps2OutputPath + datetime + "." + timelimit;
					
					if (!new File(filePath).exists()) {
						log.error("指定文件[" + filePath + "]不存在!!!");
						continue;
					}
					
					GridModel gridModel = micaps2ToGridModel(filePath, "utf-8", false);
					log.info("文件[" + filePath + "]加载成功!!!");
					gridModels.add(gridModel);
				}
				
				// 以最大时效的文件信息作为最终输出的文件信息
				GridModel gm = gridModels.get(3);
				String year = gm.getYear();
				String month = gm.getMonth();
				String day = gm.getDay();
				String batch = gm.getBatch();
				String timelimit = gm.getTimelimit();
				String layer = gm.getLayer();
				int xCount = gm.getxCount();
				int yCount = gm.getyCount();
				int pointsNum = xCount * yCount;
				
				String fileName = "max_" + datetime + "." + timelimit;
				String outputFile = maxWindOutputPath + fileName;
				out = new BufferedWriter(new FileWriter(new File(outputFile)));
				out.write("diamond 2 " + fileName + "_10mMeanWind\n");
				out.write(year + "\t" + month + "\t" + day + "\t" + batch + "\t" + layer + "\t" + pointsNum + "\n");
				
				// 提取4个时效文件中同一个点风速最大的数据，并输出为MICAPS第2类文件
				for (int i = 0; i < yCount; i++) {
					for (int j = 0; j < xCount; j++) {
						double maxWindSpeed = -1.0;
						double maxWindDirection = 9999.0;
						
						for (GridModel gridModel : gridModels) {
							Micaps2Point[][] points = (Micaps2Point[][]) gridModel.getPoints();
							double windSpeed = points[i][j].getWindSpeed();
							
							if (windSpeed != 9999.0) {
								double windDir = points[i][j].getWindDirection();
								if (windSpeed > maxWindSpeed) {
									maxWindSpeed = windSpeed;
									maxWindDirection = windDir;
								}
							}
						}
						
						if (maxWindSpeed == -1.0) {
							maxWindSpeed = 9999.0;
						}
						
						Point point = gridModels.get(0).getPoints()[i][j];
						out.write((j + 1) + "\t" + point.getX() + "\t" + point.getY() + "\t" + (j + 1) + "\t1\t9999\t9999\t9999\t" + maxWindDirection + "\t" + maxWindSpeed + "\n");
					}
				}
				out.flush();
				log.info("最大风提取成功，文件存放路径为：" + outputFile);
			}
		} catch(Exception e) {
			log.error("最大风提取失败：" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description: 将提取的最大风数据文件转为图像
	 * @Author: songwj
	 * @Date: 2017-12-4 下午3:20:03
	 * @param maxWindOutputPath
	 * @param imageOutputPath
	 */
	private static void maxWindMicaps2ToImage(String maxWindOutputPath, String imageOutputPath) {
		try {
			File micaps2File = new File(maxWindOutputPath);
			// 过滤获取所有MICAPS第2类最大风文件名
			String[] fileNames = micaps2File.list(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					if (name.matches("max_\\d{10}\\.\\d{3}")) {
						return true;
					}
					return false;
				}
			});
			
			if (fileNames.length > 0) {
				for (String fileName : fileNames) {
					String filePath = maxWindOutputPath + fileName;
					String imagePath = imageOutputPath + fileName + ".png";
					micapsToImage(filePath, "utf-8", 2, "wind", new FileOutputStream(imagePath));
					log.info("文件[" + filePath + "]转图像成功，图像存放路径为：" + imagePath);
				}
			}
		} catch(Exception e) {
			log.error("MICAPS第2类文件转图像失败：" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description: 提取指定站点从003~072逐小时的风速风向数据并输出
	 * @author: songwj
	 * @date: 2017-9-22 上午11:10:18
	 * @param micaps11OutputPath
	 * @param datetime
	 * @param timelimitNum
	 * @param timeInterval
	 * @param stationDataOutputPath
	 * @param out
	 */
	private static void getStationHourlyData(String micaps2OutputPath, String micaps11OutputPath, String datetime, int timelimitNum, int timeInterval, String stationDataOutputPath, BufferedWriter out) {
		try {
			DecimalFormat dcmFmt = new DecimalFormat("###.00");
			// MICAPS第2类逐3小时文件解析数据
			List<GridModel> gms = new ArrayList<GridModel>();
			// 存放逐小时时效各个站点的风速值数据
			Map<String, Double> speedMap = new HashMap<String, Double>();
			
			for (int i = 1; i < timelimitNum; i++) {
				String timelimit = StringUtils.addZero(i * timeInterval, 3);
				String micaps2File = micaps2OutputPath + datetime + "." + timelimit;
				
				if (!new File(micaps2File).exists())
					continue;
				
				gms.add(micaps2ToGridModel(micaps2File, "utf-8", false));
				log.info("文件[" + micaps2File + "]加载成功!!!");
			}
			
			// 使用MICAPS第2类逐3小时的站点风速值，插值为逐小时
			if (gms != null && gms.size() == timelimitNum - 1) {
				for (int i = 1; i < timelimitNum - 1; i++) {
					GridModel gm1 = gms.get(i - 1);// 003
					GridModel gm4 = gms.get(i);// 006
					
					for (BuoyStationInfo stationInfo : stationInfos) {
						String stationCode = stationInfo.getStationCode();
						double val1 = getValByInterpolation(gm1, stationInfo.getLon(), stationInfo.getLat(), df);
						double val4 = getValByInterpolation(gm4, stationInfo.getLon(), stationInfo.getLat(), df);
						double delta = (val4 - val1) / 3;
						double val2 = NumberFormatUtil.numberFormat(val1 + delta, 2);
						double val3 = NumberFormatUtil.numberFormat(val1 + delta * 2, 2);
						val1 = NumberFormatUtil.numberFormat(val1, 2);
						val4 = NumberFormatUtil.numberFormat(val4, 2);
						// key: 003_54558
						speedMap.put(StringUtils.addZero(i * timeInterval, 3) + "_" + stationCode, val1);
						speedMap.put(StringUtils.addZero(i * timeInterval + 1, 3) + "_" + stationCode, val2);
						speedMap.put(StringUtils.addZero(i * timeInterval + 2, 3) + "_" + stationCode, val3);
						speedMap.put(StringUtils.addZero(i * timeInterval + 3, 3) + "_" + stationCode, val4);
					}
				}
			} else {
				return;
			}
			
			// 文件数量
			int fileNum = (timelimitNum - 1) * timeInterval - 2;
			
			for (int n = 1; n <= fileNum; n++) {
				String timelimit = StringUtils.addZero(n + 2, 3);
				String filePath = micaps11OutputPath + datetime + "." + timelimit;
				
				if (!new File(filePath).exists()) {
					log.error("指定文件[" + filePath + "]不存在!!!");
					continue;
				}
				
				Micaps11GridModel gridModel = micaps11ToGridModel(filePath, "utf-8");
				log.info("文件[" + filePath + "]加载成功!!!");
				
				for (BuoyStationInfo stationInfo : stationInfos) {
					String stationCode = stationInfo.getStationCode();
					StationPoint point = new StationPoint(stationCode, stationInfo.getLon(), stationInfo.getLat());
					String outputFile = stationDataOutputPath + stationCode + ".txt";
					out = new BufferedWriter(new FileWriter(new File(outputFile), true));
					// 计算获取对应预报时间
					String beijingTime = DatetimeUtil.universalTime2beijingTime(datetime, DatetimeUtil.YYYYMMDDHH);
					Date date = new Date(DatetimeUtil.parse(beijingTime, DatetimeUtil.YYYYMMDDHH).getTime() + (n + 2) * 60 * 60 * 1000);
					beijingTime = DatetimeUtil.format(date, DatetimeUtil.YYYYMMDDHH);
					// 插值出站点u和v分量，并合成风速风向输出
					WindSpdAndDirModel model = getStationWindSpdAndDir(point, gridModel);
					String key = timelimit + "_" + stationCode;
					out.write(beijingTime + "\t" + (speedMap.containsKey(key) ? speedMap.get(key) : "9999.0") + "\t" + formatDouble(model.getWindDirection(), dcmFmt) + "\n");
					out.flush();
					
					if (n == fileNum) {
						log.info("站点[" + stationCode + "]逐小时数据提取成功，文件存放路径为：" + outputFile);
					}
				}
			}
		} catch(Exception e) {
			log.error("指定站点逐小时数据提取失败：" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description: 获取所有浮标站点的实况数据
	 * @Author: songwj
	 * @Date: 2017-12-7 下午5:21:02
	 * @param datetime 世界时，如：2017120600
	 * @param liveDataOutputPath
	 * @param out
	 */
	private static void getBuoyStationLiveData(String datetime, String liveDataOutputPath, BufferedWriter out) {
		try {
			// 平均风实况数据根目录
			String liveDataRootPath = FileConfigUtil.getProperty("file.meanWindLiveDataRootPath");
			// YYYYMMDDHH + 8 → YYMMDDHH
			String beijingTime = DatetimeUtil.universalTime2beijingTime(datetime, DatetimeUtil.YYYYMMDDHH, DatetimeUtil.YYMMDDHH);
			String fileName = beijingTime + ".000";
			String stationFilePath = liveDataRootPath + fileName;
			
			if (!new File(stationFilePath).exists()) {
				log.error("指定的10米平均风实况站点数据文件[" + stationFilePath + "]不存在!!!");
				return;
			}
			
			// key：站点编号，value：风速
			Map<String, Double> windMap = StationFileUtil.micaps1ToWindMap(stationFilePath);
			String outFile = liveDataOutputPath + fileName;
			out = new BufferedWriter(new FileWriter(new File(outFile)));
			
			// 提取浮标站点实况数据
			for (BuoyStationInfo stationInfo : stationInfos) {
				String stationCode = stationInfo.getStationCode();
				String stCode = StringUtils.addZero(stationCode, 6);
				String stationVal = windMap.containsKey(stCode) ? windMap.get(stCode).toString() : "9999";
				out.write(stationCode + "\t" + stationVal + "\n");
			}
			out.flush();
			log.info("10米平均风实况站点数据提取成功，存放路径：" + outFile);
		} catch (Exception e) {
			log.error("获取浮标站点实况数据失败：" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description: 计算指定站点的风速和风向
	 * @author: songwj
	 * @date: 2017-9-22 上午10:59:51
	 * @param point
	 * @param gridModel
	 * @return
	 */
	private static WindSpdAndDirModel getStationWindSpdAndDir(StationPoint point, Micaps11GridModel gridModel) {
		List<Point> uFourPoints = getNearFourPoints(point, gridModel, df);
		// 插值出u分量
		double u = calcPointValue(uFourPoints.get(0), uFourPoints.get(1), uFourPoints.get(2), uFourPoints.get(3), point, df);
		// 获取v分量格点数据
		Point[][] vPoints = gridModel.getvPoints();
		Micaps11GridModel vGridModel = gridModel;
		vGridModel.setPoints(vPoints);
		List<Point> vFourPoints = getNearFourPoints(point, vGridModel, df);
		// 插值出v分量
		double v = calcPointValue(vFourPoints.get(0), vFourPoints.get(1), vFourPoints.get(2), vFourPoints.get(3), point, df);
		return getWindSpeedAndDirection(u, v);
	}

	/**
	 * @Description: 指定时间段内的10米阵风基础数据处理
	 * @Author: songwj
	 * @Date: 2017-11-30 下午8:08:39
	 * @param timeStart
	 * @param timeEnd
	 */
	public static void grads10mMaxWindProcessByTimeRange(String timeStart, String timeEnd) {
		// 10m风预报原始数据根目录
		String rootPath = FileConfigUtil.getProperty("file.10mWindRootPath");
		// 获取起止时间段内逐12小时的所有时间序列
		List<String> timeSequence = DatetimeUtil.getTimeSequence(timeStart, timeEnd, DatetimeUtil.YYYYMMDDHH, 12);
		for (String tmSeq : timeSequence) {
			String path = rootPath + tmSeq + "/ecmf_short_10m_wind_gust_in_last_6h.dat";
			grads10mMaxWindProcess(path, "utf-8");
		}
	}
	
	/**
	 * @Description: 10米阵风基础数据处理
	 * @Author: songwj
	 * @Date: 2017-11-30 下午8:11:54
	 * @param gradsFilePath
	 * @param charset
	 */
	public static void grads10mMaxWindProcess(String gradsFilePath, String charset) {
		BufferedReader br = null;
		DataInputStream in = null;
		BufferedWriter out1 = null;
		BufferedWriter out2 = null;
		// 存储每行拆分后的字符串
		List<String> strData = new ArrayList<String>();
		DecimalFormat df = new DecimalFormat("###.000000");
		
		try {
			File file = new File(gradsFilePath);
			if (!file.exists()) {
				log.error("指定路径文件不存在，指定的文件为：" + gradsFilePath);
				return;
			}
			// 读取GrADS二进制格点文件数据
			in = new DataInputStream(new FileInputStream(file));
			// 获取ctl文件路径
			String ctlRootPath = file.getParent();
			String ctlPath = ctlRootPath + "/ecmf_short_10m_wind_gust_in_last_6h.ctl";
			if (!new File(ctlPath).exists()) {
				log.error("指定的文件不存在，指定的文件为：" + ctlPath);
				return;
			}
			// 读取ctl文件
			br = new BufferedReader(new InputStreamReader(new FileInputStream(ctlPath), charset));
			// 跳过前面三行
			FileOperationUtil.readLine(br);
			FileOperationUtil.readLine(br);
			FileOperationUtil.readLine(br);
			// 读取第四行
			String row = FileOperationUtil.readLine(br);
			StringUtils.split(strData, row, StringUtils.DELIM_TAB);
			// 纬向格点数
			int xCount = Integer.parseInt(strData.get(1));
			// 起始经度
			double lonStart = formatDouble(Double.parseDouble(strData.get(3)), df);
			// 经度格距
			double lonGap = formatDouble(Double.parseDouble(strData.get(4)), df);
			// 终止经度
			double lonEnd = formatDouble(lonStart + (xCount - 1) * lonGap, df);
			// 读取第五行
			row = FileOperationUtil.readLine(br);
			strData = new ArrayList<String>();
			StringUtils.split(strData, row, StringUtils.DELIM_TAB);
			// 经向格点数
			int yCount = Integer.parseInt(strData.get(1));
			// 起始纬度
			double latStart = formatDouble(Double.parseDouble(strData.get(3)), df);
			// 纬度格距
			double latGap = formatDouble(Double.parseDouble(strData.get(4)), df);
			// 终止纬度
			double latEnd = formatDouble(latStart + (yCount - 1) * latGap, df);
			// 2016030100
			String datetime = file.getParentFile().getName();
			String year = datetime.substring(0, 4);
			String month = datetime.substring(4, 6);
			String day = datetime.substring(6, 8);
			String batch = datetime.substring(8, 10);
			// 世界时转北京时（+8）
			String bjDatetime = DatetimeUtil.universalTime2beijingTime(datetime, DatetimeUtil.YYYYMMDDHH);
			// 第6行
			FileOperationUtil.readLine(br);
			// 第7行
			row =  FileOperationUtil.readLine(br);
			strData = new ArrayList<String>();
			StringUtils.split(strData, row, StringUtils.DELIM_TAB);
			// 时效个数
			int timelimitNum = Integer.parseInt(strData.get(1));
			// 小时间隔数
			String hr = strData.get(4);
			int timeInterval = Integer.parseInt(hr.substring(0, hr.indexOf("hr")));
			// 第8行
			row =  FileOperationUtil.readLine(br);
			strData = new ArrayList<String>();
			StringUtils.split(strData, row, StringUtils.DELIM_TAB);
			// 成员（要素）个数
			int memberNum = Integer.parseInt(strData.get(1));
			// 所有成员名
			List<String> members = new ArrayList<String>();
			for (int i = 0; i < memberNum; i++) {
				members.add(strData.get(3 + i));
			}
			// 提取GrADS二进制文件中的格点数据值
			int length = (int)file.length();
			byte[] buf = new byte[length];
			float[] values = new float[length/4];// 存放格点数据值，float类型数据占4字节
			
			while (true) {
				int len = in.read(buf, 0, length);
				if (len == -1) {
					break;
				}
			}
			
			int index = 0;
			for (int start = 0; start < length; start = start + 4) {
				values[index] = arr2Float(buf, start);
				index++;
			}
			
			GridModel gridModel = new GridModel();
			
			gridModel.setYear(year);// 年
			gridModel.setMonth(month);// 月
			gridModel.setDay(day);// 日
			gridModel.setBatch(batch);// 时次
			gridModel.setTimelimit(StringUtils.addZero(0, 3));// 时效
			gridModel.setParameter(members.get(0));// 成员名（要素）
			gridModel.setxCount(xCount);// 纬向格点数
			gridModel.setyCount(yCount);// 经向格点数
			gridModel.setLonStart(lonStart);// 起始经度
			gridModel.setLatStart(latStart);// 起始纬度
			gridModel.setLonEnd(lonEnd);// 终止经度
			gridModel.setLatEnd(latEnd);// 终止纬度
			gridModel.setLonGap(lonGap);// 经度格距
			gridModel.setLatGap(latGap);// 纬度格距
			// 存储数据点
			Point[][] points = new Point[yCount][xCount];
			// 重新归零起算
			index = 0;
			
			for (int i = 0; i < yCount; i++) {
				for (int j = 0; j < xCount; j++) {
					points[i][j] = new Point(formatDouble(lonStart + j * lonGap, df), formatDouble(latStart + i * latGap, df), formatDouble(values[index], df));
					index++;
				}
			}
			
			gridModel.setPoints(points);
			
			// 存储每个站点每个时间的众数（key: 2016030108_006_706040, value: 1.772727）
			Map<String, Double> modeVals = new HashMap<String, Double>();
			// 存放所有的站点编号
			List<String> stationCodes = new ArrayList<String>();
			// 获取风所有的站点信息
			Map<String, String> windStationConfig = WindStationConfig.windStationConfig;
			Set<Map.Entry<String, String>> entrySet = windStationConfig.entrySet();
			
			for (Map.Entry<String, String> me : entrySet) {
				// 站点编号
				String stationCode = me.getKey();
				stationCodes.add(stationCode);
				// 站点所对应的经纬度
				List<String> lonLat = StringUtils.split(me.getValue(), StringUtils.DELIM_COMMA);
				double lon = Double.parseDouble(lonLat.get(1));
				double lat = Double.parseDouble(lonLat.get(0));
				
				Point pnt = new Point(lon, lat);
				// 计算获取指定站点的临近四个点以及这个四个点分别在网格中位置下标
				Map<String, List<Point>> pointMap = getNearFourPointsAndIndex(pnt, gridModel, df);
				// 获取指定站点所有成员从006~084时效的临近4个点，并插值出指定站点的值（key：成员_时效，value：风速）
				Map<String, Double> perMemberAndTimelimitVals = getAllMembersPerTimelimitVals(pointMap, values, members, timelimitNum, timeInterval, xCount, yCount, pnt);
				// 计算获取每个站点每个时效的众数（key: 2016030108_006_706040, value: 1.772727）
				getPerStationPerTimelimitMode(modeVals, perMemberAndTimelimitVals, members, timelimitNum, timeInterval, bjDatetime, stationCode);
			}
			
			// 计算获取每个站点每个时效的平均误差和平均绝对误差（key: 2016030108_006_706040）
			// 输出误差数据，含指定时间指定站点的：众数、实况数据（最近6小时站点最大值）、平均误差和平均绝对误差（平均误差 = 预报数据（众数） - 实况数据，平均绝对误差 = |预报数据（众数） - 实况数据|）
			Map<String, StationErrorModel> stationErrModelMap = getMaxWindPerStationAndPerTimeErrData(modeVals, stationCodes, bjDatetime, timelimitNum, timeInterval);
			String baseDataOutputRootPath = FileConfigUtil.getProperty("file.maxWindBaseDataOutputRootPath");
			
			if (!new File(baseDataOutputRootPath).exists()) {
				new File(baseDataOutputRootPath).mkdirs();
			}
			
			String baseDataOutputPath = baseDataOutputRootPath + datetime + ".txt";
			out1 = new BufferedWriter(new FileWriter(new File(baseDataOutputPath)));
			for (int i = 1; i < timelimitNum; i++) {
				String tmlimit = StringUtils.addZero(i * timeInterval, 3);
				for (String stcode : stationCodes) {
					String key = bjDatetime + "_" + tmlimit + "_" + stcode;
					if (stationErrModelMap.containsKey(key)) {
						StationErrorModel model = stationErrModelMap.get(key);
						out1.write(key + "\t" + model.getMode() + "\t" + model.getActualWindSpeed() + "\t" + model.getAvgError() + "\t" + model.getAvgAbsError() + "\n");
					} else {
						out1.write(key + "\n");
					}
				}
			}
			log.info("10米阵风站点的基础数据输出成功，数据存放路径为：" + baseDataOutputPath);
			
			// 计算获取每个时效不同风速区间的累加平均误差和平均绝对误差数据（key：2016030108_006_0~3）
			Map<String, ErrorModel> errModelMap = getAccumulationErrorData(stationErrModelMap, stationCodes, bjDatetime, timelimitNum, timeInterval);
			// 输出累加后的最终数据，含：累加后的平均误差、平均绝对误差和累加的次数
			String finalDataOutputRootPath = FileConfigUtil.getProperty("file.maxWindFinalDataOutputRootPath");
			
			if (!new File(finalDataOutputRootPath).exists()) {
				new File(finalDataOutputRootPath).mkdirs();
			}
			
			String finalDataOutputPath = finalDataOutputRootPath + datetime + ".txt";
			out2 = new BufferedWriter(new FileWriter(new File(finalDataOutputPath)));
			int maxSpeed = 24;// 风速段的最大值
			for (int i = 1; i < timelimitNum; i++) {
				String tmlimit = StringUtils.addZero(i * timeInterval, 3);
				for (int j = 0; j <= maxSpeed; j+=3 ) {
					String key = j == maxSpeed ? (bjDatetime + "_" + tmlimit + "_" + maxSpeed) : (bjDatetime + "_" + tmlimit + "_" + j + "~" + (j + 3));
					if (errModelMap.containsKey(key)) {
						ErrorModel model = errModelMap.get(key);
						// 输出依次为：yyyyMMddHH_时效_风速段、平均误差、平均绝对误差、偏小次数、正确次数、偏大次数、累积总次数
						out2.write(key + "\t" + model.getAvgError() + "\t" + model.getAvgAbsError() + "\t" + model.getSmallCount() + "\t" + model.getRightCount() + "\t" + model.getLargeCount() + "\t" + model.getCount() + "\n");
					}
				}
			}
			log.info("10米阵风文件日累积数据输出成功，数据存放路径为：" + finalDataOutputPath);
		} catch(Exception e) {
			log.error("10m阵风数据处理失败：" + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (out1 != null) {
					out1.flush();
					out1.close();
				}
				if (out2 != null) {
					out2.flush();
					out2.close();
				}
				if (br != null) {
					br.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @Description: 累加统计指定时间段的10m阵风平均误差、平均绝对误差、偏小概率、正确概率、偏大概率、偏大、正确和偏小区间
	 * @Author: songwj
	 * @Date: 2017-11-30 上午11:11:35
	 * @param timeStart
	 * @param timeEnd
	 */
	public static void get10mMaxWindAccumulateResult(String timeStart, String timeEnd) {
		// 10m阵风预报最终存放数据根目录
		String finalDataOutputRootPath = FileConfigUtil.getProperty("file.maxWindFinalDataOutputRootPath");
		// 10米阵风最终累积数据输出根目录
		String finalAccOutputRootPath = FileConfigUtil.getProperty("file.maxWindFinalAccOutputRootPath");
		// 10米阵风统计结果输出根目录
		String finalStatisticOutputRootPath = FileConfigUtil.getProperty("file.maxWindFinalStatisticOutputRootPath");
		int timelimitNum = 15;
		int timeInterval = 6;
		accumulate10mWind(timeStart, timeEnd, finalDataOutputRootPath, finalAccOutputRootPath, finalStatisticOutputRootPath, timelimitNum, timeInterval);
	}
	
	/**
	 * @Description: 获取指定时间段内的10m阵风平均偏大、偏小误差
	 * @Author: songwj
	 * @Date: 2017-11-30 下午12:44:45
	 * @param timeStart
	 * @param timeEnd
	 */
	public static void get10mMaxWindAvgLargeAndSmallError(String timeStart, String timeEnd) {
		// 10m风预报最终累加数据存放根目录
		String finalAccumulateOutputRootPath = FileConfigUtil.getProperty("file.maxWindFinalAccOutputRootPath");
		int timelimitNum = 15;// 时效个数
		int timeInterval = 6;// 时效间隔
		getAvgLargeAndSmallError(timeStart, timeEnd, finalAccumulateOutputRootPath, timelimitNum, timeInterval);
	}
	
	/**
	 * @Description: 将10m阵风数据文件依次插值为0.1公里场、求众数、订正、最后处理为MICAPS文件
	 * @author: songwj
	 * @date: 2017-9-7 下午6:41:49
	 * @param gradsFilePath GrADS文件全路径
	 * @param avgDeviationFilePath 平均偏大、偏小误差文件全路径
	 * @param rangeFilePath 偏大、正确、偏小区间文件全路径
	 * @param charset
	 */
	public static void grads10mMaxWindToMicapsProcess(String gradsFilePath, String avgDeviationFilePath, String rangeFilePath, String charset) {
		BufferedReader br = null;
		DataInputStream in = null;
		BufferedWriter out = null;
		// 存储每行拆分后的字符串
		List<String> strData = new ArrayList<String>();
		DecimalFormat df = new DecimalFormat("###.000000");
		
		try {
			File file = new File(gradsFilePath);
			if (!file.exists()) {
				log.error("指定路径文件不存在，指定的文件为：" + gradsFilePath);
				return;
			}
			// 数据存放的根目录
			String rootPath = FileConfigUtil.getProperty("file.actualMaxWindDataOutputRootPath");
			// 剔除陆地点的MICAPS文件存放路径
			String wavePath = rootPath + "wave";
			
			// 读取GrADS二进制格点文件数据
			in = new DataInputStream(new FileInputStream(file));
			// 获取ctl文件路径
			String ctlRootPath = file.getParent();
			String ctlPath = ctlRootPath + "/ecmf_short_10m_wind_gust_in_last_6h.ctl";
			if (!new File(ctlPath).exists()) {
				log.error("指定的文件不存在，指定的文件为：" + ctlPath);
				return;
			}
			// 读取ctl文件
			br = new BufferedReader(new InputStreamReader(new FileInputStream(ctlPath), charset));
			// 跳过前面三行
			FileOperationUtil.readLine(br);
			FileOperationUtil.readLine(br);
			FileOperationUtil.readLine(br);
			// 读取第四行
			String row = FileOperationUtil.readLine(br);
			StringUtils.split(strData, row, StringUtils.DELIM_TAB);
			// 纬向格点数
			int xCount = Integer.parseInt(strData.get(1));
			// 起始经度
			double lonStart = formatDouble(Double.parseDouble(strData.get(3)), df);
			// 经度格距
			double lonGap = formatDouble(Double.parseDouble(strData.get(4)), df);
			// 终止经度
			double lonEnd = formatDouble(lonStart + (xCount - 1) * lonGap, df);
			// 读取第五行
			row = FileOperationUtil.readLine(br);
			strData = new ArrayList<String>();
			StringUtils.split(strData, row, StringUtils.DELIM_TAB);
			// 经向格点数
			int yCount = Integer.parseInt(strData.get(1));
			// 起始纬度
			double latStart = formatDouble(Double.parseDouble(strData.get(3)), df);
			// 纬度格距
			double latGap = formatDouble(Double.parseDouble(strData.get(4)), df);
			// 终止纬度
			double latEnd = formatDouble(latStart + (yCount - 1) * latGap, df);
			// 2016030100
			String datetime = file.getParentFile().getName();
			String year = datetime.substring(0, 4);
			String month = datetime.substring(4, 6);
			String day = datetime.substring(6, 8);
			String batch = datetime.substring(8, 10);
			// 第6行
			FileOperationUtil.readLine(br);
			// 第7行
			row =  FileOperationUtil.readLine(br);
			strData = new ArrayList<String>();
			StringUtils.split(strData, row, StringUtils.DELIM_TAB);
			// 时效个数
			int timelimitNum = Integer.parseInt(strData.get(1));
			// 小时间隔数
			String hr = strData.get(4);
			int timeInterval = Integer.parseInt(hr.substring(0, hr.indexOf("hr")));
			// 第8行
			row =  FileOperationUtil.readLine(br);
			strData = new ArrayList<String>();
			StringUtils.split(strData, row, StringUtils.DELIM_TAB);
			// 成员（要素）个数
			int memberNum = Integer.parseInt(strData.get(1));
			// 所有成员名
			List<String> members = new ArrayList<String>();
			for (int i = 0; i < memberNum; i++) {
				members.add(strData.get(3 + i));
			}
			// 第9行
			row =  FileOperationUtil.readLine(br);
			strData = new ArrayList<String>();
			StringUtils.split(strData, row, StringUtils.DELIM_TAB);
			// 变量个数
			int varNum = Integer.parseInt(strData.get(1));
			
			// MICAPS第4类文件输出的根目录
			String micaps4OutputPath = rootPath + "micaps4/" + datetime + "/";
			// 图像输出的根目录
			String imageOutputPath = rootPath + "image/" + datetime + "/";
			// 指定站点提取的逐小时数据存放根目录
			String stationDataOutputPath = rootPath + "stationData/" + datetime + "/";
			
			if (!new File(micaps4OutputPath).exists())
				new File(micaps4OutputPath).mkdirs();
			
			if (!new File(imageOutputPath).exists())
				new File(imageOutputPath).mkdirs();
			
			// 如果对应日期站点该目录及站点数据文件事先存在则先删除后再创建目录
			FileUtils.deleteDirectory(new File(stationDataOutputPath));
			
			if (!new File(stationDataOutputPath).exists())
				new File(stationDataOutputPath).mkdirs();
			
			// 获取GrADS二进制文件中所有格点的数据值
			int length = (int)file.length();
			byte[] buf = new byte[length];
			float[] values = new float[length/4];// 存放格点数据值，float类型数据占4字节
			
			while (true) {
				int len = in.read(buf, 0, length);
				if (len == -1) {
					break;
				}
			}
			
			int index = 0;
			for (int start = 0; start < length; start = start + 4) {
				values[index] = arr2Float(buf, start);
				index++;
			}
			
			GridModel gridModel = new GridModel();
			
			gridModel.setYear(year);// 年
			gridModel.setMonth(month);// 月
			gridModel.setDay(day);// 日
			gridModel.setBatch(batch);// 时次
			gridModel.setTimelimit(StringUtils.addZero(0, 3));// 时效
			gridModel.setParameter(members.get(0));// 成员名（要素）
			gridModel.setxCount(xCount);// 纬向格点数
			gridModel.setyCount(yCount);// 经向格点数
			gridModel.setLonStart(lonStart);// 起始经度
			gridModel.setLatStart(latStart);// 起始纬度
			gridModel.setLonEnd(lonEnd);// 终止经度
			gridModel.setLatEnd(latEnd);// 终止纬度
			gridModel.setLonGap(lonGap);// 经度格距
			gridModel.setLatGap(latGap);// 纬度格距
			// 存储数据点
			Point[][] points = new Point[yCount][xCount];
			// 重新归零起算
			index = 0;
			
			for (int i = 0; i < yCount; i++) {
				for (int j = 0; j < xCount; j++) {
					points[i][j] = new Point(formatDouble(lonStart + j * lonGap, df), formatDouble(latStart + i * latGap, df), formatDouble(values[index], df));
					index++;
				}
			}
			
			gridModel.setPoints(points);
			// 1.获取从006~084时效的众数、对众数订正、最终将订正后众数输出为MICAP第4类文件
			getModeAndOutputMicaps4(gridModel, datetime, timelimitNum, timeInterval, varNum, members, values, year, month, day, batch, xCount, yCount, 
					lonStart, latStart, lonEnd, latEnd, avgDeviationFilePath, rangeFilePath, wavePath, micaps4OutputPath, out);
			// 2.将MICAP第4类逐6小时文件的风速转为图像
			maxWindMicaps4ToImage(micaps4OutputPath, imageOutputPath);
			// 3.提取指定站点从006~084逐6小时的风速值
			getMaxWindStationData(micaps4OutputPath, datetime, timelimitNum, timeInterval, stationDataOutputPath, out);
		} catch(Exception e) {
			log.error("10m阵风数据处理失败：" + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
				if (br != null) {
					br.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @Description: 获取从006~084时效的众数、对众数订正、最终将订正后众数输出为MICAP第4类文件
	 * @Author: songwj
	 * @Date: 2017-12-1 下午8:02:17
	 * @param gridModel
	 * @param datetime
	 * @param timelimitNum
	 * @param timeInterval
	 * @param varNum
	 * @param members
	 * @param values
	 * @param year
	 * @param month
	 * @param day
	 * @param batch
	 * @param xCount
	 * @param yCount
	 * @param lonStart
	 * @param latStart
	 * @param lonEnd
	 * @param latEnd
	 * @param avgDeviationFilePath
	 * @param rangeFilePath
	 * @param wavePath
	 * @param micaps4OutputPath
	 * @param out
	 */
	private static void getModeAndOutputMicaps4(GridModel gridModel, String datetime, int timelimitNum, int timeInterval, int varNum, List<String> members, 
			float[] values, String year, String month, String day, String batch, int xCount, int yCount, double lonStart, double latStart, double lonEnd, 
			double latEnd, String avgDeviationFilePath, String rangeFilePath, String wavePath, String micaps4OutputPath, BufferedWriter out) {
		try {
			if (!new File(avgDeviationFilePath).exists()) {
				log.error("指定的平均偏大、偏小误差数据文件不存在，指定文件为：" + avgDeviationFilePath);
				return;
			}
			
			if (!new File(rangeFilePath).exists()) {
				log.error("指定的偏大、正确、偏小区间数据文件不存在，指定文件为：" + rangeFilePath);
				return;
			}
			
			if (!new File(wavePath).exists()) {
				log.error("指定位置的剔除陆地点模版文件不存在，指定路径为：" + wavePath);
				return;
			}
			
			DecimalFormat dcmFmt = new DecimalFormat("###.00");
			// 加载剔除陆地点的模版文件
			GridModel waveGridModel = micaps4ToGridModel(wavePath, "utf-8", false);
			log.info("剔除陆地点的模版文件[" + wavePath + "]加载成功!!!");
			Point[][] wavePoints = waveGridModel.getPoints();
			
			// 获取平均偏大、偏小误差
			Map<String, String> avgDeviationMap = file2Map(avgDeviationFilePath);
			// 获取偏大、正确、偏小区间
			Map<String, String> rangeMap = file2Map(rangeFilePath);
			
			// 降完尺度后的经纬度格距及经纬向格点数
			double lon_gap = 0.1;
			double lat_gap = 0.1;
			double lonSub = formatDouble(Math.abs(lonEnd - lonStart), df);
			double latSub = formatDouble(Math.abs(latStart - latEnd), df);
			int x_count = (int) (lonSub / lon_gap + 1);
			int y_count = (int) (latSub / lat_gap + 1);
			
			int len = xCount * yCount;// 单个网格数据点个数
			int timelimitSpan = len * varNum;// 时效跨度
			int memberSpan = timelimitSpan * timelimitNum;// 成员跨度
			int memberNum = members.size();// 成员数
			// 时效
			for (int n = 1; n < timelimitNum; n++) {
				String timelimit = StringUtils.addZero(n * timeInterval, 3);
				String fileName = datetime + "." + timelimit;
				String outputFile = micaps4OutputPath + fileName;
				out = new BufferedWriter(new FileWriter(new File(outputFile)));
				out.write("diamond 4 " + fileName + "_10mMaxWind\n");
				out.write(year + "\t" + month + "\t" + day + "\t" + batch + "\t" + timelimit + "\t10\t0.1\t0.1\t" + lonStart + "\t" + lonEnd + "\t" 
						+ latStart + "\t" + latEnd + "\t" + x_count + "\t" + y_count + "\t2.0\t0.0\t70.0\t0.0\t10.8\n");
				
				for (int i = 0; i < y_count; i++) {
					for (int j = 0; j < x_count; j++) {
						// 订正后的众数值
						double correntMode = 9999.0;
						double lon = formatDouble(wavePoints[i][j].getX(), dcmFmt);
						double lat = formatDouble(wavePoints[i][j].getY(), dcmFmt);
						
						// 如果为陆地点则直接处理为缺报
						if (wavePoints[i][j].getValue() != 9999.0) {
							// 待插值点
							Point pnt = new Point(lon, lat);
							// 计算获取指定点的临近四个点以及这个四个点分别在网格中下标位置
							Map<String, List<Point>> pointMap = getNearFourPointsAndIndex(pnt, gridModel, df);
							
							List<Point> points = pointMap.get("fourPoints");
							List<Point> index = pointMap.get("fourIndex");
							
							Point p11 = points.get(0);// 左下
							Point p12 = points.get(1);// 左上
							Point p21 = points.get(2);// 右下
							Point p22 = points.get(3);// 右上
							Point idx11 = index.get(0);// 左下
							Point idx12 = index.get(1);// 左上
							Point idx21 = index.get(2);// 右下
							Point idx22 = index.get(3);// 右上
							// 获取临近四个点在网格的行列下标值（从0起算）
							int x11 = (int) idx11.getX();// 行
							int y11 = (int) idx11.getY();// 列
							int x12 = (int) idx12.getX();// 行
							int y12 = (int) idx12.getY();// 列
							int x21 = (int) idx21.getX();// 行
							int y21 = (int) idx21.getY();// 列
							int x22 = (int) idx22.getX();// 行
							int y22 = (int) idx22.getY();// 列
							// 统计落在各个风速段的次数
							int sum0_3 = 0;
							int sum3_6 = 0;
							int sum6_9 = 0;
							int sum9_12 = 0;
							int sum12_15 = 0;
							int sum15_18 = 0;
							int sum18_21 = 0;
							int sum21_24 = 0;
							int sum24 = 0;
							// 统计各个成员非异常值的总次数
							int count = 0;
							
							for (int k = 0; k < memberNum; k++) {
								// 获取临近4个点，values中依次对应时效跨度、本网格中的位置、成员跨度
								Point pnt11 = new Point(p11.getX(), p11.getY(), values[n * timelimitSpan + xCount * x11 + y11 + k * memberSpan]);
								Point pnt12 = new Point(p12.getX(), p12.getY(), values[n * timelimitSpan + xCount * x12 + y12 + k * memberSpan]);
								Point pnt21 = new Point(p21.getX(), p21.getY(), values[n * timelimitSpan + xCount * x21 + y21 + k * memberSpan]);
								Point pnt22 = new Point(p22.getX(), p22.getY(), values[n * timelimitSpan + xCount * x22 + y22 + k * memberSpan]);
								
								// 插值出指定点风速值
								double speed = calcPointValue(pnt11, pnt12, pnt21, pnt22, pnt, new DecimalFormat("###.0000000"));
								
								// 剔除异常值
								if (speed <= 40) {
									// 判断各个成员的风速所处的区间，统计各个在各个区间的次数
									if (0 <= speed && speed <= 3) {
										sum0_3 += 1;
									} else if (3 < speed && speed <= 6) {
										sum3_6 += 1;
									} else if (6 < speed && speed <= 9) {
										sum6_9 += 1;
									} else if (9 < speed && speed <= 12) {
										sum9_12 += 1;
									} else if (12 < speed && speed <= 15) {
										sum12_15 += 1;
									} else if (15 < speed && speed <= 18) {
										sum15_18 += 1;
									} else if (18 < speed && speed <= 21) {
										sum18_21 += 1;
									}  else if (21 < speed && speed <= 24) {
										sum21_24 += 1;
									} else {
										sum24 += 1;
									}
									count++;
								}
							}
							
							if (count > 0) {
								// 存放每个区间的次数
								Map<String, Integer> perTimes = new HashMap<String, Integer>();
								perTimes.put("0_3", sum0_3);
								perTimes.put("3_6", sum3_6);
								perTimes.put("6_9", sum6_9);
								perTimes.put("9_12", sum9_12);
								perTimes.put("12_15", sum12_15);
								perTimes.put("15_18", sum15_18);
								perTimes.put("18_21", sum18_21);
								perTimes.put("21_24", sum21_24);
								perTimes.put("24", sum24);
								// 存放频数最高的区间名和对应值
								String maxRangeName = null;
								int maxRangeNum = 0;
								// 获取频数最高的区间及对应数值
								Set<Map.Entry<String, Integer>> entrySet = perTimes.entrySet();
								for (Map.Entry<String, Integer> me : entrySet) {
									if (me.getValue() > maxRangeNum) {
										maxRangeName = me.getKey();
										maxRangeNum = me.getValue();
									}
								}
								// 众数组下限值
								int L = 0;
								// 风速区间中的最大值
								int maxWindSpd = 24;
								if ((maxWindSpd + "").equals(maxRangeName)) {
									L = maxWindSpd;
								} else {
									L = Integer.parseInt(StringUtils.split(maxRangeName, StringUtils.UNDERLINE).get(0));
								}
								// 计算众数组相连的前一组和后一组频数
								int f1 = 0;
								int f2 = 0;
								int K = 3;// 组距（单位：m/s）
								if (L == 0) {
									f1 = 0;
									f2 = perTimes.get("3_6");
								} else if (L == maxWindSpd) {
									f1 = perTimes.get((maxWindSpd - K) + "_" + maxWindSpd);
									f2 = 0;
								} else {
									f1 = perTimes.get((L - K) + "_" + L);
									int floor = L + K;
									f2 = perTimes.get((floor == maxWindSpd ? (maxWindSpd + "") : (L + K) + "_" + (L + K * 2)));
								}
								// 计算获取众数
								double speed = calcMode(L, maxRangeNum, f1, f2, K);
								// 众数订正
								// 缺报不订正
								if (speed == 9999.0) {
									correntMode = speed;
									// 众数在0~3之间不订正
								} else if (0 <= speed && speed <= 3) {
									correntMode = speed;
								} else if (3 < speed && speed <= 6) {
									correntMode = getCorrentMode(avgDeviationMap, rangeMap, speed, timelimit + "_3~6", timelimit + "_0~3", timelimit + "_6~9");
								} else if (6 < speed && speed <= 9) {
									correntMode = getCorrentMode(avgDeviationMap, rangeMap, speed, timelimit + "_6~9", timelimit + "_3~6", timelimit + "_9~12");
								} else if (9 < speed && speed <= 12) {
									correntMode = getCorrentMode(avgDeviationMap, rangeMap, speed, timelimit + "_9~12", timelimit + "_6~9", timelimit + "_12~15");
								} else if (12 < speed && speed <= 15) {
									correntMode = getCorrentMode(avgDeviationMap, rangeMap, speed, timelimit + "_12~15", timelimit + "_9~12", timelimit + "_15~18");
								} else if (15 < speed && speed <= 18) {
									correntMode = getCorrentMode(avgDeviationMap, rangeMap, speed, timelimit + "_15~18", timelimit + "_12~15", timelimit + "_18~21");
								} else if (18 < speed && speed <= 21) {
									correntMode = getCorrentMode(avgDeviationMap, rangeMap, speed, timelimit + "_18~21", timelimit + "_15~18", timelimit + "_21~24");
								} else if (21 < speed && speed <= 24) {
									correntMode = getCorrentMode(avgDeviationMap, rangeMap, speed, timelimit + "_21~24", timelimit + "_18~21", timelimit + "_24");
								} else {
									// 24以上则订正数据 = 众数 + 本级的平均偏小误差
									String smallError = StringUtils.split(avgDeviationMap.get(timelimit + "_24"), StringUtils.DELIM_COMMA).get(1);// 平均偏小误差
									correntMode = speed + Double.parseDouble(smallError);
								}
							}
						}
						
						if ((correntMode + "").matches(numericPattern)) {
							correntMode = formatDouble(correntMode, dcmFmt);
						} else {
							log.error("文件[" + fileName + "]经纬度位置[" + lon + ", " + lat + "]订正的最终风速值为非数值类型，该风速值为：" + correntMode);
							correntMode = 9999.0;
						}
						
						int col = j + 1;
								
						if (col == x_count)
							out.write(correntMode + "\n");
						else if (col % 10 == 0)	
							out.write(correntMode + "\n");
						else
							out.write(correntMode + "\t");
					}
				}
				out.flush();
				log.info("10米阵风订正转MICAPS第4类文件成功，文件存放路径为：" + outputFile);
			}
		} catch(Exception e) {
			log.error("10米阵风订正转MICAPS第4类文件失败：" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description: 将MICAP第4类逐6小时文件的风速转为图像
	 * @Author: songwj
	 * @Date: 2017-12-1 下午8:02:30
	 * @param micaps4OutputPath
	 * @param imageOutputPath
	 */
	private static void maxWindMicaps4ToImage(String micaps4OutputPath, String imageOutputPath) {
		try {
			File micaps4File = new File(micaps4OutputPath);
			// 过滤获取所有MICAPS第4类文件名
			String[] fileNames = micaps4File.list(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					if (name.matches("\\d{10}\\.\\d{3}")) {
						return true;
					}
					return false;
				}
			});
			
			if (fileNames.length > 0) {
				for (String fileName : fileNames) {
					String filePath = micaps4OutputPath + fileName;
					String imagePath = imageOutputPath + fileName + ".png";
					micapsToImage(filePath, "utf-8", 4, "wind", new FileOutputStream(imagePath));
					log.info("文件[" + filePath + "]转图像成功，图像存放路径为：" + imagePath);
				}
			}
		} catch(Exception e) {
			log.error("阵风MICAPS第4类文件转图像失败：" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description: 提取指定站点从006~084逐6小时的风速值
	 * @Author: songwj
	 * @Date: 2017-12-1 下午8:02:55
	 * @param micaps4OutputPath
	 * @param datetime
	 * @param timelimitNum
	 * @param timeInterval
	 * @param stationDataOutputPath
	 * @param out
	 */
	private static void getMaxWindStationData(String micaps4OutputPath, String datetime, int timelimitNum, int timeInterval, String stationDataOutputPath, BufferedWriter out) {
		try {
			DecimalFormat dcmFmt = new DecimalFormat("###.00");
			// 获取待插值的站点
			List<BuoyStationInfo> stationInfos = BuoyStationInfoConfig.buoyStationInfos;
			
			for (int n = 1; n < timelimitNum; n++) {
				String timelimit = StringUtils.addZero(n * timeInterval, 3);
				String filePath = micaps4OutputPath + datetime + "." + timelimit;
				
				if (!new File(filePath).exists()) {
					log.error("指定文件[" + filePath + "]不存在!!!");
					continue;
				}
				
				GridModel gridModel = micaps4ToGridModel(filePath, "utf-8", false);
				log.info("文件[" + filePath + "]加载成功!!!");
				
				for (BuoyStationInfo stationInfo : stationInfos) {
					String stationCode = stationInfo.getStationCode();
					StationPoint point = new StationPoint(stationCode, stationInfo.getLon(), stationInfo.getLat());
					String outputFile = stationDataOutputPath + stationCode + ".txt";
					out = new BufferedWriter(new FileWriter(new File(outputFile), true));
					// 计算获取对应预报时间
					String beijingTime = DatetimeUtil.universalTime2beijingTime(datetime, DatetimeUtil.YYYYMMDDHH);
					Date date = new Date(DatetimeUtil.parse(beijingTime, DatetimeUtil.YYYYMMDDHH).getTime() + n * timeInterval * 60 * 60 * 1000);
					beijingTime = DatetimeUtil.format(date, DatetimeUtil.YYYYMMDDHH);
					// 插值出站点的风速值
					List<Point> fourPoints = getNearFourPoints(point, gridModel, df);
					double speed = calcPointValue(fourPoints.get(0), fourPoints.get(1), fourPoints.get(2), fourPoints.get(3), point, dcmFmt);
					out.write(beijingTime + "\t" + speed + "\n");
					out.flush();
					
					if (n == timelimitNum - 1) {
						log.info("站点[" + stationCode + "]10米阵风逐6小时数据提取成功，文件存放路径为：" + outputFile);
					}
				}
			}
		} catch(Exception e) {
			log.error("10米阵风指定站点逐6小时数据提取失败：" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description: 获取指定站点所有成员从006~084时效的临近4个点，并插值出指定站点的值（key：成员_时效，value：风速）
	 * @Author: songwj
	 * @Date: 2017-12-1 上午10:35:50
	 * @param pointMap
	 * @param values
	 * @param members
	 * @param timelimitNum
	 * @param timeInterval
	 * @param xCount
	 * @param yCount
	 * @param pnt
	 * @return
	 */
	private static Map<String, Double> getAllMembersPerTimelimitVals(Map<String, List<Point>> pointMap, float[] values, List<String> members, int timelimitNum, int timeInterval, int xCount, int yCount, Point pnt) {
		Map<String, Double> map = new HashMap<String, Double>();
		
		if (pointMap != null && members != null && members.size() > 0) {
			List<Point> points = pointMap.get("fourPoints");
			List<Point> index = pointMap.get("fourIndex");
			
			Point p11 = points.get(0);// 左下
			Point p12 = points.get(1);// 左上
			Point p21 = points.get(2);// 右下
			Point p22 = points.get(3);// 右上
			Point idx11 = index.get(0);// 左下
			Point idx12 = index.get(1);// 左上
			Point idx21 = index.get(2);// 右下
			Point idx22 = index.get(3);// 右上
			// 获取临近四个点在网格的行列下标值（从0起算）
			int x11 = (int) idx11.getX();// 行
			int y11 = (int) idx11.getY();// 列
			int x12 = (int) idx12.getX();// 行
			int y12 = (int) idx12.getY();// 列
			int x21 = (int) idx21.getX();// 行
			int y21 = (int) idx21.getY();// 列
			int x22 = (int) idx22.getX();// 行
			int y22 = (int) idx22.getY();// 列
			
			int idx = 0;
			int len = xCount * yCount;
			// 遍历所有成员
			for (String member : members) {
				// 15个时效，跳过000时效
				for (int i = 0; i < timelimitNum; i++) {
					if (i == 0) {
						idx = idx + len;
					} else {
						String timelimit = StringUtils.addZero(i * timeInterval, 3);
						Point pnt11 = new Point(p11.getX(), p11.getY(), values[idx + xCount * x11 + y11]);
						Point pnt12 = new Point(p12.getX(), p12.getY(), values[idx + xCount * x12 + y12]);
						Point pnt21 = new Point(p21.getX(), p21.getY(), values[idx + xCount * x21 + y21]);
						Point pnt22 = new Point(p22.getX(), p22.getY(), values[idx + xCount * x22 + y22]);
						// 插值出风速值
						double value = calcPointValue(pnt11, pnt12, pnt21, pnt22, pnt, new DecimalFormat("###.00000000"));
						// key → c00_006
						map.put(member + "_" + timelimit, value);
						idx = idx + len;
					}
				}
			}
		}
		
		return map;
	}
	
	/**
	 * @Description: 计算获取每个站点每个时效的众数（key: 2016030108_006_706040, value: 1.772727）
	 * @Author: songwj
	 * @Date: 2017-11-30 下午8:13:21
	 * @param modeVals
	 * @param perMemberAndTimelimitVals
	 * @param members
	 * @param timelimitNum
	 * @param timeInterval
	 * @param datetime
	 * @param stationCode
	 */
	private static void getPerStationPerTimelimitMode(Map<String, Double> modeVals, Map<String, Double> perMemberAndTimelimitVals, List<String> members, int timelimitNum, int timeInterval,
			String datetime, String stationCode) {
		if (perMemberAndTimelimitVals != null && members != null && members.size() > 0) {
			for (int i = 1; i < timelimitNum; i++) {
				// 时效
				String timelimit = StringUtils.addZero(i * timeInterval, 3);
				int sum0_3 = 0;
				int sum3_6 = 0;
				int sum6_9 = 0;
				int sum9_12 = 0;
				int sum12_15 = 0;
				int sum15_18 = 0;
				int sum18_21 = 0;
				int sum21_24 = 0;
				int sum24 = 0;
				
				// 计算每个时效的众数
				for (String member : members) {
					// 风速（单位：m/s）
					double speed = perMemberAndTimelimitVals.get(member + "_" + timelimit);
					// 判断各个成员的风速所处的区间，统计各个在各个区间的次数
					if (0 <= speed && speed <= 3) {
						sum0_3 += 1;
					} else if (3 < speed && speed <= 6) {
						sum3_6 += 1;
					} else if (6 < speed && speed <= 9) {
						sum6_9 += 1;
					} else if (9 < speed && speed <= 12) {
						sum9_12 += 1;
					} else if (12 < speed && speed <= 15) {
						sum12_15 += 1;
					} else if (15 < speed && speed <= 18) {
						sum15_18 += 1;
					} else if (18 < speed && speed <= 21) {
						sum18_21 += 1;
					} else if (21 < speed && speed <= 24) {
						sum21_24 += 1;
					} else {
						sum24 += 1;
					}
				}
				
				// 存放每个区间的次数
				Map<String, Integer> perTimes = new HashMap<String, Integer>();
				perTimes.put("0_3", sum0_3);
				perTimes.put("3_6", sum3_6);
				perTimes.put("6_9", sum6_9);
				perTimes.put("9_12", sum9_12);
				perTimes.put("12_15", sum12_15);
				perTimes.put("15_18", sum15_18);
				perTimes.put("18_21", sum18_21);
				perTimes.put("21_24", sum21_24);
				perTimes.put("24", sum24);
				// 存放频数最高的区间名和对应值
				String maxRangeName = null;
				int maxRangeNum = 0;
				// 获取频数最高的区间及对应数值
				Set<Map.Entry<String, Integer>> entrySet = perTimes.entrySet();
				for (Map.Entry<String, Integer> me : entrySet) {
					if (me.getValue() > maxRangeNum) {
						maxRangeName = me.getKey();
						maxRangeNum = me.getValue();
					}
				}
				// 众数组下限值
				int L = 0;
				int maxSpeed = 24;// 风速段中的最大值
				if ((maxSpeed + "").equals(maxRangeName)) {
					L = maxSpeed;
				} else {
					L = Integer.parseInt(StringUtils.split(maxRangeName, StringUtils.UNDERLINE).get(0));
				}
				// 计算众数组相连的前一组和后一组频数
				int f1 = 0;
				int f2 = 0;
				int K = 3;// 组距（单位：m/s）
				if (L == 0) {
					f1 = 0;
					f2 = perTimes.get("3_6");
				} else if (L == maxSpeed) {
					f1 = perTimes.get((maxSpeed - 3) + "_" + maxSpeed);
					f2 = 0;
				} else {
					f1 = perTimes.get((L - K) + "_" + L);
					int floor = L + K;
					f2 = perTimes.get((floor == maxSpeed ? (maxSpeed + "") : (L + K) + "_" + (L + K * 2)));
				}
				// 计算获取众数
				double mode = calcMode(L, maxRangeNum, f1, f2, K);
				
				modeVals.put(datetime + "_" + timelimit + "_" + stationCode, mode);
			}
		}
	}
	
	/**
	 * @Description: 计算获取每个站点每个时效的平均误差和平均绝对误差（key: 2016030108_006_706040）
	 * @Author: songwj
	 * @Date: 2017-11-30 下午8:13:44
	 * @param modeVals
	 * @param stationCodes
	 * @param datetime
	 * @param timelimitNum
	 * @param timeInterval
	 * @return
	 */
	private static Map<String, StationErrorModel> getMaxWindPerStationAndPerTimeErrData(Map<String, Double> modeVals, List<String> stationCodes, String datetime, int timelimitNum, int timeInterval) {
		Map<String, StationErrorModel> map = new HashMap<String, StationErrorModel>();
		
		if (modeVals != null && modeVals.size() > 0 && stationCodes != null && stationCodes.size() > 0) {
			for (int i = 1; i < timelimitNum; i++) {
				// 计算获取预报所对应的实况时间
				Date date = DatetimeUtil.parse(datetime, DatetimeUtil.YYYYMMDDHH);
				String actualTime = DatetimeUtil.format(new Date(date.getTime() + i * timeInterval * 60 * 60 * 1000), DatetimeUtil.YYYYMMDDHH);
				// 解析获取所有站点阵风实况数据近6小时最大风速值
				// 例如：预报时间为2016030108.006，对应实况时间为2016030114，则取实况时间中2016030114、2016030113、2016030112、2016030111、2016030110和2016030109对应站点最大的值
				Map<String, Double> maxWindMap = getStationLast6HMaxWindSpeed(actualTime, stationCodes);
				// 时效
				String timelimit = StringUtils.addZero(i * timeInterval, 3);
				
				// 计算每个站点每个时效的平均误差和平均绝对误差
				for (String stationCode : stationCodes) {
					String modeValKey = datetime + "_" + timelimit + "_" + stationCode;
					if (modeVals.containsKey(modeValKey) && maxWindMap.containsKey(stationCode)) {
						// 实况风速
						double actualWindSpd = maxWindMap.get(stationCode);
						// 众数
						double modeVal = modeVals.get(datetime + "_" + timelimit + "_" + stationCode);
						
						// 剔除实况和预报大于40的数据
						if (actualWindSpd <= 40.0 && modeVal <= 40.0) {
							// 平均误差 = 预报数据（众数） - 实况数据
							double avgError = modeVal - actualWindSpd;
							// 平均绝对误差 = |预报数据（众数） - 实况数据|
							double avgAbsError = Math.abs(modeVal - actualWindSpd);
							map.put(datetime + "_" + timelimit + "_" + stationCode, new StationErrorModel(modeVal, actualWindSpd, avgError, avgAbsError));
						} else {
							log.warn(datetime + "_" + timelimit + "_" + stationCode + " >>> 实况风速为：" + actualWindSpd + "，众数值为：" + modeVal);
						}
					}
				}
			}
		}
		
		return map;
	}
	
	/**
	 * @Description: 解析获取所有站点阵风实况数据近6小时最大风速值
	 * @Author: songwj
	 * @Date: 2017-11-30 下午8:14:01
	 * @param actualTime
	 * @param stationCodes
	 * @return
	 */
	private static Map<String, Double> getStationLast6HMaxWindSpeed(String actualTime, List<String> stationCodes) {
		Map<String, Double> maxWindMap = new HashMap<String, Double>();
		// 阵风实况数据存放根目录
		String maxWindRootPath = FileConfigUtil.getProperty("file.maxWindRootPath");
		
		Date date = DatetimeUtil.parse(actualTime, DatetimeUtil.YYYYMMDDHH);
		// 获取最近6小时中的起始时间
		String timeStart = DatetimeUtil.format(new Date(date.getTime() - 5 * 60 * 60 * 1000), DatetimeUtil.YYYYMMDDHH);
		List<String> timeSequence = DatetimeUtil.getTimeSequence(timeStart, actualTime, DatetimeUtil.YYYYMMDDHH, 1);
		List<Map<String, Double>> windMapList = new ArrayList<Map<String, Double>>();
		
		for (String tmSeq : timeSequence) {
			// 解析实况站点数据文件（MICAPS第一类自动观测站数据）
			String stationFilePath = maxWindRootPath + tmSeq.substring(0, 6) + "/" + tmSeq.substring(2) + ".000";
			
			if (!new File(stationFilePath).exists()) {
				log.error("指定的实况自动观测站数据不存在，指定的数据文件为：" + stationFilePath);
				continue;
			}
			
			windMapList.add(StationFileUtil.micaps1ToWindMap(stationFilePath));
		}
		
		// 获取最近6小时阵风数据同一站点的最大值
		for (String stationCode : stationCodes) {
			double maxSpeed = -10;
			String key = StringUtils.addZero(stationCode, 6);
			
			for (Map<String, Double> windMap : windMapList) {
				if (windMap != null) {
					if (windMap.containsKey(key)) {
						double speed = windMap.get(key);
						if (speed != 9999.0 && speed > maxSpeed) {
							maxSpeed = speed;
						}
					}
				}
			}
			
			maxWindMap.put(stationCode, maxSpeed >= 0 ? maxSpeed : 9999.0);
		}
		
		return maxWindMap;
	}

	/**
	 * @Description: 将MICAPS格点文件处理为图像输出
	 * @author: songwj
	 * @date: 2017-9-14 下午5:25:27
	 * @param micapsFilePath
	 * @param charset
	 * @param micapsType MICAPS文件类别，如：2、4等
	 * @param productType 产品类型，如：wind
	 * @param out
	 */
	public static void micapsToImage(String micapsFilePath, String charset, int micapsType, String productType, OutputStream out) {
		try {
			long start = System.currentTimeMillis();
			
			if(!new File(micapsFilePath).exists()){
				log.info("指定路径的Micaps4文件不存在，指定路径为：" + micapsFilePath);
				return;
			}
			
			// 获取背景图（无经纬度标注和头信息描述）
			InputStream is = GridFileUtil.class.getResourceAsStream("/com/webyun/seagrid/common/image/bkgd.png");
			BufferedImage bkgd = ImageIO.read(is);

			// 背景图片起始终止经纬度值（左上角和右下角）
			double bkgdLonStart = 55;
			double bkgdLatStart = 60;
			double bkgdLonEnd = 155;
			double bkgdLatEnd = 0;
			
			GridModel gridModel = null;
			
			if (micapsType == 2) {
				gridModel = micaps2ToGridModel(micapsFilePath, charset, true);
			} else if (micapsType == 4) {
				gridModel = micaps4ToGridModel(micapsFilePath, charset, true);
			}
			
			// 获取背景图片的宽度和高度
			int width = bkgd.getWidth();
			int height = bkgd.getHeight();
			// 图像距离背景图的左边界距离
			int left = 63;
			// 图像距离背景图的上边界距离
			int top = 91;
			// 最终图像
			BufferedImage finalImg = new BufferedImage(width + 2 * left, height + top + 47, BufferedImage.TYPE_INT_ARGB);
			
			// 绘图
			drawImage(gridModel, bkgd, finalImg, bkgdLonStart, bkgdLatStart, bkgdLonEnd, bkgdLatEnd, micapsType, productType, width, height, left, top);
			
			// 输出png图片，只有png图像支持alpha通道
			ImageIO.write(finalImg, "PNG", out);
			long end = System.currentTimeMillis();
			log.info("文件[" + micapsFilePath + "]转图像成功，耗时：" + (end - start) / 1000.0 + "秒");
		} catch (Exception e) {
			log.error("文件[" + micapsFilePath + "]转图像失败：" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 绘制色标
	 * @param g 画笔
	 * @param pal 调色板对象
	 * @param clr 色标上边三角形填充颜色
	 * @param left 图像距离背景图的左边界距离
	 * @param top 图像距离背景图的上边界距离
	 * @param width 底图宽度
	 * @param height 底图高度
	 * @param direction 方向（水平(h)和垂直(v)）
	 */
	public static void drawColorLabel(Graphics2D g, Palette pal, Color clr, int left, int top, int width, int height, String direction) {
		if (pal != null) {
			List<Entry> entries = pal.getEntries();
			int len = entries.size();
			boolean flag = true;// 标记默认数值小于1000
			
			// 如果色标值有超过1000，则字体变小，防止显示不全
			for (Entry entry : entries) {
				if (entry.getValue() > 1000){
					g.setFont(FontUtil.FNT_7);
					flag = false;
					break;
				}
			}
			
			if (flag) {
				g.setFont(FontUtil.FNT_5);
			}
			
			// 绘制垂直色标
			if ("v".equals(direction)) {
				// 色标宽高
				int w = 16;
				int h = len <= 15 ? 35 : 20;
				// 色标距离背景图左边边界的距离
				left = left + width + 8;
				// 色标距离背景图上边边界的距离
				top = top + (height - ((len + 1) * h)) / 2;
				// 上边三角形
				int[] xTopPoints = { left, left + 8, left + 16 };
				int[] yTopPoints = { top + h, top, top + h };
				g.setColor(clr);
				g.fillPolygon(xTopPoints, yTopPoints, 3);
				g.setColor(Color.BLACK);
				g.drawPolygon(xTopPoints, yTopPoints, 3);
				// 倒序取颜色分量并进行着色
				for (int i = len - 1, j = 1; i >= 0; i--, j++) {
					Entry entry = entries.get(i);
					String valText = null;
					float value = entry.getValue();
					if (value % 1 == 0) {
						int val = (int) value;
						valText = val + "";
					} else {
						valText = value + "";
					}
					String str = entry.getRgba();
					List<String> strDatas = StringUtils.split(str, StringUtils.DELIM_COMMA);
					List<Integer> rgba = strToInt(strDatas);
					g.setColor(new Color(rgba.get(0), rgba.get(1), rgba.get(2), rgba.get(3)));

					if (i != 0) {
						g.fillRect(left, top + h * j, w, h);
						g.setColor(Color.BLACK);
						g.drawRect(left, top + h * j, w, h);
					} else {
						// 下边三角形
						int[] xBottomPoints = { left, left + 8, left + 16 };
						int[] yBottomPoints = { top + h * j, top + h * (j + 1), top + h * j };
						g.fillPolygon(xBottomPoints, yBottomPoints, 3);
						g.setColor(Color.BLACK);
						g.drawPolygon(xBottomPoints, yBottomPoints, 3);
					}
					// 绘制标注值
					g.setColor(Color.BLACK);
					g.drawString(valText, left + 21, top + h * j + 5);
				}
				// 绘制水平色标
			} else {
				// 色标宽度
				int w = len <= 15 ? 60 : 35;
				int h = 16;
				// 色标距离背景图左边边界的距离
				left = left + (width - ((len + 1) * w)) / 2;
				// 色标距离背景图上边边界的距离
				top = top + height + 12;
				
				// 正序取颜色分量并进行着色
				for (int i = 0, j = 1; i < len; i++, j++) {
					Entry entry = entries.get(i);
					String valText = null;
					float value = entry.getValue();
					if (value % 1 == 0) {
						int val = (int) value;
						valText = val + "";
					} else {
						valText = value + "";
					}
					String str = entry.getRgba();
					List<String> strDatas = StringUtils.split(str, StringUtils.DELIM_COMMA);
					List<Integer> rgba = strToInt(strDatas);
					g.setColor(new Color(rgba.get(0), rgba.get(1), rgba.get(2), rgba.get(3)));

					if (i != 0) {
						g.fillRect(left + w * (j - 1), top, w, h);
						g.setColor(Color.BLACK);
						g.drawRect(left + w * (j - 1), top, w, h);
					} else {
						// 左边三角形
						int[] xBottomPoints = {left, left + w, left + w};
						int[] yBottomPoints = {top + 8, top, top + 16};
						g.fillPolygon(xBottomPoints, yBottomPoints, 3);
						g.setColor(Color.BLACK);
						g.drawPolygon(xBottomPoints, yBottomPoints, 3);
					}
					// 绘制标注值
					g.setColor(Color.BLACK);
					g.setFont(FontUtil.FNT_5);
					g.drawString(valText, left + w * j - 10, top + 40);
				}
				
				// 右边三角形
				int[] xTopPoints = {left + len * w, left + len * w + w, left + len * w};
				int[] yTopPoints = {top, top + 8, top + 16};
				g.setColor(clr);
				g.fillPolygon(xTopPoints, yTopPoints, 3);
				g.setColor(Color.BLACK);
				g.drawPolygon(xTopPoints, yTopPoints, 3);
			}
		}
	}
	
	/**
	 * @Description: 绘制风色标
	 * @author: songwj
	 * @date: 2017-9-22 下午6:43:04
	 * @param g
	 * @param pal
	 * @param clr
	 * @param left
	 * @param top
	 * @param width
	 * @param height
	 */
	public static void drawWindColorLabel(Graphics2D g, Palette pal, Color clr, int left, int top, int width, int height) {
		if (pal != null) {
			List<Entry> entries = pal.getEntries();
			int len = entries.size();
			g.setFont(FontUtil.FNT_8);
			
			// 色标宽高
			int w = 16;
			int h = len <= 15 ? 35 : 20;
			// 色标距离背景图左边边界的距离
			left = left + width + 8;
			// 色标距离背景图上边边界的距离
			top = top + (height - ((len + 1) * h)) / 2;
			// 上边三角形
			int[] xTopPoints = { left, left + 8, left + 16 };
			int[] yTopPoints = { top + h, top, top + h };
			g.setColor(clr);
			g.fillPolygon(xTopPoints, yTopPoints, 3);
			g.setColor(Color.BLACK);
			g.drawPolygon(xTopPoints, yTopPoints, 3);
			// 倒序取颜色分量并进行着色
			for (int i = len - 1, j = 1; i >= 0; i--, j++) {
				Entry entry = entries.get(i);
				String levText = i == 0 ? "" : (i + 3) + "级";
				String str = entry.getRgba();
				List<String> strDatas = StringUtils.split(str, StringUtils.DELIM_COMMA);
				List<Integer> rgba = strToInt(strDatas);
				g.setColor(new Color(rgba.get(0), rgba.get(1), rgba.get(2), rgba.get(3)));
				
				if (i != 0) {
					g.fillRect(left, top + h * j, w, h);
					g.setColor(Color.BLACK);
					g.drawRect(left, top + h * j, w, h);
				} else {
					// 下边三角形
					int[] xBottomPoints = { left, left + 8, left + 16 };
					int[] yBottomPoints = { top + h * j, top + h * (j + 1), top + h * j };
					g.fillPolygon(xBottomPoints, yBottomPoints, 3);
					g.setColor(Color.BLACK);
					g.drawPolygon(xBottomPoints, yBottomPoints, 3);
				}
				// 绘制标注值
				g.setColor(Color.BLACK);
				g.drawString(levText, left + 21, top + h * j + 22);
			}
		}
	}
	
	/**
	 * @Description: 图像绘制
	 * @author: songwj
	 * @date: 2017-9-14 下午7:49:42
	 * @param gridModel
	 * @param bkgd
	 * @param finalImg 最终图像
	 * @param bkgdLonStart
	 * @param bkgdLatStart
	 * @param bkgdLonEnd
	 * @param bkgdLatEnd
	 * @param micapsType
	 * @param productType
	 * @param width 背景图片的宽度
	 * @param height 背景图片的高度
	 * @param left 图像距离背景图的左边界距离
	 * @param top 图像距离背景图的上边界距离
	 */
	private static void drawImage(GridModel gridModel, BufferedImage bkgd, BufferedImage finalImg, double bkgdLonStart, double bkgdLatStart, 
			double bkgdLonEnd, double bkgdLatEnd, int micapsType, String productType, int width, int height, int left, int top) {
		String year = gridModel.getYear();
		String month = gridModel.getMonth();
		String day = gridModel.getDay();
		String batch = gridModel.getBatch();
		String timelimit = gridModel.getTimelimit();
		// 起始经度
		double lonStart = gridModel.getLonStart();
		// 起始纬度
		double latStart = gridModel.getLatStart();
		// 终止经度
		double lonEnd = gridModel.getLonEnd();
		// 终止纬度
		double latEnd = gridModel.getLatEnd();
		// 纬向格点数
		int xCount = gridModel.getxCount();
		// 经向格点数
		int yCount = gridModel.getyCount();
		// 数据值
		Point[][] points = gridModel.getPoints();
		// 前景图
		BufferedImage bufImg = new BufferedImage(xCount, yCount, BufferedImage.TYPE_INT_ARGB);
		// 获取对应要素的调色板信息
		Palette pal = getPaletteByParameter("/com/webyun/seagrid/common/config/palette/" + productType.toLowerCase() + "_pal.xml");
		// 画笔对象
		Graphics2D g = null;
		// 数据点颜色
		Color clr = null;
		// 数据值
		double data;
		
		// 将图像转正
		if (latStart < latEnd) {
			double tmp = latStart;
			latStart = latEnd;
			latEnd = tmp;
		}
		
		for (int x = 0; x < xCount; x++) {
			for (int y = 0; y < yCount; y++) {
				data = micapsType == 2 ? ((Micaps2Point)points[y][x]).getWindSpeed() : points[y][x].getValue();
				// 通过数据值获取对应颜色分量
				List<Integer> rgba = getRGBA(pal, data, productType.toLowerCase());
				clr = new Color(rgba.get(0), rgba.get(1), rgba.get(2), rgba.get(3));
				// 为对应点绘制颜色
				bufImg.setRGB(x, y, clr.getRGB());
			}
		}
		
		int x = (int) (Math.abs(lonStart - bkgdLonStart) / Math.abs(bkgdLonEnd - bkgdLonStart) * width);
		int y = (int) (Math.abs(bkgdLatStart - latStart) / Math.abs(bkgdLatStart - bkgdLatEnd) * height);
		int w = (int) (Math.abs(lonEnd - lonStart) / Math.abs(bkgdLonEnd - bkgdLonStart) * width);
		int h = (int) (Math.abs(latStart - latEnd) / Math.abs(bkgdLatStart - bkgdLatEnd) * height);
		
		g = bkgd.createGraphics();
		g.drawImage(bufImg, x, y, w, h, null);
		g = finalImg.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, finalImg.getWidth(), finalImg.getHeight());
		g.drawImage(bkgd, left, top, width, height, null);
		
		// 绘制网格线及经纬度标注值
		drawDashLineAndLonLat(g, width, height, bkgdLonStart, bkgdLatStart, bkgdLonEnd, bkgdLatEnd, left, top);
		// 绘制色标
		Color topColor = new Color(153, 0, 51);// 顶端颜色：(rgba="153, 0, 51, 180")
		drawWindColorLabel(g, pal, topColor, left, top, width, height);
		// 绘制标题栏
		g.setColor(Color.BLUE);
		g.setFont(FontUtil.FNT_1);
		// 绘制标题
		String datetime = year + month + day + batch;
		String title = datetime + "." + timelimit + "_10m风预报";
		g.drawString(title, 275, 37);
		// 绘制时间日期
		g.setFont(FontUtil.FNT_2);
		if (datetime.length() == 8) {
			datetime = DatetimeUtil.format(DatetimeUtil.parse(datetime, DatetimeUtil.YYMMDDHH), DatetimeUtil.YYYYMMDDHH);
		}
		long dateMillisecond = DatetimeUtil.parse(datetime, DatetimeUtil.YYYYMMDDHH).getTime();
		String newDatetime = getNewDatetime(dateMillisecond, timelimit);
		g.drawString(datetime, 66, 80);
		g.drawString(timelimit, left + (width - 44)/2, 80);
		g.drawString(newDatetime, left + width - 168, 80);
		
		// 释放资源
		g.dispose();
	}
	
	/**
	 * 绘制EC产品图像的经纬度线及标注值
	 * @param g 画笔
	 * @param width 底图宽度
	 * @param height 底图高度
	 * @param lon_start 底图起始经度值
	 * @param lat_start 底图起始纬度值
	 * @param lon_end 底图终止经度值
	 * @param lat_end 底图终止纬度值
	 * @param left 图像距离背景图的左边界距离
	 * @param top 图像距离背景图的上边界距离
	 */
	private static void drawDashLineAndLonLat(Graphics2D g, int width, int height, double lon_start,
			double lat_start, double lon_end, double lat_end, int left, int top) {
		// 经向单位长度（单位为度）
		int lonUnit;
		// 纬向单位长度（单位为度）
		int latUnit;
		double lonSub = Math.abs(lon_end - lon_start);
		double latSub = Math.abs(lat_start - lat_end);
		
		// 获取经向单位长度
		if (lonSub > 50) {
			lonUnit = 10;
		} else if (30 < lonSub && lonSub <= 50) {
			lonUnit = 5;
		} else if (20 < lonSub && lonSub <= 30) {
			lonUnit = 3;
		} else if (5 < lonSub && lonSub <= 20) {
			lonUnit = 2;
		} else {
			lonUnit = 1;
		}
		
		// 获取纬向单位长度
		if (30 < latSub && latSub <= 60) {
			latUnit = 5;
		} else if (15 < latSub && latSub <= 30) {
			latUnit = 3;
		} else if (5 < latSub && latSub <= 15) {
			latUnit = 2;
		} else {
			latUnit = 1;
		}
		
		// 绘制经纬度网格线和标注值及外边框
		drawDashLineAndLonLat(g, lonUnit, latUnit, width, height, lon_start, lat_start, lon_end, lat_end, left, top);
	}
	
	/**
	 * 绘制虚线和经纬度标注及外边框
	 * @param g 画笔
	 * @param lonUnit 经向单位长度（单位为度）
	 * @param latUnit 纬向单位长度（单位为度）
	 * @param width 底图宽度
	 * @param height 底图高度
	 * @param lon_start 底图起始经度值
	 * @param lat_start 底图起始纬度值
	 * @param lon_end 底图终止经度值
	 * @param lat_end 底图终止纬度值
	 * @param left 图像距离背景图的左边界距离
	 * @param top 图像距离背景图的上边界距离
	 */
	private static void drawDashLineAndLonLat(Graphics2D g, int lonUnit, int latUnit, int width, int height,
			double lon_start, double lat_start, double lon_end, double lat_end, int left, int top) {
		// 将画笔设置为虚线
		Stroke dash = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, new float[]{5, 5}, 0f);
		g.setStroke(dash);
		
		// 计算虚线的起始绘制位置
		double x = ((lon_start % lonUnit) / Math.abs(lon_end - lon_start)) * width + left;
		double y = ((lat_start % latUnit) / Math.abs(lat_start - lat_end)) * height + top;
		// 计算绘制的经纬度起始值
		double latStart = lat_start - lat_start % latUnit;
		double lonStart = lon_start + lon_start % lonUnit;
		// 计算经纬向单位长度所对应像素长度
		double lonLen = (lonUnit / Math.abs(lon_end - lon_start)) * width;
		double latLen = (latUnit / Math.abs(lat_start - lat_end)) * height;
		int i = 0;
		int j = 0;
		
		// 绘制水平虚线及纬度标注
		while (true) {
			// 计算绘制的纵向距离
			double y1 = y + latLen * i;
			// 计算绘制的纬度值
			int lat = (int) (latStart - latUnit * i);
			// 超出边界范围跳出
			if (y1 > (height + top)) {
				break;
			}
			// 绘制纬度标注
			g.setColor(Color.BLACK);
			g.setFont(FontUtil.FNT_3);
			if (lat != 0) {
				String content = lat + "N";
				if (content.length() == 2) {
					g.drawString(content, 25, (float)(y1 + 8));
				} else {
					g.drawString(content, 15, (float)(y1 + 8));
				}
			} else {
				g.drawString("EQ", 25, (float)(y1 + 8));
			}
			// 绘制虚线
			g.setColor(Color.GRAY);
			g.drawLine(left, (int)Math.round(y1), left + width, (int)Math.round(y1));
			i++;
		}

		// 绘制纵向虚线及经度标注
		while (true) {
			// 计算绘制的水平距离
			double x1 = x + lonLen * j;
			// 计算绘制的经度值
			int lon = (int) (lonStart + lonUnit * j);
			// 超出边界范围跳出
			if (x1 > (width + left)) {
				break;
			}
			// 绘制纬度标注
			g.setColor(Color.BLACK);
			g.setFont(FontUtil.FNT_3);
			g.drawString(lon + "E", (float)(x1 - 16), top + height + 23);
			// 绘制虚线
			g.setColor(Color.GRAY);
			g.drawLine((int)Math.round(x1), top, (int)Math.round(x1), height + top);
			j++;
		}
		
		// 绘制外边框
		Stroke line = new BasicStroke(2.0f);
		g.setStroke(line);
		g.setColor(Color.BLACK);
		g.drawRect(left, top, width, height);
	}
	

	
	/**
	 * @Description: 通过格点文件要素获取对应的调色板
	 * @author: songwj
	 * @date: 2017-9-11 下午8:05:44
	 * @param palettePath 调色板配置文件路径
	 * @return
	 */
	private static Palette getPaletteByParameter(String palettePath) {
		Palette pal = new Palette();
		pal = XMLConverter.xmlToObj(pal, Palette.class, palettePath);
		return pal;
	}
	
	/**
	 * @Description: 通过时间日期和时效计算出相加后的时间日期
	 * @author: songwj
	 * @date: 2017-9-11 下午8:08:49
	 * @param dateMillisecond 时间日期毫秒值
	 * @param timelimit 时效，如：012
	 * @return 返回相加后的时间日期
	 */
	private static String getNewDatetime(long dateMillisecond, String timelimit) {
		// 计算该时效所对应的时间毫秒值
		int tmlimit = Integer.parseInt(timelimit);
		long tmlimitMilliscd = tmlimit * 60 * 60 * 1000;
		// 获取相加后的时间日期
		return DatetimeUtil.format(new Date(dateMillisecond + tmlimitMilliscd), DatetimeUtil.YYYY_MM_DD_HH);
	}
	
	/**
	 * 通过调色板配置文件中的数据值获取对应颜色分量
	 * @param pal 调色板对象
	 * @param data 调色板中的数据值
	 * @param productType 产品类型，如：能见度(vis)、AQI预报、PM2.5预报和PM10预报等
	 * @return
	 */
	private static List<Integer> getRGBA(Palette pal, double data, String productType) {
		List<Integer> rgba = new ArrayList<Integer>();
		productType = productType.toLowerCase();
		
		// 获取调色信息
		List<Entry> entries = pal.getEntries();
		if (entries != null) {
			int len = entries.size();
			
			// 通过产品类型和数据值获取对应颜色
			if ("wind".equals(productType)) {
				// 如果为缺报则处理为透明
				if (data == -9999.0 || data == 9999.0) {
					rgba.add(255);// 红
					rgba.add(255);// 绿
					rgba.add(255);// 蓝
					rgba.add(0);// alpha分量，此处为透明
					// 超过最后一个值则处理为深红色(rgba="153, 0, 51, 180")
				} else if (data >= entries.get(len - 1).getValue()) {
					rgba.add(153);// 红
					rgba.add(0);// 绿
					rgba.add(51);// 蓝
					rgba.add(180);// alpha分量，此处为不透明
				} else {
					for (Entry entry : entries) {
						if (data < entry.getValue()) {
							String str = entry.getRgba();
							List<String> strDatas = StringUtils.split(str, StringUtils.DELIM_COMMA);
							rgba = strToInt(strDatas);
							break;
						}
					}
				}
			}
		}
		
		return rgba;
	}
	
	/**
	 * 将字符串集合转为整型集合
	 * @param strDatas
	 * @return
	 */
	private static List<Integer> strToInt(List<String> strDatas){
		List<Integer> intDatas = new ArrayList<Integer>();
		
		for (String strData : strDatas) {
			intDatas.add(Integer.valueOf(strData));
		}
		
		return intDatas;
	}

	/**
	 * @Description: 解析获取每个时效每个风速段的平均偏大、偏小误差或解析获取偏大、正确和偏小区间数据文件
	 * @author: songwj
	 * @date: 2017-9-7 下午6:58:45
	 * @param avgDeviationFilePath
	 * @return
	 */
	private static Map<String, String> file2Map(String filePath) {
		Map<String, String> map = new HashMap<String, String>();
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			// 跳过第一行
			FileOperationUtil.readLine(br);
			
			while (true) {
				String row = FileOperationUtil.readLine(br);
				
				if (row == null)
					break;
				
				List<String> strData = new ArrayList<String>();
				StringUtils.split(strData, row, StringUtils.DOUBLE_TAB);
				String timelimit = strData.get(0);// 时效
				map.put(timelimit + "_0~3", strData.get(1));
				map.put(timelimit + "_3~6", strData.get(2));
				map.put(timelimit + "_6~9", strData.get(3));
				map.put(timelimit + "_9~12", strData.get(4));
				map.put(timelimit + "_12~15", strData.get(5));
				map.put(timelimit + "_15~18", strData.get(6));
				map.put(timelimit + "_18~21", strData.get(7));
				map.put(timelimit + "_21~24", strData.get(8));
				map.put(timelimit + "_24", strData.get(9));
			}
		} catch(Exception e) {
			log.error("平均偏大、偏小误差解析失败：" + e.getMessage());
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return map;
	}
	
	/**
	 * @Description: 订正众数
	 * @author: songwj
	 * @date: 2017-9-8 上午10:36:06
	 * @param avgDeviationMap 偏大、偏小误差
	 * @param rangeMap 偏大、正确、偏小区间
	 * @param speed 风速（待订正的众数）
	 * @param currentRange 当前区间
	 * @param prevRange 上一区间
	 * @param nextRange 下一区间
	 * @return
	 */
	private static double getCorrentMode(Map<String, String> avgDeviationMap, Map<String, String> rangeMap, double speed, String currentRange, String prevRange, String nextRange) {
		double correntMode;
		
		List<String> range = StringUtils.split(rangeMap.get(currentRange), StringUtils.UNDERLINE);
		double rightFloor = Double.parseDouble(range.get(1));// 正确下限
		double rightCeil = Double.parseDouble(range.get(2));// 正确上限
		List<String> avgDeviation = StringUtils.split(avgDeviationMap.get(prevRange), StringUtils.DELIM_COMMA);
		double avgLarge = Double.parseDouble(avgDeviation.get(0));// 小一级平均偏大误差
		avgDeviation = StringUtils.split(avgDeviationMap.get(nextRange), StringUtils.DELIM_COMMA);
		double avgSmall = Double.parseDouble(avgDeviation.get(1));// 大一级平均偏小误差
		// 在偏大区间，订正数据 = 众数 - 小一级偏大误差
		if (speed <= rightFloor)
			if (currentRange.endsWith("_3~6"))
				correntMode = speed;
			else
				correntMode = speed - avgLarge;
			// 正确区间不订正
		else if (speed > rightFloor && speed <= rightCeil)
			correntMode = speed;
			// 在偏小区间，订正数据 = 众数 + 大一级偏小误差
		else
			correntMode = speed + avgSmall;
		
		return correntMode;
	}
	
	/**
	 * @Description: 计算获取指定站点的临近四个点以及这个四个点分别在网格中位置下标
	 * @author: songwj
	 * @date: 2017-8-17 下午8:21:25
	 * @param point
	 * @param gridModel
	 * @param df
	 * @return
	 */
	private static Map<String, List<Point>> getNearFourPointsAndIndex(Point point, GridModel gridModel, DecimalFormat df) {
		Map<String, List<Point>> map = new HashMap<String, List<Point>>();
		
		if (gridModel != null) {
			List<Point> fourPnts = new ArrayList<Point>();
			List<Point> fourIdx = new ArrayList<Point>();
			
			Point[][] points = gridModel.getPoints();
			if(points == null){
				log.error("传入的GridModel为空!!!");
				return null;
			}
			double lon = formatDouble(point.getX(), df);
			double lat = formatDouble(point.getY(), df);
			double lat_start = formatDouble(gridModel.getLatStart(), df);
			double lon_start = formatDouble(gridModel.getLonStart(), df);
			double lat_gap = formatDouble(gridModel.getLatGap(), df);
			double lon_gap = formatDouble(gridModel.getLonGap(), df);
			
			// 获取与该点临近的经纬度坐标
			int x = (int)(formatDouble(formatDouble(Math.abs(lat - lat_start), df) / lat_gap, df));
			int y = (int)(formatDouble(formatDouble(Math.abs(lon - lon_start), df) / lon_gap, df));
			Point p11 = null;
			Point idx11 = null;
			Point p12 = null;
			Point idx12 = null;
			Point p21 = null;
			Point idx21 = null;
			Point p22 = null;
			Point idx22 = null;
			int rowNum = gridModel.getPoints().length;
			int colNum = gridModel.getPoints()[0].length;
			
			//该算法针对起始和终止经纬度在左上角右下角和左下角右上角两种情况都通用
			if(x < (rowNum - 1) && y < (colNum - 1)){
				p11 = points[x][y];//左上
				p12 = points[x][y + 1];//右上
				p21 = points[x + 1][y];//左下
				p22 = points[x + 1][y + 1];//右下
				idx11 = new Point(x, y);
				idx12 = new Point(x, y + 1);
				idx21 = new Point(x + 1, y);
				idx22 = new Point(x + 1, y + 1);
			}else if(x < (rowNum - 1) && y >= (colNum - 1)){
				p11 = points[x][y - 1];//左上
				p12 = points[x][y];//右上
				p21 = points[x + 1][y - 1];//左下
				p22 = points[x + 1][y];//右下
				idx11 = new Point(x, y - 1);
				idx12 = new Point(x, y);
				idx21 = new Point(x + 1, y - 1);
				idx22 = new Point(x + 1, y);
			}else if(x >= (rowNum - 1) && y < (colNum - 1)){
				p11 = points[x - 1][y];//左上
				p12 = points[x - 1][y + 1];//右上
				p21 = points[x][y];//左下
				p22 = points[x][y + 1];//右下
				idx11 = new Point(x - 1, y);
				idx12 = new Point(x - 1, y + 1);
				idx21 = new Point(x, y);
				idx22 = new Point(x, y + 1);
			}else if(x >= (rowNum - 1) && y >= (colNum - 1)){
				p11 = points[x - 1][y - 1];//左上
				p12 = points[x - 1][y];//右上
				p21 = points[x][y - 1];//左下
				p22 = points[x][y];//右下
				idx11 = new Point(x - 1, y - 1);
				idx12 = new Point(x - 1, y);
				idx21 = new Point(x, y - 1);
				idx22 = new Point(x, y);
			}
			
			fourPnts.add(p21);
			fourPnts.add(p11);
			fourPnts.add(p22);
			fourPnts.add(p12);
			fourIdx.add(idx21);
			fourIdx.add(idx11);
			fourIdx.add(idx22);
			fourIdx.add(idx12);
			
			map.put("fourPoints", fourPnts);
			map.put("fourIndex", fourIdx);
		}
		
		return map;
	}

	/**
	 * @Description: 获取指定站点所有成员从003~072时效的每个方向的临近4个点，并插值出指定站点的值
	 * @author: songwj
	 * @date: 2017-8-18 上午11:15:13
	 * @param pointMap
	 * @param values 
	 * @param members
	 * @param timelimitNum
	 * @param timeInterval
	 * @param varNum
	 * @param yCount 
	 * @param xCount 
	 * @param pnt 待插值点
	 * @return
	 */
	private static Map<String, Double> getAllMembersPerDirVals(Map<String, List<Point>> pointMap, float[] values, List<String> members, int timelimitNum, int timeInterval, int varNum, int xCount, int yCount, Point pnt) {
		Map<String, Double> map = new HashMap<String, Double>();
		
		if (pointMap != null && members != null && members.size() > 0) {
			List<Point> points = pointMap.get("fourPoints");
			List<Point> index = pointMap.get("fourIndex");
			
			Point p11 = points.get(0);// 左下
			Point p12 = points.get(1);// 左上
			Point p21 = points.get(2);// 右下
			Point p22 = points.get(3);// 右上
			Point idx11 = index.get(0);// 左下
			Point idx12 = index.get(1);// 左上
			Point idx21 = index.get(2);// 右下
			Point idx22 = index.get(3);// 右上
			// 获取临近四个点在网格的行列下标值（从0起算）
			int x11 = (int) idx11.getX();// 行
			int y11 = (int) idx11.getY();// 列
			int x12 = (int) idx12.getX();// 行
			int y12 = (int) idx12.getY();// 列
			int x21 = (int) idx21.getX();// 行
			int y21 = (int) idx21.getY();// 列
			int x22 = (int) idx22.getX();// 行
			int y22 = (int) idx22.getY();// 列
			
			int idx = 0;
			int len = xCount * yCount;
			// 遍历所有成员
			for (String member : members) {
				// 25个时效，跳过000时效
				for (int i = 0; i < timelimitNum; i++) {
					if (i == 0) {
						idx = idx + len * 2;// 每个时效有U和V两个方向数据
					} else {
						String timelimit = StringUtils.addZero(i * timeInterval, 3);
						// 0 → U, 1 → V
						for (int j = 0; j < varNum; j++) {
							Point pnt11 = new Point(p11.getX(), p11.getY(), values[idx + xCount * x11 + y11]);
							Point pnt12 = new Point(p12.getX(), p12.getY(), values[idx + xCount * x12 + y12]);
							Point pnt21 = new Point(p21.getX(), p21.getY(), values[idx + xCount * x21 + y21]);
							Point pnt22 = new Point(p22.getX(), p22.getY(), values[idx + xCount * x22 + y22]);
							
							// 插值
							double value = calcPointValue(pnt11, pnt12, pnt21, pnt22, pnt, new DecimalFormat("###.00000000"));
							
							// key → c00_003_u
							map.put(member + "_" + timelimit + "_" + (j == 0 ? "u" : "v"), value);
							
							idx = idx + len;
						}
					}
				}
			}
		}
		
		return map;
	}
	
	/**
	 * @Description: 将每个成员相同时效的U和V分量数据合成，获取合成后的站点风速和风向值
	 * @author: songwj
	 * @date: 2017-8-18 下午3:51:33
	 * @param perDirVals
	 * @param members
	 * @param timelimitNum
	 * @param timeInterval
	 * @param varNum
	 * @return
	 */
	private static Map<String, WindSpdAndDirModel> getAllMembersMegerVals(Map<String, Double> perDirVals, List<String> members, int timelimitNum, int timeInterval, int varNum) {
		Map<String, WindSpdAndDirModel> map = new HashMap<String, WindSpdAndDirModel>();
		
		if (perDirVals != null) {
			// 遍历所有成员
			for (String member : members) {
				// 25个时效，跳过000时效
				for (int i = 1; i < timelimitNum; i++) {
					String timelimit = StringUtils.addZero(i * timeInterval, 3);
					double u = perDirVals.get(member + "_" + timelimit + "_" + "u");
					double v = perDirVals.get(member + "_" + timelimit + "_" + "v");
					// 风合成
					WindSpdAndDirModel model = getWindSpeedAndDirection(u, v);
					map.put(member + "_" + timelimit, model);
				}
			}
		}
		
		return map;
	}
	
	/**
	 * @Description: 计算获取每个时效的众数
	 * @author: songwj
	 * @date: 2017-8-18 下午4:40:55
	 * @param modeVals 
	 * @param megerVals
	 * @param members
	 * @param timelimitNum
	 * @param timeInterval
	 * @param stationCode 
	 * @param datetime 
	 * @return
	 */
	private static void getPerTimelimitMode(Map<String, Double> modeVals, Map<String, WindSpdAndDirModel> megerVals, List<String> members, int timelimitNum, int timeInterval, String datetime, String stationCode) {
		if (megerVals != null && members != null && members.size() > 0) {
			for (int i = 1; i < timelimitNum; i++) {
				// 时效
				String timelimit = StringUtils.addZero(i * timeInterval, 3);
				int sum0_3 = 0;
				int sum3_6 = 0;
				int sum6_9 = 0;
				int sum9_12 = 0;
				int sum12_15 = 0;
				int sum15_18 = 0;
				int sum18_21 = 0;
				int sum21_24 = 0;
				int sum24 = 0;
				
				// 计算每个时效的众数
				for (String member : members) {
					// 风速（单位：m/s）
					double speed = megerVals.get(member + "_" + timelimit).getWindSpeed();
					// 判断各个成员的风速所处的区间，统计各个在各个区间的次数
					if (0 <= speed && speed <= 3) {
						sum0_3 += 1;
					} else if (3 < speed && speed <= 6) {
						sum3_6 += 1;
					} else if (6 < speed && speed <= 9) {
						sum6_9 += 1;
					} else if (9 < speed && speed <= 12) {
						sum9_12 += 1;
					} else if (12 < speed && speed <= 15) {
						sum12_15 += 1;
					} else if (15 < speed && speed <= 18) {
						sum15_18 += 1;
					} else if (18 < speed && speed <= 21) {
						sum18_21 += 1;
					} else if (21 < speed && speed <= 24) {
						sum21_24 += 1;
					} else {
						sum24 += 1;
					}
				}
				
				// 存放每个区间的次数
				Map<String, Integer> perTimes = new HashMap<String, Integer>();
				perTimes.put("0_3", sum0_3);
				perTimes.put("3_6", sum3_6);
				perTimes.put("6_9", sum6_9);
				perTimes.put("9_12", sum9_12);
				perTimes.put("12_15", sum12_15);
				perTimes.put("15_18", sum15_18);
				perTimes.put("18_21", sum18_21);
				perTimes.put("21_24", sum21_24);
				perTimes.put("24", sum24);
				// 存放频数最高的区间名和对应值
				String maxRangeName = null;
				int maxRangeNum = 0;
				// 获取频数最高的区间及对应数值
				Set<Map.Entry<String, Integer>> entrySet = perTimes.entrySet();
				for (Map.Entry<String, Integer> me : entrySet) {
					if (me.getValue() > maxRangeNum) {
						maxRangeName = me.getKey();
						maxRangeNum = me.getValue();
					}
				}
				// 众数组下限值
				int L = 0;
				int maxSpeed = 24;// 风速段中的最大值
				if ((maxSpeed + "").equals(maxRangeName)) {
					L = maxSpeed;
				} else {
					L = Integer.parseInt(StringUtils.split(maxRangeName, StringUtils.UNDERLINE).get(0));
				}
				// 计算众数组相连的前一组和后一组频数
				int f1 = 0;
				int f2 = 0;
				int K = 3;// 组距（单位：m/s）
				if (L == 0) {
					f1 = 0;
					f2 = perTimes.get("3_6");
				} else if (L == maxSpeed) {
					f1 = perTimes.get((maxSpeed - 3) + "_" + maxSpeed);
					f2 = 0;
				} else {
					f1 = perTimes.get((L - K) + "_" + L);
					int floor = L + K;
					f2 = perTimes.get((floor == maxSpeed ? (maxSpeed + "") : (L + K) + "_" + (L + K * 2)));
				}
				// 计算获取众数
				double mode = calcMode(L, maxRangeNum, f1, f2, K);
				
				modeVals.put(datetime + "_" + timelimit + "_" + stationCode, mode);
			}
		}
	}
	
	/**
	 * @Description: 计算获取众数
	 * @author: songwj
	 * @date: 2017-8-18 下午6:05:10
	 * @param L 众数组下限值
	 * @param f0  众数组
	 * @param f1  众数组相邻前一组频数
	 * @param f2 众数组相邻后一组频数
	 * @param K 组距
	 * @return
	 */
	private static double calcMode(int L, int f0, int f1, int f2, int K) {
		return L + ((double)(f0 - f1) / ((f0 - f1) + (f0 - f2))) * K;
	}
	
	/**
	 * @Description: 计算获取每个站点每个时效的平均误差和平均绝对误差
	 * @author: songwj
	 * @date: 2017-8-21 下午5:20:23
	 * @param modeVals
	 * @param stationCodes
	 * @param datetime
	 * @param timelimitNum
	 * @param timeInterval
	 * @return
	 */
	private static Map<String, StationErrorModel> getPerStationAndPerTimeErrorData(Map<String, Double> modeVals, List<String> stationCodes, String datetime, int timelimitNum, int timeInterval) {
		Map<String, StationErrorModel> map = new HashMap<String, StationErrorModel>();
		
		if (modeVals != null && modeVals.size() > 0 && stationCodes != null && stationCodes.size() > 0) {
			for (int i = 1; i < timelimitNum; i++) {
				// 计算获取预报所对应的实况时间
				Date date = DatetimeUtil.parse(datetime, DatetimeUtil.YYYYMMDDHH);
				String actualTime = DatetimeUtil.format(new Date(date.getTime() + i * timeInterval * 60 * 60 * 1000), DatetimeUtil.YYYYMMDDHH);
				// 解析实况站点数据文件（MICAPS第一类自动观测站数据）
				String stationFilePath = FileConfigUtil.getProperty("file.meanWindRootPath") + actualTime.substring(0, 6) + "/" + actualTime.substring(2) + ".000";
				
				if (!new File(stationFilePath).exists()) {
					log.error("指定的实况自动观测站数据不存在，指定的数据文件为：" + stationFilePath);
					continue;
				}
				
				// 时效
				String timelimit = StringUtils.addZero(i * timeInterval, 3);
				// key：站点编号，value：风速
				Map<String, Double> windMap = StationFileUtil.micaps1ToWindMap(stationFilePath);
				
				// 计算每个站点每个时效的平均误差和平均绝对误差
				for (String stationCode : stationCodes) {
					String modeValKey = datetime + "_" + timelimit + "_" + stationCode;
					String actualWindSpdKey = StringUtils.addZero(stationCode, 6);
					if (modeVals.containsKey(modeValKey) && windMap.containsKey(actualWindSpdKey)) {
						// 实况风速
						double actualWindSpd = windMap.get(StringUtils.addZero(stationCode, 6));
						// 众数
						double modeVal = modeVals.get(datetime + "_" + timelimit + "_" + stationCode);
						
						// 剔除实况和预报大于40的数据
						if (actualWindSpd <= 40.0 && modeVal <= 40.0) {
							// 平均误差 = 预报数据（众数） - 实况数据
							double avgError = modeVal - actualWindSpd;
							// 平均绝对误差 = |预报数据（众数） - 实况数据|
							double avgAbsError = Math.abs(modeVal - actualWindSpd);
							map.put(datetime + "_" + timelimit + "_" + stationCode, new StationErrorModel(modeVal, actualWindSpd, avgError, avgAbsError));
						} else {
							log.warn(datetime + "_" + timelimit + "_" + stationCode + " >>> 实况风速为：" + actualWindSpd + "，众数值为：" + modeVal);
						}
					}
				}
			}
		}
		
		return map;
	}
	
	/**
	 * @Description: 计算获取每个时效不同风速区间的累加平均误差和平均绝对误差数据
	 * @author: songwj
	 * @date: 2017-8-22 下午6:17:31
	 * @param stationErrModelMap
	 * @param stationCodes
	 * @param datetime
	 * @param timelimitNum
	 * @param timeInterval
	 * @return
	 */
	private static Map<String, ErrorModel> getAccumulationErrorData(Map<String, StationErrorModel> stationErrModelMap, List<String> stationCodes, String datetime, int timelimitNum, int timeInterval) {
		Map<String, ErrorModel> map = new HashMap<String, ErrorModel>();
		
		if (stationErrModelMap != null && stationErrModelMap.size() > 0 && stationCodes != null && stationCodes.size() > 0) {
			// 时效
			for (int i = 1; i < timelimitNum; i++) {
				String timelimit = StringUtils.addZero(i * timeInterval, 3);
				
				// 处在各个风速区间的平均误差和平均绝对误差的分别累加和
				double sumAvgError0_3 = 0;
				double sumAvgAbsError0_3 = 0;
				double sumAvgError3_6 = 0;
				double sumAvgAbsError3_6 = 0;
				double sumAvgError6_9 = 0;
				double sumAvgAbsError6_9 = 0;
				double sumAvgError9_12 = 0;
				double sumAvgAbsError9_12 = 0;
				double sumAvgError12_15 = 0;
				double sumAvgAbsError12_15 = 0;
				double sumAvgError15_18 = 0;
				double sumAvgAbsError15_18 = 0;
				double sumAvgError18_21 = 0;
				double sumAvgAbsError18_21 = 0;
				double sumAvgError21_24 = 0;
				double sumAvgAbsError21_24 = 0;
				double sumAvgError24 = 0;
				double sumAvgAbsError24 = 0;
				// 处在各个风速区间的平均误差和平均绝对误差的总次数和偏大、偏小及正确次数
				int count0_3 = 0;
				int count3_6 = 0;
				int count6_9 = 0;
				int count9_12 = 0;
				int count12_15 = 0;
				int count15_18 = 0;
				int count18_21 = 0;
				int count21_24 = 0;
				int count24 = 0;
				int largeCount0_3 = 0;
				int largeCount3_6 = 0;
				int largeCount6_9 = 0;
				int largeCount9_12 = 0;
				int largeCount12_15 = 0;
				int largeCount15_18 = 0;
				int largeCount18_21 = 0;
				int largeCount21_24 = 0;
				int largeCount24 = 0;
				int smallCount0_3 = 0;
				int smallCount3_6 = 0;
				int smallCount6_9 = 0;
				int smallCount9_12 = 0;
				int smallCount12_15 = 0;
				int smallCount15_18 = 0;
				int smallCount18_21 = 0;
				int smallCount21_24 = 0;
				int smallCount24 = 0;
				int rightCount0_3 = 0;
				int rightCount3_6 = 0;
				int rightCount6_9 = 0;
				int rightCount9_12 = 0;
				int rightCount12_15 = 0;
				int rightCount15_18 = 0;
				int rightCount18_21 = 0;
				int rightCount21_24 = 0;
				int rightCount24 = 0;
				
				// 站点编号
				for (String stationCode : stationCodes) {
					String key = datetime + "_" + timelimit + "_" + stationCode;
					if (stationErrModelMap.containsKey(key)) {
						StationErrorModel model = stationErrModelMap.get(key);
						// 实况风速
						double actualWindSpd = model.getActualWindSpeed();
						// 众数（预报风速）
						double mode = model.getMode();
						// 平均误差
						double avgError = model.getAvgError();
						// 平均绝对误差
						double avgAbsError = model.getAvgAbsError();
						
						if (0 <= actualWindSpd && actualWindSpd <= 3) {
							sumAvgError0_3 += avgError;
							sumAvgAbsError0_3 += avgAbsError;
							count0_3 += 1;
							if (mode > 3)
								largeCount0_3 += 1;
							else
								rightCount0_3 += 1;
						} else if (3 < actualWindSpd && actualWindSpd <= 6) {
							sumAvgError3_6 += avgError;
							sumAvgAbsError3_6 += avgAbsError;
							count3_6 += 1;
							if (mode <= 3)
								smallCount3_6 += 1;
							else if (mode > 6)
								largeCount3_6 += 1;
							else
								rightCount3_6 += 1;
						} else if (6 < actualWindSpd && actualWindSpd <= 9) {
							sumAvgError6_9 += avgError;
							sumAvgAbsError6_9 += avgAbsError;
							count6_9 += 1;
							if (mode <= 6)
								smallCount6_9 += 1;
							else if (mode > 9)
								largeCount6_9 += 1;
							else
								rightCount6_9 += 1;
						} else if (9 < actualWindSpd && actualWindSpd <= 12) {
							sumAvgError9_12 += avgError;
							sumAvgAbsError9_12 += avgAbsError;
							count9_12 += 1;
							if (mode <= 9)
								smallCount9_12 += 1;
							else if (mode > 12)
								largeCount9_12 += 1;
							else
								rightCount9_12 += 1;
						} else if (12 < actualWindSpd && actualWindSpd <= 15) {
							sumAvgError12_15 += avgError;
							sumAvgAbsError12_15 += avgAbsError;
							count12_15 += 1;
							if (mode <= 12)
								smallCount12_15 += 1;
							else if (mode > 15)
								largeCount12_15 += 1;
							else
								rightCount12_15 += 1;
						} else if (15 < actualWindSpd && actualWindSpd <= 18) {
							sumAvgError15_18 += avgError;
							sumAvgAbsError15_18 += avgAbsError;
							count15_18 += 1;
							if (mode <= 15)
								smallCount15_18 += 1;
							else if (mode > 18)
								largeCount15_18 += 1;
							else
								rightCount15_18 += 1;
						} else if (18 < actualWindSpd && actualWindSpd <= 21) {
							sumAvgError18_21 += avgError;
							sumAvgAbsError18_21 += avgAbsError;
							count18_21 += 1;
							if (mode <= 18)
								smallCount18_21 += 1;
							else if (mode > 21)
								largeCount18_21 += 1;
							else
								rightCount18_21 += 1;
						}  else if (21 < actualWindSpd && actualWindSpd <= 24) {
							sumAvgError21_24 += avgError;
							sumAvgAbsError21_24 += avgAbsError;
							count21_24 += 1;
							if (mode <= 21)
								smallCount21_24 += 1;
							else if (mode > 24)
								largeCount21_24 += 1;
							else
								rightCount21_24 += 1;
						} else {
							sumAvgError24 += avgError;
							sumAvgAbsError24 += avgAbsError;
							count24 += 1;
							if (mode <= 24)
								smallCount24 += 1;
							else
								rightCount24 += 1;
						}
					}
				}
				
				map.put(datetime + "_" + timelimit + "_0~3", new ErrorModel(sumAvgError0_3, sumAvgAbsError0_3, smallCount0_3, rightCount0_3, largeCount0_3, count0_3));
				map.put(datetime + "_" + timelimit + "_3~6", new ErrorModel(sumAvgError3_6, sumAvgAbsError3_6, smallCount3_6, rightCount3_6, largeCount3_6, count3_6));
				map.put(datetime + "_" + timelimit + "_6~9", new ErrorModel(sumAvgError6_9, sumAvgAbsError6_9, smallCount6_9, rightCount6_9, largeCount6_9, count6_9));
				map.put(datetime + "_" + timelimit + "_9~12", new ErrorModel(sumAvgError9_12, sumAvgAbsError9_12, smallCount9_12, rightCount9_12, largeCount9_12, count9_12));
				map.put(datetime + "_" + timelimit + "_12~15", new ErrorModel(sumAvgError12_15, sumAvgAbsError12_15, smallCount12_15, rightCount12_15, largeCount12_15, count12_15));
				map.put(datetime + "_" + timelimit + "_15~18", new ErrorModel(sumAvgError15_18, sumAvgAbsError15_18, smallCount15_18, rightCount15_18, largeCount15_18, count15_18));
				map.put(datetime + "_" + timelimit + "_18~21", new ErrorModel(sumAvgError18_21, sumAvgAbsError18_21, smallCount18_21, rightCount18_21, largeCount18_21, count18_21));
				map.put(datetime + "_" + timelimit + "_21~24", new ErrorModel(sumAvgError21_24, sumAvgAbsError21_24, smallCount21_24, rightCount21_24, largeCount21_24, count21_24));
				map.put(datetime + "_" + timelimit + "_24", new ErrorModel(sumAvgError24, sumAvgAbsError24, smallCount24, rightCount24, largeCount24, count24));
			}
		}
		
		return map;
	}
	
	/**
	 * 将二进制数据转为单精度数据值
	 * @param arr
	 * @param start
	 * @return
	 */
	private static float arr2Float(byte[] arr, int start) {
		int cnt = 0;
		byte[] tmp = new byte[4];
		for (int i = start; i < (start + 4); i++) {
			tmp[cnt] = arr[i];
			cnt++;
		}
		int sum = 0;
		int idx = 0;
		for (int shiftBy = 0; shiftBy < 32; shiftBy += 8) {
			sum |= ((long) (tmp[idx] & 0xff)) << shiftBy;
			idx++;
		}
		return Float.intBitsToFloat(sum);
	}
	
	/**
	 * 获取与指定点距离最近点的值
	 * @param p11	左下
	 * @param p12	左上
	 * @param p21	右下
	 * @param p22	右上 
	 * @param p		待计算点
	 * @param df	数据格式化模版	
	 * @return 返回最近点值
	 */
	private static double getNearestPointValue(Point p11,Point p12,Point p21,Point p22, Point p, DecimalFormat df) {
		double value = 0.0;
		
		double x = p.getX();
		double y = p.getY();
		double x11 = p11.getX();
		double y11 = p11.getY();
		double x12 = p12.getX();
		double y12 = p12.getY();
		double x21 = p21.getX();
		double y21 = p21.getY();
		double x22 = p22.getX();
		double y22 = p22.getY();
		
		// 计算左下点与指定点的距离
		double d11 = Math.sqrt((x11 - x) * (x11 - x) + (y11 - y) * (y11 - y));
		// 计算左上点与指定点的距离
		double d12 = Math.sqrt((x12 - x) * (x12 - x) + (y12 - y) * (y12 - y));
		// 计算右下点与指定点的距离
		double d21 = Math.sqrt((x21 - x) * (x21 - x) + (y21 - y) * (y21 - y));
		// 计算右上点与指定点的距离
		double d22 = Math.sqrt((x22 - x) * (x22 - x) + (y22 - y) * (y22 - y));
		
		// 获取最近的距离的点并取该点的值
		if (d11 <= d12 && d11 <= d21 && d11 <= d22) {
			value = p11.getValue();
		} else if (d12 <= d11 && d12 <= d21 && d12 <= d22) {
			value = p12.getValue();
		} else if (d21 <= d11 && d21 <= d12 && d21 <= d22) {
			value = p21.getValue();
		} else {
			value = p22.getValue();
		}
		// 格式化数据值
		value = formatDouble(value, df);
		
		return value;
	}

	/**
	 * 将双精度数据值按指定模版格式化
	 * @param value 双精度值
	 * @param df 格式化模版
	 * @return 格式化后的双精度值
	 */
	private static double formatDouble(double value, DecimalFormat df) {
		return Double.parseDouble(df.format(value));
	}
	
}