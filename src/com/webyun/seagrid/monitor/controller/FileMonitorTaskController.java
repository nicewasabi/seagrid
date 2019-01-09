package com.webyun.seagrid.monitor.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.webyun.seagrid.common.model.FileMonitorTask;
import com.webyun.seagrid.monitor.service.IMonitorService;

/**
 * @Project: seaGrid
 * @Title: FileMonitorTaskController
 * @Description: 文件监控配置控制器
 * @author: zhongsb
 * @date: 2017年8月25日
 * @company: webyun
 * @Copyright: Copyright (c) 2014
 * @UpdateUser：
 * @UpdateDescription：
 * @UpdateTime：下午4:07:21
 * @Version :V1.0
 */
@Controller
@RequestMapping("/monitor")
public class FileMonitorTaskController {
	
	@Autowired
	private IMonitorService service;
	
	@RequestMapping("/getAllTask.do")
	@ResponseBody
	public List getAllTask(){
		List allTask = service.getAllTask();
		Map<String, Object> map = new HashMap<>();
		map.put("allTask", allTask);
		return allTask;
		
	}
	
	/**
	* @Description: 功能入口
	* @Author ：zhongsb
	* @return   
	*/
	@RequestMapping("/fileMonitor.do")
	public String fileMonitor(){
		return "monitor/fileMonitor";
	}
	@RequestMapping("/fileMonitorbak.do")
	public String fileMonitorbak(){
		return "monitor/fileMonitor-bak";
	}
	
	/**
	* @Description: 获取所有任务
	* @Author ：zhongsb
	* @param request
	* @param response
	* @return   
	*/
	@RequestMapping("/getAllMonitorTask.do")
	@ResponseBody
	public Map<String,Object> getAllMonitorTask(HttpServletRequest request, HttpServletResponse response){
		String like = request.getParameter("like");
		String orderBy2 = request.getParameter("orderBy2");
		String page = request.getParameter("page");//第page页
		String rows = request.getParameter("rows");//每页行数
		
		List<FileMonitorTask> list = null;
		
		int size = service.getCount(like);
		Map<String,Object> map = new HashMap<String, Object>();
		list = service.getPageList(like,orderBy2,page,rows);
		map.put("rows",list);
		map.put("total", size);
		
		return map;
	}
	
	/**
	* @Description: 删除任务
	* @Author ：zhongsb
	* @param request
	* @return   
	*/
	@RequestMapping("/deleteTask.do")
	@ResponseBody
	public Map<String,Object> deleteTask(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String, Object>();
		
		String id = request.getParameter("id");
		int count = service.deleteTaskById(id);
		if(count==1){
			map.put("success", "true");
		}else{
			map.put("success", "false");
		}
		return map;
	}
	
	/**
	* @Description: 根据ID获取任务对象
	* @Author ：zhongsb
	* @param request
	* @return   
	*/
	@RequestMapping("/getTaskById.do")
	@ResponseBody
	public Map<String, Object> getTaskById(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String, Object>();
		String id = request.getParameter("id");
		FileMonitorTask task = null;
		try {
			task = service.getTaskById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("task", task);
		return map;
	}
	
	/**
	* @Description: 根据ID更新任务对象
	* @Author ：zhongsb
	* @param request
	* @return   
	*/
	@RequestMapping("/updateTaskById.do")
	@ResponseBody
	public Map<String, Object> updateTaskById(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String, Object>();
		String id = request.getParameter("id");
		String fileName = request.getParameter("fileName");
		String filePath = request.getParameter("filePath");
		String endTime = request.getParameter("endTime");
		String deadLine = request.getParameter("deadLine");
		
		try {
			service.updateTaskById(id,fileName,filePath,endTime,deadLine);
			map.put("flag", "true");
		} catch (Exception e) {
			map.put("flag", "false");
			e.printStackTrace();
		}
		return map;
		
	}

	/**
	* @Description: 添加任务对象
	* @Author ：zhongsb
	* @param request
	* @return   
	*/
	@RequestMapping("/addTask.do")
	@ResponseBody
	public Map<String, Object> addTask(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String, Object>();
		String fileName = request.getParameter("fileName");
		String filePath = request.getParameter("filePath");
		String toPath = request.getParameter("toPath");
		String endTime = request.getParameter("endTime");
		String deadLine = request.getParameter("deadLine");
		try {
			service.addTask(fileName,filePath,toPath,endTime,deadLine);
			map.put("flag", "true");
		} catch (Exception e) {
			map.put("flag", "false");
			e.printStackTrace();
		}
		return map;
		
	}
}
