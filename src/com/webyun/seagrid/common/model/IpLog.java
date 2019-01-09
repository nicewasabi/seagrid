package com.webyun.seagrid.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Project: xywl
 * @Title: IpLog
 * @Description: ip日志
 * @author: zhangx
 * @date: 2017年3月23日 下午2:06:22
 * @company: webyun
 * @Copyright: Copyright (c) 2017
 * @version v1.0
 */
@Entity
@Table(name = "T_IP_LOG")
public class IpLog implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "ip")
	private String ip;
	
	@Column(name = "url")
	private String url;
	
	@Column(name = "method")
	private String method;
	
	@Column(name = "params")
	private String params;
	
	@Column(name = "user_name")
	private String userName;
	
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "create_time")
	private Date createTime;
	
	public IpLog() {	}

	public IpLog(String id, String ip, String url, String method, String params, String userName, String userId,
			Date createTime) {
		this.id = id;
		this.ip = ip;
		this.url = url;
		this.method = method;
		this.params = params;
		this.userName = userName;
		this.userId = userId;
		this.createTime = createTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
