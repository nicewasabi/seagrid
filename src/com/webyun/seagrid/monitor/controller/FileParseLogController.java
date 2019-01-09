package com.webyun.seagrid.monitor.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.webyun.seagrid.common.model.FileParseLogModel;
import com.webyun.seagrid.monitor.service.IFileParseLogService;


/**
 * @Project: seaGrid
 * @Title: FileParseLogController
 * @Description: 文件解析日志controller
 * @author: zhongsb
 * @date: 2017年9月18日
 * @company: webyun
 * @Copyright: Copyright (c) 2014
 * @UpdateUser：
 * @UpdateDescription：
 * @UpdateTime：上午9:45:13
 * @Version :V1.0
 */
@Controller
@RequestMapping("/fileParseLog")
public class FileParseLogController {
	
	@Autowired
	private IFileParseLogService service;
	
	
	@RequestMapping("/fileMonitor.do")
	public String fileMonitor(){
		return "monitor/fileParse";
	}
	
	/**
	* @Description: 根据条件获取所有文件解析过程监控日志
	* @Author ：zhongsb
	* @param startDate
	* @param endDate
	* @param orderBy
	* @param page
	* @param rows
	* @return   
	*/
	@RequestMapping("/getAllParseLog.do")
	@ResponseBody
	public Map<String, Object> getAllLog(@RequestParam("startDate") String startDate,@RequestParam("endDate") String endDate,@RequestParam("orderBy") String orderBy, @RequestParam("page") String page, @RequestParam("rows") String rows){
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<FileParseLogModel> list = service.getPageList(startDate, endDate, orderBy, page, rows);
		int list2 = service.getAllParseLog(startDate, endDate, orderBy);
		map.put("rows", list);
		map.put("total", list2);
		
		return map;
	} 

}
