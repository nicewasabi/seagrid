package com.webyun.seagrid.seaWindProbability.controller;

import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.webyun.seagrid.common.util.FileOperationUtil;
import com.webyun.seagrid.seaWindProbability.util.WindProbabilityUtil;

/**
 * @Project: seaGrid
 * @Title: NPController
 * @Description: 北太平洋数据展示控制器
 * @Author: songwj
 * @Date: 2017-12-5 下午7:36:47
 * @Company: webyun
 * @Copyright: Copyright (c) 2017 Webyun. All Rights Reserved.
 * @Version v1.0
 */
@Controller
@RequestMapping("/np")
public class NPController {
	
	private Logger log = Logger.getLogger(NPController.class);
	
	/**
	 * @Description: 进入10米8级大风概率数据展示主界面
	 * @author: songwj
	 * @date: 2017-9-22 下午3:06:16
	 * @return
	 */
	@RequestMapping("/main.do")
	public String toMain() {
		return "/seaWindProbability/np";
	}
	
	/**
	 * @Description: 进入8级大风确定性预报主界面
	 * @Author: songwj
	 * @Date: 2017-12-13 上午11:04:02
	 * @return
	 */
	@RequestMapping("/determineForecastMain.do")
	public String toDetermineForecastMain() {
		return "/determineForecast/np";
	}
	
	/**
	 * @Description: 判断指定时间的图像文件是否存在
	 * @Author: songwj
	 * @Date: 2017-12-5 下午7:53:52
	 * @param datetime 世界时，如：2017120400
	 * @param timelimit 时效
	 * @param windType 风类型：average_dispersion、probability、percent、stamp
	 * @param response
	 * @return
	 */
	@RequestMapping("/validImage.do")
	@ResponseBody
	public Boolean validImage(String datetime, String timelimit, String windType, HttpServletResponse response) {
		try {
			// 获取对应日期时间的图片所在路径
			String path = WindProbabilityUtil.getNPWindImagePath(datetime, timelimit, windType);

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
	 * @Author: songwj
	 * @Date: 2017-12-5 下午8:01:17
	 * @param datetime 世界时，如：2017120400
	 * @param timelimit 时效
	 * @param windType 风类型：average_dispersion、probability、percent、stamp
	 * @param response
	 */
	@RequestMapping("/showImage.do")
	public void showImage(String datetime, String timelimit, String windType, HttpServletResponse response){
		response.setContentType("multipart/form-data");
		
		try {
			// 获取对应日期时间的图片所在路径
			String path = WindProbabilityUtil.getNPWindImagePath(datetime, timelimit, windType);
			
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
