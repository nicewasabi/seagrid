package com.webyun.seagrid.cache;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.webyun.seagrid.common.model.FileMonitorTask;
import com.webyun.seagrid.common.util.JsonHelper;
import com.webyun.seagrid.monitor.service.impl.MonitorServiceImpl;


/**
 * @Project: seaGrid
 * @Title: CacheManage
 * @Description: 
 * @author: zhongsb
 * @date: 2017年8月17日
 * @company: webyun
 * @Copyright: Copyright (c) 2014
 * @UpdateUser：
 * @UpdateDescription：
 * @UpdateTime：上午11:03:59
 * @Version :V1.0
 */
@Repository
public class CacheManage {
	@Autowired
	private MonitorServiceImpl service = new MonitorServiceImpl();
	//加载指定路径的xml
    private static CacheManager manager = new CacheManager(CacheManage.class.getResourceAsStream("classpath:ehcache.xml"));
	private static Cache cache = null; 
    static {  
    	cache = manager.getCache("sampleCache1");  
    }  
	/**
	* @Description: 获取缓存对象
	* @Author ：zhongsb
	* @return   
	*/
	public static Cache getCache(){
		return cache;
	}
	
	/**
	* @Description: 添加缓存对象
	* @Author ：zhongsb
	* @param key
	* @param value   
	*/
	public void put(String key, String value) {
		Element element = new Element(key,value);
		cache.put(element);
	}
	
	public void put(FileMonitorTask task){
		Element e1 = new Element("task-"+task.getFileName(), JsonHelper.getJson(task));
		cache.put(e1);
	}
	
	/**
	* @Description: 删除单一缓存对象
	* @Author ：zhongsb
	* @param key   
	*/
	public void remove(String key){
		cache.remove(key);
	}
	
	/**
	* @Description: 删除所有缓存对象
	* @Author ：zhongsb   
	*/
	public void removeAll(){
		cache.removeAll();
	}
	
	/**
	* @Description: 获取缓存对象
	* @Author ：zhongsb
	* @param key
	* @return   
	*/
	public Element getString(String key){
		return cache.get(key);
	}
	
	public static void main (String[] args){
//		CacheManager manager = new CacheManager(CacheManage.class.getResourceAsStream("classpath:ehcache.xml"));
//		Cache cache = manager.getCache("sampleCache1");  
		FileMonitorTask task = new FileMonitorTask();
		task.setId(1);
		task.setBusinessCode("0800");
		task.setEndTime("04:00");
		Element e1 = new Element("task1", JsonHelper.getJson(task));
//		System.out.println(JsonHelper.getJson(task));
		cache.put(e1);
		Element element = cache.get("task1");
		System.out.println(element.toString());
		System.out.println(JSONObject.fromObject(element.getValue().toString()));
//		JSONObject fromObject = JSONObject.fromObject(element.getValue().toString());
//		String endTime = (String) fromObject.get("endTime");
//		System.out.println(endTime);//获取迟到时间
		
		List<String> keys = cache.getKeys();
		System.out.println(keys.size());
		for (String key : keys) {  
    		System.out.println(key + "," + cache.get(key)); 
    		JSONObject fromObject = JSONObject.fromObject(cache.get(key).getObjectValue().toString());
    		String endTime = (String) fromObject.get("endTime");
    		System.out.println(endTime);
    	}
		Date date = null;
		date = new Date(2017, 8, 21, 16, 58, 0);
		boolean after = date.after(date);
		if(after){
			System.out.println("16:58le");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd hh:mm:ss");
		String format = sdf.format(date);
		System.out.println(format);
	}
}
