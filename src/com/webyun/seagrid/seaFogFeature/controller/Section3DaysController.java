/**  

 * Copyright © 2019webyun. All rights reserved.

 *

 * @Title: SeaFogFeatureController.java

 * @Prject: seaGrid

 * @Package: com.webyun.seagrid.seaFogFeature.controller

 * @Description: TODO

 * @author: wasabi  

 * @date: 2019年1月10日 下午3:46:39

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
import com.webyun.seagrid.objectiveMethodProduct.controller.HuananSeaFogController;
import com.webyun.seagrid.objectiveMethodProduct.util.ObjectiveMethodProductUtil;
import com.webyun.seagrid.seaFogFeature.util.SeaFogFeatureUtil;

/**

 * @ClassName: SeaFogFeatureController

 * @Description: TODO

 * @author: wasabi

 * @date: 2019年1月10日 下午3:46:39

 */
@Controller
@RequestMapping("/seaFogFeature/section3days")
public class Section3DaysController {
	private Logger log = Logger.getLogger(Section3DaysController.class);
	
	@RequestMapping("/main.do")
	public String toMain() {
		return "/seaFogFeature/section3days";
	}
	
	
	@RequestMapping("/validImage.do")
	@ResponseBody
	public Boolean validImage(String datetime, String hour, String location, HttpServletResponse response) {
		try {
			// 获取对应日期时间的图片所在路径
			String path = SeaFogFeatureUtil.getSeaFogSection3DaysImagePath(datetime, hour, location);

			if (path != null) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	
	@RequestMapping("/showImage.do")
	public void showImage(String datetime, String hour, String location, HttpServletResponse response){
		response.setContentType("multipart/form-data");
		
		try {
			// 获取对应日期时间的图片所在路径
			String path = SeaFogFeatureUtil.getSeaFogSection3DaysImagePath(datetime, hour, location);
			
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
