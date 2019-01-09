package com.webyun.seagrid.common.util;


import java.util.List;
import java.util.Map;

/**
 * 
 * @Project: ems
 * @Title: Assert
 * @Description: 断言判断工具类
 * @author: zhangx
 * @date: 2015年12月30日 下午2:42:14
 * @company: webyun
 * @Copyright: Copyright (c) 2015
 * @version v1.0
 */
public abstract class Assert {
	
	public static void main(String[] args) {
		
	}

	/**
	 * 判断一般对象是否为null
	 * 
	 * @param	obj		目标对象
	 * @return 	如果目标对象为null，则返回true，否则返回false
	 */
	public static boolean isEmpty(Object obj) {
		return obj == null ? true : false;
	}

	/**
	 * 判断对象不为null
	 * @param 	obj		目标对象
	 * @return	对象为null，则返回false，否则返回true
	 */
	public static boolean isNotEmpty(Object obj) {
		return !Assert.isEmpty(obj);
	}
	
	/**
	 * 判断数组是否为空
	 * @param objects
	 * @return
	 */
	public static boolean isEmpty(Object[] objects) {
		return (objects == null || objects.length == 0) ? true : false;
	}
	
	/**
	 * 判断数组不为null
	 * @param 	obj		目标对象
	 * @return	对象为null，则返回false，否则返回true
	 */
	public static boolean isNotEmpty(Object[] objects) {
		return !Assert.isEmpty(objects);
	}
	
	/**
	 * 判断字符串是否为null，或者空字符串
	 * @param	str		目标字符串
	 * @return 	如果字符串为null，或空字符串，则返回true，false返回false
	 */
	public static boolean isEmpty(String str) {
		return (str == null || str.length() == 0) ? true : false;
	}

	/**
	 * 判断字符串不为null与空字符串
	 * @param 	str		字符串对象
	 * @return	字符串为null或为空字符串，则返回false，否则返回true
	 */
	public static boolean isNotEmpty(String str) {
		return !Assert.isEmpty(str);
	}

	/**
	 * 判断集合是否为null，或空集合
	 * @param 	list 	目标对象
	 * @return 	如果集合对象为null，或者为空集合，则返回true，否则返回false
	 */
	public static boolean isEmpty(List<?> list) {
		return (list != null && list.size() > 0) ? false : true;
	}

	/**
	 * 判断集合对象不为null且不为空集合
	 * @param 	list	目标对象
	 * @return	集合为null，或为空集合，则返回false，否则返回true
	 */
	public static boolean isNotEmpty(List<?> list) {
		return !Assert.isEmpty(list);
	}

	/**
	 * 判断Map对象是否为null，或空对象
	 * @param 	map 	目标对象
	 * @return 	如果Map为null，或者为空，则返回true，否则返回false
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return (map != null && map.size() > 0) ? false : true;
	}

	/**
	 * Map对象不为null且不为空
	 * @param 	map		目标对象
	 * @return	Map对象为null或空，则返回false，否则返回true
	 */
	public static boolean isNotEmpty(Map<?, ?> map) {
		return !Assert.isEmpty(map);
	}
}
