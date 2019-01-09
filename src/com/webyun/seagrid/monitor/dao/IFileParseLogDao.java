package com.webyun.seagrid.monitor.dao;

import java.util.List;

import com.webyun.seagrid.common.model.FileParseLogModel;

public interface IFileParseLogDao {

	/**
	 * 
	* @Description: 分页查询
	* @Author ：zhongsb
	* @param startDate
	* @param endDate
	* @param orderBy
	* @param startRow
	* @param endRow
	* @return
	 */
	List<FileParseLogModel> getPageList(String startDate, String endDate,
			String orderBy, int startRow, int endRow);

	/**
	 * 
	* @Description: 根据开始结束日期查询总记录数
	* @Author ：zhongsb
	* @param startDate
	* @param endDate
	* @param orderBy
	* @return
	 */
	List<Object[]> getAllParseLog(String startDate, String endDate, String orderBy);

	/**
	* @Description: 保存日志对象
	* @Author ：zhongsb
	* @param parseLog   
	*/
	void save(FileParseLogModel parseLog);

}
