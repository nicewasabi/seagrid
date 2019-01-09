package com.webyun.seagrid.monitor.dao;


import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.webyun.seagrid.common.model.FileMonitorLog;
import com.webyun.seagrid.monitor.service.IFileMonitorLogService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:applicationContext.xml","classpath:spring-servlet.xml" })
public class TestFileMonitorLogImpl {
	
	@Resource
	private IFileMonitorLogDao dao;
	@Resource
	private IFileMonitorLogService service;
	
	@Test
//	 @Transactional   //标明此方法需使用事务  
	 @Rollback(true)  //标明使用完此方法后事务不回滚,true时为回滚
	public void TestGetAllMonitorLog(){
		String startTime="20170822";
		String endTime = "20170822";
		String status = "1";
		String orderBy = "order by fileName";
		String page = "1";
		String rows="5";
		//List<FileMonitorLog> allMonitorLog = service.getAllMonitorLog(startTime, endTime, status, orderBy);
		//System.out.println("dfasdfasd"+allMonitorLog.size());
//		System.out.println(123);
	}
	
	@Test
//	 @Transactional   //标明此方法需使用事务  
	 @Rollback(true)  //标明使用完此方法后事务不回滚,true时为回滚
	public void TestgetPageList(){
		String startTime="20170822";
		String endTime = "20170822";
		String status = "1";
		String orderBy = "order by fileName";
		String page = "1";
		String rows="5";
		//List<FileMonitorLog> allMonitorLog = dao.getPageList(startTime, endTime, status, orderBy, page, rows);
		//System.out.println("dfasdfasd"+allMonitorLog.size());
//		System.out.println(123);
	}
}
