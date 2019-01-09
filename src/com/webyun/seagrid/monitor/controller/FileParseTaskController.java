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
import com.webyun.seagrid.common.model.FileParseTaskModel;
import com.webyun.seagrid.monitor.service.IFileParseLogService;
import com.webyun.seagrid.monitor.service.IFileParseTaskService;


/**
 * @Project: seaGrid
 * @Title: FileParseTaskController
 * @Description: 文件解析过程配置controller
 * @author: zhongsb
 * @date: 2017年9月18日
 * @company: webyun
 * @Copyright: Copyright (c) 2014
 * @UpdateUser：
 * @UpdateDescription：
 * @UpdateTime：上午9:44:54
 * @Version :V1.0
 */
@Controller
@RequestMapping("/fileParseTask")
public class FileParseTaskController {

	@Autowired
	private IFileParseTaskService service;

	@RequestMapping("/getAllParseTask.do")
	@ResponseBody
	public Map<String, Object> getAllParseTask(@RequestParam("like") String like, @RequestParam("orderBy") String orderBy, @RequestParam("page") String page, @RequestParam("rows") String rows){
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<FileParseTaskModel> list = service.getPageList(like, orderBy, page, rows);
		
		int count = service.getCountTask(like);
		
		map.put("rows", list);
		map.put("total", count);
		
		return map;
	} 
	
}
