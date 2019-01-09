package com.webyun.seagrid.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Project: seaGrid
 * @Title: Dict_data
 * @Description: 字典类
 * @author: zhongsb
 * @date: 2017年9月13日
 * @company: webyun
 * @Copyright: Copyright (c) 2014
 * @UpdateUser：
 * @UpdateDescription：
 * @UpdateTime：下午8:00:50
 * @Version :V1.0
 */
/*@Entity
@Table(name="Dict_data")*/
public class DictDataModel extends IdEntity<Integer>{
	/**
	*/
	private static final long serialVersionUID = 1L;
	/**
	 * 字典编号
	 */
	@Column(name="dict_code")
	private String  dict_code;  
	/**
	 * 字典编号
	 */
	@Column(name="data_id")
	private String  data_id; 
	/**
	 * 字典编号
	 */
	@Column(name="data_name")
	private String  data_name;   
	/**
	 * 字典编号
	 */
	@Column(name="data_default")
	private int  data_default; 
	/**
	 * 字典编号
	 */
	@Column(name="sortid")
	private int  sortid;  
	/**
	 * 字典编号
	 */
	@Column(name="addtime")
	private Date  addtime;     
	/**
	 * 字典编号
	 */
	@Column(name="modifytime")
	private Date  modifytime; 
	/**
	 * 字典编号
	 */
	@Column(name="operatorid")
	private int  operatorid;
	/**
	 * 字典编号
	 */
	@Column(name="operatorname")
	private String  operatorname;
	
	public String getDict_code() {
		return dict_code;
	}
	public void setDict_code(String dict_code) {
		this.dict_code = dict_code;
	}
	public String getData_id() {
		return data_id;
	}
	public void setData_id(String data_id) {
		this.data_id = data_id;
	}
	public String getData_name() {
		return data_name;
	}
	public void setData_name(String data_name) {
		this.data_name = data_name;
	}
	public int getData_default() {
		return data_default;
	}
	public void setData_default(int data_default) {
		this.data_default = data_default;
	}
	public int getSortid() {
		return sortid;
	}
	public void setSortid(int sortid) {
		this.sortid = sortid;
	}
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	public Date getModifytime() {
		return modifytime;
	}
	public void setModifytime(Date modifytime) {
		this.modifytime = modifytime;
	}
	public int getOperatorid() {
		return operatorid;
	}
	public void setOperatorid(int operatorid) {
		this.operatorid = operatorid;
	}
	public String getOperatorname() {
		return operatorname;
	}
	public void setOperatorname(String operatorname) {
		this.operatorname = operatorname;
	} 

	
	
}
