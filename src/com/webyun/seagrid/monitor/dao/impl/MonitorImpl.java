package com.webyun.seagrid.monitor.dao.impl;


import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.webyun.seagrid.common.dao.impl.BaseDaoImpl;
import com.webyun.seagrid.common.model.FileMonitorTask;
import com.webyun.seagrid.common.util.Assert;
import com.webyun.seagrid.monitor.dao.IMonitorDao;

@Repository
public class MonitorImpl extends BaseDaoImpl<FileMonitorTask, Integer> implements IMonitorDao{
	
	private static Logger log = Logger.getLogger(MonitorImpl.class);
	
	public List<FileMonitorTask> getTaskByBusinessCode(final String businessCode){
		return this.getHibernateTemplate().execute(new HibernateCallback<List<FileMonitorTask>>(){

			@Override
			public List<FileMonitorTask> doInHibernate(Session session)
					throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer();
				hql.append("from FileMonitorTask where type=1");
				if(Assert.isNotEmpty(businessCode)){
					hql.append(" and businessCode="+businessCode);
				}
				return session.createQuery(hql.toString()).list();
			}
		});
	}
	
	public List<FileMonitorTask> getAllMonitorTask(){
		return this.getHibernateTemplate().execute(new HibernateCallback<List<FileMonitorTask>>(){

			@Override
			public List<FileMonitorTask> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hql = "from FileMonitorTask where type=1";
				
				return session.createQuery(hql).list();
			}
		});
		
	/*	List<FileMonitorTask> allMonitorTask = null;
		try {
			allMonitorTask = getAll();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}

	public List getByFile(File file) {
/*		List<Object[]> list = new ArrayList<Object[]>();
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "select FILENAME,filepath,endtime,deadline,isnextday from FTPFILEMONITORTASK where fileName=? and filePath=? and type=1";
		map.put("fileName", file.getName());
		map.put("filePath", file.getParentFile());
		list = executeSQL(sql, map);*/
		
		return  null;
	}

	public int getCount(final String like) {
		/*Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from FTPFILEMONITORTASK ");
		if(Assert.isNotEmpty(like)){
			sql.append("where filePath like :filePath or fileName like :fileName");
		}
		
		map.put("filePath", "'%"+like.toLowerCase()+"%'");
		map.put("fileName", "'%"+like.toLowerCase()+"%'");
		List<FileMonitorTask> list = null;
		list = findList(sql.toString(),map);
		System.out.println(sql.toString());
		if(Assert.isEmpty(list)){
			return 0;
		}else {
			return list.size();
		}*/
		return this.getHibernateTemplate().execute(new HibernateCallback<Integer>(){
			@Override
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer();
				hql.append("from FileMonitorTask  ");
				if(Assert.isNotEmpty(like)){
					hql.append(" where lower(fileName) like '%"+like.toLowerCase()+"%' or lower(filePath) like '%"+like.toLowerCase()+"%' ");
				}
				Query query = null;
				try {
					query = session.createQuery(hql.toString());
				} catch (NumberFormatException e) {
					log.error("类型转化异常");
					e.printStackTrace();
				}
				return query==null?0:(query.list().size());
			}
			
		});
		
	}

	@Override
	public void updateLog() {
		
	}

	public List<FileMonitorTask> getPageList(final String like, final String orderBy2,
			final String page, final String rows) {
		return this.getHibernateTemplate().execute(new HibernateCallback<List<FileMonitorTask>>(){
			@Override
			public List<FileMonitorTask> doInHibernate(Session session)
					throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer();
				hql.append("from FileMonitorTask  ");
				if(Assert.isNotEmpty(like)){
					hql.append(" where lower(fileName) like lower('%"+like.toLowerCase()+"%') or lower(filePath) like lower('%"+like.toLowerCase()+"%') ");
				}
				if(Assert.isNotEmpty(orderBy2)){
					hql.append(orderBy2);
				}
				Query query = null;
				try {
					query = session.createQuery(hql.toString());
					if(Assert.isNotEmpty(page) && Assert.isNotEmpty(rows)){
						int pageNum = Integer.parseInt(page);
						int rowsNum = Integer.parseInt(rows);
						query.setFirstResult((pageNum-1)*rowsNum);
						query.setMaxResults(rowsNum);
					}
				} catch (NumberFormatException e) {
					log.error("数字格式化异常");
					e.printStackTrace();
				}
				return query==null?null:query.list();
			}
			
		});
	}

	public int deleteTaskById(String id) {
		List<Object[]> list = new ArrayList<Object[]>();
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "delete from FTPFILEMONITORTASK where id=:id";
		map.put("id", id);
		int count = executeSQL(sql, map);
		return count;
	}

	public FileMonitorTask getTaskById(String id) {
		StringBuffer hql = new StringBuffer();
		hql.append("from FileMonitorTask where 1=1 ");
		if(Assert.isNotEmpty(id)){
			hql.append("and id ="+id);
		}
		FileMonitorTask task = null;
		try {
			task = this.findUnique(hql.toString());
		} catch (Exception e) {
			log.error("数字格式化异常");
			e.printStackTrace();
		}
		 return task;
	}


}
