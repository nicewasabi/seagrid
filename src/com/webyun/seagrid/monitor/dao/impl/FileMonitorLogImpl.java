package com.webyun.seagrid.monitor.dao.impl;


import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.webyun.seagrid.common.dao.impl.BaseDaoImpl;
import com.webyun.seagrid.common.model.FileMonitorLog;
import com.webyun.seagrid.common.util.Assert;
import com.webyun.seagrid.monitor.dao.IFileMonitorLogDao;

@Repository
public class FileMonitorLogImpl extends BaseDaoImpl<FileMonitorLog, Integer> implements IFileMonitorLogDao{

	public List<FileMonitorLog[]>  getByFile(File file) {
		List<FileMonitorLog[]> list = new ArrayList<FileMonitorLog[]>();
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "select FILENAME,filepath,endtime,deadline,isnextday from FILEMONITORLOG where fileName=? and filePath=? and type=1";
		map.put("fileName", file.getName());
		map.put("filePath", file.getParentFile());
		list = queryForList(sql, map);
		
		return  list;
	}

	@Override
	public void updateFileMonitorLog(FileMonitorLog fileMonitorLog) {
		update(fileMonitorLog);
	}

	@Override
	public void insertFileMonitorLog(FileMonitorLog fileMonitorLog) {
		insert(fileMonitorLog);
	}

	@Override
	public List<FileMonitorLog> getAllMonitorLog(String startTime,
			String endTime,  String orderBy) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select t.*, rownum from FILEMONITORLOG t "
				+ "where yyyymmdd between :startTime and :endTime  ");
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		
		if(Assert.isNotEmpty(orderBy)){
			sql.append(" "+orderBy);
		}
		
		List<FileMonitorLog> list = null;
		try {
			list = findList(sql.toString(),map);
			
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<FileMonitorLog> getPageList(final String startTime, final String endTime,
			 final String orderBy, final String page, final String rows) {
		return this.getHibernateTemplate().execute(new HibernateCallback<List<FileMonitorLog>>() {

			@SuppressWarnings("unchecked")
			@Override
			public List<FileMonitorLog> doInHibernate(Session session) throws HibernateException, SQLException {
				
				StringBuffer hql = new StringBuffer();
				int startRow = Integer.parseInt(page);
				int endRow = Integer.parseInt(rows);
				startRow = (startRow-1)*endRow;
				endRow = startRow + endRow;
				hql.append("from FileMonitorLog where 1=1  ");
				if(Assert.isNotEmpty(startTime) && Assert.isNotEmpty(endTime)){
					hql.append(" and yyyymmdd between "+startTime+" and "+endTime);
				}
				
				if(Assert.isNotEmpty(orderBy)){
					hql.append(" "+orderBy);
				}
				
				try {
					Query query = session.createQuery(hql.toString());
					
					//分页查询
					if(Assert.isNotEmpty(startRow) || Assert.isNotEmpty(endRow)){
						query.setFirstResult(startRow);
						query.setMaxResults(Integer.parseInt(rows));
					}
					
					return query.list();
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		});
	}

	@Override
	public List<FileMonitorLog> getLogByStatus(final int []status, final String yyyymmdd, final String businessCode) {
		return this.getHibernateTemplate().execute(new HibernateCallback<List<FileMonitorLog>>(){

			@Override
			public List<FileMonitorLog> doInHibernate(Session session)
					throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer();
				hql.append("from FileMonitorLog where 1=1 ");
				if(Assert.isNotEmpty(status)){
					hql.append(" and status in (");
					for(int i=0;i<status.length;i++){
						if(i==status.length-1){
							hql.append(status[i]+")");
						}else{
							hql.append(status[i]+",");
						}
					}
				}
				if(Assert.isNotEmpty(yyyymmdd)){
					hql.append(" and yyyymmdd="+yyyymmdd);
				}
				if(Assert.isNotEmpty(businessCode)){
					hql.append(" and businessCode="+businessCode);
				}
				try {
					Query list = session.createQuery(hql.toString());
					return list.list();
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		});
	}

	@Override
	public FileMonitorLog getNewLog(final int[] status) {
		
		return this.getHibernateTemplate().execute(new HibernateCallback<FileMonitorLog>(){

			@Override
			public FileMonitorLog doInHibernate(Session session)
					throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer();
				hql.append("from FileMonitorLog where 1=1 ");
				if(Assert.isNotEmpty(status)){
					hql.append(" and status in (");
					for(int i=0;i<status.length;i++){
						if(i==status.length-1){
							hql.append(status[i]+")");
						}else{
							hql.append(status[i]+",");
						}
					}
				}
				hql.append("order by optime desc ");
				try {
					Query list = session.createQuery(hql.toString());
					return (FileMonitorLog) list.list().get(0);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		});
	}
	


}
