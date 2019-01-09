package com.webyun.seagrid.monitor.service;

import java.io.File;
import java.util.List;

import com.webyun.seagrid.common.model.FileMonitorLog;

public interface IFileMonitorLogService {
	
	/**
	* @Description: 根据文件查找log表中的记录
	* @Author ：zhongsb
	* @param file
	* @return   
	*/
	public List<FileMonitorLog[]> getByFile(File file);
	
	/**
	* @Description: 更新日志状态
	* @Author ：zhongsb
	* @param fileMonitorLog   
	*/
	public void updateFileMonitorLog(FileMonitorLog fileMonitorLog);
	
	
	/**
	* @Description: 插入日志
	* @Author ：zhongsb
	* @param fileMonitorLog   
	*/
	public void insertFileMonitorLog(FileMonitorLog fileMonitorLog);
	
	
	/**
	* @Description: 
	* @Author ：zhongsb
	* @param startTime 开始时间
	* @param endTime 结束时间
	* @param status 文件状态
	* @param orderBy 排序字段
	* @param page 第几页
	* @param rows 每页行数
	* @return   
	*/
	public List<FileMonitorLog> getAllMonitorLog(String startTime, String endTime,  String orderBy);

	List<FileMonitorLog> getPageList(String startTime, String endTime,  String orderBy, String page, String rows);

	/**
	* @Description: 获取最新的日志（未获取成功）
	* @Author ：zhongsb
	* @return   
	*/
	public FileMonitorLog getNewLog();

}
