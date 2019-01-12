/**  

 * Copyright © 2019webyun. All rights reserved.

 *

 * @Title: FeatureCombinationController.java

 * @Prject: seaGrid

 * @Package: com.webyun.seagrid.seaFogFeature.controller

 * @Description: TODO

 * @author: wasabi  

 * @date: 2019年1月11日 下午2:20:32

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
import com.webyun.seagrid.seaFogFeature.util.SeaFogFeatureUtil;

/**

 * @ClassName: FeatureCombinationController

 * @Description: TODO

 * @author: wasabi

 * @date: 2019年1月11日 下午2:20:32

/seaFogFeature/featureCombination/mian.do
 */
@Controller
@RequestMapping("/seaFogFeature/featureCombination")
public class FeatureCombinationController {
private Logger log = Logger.getLogger(FeatureCombinationController.class);
	
	@RequestMapping("/main.do")
	public String toMain() {
		return "/seaFogFeature/featureCombination";
	}
	
	@RequestMapping("/validImage.do")
	@ResponseBody
	public Boolean validImage(String datetime, String timelimit, String feature, HttpServletResponse response) {
		try {
			// 获取对应日期时间的图片所在路径
			String path = SeaFogFeatureUtil.getFeatureCombinationImagePath(datetime, timelimit, feature);

			if (path != null) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	
	@RequestMapping("/showImage.do")
	public void showImage(String datetime, String timelimit, String feature, HttpServletResponse response){
		response.setContentType("multipart/form-data");
		
		try {
			// 获取对应日期时间的图片所在路径
			String path = SeaFogFeatureUtil.getFeatureCombinationImagePath(datetime, timelimit, feature);
			
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
