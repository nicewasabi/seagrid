package com.webyun.seagrid.core.area.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.webyun.seagrid.common.model.GridModel;
import com.webyun.seagrid.common.model.Micaps11GridModel;
import com.webyun.seagrid.common.model.Point;
import com.webyun.seagrid.common.util.DatetimeUtil;
import com.webyun.seagrid.common.util.FileConfigUtil;
import com.webyun.seagrid.common.util.GridFileUtil;
import com.webyun.seagrid.common.util.NumberFormatUtil;
import com.webyun.seagrid.common.util.StringUtils;

/**
 * @Project: seaGrid
 * @Title: AreaCheckUtil
 * @Description: 区域检验工具
 * @Author: songwj
 * @Date: 2017-12-6 上午10:15:56
 * @Company: webyun
 * @Copyright: Copyright (c) 2017 Webyun. All Rights Reserved.
 * @Version v1.0
 */
public class AreaCheckUtil {
	
	private static Logger log = Logger.getLogger(AreaCheckUtil.class);
	// EC数据根目录
	private static final String EC_ROOT_PATH = FileConfigUtil.getProperty("file.areaCheckECRootPath");
	// NCEP数据根目录
	private static final String NCEP_ROOT_PATH = FileConfigUtil.getProperty("file.areaCheckNCEPRootPath");
	// 原始网格的经纬度范围
	private static final double SRC_LON_START = 95.0;// 起始经度 
	private static final double SRC_LAT_START = 45.0;// 起始纬度 
	private static final double SRC_LON_END = 142.0;// 终止经度 
	private static final double SRC_LAT_END = -10.0;// 终止纬度 
	
	/**
	 * @Description: 获取指定预报时间指定区域的检验数据
	 * @Author: songwj
	 * @Date: 2017-12-6 上午10:44:05
	 * @param model 模式，ec和ncep
	 * @param datetime 预报时间，如：2017120508
	 * @param lonStart 起始经度
	 * @param latStart 起始纬度
	 * @param lonEnd 终止经度
	 * @param latEnd 终止纬度
	 * @param prodType 产品类型，如：uv10
	 * @return
	 */
	public static Map<String, Object> getAreaCheckSingleDatas(String model, String datetime, double lonStart, double latStart, double lonEnd, double latEnd, String prodType) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			if (!(lonStart >= SRC_LON_START && lonStart <= SRC_LON_END && latStart <= SRC_LAT_START && latStart >= SRC_LAT_END
					&& lonEnd >= SRC_LON_START && lonEnd <= SRC_LON_END && latEnd <= SRC_LAT_START && latEnd >= SRC_LAT_END)) {
				log.error("输入的起止经纬度[" + lonStart + ", " + latStart + ", " + lonEnd + ", " + latEnd + "]超出了原始数据[" + SRC_LON_START + ", " + SRC_LAT_START + ", " + SRC_LON_END + ", " + SRC_LAT_END + "]的边界范围");
				return map;
			}
			
			if (lonStart > lonEnd) {
				log.error("输入的起始经度[" + lonStart + "]超过了终止经度[" + lonEnd + "]");
				return map;
			}
			
			if (latStart < latEnd) {
				log.error("输入的起始纬度[" + latStart + "]小于终止纬度[" + latEnd + "]");
				return map;
			}
			
			// 存放横坐标数据
			List<String> xLabs = new ArrayList<String>();
			// 存放各个时效检验结果数据
			List<Double> yDatas = new ArrayList<Double>();
			// 存放所需的所有文件全路径
			List<String> paths = new ArrayList<String>();
			// 存放所有加载的10米风场收据
			List<Micaps11GridModel> gribModels = new ArrayList<Micaps11GridModel>();
			
			int fileNums = 8;// 文件总数
			// 获取横坐标及对应文件全路径
			for (int i = fileNums; i >= 0; i--) {
				int hour = i * 12;
				String timelimit = StringUtils.addZero(hour, 3);
				// 横轴不要000时效
				if (i != 0)
					xLabs.add(timelimit);
				// 计算获取对应起报文件名
				Date date = DatetimeUtil.parse(datetime, DatetimeUtil.YYYYMMDDHH);
				String timeStart = DatetimeUtil.format(new Date(date.getTime() - hour * 60 * 60 * 1000), DatetimeUtil.YYMMDDHH);
				String fileName = timeStart + "." + timelimit;
				String path = EC_ROOT_PATH + prodType + "/" + fileName;
				if("ncep".equalsIgnoreCase(model)) {
					path = NCEP_ROOT_PATH + prodType + "/" + fileName;
				}
				
				paths.add(path);
			}
			
			// 加载数据
			for (String path : paths) {
				if (new File(path).exists()) {
					gribModels.add(GridFileUtil.micaps11ToGridModel(path, "UTF-8"));
					log.info("文件[" + path + "]加载成功!!!");
				} else {
					log.info("文件[" + path + "]不存在!!!");
				}
			}
			
			if (gribModels != null && gribModels.size() == fileNums + 1) {
				for (int n = 0; n < fileNums; n++) {
					Micaps11GridModel gridModel_1 = gribModels.get(n);
					Micaps11GridModel gridModel_2 = gribModels.get(n + 1);
					// 提取u分量指定区域
					GridModel gridModel_1_u = GridFileUtil.regionExtract(gridModel_1, lonStart, latStart, lonEnd, latEnd);
					GridModel gridModel_2_u = GridFileUtil.regionExtract(gridModel_2, lonStart, latStart, lonEnd, latEnd);
					// 获取v分量指定区域数据
					Point[][] vPoints_1 = gridModel_1.getvPoints();
					Micaps11GridModel vGridModel_1 = gridModel_1;
					vGridModel_1.setPoints(vPoints_1);
					GridModel gridModel_1_v = GridFileUtil.regionExtract(vGridModel_1, lonStart, latStart, lonEnd, latEnd);
					Point[][] vPoints_2 = gridModel_2.getvPoints();
					Micaps11GridModel vGridModel_2 = gridModel_2;
					vGridModel_2.setPoints(vPoints_2);
					GridModel gridModel_2_v = GridFileUtil.regionExtract(vGridModel_2, lonStart, latStart, lonEnd, latEnd);
					// 所提取区域的经纬向格点数
					int xCount = gridModel_1_u.getxCount();
					int yCount = gridModel_1_u.getyCount();
					// 所提取区域的的u和v分量数据
					Point[][] points_1_u = gridModel_1_u.getPoints();
					Point[][] points_1_v = gridModel_1_v.getPoints();
					Point[][] points_2_u = gridModel_2_u.getPoints();
					Point[][] points_2_v = gridModel_2_v.getPoints();
					double sum1 = 0;
					double sum2 = 0;
					double sum3 = 0;
					double sum4 = 0;
					// 合成风场数据获取风速并进行区域检验计算
					for (int i = 0; i < yCount; i++) {
						for (int j = 0; j < xCount; j++) {
							double u1 = points_1_u[i][j].getValue();
							double v1 = points_1_v[i][j].getValue();
							double u2 = points_2_u[i][j].getValue();
							double v2 = points_2_v[i][j].getValue();
							// 前后两点的风速值
							double windSpeed1 = GridFileUtil.getWindSpeedAndDirection(u1, v1).getWindSpeed();
							double windSpeed2 = GridFileUtil.getWindSpeedAndDirection(u2, v2).getWindSpeed();
							// 对应点纬度值
							double lat = points_1_u[i][j].getY();
							//标准化
							// ∑(a-b)*cos(lat)
							sum1 += (windSpeed1 - windSpeed2)*Math.cos(Math.toRadians(lat));
							//∑cos(lat) 正的
							sum2 += Math.cos(Math.toRadians(lat));
							//∑a*cos(lat)
							sum3 += windSpeed1*Math.cos(Math.toRadians(lat));
							//∑b*cos(lat)
							sum4 += windSpeed2*Math.cos(Math.toRadians(lat));
						}
					}
					
					double std1Sum = 0;
					double std2Sum = 0;
					//计算一致性检验结果
					for (int i = 0; i < yCount; i++) {
						for (int j = 0; j < xCount; j++) {
							double u1 = points_1_u[i][j].getValue();
							double v1 = points_1_v[i][j].getValue();
							double u2 = points_2_u[i][j].getValue();
							double v2 = points_2_v[i][j].getValue();
							double lat = points_1_u[i][j].getY();
							// 前后两点的风速值
							double windSpeed1 = GridFileUtil.getWindSpeedAndDirection(u1, v1).getWindSpeed();
							double windSpeed2 = GridFileUtil.getWindSpeedAndDirection(u2, v2).getWindSpeed();
							if(sum2 != 0){
								std1Sum += ((windSpeed1-sum3/sum2)*(windSpeed1-sum3/sum2))*Math.cos(Math.toRadians(lat))/sum2;
								std2Sum += ((windSpeed2-sum4/sum2)*(windSpeed2-sum4/sum2))*Math.cos(Math.toRadians(lat))/sum2;
							}
						}
					}
					//计算最终结果
					double result = 0;
					if((std1Sum+std2Sum) != 0 && sum2 != 0) {
						result = 2*(sum1/sum2)/(Math.sqrt(std1Sum)+Math.sqrt(std2Sum));
					}
					//保留2位小数
					yDatas.add(NumberFormatUtil.numberFormat(result, 2));
				}
			}
			
			map.put("xLabs", xLabs);
			map.put("yDatas", yDatas);
		} catch(Exception e) {
			log.error("区域检验单点序列数据获取失败：" + e.getMessage());
			e.printStackTrace();
		}
		
		return map;
	}
	
}
