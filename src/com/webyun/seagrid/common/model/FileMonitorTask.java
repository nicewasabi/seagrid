package com.webyun.seagrid.common.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="FTPFILEMONITORTASK")
public class FileMonitorTask extends IdEntity<Integer> {
	
	/**
	*/
	private static final long serialVersionUID = 1L;
	/**
	* 文件名称
	*/
	@Column(name="fileName")
	private String fileName;
	/**
	 * 远程存储文件路径
	 */
	@Column(name="filePath")
	private String filePath;
	
	/**
	 * 本地存储路径
	 */
	@Column(name="toPath")
	private String toPath;
	/**
	 * 开始时间
	 */
	@Column(name="startTime")
	private String startTime;
	/**
	 * 迟到时间
	 */
	@Column(name="endTime")
	private String endTime;
	/**
	 * 失效时间
	 */
	@Column(name="deadline")
	private String deadline;
	/**
	 * 产品ID
	 */
	@Column(name="productId")
	private String productId;
	/**
	 * 是否1启用 2弃用
	 */
	@Column(name="type")
	private int type;
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
	 * 过期时间是否下一天，0不是，1是
	 */
	@Column(name="isNextDay")
	private int isNextDay;
	/**
	 * 操作时间
	 */
	@Column(name="optime")
	private String optime;
	public FileMonitorTask(String fileName, String filePath, String startTime,
			String endTime, String deadline, String productId, int type,
			String businessCode, String timeLimit, int isNextDay) {
		super();
		this.fileName = fileName;
		this.filePath = filePath;
		this.startTime = startTime;
		this.endTime = endTime;
		this.deadline = deadline;
		this.productId = productId;
		this.type = type;
		this.businessCode = businessCode;
		this.timeLimit = timeLimit;
		this.isNextDay = isNextDay;
	}
	public FileMonitorTask() {
		super();
	}
	public String getFileName() {
		return fileName;
	}
	public String getToPath() {
		return toPath;
	}
	public void setToPath(String toPath) {
		this.toPath = toPath;
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
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getDeadline() {
		return deadline;
	}
	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
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
	public int getIsNextDay() {
		return isNextDay;
	}
	public void setIsNextDay(int isNextDay) {
		this.isNextDay = isNextDay;
	}
	public String getOptime() {
		return optime;
	}
	public void setOptime(String optime) {
		this.optime = optime;
	}

	
	

}
