package com.webyun.seagrid.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @Project: seaGrid
 * @Title: DatetimeUtil
 * @Description: 日期时间处理工具类
 * @author: songwj
 * @date: 2017-8-21 下午4:24:22
 * @company: webyun
 * @Copyright: Copyright (c) 2017 Webyun. All Rights Reserved.
 * @version v1.0
 */
public class DatetimeUtil {
	
	private static Logger log = Logger.getLogger(DatetimeUtil.class);
	
	/**
	 * 日期模版：2016
	 */
	public static final String YYYY = "yyyy";
	
	/**
	 * 日期模版：01
	 */
	public static final String MM = "MM";
	
	/**
	 * 日期模版：3（时）
	 */
	public static final String H = "H";
	
	/**
	 * 日期模版：毫秒值
	 */
	public static final String SSS = "SSS";
	
	/**
	 * 日期模版：201601
	 */
	public static final String YYYYMM = "yyyyMM";
	
	/**
	 * 日期模版：2016-01
	 */
	public static final String YYYY_MM = "yyyy-MM";
	
	/**
	 * 日期模版：0105
	 */
	public static final String MMDD = "MMdd";
	
	/**
	 * 日期模版：0512
	 */
	public static final String DDHH = "ddHH";
	
	/**
	 * 日期模版：010512
	 */
	public static final String MMDDHH = "MMddHH";
	
	/**
	 * 日期模版：120000
	 */
	public static final String HHMMSS = "HHmmss";
	
	/**
	 * 日期模版：20160105
	 */
	public static final String YYYYMMDD = "yyyyMMdd";
	
	/**
	 * 日期模版：2016-01-05
	 */
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	
	/**
	 * 日期模版：160105
	 */
	public static final String YYMMDD = "yyMMdd";
	
	/**
	 * 日期模版：16010512
	 */
	public static final String YYMMDDHH = "yyMMddHH";
	
	/**
	 * 日期模版：2016010512
	 */
	public static final String YYYYMMDDHH = "yyyyMMddHH";

	/**
	 * 日期模版：2016:01:05:12
	 */
	public static final String YYYY_MM_DD_HH = "yyyy:MM:dd:HH";

	/**
	 * 日期模版：2016-01-05 12:08:00
	 */
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 日期模版：2016-01-05_120800
	 */
	public static final String YYYY_MM_DD_HHMMSS = "yyyy-MM-dd_HHmmss";
	
	/**
	 * 日期模版：20160105120800
	 */
	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	
	/**
	 * 日期模版：20160105120800000
	 */
	public static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";

	private static SimpleDateFormat sdf = null;
	
	/**
	 * 将字符串转为日期格式
	 * @param datetime 字符串日期，如：20160126
	 * @param pattern
	 * @return
	 */
	public static Date parse(String datetime, String pattern){
		Date date = null;
		
		try {
			sdf = new SimpleDateFormat(pattern);
			date = sdf.parse(datetime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return date;
	}
	
	/**
	 * 将日期按指定模版转为字符串
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date, String pattern) {
		return new SimpleDateFormat(pattern).format(date);
	}
	
	/**
	 * @Title: 获得某个日期之后的日期
	 * @author: gaos
	 * @date: 2016年12月20日 下午3:26:31
	 * @param date 原始日期
	 * @param days 天数差，正值则往后推[days]天，负值则往前推[days]天
	 * @return yyyyMMdd
	 */
	public static String getNextDate(Date date, int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, days);
		return format(c.getTime(), YYYYMMDD);
	}
	
	/**
	 * 获取指定月份的最后一天
	 * @param datetime 时间，如：201606 
	 * @return
	 */
	public static int getLastDayOfMonth(String datetime) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parse(datetime, YYYYMM));
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 根据起止时间及小时间隔获取对应时间序列
	 * @param timeStart 起始时间，如格式：16120108
	 * @param timeEnd 终止时间，如格式：16120120
	 * @param datePattern 日期模版
	 * @param hourInterval 小时间隔，如：6（6小时间隔）
	 * @return
	 */
	public static List<String> getTimeSequence(String timeStart, String timeEnd, String datePattern, int hourInterval) {
		List<String> tmseq = new ArrayList<String>();
		
		if (hourInterval <= 0) {
			log.error("时间间隔必须大于0");
			return tmseq;
		}
		
		if (timeStart.equals(timeEnd)) {
			Date dateStart = DatetimeUtil.parse(timeStart, datePattern);
			tmseq.add(DatetimeUtil.format(dateStart, datePattern));
		} else {
			long msEnd = DatetimeUtil.parse(timeEnd, datePattern).getTime();
			long msStart = DatetimeUtil.parse(timeStart, datePattern).getTime();
			while (msStart <= msEnd) {
				tmseq.add(DatetimeUtil.format(new Date(msStart), datePattern));
				msStart += hourInterval * 60 * 60 * 1000;
			}
		}
		
		return tmseq;
	}
	
	/**
	 * 根据指定的时间间隔和起止时间获取对应时间序列（时次或时效序列）
	 * @param timeInterval 时间间隔
	 * @param startVal 起始时间
	 * @param endVal 终止时间
	 * @param formatLen 格式化后的长度，（时次为2，时效为3）
	 * @return
	 */
	public static List<String> getTimeList(int timeInterval, int startVal, int endVal, int formatLen) {
		List<String> timeLists = new ArrayList<String>();
		
		if (startVal > endVal) {
			log.error("起始值不超过终止值，此处的起始值为：" + startVal + "，终止值为：" + endVal);
			return null;
		}
		
		int times = (endVal - startVal)/timeInterval + 1;
		for(int i = 0; i < times; i++){
			int startValue = i * timeInterval + startVal;
			timeLists.add(StringUtils.addZero(startValue, formatLen));
		}
		
		return timeLists;
	}

	/**
	 * @Description: 世界时转北京时
	 * @author: songwj
	 * @date: 2017-4-25 下午6:15:13
	 * @param datetime 世界时，如：2017042500
	 * @param pattern 转换日期模版
	 * @return
	 */
	public static String universalTime2beijingTime(String datetime, String pattern) {
		// 世界时加8为北京时
		long ms = parse(datetime, pattern).getTime() + 8 * 60 * 60 * 1000;
		return format(new Date(ms), pattern);
	}
	
	/**
	 * @Description: 世界时转北京时
	 * @Author: songwj
	 * @Date: 2017-12-7 下午5:24:48
	 * @param datetime 世界时，如：2017042500
	 * @param fromPattern 原始日期解析模版
	 * @param toPattern 输出的日期模版
	 * @return
	 */
	public static String universalTime2beijingTime(String datetime, String fromPattern, String toPattern) {
		// 世界时加8为北京时
		long ms = parse(datetime, fromPattern).getTime() + 8 * 60 * 60 * 1000;
		return format(new Date(ms), toPattern);
	}
	
	/**
	 * @Description: 北京时转世界时
	 * @author: songwj
	 * @date: 2017-9-22 下午4:53:19
	 * @param datetime 北京时，如：2017042508
	 * @param pattern 转换日期模版
	 * @return
	 */
	public static String beijingTime2universalTime(String datetime, String pattern) {
		// 北京时减8为世界时
		long ms = parse(datetime, pattern).getTime() - 8 * 60 * 60 * 1000;
		return format(new Date(ms), pattern);
	}
	
}