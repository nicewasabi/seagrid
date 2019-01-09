package com.webyun.seagrid.monitor.service.impl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webyun.seagrid.common.model.FileMonitorTask;
import com.webyun.seagrid.monitor.dao.impl.MonitorImpl;
import com.webyun.seagrid.monitor.service.IMonitorService;

@Service
public class MonitorServiceImpl implements IMonitorService {
	
	@Autowired
	private MonitorImpl dao;
	
	public List<FileMonitorTask> getAllTask(){
		List<FileMonitorTask> list = dao.getAll();
		return list;
	}

	@Override
	public List<FileMonitorTask> getByFile(File file) {
		
		return this.dao.getByFile(file);
	}

	@Override
	public int getCount(String like) {
		
		return this.dao.getCount(like);
	}

	@Override
	public List<FileMonitorTask> getPageList(String like, String orderBy2,
			String page, String rows) {
		return this.dao.getPageList(like,orderBy2,page,rows);
	}

	@Override
	public int deleteTaskById(String id) {
		return this.dao.deleteTaskById(id);
	}

	@Override
	public FileMonitorTask getTaskById(String id) {
		return this.dao.getTaskById(id);
	}

	@Override
	public void updateTaskById(String id, String fileName, String filePath,
			String endTime, String deadLine) {
		FileMonitorTask task = dao.getTaskById(id);
		task.setFileName(fileName);
		task.setFilePath(filePath);
		task.setEndTime(endTime);
		task.setDeadline(deadLine);
		try {
			dao.update(task);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addTask(String fileName, String filePath, String toPath, String endTime,
			String deadLine) {
		FileMonitorTask task = new FileMonitorTask();
		task.setFileName(fileName);
		task.setFilePath(filePath);
		task.setToPath(toPath);
		task.setEndTime(endTime);
		task.setDeadline(deadLine);
		try {
			dao.insert(task);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		
	}
}
