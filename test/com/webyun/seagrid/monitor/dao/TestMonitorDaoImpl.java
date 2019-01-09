package com.webyun.seagrid.monitor.dao;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.webyun.seagrid.common.model.FileMonitorTask;
import com.webyun.seagrid.monitor.service.IMonitorService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml","classpath:spring-servlet.xml"})
public class TestMonitorDaoImpl {
	
	@Resource
	private IMonitorDao dao;
	
	@Resource
	private IMonitorService service;
	
	@Test
	public void testgetCount(){
		String like = "filepath";
		int size = dao.getCount(like);
		System.out.println(size);
	}
	
	@Test
	public void testGetPageList(){
		
		String like = "filePath";
		String page = "1";
		String rows = "10";
		String orderBy2 = "order by fileName";
		List<FileMonitorTask> pageList = dao.getPageList(like, orderBy2, page, rows);
		for(int i=0;i<pageList.size();i++){
			System.out.println(pageList.get(i).getFileName());
		}
	}
	
	@Test
	public void testDeleteById(){
		String id = "1";
		int count = dao.deleteTaskById(id);
		System.out.println("count=="+count);
	}
	
	@Test 
	public void testgetTaskById(){
		String id = "15";
		FileMonitorTask taskById = dao.getTaskById(id);
		System.out.println(taskById.getFilePath());
	}
	
	@Test 
	public void testupdateTaskById(){
		String id = "15";
		String filePath = "/data/eps";
		String fileName = "eps_t639_201708250012.mic";
		String endTime = "14:35";
		String deadLine = "17:59";
		service.updateTaskById(id, fileName, filePath, endTime, deadLine);
		System.out.println("==============");
	}
	
	@Test
	public void testGetAllMonitorTask(){
		List<FileMonitorTask> list = dao.getAllMonitorTask();
		System.out.println(list.size());
	}

}
