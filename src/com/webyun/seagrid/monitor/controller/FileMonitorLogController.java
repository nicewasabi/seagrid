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

import com.webyun.seagrid.common.model.FileMonitorLog;
import com.webyun.seagrid.monitor.service.IFileMonitorLogService;


/**
 * @Project: seaGrid
 * @Title: FileMonitorLogController
 * @Description: ftp日志controller
 * @author: zhongsb
 * @date: 2017年9月18日
 * @company: webyun
 * @Copyright: Copyright (c) 2014
 * @UpdateUser：
 * @UpdateDescription：
 * @UpdateTime：上午9:45:25
 * @Version :V1.0
 */
@Controller
@RequestMapping("/filelog")
public class FileMonitorLogController {
	
	@Autowired
	private IFileMonitorLogService service;
	
	
	public FileMonitorLog logMessage(){
		FileMonitorLog log = service.getNewLog();
		return log;
	}
	
	/**
	* @Description: 根据条件获取所有ftp监控日志
	* @Author ：zhongsb
	* @param request
	* @param response
	* @return   
	*/
	@RequestMapping("/getAllMonitorLog.do")
	@ResponseBody
	public Map<String, Object> getAllMonitorLog(HttpServletRequest request, HttpServletResponse response){
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String orderBy = request.getParameter("orderBy");
		String page = request.getParameter("page");//第page页
		String rows = request.getParameter("rows");//每页行数
		
		List<FileMonitorLog> list = service.getPageList(startTime, endTime, orderBy, page, rows);
		List<FileMonitorLog> list2 = service.getAllMonitorLog(startTime, endTime, orderBy);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("total", list2.size());
		return map;
	} 
	

}
