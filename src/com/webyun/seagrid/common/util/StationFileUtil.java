package com.webyun.seagrid.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.webyun.seagrid.common.model.Micaps1StationFileModel;
import com.webyun.seagrid.common.model.Micaps1StationModel;

/**
 * @Project: seaGrid
 * @Title: StationFileUtil
 * @Description: 站点文件解析工具类
 * @author: songwj
 * @date: 2017-8-21 下午5:50:53
 * @company: webyun
 * @Copyright: Copyright (c) 2017 Webyun. All Rights Reserved.
 * @version v1.0
 */
public class StationFileUtil {
	
	private static Logger log = Logger.getLogger(StationFileUtil.class);
	
	/**
	 * @Description: 将Micaps第1类文件解析为站点模型
	 * @author: songwj
	 * @date: 2017-8-21 下午5:51:08
	 * @param stationFilePath 站点文件全路径
	 * @param charset 解码方式，如：utf-8
	 * @return
	 */
	public static Micaps1StationFileModel micaps1ToStationFileModel(String stationFilePath, String charset) {
		Micaps1StationFileModel stationFileModel = new Micaps1StationFileModel();
		BufferedReader br = null;
		
		try {
			if (!new File(stationFilePath).exists()) {
				log.error("指定的文件不存在!!!，该文件为：" + stationFilePath);
				return null;
			}
			
			br = new BufferedReader(new InputStreamReader(new FileInputStream(stationFilePath), charset));
			// 第1行，文件描述
			String row = FileOperationUtil.readLine(br);
			stationFileModel.setDescription(row.trim());// 描述
			// 第2行
			row = FileOperationUtil.readLine(br);
			List<String> strDatas = new ArrayList<String>();
			StringUtils.split(strDatas, row, StringUtils.DELIM_TAB);
			stationFileModel.setYear(strDatas.get(0));// 年
			stationFileModel.setMonth(strDatas.get(1));// 月
			stationFileModel.setDay(strDatas.get(2));// 日
			stationFileModel.setTimeLevel(strDatas.get(3));// 时次
			int stationNum = Integer.parseInt(strDatas.get(4));
			stationFileModel.setStationNum(stationNum);// 站点总数
			// 存放站点信息
			List<Micaps1StationModel> stationModels = new ArrayList<Micaps1StationModel>();
			Micaps1StationModel stationModel = null;
			String regex = "[-+]?\\d+(.\\d+)?";// 匹配数值类型
			int missing = 9999;
			
			while (true) {
				row = FileOperationUtil.readLine(br);
				if (row == null) {
					break;
				}
				strDatas = new ArrayList<String>();
				StringUtils.split(strDatas, row, StringUtils.DELIM_TAB);
				if (strDatas.size() >= 24) {
					stationModel = new Micaps1StationModel();
					
					stationModel.setStationCode(strDatas.get(0));// 站点编号
					String lon = strDatas.get(1);
					stationModel.setLon(lon.matches(regex) ? Double.parseDouble(lon) : missing);// 经度
					String lat = strDatas.get(2);
					stationModel.setLat(lat.matches(regex) ? Double.parseDouble(lat) : missing);// 纬度
					String alt = strDatas.get(3);
					stationModel.setAlt(alt.matches(regex) ? Double.parseDouble(alt) : missing);// 海拔
					String siteLevel = strDatas.get(4);
					stationModel.setSiteLevel(siteLevel.matches(regex) ? Integer.parseInt(siteLevel) : missing);// 站点级别
					String totalCloudAmount = strDatas.get(5);
					stationModel.setTotalCloudAmount(totalCloudAmount.matches(regex) ? Integer.parseInt(totalCloudAmount) : missing);// 总云量
					String windDirection = strDatas.get(6);
					stationModel.setWindDirection(windDirection.matches(regex) ? Double.parseDouble(windDirection) : missing);// 风向
					String windSpeed = strDatas.get(7);
					stationModel.setWindSpeed(windSpeed.matches(regex) ? Double.parseDouble(windSpeed) : missing);// 风速
					String seaLevelPressure = strDatas.get(8);
					stationModel.setSeaLevelPressure(seaLevelPressure.matches(regex) ? Integer.parseInt(seaLevelPressure) : missing);// 海平面气压
					String livePressure3h = strDatas.get(9);
					stationModel.setLivePressure3h(livePressure3h.matches(regex) ? Integer.parseInt(livePressure3h) : missing);// 3小时变压
					String pastWeather1 = strDatas.get(10);
					stationModel.setPastWeather1(pastWeather1.matches(regex) ? Integer.parseInt(pastWeather1) : missing);// 过去天气1
					String pastWeather2 = strDatas.get(11);
					stationModel.setPastWeather2(pastWeather2.matches(regex) ? Integer.parseInt(pastWeather2) : missing);// 过去天气2
					String rainfall6h = strDatas.get(12);
					stationModel.setRainfall6h(rainfall6h.matches(regex) ? Double.parseDouble(rainfall6h) : missing);// 6小时降水
					String lowCloud = strDatas.get(13);
					stationModel.setLowCloud(lowCloud.matches(regex) ? Integer.parseInt(lowCloud) : missing);// 低云状
					String lowCloudAmount = strDatas.get(14);
					stationModel.setLowCloudAmount(lowCloudAmount.matches(regex) ? Integer.parseInt(lowCloudAmount) : missing);// 低云量
					String lowCloudHigh = strDatas.get(15);
					stationModel.setLowCloudHigh(lowCloudHigh.matches(regex) ? Integer.parseInt(lowCloudHigh) : missing);// 低云高
					String dewPoint = strDatas.get(16);
					stationModel.setDewPoint(dewPoint.matches(regex) ? Double.parseDouble(dewPoint) : missing);// 露点
					String vis = strDatas.get(17);
					stationModel.setVis(vis.matches(regex) ? Double.parseDouble(vis) : missing);// 能见度
					String currentWeather = strDatas.get(18);
					stationModel.setCurrentWeather(currentWeather.matches(regex) ? Integer.parseInt(currentWeather) : missing);// 现在天气
					String temperature = strDatas.get(19);
					stationModel.setTemperature(temperature.matches(regex) ? Double.parseDouble(temperature) : missing);// 温度
					String mediumCloud = strDatas.get(20);
					stationModel.setMediumCloud(mediumCloud.matches(regex) ? Integer.parseInt(mediumCloud) : missing);// 中云状
					String highCloud = strDatas.get(21);
					stationModel.setHighCloud(highCloud.matches(regex) ? Integer.parseInt(highCloud) : missing);// 高云状
					String mark1 = strDatas.get(22);
					stationModel.setMark1(mark1.matches(regex) ? Integer.parseInt(mark1) : missing);// 标志1
					String mark2 = strDatas.get(23);
					stationModel.setMark2(mark2.matches(regex) ? Integer.parseInt(mark2) : missing);// 标志2
					
					stationModels.add(stationModel);
				}
			}
			stationFileModel.setStationModels(stationModels);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return stationFileModel;
	}
	
	/**
	 * @Description: 将MICAPS第一类自动观测站数据文件解析转为Map（key：站点编号，value：风速）
	 * @author: songwj
	 * @date: 2017-8-22 上午9:55:30
	 * @param stationFilePath
	 * @return
	 */
	public static Map<String, Double> micaps1ToWindMap(String stationFilePath) {
		Map<String, Double> map = new HashMap<String, Double>();
		BufferedReader br = null;
		
		try {
			if (!new File(stationFilePath).exists()) {
				log.error("指定的文件[" + stationFilePath + "]不存在!!!");
				return null;
			}
			
			if (new File(stationFilePath).length() == 0) {
				log.error("指定的文件[" + stationFilePath + "]内容为空!!!");
				return null;
			}
			
			br = new BufferedReader(new InputStreamReader(new FileInputStream(stationFilePath)));
			// 第1行，文件描述
			FileOperationUtil.readLine(br);
			// 第2行
			FileOperationUtil.readLine(br);
			List<String> strDatas = new ArrayList<String>();
			// 存放站点信息
			String regex = "[-+]?\\d+(.\\d+)?";// 匹配数值类型
			int missing = 9999;
			
			while (true) {
				String row = FileOperationUtil.readLine(br);
				if (row == null) {
					break;
				}
				strDatas = new ArrayList<String>();
				StringUtils.split(strDatas, row, StringUtils.DELIM_TAB);
				if (strDatas.size() >= 24) {
					// 站点编号
					String stationCode = strDatas.get(0);
					// 风速
					String windSpeed = strDatas.get(7);
					double windSpd = windSpeed.matches(regex) ? Double.parseDouble(windSpeed) : missing;
					map.put(stationCode, windSpd);
				}
			}
		} catch(Exception e) {
			log.error("MICAPS第一类站点文件解析失败：" + e.getMessage());
			e.printStackTrace();
		}
		
		return map;
	}
	
}
