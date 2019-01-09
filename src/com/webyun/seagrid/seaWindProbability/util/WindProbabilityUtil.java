package com.webyun.seagrid.seaWindProbability.util;

import java.io.File;

import org.apache.log4j.Logger;

import com.webyun.seagrid.common.util.FileConfigUtil;

/**
 * @Project: seaGrid
 * @Title: WindProbabilityUtil
 * @Description: 海上8级大风数据路径获取工具
 * @Author: songwj
 * @Date: 2017-12-5 下午7:56:36
 * @Company: webyun
 * @Copyright: Copyright (c) 2017 Webyun. All Rights Reserved.
 * @Version v1.0
 */
public class WindProbabilityUtil {
	
	private static final Logger log = Logger.getLogger(WindProbabilityUtil.class);
	// 海上8级大风数据存储根目录
	private static final String SEA_WIND_ROOT_PATH = FileConfigUtil.getProperty("image.seaWindProbability");
	
	/**
	 * @Description: 获取北太平洋图像路径
	 * @Author: songwj
	 * @Date: 2017-12-5 下午7:56:56
	 * @param datetime 世界时，如：2017120400
	 * @param timelimit 时效
	 * @param windType 风类型：average_dispersion、probability、percent、stamp
	 * @return
	 */
	public static String getNPWindImagePath(String datetime, String timelimit, String windType) {
		try {
			String path = SEA_WIND_ROOT_PATH + "NP/" + windType + "/" + datetime + "/" + datetime + "_" + timelimit + ".png";
			
			if (new File(path).exists()) {
				return path;
			}
		} catch (Exception e) {
			log.error("北太平洋8级大风图像路径获取失败：" + e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * @Description: 获取北印度洋图像路径
	 * @Author: songwj
	 * @Date: 2017-12-5 下午8:37:11
	 * @param datetime 世界时，如：2017120400
	 * @param timelimit 时效
	 * @param windType 风类型：average_dispersion、probability、percent、stamp
	 * @return
	 */
	public static String getNIWindImagePath(String datetime, String timelimit, String windType) {
		try {
			String path = SEA_WIND_ROOT_PATH + "NI/" + windType + "/" + datetime + "/" + datetime + "_" + timelimit + ".png";
			
			if (new File(path).exists()) {
				return path;
			}
		} catch (Exception e) {
			log.error("北印度洋8级大风图像路径获取失败：" + e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}

}
