package com.webyun.seagrid.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Project: seaGrid
 * @Title: FileParseLogModel
 * @Description: 文件解析日志实体
 * @author: zhongsb
 * @date: 2017年9月14日
 * @company: webyun
 * @Copyright: Copyright (c) 2014
 * @UpdateUser：
 * @UpdateDescription：
 * @UpdateTime：上午9:04:44
 * @Version :V1.0
 */
@Entity
@Table(name="file_parse_log")
public class FileParseLogModel extends IdEntity<Integer>{
	/**
	*/
	private static final long serialVersionUID = 1L;
	/**
	 * 监控路径
	 */
	@Column(name="monitorpath")
	private String monitorpath; 
	/**
	 * 脚本编号
	 */
	@Column(name="shellcode")
	private int shellcode;
	/**
	 * 是否正常1正常，0异常
	 */
	@Column(name="isnormal")
	private int isnormal;    
	/**
	 * 产品类型
	 */
	@Column(name="product_type")
	private int product_type;
	/**
	 * 路径下文件的个数
	 */
	@Column(name="file_num")
	private int file_num;
	
	/**
	 * 路径下文件的个数
	 */
	@Column(name="yyyymmdd")
	private String yyyymmdd;
	
	/**
	 * 路径下文件的个数
	 */
	@Column(name="optime")
	private Date optime;
	
	
	public String getMonitorpath() {
		return monitorpath;
	}
	public void setMonitorpath(String monitorpath) {
		this.monitorpath = monitorpath;
	}
	public int getShellcode() {
		return shellcode;
	}
	public void setShellcode(int shellcode) {
		this.shellcode = shellcode;
	}
	public int getIsnormal() {
		return isnormal;
	}
	public void setIsnormal(int isnormal) {
		this.isnormal = isnormal;
	}
	public int getProduct_type() {
		return product_type;
	}
	public void setProduct_type(int product_type) {
		this.product_type = product_type;
	}
	public int getFile_num() {
		return file_num;
	}
	public void setFile_num(int file_num) {
		this.file_num = file_num;
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
	
	
	

}
