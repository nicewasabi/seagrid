/**  

 * Copyright © 2019webyun. All rights reserved.

 *

 * @Title: HuananSeaFogController.java

 * @Prject: seaGrid

 * @Package: com.webyun.seagrid.objectiveMethodProduct.controller

 * @Description: TODO

 * @author: wasabi  

 * @date: 2019年1月10日 上午10:17:46

 * @version: V1.0  

 */
package com.webyun.seagrid.objectiveMethodProduct.controller;

import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.webyun.seagrid.common.util.FileOperationUtil;
import com.webyun.seagrid.objectiveMethodProduct.util.ObjectiveMethodProductUtil;
import com.webyun.seagrid.seaWindProbability.controller.NPController;
import com.webyun.seagrid.seaWindProbability.util.WindProbabilityUtil;

/**
 * 
 * @ClassName: HuananSeaFogController
 * 
 * @Description: TODO华南海雾
 * 
 * @author: wasabi
 * 
 * @date: 2019年1月10日 上午10:17:46
 * 
 * 
 */
@Controller
@RequestMapping("/objectiveMethodProduct/huananSeaFog")
public class HuananSeaFogController {

	private Logger log = Logger.getLogger(HuananSeaFogController.class);

	/**
	 * 
	 * 
	 * @Title: toDetermineForecastMain
	 * 
	 * @Description: TODO
	 * 
	 * @return
	 * 
	 * @return: String
	 */
	@RequestMapping("/main.do")
	public String toMain() {
		return "/objectiveMethodProduct/huananSeaFog";
	}
	
	
	
	
	
	@RequestMapping("/validImage.do")
	@ResponseBody
	public Boolean validImage(String datetime, String timelimit, String featuretype, HttpServletResponse response) {
		try {
			// 获取对应日期时间的图片所在路径
			String path = ObjectiveMethodProductUtil.getSeaFogImagePath(datetime, timelimit, featuretype);

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
			String path = ObjectiveMethodProductUtil.getSeaFogImagePath(datetime, timelimit, featuretype);
			
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
