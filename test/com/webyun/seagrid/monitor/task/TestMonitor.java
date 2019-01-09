package com.webyun.seagrid.monitor.task;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml","classpath:spring-servlet.xml"})
public class TestMonitor {
	
	@Resource
	private Monitor monitor;
	
	@Test
	public void testGetFile(){
		//monitor.getFile();
	}

}
