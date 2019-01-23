package com.webyun.seagrid.seaFogInversion.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.webyun.seagrid.common.util.FileConfigUtil;
import com.webyun.seagrid.seaFogFeature.util.SeaFogFeatureUtil;

import antlr.collections.List;
import net.sf.json.JSONObject;

public class SeaFogOffshoreUtil {

	private static Logger log = Logger.getLogger(SeaFogOffshoreUtil.class);

	private static final String FYSeafogREGC05_ROOTPATH = FileConfigUtil.getProperty("image.FYSeafogREGC05");

	private static final String FYSeafogNHEM15_ROOT_PATH = FileConfigUtil.getProperty("image.FYSeafogNHEM15");

	public static String getImagePath(String datetime, String hour, String step, String minute) {
		
		String dirPathPrefixString="";
		if (step != null && !step.equals("")) {
			switch (step) {
			case "05":
				dirPathPrefixString=FYSeafogREGC05_ROOTPATH;
				break;
			case "15":
				dirPathPrefixString=FYSeafogNHEM15_ROOT_PATH;
				break;
			default:
				break;
			}
		}
		String yyyymmString = datetime.substring(0, 6);
		String filepathPrefixString = "FY4_" + datetime + "_" + hour;
		String filesuffix = "_d02.jpg";
		try {
			String path = dirPathPrefixString + "/" + yyyymmString + "/" + datetime + "/" + filepathPrefixString
					+ minute + filesuffix;
			if (new File(path).exists()) {
				return path;
			}
		} catch (Exception e) {
			log.error("图像路径获取失败：" + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	public static ArrayList<String> getMinutesList(String datetime, String hour, String step) {
		String dirPathPrefixString="";
		if (step != null && !step.equals("")) {
			switch (step) {
			case "05":
				dirPathPrefixString=FYSeafogREGC05_ROOTPATH;
				break;
			case "15":
				dirPathPrefixString=FYSeafogNHEM15_ROOT_PATH;
				break;
			default:
				break;
			}
		}
		final String filepathPrefixString = "FY4_" + datetime + "_" + hour;
		ArrayList<String> minutesList = new ArrayList<>();
		String yyyymmString = datetime.substring(0, 6);
		try {
			String dirPath = dirPathPrefixString + "/" + yyyymmString + "/" + datetime;
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
			log.error("分钟集合获取失败：" + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

}
