/**  

 * Copyright © 2019webyun. All rights reserved.

 *

 * @Title: SeaFogFeatureUtil.java

 * @Prject: seaGrid

 * @Package: com.webyun.seagrid.seaFogFeature.util

 * @Description: TODO

 * @author: wasabi  

 * @date: 2019年1月10日 下午5:54:29

 * @version: V1.0  

 */
package com.webyun.seagrid.seaFogFeature.util;

import java.io.File;

import org.apache.log4j.Logger;

import com.webyun.seagrid.common.util.FileConfigUtil;
import com.webyun.seagrid.seaFogFeature.controller.Section3DaysController;

/**

 * @ClassName: SeaFogFeatureUtil

 * @Description: TODO

 * @author: wasabi

 * @date: 2019年1月10日 下午5:54:29


 */
public class SeaFogFeatureUtil {
	
	private static Logger log = Logger.getLogger(SeaFogFeatureUtil.class);
	private static final String SeaFogSection3Days_ROOT_PATH = FileConfigUtil
			.getProperty("image.seaFogSection3Days");
	private static final String SeaFogSection7Days_ROOT_PATH = FileConfigUtil
			.getProperty("image.seaFogSection7Days");
	
	private static final String FeatureCombination_ROOT_PATH = FileConfigUtil
			.getProperty("image.featureCombination");
	
	public static final String SeaFogSection3AND7Days_PREFIX="veti_";

	public static String getSeaFogSection3DaysImagePath(String datetime, String hour, String location) {
		
		try {
			String path = SeaFogSection3Days_ROOT_PATH+ "/" + SeaFogSection3AND7Days_PREFIX + datetime+hour+"_"+location + ".png";
			if (new File(path).exists()) {
				return path;
			}
		} catch (Exception e) {
			log.error("未来3天站点预报图像路径获取失败：" + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}
	
	public static String getSeaFogSection7DaysImagePath(String datetime, String hour, String location) {
		
		
		
		try {
			String path = SeaFogSection7Days_ROOT_PATH  + "/" + SeaFogSection3AND7Days_PREFIX + datetime+hour+"_"+location + ".png";
			if (new File(path).exists()) {
				return path;
			}
		} catch (Exception e) {
			log.error("未来7天站点预报图像路径获取失败：" + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}
	
	
	
	public static String getFeatureCombinationImagePath(String datetime, String timelimit, String feature) {
		String prexifString="";
		switch (feature) {
		case "rh1000_wind10":
			prexifString="rh1000_";
			break;
		case "rh2m_wind10":
			prexifString="rh2m_";
			break;
		case "rh850_wind":
			prexifString="rh850_";
			break;
		case "rh925_wind":
			prexifString="rh925_";
			break;
		case "sst":
			prexifString="sst";
			break;
		case "t1000_t2m":
			prexifString="t1000_t2m_";
			break;
		case "t925_t1000":
			prexifString="t925_1000_";
			break;
		case "ta_sst":
			prexifString="ta_sst";
			break;
		case "ta_td":
			prexifString="ta_td";
			break;
		case "td_sst":
			prexifString="td_sst";
			break;
		default:
			break;
		}
		
		try {
			String path = FeatureCombination_ROOT_PATH  + "/" +feature+"/"+prexifString + datetime+"_"+timelimit + ".png";
			if (new File(path).exists()) {
				return path;
			}
		} catch (Exception e) {
			log.error("要素组合预报图像路径获取失败：" + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	
	

}
