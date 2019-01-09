package com.webyun.seagrid.monitor.service;

import java.util.List;

import com.webyun.seagrid.common.model.FileParseLogModel;

public interface IFileParseLogService {

	/**
	 * 
	* @Description: 根据日期获取解析过程日志
	* @Author ：zhongsb
	* @param startDate
	* @param endDate
	 */
	void getAllLog(String startDate, String endDate);

	
	/**
	* @Description: 分页查询
	* @Author ：zhongsb
	* @param startDate
	* @param endDate
	* @param orderBy 排序字段
	* @param page easyui传递的当前页数
	* @param rows easyui传递的行数
	* @return   List<FileParseLogModel>
	*/
	List<FileParseLogModel> getPageList(String startDate, String endDate,
			String orderBy, String page, String rows);

	/**
	* @Description: 查询开始时间到结束时间的总记录数
	* @Author ：zhongsb
	* @param startDate
	* @param endDate
	* @param orderBy
	* @return   
	*/
	int getAllParseLog(String startDate, String endDate, String orderBy);

}
