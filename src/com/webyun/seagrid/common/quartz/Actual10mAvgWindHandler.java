package com.webyun.seagrid.common.quartz;

import java.util.Date;

import com.webyun.seagrid.common.util.DatetimeUtil;
import com.webyun.seagrid.common.util.FileConfigUtil;
import com.webyun.seagrid.common.util.GridFileUtil;

/**
 * @Project: seaGrid
 * @Title: Actual10mAvgWindHandler
 * @Description: 实时10m平均风预报数据处理
 * @author: songwj
 * @date: 2017-10-17 下午4:02:00
 * @company: webyun
 * @Copyright: Copyright (c) 2017 Webyun. All Rights Reserved.
 * @version v1.0
 */
public class Actual10mAvgWindHandler {
	
	// GrADS的10m风实时数据存放根目录
	private static final String GRADS_ROOT_PATH = FileConfigUtil.getProperty("file.actualGradsRootPath");
	// 10m风数据处理后的存放根目录
	private static final String OUTPUT_ROOT_PATH = FileConfigUtil.getProperty("file.actualDataOutputRootPath");
	
	/**
	 * @Description: 00时次10m平均风数据处理（处理的时间以当前系统时间为准，只处理昨天的数据）
	 * @author: songwj
	 * @date: 2017-10-17 下午4:32:55
	 */
	public void windProcess_00() {
		// 获取昨天的日期（以系统当前时间为基准）
		String datetime = DatetimeUtil.format(new Date(new Date().getTime() - 24 * 60 * 60 * 1000), DatetimeUtil.YYYYMMDD);
		// 处理文件路径
		String gradsFilePath = GRADS_ROOT_PATH + datetime + "00/ecmf_short3h_10m_uv-component_of_wind.dat";
		gradsProcess(gradsFilePath);
	}
	
	/**
	 * @Description: 12时次10m平均风数据处理（处理的时间以当前系统时间为准，只处理昨天的数据）
	 * @author: songwj
	 * @date: 2017-10-17 下午5:00:17
	 */
	public void windProcess_12() {
		// 获取昨天的日期（以系统当前时间为基准）
		String datetime = DatetimeUtil.format(new Date(new Date().getTime() - 24 * 60 * 60 * 1000), DatetimeUtil.YYYYMMDD);
		// 处理文件路径
		String gradsFilePath = GRADS_ROOT_PATH + datetime + "12/ecmf_short3h_10m_uv-component_of_wind.dat";
		gradsProcess(gradsFilePath);
	}
	
	/**
	 * @Description: GrADS数据处理
	 * @author: songwj
	 * @date: 2017-10-17 下午5:00:59
	 * @param gradsFilePath
	 */
	private void gradsProcess(String gradsFilePath) {
		// 平均偏大、偏小误差文件存放路径
		String avgDeviationFilePath = OUTPUT_ROOT_PATH + "2014112000_2017033112_avg_deviation.txt";
		// 偏大、正确、偏小区间文件存放路径
		String rangeFilePath = OUTPUT_ROOT_PATH + "2014112000_2017033112_range.txt";
		// 10m风处理
		GridFileUtil.grads10mWindToMicapsProcess(gradsFilePath, avgDeviationFilePath, rangeFilePath, "UTF-8");
	}
	
}
