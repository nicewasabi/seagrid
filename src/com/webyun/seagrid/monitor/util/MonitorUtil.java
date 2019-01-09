package com.webyun.seagrid.monitor.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.webyun.seagrid.common.util.DateTime;
import com.webyun.seagrid.common.util.DatetimeUtil;


/**
 * @Project: seaGrid
 * @Title: MonitorUtil
 * @Description: 文件监控工具类
 * @author: zhongsb
 * @date: 2017年8月22日
 * @company: webyun
 * @Copyright: Copyright (c) 2014
 * @UpdateUser：
 * @UpdateDescription：
 * @UpdateTime：上午10:57:32
 * @Version :V1.0
 */
public class MonitorUtil {
	
	public static int[] getUnnormalStatus(){
		int[]a = {0,2,3,4};
		return a;
	}
	
	/**
	* @Description: 获取文件状态
	* @Author ：zhongsb
	* @param fileTime
	* @param endTime
	* @param deadLine
	* @return   
	*/
	public static int getStatus(String fileTime, String endTime, String deadLine){
		int status = 3;
		if(compare_date(fileTime, endTime)){
			status = 1;//正常
		}else{
			status = compare_date(deadLine, fileTime)?3:2;
		}
		return status;
	}
	
	/**
	* @Description: 获取文件时间，格式为yyyyMMdd HH:mm
	* @Author ：zhongsb
	* @param file
	* @return   
	*/
	public static String getFileTime(File file) {
		long lastModified = file.lastModified();
		Date date = new Date(lastModified);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm");
		return sdf.format(date);
		
	}
	/**
	* @Description: 获取失效时间
	* @Author ：zhongsb
	* @param deadLine
	* @param isNextDay 失效时间是否是下一天
	* @param file
	* @return   
	*/
	public static String getDeadTime(String deadLine,int isNextDay, File file){
		long lastModified = file.lastModified();
		Date date = new Date(lastModified);
		date = isNextDay==1?DateTime.addDays(date, 1):date;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String format = sdf.format(date);
		String dead = format+" "+deadLine;
		return dead;
	}
	
	/**
	* @Description: 获取迟到时间
	* @Author ：zhongsb
	* @param endTime
	* @param file
	* @return   
	*/
	public static String getEndTime(String endTime,File file){
		long lastModified = file.lastModified();
		Date date = new Date(lastModified);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String format = sdf.format(date);
		String end = format+" "+endTime;
		return end;
	}
	
	/**
	 * 比较两个字符串格式的日期大小
	 * @param date1 比如：20170822 04:00
	 * @param date2
	 * @return 如果date1大，返回false，否则返回true
	 */
	public static boolean compare_date(String date1,String date2){
		boolean flag = false;
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm");
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if(dt1.getTime()>dt2.getTime()){
				flag = false;
			}else {
				flag = true;
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	* @Description: 比较两个日期的大小
	* @Author ：zhongsb
	* @param date1
	* @param date2
	* @return   
	*/
	public boolean compareTo(Date date1, Date date2){
		boolean flag=false;
		if(date1.getTime()>=date2.getTime()){
			flag = false;
		}else {
			flag = true;
		}		
		return flag;
	}
	
	/**
	* @Description: 根据任务表中的路径获取真正的路径
	* @Author ：zhongsb
	* @param filePath
	* @return   
	*/
	public static String replaceYyyyMMdd(String filePath){
		
		String yyyymmdd = filePath.replaceFirst("yyyymmdd", DatetimeUtil.getNextDate(new Date(),-1));
		return yyyymmdd;
	}
	/**
	* @Description: 根据任务表中的路径获取真正的路径
	* @Author ：zhongsb
	* @param filePath 数据库中路径
	* @param businessCode 时次
	* @return   返回真正的路径
	*/
	public static String replaceParams(String filePath, String businessCode){
		
		String yyyymmdd = filePath.replaceFirst("YYYYMMDD", DatetimeUtil.getNextDate(new Date(),-1));
		yyyymmdd = yyyymmdd.replace("HH", businessCode);
		return yyyymmdd;
	}
	
	
	/**
	* @Description: 获取昨天的日期，格式为yyyyMMdd
	* @Author ：zhongsb
	* @return  String 年月日
	*/
	public static String getLastDay(){
		Calendar  cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String format = sdf.format(cal.getTime());
		return format;
		
	}
	
	/**
	* @Description: 获取昨天的日期，格式为yyyyMMdd
	* @Author ：zhongsb
	* @return  String 年月日
	*/
	public static String getToday(){
		Calendar  cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String format = sdf.format(cal.getTime());
		return format;
		
	}
	public static void main(String[] args) {
//		System.out.println(MonitorUtil.replaceYyyyMMdd("/NAFP/ECMWF/CAMS/yyyymmdd/00/"));
		System.out.println(getToday());
	}
	
	
}
