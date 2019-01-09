package com.webyun.seagrid.monitor.service;

import java.util.List;

import com.webyun.seagrid.common.model.FileParseLogModel;
import com.webyun.seagrid.common.model.FileParseTaskModel;


/**
 * @Project: seaGrid
 * @Title: IFileParseTaskService
 * @Description: 文件解析过程监控service接口
 * @author: zhongsb
 * @date: 2017年9月18日
 * @company: webyun
 * @Copyright: Copyright (c) 2014
 * @UpdateUser：
 * @UpdateDescription：
 * @UpdateTime：上午9:43:57
 * @Version :V1.0
 */
public interface IFileParseTaskService {

	
	/**
	* @Description: 分页查询，
	* @Author ：zhongsb
	* @param like like为monitorpath
	* @param orderBy 排序字段
	* @param page easyui传递的参数page
	* @param rows easyui传递的参数rows
	* @return   
	*/
	List<FileParseTaskModel> getPageList(String like, String orderBy, String page, String rows);

	
	/**
	* @Description: 获取满足条件的任务条数
	* @Author ：zhongsb
	* @param like
	* @return   
	*/
	int getCountTask(String like);

	

}
