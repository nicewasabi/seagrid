package com.webyun.seagrid.common.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.webyun.seagrid.common.dao.BaseDao;



@Repository
@Scope("prototype")
@SuppressWarnings("rawtypes")
public class BaseDaoImpl<T, PK extends Serializable> extends HibernateDaoSupport implements BaseDao<T, PK> {

	
	@Autowired  
    public void setSessionFactoryOverride(SessionFactory sessionFactory)  
    {  
  
        super.setSessionFactory(sessionFactory);  
    }  
	
	
	private Class entityClass;
	public void addSessionFactory(SessionFactory factory) {
		super.setSessionFactory(factory);
	}
	
	@SuppressWarnings("unchecked")
	public BaseDaoImpl(){
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
		entityClass = (Class<T>)params[0];
	}
	
	public Class getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class entityClass) {
		this.entityClass = entityClass;
	}

	@Override
	public void insert(T t) throws RuntimeException {
		this.getHibernateTemplate().save(t);
	}

	@Override
	public void update(T t) throws RuntimeException {
		this.getHibernateTemplate().update(t);
	}

	@Override
	public void delete(T t) throws RuntimeException {
		this.getHibernateTemplate().delete(t);
	}
	@SuppressWarnings("unchecked")
	@Override
	public T getById(PK pk) {
		try {
			T t = (T) this.getHibernateTemplate().get(entityClass, pk);
			return t;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> getAll() {
		try {
			List<T> li = this.getHibernateTemplate().find("from "+this.entityClass.getSimpleName());
			return li;
		} catch (Exception e ) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List executeSQL(final String sql, final Object... values) throws RuntimeException {
		return this.getHibernateTemplate().execute(new HibernateCallback<List>() {
			@Override
			public List doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery sqlQuery = session.createSQLQuery(sql);
				if(values != null) {
					for (int i = 0; i < values.length; i++) {
						sqlQuery.setParameter(i, values[i]);
					}
				}
				return sqlQuery.list();
			}
		});
	}

	@Override
	public List<T> executeHQL(final String hql, final Object... values) throws RuntimeException {
		return this.getHibernateTemplate().execute(new HibernateCallback<List<T>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<T> doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				if(values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				return query.list();
			}
		});
	}

	/**
	 * @Title: 
	 * @param hql
	 * @param values
	 * @return
	 * @author: zhangx
	 * @date: 2017年7月26日 下午3:11:32
	 * @version v1.0
	 */
	@Override
	public int bulkUpdate(String hql, Object... values){
		return this.getHibernateTemplate().bulkUpdate(hql, values);
	}
	
	public List<T[]> queryForList(final String queryString, final Map<String, ?> values) {
		Session session = getSession();
		SQLQuery query = session.createSQLQuery(queryString);
		try {
			if (values != null) {
				query.setProperties(values);
			}
			return (List<T[]>)query.list();
		} catch (Exception e) {
				e.printStackTrace();
		}finally{
			if(session!=null){
				session.close();
			}
		}
		return null;
	}
	/* 查询底层操作 */
	public Query createQuery(final String queryString, final Object... values) {
		Query query = getSession().createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		//关闭session
		getSession().close();
		return query;
	}
	public List<T> find(final String hql, final Map<String, ?> values) {
		return createQuery(hql, values).list();
	}

	public T findUnique(final String hql, final Object... values) {
		return (T) createQuery(hql, values).uniqueResult();
	}

	public T findUnique(final String hql, final Map<String, ?> values) {
		return (T) createQuery(hql, values).uniqueResult();
	}
	
	public List<T> findList(String clazz, String sql, Map<String, ?> values) {
		return createSQLQuery(sql, values).addEntity(clazz).list();
	}
	public SQLQuery createSQLQuery(final String queryString,
			final Map<String, ?> values) {
		Session session = getSession();
		SQLQuery query=null;
		try {
			 query = session.createSQLQuery(queryString);
			if (values != null) {
				query.setProperties(values);
			}
			return query;
		} catch (Exception e) {
				e.printStackTrace();
		}
		if(session!=null){
			session.close();
		}
		return null;
		
	}
	@Override
	public int executeHQLForUpdate(final String hql, final Object... values) throws RuntimeException {
		return this.getHibernateTemplate().execute(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				if(values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				return query.executeUpdate();
			}
		});
	}
	public List<T> findList(String sql, Map<String, ?> values) {
		return createSQLQuery(sql, values).list();
	}
	public int executeSQL(String sql, Map<String, ?> values) {
		return createSQLQuery(sql, values).executeUpdate();
	}
}
