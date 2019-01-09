package com.webyun.seagrid.monitor.service.impl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webyun.seagrid.common.model.FileMonitorLog;
import com.webyun.seagrid.monitor.dao.IFileMonitorLogDao;
import com.webyun.seagrid.monitor.service.IFileMonitorLogService;

@Service
public class FileMonitorLogServiceImpl implements IFileMonitorLogService {
	
	@Autowired
	private IFileMonitorLogDao dao;
	

	@Override
	public List<FileMonitorLog[]> getByFile(File file) {
		
		return this.dao.getByFile(file);
	}


	@Override
	public void updateFileMonitorLog(FileMonitorLog fileMonitorLog) {
		this.dao.updateFileMonitorLog(fileMonitorLog);
	}


	@Override
	public void insertFileMonitorLog(FileMonitorLog fileMonitorLog) {
		this.dao.insertFileMonitorLog(fileMonitorLog);
	}


	@Override
	public List<FileMonitorLog> getAllMonitorLog(String startTime,
			String endTime,  String orderBy) {
		return this.dao.getAllMonitorLog(startTime,endTime,orderBy);
	}


	@Override
	public List<FileMonitorLog> getPageList(String startTime, String endTime,
			 String orderBy, String page, String rows) {
		// TODO Auto-generated method stub
		return this.dao.getPageList(startTime, endTime,  orderBy,page, rows);
	}


	@Override
	public FileMonitorLog getNewLog() {
		int []status={0,2,3,4};
		return this.dao.getNewLog(status);
	}
}
