package com.webyun.seagrid.monitor.task;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.webyun.seagrid.common.model.FileParseLogModel;
import com.webyun.seagrid.common.model.FileParseTaskModel;
import com.webyun.seagrid.monitor.dao.IFileParseLogDao;
import com.webyun.seagrid.monitor.dao.IFileParseTaskDao;

/**
 * 
 * @Project: seaGrid
 * @Title: ParseCheck
 * @Description: 监测文件解析各个过程目录文件生成是否正常
 * @author: zhongsb
 * @date: 2017年9月14日
 * @company: webyun
 * @Copyright: Copyright (c) 2014
 * @UpdateUser：
 * @UpdateDescription：
 * @UpdateTime：下午12:22:35
 * @Version :V1.0
 */
@Component
public class ParseCheck {
	
	@Resource
	private IFileParseTaskDao taskDao;
	
	@Resource
	private IFileParseLogDao logDao;
	
	
	/**
	* @Description: 
	* @Author ：zhongsb
	* @param shellCode 在util.js中定义
	 */ 
	public void isArrived(String shellCode){
		List<FileParseTaskModel> list =  new ArrayList<FileParseTaskModel>();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String yyyyMMdd = sdf.format(cal.getTime());
		list = taskDao.getByShellCode(shellCode);
		//遍历检查路径下的满足条件的文件个数是否与配置的一致
		list:for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			FileParseTaskModel fileParseTaskModel = (FileParseTaskModel) iterator.next();
			String monitorpath = fileParseTaskModel.getMonitorpath().replace("yyyymmdd".toUpperCase(), yyyyMMdd);
			File file = new File(monitorpath);
			final String expression = fileParseTaskModel.getExpression();
			File[] fileList = file.listFiles(new FileFilter() {// 此处是对指定的目录下的过滤操作
				public boolean accept(File pathname) {
					return pathname.getName().matches(expression);
				}
			});
			
			int size = 0;
			if(fileList != null){
				size = fileList.length;
			}
			
			FileParseLogModel parseLog = new FileParseLogModel();
			
			parseLog.setFile_num(size);
			parseLog.setMonitorpath(monitorpath);
			parseLog.setOptime(new Date());
			parseLog.setProduct_type(fileParseTaskModel.getProduct_type());
			parseLog.setShellcode(Integer.parseInt(shellCode));
			
			parseLog.setYyyymmdd(yyyyMMdd);
			
			if(size == fileParseTaskModel.getFilenum()){//正常
				parseLog.setIsnormal(1);
			}else{//异常
				parseLog.setIsnormal(0);
			}
			//保存日志对象
			logDao.save(parseLog);
			
		}
	}
	public static void main(String[] args) {
		System.out.println("monitor==============================================");
		ApplicationContext  context = new ClassPathXmlApplicationContext("applicationContext.xml");
		ParseCheck monitor = context.getBean(ParseCheck.class);
//		monitor.run(args[0]);
		monitor.isArrived("1");
		
	}

}
