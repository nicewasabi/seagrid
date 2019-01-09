package com.webyun.seagrid.monitor.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.webyun.seagrid.common.dao.impl.BaseDaoImpl;
import com.webyun.seagrid.common.model.FileParseLogModel;
import com.webyun.seagrid.common.util.Assert;
import com.webyun.seagrid.monitor.dao.IFileParseLogDao;

@Repository
public class FileParseLogImpl extends BaseDaoImpl<FileParseLogModel, Integer> implements IFileParseLogDao {



	@Override
	public List<Object[]> getAllParseLog(final String startDate, final String endDate, String orderBy) {
		return this.getHibernateTemplate().execute(new HibernateCallback<List<Object[]>>() {

			@Override
			public List<Object[]> doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer();
				hql.append("select monitorpath from FileParseLogModel where 1=1 ");
				if(Assert.isNotEmpty(startDate) && Assert.isNotEmpty(endDate)){
					hql.append(" and yyyymmdd between "+startDate+" and "+endDate);
				}
				Query query = null;
				try {
					query = session.createQuery(hql.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				return query==null?null:query.list();
			}
	});

	}
	


	@Override
	public List<FileParseLogModel> getPageList(final String startDate, final String endDate,
			final String orderBy, final int startRow, final int endRow) {
		
		return this.getHibernateTemplate().execute(new HibernateCallback<List<FileParseLogModel>>() {

			@Override
			public List<FileParseLogModel> doInHibernate(Session session)
					throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer();
				hql.append(" from FileParseLogModel t where 1=1 ");
				
				if(Assert.isNotEmpty(startDate) && Assert.isNotEmpty(endDate)){
					hql.append(" and t.yyyymmdd between "+startDate+" and "+endDate);
				}
				
				if(Assert.isNotEmpty(orderBy)){
					hql.append(" "+orderBy);
				}
				
				Query query = null;
				try {
					query = session.createQuery(hql.toString());
					if(Assert.isNotEmpty(startRow) || Assert.isNotEmpty(endRow)){
						query.setFirstResult(startRow);
						query.setMaxResults(endRow-startRow);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				//分页查询
				return query==null?null:query.list();
			}
		});
	}



	@Override
	public void save(FileParseLogModel parseLog) {
		try {
			insert(parseLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}