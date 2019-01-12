/**  

 * Copyright © 2019webyun. All rights reserved.

 *

 * @Title: Section7DaysController.java

 * @Prject: seaGrid

 * @Package: com.webyun.seagrid.seaFogFeature.controller

 * @Description: TODO

 * @author: wasabi  

 * @date: 2019年1月10日 下午3:54:13

 * @version: V1.0  

 */
package com.webyun.seagrid.seaFogFeature.controller;

import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.webyun.seagrid.common.util.FileOperationUtil;
import com.webyun.seagrid.objectiveMethodProduct.util.ObjectiveMethodProductUtil;
import com.webyun.seagrid.seaFogFeature.util.SeaFogFeatureUtil;

/**

 * @ClassName: Section7DaysController

 * @Description: TODO

 * @author: wasabi

 * @date: 2019年1月10日 下午3:54:13


 */
@Controller
@RequestMapping("/seaFogFeature/section7days")
public class Section7DaysController {
	
	private Logger log = Logger.getLogger(Section7DaysController.class);
	
	
	
	@RequestMapping("/main.do")
	public String toMain() {
		return "/seaFogFeature/section7days";
	}
	
	
	@RequestMapping("/validImage.do")
	@ResponseBody
	public Boolean validImage(String datetime, String hour, String location, HttpServletResponse response) {
		try {
			// 获取对应日期时间的图片所在路径
			String path = SeaFogFeatureUtil.getSeaFogSection7DaysImagePath(datetime, hour, location);

			if (path != null) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	@RequestMapping("/showImage.do")
	public void showImage(String datetime, String timelimit, String featuretype, HttpServletResponse response){
		response.setContentType("multipart/form-data");
		
		try {
			// 获取对应日期时间的图片所在路径
			String path = SeaFogFeatureUtil.getSeaFogSection7DaysImagePath(datetime, timelimit, featuretype);
			
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
