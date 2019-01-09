package com.webyun.seagrid.monitor.task;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.webyun.seagrid.common.model.FileMonitorLog;
import com.webyun.seagrid.common.model.FileMonitorTask;
import com.webyun.seagrid.common.util.DatetimeUtil;
import com.webyun.seagrid.common.util.FTPUtil;
import com.webyun.seagrid.common.util.FileConfigUtil;
import com.webyun.seagrid.monitor.dao.IFileMonitorLogDao;
import com.webyun.seagrid.monitor.dao.IMonitorDao;
import com.webyun.seagrid.monitor.util.MonitorUtil;

/**
 * @Project: seaGrid
 * @Title: Monitor
 * @Description: 从ftp服务器抓取产品文件到生产服务器监控类
 * @author: zhongsb
 * @date: 2017年8月28日
 * @company: webyun
 * @Copyright: Copyright (c) 2014
 * @UpdateUser：
 * @UpdateDescription：
 * @UpdateTime：上午11:18:02
 * @Version :V1.0
 */
@Component
public class Monitor {
	
	@Resource
	private IMonitorDao taskDao ;
	
	@Resource
	private IFileMonitorLogDao logDao;
	
	
	@Resource
	private ParseCheck check;
	
	
	/**
	* @Description: 00时次的数据判断是否迟到
	* @Author ：zhongsb   
	*/
	public void isLate00(){
		isLate("00");
		isDead("00",1);//12时次的是否失效
		check.isArrived("3");//检验生成的文件是否正常
		
	}
	public void isLate12(){
		isLate("12");
		isDead("12",1);//00时次的是否失效
		check.isArrived("4");//检验生成的文件是否正常
	}
/*	public void isDead00_1(){
		isDead("00",1);
	}
	public void isDead12_1(){
		isDead("12",1);
	}*/
	
	/**
	* @Description: 获取00上一时次和12上一时次没有抓取成功的数据
	* @Author ：zhongsb   
	*/
	public void downloadNoSuccess(){
		isDead("00",0);
		isDead("12",0);
	}
/*	public void isDead12_0(){
		
	}*/
	
	
	public void run(String businessCode){
		isLate(businessCode);//文件是否迟到
		isDead(businessCode,1);//迟到的文件是否失效
		isDead(businessCode,0);//重新抓取未抓取成功的文件
		
	}
	
	
	/**
	 * 
	* @Description: 是否迟到
	* @Author ：zhongsb
	 */
	public  void isLate(String businessCode){
		
		//查找所有的单个时次的任务
		List<FileMonitorTask> taskByBusinessCode = taskDao.getTaskByBusinessCode(businessCode);
		int i;
		FileMonitorLog monitorLog = null;
		//遍历所有的任务
		for(i=0;i<taskByBusinessCode.size();i++){
			monitorLog = new FileMonitorLog();
			FileMonitorTask task = taskByBusinessCode.get(i);
			//返回确切的路径
			String filePath = MonitorUtil.replaceParams(task.getFilePath(),task.getBusinessCode());
			
			String toPath = MonitorUtil.replaceParams(task.getToPath(),task.getBusinessCode());
			File file = new File(toPath);
			if(!file.exists()){//如果本地目录不存在，则创建
				file.mkdirs();
			}
			//获取路径下所有的文件名称
			String[] downAllFile = FTPUtil.downAllFile(FileConfigUtil.getProperty("ftp.ip"), Integer.parseInt(FileConfigUtil.getProperty("ftp.port")), FileConfigUtil.getProperty("ftp.username"),
					FileConfigUtil.getProperty("ftp.password"), filePath,toPath);
			
			for(int j=0;j<downAllFile.length;j++){
				String fileName = downAllFile[j];
				//ftp抓取对应的文件
				int flag = FTPUtil.downFile(FileConfigUtil.getProperty("ftp.ip"), Integer.parseInt(FileConfigUtil.getProperty("ftp.port")), FileConfigUtil.getProperty("ftp.username"),
						FileConfigUtil.getProperty("ftp.password"), filePath, fileName, toPath);
				monitorLog.setFileName(fileName);
				monitorLog.setFilePath(filePath);
				monitorLog.setYyyymmdd(DatetimeUtil.getNextDate(new Date(),-1));
				monitorLog.setBusinessCode(task.getBusinessCode());//时次
				monitorLog.setStatus(Monitor.getStatus(flag));
				monitorLog.setProductId(task.getProductId());
				monitorLog.setToPath(toPath);
				monitorLog.setOptime(new Date());
				monitorLog.setLastmodifiedtime(new Date());
				saveLog(monitorLog);
			}
			
			
		}
	}
	
	/**
	* @Description: 获取上一次时次
	* @Author ：zhongsb
	* @param businessCode 时次
	* @return   
	*/
	public String getLastBusinessCode(String businessCode){
		if(businessCode.equals("00")){
			businessCode = "12";
		}else if(businessCode.equals("12")){
			businessCode = "00";
		}
		return businessCode;
	}
	
	/**
	* @Description: 获取上一时次的年月日
	* @Author ：zhongsb
	* @param businessCode
	* @return   
	*/
	public String getLastBusinessCodeDate(String businessCode){
		String yyyymmdd = null;
		if(businessCode.equals("00")){
			yyyymmdd =  DatetimeUtil.getNextDate(new Date(),-1);
		}else if(businessCode.equals("12")){
			yyyymmdd =  DatetimeUtil.getNextDate(new Date(),0);
		}
		return yyyymmdd;
	}
	/**
	* @Description: 查看前一个时次迟到的文件是否失效
	* @Author ：zhongsb   
	* @param businessCode 时次
	* @param isLastGet 是否是失效点进行判断 1 是 0 不是
	* 如果是1，没有抓取到就是失效，如果是0（），则只进行抓取，不修改状态
	*/
	public  void isDead(String businessCode, int isLastGet){
		//获取当前时次上一个时次的年月日以及时次
		String yyyymmdd = getLastBusinessCodeDate(businessCode);
		businessCode = getLastBusinessCode(businessCode);
		//查找所有非正常状态的日志
		int []status = MonitorUtil.getUnnormalStatus();
		//查看上一次时次的迟到的文件有没有到达，没有到达改为失效
		List<FileMonitorLog> abnormalList = logDao.getLogByStatus(status, yyyymmdd, businessCode);
		int i;
		for(i=0;i<abnormalList.size();i++){
			
			int flag = FTPUtil.downFile(FileConfigUtil.getProperty("ftp.ip"), Integer.parseInt(FileConfigUtil.getProperty("ftp.port")), FileConfigUtil.getProperty("ftp.username"),
					FileConfigUtil.getProperty("ftp.password"), abnormalList.get(i).getFilePath(), abnormalList.get(i).getFileName(), abnormalList.get(i).getToPath());
			abnormalList.get(i).setLastmodifiedtime(new Date());
			if(isLastGet==1){
				int deadStatus = getDeadStatus(flag);
				if(deadStatus!=abnormalList.get(i).getStatus()){
					//修改日志表中的状态
					abnormalList.get(i).setStatus(deadStatus);
				}
			}
			logDao.updateFileMonitorLog(abnormalList.get(i));
		}
		
	}
	/**
	* @Description: 根据ftp执行返回码确定文件状态--失效
	* @Author ：zhongsb
	* @param flag
	* @return   
	*/
	public static int getDeadStatus(int flag){
		if(flag==1){
			return 2;//正常抓取则不修改状态
		}else if(flag==2){//迟到
			return 4;//还是迟到，就是失效
		}else if(flag==0){//连接异常
			return 0;
		}else{//抓取异常
			return 3;
		}
	}
	/**
	* @Description: 根据ftp执行返回码确定文件状态
	* @Author ：zhongsb
	* @param flag
	* @return   
	*/
	public static int getStatus(int flag){
		if(flag==1){//正常
			return 1;
		}else if(flag==2){//迟到
			return 2;
		}else if(flag==0){//连接异常
			return 0;
		}else{//抓取异常
			return 3;
		}
	}
	
	public void saveLog(FileMonitorLog monitorLog){
		logDao.insertFileMonitorLog(monitorLog);
	}
	
	
	public static void main(String[] args) {
		System.out.println("monitor==============================================");
		ApplicationContext  context = new ClassPathXmlApplicationContext("applicationContext.xml");
		Monitor monitor = context.getBean(Monitor.class);
//		monitor.run(args[0]);
		monitor.run("00");
		
	}

}
