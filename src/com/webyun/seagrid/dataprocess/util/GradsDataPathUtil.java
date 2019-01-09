package com.webyun.seagrid.dataprocess.util;

import java.io.File;

import org.apache.log4j.Logger;

import com.webyun.seagrid.common.util.FileConfigUtil;

/**
 * @Project: seaGrid
 * @Title: GradsDataPathUtil
 * @Description: GrADS10米风数据路径获取工具
 * @author: songwj
 * @date: 2017-10-23 下午4:37:22
 * @company: webyun
 * @Copyright: Copyright (c) 2017 Webyun. All Rights Reserved.
 * @version v1.0
 */
public class GradsDataPathUtil {
	
	private static final Logger log = Logger.getLogger(GradsDataPathUtil.class);
	// 10米平均风数据存储根目录
	private static final String ROOT_PATH = FileConfigUtil.getProperty("file.actualDataOutputRootPath");
	// 10米阵风数据存储根目录
	private static final String MAX_WIND_ROOT_PATH = FileConfigUtil.getProperty("file.actualMaxWindDataOutputRootPath");

	/**
	 * @Description: 获取指定条件的10米平均风图像路径
	 * @author: songwj
	 * @date: 2017-10-23 下午4:47:15
	 * @param datetime
	 * @param timelimit
	 * @return
	 */
	public static String get10mMeanWindImagePath(String datetime, String timelimit) {
		try {
			String path = ROOT_PATH + "image/" + datetime + "/" + datetime + "." + timelimit + ".png";
			
			if (new File(path).exists()) {
				return path;
			}
		} catch (Exception e) {
			log.error("10米平均风图像路径获取失败：" + e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * @Description: 获取指定条件的10米平均风所提取的最大风图像路径
	 * @Author: songwj
	 * @Date: 2017-12-4 下午4:08:11
	 * @param datetime 世界时，如：2017120300
	 * @param timelimit 时效
	 * @return
	 */
	public static String get10mMeanMaxWindImagePath(String datetime, String timelimit) {
		try {
			String path = ROOT_PATH + "image/" + datetime + "/max_" + datetime + "." + timelimit + ".png";
			
			if (new File(path).exists()) {
				return path;
			}
		} catch (Exception e) {
			log.error("10米平均风中所对应的最大风图像路径获取失败：" + e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * @Description: 获取指定条件的10米阵风图像路径
	 * @Author: songwj
	 * @Date: 2017-12-4 下午2:27:25
	 * @param datetime 世界时，如：2017120300
	 * @param timelimit 时效
	 * @return
	 */
	public static String get10mMaxWindImagePath(String datetime, String timelimit) {
		try {
			String path = MAX_WIND_ROOT_PATH + "image/" + datetime + "/" + datetime + "." + timelimit + ".png";
			
			if (new File(path).exists()) {
				return path;
			}
		} catch (Exception e) {
			log.error("10米平均风图像路径获取失败：" + e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}

}
