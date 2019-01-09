package com.webyun.seagrid.core.system.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.webyun.seagrid.common.dao.impl.BaseDaoImpl;
import com.webyun.seagrid.common.model.IpLog;
import com.webyun.seagrid.common.util.Assert;


@Repository
public class IpLogDao extends BaseDaoImpl<IpLog, Integer> {
	
	public List<IpLog> getPageList(final Date beginTime, final Date endTime, final String ip, final String userName, final Integer from, final Integer rows) {
		return this.getHibernateTemplate().execute(new HibernateCallback<List<IpLog>>() {

			@SuppressWarnings("unchecked")
			@Override
			public List<IpLog> doInHibernate(Session session) throws HibernateException, SQLException {
				
				StringBuffer hql = new StringBuffer();
				
				hql.append("from IpLog where 1=1 ");
				
				//时间范围
				if(Assert.isNotEmpty(beginTime) && Assert.isNotEmpty(endTime)){
					hql.append(" and createTime between ? and ? ");
				}

				//ip
				if(Assert.isNotEmpty(ip)){
					hql.append(" and ip=? ");
				}
				
				//用户
				if(Assert.isNotEmpty(userName)){
					hql.append(" and userName=? ");
				}
				
				hql.append(" order by createTime desc ");
				
				try {
					Query query = session.createQuery(hql.toString());
					//绑定参数
					int i = 0;
					//时间范围
					if(Assert.isNotEmpty(beginTime) && Assert.isNotEmpty(endTime)){
						query.setDate(i, beginTime);i++;
						query.setDate(i, endTime);i++;
					}

					//ip
					if(Assert.isNotEmpty(ip)){
						query.setString(i, ip);
						i++;
					}
					
					//用户
					if(Assert.isNotEmpty(userName)){
						query.setString(i, userName);
					}
					//分页查询
					if(Assert.isNotEmpty(from) || Assert.isNotEmpty(rows)){
						query.setFirstResult(from);
						query.setMaxResults(rows);
					}
					return query.list();
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		});
	}

	public Long getPageCount(Date beginTime, Date endTime, String ip, String userName) {
		StringBuffer hql = new StringBuffer();
		hql.append("select count(*) from IpLog where 1=1 ");
		//保存查询的参数
		List<Object> os = new ArrayList<>();
				
		//时间范围
		if(Assert.isNotEmpty(beginTime) && Assert.isNotEmpty(endTime)){
			hql.append(" and createTime between ? and ? ");
			os.add(beginTime);
			os.add(endTime);
		}

		//ip
		if(Assert.isNotEmpty(ip)){
			hql.append(" and ip=? ");
			os.add(ip);
		}

		//用户
		if(Assert.isNotEmpty(userName)){
			hql.append(" and userName=? ");
			os.add(userName);
		}
		
		List find = null;
		if(os.size() == 0) {
			find = this.getHibernateTemplate().find(hql.toString());
		} else {
			Object[] args = os.toArray(new Object[os.size()]);
			find = this.getHibernateTemplate().find(hql.toString() ,args);
		}
		Long temp = (Long)find.get(0);
		
		return temp.longValue();
	}
	
}