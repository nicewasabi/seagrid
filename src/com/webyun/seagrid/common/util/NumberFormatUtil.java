package com.webyun.seagrid.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @Project: seaGrid
 * @Title: NumberFormatUtil
 * @Description: 数值格式化工具类
 * @Author: songwj
 * @Date: 2017-12-6 下午7:56:23
 * @Company: webyun
 * @Copyright: Copyright (c) 2017 Webyun. All Rights Reserved.
 * @Version v1.0
 */
public class NumberFormatUtil {
	
	/**
	 * @Description: 将数值保留指定小数位
	 * @Author: songwj
	 * @Date: 2017-10-27 下午3:24:06
	 * @param num 原始数值
	 * @param scale 小数点后保留位数
	 * @return
	 */
	public static double numberFormat(double num, int scale) {
		BigDecimal decimal = new BigDecimal(num + "");// 将传入的double值转为字符串，防止出现2.555保留两位小数后变为2.55
		return decimal.setScale(scale, RoundingMode.HALF_UP).doubleValue();
	}
	
}
