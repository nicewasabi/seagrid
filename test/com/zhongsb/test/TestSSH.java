package com.zhongsb.test;


import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.webyun.seagrid.monitor.controller.FileMonitorTaskController;


public class TestSSH {

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	private ApplicationContext ctx = null;
	
	@Test
	public void tetsDataSource() throws SQLException{
		ctx = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		System.out.println("数据源:"+ctx);
		DataSource dataSource = ctx.getBean(DataSource.class);
		System.out.println("打开数据连接："+dataSource.getConnection().toString());
		SessionFactory sessionFactory = ctx.getBean(SessionFactory.class);
		System.out.println("sessionFactory:"+sessionFactory); 
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		tx.commit();
		session.close();
	}
	
	public void test2(){
	}
	
}
