package com.webyun.seagrid.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;



/**
 * @Project: seaGrid
 * @Title: FileParseTask
 * @Description: 文件解析过程监控
 * @author: zhongsb
 * @date: 2017年9月14日
 * @company: webyun
 * @Copyright: Copyright (c) 2014
 * @UpdateUser：
 * @UpdateDescription：
 * @UpdateTime：上午9:21:42
 * @Version :V1.0
 */
@Entity
@Table(name="file_parse_task")
public class FileParseTaskModel extends IdEntity<Integer>{
	/**
	*/
	private static final long serialVersionUID = 1L;
	/**
	 * 监控路径
	 */
	@Column(name="monitorpath")
	 private String monitorpath; 
	/**
	 * 满足正则的文件数量
	 */
	@Column(name="filenum")
	 private Integer filenum;     
	/**
	 * 过程编号
	 */
	@Column(name="process_type")
	 private Integer process_type;
	/**
	 * 
	 */
	@Column(name="product_type")
	 private Integer product_type;
	/**
	 * 产品编号
	 */
	@Column(name="yyyymmdd")
	 private Integer yyyymmdd;    
	/**
	 * 时次
	 */
	@Column(name="hh")
	 private Integer hh;          
	/**
	 * 正则表达式
	 */
	@Column(name="expression")
	 private String expression;  
	/**
	 * 脚本编号
	 */
	@Column(name="shellcode")
	 private String shellcode;
	
	public String getMonitorpath() {
		return monitorpath;
	}
	public void setMonitorpath(String monitorpath) {
		this.monitorpath = monitorpath;
	}
	public Integer getFilenum() {
		return filenum;
	}
	public void setFilenum(Integer filenum) {
		this.filenum = filenum;
	}
	public Integer getProcess_type() {
		return process_type;
	}
	public void setProcess_type(Integer process_type) {
		this.process_type = process_type;
	}
	public Integer getProduct_type() {
		return product_type;
	}
	public void setProduct_type(Integer product_type) {
		this.product_type = product_type;
	}
	public Integer getYyyymmdd() {
		return yyyymmdd;
	}
	public void setYyyymmdd(Integer yyyymmdd) {
		this.yyyymmdd = yyyymmdd;
	}
	public Integer getHh() {
		return hh;
	}
	public void setHh(Integer hh) {
		this.hh = hh;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	public String getShellcode() {
		return shellcode;
	}
	public void setShellcode(String shellcode) {
		this.shellcode = shellcode;
	}


}
