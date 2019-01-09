package com.webyun.seagrid.common.config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.webyun.seagrid.common.model.BuoyStationInfo;
import com.webyun.seagrid.common.util.FileOperationUtil;
import com.webyun.seagrid.common.util.StringUtils;

/**
 * @Project: seaGrid
 * @Title: BuoyStationInfoConfig
 * @Description: 浮标站点信息配置
 * @Author: songwj
 * @Date: 2017-11-15 下午4:55:09
 * @Company: webyun
 * @Copyright: Copyright (c) 2017 Webyun. All Rights Reserved.
 * @Version v1.0
 */
public class BuoyStationInfoConfig {
	
	private static Logger log = Logger.getLogger(BuoyStationInfoConfig.class);
	// 浮标站点信息
	public static List<BuoyStationInfo> buoyStationInfos = new ArrayList<BuoyStationInfo>();
	
	// 静态加载浮标站点配置信息
	static {
		try {
			InputStream in = BuoyStationInfoConfig.class.getResourceAsStream("/com/webyun/seagrid/common/config/buoyStationInfo.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
			BuoyStationInfo info = null;
			
			while (true) {
				String row = FileOperationUtil.readLine(br);
				
				if (row == null) {
					break;
				}
				
				List<String> split = StringUtils.split(row, StringUtils.DELIM_TAB);
				info = new BuoyStationInfo();
				
				info.setStationCode(split.get(0));// 站点编号
				info.setLon(Double.parseDouble(split.get(2)));// 经度
				info.setLat(Double.parseDouble(split.get(1)));// 纬度
				info.setBuoyName(split.get(3));// 浮标名称
				info.setBuoyType(split.get(4));// 浮标类型
				info.setLevel(Integer.parseInt(split.get(5)));// 浮标等级
				
				buoyStationInfos.add(info);
			}
		} catch (Exception e) {
			log.error("浮标站点配置信息加载失败：" + e.getMessage());
			e.printStackTrace();
		}
	}
	
}
