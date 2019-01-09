package com.webyun.seagrid.core.system.controller;

import java.io.Serializable;
import java.text.DecimalFormat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.webyun.seagrid.common.util.ToolOS;

/**
 * @Project: dataShare
 * @Title: ComputerController
 * @Description: 服务器状态
 * @author: zhangx
 * @date: 2017年7月18日 上午10:54:51
 * @company: webyun
 * @Copyright: Copyright (c) 2017
 * @version v1.0
 */
@Controller
@RequestMapping("/computer")
public class ComputerController {
	
	private static java.text.DecimalFormat df = new DecimalFormat("##.##");
	
	@RequestMapping("/manage.do")
	public ModelAndView getStatus(){
		ModelAndView mav = new ModelAndView();
		String jvmTotalMemory = df.format(ToolOS.getJvmTotalMemory()); // JVM内存，内存总量
		String jvmFreeMemory = df.format(ToolOS.getJvmFreeMemory()); // JVM内存，空闲内存量
		String jvmMaxMemory = df.format(ToolOS.getJvmMaxMemory()); // JVM内存，最大内存量
		String memTotal= df.format(ToolOS.getOsPhysicalMemory()); // 物理内存，总的可使用的
		String memUsed= df.format(ToolOS.getOsPhysicalMemory() - ToolOS.getOsPhysicalFreeMemory());
		Integer cpuMhz= ToolOS.getOsCpuNumber();//CPU的总量MHz
		String cpuUsed= df.format(ToolOS.getOscpuRatio() * 100); // cpu使用率
		String coName= ToolOS.getOsName(); // 获取操作系统类型名称
		String osDataModel= ToolOS.getDataModel();//系统内核类型
		String ssdTotal= df.format(ToolOS.getOsDiskMemory() / 1024);// 盘总量
		String ssdUsed= df.format(ToolOS.getOsDiskFreeMemory() / 1024);// c盘空闲大小
		Computer computer = new Computer(memTotal, memUsed, cpuMhz, cpuUsed, coName, osDataModel, ssdTotal, ssdUsed, jvmTotalMemory, jvmFreeMemory, jvmMaxMemory);
		mav.addObject("computer", computer);
		mav.setViewName("system/computer");
	
		return mav;
	}
	
	
	/**
	 * @Project: dataShare
	 * @Title: Computer
	 * @Description: 服务器状态（都是基于当前一个用户的状态）
	 * @author: zhangx
	 * @date: 2017年7月18日 上午11:00:31
	 * @company: webyun
	 * @Copyright: Copyright (c) 2017
	 * @version v1.0
	 */
	public static class Computer implements Serializable{

		private static final long serialVersionUID = 8202117237839093597L;
		
		private String userName;//当前用户的名称
		
		private String runTime;//运行时间
		
		private Integer connecterAll;//所有的连接数
		
		private Integer connecterClient;//客户端的连接数
		
		private String memTotal;//内存总量
		
		private String memUsed;//内存使用量
		
		private Integer cpuMhz;//CPU的总量MHz
		
		private String cpuUsed;//cpu使用率
		
		private String coName;//操作系统
		
		private String osDataModel;//系统内核类型
		
		private String ssdTotal;//磁盘总量
		
		private String ssdUsed;//磁盘使用量
		
		private String jvmTotalMemory; // JVM内存，内存总量
		
		private String jvmFreeMemory; // JVM内存，空闲内存量
		
		private String jvmMaxMemory; // JVM内存，最大内存量
		
		public Computer() {		}

		public Computer(String userName, String runTime, Integer connecterAll, Integer connecterClient, String memTotal,
				String memUsed, Integer cpuMhz, String cpuUsed, String coName, String osDataModel, String ssdTotal,
				String ssdUsed, String jvmTotalMemory, String jvmFreeMemory, String jvmMaxMemory) {
			this.userName = userName;
			this.runTime = runTime;
			this.connecterAll = connecterAll;
			this.connecterClient = connecterClient;
			this.memTotal = memTotal;
			this.memUsed = memUsed;
			this.cpuMhz = cpuMhz;
			this.cpuUsed = cpuUsed;
			this.coName = coName;
			this.osDataModel = osDataModel;
			this.ssdTotal = ssdTotal;
			this.ssdUsed = ssdUsed;
			this.jvmTotalMemory = jvmTotalMemory;
			this.jvmFreeMemory = jvmFreeMemory;
			this.jvmMaxMemory = jvmMaxMemory;
		}

		public Computer(String memTotal2, String memUsed2, Integer cpuMhz2,
				String cpuUsed2, String coName2, String osDataModel2,
				String ssdTotal2, String ssdUsed2, String jvmTotalMemory2,
				String jvmFreeMemory2, String jvmMaxMemory2) {
			this.memTotal = memTotal2;
			this.memUsed = memUsed2;
			this.cpuMhz = cpuMhz2;
			this.cpuUsed = cpuUsed2;
			this.coName = coName2;
			this.osDataModel = osDataModel2;
			this.ssdTotal = ssdTotal2;
			this.ssdUsed = ssdUsed2;
			this.jvmTotalMemory = jvmTotalMemory2;
			this.jvmFreeMemory = jvmFreeMemory2;
			this.jvmMaxMemory = jvmMaxMemory2;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getRunTime() {
			return runTime;
		}

		public void setRunTime(String runTime) {
			this.runTime = runTime;
		}

		public Integer getConnecterAll() {
			return connecterAll;
		}

		public void setConnecterAll(Integer connecterAll) {
			this.connecterAll = connecterAll;
		}

		public Integer getConnecterClient() {
			return connecterClient;
		}

		public void setConnecterClient(Integer connecterClient) {
			this.connecterClient = connecterClient;
		}

		public String getMemTotal() {
			return memTotal;
		}

		public void setMemTotal(String memTotal) {
			this.memTotal = memTotal;
		}

		public String getMemUsed() {
			return memUsed;
		}

		public void setMemUsed(String memUsed) {
			this.memUsed = memUsed;
		}

		public Integer getCpuMhz() {
			return cpuMhz;
		}

		public void setCpuMhz(Integer cpuMhz) {
			this.cpuMhz = cpuMhz;
		}

		public String getCpuUsed() {
			return cpuUsed;
		}

		public void setCpuUsed(String cpuUsed) {
			this.cpuUsed = cpuUsed;
		}

		public String getCoName() {
			return coName;
		}

		public void setCoName(String coName) {
			this.coName = coName;
		}

		public String getOsDataModel() {
			return osDataModel;
		}

		public void setOsDataModel(String osDataModel) {
			this.osDataModel = osDataModel;
		}

		public String getSsdTotal() {
			return ssdTotal;
		}

		public void setSsdTotal(String ssdTotal) {
			this.ssdTotal = ssdTotal;
		}

		public String getSsdUsed() {
			return ssdUsed;
		}

		public void setSsdUsed(String ssdUsed) {
			this.ssdUsed = ssdUsed;
		}

		public String getJvmTotalMemory() {
			return jvmTotalMemory;
		}

		public void setJvmTotalMemory(String jvmTotalMemory) {
			this.jvmTotalMemory = jvmTotalMemory;
		}

		public String getJvmFreeMemory() {
			return jvmFreeMemory;
		}

		public void setJvmFreeMemory(String jvmFreeMemory) {
			this.jvmFreeMemory = jvmFreeMemory;
		}

		public String getJvmMaxMemory() {
			return jvmMaxMemory;
		}

		public void setJvmMaxMemory(String jvmMaxMemory) {
			this.jvmMaxMemory = jvmMaxMemory;
		}

		@Override
		public String toString() {
			return "Computer [userName=" + userName + ", runTime=" + runTime + ", connecterAll=" + connecterAll
					+ ", connecterClient=" + connecterClient + ", memTotal=" + memTotal + ", memUsed=" + memUsed
					+ ", cpuMhz=" + cpuMhz + ", cpuUsed=" + cpuUsed + ", coName=" + coName + ", osDataModel="
					+ osDataModel + ", ssdTotal=" + ssdTotal + ", ssdUsed=" + ssdUsed + ", jvmTotalMemory="
					+ jvmTotalMemory + ", jvmFreeMemory=" + jvmFreeMemory + ", jvmMaxMemory=" + jvmMaxMemory + "]";
		}
		
	}
	
}
