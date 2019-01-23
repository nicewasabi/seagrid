package com.webyun.seagrid.seaFogInversion.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.webyun.seagrid.common.util.FileConfigUtil;
import com.webyun.seagrid.seaFogFeature.util.SeaFogFeatureUtil;

import antlr.collections.List;
import net.sf.json.JSONObject;

public class SeaFogRegcUtil {

	private static Logger log = Logger.getLogger(SeaFogRegcUtil.class);

	private static final String FYSeafogREGC05_ROOTPATH = FileConfigUtil.getProperty("image.FYSeafogREGC05");

	private static final String FYSeafogREGC05H_ROOT_PATH = FileConfigUtil.getProperty("image.FYSeafogREGC05H");

	public static ArrayList<String> getMinutesList(String datetime, String hour) {
		final String filepathPrefixString = "FY4_" + datetime + "_" + hour;
		ArrayList<String> minutesList = new ArrayList<>();
		String yyyymmString = datetime.substring(0, 6);
		try {
			String dirPath = FYSeafogREGC05_ROOTPATH + "/" + yyyymmString + "/" + datetime;
			File dirFile = new File(dirPath);
			if (dirFile.exists()) {
				File[] files = dirFile.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.startsWith(filepathPrefixString);
					}
				});
				for (File file : files) {
					minutesList.add(file.getName());
				}
				log.info("分钟文件名称======================" + minutesList);
				return minutesList;
			}
		} catch (Exception e) {
			log.error("未来3天站点预报图像路径获取失败：" + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

}
