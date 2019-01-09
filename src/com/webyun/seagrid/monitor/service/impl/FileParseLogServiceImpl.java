package com.webyun.seagrid.monitor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webyun.seagrid.common.model.FileParseLogModel;
import com.webyun.seagrid.monitor.dao.IFileParseLogDao;
import com.webyun.seagrid.monitor.service.IFileParseLogService;

@Service
public class FileParseLogServiceImpl implements IFileParseLogService {
	
	@Autowired
	private IFileParseLogDao dao;

	@Override
	public void getAllLog(String startDate, String endDate) {
		
	}

	@Override
	public List<FileParseLogModel> getPageList(String startDate, String endDate,
			String orderBy, String page, String rows) {
		int startRow = Integer.parseInt(page);
		int endRow = Integer.parseInt(rows);
		startRow = (startRow-1)*endRow;
		endRow = startRow + endRow;
		return this.dao.getPageList(startDate,endDate,orderBy,startRow,endRow);
	}

	@Override
	public int getAllParseLog(String startDate, String endDate,
			String orderBy) {
		
		return this.dao.getAllParseLog(startDate,endDate,orderBy).size();
	}

}
