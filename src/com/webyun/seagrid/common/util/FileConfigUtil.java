package com.webyun.seagrid.common.util;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @Project: seaGrid
 * @Title: FileConfigUtil
 * @Description: 配置文件路径获取工具
 * @author: songwj
 * @date: 2017-8-23 上午10:35:51
 * @company: webyun
 * @Copyright: Copyright (c) 2017 Webyun. All Rights Reserved.
 * @version v1.0
 */
public class FileConfigUtil {
	
	private static Logger log = Logger.getLogger(FileConfigUtil.class);
	
	private static Properties prop = new Properties();
	
	static {
		try {
			// 静态加载初始配置文件
			prop.load(FileConfigUtil.class.getClassLoader().getResourceAsStream("config.properties"));
		} catch (IOException e) {
			log.error("初始路径配置文件加载失败：" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description: 获取指定配置
	 * @author: songwj
	 * @date: 2017-8-23 上午10:45:57
	 * @param key 键
	 * @return
	 */
	public static String getProperty(String key) {
		return prop.getProperty(key);
	}
}
