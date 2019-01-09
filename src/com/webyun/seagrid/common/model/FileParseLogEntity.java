package com.webyun.seagrid.common.model;

import java.util.Date;


public class FileParseLogEntity {
	
	/**
	 * 监控路径
	 */
	private String monitorpath; 
	/**
	 * 是否正常1正常，0异常
	 */
	private int isnormal;    
	/**
	 * 路径下文件的个数
	 */
	private String yyyymmdd;
	
	/**
	 * 路径下文件的个数
	 */
	private Date optime;
	
	private String shellDescription;

	public String getMonitorpath() {
		return monitorpath;
	}

	public void setMonitorpath(String monitorpath) {
		this.monitorpath = monitorpath;
	}

	public int getIsnormal() {
		return isnormal;
	}

	public void setIsnormal(int isnormal) {
		this.isnormal = isnormal;
	}

	public String getYyyymmdd() {
		return yyyymmdd;
	}

	public void setYyyymmdd(String yyyymmdd) {
		this.yyyymmdd = yyyymmdd;
	}

	public Date getOptime() {
		return optime;
	}

	public void setOptime(Date optime) {
		this.optime = optime;
	}

	public String getShellDescription() {
		return shellDescription;
	}

	public void setShellDescription(String shellDescription) {
		this.shellDescription = shellDescription;
	}
	
	

}
