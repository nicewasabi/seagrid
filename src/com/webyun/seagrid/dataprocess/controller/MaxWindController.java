package com.webyun.seagrid.dataprocess.controller;

import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.webyun.seagrid.common.util.DatetimeUtil;
import com.webyun.seagrid.common.util.FileOperationUtil;
import com.webyun.seagrid.dataprocess.util.GradsDataPathUtil;

/**
 * @Project: seaGrid
 * @Title: MaxWindController
 * @Description: GrADS10米阵风数据处理展示控制器
 * @Author: songwj
 * @Date: 2017-12-4 下午2:23:42
 * @Company: webyun
 * @Copyright: Copyright (c) 2017 Webyun. All Rights Reserved.
 * @Version v1.0
 */
@Controller
@RequestMapping("/maxWind")
public class MaxWindController {
	
	private Logger log = Logger.getLogger(MaxWindController.class);
	
	/**
	 * @Description: 进入数据展示主界面
	 * @author: songwj
	 * @date: 2017-9-22 下午3:06:16
	 * @return
	 */
	@RequestMapping("/main.do")
	public String toMain() {
		return "/dataProcess/maxWind";
	}
	
	/**
	 * @Description: 判断指定时间的图像文件是否存在
	 * @author: songwj
	 * @date: 2017-10-23 下午4:48:28
	 * @param datetime yyyyMMddHH，北京时
	 * @param timelimit 时效
	 * @param response
	 * @return
	 */
	@RequestMapping("/validImage.do")
	@ResponseBody
	public Boolean validImage(String datetime, String timelimit, HttpServletResponse response) {
		try {
			// 北京时转世界时
			String universalTime = DatetimeUtil.beijingTime2universalTime(datetime, DatetimeUtil.YYYYMMDDHH);
			// 获取对应日期时间的图片所在路径
			String path = GradsDataPathUtil.get10mMaxWindImagePath(universalTime, timelimit);

			if (path != null) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	/**
	 * @Description: 图像展示
	 * @author: songwj
	 * @date: 2017-10-23 下午4:52:57
	 * @param datetime yyyyMMddHH，北京时
	 * @param timelimit 时效
	 * @param response
	 */
	@RequestMapping("/showImage.do")
	public void showImage(String datetime, String timelimit, HttpServletResponse response){
		response.setContentType("multipart/form-data");
		
		try {
			// 北京时转世界时
			String universalTime = DatetimeUtil.beijingTime2universalTime(datetime, DatetimeUtil.YYYYMMDDHH);
			// 获取对应日期时间的图片所在路径
			String path = GradsDataPathUtil.get10mMaxWindImagePath(universalTime, timelimit);
			
			if (path != null) {
				// 将指定路径下的图片输出到页面
				FileOperationUtil.writeFile(new FileInputStream(path), response.getOutputStream(), 10240);
			}
		} catch (IOException e) {
			log.error("图像获取失败：" + e.getMessage());
			e.printStackTrace();
		}
	}
	
}
