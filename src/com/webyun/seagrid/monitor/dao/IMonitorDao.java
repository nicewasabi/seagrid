package com.webyun.seagrid.monitor.dao;

import java.util.List;

import com.webyun.seagrid.common.model.FileMonitorTask;


public interface IMonitorDao {
	
	/**
	* @Description: 查找单个时次的任务
	* @Author ：zhongsb
	* @param businessCode 时次
	* @return   
	*/
	public List<FileMonitorTask> getTaskByBusinessCode(String businessCode);
	
	/**
	* @Description: 获取所有的配置信息
	* @Author ：zhongsb
	* @return  List<FileMonitorTask>
	*/
	public List<FileMonitorTask> getAllMonitorTask();
	
	
	public void updateLog();
	
	/**
	* @Description: 获取满足条件的行数
	* @Author ：zhongsb
	* @param like
	* @return   
	*/
	public int getCount(String like);
	
	/**
	* @Description: 获取配置表分页信息
	* @Author ：zhongsb
	* @param like 文件名或者文件路径关键词
	* @param orderBy2 排序语句
	* @param page 第几页
	* @param rows 每页行数
	* @return   
	*/
	public List<FileMonitorTask> getPageList(String like, String orderBy2,String page, String rows);
	
	/**
	* @Description: 根据任务配置ID删除记录
	* @Author ：zhongsb
	* @param id
	* @return   
	*/
	public int deleteTaskById(String id);
	
	/**
	* @Description: 根据ID查找FileMonitorTask对象
	* @Author ：zhongsb
	* @param id
	* @return   
	*/
	public FileMonitorTask getTaskById(String id);
}
