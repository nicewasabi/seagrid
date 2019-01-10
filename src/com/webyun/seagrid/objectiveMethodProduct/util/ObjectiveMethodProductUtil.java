/**  

 * Copyright © 2019webyun. All rights reserved.

 *

 * @Title: ObjectiveMethodProductUtil.java

 * @Prject: seaGrid

 * @Package: com.webyun.seagrid.objectiveMethodProduct.util

 * @Description: TODO

 * @author: wasabi  

 * @date: 2019年1月10日 上午10:56:18

 * @version: V1.0  

 */
package com.webyun.seagrid.objectiveMethodProduct.util;

import java.io.File;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.JCERSACipher.PKCS1v1_5Padding_PrivateOnly;

import com.webyun.seagrid.common.util.FileConfigUtil;
import com.webyun.seagrid.seaWindProbability.util.WindProbabilityUtil;

/**
 * 
 * @ClassName: ObjectiveMethodProductUtil
 * 
 * @Description: TODO
 * 
 * @author: wasabi
 * 
 * @date: 2019年1月10日 上午10:56:18
 * 
 * 
 */
public class ObjectiveMethodProductUtil {

	public static final String SEAFOG_VIS_PREFIX = "MAMSSEAFOG";
	public static final String SEAFOG_1000hPaRH_PREFIX = "1000hPaRH";
	public static final String SEAFOG_2mRH_PREFIX = "2mRH";
	public static final String SEAFOG_T1000hPa_T2m_PREFIX = "T1000hPa-T2m";
	public static final String SEAFOG_T2m_Ts_PREFIX = "T2m-Ts";
	public static final String SEAFOG_T925hPa_T1000hPa_PREFIX = "T925hPa-T1000hPa";
	SEAFOG_PREFIX

	private static final Logger log = Logger.getLogger(ObjectiveMethodProductUtil.class);
	// 数据存储根目录
	private static final String ObjectiveMethodProduct_ROOT_PATH = FileConfigUtil
			.getProperty("image.objectiveMethodProduct");

	/**
	 * 
	 * 
	 * @Title:
	 * 
	 * @Description: TODO
	 * 
	 * @param datetime
	 * @param timelimit
	 * @param windType
	 * @return
	 * 
	 * @return: String
	 */
	public static String getSeaFogImagePath(String datetime, String timelimit, String featuretype) {

		String SEAFOG_PREFIX="";
		switch (featuretype) {
		case "Vis":
			SEAFOG_PREFIX=SEAFOG_VIS_PREFIX;
			break;
		case "2mRH":
			SEAFOG_PREFIX=SEAFOG_2mRH_PREFIX;
			break;
		case "1000hPaRH":
			SEAFOG_PREFIX=SEAFOG_1000hPaRH_PREFIX;
			break;
		case "T2m-Ts":
			SEAFOG_PREFIX=SEAFOG_T2m_Ts_PREFIX;
			break;
		case "T925hPa-T1000hPa":
			SEAFOG_PREFIX=SEAFOG_T925hPa_T1000hPa_PREFIX;
			break;
		case "T1000hPa-T2m":
			SEAFOG_PREFIX=SEAFOG_T1000hPa_T2m_PREFIX;
			break;

		default:
			break;
		}
		try {
			String yearPath = datetime.substring(0, 4);

			String path = ObjectiveMethodProduct_ROOT_PATH  + yearPath + "/" + featuretype + "/" + SEAFOG_PREFIX
					+ datetime + timelimit + ".PNG";

			if (new File(path).exists()) {
				return path;
			}
		} catch (Exception e) {
			log.error("海雾产品图像路径获取失败：" + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}


}
