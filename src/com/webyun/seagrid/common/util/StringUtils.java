package com.webyun.seagrid.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @Project: ems
 * @Title: StringUtils
 * @Description: 字符串处理工具类
 * @author: songwj
 * @date: 2015-12-31 上午10:36:49
 * @company: webyun
 * @Copyright: Copyright (c) 2015-2016 Webyun. All Rights Reserved.
 * @version v1.0
 */
public class StringUtils {
	/**
	 * 以点分割
	 */
	public static final String DOT = "\\.";
	
	/**
	 * 以下划线分割
	 */
	public static final String UNDERLINE = "_";
	
	/**
	 * 以横杆分割
	 */
	public static final String MIDDLELINE = "-";
	
	/**
	 * 以WMF字符分割
	 */
	public static final String WMF = "WMF";
	
	/**
	 * 以tab拆分
	 */
	public static final String DELIM_TAB = "\t ";
	
	/**
	 * 以两个tab拆分
	 */
	public static final String DOUBLE_TAB = "\t\t";
	
	/**
	 * 以逗号拆分
	 */
	public static final String DELIM_COMMA = ",";

	/**
	 * 斜杠
	 */
	public static final String SLASH = "/";
	
	/**
	 * 分号
	 */
	public static final String SEMICOLON = ";";
	
	/**
	 * 空格
	 */
	public static final String SPACE = " ";
	
	/**
	 * 冒号
	 */
	public static final String COLON = ":";
	
	/**
	 * 将字符串按指定分割符进行拆分
	 * @param str 原始字符串
	 * @param delim 分隔符
	 * @return
	 */
	public static List<String> split(String str, String delim){
		List<String> strs = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(str, delim);
		while(st.hasMoreElements()){
			strs.add(((String)st.nextElement()).trim());
		}
		return strs;
	}
	
	/**
	 * 按指定分隔符拆分字符串
	 * @param strData 存放拆分后的字符串数据
	 * @param str 原始字符串
	 * @param delim 分隔符
	 */
	public static void split(List<String> strData, String str, String delim){
		StringTokenizer st = new StringTokenizer(str, delim);
		while(st.hasMoreTokens()){
			String value = st.nextToken();
			strData.add(value.trim());
		}
	}
	
	/**
	 * 判断输入的值的位数是否达到指定长度，若不满足则在前面补零
	 * @param value 原始数据值
	 * @param len 补零后的总长度
	 * @return
	 */
	public static String addZero(int value, int len) {
		StringBuffer sb = new StringBuffer(value + "");
		
		while (sb.length() < len) {
			sb = sb.insert(0, 0);
		}
		
		return sb.toString();
	}
	
	/**
	 * @Description: 判断输入的值的位数是否达到指定长度，若不满足则在前面补零
	 * @author: songwj
	 * @date: 2017-8-22 上午10:20:14
	 * @param value 原始数据
	 * @param len 补零后的总长度
	 * @return
	 */
	public static String addZero(String value, int len) {
		StringBuffer sb = new StringBuffer(value);
		
		while (sb.length() < len) {
			sb = sb.insert(0, 0);
		}
		
		return sb.toString();
	}
	
	/**
	 * @Title: 检测字符串是否为空，值为null或者""则为空
	 * @author: gaos
	 * @date: 2016年11月17日 下午5:17:50
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || "".equals(str);
	}
	
	/**
	 * @Title: 检测字符串是否不为空，值为非null、非""则为非空
	 * @author: gaos
	 * @date: 2016年11月17日 下午5:19:32
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}
	
	/**
	 * @Title: 将字符串数组拼接成字符串，为null或""的元素将会被剔除
	 * @author: gaos
	 * @date: 2016年11月30日 下午3:44:06
	 * @param arr
	 * @return
	 */
	public static String arrayToString(String [] arr) {
		if(arr != null && arr.length > 0) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < arr.length; i++) {
				if(isNotEmpty(arr[i])) {
					sb.append(",").append(arr[i]);
				}
			}
			if(sb.length() > 1) {
				return sb.substring(1);
			}
		}
		return null;
	}
	
	/**
	 * @Title: 将字符串数组拼接成字符串，为null或""的元素将会被剔除
	 * list参数的成员类型不能是引用类型,当然String除外
	 * @author: gaos
	 * @date: 2016年12月22日 下午3:46:00
	 * @param list
	 * @return
	 */
	public static String listToString(List<Object> list) {
		if(list != null && list.size() > 0) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < list.size(); i++) {
				if(list.get(i) != null) {
					sb.append(",").append(list.get(i).toString());
				}
			}
			if(sb.length() > 1) {
				return sb.substring(1);
			}
		}
		return null;
	}
	
}
