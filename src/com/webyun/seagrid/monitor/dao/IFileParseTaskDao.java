package com.webyun.seagrid.monitor.dao;

import java.util.List;



import com.webyun.seagrid.common.model.FileParseTaskModel;

/**
 * @Project: seaGrid
 * @Title: IFileParseTaskDao
 * @Description: 文件解析监控dao接口
 * @author: zhongsb
 * @date: 2017年9月18日
 * @company: webyun
 * @Copyright: Copyright (c) 2014
 * @UpdateUser：
 * @UpdateDescription：
 * @UpdateTime：上午9:51:24
 * @Version :V1.0
 */
public interface IFileParseTaskDao {

	/**
	 * 
	* @Description: 获取满足条件的任务记录总行数
	* @Author ：zhongsb
	* @param like
	* @return
	 */
	List<FileParseTaskModel> getCountTask(String like);

	
	/**
	* @Description: 分页查询
	* @Author ：zhongsb
	* @param like
	* @param orderBy
	* @param startRow
	* @param endRow
	* @return   
	*/
	List<FileParseTaskModel> getPageList(String like, String orderBy, int startRow, int endRow);


	/**
	* @Description: 根据脚本编号查询任务对象数组
	* @Author ：zhongsb
	* @param shellCode
	* @return   
	*/
	List<FileParseTaskModel> getByShellCode(String shellCode);


}
