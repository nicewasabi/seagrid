package com.webyun.seagrid.monitor.service;

import java.io.File;
import java.util.List;

import com.webyun.seagrid.common.model.FileMonitorTask;


/**
 * @Project: seaGrid
 * @Title: IMonitorService
 * @Description: service接口--文件监控配置
 * @author: zhongsb
 * @date: 2017年8月25日
 * @company: webyun
 * @Copyright: Copyright (c) 2014
 * @UpdateUser：
 * @UpdateDescription：
 * @UpdateTime：上午10:45:00
 * @Version :V1.0
 */
public interface IMonitorService {
	
	public List<FileMonitorTask> getByFile(File file);
	
	public List<FileMonitorTask> getAllTask();

	/**
	* @Description: 获取满足条件的总条数
	* @Author ：zhongsb
	* @param like 文件路径或者文件名关键词
	* @return   
	*/
	public int getCount(String like);

	/**
	* @Description: 获取文件监控配置分页信息
	* @Author ：zhongsb
	* @param like
	* @param orderBy2
	* @param page
	* @param rows
	* @return   
	*/
	public List<FileMonitorTask> getPageList(String like, String orderBy2,
			String page, String rows);

	/**
	* @Description: 根据任务配置ID删除记录
	* @Author ：zhongsb
	* @param id
	* @return   
	*/
	public int deleteTaskById(String id);

	
	/**
	* @Description: 根据Id查找FileMonitorTask对象
	* @Author ：zhongsb
	* @param id
	* @return   FileMonitorTask
	*/
	public FileMonitorTask getTaskById(String id);

	/**
	* @Description: 根据ID修改对象
	* @Author ：zhongsb
	* @param id
	* @param fileName
	* @param filePath
	* @param endTime
	* @param deadLine   
	*/
	public void updateTaskById(String id, String fileName, String filePath,
			String endTime, String deadLine);

	/**
	* @Description: 增加任务配置
	* @Author ：zhongsb
	* @param fileName
	* @param filePath
	* @param endTime
	* @param deadLine   
	 * @param deadLine2 
	*/
	public void addTask(String fileName, String filePath, String toPath, String endTime,
			String deadLine);

}
