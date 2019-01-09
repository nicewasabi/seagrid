package com.webyun.seagrid.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="FILEMONITORLOG")
public class FileMonitorLog extends IdEntity<Integer> {
	
	/**
	*/

	/**
	* 文件名称
	*/
	@Column(name="fileName")
	private String fileName;
	/**
	 * 文件路径
	 */
	@Column(name="filePath")
	private String filePath;
	
	/**
	 * 本地存放路径
	 */
	@Column(name="toPath")
	private String toPath;
	
	/**
	 * //0缺报 1正常 2迟到  4失效
	 */
	@Column(name="status")
	private int status;
	/**
	 * 操作时间
	 */
	@Column(name="optime")
	private Date optime;
	
	/**
	 * 文件最后修改时间
	 */
	@Column(name="lastmodifiedtime")
	private Date lastmodifiedtime;
	/**
	 * 产品ID
	 */
	@Column(name="productId")
	private String productId;
	
	/**
	 * 产品文件年月日
	 */
	@Column(name="yyyymmdd")
	private String yyyymmdd;
	/**
	 * 时次
	 */
	@Column(name="businessCode")
	private String businessCode;
	/**
	 * 时效
	 */
	@Column(name="timeLimit")
	private String timeLimit;
	
	/**
	 * 文件是否到达，0未到，1已到
	 */
	@Column(name="isArrived")
	private int isArrived;
	public FileMonitorLog() {
		super();
	}
	public FileMonitorLog(String fileName, String filePath, int status,
			Date lastmodifiedtime, String productId, String yyyymmdd,
			String businessCode, String timeLimit) {
		super();
		this.fileName = fileName;
		this.filePath = filePath;
		this.status = status;
		this.lastmodifiedtime = lastmodifiedtime;
		this.productId = productId;
		this.yyyymmdd = yyyymmdd;
		this.businessCode = businessCode;
		this.timeLimit = timeLimit;
	}
	
	public int getIsArrived() {
		return isArrived;
	}
	public void setIsArrived(int isArrived) {
		this.isArrived = isArrived;
	}
	public String getToPath() {
		return toPath;
	}
	public void setToPath(String toPath) {
		this.toPath = toPath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getOptime() {
		return optime;
	}
	public void setOptime(Date optime) {
		this.optime = optime;
	}
	public Date getLastmodifiedtime() {
		return lastmodifiedtime;
	}
	public void setLastmodifiedtime(Date lastmodifiedtime) {
		this.lastmodifiedtime = lastmodifiedtime;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getYyyymmdd() {
		return yyyymmdd;
	}
	public void setYyyymmdd(String yyyymmdd) {
		this.yyyymmdd = yyyymmdd;
	}
	public String getBusinessCode() {
		return businessCode;
	}
	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}
	public String getTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(String timeLimit) {
		this.timeLimit = timeLimit;
	}

	
	

}
