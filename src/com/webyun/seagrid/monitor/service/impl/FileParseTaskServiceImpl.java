package com.webyun.seagrid.monitor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webyun.seagrid.common.model.FileParseTaskModel;
import com.webyun.seagrid.monitor.dao.IFileParseTaskDao;
import com.webyun.seagrid.monitor.service.IFileParseTaskService;

@Service
public class FileParseTaskServiceImpl implements IFileParseTaskService {
	
	@Autowired
	private IFileParseTaskDao dao;

	@Override
	public List<FileParseTaskModel> getPageList(String like, String orderBy, String page, String rows) {
		int startRow = Integer.parseInt(page);
		int endRow = Integer.parseInt(rows);
		startRow = (startRow-1)*endRow;
		endRow = startRow + endRow;
		return this.dao.getPageList(like, orderBy, startRow, endRow);
	}

	@Override
	public int getCountTask(String like) {
		return this.dao.getCountTask(like).size();
	}

}
