package com.webyun.seagrid.common.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public interface BaseDao<T,PK extends Serializable> {	
	
	void insert(T t) throws RuntimeException;
	
	void update(T t) throws RuntimeException;
	
	void delete(T t) throws RuntimeException;
	
	T getById(PK pk) throws RuntimeException;
	
	List<T> getAll() throws RuntimeException; 
	
	List executeSQL(String sql, Object... values) throws RuntimeException;
	
	List<T> executeHQL(String hql, Object... values) throws RuntimeException;
	/**
	 * 批量执行数据更新，但时不能进行数据删除
	 */
	int bulkUpdate(String hql, Object... values);
	
	public List<T[]> queryForList(String sql, Map<String, ?> values);
	
	public T findUnique(final String hql, final Map<String, ?> values);
	
	public List<T> findList(String sql, Map<String, ?> values);

	int executeHQLForUpdate(String hql, Object[] values)
			throws RuntimeException;
}
