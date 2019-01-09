package com.webyun.seagrid.monitor.dao;

import java.io.File;
import java.util.List;

import com.webyun.seagrid.common.model.FileMonitorLog;

public interface IFileMonitorLogDao {
	
	
	
	
	/**
	* @Description: 获取所有迟到的上一个时次的日志
	* @Author ：zhongsb
	* @param status
	* @param yyyymmdd
	* @return   
	*/
	public List<FileMonitorLog> getLogByStatus(int []status, String yyyymmdd, String businessCode);

	/**
	* @Description: 根据文件查找log表中的记录
	* @Author ：zhongsb
	* @param file
	* @return   
	*/
	List<FileMonitorLog[]> getByFile(File file);

	/**
	* @Description: 更新日志状态
	* @Author ：zhongsb
	* @param fileMonitorLog   
	*/
	void updateFileMonitorLog(FileMonitorLog fileMonitorLog);

	/**
	* @Description: 插入日志
	* @Author ：zhongsb
	* @param fileMonitorLog   
	*/
	void insertFileMonitorLog(FileMonitorLog fileMonitorLog);

	/**
	* @Description: 查询分页信息
	* @Author ：zhongsb
	* @param startTime 开始时间
	* @param endTime 结束时间
	* @param status 文件状态
	* @param orderBy 排序字段
	 * @param rows 第几页
	 * @param page 每页行数
	* @return   
	*/
	List<FileMonitorLog> getAllMonitorLog(String startTime, String endTime,  String orderBy);
	
	List<FileMonitorLog> getPageList(String startTime, String endTime,  String orderBy, String page, String rows);

	/**
	* @Description: 获取最新的非正常的日志记录
	* @Author ：zhongsb
	* @param status
	* @return   
	*/
	public FileMonitorLog getNewLog(int[] status);

}
