package com.webyun.seagrid.core.system.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webyun.seagrid.common.model.IpLog;
import com.webyun.seagrid.core.system.dao.IpLogDao;


@Service
public class IpLogService {

	@Autowired
	private IpLogDao ipLogDao;
	
	public List<IpLog> getPageList(Date beginTime, Date endTime, String ip, String userName, Integer from, Integer rows) {
		return this.ipLogDao.getPageList(beginTime, endTime, ip, userName, from, rows);
	}
	
	public Long getPageCount(Date beginTime, Date endTime, String ip, String userName) {
		return this.ipLogDao.getPageCount(beginTime, endTime, ip, userName);
	}
	
	public void insert(IpLog log) throws RuntimeException {
		this.ipLogDao.insert(log);
	}
	
	public void deleteForClearLog(Date date) throws RuntimeException {
		String hql = "delete from IpLog where createTime < ?";
		int update = this.ipLogDao.executeHQLForUpdate(hql, date);
	}
}
