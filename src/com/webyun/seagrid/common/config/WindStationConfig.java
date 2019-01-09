package com.webyun.seagrid.common.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @Project: seaGrid
 * @Title: WindStationConfig
 * @Description: 10米风站点配置
 * @author: songwj
 * @date: 2017-8-21 上午11:28:01
 * @company: webyun
 * @Copyright: Copyright (c) 2017 Webyun. All Rights Reserved.
 * @version v1.0
 */
public class WindStationConfig {
	// 存储10m风所有站点数据信息
	public static Map<String, String> windStationConfig = new HashMap<String, String>();
	
	static {
		// key：站点编号，value：纬度，经度
		windStationConfig.put("766080", "40.52, 121.98");
		windStationConfig.put("765070", "40.94, 121.47");
		windStationConfig.put("765069", "40.79, 121.06");
		windStationConfig.put("762215", "39.73, 121.56");
		windStationConfig.put("762245", "40.01, 121.89");
		windStationConfig.put("762239", "39.42, 121.38");
		windStationConfig.put("54661", "38.7, 121.14");
		windStationConfig.put("54660", "38.82, 121.23");
		windStationConfig.put("762128", "39.69, 123.2");
		windStationConfig.put("54579", "39.27, 122.58");
		windStationConfig.put("762506", "38.95, 122.88");
		windStationConfig.put("54544", "39.57, 119.27");
		windStationConfig.put("54449", "39.85, 119.52");
		windStationConfig.put("54647", "39.17, 118.88");
		windStationConfig.put("54641", "38.85, 118.55");
		windStationConfig.put("54538", "39.18, 118.75");
		windStationConfig.put("54720", "38.45, 117.7");
		windStationConfig.put("54646", "38.45, 118.42");
		windStationConfig.put("54721", "38.32, 117.85");
		windStationConfig.put("54630", "38.93, 117.92");
		windStationConfig.put("683118", "37.68, 119.11");
		windStationConfig.put("54741", "38.22, 118.81");
		windStationConfig.put("682002", "37.25, 119.19");
		windStationConfig.put("54751", "37.94, 120.73");
		windStationConfig.put("54750", "37.7, 121.2");
		windStationConfig.put("681083", "38.3, 120.75");
		windStationConfig.put("54772", "37.5, 122.55");
		windStationConfig.put("681104", "37.56, 121.51");
		windStationConfig.put("54776", "37.4, 122.7");
		windStationConfig.put("680053", "37.04, 122.57");
		windStationConfig.put("680046", "37.19, 122.62");
		windStationConfig.put("99998", "36.2, 120.29");
		windStationConfig.put("680036", "36.72, 121.62");
		windStationConfig.put("54946", "35.42, 119.59");
		windStationConfig.put("58041", "34.78, 119.44");
		windStationConfig.put("778005", "34.53, 119.87");
		windStationConfig.put("778008", "33.3, 120.81");
		windStationConfig.put("778003", "32.53, 121.42");
		windStationConfig.put("778009", "32.12, 121.52");
		windStationConfig.put("778023", "31.69, 121.9");
		windStationConfig.put("58474", "30.63, 122.05");
		windStationConfig.put("95013", "31.1, 122.53");
		windStationConfig.put("58573", "29.75, 122.75");
		windStationConfig.put("58472", "30.73, 122.45");
		windStationConfig.put("58469", "30.32, 121.93");
		windStationConfig.put("752927", "28.89, 122.26");
		windStationConfig.put("752926", "29.43, 122.19");
		windStationConfig.put("58666", "28.45, 121.9");
		windStationConfig.put("58768", "27.54, 121.39");
		windStationConfig.put("753007", "27.62, 121.2");
		windStationConfig.put("753255", "27.42, 121.07");
		windStationConfig.put("58767", "27.01, 121");
		windStationConfig.put("703520", "26.94, 120.38");
		windStationConfig.put("58855", "26.38, 119.95");
		windStationConfig.put("58951", "25.5, 120.31");
		windStationConfig.put("58959", "25.21, 119.52");
		windStationConfig.put("59133", "24.89, 118.92");
		windStationConfig.put("59334", "23.64, 118.2");
		windStationConfig.put("705612", "24.52, 118.56");
		windStationConfig.put("706040", "23.72, 117.58");
		windStationConfig.put("59324", "23.43, 117.02");
		windStationConfig.put("712904", "22.96, 116.31");
		windStationConfig.put("59506", "22.61, 115.56");
		windStationConfig.put("711811", "22.66, 115.56");
		windStationConfig.put("711808", "22.69, 115.42");
		windStationConfig.put("711209", "22.24, 113.59");
		windStationConfig.put("59479", "22.1, 114.02");
		windStationConfig.put("712304", "21.57, 111.88");
		windStationConfig.put("59673", "21.73, 112.77");
		windStationConfig.put("712186", "21.9, 112.97");
		windStationConfig.put("59765", "20.52, 111.48");
		windStationConfig.put("59667", "21.37, 111.17");
		windStationConfig.put("712442", "20.26, 109.93");
		windStationConfig.put("712477", "21.03, 109.71");
		windStationConfig.put("712472", "21.27, 109.77");
		windStationConfig.put("771023", "20.06, 110.42");
		windStationConfig.put("771001", "20.04, 110.15");
		windStationConfig.put("771175", "19.79, 109.14");
		windStationConfig.put("59838", "19.1, 108.62");
		windStationConfig.put("771663", "19.49, 108.91");
		windStationConfig.put("771071", "18.24, 109.36");
		windStationConfig.put("771072", "18.32, 108.95");
		windStationConfig.put("771328", "19.98, 111.26");
		windStationConfig.put("771372", "18.64, 110.49");
		windStationConfig.put("789080", "20.91, 109.22");
		windStationConfig.put("789560", "21.39, 108.21");
		windStationConfig.put("59647", "21.03, 109.1");
	}
}
