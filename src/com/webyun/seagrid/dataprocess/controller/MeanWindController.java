package com.webyun.seagrid.dataprocess.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.webyun.seagrid.common.config.BuoyStationInfoConfig;
import com.webyun.seagrid.common.model.BuoyStationInfo;
import com.webyun.seagrid.common.model.WindSpdAndDirModel;
import com.webyun.seagrid.common.util.DatetimeUtil;
import com.webyun.seagrid.common.util.FileConfigUtil;
import com.webyun.seagrid.common.util.FileOperationUtil;
import com.webyun.seagrid.common.util.GraphUtil;
import com.webyun.seagrid.common.util.StringUtils;
import com.webyun.seagrid.dataprocess.util.GradsDataPathUtil;

/**
 * @Project: seaGrid
 * @Title: MeanWindController
 * @Description: GrADS10米平均风数据处理展示控制器
 * @author: songwj
 * @date: 2017-9-22 下午3:02:50
 * @company: webyun
 * @Copyright: Copyright (c) 2017 Webyun. All Rights Reserved.
 * @version v1.0
 */
@Controller
@RequestMapping("/meanWind")
public class MeanWindController {
	
	private Logger log = Logger.getLogger(MeanWindController.class);
	// 10米平均风数据存放的根目录
	private String rootPath = FileConfigUtil.getProperty("file.actualDataOutputRootPath");
	
	/**
	 * @Description: 进入风速风向数据展示主界面
	 * @author: songwj
	 * @date: 2017-9-22 下午3:06:16
	 * @return
	 */
	@RequestMapping("/main.do")
	public String toMain() {
		return "/dataProcess/meanWind";
	}
	
	/**
	 * @Description: 获取浮标站点配置信息
	 * @Author: songwj
	 * @Date: 2017-11-15 下午7:54:27
	 * @return
	 */
	@RequestMapping("/getBuoyStationInfos.do")
	@ResponseBody
	public List<BuoyStationInfo> getBuoyStationInfos() {
		return BuoyStationInfoConfig.buoyStationInfos;
	}
	
	/**
	 * @Description: 获取指定时间指定站点风速风向数据
	 * @author: songwj
	 * @date: 2017-9-22 下午5:11:05
	 * @param datetime 北京时
	 * @param stationCode 站点编号
	 * @return
	 */
	@RequestMapping("/getWindSpdAndirData.do")
	@ResponseBody
	public Map<String, Object> getWindSpdAndirData(String datetime, String stationCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		BufferedReader br = null;
		
		try {
			// 存放横坐标数据
			List<String> xLabs = new ArrayList<String>();
			// 存放风速风向数据
			List<WindSpdAndDirModel> yDatas = new ArrayList<WindSpdAndDirModel>();
			// 存放阵风数据
			List<Double> maxWindDatas = new ArrayList<Double>();
			// 北京时转世界时
			datetime = DatetimeUtil.beijingTime2universalTime(datetime, DatetimeUtil.YYYYMMDDHH);
			String filePath = FileConfigUtil.getProperty("file.actualDataOutputRootPath") + "stationData/" + datetime + "/" + stationCode + ".txt";
			String maxWindfilePath = FileConfigUtil.getProperty("file.actualMaxWindDataOutputRootPath") + "stationData/" + datetime + "/" + stationCode + ".txt";
			
			if (!new File(filePath).exists()) {
				log.error("指定的平均风站点数据文件[" + filePath + "]不存在!!!");
				return map;
			}
			
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			// 获取指定站点数据
			List<String> strData = null;
			
			while (true) {
				String row = FileOperationUtil.readLine(br);
				
				if (row == null) {
					break;
				}
				
				strData = new ArrayList<String>();
				StringUtils.split(strData, row, StringUtils.DELIM_TAB);
				xLabs.add(strData.get(0));
				yDatas.add(new WindSpdAndDirModel(Double.parseDouble(strData.get(1)), Double.parseDouble(strData.get(2))));
			}
			
			if (new File(maxWindfilePath).exists()) {
				// 所有阵风风速
				List<Double> maxWindSpeeds = new ArrayList<Double>();
				br = new BufferedReader(new InputStreamReader(new FileInputStream(maxWindfilePath)));
				
				while (true) {
					String row = FileOperationUtil.readLine(br);
					
					if (row == null) {
						break;
					}
					
					strData = new ArrayList<String>();
					StringUtils.split(strData, row, StringUtils.DELIM_TAB);
					maxWindSpeeds.add(Double.parseDouble(strData.get(1)));
				}
				
				// 阵风初始化赋值
				for (int i = 0; i < yDatas.size(); i++) {
					maxWindDatas.add(null);
				}
				
				double maxWind = 0;
				int index = 0;
				// 找出平均风中前4个风速最大值的下标
				for (int i = 0; i < 4; i++) {
					double speed = yDatas.get(i).getWindSpeed();
					if (speed > maxWind) {
						maxWind = speed;
						index = i;
					}
				}
				// 将每个时段的阵风放在该时段平均风最大值上
				maxWindDatas.set(index, maxWindSpeeds.get(0));
				
				// 舍弃最后的078时效
				for (int i = 1, span = 4; i < maxWindSpeeds.size() - 1; i++, span+=6) {
					maxWind = 0;
					index = span;
					
					for (int j = span; j < span + 6; j++) {
						double speed = yDatas.get(j).getWindSpeed();
						if (speed > maxWind) {
							maxWind = speed;
							index = j;
						}
					}
					// 将每个时段的阵风放在该时段平均风最大值上
					maxWindDatas.set(index, maxWindSpeeds.get(i));
				}
			} else {
				log.error("指定的阵风站点数据文件[" + filePath + "]不存在!!!");
			}
			
			map.put("xLabs", xLabs);
			map.put("yDatas", yDatas);
			map.put("maxWindDatas", maxWindDatas);
		} catch(Exception e) {
			log.error("起报时间为[" + datetime + "]的10米平均风站点[" + stationCode + "]数据获取失败：" + e.getMessage());
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
		
		return map;
	}
	
	/**
	 * @Description: 根据指定角度及风速绘制风向图标
	 * @author: songwj
	 * @date: 2017-9-22 下午4:29:09
	 * @param windSpd 风速
	 * @param windDir 风向值（0~360）
	 * @param response
	 */
	@RequestMapping("/drawArrowByAngle.do")
	public void drawArrowByAngle(double windSpd, double windDir, HttpServletResponse response) {
		response.setContentType("multipart/form-data");
		
		try {
			GraphUtil.drawArrowByAngleAndValue(windSpd, windDir, response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description: 判断指定时间的图像文件是否存在
	 * @author: songwj
	 * @date: 2017-10-23 下午4:48:28
	 * @param datetime yyyyMMddHH，北京时
	 * @param timelimit 时效
	 * @param response
	 * @return
	 */
	@RequestMapping("/validImage.do")
	@ResponseBody
	public Boolean validImage(String datetime, String timelimit, HttpServletResponse response) {
		try {
			// 北京时转世界时
			String universalTime = DatetimeUtil.beijingTime2universalTime(datetime, DatetimeUtil.YYYYMMDDHH);
			// 获取对应日期时间的图片所在路径
			String path = GradsDataPathUtil.get10mMeanWindImagePath(universalTime, timelimit);

			if (path != null) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	/**
	 * @Description: 图像展示
	 * @author: songwj
	 * @date: 2017-10-23 下午4:52:57
	 * @param datetime yyyyMMddHH，北京时
	 * @param timelimit 时效
	 * @param response
	 */
	@RequestMapping("/showImage.do")
	public void showImage(String datetime, String timelimit, HttpServletResponse response){
		response.setContentType("multipart/form-data");
		
		try {
			// 北京时转世界时
			String universalTime = DatetimeUtil.beijingTime2universalTime(datetime, DatetimeUtil.YYYYMMDDHH);
			// 获取对应日期时间的图片所在路径
			String path = GradsDataPathUtil.get10mMeanWindImagePath(universalTime, timelimit);
			
			if (path != null) {
				// 将指定路径下的图片输出到页面
				FileOperationUtil.writeFile(new FileInputStream(path), response.getOutputStream(), 10240);
			}
		} catch (IOException e) {
			log.error("图像获取失败：" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description: 判断指定时间的最大风图像文件是否存在
	 * @Author: songwj
	 * @Date: 2017-12-4 下午4:06:30
	 * @param datetime yyyyMMddHH，北京时
	 * @param timelimit 时效，如：012
	 * @param response
	 * @return
	 */
	@RequestMapping("/validMaxWindImage.do")
	@ResponseBody
	public Boolean validMaxWindImage(String datetime, String timelimit, HttpServletResponse response) {
		try {
			// 北京时转世界时
			String universalTime = DatetimeUtil.beijingTime2universalTime(datetime, DatetimeUtil.YYYYMMDDHH);
			// 获取对应日期时间的图片所在路径
			String path = GradsDataPathUtil.get10mMeanMaxWindImagePath(universalTime, timelimit);

			if (path != null) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	/**
	 * @Description: 最大风图像展示
	 * @Author: songwj
	 * @Date: 2017-12-4 下午4:10:36
	 * @param datetime yyyyMMddHH，北京时
	 * @param timelimit 时效，如：012
	 * @param response
	 */
	@RequestMapping("/showMaxWindImage.do")
	public void showMaxWindImage(String datetime, String timelimit, HttpServletResponse response){
		response.setContentType("multipart/form-data");
		
		try {
			// 北京时转世界时
			String universalTime = DatetimeUtil.beijingTime2universalTime(datetime, DatetimeUtil.YYYYMMDDHH);
			// 获取对应日期时间的图片所在路径
			String path = GradsDataPathUtil.get10mMeanMaxWindImagePath(universalTime, timelimit);
			
			if (path != null) {
				// 将指定路径下的图片输出到页面
				FileOperationUtil.writeFile(new FileInputStream(path), response.getOutputStream(), 10240);
			}
		} catch (IOException e) {
			log.error("图像获取失败：" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description: 获取平均风误差检验数据
	 * @Author: songwj
	 * @Date: 2017-12-7 下午8:23:31
	 * @param datetime 北京时，如：2017120608
	 * @param stationCode 站点编号
	 * @return
	 */
	@RequestMapping("/getMeanWindErrCheckSeriesData.do")
	@ResponseBody
	public Map<String, Object> getMeanWindErrCheckSeriesData(String datetime, String stationCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			// 存放横坐标数据
			List<String> xLabs = new ArrayList<String>();
			// 存放订正前数据
			List<Double> beforeCorrectDatas = new ArrayList<Double>();
			// 存放订正后数据
			List<Double> afterCorrectDatas = new ArrayList<Double>();
			// 存放实况数据
			List<Double> liveDatas = new ArrayList<Double>();
			
			// 北京时转世界时
			String universalTime = DatetimeUtil.beijingTime2universalTime(datetime, DatetimeUtil.YYYYMMDDHH);
			String beforeCorrectRootPath = rootPath + "errorCheck/beforeCorrect/";
			String afterCorrectRootPath = rootPath + "errorCheck/afterCorrect/";
			String liveDataRootPath = rootPath + "errorCheck/liveData/";
			// 存放订正前文件路径
			List<String> beforeCorrectFilePaths = new ArrayList<String>();
			// 存放订正后文件路径
			List<String> afterCorrectFilePaths = new ArrayList<String>();
			
			int fileNums = 6;// 文件总数
			// 获取横坐标及对应文件全路径
			for (int i = fileNums; i > 0; i--) {
				int hour = i * 12;
				String timelimit = StringUtils.addZero(hour, 3);
				xLabs.add(timelimit);
				// 计算获取对应起报文件名
				Date date = DatetimeUtil.parse(universalTime, DatetimeUtil.YYYYMMDDHH);
				String timeStart = DatetimeUtil.format(new Date(date.getTime() - hour * 60 * 60 * 1000), DatetimeUtil.YYYYMMDDHH);
				String fileName = timeStart + "." + timelimit;
				String beforeCorrectFilePath = beforeCorrectRootPath + timeStart + "/" + fileName;
				String afterCorrectFilePath = afterCorrectRootPath + timeStart + "/" + fileName;
				beforeCorrectFilePaths.add(beforeCorrectFilePath);
				afterCorrectFilePaths.add(afterCorrectFilePath);
			}
			
			// 遍历获取订正前站点数据，缺测或缺失的处理为null
			for (String path : beforeCorrectFilePaths) {
				if (new File(path).exists()) {
					double beforeCorrentVal = getStationValue(path, stationCode);
					beforeCorrectDatas.add(beforeCorrentVal == 9999.0 ? null : beforeCorrentVal);
				} else {
					beforeCorrectDatas.add(null);
				}
			}
			
			// 遍历获取订正后站点数据，缺测或缺失的处理为null
			for (String path : afterCorrectFilePaths) {
				if (new File(path).exists()) {
					double afterCorrentVal = getStationValue(path, stationCode);
					afterCorrectDatas.add(afterCorrentVal == 9999.0 ? null : afterCorrentVal);
				} else {
					afterCorrectDatas.add(null);
				}
			}
			
			// 获取实况数据
			String liveDataFilePath = liveDataRootPath + datetime.substring(2) + ".000";
			double liveVal = new File(liveDataFilePath).exists() ? getStationValue(liveDataFilePath, stationCode) : 9999;
			// 将实况值赋值到没有点上，缺测或缺失的处理为null
			for (int i = 0; i < xLabs.size(); i++) {
				liveDatas.add(liveVal == 9999.0 ? null : liveVal);
			}
			
			map.put("xLabs", xLabs);
			map.put("beforeCorrectDatas", beforeCorrectDatas);
			map.put("afterCorrectDatas", afterCorrectDatas);
			map.put("liveDatas", liveDatas);
		} catch(Exception e) {
			log.error("实况时间为[" + datetime + "]的10米平均风站点[" + stationCode + "]误差检验数据获取失败：" + e.getMessage());
			e.printStackTrace();
		}
		
		return map;
	}
	
	/**
	 * @Description: 读取平均风误差检验对应文件获取指定站点数据值
	 * @Author: songwj
	 * @Date: 2017-12-8 下午5:04:18
	 * @param filePath
	 * @param stationCode
	 * @return
	 */
	private double getStationValue(String filePath, String stationCode) {
		BufferedReader br = null;
		double val = 9999;
		
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			
			while (true) {
				String row = FileOperationUtil.readLine(br);
				
				if (row == null) {
					break;
				}
				
				List<String> split = StringUtils.split(row, StringUtils.DOUBLE_TAB);
				String stCode = split.get(0);
				if (stationCode.equals(stCode)) {
					val = Double.parseDouble(split.get(1));
					break;
				}
			}
		} catch (Exception e) {
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
		
		return val;
	}
	
}
