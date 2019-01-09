package com.webyun.seagrid.monitor.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.webyun.seagrid.common.dao.impl.BaseDaoImpl;
import com.webyun.seagrid.common.model.FileParseTaskModel;
import com.webyun.seagrid.common.util.Assert;
import com.webyun.seagrid.monitor.dao.IFileParseTaskDao;

@Repository
public class FileParseTaskDaoImpl extends BaseDaoImpl<FileParseTaskModel, Serializable> implements IFileParseTaskDao {

	@Override
	public List<FileParseTaskModel> getCountTask(final String like) {
		
		return this.getHibernateTemplate().execute(new HibernateCallback<List<FileParseTaskModel>>() {

			@Override
			public List<FileParseTaskModel> doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer();
				hql.append("from FileParseTaskModel where 1=1 ");
				
				if(Assert.isNotEmpty(like)){
					hql.append(" and lower(monitorpath) like lower('%"+like.toLowerCase()+"%')");
				}
				Query query = null;
				try {
					query = session.createQuery(hql.toString());
					List list = query.list();
					return query==null?null:query.list();
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
				
				
			}
			
		});
		
	}

	@Override
	public List<FileParseTaskModel> getPageList(final String like, final String orderBy, final int startRow, final int endRow) {
		return this.getHibernateTemplate().execute(new HibernateCallback<List<FileParseTaskModel>>() {

			@Override
			public List<FileParseTaskModel> doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer();
				hql.append("from FileParseTaskModel where 1=1 ");
				
				if(Assert.isNotEmpty(like)){
					hql.append(" and lower(monitorpath) like lower('%"+like.toLowerCase()+"%')");
				}
				
				if(Assert.isNotEmpty(orderBy)){
					hql.append(" "+orderBy);
				}
				Query query = null;
				try {
					query = session.createQuery(hql.toString());
					query.setFirstResult(startRow);
					query.setMaxResults(endRow-startRow);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return query==null?null:query.list();
			}
			
		});
	}

	@Override
	public List<FileParseTaskModel> getByShellCode(final String shellCode) {
		return this.getHibernateTemplate().execute(new HibernateCallback<List<FileParseTaskModel>>() {

			@Override
			public List<FileParseTaskModel> doInHibernate(Session session)
					throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer();
				hql.append("from FileParseTaskModel where 1 = 1");
				if(Assert.isNotEmpty(shellCode)){
					hql.append(" and shellCode = "+shellCode);
				}
				try {
					Query query = session.createQuery(hql.toString());
					return query==null?null:query.list();
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		});
	}

}
