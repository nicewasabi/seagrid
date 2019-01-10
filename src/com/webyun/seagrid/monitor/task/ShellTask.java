package com.webyun.seagrid.monitor.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.webyun.seagrid.common.util.FileConfigUtil;
import com.webyun.seagrid.common.util.RunShell;
import com.webyun.seagrid.monitor.util.MonitorUtil;


/**
 * @Project: seaGrid
 * @Title: ShellTask
 * @Description: 
 * @author: zhongsb
 * @date: 2017年9月18日
 * @company: webyun
 * @Copyright: Copyright (c) 2014
 * @UpdateUser：
 * @UpdateDescription：
 * @UpdateTime：下午7:29:07
 * @Version :V1.0
 */

public class ShellTask {
	
	@Resource
	private ParseCheck check;
	
	
	/**
	* @Description: 获取流数据
	* @Author ：zhongsb   
	*/
	public void getNcData(){
		String shellPath = FileConfigUtil.getProperty("nc.data.shell");
		String logPath = FileConfigUtil.getProperty("nc.data.log");
		runShell(shellPath,  logPath);
		check.isArrived("2");//检验生成的文件是否正常
	}
	
	/**
	* @Description: java执行shell脚本
	*  主要针对：getgrib.sh， getgribhres.h
	* @Author ：zhongsb
	* @param shellPath 脚本全路径
	* @param param1 参数一
	* @param param2 参数二
	* @param logPath 输出文件路径，根据config.properties
	*/
	public  boolean runShell(String shellPath, String param1, String param2, String logPath){
		String[] str = {shellPath, param1, param2};
		RunShell runScript = new RunShell();
		String run;
		try {
			run = runScript.run(str);
			//根据配置的路径
			File file = new File(logPath);
			OutputStream  out = new FileOutputStream(file);
			byte[] b = run.getBytes();
			out.write(b);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	* @Description: 执行shell脚本 ,无参数
	* ncProcess_${area}_${hh}.sh area=china/global; hh=001,024,048,072,096,120
	* @Author ：zhongsb
	* @param shellPath  shell脚本全路径 
	* @param logPath 输出文件路径
	*/
	public  boolean runShell(String shellPath, String logPath){
		RunShell runScript = new RunShell();
		String[] str = {shellPath};
		String run;
		try {
			run = runScript.run(str);
			//根据配置的路径
			File file = new File(logPath);
			OutputStream  out = new FileOutputStream(file);
			byte[] b = run.getBytes();
			out.write(b);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	* @Description: Wamhres模式 12时次
	* @Author ：zhongsb   
	*/
	public void Wamhres12(){
		String shellPath = FileConfigUtil.getProperty("WAMHRES12.shell");
		String yyyyMMdd = MonitorUtil.getLastDay();
		String logPath = FileConfigUtil.getProperty("WAMHRES12.log");
		boolean runShell = runShell(shellPath, yyyyMMdd, "12", logPath);
		if(runShell){
			check.isArrived("1");//检验生成的文件是否正常
		}
	}
	
	/**
	* @Description: Wamhres模式 00时次
	* @Author ：zhongsb   
	*/
	public void Wamhres00(){
		String shellPath = FileConfigUtil.getProperty("WAMHRES00.shell");
		String yyyyMMdd = MonitorUtil.getToday();
		String logPath = FileConfigUtil.getProperty("WAMHRES00.log");
		boolean runShell = runShell(shellPath, yyyyMMdd, "00", logPath);
		if(runShell){
			check.isArrived("2");//检验生成的文件是否正常
		}
	}
	
	/**
	* @Description: Wamhres模式 12时次
	* @Author ：zhongsb   
	*/
	public void hres12(){
		String shellPath = FileConfigUtil.getProperty("hres12.shell");
		String yyyyMMdd = MonitorUtil.getLastDay();
		String logPath = FileConfigUtil.getProperty("hres12.log");
		boolean runShell = runShell(shellPath, yyyyMMdd, "12", logPath);
		if(runShell){
			check.isArrived("12");//检验生成的文件是否正常
		}
	}
	
	/**
	* @Description: Wamhres模式 00时次
	* @Author ：zhongsb   
	*/
	public void hres00(){
		String shellPath = FileConfigUtil.getProperty("hres00.shell");
		String yyyyMMdd = MonitorUtil.getToday();
		String logPath = FileConfigUtil.getProperty("hres00.log");
		boolean runShell = runShell(shellPath, yyyyMMdd, "00", logPath);
		if(runShell){
			check.isArrived("8");//检验生成的文件是否正常
		}
	}
	
	
	/**
	* @Description: nc数据处理，区域：china，时效001
	* @Author ：zhongsb   
	*/
	public void ncProcess_china_001(){
		
		String shellPath = FileConfigUtil.getProperty("rtofs.nc.china_001.shell");
		String logPath = FileConfigUtil.getProperty("rtofs.nc.china_001.log");
		runShell(shellPath, logPath);
		
	}
	public void ncProcess_china_024(){
		String shellPath = FileConfigUtil.getProperty("rtofs.nc.china_024.shell");
		String logPath = FileConfigUtil.getProperty("rtofs.nc.china_024.log");
		runShell(shellPath, logPath);
		
	}
	public void ncProcess_china_048(){
		String shellPath = FileConfigUtil.getProperty("rtofs.nc.china_048.shell");
		String logPath = FileConfigUtil.getProperty("rtofs.nc.china_048.log");
		runShell(shellPath, logPath);
	}
	public void ncProcess_china_072(){
		String shellPath = FileConfigUtil.getProperty("rtofs.nc.china_072.shell");
		String logPath = FileConfigUtil.getProperty("rtofs.nc.china_072.log");
		runShell(shellPath, logPath);
		
	}
	public void ncProcess_china_096(){
		String shellPath = FileConfigUtil.getProperty("rtofs.nc.china_096.shell");
		String logPath = FileConfigUtil.getProperty("rtofs.nc.china_096.log");
		runShell(shellPath, logPath);
	}
	public void ncProcess_china_120(){
		String shellPath = FileConfigUtil.getProperty("rtofs.nc.china_120.shell");
		String logPath = FileConfigUtil.getProperty("rtofs.nc.china_120.log");
		runShell(shellPath, logPath);
	}
	public void ncProcess_global_001(){
		String shellPath = FileConfigUtil.getProperty("rtofs.nc.global_001.shell");
		String logPath = FileConfigUtil.getProperty("rtofs.nc.global_001.log");
		runShell(shellPath, logPath);
	}
	public void ncProcess_global_024(){
		String shellPath = FileConfigUtil.getProperty("rtofs.nc.global_024.shell");
		String logPath = FileConfigUtil.getProperty("rtofs.nc.global_024.log");
		runShell(shellPath, logPath);
	}
	public void ncProcess_global_048(){
		String shellPath = FileConfigUtil.getProperty("rtofs.nc.global_048.shell");
		String logPath = FileConfigUtil.getProperty("rtofs.nc.global_048.log");
		runShell(shellPath, logPath);
	}
	public void ncProcess_global_072(){
		String shellPath = FileConfigUtil.getProperty("rtofs.nc.global_072.shell");
		String logPath = FileConfigUtil.getProperty("rtofs.nc.global_072.log");
		runShell(shellPath, logPath);
	}
	public void ncProcess_global_096(){
		String shellPath = FileConfigUtil.getProperty("rtofs.nc.global_096.shell");
		String logPath = FileConfigUtil.getProperty("rtofs.nc.global_096.log");
		runShell(shellPath, logPath);
	}
	public void ncProcess_global_120(){
		String shellPath = FileConfigUtil.getProperty("rtofs.nc.global_120.shell");
		String logPath = FileConfigUtil.getProperty("rtofs.nc.global_120.log");
		boolean runShell = runShell(shellPath, logPath);
		if(runShell){
			check.isArrived("6");//检验生成的文件是否正常
		}
	}
	
	
	public static void main(String[] args) {
		System.out.println("monitor==============================================");
		ShellTask monitor = null;
		try {
			ApplicationContext  context = new ClassPathXmlApplicationContext("applicationContext.xml");
			monitor = context.getBean(ShellTask.class);
		} catch (BeansException e) {
			e.printStackTrace();
		}
//		ShellTask monitor = new ShellTask();
//		monitor.runShell("");
//		monitor.run(args[0]);
	}
	
}
