package com.webyun.seagrid.core.system.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.webyun.seagrid.common.model.IpLog;
import com.webyun.seagrid.common.util.Assert;
import com.webyun.seagrid.common.util.DatetimeUtil;
import com.webyun.seagrid.core.system.service.IpLogService;

/**
 * @Project: dataShare
 * @Title: IpLogController
 * @Description: 系统日志
 * @author: zhangx
 * @date: 2017年7月19日 上午9:16:03
 * @company: webyun
 * @Copyright: Copyright (c) 2017
 * @version v1.0
 */
@Controller
@RequestMapping("/ipLog")
public class IpLogController {

	@Autowired
	private IpLogService ipLogService;
	
	@RequestMapping("/page.do")
	@ResponseBody
	public Map<String, Object> getPage(String beginTime, String endTime, String ip, String userName,Integer page,Integer rows) {
		int from = (page - 1) * rows;
		//处理时间格式
		Date beginDate = null;
		Date endDate = null;
		if(Assert.isNotEmpty(beginTime) && Assert.isNotEmpty(endTime) ) {
			beginDate = DatetimeUtil.parse(beginTime + " 00:00:00", DatetimeUtil.YYYY_MM_DD_HH_MM_SS );
			endDate = DatetimeUtil.parse(endTime + " 23:59:59", DatetimeUtil.YYYY_MM_DD_HH_MM_SS );
		}
		
		List<IpLog> pageList = ipLogService.getPageList(beginDate, endDate, ip, userName, from, rows);
		
		Long count = ipLogService.getPageCount(beginDate, endDate, ip, userName);
		
		Map<String, Object> res = new HashMap<>();
		res.put("rows", pageList);
		res.put("total", count);
		
		return res;
	}
}
